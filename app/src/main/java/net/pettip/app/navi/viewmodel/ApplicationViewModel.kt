package net.pettip.app.navi.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @Project     : PetTip-Android
 * @FileName    : ApplicationViewModel
 * @Date        : 2024-05-20
 * @author      : CareBiz
 * @description : net.pettip.app.navi.viewmodel
 * @see net.pettip.app.navi.viewmodel.ApplicationViewModel
 */
@HiltViewModel
class ApplicationViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    val mapView: MapView = MapView(context, NaverMapOptions())

    private var _naverMap: NaverMap? = null
    val naverMap: NaverMap?
        get() = _naverMap

    init {
        viewModelScope.launch {
            initializeMap()
        }
    }

    private suspend fun initializeMap() {
        _naverMap = mapView.awaitMap()
    }

    private suspend inline fun MapView.awaitMap(): NaverMap {
        return kotlinx.coroutines.suspendCancellableCoroutine { continuation ->
            getMapAsync {
                continuation.resume(it) { throwable -> throwable.printStackTrace() }
            }
        }
    }
}