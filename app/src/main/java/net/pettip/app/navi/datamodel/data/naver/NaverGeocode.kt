package net.pettip.app.navi.datamodel.data.naver


import com.google.gson.annotations.SerializedName

data class NaverGeocode(
    @SerializedName("addresses")
    var addresses: List<Addresse?>?,
    @SerializedName("errorMessage")
    var errorMessage: String?,
    @SerializedName("meta")
    var meta: Meta?,
    @SerializedName("status")
    var status: String? // OK
) {
    data class Addresse(
        @SerializedName("addressElements")
        var addressElements: List<AddressElement?>?,
        @SerializedName("distance")
        var distance: Double?, // 20.925857741585514
        @SerializedName("englishAddress")
        var englishAddress: String?, // 6, Buljeong-ro, Bundang-gu, Seongnam-si, Gyeonggi-do, Republic of Korea
        @SerializedName("jibunAddress")
        var jibunAddress: String?, // 경기도 성남시 분당구 정자동 178-1 그린팩토리
        @SerializedName("roadAddress")
        var roadAddress: String?, // 경기도 성남시 분당구 불정로 6 그린팩토리
        @SerializedName("x")
        var x: String?, // 127.10522081658463
        @SerializedName("y")
        var y: String? // 37.35951219616309
    ) {
        data class AddressElement(
            @SerializedName("code")
            var code: String?,
            @SerializedName("longName")
            var longName: String?, // 13561
            @SerializedName("shortName")
            var shortName: String?,
            @SerializedName("types")
            var types: List<String?>?
        )
    }

    data class Meta(
        @SerializedName("count")
        var count: Int?, // 1
        @SerializedName("page")
        var page: Int?, // 1
        @SerializedName("totalCount")
        var totalCount: Int? // 1
    )
}