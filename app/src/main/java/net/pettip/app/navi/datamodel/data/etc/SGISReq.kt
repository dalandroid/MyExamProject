package net.pettip.app.navi.datamodel.data.etc

import com.google.gson.annotations.SerializedName

/**
 * @Project     : PetTip-Android
 * @FileName    : SGISReq
 * @Date        : 2024-05-21
 * @author      : CareBiz
 * @description : net.pettip.app.navi.datamodel.data.etc
 * @see net.pettip.app.navi.datamodel.data.etc.SGISReq
 */
data class SGISReq(
    @SerializedName("consumer_key")
    var consumerKey: String?,
    @SerializedName("consumer_secret")
    var consumerSecret: String?
)

data class SGISRes(
    @SerializedName("errCd")
    var errCd: Int?, // 0
    @SerializedName("errMsg")
    var errMsg: String?, // Success
    @SerializedName("id")
    var id: String?, // API_0101
    @SerializedName("result")
    var result: Result?,
    @SerializedName("trId")
    var trId: String? // SIFh_API_0101_1716269163248
) {
    data class Result(
        @SerializedName("accessTimeout")
        var accessTimeout: String?, // 1716283563345
        @SerializedName("accessToken")
        var accessToken: String? // c88faffa-0418-4891-b1e8-3a4d9a5aa648
    )
}