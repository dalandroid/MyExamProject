package net.pettip.app.navi.componet

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import net.pettip.app.navi.R
import net.pettip.app.navi.utils.singleton.MySharedPreference

/**
 * @Project     : PetTip-Android
 * @FileName    : NavigationDrawer
 * @Date        : 2024-05-01
 * @author      : CareBiz
 * @description : net.pettip.app.navi.componet
 * @see net.pettip.app.navi.componet.NavigationDrawer
 */
@Composable
fun NavigationDrawer(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    var isLogin by remember{ mutableStateOf(MySharedPreference.getIsLogin()) }
    var lazyState = rememberLazyListState()

    val firstList = listOf("펫 등록 / 관리")
    val secondList = listOf("구매관리","판매관리","우리동네 맞춤지도","채팅")
    val thirdList = listOf("초대관리")
    val fourthList = listOf("1:1문의", "제휴상담문의")
    val fifthList = listOf("공지사항", "FAQ", "이벤트")
    val sixthList = listOf("설정")

    val bottomList = listOf("산책","펫팁몰","홈","My")

    LaunchedEffect (isLogin){
        if (isLogin){
            MySharedPreference.setIsLogin(true)
            Log.d("LOG","true")
        }else{
            MySharedPreference.setIsLogin(false)
            Log.d("LOG","false")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        /** Header 영역 */
        Box (
            modifier = modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color.Gray)
        ){
            if (isLogin){
                HeaderLoginComponent(
                    onClick = {isLogin = false}
                )
            }else{
                HeaderLogoutComponent(
                    onClick = {isLogin = true}
                )
            }
        }

        /** Body 영역 */
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyState
        ){
            items(firstList) {
                Text(text = it)
            }

            item{
                DottedDivider()
            }

            items(secondList){
                Text(text = it)
            }
            items(secondList){
                Text(text = it)
            }
            items(secondList){
                Text(text = it)
            }
            items(secondList){
                Text(text = it)
            }

            item{
                DottedDivider()
            }

            items(thirdList){
                Text(text = it)
            }

            item {
                DottedDivider()
            }

            items(fourthList){
                Text(text = it)
            }

            item {
                DottedDivider()
            }

            items(fifthList){
                Text(text = it)
            }

            item {
                DottedDivider()
            }

            items(sixthList){
                Text(text = it)
            }
        }

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
            Row (
                modifier = Modifier
                    .padding(bottom = 20.dp, top = 20.dp)
                    .wrapContentWidth()
                    .height(IntrinsicSize.Min)
                    .border(1.dp, Color.Gray)
                    .shadow(4.dp, ambientColor = Color.LightGray)
                    .background(Color.White),
                horizontalArrangement = Arrangement.Center
            ){
                repeat(bottomList.size){
                    Box(modifier = Modifier
                        .wrapContentSize()
                        .clickable {  },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = bottomList[it],
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                        )
                    }


                    if (it != 3){
                        Spacer(modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .background(Color.Gray))
                    }
                }
            }
        }
    }
}

data class HeaderData(val icon: Int, val title: String, val onClick:()->Unit)


@Composable
fun HeaderLoginComponent(
    onClick: () -> Unit
){
    Column (
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "닉네임",
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                fontSize = 16.sp,
                lineHeight = 16.sp
            )

            Box(modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White)
                .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                .clickable { onClick() },
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "LogOut",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 10.sp,
                    lineHeight = 10.sp
                )
            }
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
        ){
            val headerList = listOf(
                HeaderData(icon = R.drawable.icon_kakao, title = "1번째", onClick = {}),
                HeaderData(icon = R.drawable.icon_google, title = "2번째", onClick = {}),
                HeaderData(icon = R.drawable.icon_naver, title = "3번째", onClick = {}),
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ){
                repeat(3){index ->
                    HeaderItem(item = headerList[index] , modifier = Modifier.weight(1f))

                    if (index != 2) {
                        Spacer(modifier = Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(Color.Black))
                    }
                }
            }
        }
    }// col
}

@Composable
fun HeaderLogoutComponent(
    onClick: () -> Unit
){

    val text = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 16.sp, color = Color.Black)) {
            append("다양한 펫팁 서비스는\n")
        }
        withStyle(style = SpanStyle(fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Bold)) {
            append("로그인 후 사용가능해요")
        }
    }

    Column (
        modifier = Modifier
            .padding(vertical = 12.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = text,
            textAlign = TextAlign.Center
        )

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Box(modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White)
                .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                .clickable { onClick() },
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "로그인",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 10.sp,
                    lineHeight = 10.sp
                )
            }

            Box(modifier = Modifier
                .wrapContentSize()
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White)
                .border(1.dp, Color.Black, RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "회원가입",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 10.sp,
                    lineHeight = 10.sp
                )
            }
        }
    }// col
}




@Composable
fun HeaderItem(
    item: HeaderData,
    modifier: Modifier
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = item.icon),
            contentDescription = ""
        )
        
        Spacer(modifier = Modifier.padding(top = 8.dp))
        
        Text(text = item.title)
    }
}

@Composable
fun DottedDivider(
    color: Color = Color.Gray,
    thickness: Float = 1f,
    interval: Float = 4f
) {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(thickness.dp)
    ) {
        drawLine(
            color = color,
            start = androidx.compose.ui.geometry.Offset(0f, 0f),
            end = androidx.compose.ui.geometry.Offset(size.width, 0f),
            strokeWidth = thickness,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(interval, interval), 0f)
        )
    }
}
