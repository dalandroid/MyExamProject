package net.pettip.app.navi.viewmodel.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.pettip.app.navi.datamodel.data.etc.SGISRes
import net.pettip.app.navi.datamodel.data.etc.SGISTransXY
import net.pettip.app.navi.datamodel.data.etc.UserAreaRes
import net.pettip.app.navi.datamodel.data.naver.NaverAddress
import net.pettip.app.navi.datamodel.data.naver.NaverGeocode
import net.pettip.app.navi.utils.service.ExternalApiService
import org.locationtech.proj4j.CRSFactory
import org.locationtech.proj4j.CoordinateTransformFactory
import org.locationtech.proj4j.ProjCoordinate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

/**
 * @Project     : PetTip-Android
 * @FileName    : MapViewModel
 * @Date        : 2024-05-20
 * @author      : CareBiz
 * @description : net.pettip.app.navi.viewmodel.map
 * @see net.pettip.app.navi.viewmodel.map.MapViewModel
 */
sealed class AddressResult {
    data object Loading : AddressResult()
    data class Success(val legalCode: String?, val admCode: String?, val roadAddr: String?) : AddressResult()
    data class Failure(val message: String) : AddressResult()
}

sealed class GeocodeResult {
    data object Loading : GeocodeResult()
    data class Success(val geocode: List<String>) : GeocodeResult()
    data class Failure(val message: String) : GeocodeResult()
}

sealed class SearchResult {
    data object Loading : SearchResult()
    data class Success(val region: List<String>) : SearchResult()
    data class Failure(val message: String) : SearchResult()
}

