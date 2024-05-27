package net.pettip.app.navi.utils.function

import org.json.JSONObject

/**
 * @Project     : PetTip-Android
 * @FileName    : ErrorBodyParse
 * @Date        : 2024-05-01
 * @author      : CareBiz
 * @description : net.pettip.app.navi.utils.function
 * @see net.pettip.app.navi.utils.function.ErrorBodyParse
 */
fun errorBodyParse(errorBodyString:String?, param:String):String{
    if (!errorBodyString.isNullOrBlank()){
        val json = JSONObject(errorBodyString)
        val value = json.optString(param)

        return if (!value.isNullOrBlank()){
            value
        }else{
            "통신에 실패했습니다"
        }
    }

    return "통신에 실패했습니다"
}