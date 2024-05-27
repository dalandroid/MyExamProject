package net.pettip.app.navi.datamodel.data.login

import com.google.gson.annotations.SerializedName

/**
 * @Project     : PetTip-Android
 * @FileName    : LoginReq
 * @Date        : 2024-05-01
 * @author      : CareBiz
 * @description : net.pettip.app.navi.datamodel.login
 * @see net.pettip.app.navi.datamodel.login.LoginReq
 */
data class LoginReq(
    @SerializedName("appTypNm")
    var appTypNm: String?,
    @SerializedName("userID")
    var userID: String?,
    @SerializedName("userPW")
    var userPW: String?,
)