package net.pettip.app.navi.screen.test

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * @Project     : PetTip-Android
 * @FileName    : TestPetRegScreen
 * @Date        : 2024-06-20
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.test
 * @see net.pettip.app.navi.screen.test.TestPetRegScreen
 */
@Composable
fun TestPetRegScreen(){

    val scope = rememberCoroutineScope()

    /** pageCount 를 조절해서 userScroll 을 통해 이전페이지로만 보내기 */
    var pageCount by remember{ mutableIntStateOf(1) }
    val pagerState = rememberPagerState (initialPage = 0, pageCount = {pageCount})

    /** 각 페이지 상태 관리 */
    var firstCheck by remember{ mutableStateOf(false) }
    var secondCheck by remember{ mutableStateOf(false) }
    var thirdCheck by remember{ mutableStateOf(false) }

    /** result 에 따라 버튼 활성/비활성화 */
    var result by remember { mutableStateOf(false) }

    LaunchedEffect(firstCheck,secondCheck,thirdCheck,pagerState.currentPage) {
        result = checkPagerState(pagerState = pagerState, firstCheck = firstCheck, secondCheck = secondCheck, thirdCheck = thirdCheck)
        Log.d("LOG",result.toString())
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()
    ){
        Text(text = "펫 등록", modifier = Modifier.padding(top = 8.dp, start = 20.dp, bottom = 20.dp))

        VerticalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {page: Int ->
            when(page){
                0 -> PetStep1(onCheck = {newValue -> firstCheck = newValue})
                1 -> PetStep2(onCheck = {newValue -> secondCheck = newValue})
                2 -> PetStep3(pagerState)
            }
        }

        Button(
            onClick = {
                if (pagerState.currentPage==0){
                    pageCount = 2
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                }else if (pagerState.currentPage==1){
                    pageCount = 3
                    scope.launch {
                        pagerState.animateScrollToPage(2)
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                disabledContainerColor = Color.LightGray
            ),
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .fillMaxWidth(),
            enabled = result
        ) {
            Text(
                text = if (pagerState.currentPage == 2) "등록완료하기" else "다음 가기",
                color = Color.White
            )
        }
    }
}

@Composable
fun PetStep1(onCheck:(Boolean)->Unit) {

    var text by remember { mutableStateOf("") }

    LaunchedEffect (text){
        if (!text.isNullOrBlank()){
            onCheck(true)
            Log.d("LOG","FIRST:true")
        }else{
            onCheck(false)
            Log.d("LOG","FIRST:false")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "1단계 - 펫구분/품종선택", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun PetStep2(onCheck:(Boolean)->Unit) {

    var name by remember { mutableStateOf("") }

    LaunchedEffect (name){
        if (!name.isNullOrBlank()){
            onCheck(true)
            Log.d("LOG","FIRST:true")
        }else{
            onCheck(false)
            Log.d("LOG","FIRST:false")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "1단계. 성별, 이름, 프로필", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { /* 남아 선택 */ }) { Text("남아") }
                Button(onClick = { /* 여아 선택 */ }) { Text("여아") }
                Button(onClick = { /* 모름 선택 */ }) { Text("모름") }
            }
            Spacer(modifier = Modifier.height(8.dp))
            BasicTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun PetStep3(pagerState: PagerState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "2단계. 생일, 몸무게, 중성화", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            BasicTextField(
                value = TextFieldValue(""),
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            BasicTextField(
                value = TextFieldValue(""),
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            BasicTextField(
                value = TextFieldValue(""),
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { /* 중성화 유 선택 */ }) { Text("중성화 O") }
                Button(onClick = { /* 중성화 무 선택 */ }) { Text("중성화 X") }
                Button(onClick = { /* 중성화 모름 선택 */ }) { Text("모름") }
            }
            Spacer(modifier = Modifier.height(8.dp))
            BasicTextField(
                value = TextFieldValue(""),
                onValueChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(16.dp)
            )
        }
    }
}


fun checkPagerState(
    pagerState: PagerState,
    firstCheck: Boolean,
    secondCheck: Boolean,
    thirdCheck: Boolean
): Boolean {
    return when (pagerState.currentPage) {
        0 -> firstCheck
        1 -> secondCheck
        2 -> thirdCheck
        else -> false
    }
}