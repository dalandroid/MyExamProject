package net.pettip.app.navi.datamodel.data.etc


import com.google.gson.annotations.SerializedName

data class UserAreaRes(
    @SerializedName("errCd")
    var errCd: Int?, // 0
    @SerializedName("errMsg")
    var errMsg: String?, // Success
    @SerializedName("features")
    var features: List<Feature?>?,
    @SerializedName("id")
    var id: String?, // API_0706
    @SerializedName("trId")
    var trId: String?, // 6fgW_API_0706_1716274114176
    @SerializedName("type")
    var type: String? // FeatureCollection
) {
    data class Feature(
        @SerializedName("geometry")
        var geometry: Geometry?,
        @SerializedName("properties")
        var properties: Properties?,
        @SerializedName("type")
        var type: String? // Feature
    ) {
        data class Geometry(
            @SerializedName("coordinates")
            var coordinates: List<List<List<Double?>?>?>?,
            @SerializedName("type")
            var type: String? // Polygon
        )

        data class Properties(
            @SerializedName("adm_cd")
            var admCd: String?, // 11040730010002
            @SerializedName("adm_nm")
            var admNm: String?, // 11040730010002
            @SerializedName("x")
            var x: String?, // 956759.76119052421
            @SerializedName("y")
            var y: String? // 1949605.2524
        )
    }
}