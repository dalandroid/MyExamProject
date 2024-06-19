package net.pettip.app.navi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(

    /** 대타이틀 : 왼쪽정렬, 14pt, Bold, 맑은고딕 */
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 14.sp,
        letterSpacing = -(0.7).sp
    ),

    /** 중타이틀 : 왼쪽정렬, 12pt, Bold, 맑은고딕 */
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = -(0.6).sp
    ),

    /** 소타이틀 : 왼쪽정렬, 10pt, Bold, 맑은고딕 */
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 10.sp,
        letterSpacing = -(0.5).sp
    ),


    /** 본문 기본 : 왼쪽정렬, 8pt, 맑은고딕 */
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 8.sp,
        lineHeight = 10.sp,
        letterSpacing = -(0.5).sp
    ),

    /** 강조 긍정 : 8pt, Bold, 검정 */
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 8.sp,
        lineHeight = 8.sp,
        letterSpacing = -(0.4).sp,
        color = Color.Black
    ),

    /** 강조 주의 : 8pt, Bold, 빨강 */
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 8.sp,
        lineHeight = 8.sp,
        letterSpacing = -(0.4).sp,
        color = Color.Red
    ),

    /** 링크 텍스트 : 8pt, Bold, 파랑, 밑줄 */
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 8.sp,
        lineHeight = 8.sp,
        letterSpacing = -(0.4).sp,
        textDecoration = TextDecoration.Underline,
        color = Color.Blue
    )

)