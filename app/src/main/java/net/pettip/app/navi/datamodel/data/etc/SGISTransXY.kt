package net.pettip.app.navi.datamodel.data.etc


import com.google.gson.annotations.SerializedName

data class SGISTransXY(
    @SerializedName("errCd")
    var errCd: Int?, // 0
    @SerializedName("errMsg")
    var errMsg: String?, // Success
    @SerializedName("id")
    var id: String?, // API_0201
    @SerializedName("result")
    var result: Result?,
    @SerializedName("trId")
    var trId: String? // HP=N_API_0201_1716273446178
) {
    data class Result(
        @SerializedName("posX")
        var posX: Double?, // 961828.5738445225
        @SerializedName("posY")
        var posY: Double? // 1949658.7773286032
    )
}