@HiltViewModel
class MapViewModel @Inject constructor(
    private val apiService: ExternalApiService
) : ViewModel() {

    init {
        viewModelScope.launch { getAuthenticationSGIS() }
    }

    private val _addressResult = MutableStateFlow<AddressResult>(AddressResult.Loading)
    val addressResult: StateFlow<AddressResult> = _addressResult

    private val _geocodeResult = MutableStateFlow<GeocodeResult>(GeocodeResult.Loading)
    val geocodeResult: StateFlow<GeocodeResult> = _geocodeResult

    private val _searchResult = MutableStateFlow<SearchResult>(SearchResult.Loading)
    val searchResult: StateFlow<SearchResult> = _searchResult

    private val _sgisRes = MutableStateFlow<SGISRes?>(null)
    val sgisRes: StateFlow<SGISRes?> = _sgisRes.asStateFlow()

    private val _sgisTransRes = MutableStateFlow<SGISTransXY?>(null)
    val sgisTransRes:StateFlow<SGISTransXY?> = _sgisTransRes.asStateFlow()

    private val _userArea = MutableStateFlow<UserAreaRes?>(null)
    val userArea:StateFlow<UserAreaRes?> = _userArea.asStateFlow()

    private val _admNmList = MutableStateFlow<List<String>>(emptyList())
    val admNmList:StateFlow<List<String>> = _admNmList.asStateFlow()

    private val _coordinateList = MutableStateFlow<List<List<LatLng>>>(emptyList())
    val coordinateList:StateFlow<List<List<LatLng>>> = _coordinateList.asStateFlow()
    fun resetCoordinateList(){
        _coordinateList.value = emptyList()
    }

    private val _address = MutableStateFlow<String?>(null)
    val address : StateFlow<String?> = _address.asStateFlow()
    fun updateAddress(address:String?){
        _address.value = address
    }

    private val _searchToAddress = MutableStateFlow<String?>(null)
    val searchToAddress : StateFlow<String?> = _searchToAddress.asStateFlow()
    fun updateSearchToAddress(address:String?){
        _searchToAddress.value = address
    }

    /** map 클릭시 마커 위치의 법정동, 행정동, 도로명 주소  */
    fun getAddress(latLng: LatLng) {
        _addressResult.value = AddressResult.Loading

        val orders = "legalcode,admcode,roadaddr"

        val call = apiService.getAddress(
            url = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?request=coordsToaddr&coords=${latLng.longitude},${latLng.latitude}&sourcecrs=epsg:4326&output=json&orders=${orders}",
            apiKeyId = "4wgixyse4n",
            apiKey = "BmOptnqafIFz4g19H3n9kA8nAFQDb0k2bm1HX3e5",
        )

        call.enqueue(object : Callback<NaverAddress> {
            override fun onResponse(call: Call<NaverAddress>, response: Response<NaverAddress>) {
                val body = response.body()
                val results = body?.results

                if (results.isNullOrEmpty()) {
                    _addressResult.value = AddressResult.Failure("No results found")
                    return
                }

                var legalCode: String? = null
                var admCode: String? = null
                var roadAddr: String? = null

                for (result in results) {
                    when (result?.name) {
                        "legalcode" -> legalCode = formatRegion(result.region)
                        "admcode" -> admCode = formatRegion(result.region)
                        "roadaddr" -> roadAddr = formatRoadAddress(result.region, result.land)
                    }
                }

                _addressResult.value = AddressResult.Success(legalCode, admCode, roadAddr)
            }

            override fun onFailure(call: Call<NaverAddress>, t: Throwable) {
                _addressResult.value = AddressResult.Failure(t.message ?: "Unknown error")
            }
        })
    }

    /** 주소 입력시 그 주소에 대한 값 받아오는 함수 */
    fun getGeocode(coordinate:LatLng?=null){

        _geocodeResult.value = GeocodeResult.Loading

        val call = apiService.getGeocode(
            query = _address.value?:"",
            coordinate = if (coordinate==null) null else "${coordinate.longitude},${coordinate.latitude}",
            apiKeyId = "4wgixyse4n",
            apiKey = "BmOptnqafIFz4g19H3n9kA8nAFQDb0k2bm1HX3e5"
        )

        call.enqueue(object : Callback<NaverGeocode>{
            override fun onResponse(call: Call<NaverGeocode>, response: Response<NaverGeocode>) {
                _geocodeResult.value = GeocodeResult.Success(arrayListOf("GeoCode"))
            }

            override fun onFailure(call: Call<NaverGeocode>, t: Throwable) {
                _geocodeResult.value = GeocodeResult.Failure(t.message ?:"")
            }

        })
    }

    fun searchToAddress(){
        _searchResult.value = SearchResult.Loading

        val call = apiService.getSearchToAddress(
            url = "https://openapi.naver.com/v1/search/local.json",
            query = _searchToAddress.value ?: "",
            display = 5,
            apiKeyId = "4wgixyse4n",
            apiKey = "BmOptnqafIFz4g19H3n9kA8nAFQDb0k2bm1HX3e5"
        )

        call.enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {

            }

            override fun onFailure(call: Call<String>, t: Throwable) {

            }

        })
    }


    /** SGIS access Token 을 받아오기 위한 함수 */
    suspend fun getAuthenticationSGIS() {

        val maxRetries = 3
        var attempt = 0

        withContext(Dispatchers.IO) {
            while (attempt < maxRetries) {
                try {
                    attempt++
                    val response = apiService.getAuthentication(
                        url = "https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json",
                        consumerKey = "faa74deb44e640498310",
                        consumerSecret = "e0a5f2f9331a4c96b68a"
                    )
                    if (response.isSuccessful) {
                        _sgisRes.value = response.body()

                        return@withContext
                    } else {
                        throw Exception("Unsuccessful response: ${response.code()} - ${response.message()}")
                    }
                } catch (e: Exception) {
                    if (attempt >= maxRetries) {
                        println("Failed after $maxRetries attempts: ${e.message}")
                        return@withContext
                    }
                    delay(1000) // 재시도 전 1초 대기
                }
            }
        }
    }

    /** 영역내 시/도, 시군구 검색 */
    suspend fun getUserArea(distance:Double, latLng: LatLng){

        // cd -> 1: 시도 , 2: 시군구, 3: 읍면동, 4: 집계구

        val utmkCoordinate = wGS84ToUTMK(latLng = latLng)

        val posX = utmkCoordinate.x
        val posY = utmkCoordinate.y
        val accessToken = _sgisRes.value?.result?.accessToken

        val minX = posX - distance
        val maxX = posX + distance
        val minY = posY - distance
        val maxY = posY + distance
        val cd = "3"

        withContext(Dispatchers.IO){
            try {
                val response = apiService.userArea(
                    url = "https://sgisapi.kostat.go.kr/OpenAPI3/boundary/userarea.geojson",
                    minx = minX.toString(),
                    maxx = maxX.toString(),
                    miny = minY.toString(),
                    maxy = maxY.toString(),
                    cd = cd,
                    accessToken = accessToken?:""
                )
                if (response.isSuccessful){
                    response.body()?.let { userAreaRes ->

                        _userArea.value = userAreaRes
                        processCoordinates(userAreaRes)

                        val propertiesList = extractProperties(userAreaRes)
                        val admNmList = propertiesList.map { it.admNm ?: "" }
                        _admNmList.value = admNmList
                    }
                    return@withContext
                } else {
                    throw Exception("Unsuccessful response: ${response.code()} - ${response.message()}")
                }
            }catch (e:Exception){
                println("Failed : ${e.message}")
            }
        }
    }

    private fun formatRegion(region: NaverAddress.Result.Region?): String {
        if (region == null) return ""

        return listOf(
            region.area1?.name,
            region.area2?.name,
            region.area3?.name,
            region.area4?.name
        ).filterNot { it.isNullOrBlank() }
            .joinToString(" ")
    }

    private fun formatRoadAddress(region: NaverAddress.Result.Region?, land: NaverAddress.Result.Land?): String {
        if (region == null || land == null) return ""

        val regionPart = listOf(region.area1?.name, region.area2?.name).filterNot { it.isNullOrBlank() }.joinToString(" ")
        val roadName = land.name
        val buildingNumber = listOfNotNull(land.number1, land.number2).filterNot { it.isBlank() }.joinToString("-")

        return listOf(regionPart, roadName, buildingNumber).filterNot { it.isNullOrBlank() }.joinToString(" ")
    }

    fun extractProperties(userAreaRes: UserAreaRes?): List<UserAreaRes.Feature.Properties> {
        val propertiesList = mutableListOf<UserAreaRes.Feature.Properties>()
        userAreaRes?.features?.forEach { feature ->
            feature?.properties?.let {
                propertiesList.add(it)
            }
        }
        return propertiesList
    }

    /** UTM-K로 된 Coordinate 를 받아와서 WGS84 리스트로 반환 */
    private fun processCoordinates(userAreaRes: UserAreaRes) {
        val coordinatesList = mutableListOf<List<LatLng>>()

        userAreaRes.features?.forEach { feature ->
            val geometry = feature?.geometry
            val coordinates = geometry?.coordinates

            val polygonList = mutableListOf<LatLng>()
            coordinates?.forEach { polygon ->
                polygon?.forEach { coord ->
                    coord?.let {
                        val utmX = it[0] ?: 0.0
                        val utmY = it[1] ?: 0.0
                        val wgs84Coord = utmkToWGS84(utmX, utmY)
                        polygonList.add(LatLng(wgs84Coord.y, wgs84Coord.x)) // (latitude, longitude)
                    }
                }
            }
            coordinatesList.add(polygonList)
        }

        _coordinateList.value = coordinatesList
    }

    /** UTM-K 좌표를 WGS84 좌표로 변환 */
    private fun utmkToWGS84(utmX: Double, utmY: Double): ProjCoordinate {
        val crsFactory = CRSFactory()

        val wgs84Proj = "+proj=longlat +datum=WGS84 +no_defs"
        val utmkProj = "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +units=m +no_defs"

        val wGS84 = crsFactory.createFromParameters("WGS84", wgs84Proj)
        val uTMK = crsFactory.createFromParameters("UTMK", utmkProj)

        val ctFactory = CoordinateTransformFactory()
        val utmkToWgs = ctFactory.createTransform(uTMK, wGS84)

        val srcCoordinate = ProjCoordinate(utmX, utmY)
        val result = ProjCoordinate()

        return utmkToWgs.transform(srcCoordinate, result)
    }

    /** WGS84 좌표를 UTM-K 좌표로 변환 */
    fun wGS84ToUTMK(latLng: LatLng):ProjCoordinate{

        val crsFactory = CRSFactory()

        val wgs84Proj = "+proj=longlat +datum=WGS84 +no_defs"
        val utmkProj = "+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +units=m +no_defs"

        val wGS84 = crsFactory.createFromParameters("WGS84",wgs84Proj)
        val uTMK = crsFactory.createFromParameters("UTMK",utmkProj)

        val ctFactory = CoordinateTransformFactory()

        val wgsToUtmk = ctFactory.createTransform(wGS84,uTMK)

        val srcCoordinate = ProjCoordinate(latLng.longitude, latLng.latitude)
        val result = ProjCoordinate()

        return wgsToUtmk.transform(srcCoordinate,result)
    }
}