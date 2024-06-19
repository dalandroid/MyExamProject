package net.pettip.app.navi.datamodel.data.test

import com.google.gson.annotations.SerializedName

/**
 * @Project     : PetTip-Android
 * @FileName    : RTStoryListRes
 * @Date        : 2024-06-14
 * @author      : CareBiz
 * @description : net.pettip.app.navi.datamodel.data.test
 * @see net.pettip.app.navi.datamodel.data.test.RTStoryListRes
 */
data class RTStoryListRes(
    @SerializedName("data")
    var data: List<RTStoryData>?,
    @SerializedName("detailMessage")
    var detailMessage: String?, // null
    @SerializedName("resultMessage")
    var resultMessage: String?, // 실시간 스토리 조회 완료
    @SerializedName("statusCode")
    var statusCode: Int? // 200
)

data class RTStoryData(
    @SerializedName("cmntCnt")
    var cmntCnt: String?, // 0
    @SerializedName("petNm")
    var petNm: String?, // 코코
    @SerializedName("rcmdtnCnt")
    var rcmdtnCnt: String?, // 0
    @SerializedName("schTtl")
    var schTtl: String?, // 코코 산책
    @SerializedName("schUnqNo")
    var schUnqNo: Int?, // 173
    @SerializedName("storyFile")
    var storyFile: String?, //
    @SerializedName("schSeNmList")
    var schSeNmList: List<SchSeNm>?, //
)

data class SchSeNm(
    @SerializedName("cdId")
    var cdId: String?, // 002
    @SerializedName("cdNm")
    var cdNm: String?, // 일상
    @SerializedName("upCdId")
    var upCdId: String? // SCH
)