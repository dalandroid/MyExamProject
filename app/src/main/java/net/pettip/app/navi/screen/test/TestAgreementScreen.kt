package net.pettip.app.navi.screen.test

import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.pettip.app.navi.componet.CustomBottomSheet

/**
 * @Project     : PetTip-Android
 * @FileName    : TestAgreementScreen
 * @Date        : 2024-06-20
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.test
 * @see net.pettip.app.navi.screen.test.TestAgreementScreen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestAgreementScreen(){
    
    val scope = rememberCoroutineScope()
    
    var showBottomSheet by remember{ mutableStateOf(false) }

    val pagerState = rememberPagerState (
        pageCount = {3}
    )

    val examTextList = listOf(
        "제 1 장 총칙\n" +
                "제 1 조 (목적)\n" +
                "본 약관은 (주)제이피 이노베이션(이하 “회사”라 합니다)이 운영하는 웹사이트 ‘어반런드렛’ (www.urbanlaunderette.com) (이하 “웹사이트”라 합니다)에서 제공하는 온라인 서비스(이하 “서비스”라 한다)를 이용함에 있어 사이버몰과 이용자의 권리, 의무 및 책임사항을 규정함을 목적으로 합니다.\n" +
                "\n" +
                "제 2 조 (용어의 정의)\n" +
                "본 약관에서 사용하는 용어는 다음과 같이 정의한다.\n" +
                "\n" +
                "“웹사이트”란 회사가 재화 또는 용역을 이용자에게 제공하기 위하여 컴퓨터 등 정보통신설비를 이용하여 재화 또는 용역을 거래 할 수 있도록 설정한 가상의 영업장을 말하며, 아울러 사이버몰을 운영하는 사업자의 의미로도 사용합니다.\n" +
                "“이용자”란 “웹사이트”에 접속하여 서비스를 이용하는 회원 및 비회원을 말합니다.\n" +
                "“회원”이라 함은 “웹사이트”에 개인정보를 제공하여 회원등록을 한 자로서, “웹사이트”의 정보를 지속적으로 제공받으며, “웹사이트”이 제공하는 서비스를 계속적으로 이용할 수 있는 자를 말합니다.\n" +
                "“비회원”이라 함은 회원에 가입하지 않고, “웹사이트”이 제공하는 서비스를 이용하는 자를 말합니다.\n" +
                "“ID”라 함은 이용자가 회원가입당시 등록한 사용자 “개인이용문자”를 말합니다.\n" +
                "“멤버십”이라 함은 회원등록을 한 자로서, 별도의 온/오프라인 상에서 추가 서비스를 제공 받을 수 있는 회원을 말합니다.\n" +
                "제 3 조 (약관의 공시 및 효력과 변경)\n" +
                "본 약관은 회원가입 화면에 게시하여 공시하며 회사는 사정변경 및 영업상 중요한 사유가 있을 경우 약관을 변경할 수 있으며 변경된 약관은 공지사항을 통해 공시한다\n" +
                "본 약관 및 차후 회사사정에 따라 변경된 약관은 이용자에게 공시함으로써 효력을 발생한다.\n" +
                "제 4 조 (약관 외 준칙)\n" +
                "본 약관에 명시되지 않은 사항이 전기통신기본법, 전기통신사업법, 정보통신촉진법, ‘전자상거래등에서의 소비자 보호에 관한 법률’, ‘약관의 규제에관한법률’, ‘전자거래기본법’, ‘전자서명법’, ‘정보통신망 이용촉진등에 관한 법률’, ‘소비자보호법’ 등 기타 관계 법령에 규정되어 있을 경우에는 그 규정을 따르도록 한다.",
        "제 1 장 총칙\n" +
                "제 1 조 (목적)\n" +
                "본 약관은 (주)제이피 이노베이션(이하 “회사”라 합니다)이 운영하는 웹사이트 ‘어반런드렛’ (www.urbanlaunderette.com) (이하 “웹사이트”라 합니다)에서 제공하는 온라인 서비스(이하 “서비스”라 한다)를 이용함에 있어 사이버몰과 이용자의 권리, 의무 및 책임사항을 규정함을 목적으로 합니다.\n" +
                "\n" +
                "제 2 조 (용어의 정의)\n" +
                "본 약관에서 사용하는 용어는 다음과 같이 정의한다.\n" +
                "\n" +
                "“웹사이트”란 회사가 재화 또는 용역을 이용자에게 제공하기 위하여 컴퓨터 등 정보통신설비를 이용하여 재화 또는 용역을 거래 할 수 있도록 설정한 가상의 영업장을 말하며, 아울러 사이버몰을 운영하는 사업자의 의미로도 사용합니다.\n" +
                "“이용자”란 “웹사이트”에 접속하여 서비스를 이용하는 회원 및 비회원을 말합니다.\n" +
                "“회원”이라 함은 “웹사이트”에 개인정보를 제공하여 회원등록을 한 자로서, “웹사이트”의 정보를 지속적으로 제공받으며, “웹사이트”이 제공하는 서비스를 계속적으로 이용할 수 있는 자를 말합니다.\n" +
                "“비회원”이라 함은 회원에 가입하지 않고, “웹사이트”이 제공하는 서비스를 이용하는 자를 말합니다.\n" +
                "“ID”라 함은 이용자가 회원가입당시 등록한 사용자 “개인이용문자”를 말합니다.\n" +
                "“멤버십”이라 함은 회원등록을 한 자로서, 별도의 온/오프라인 상에서 추가 서비스를 제공 받을 수 있는 회원을 말합니다.\n" +
                "제 3 조 (약관의 공시 및 효력과 변경)\n" +
                "본 약관은 회원가입 화면에 게시하여 공시하며 회사는 사정변경 및 영업상 중요한 사유가 있을 경우 약관을 변경할 수 있으며 변경된 약관은 공지사항을 통해 공시한다\n" +
                "본 약관 및 차후 회사사정에 따라 변경된 약관은 이용자에게 공시함으로써 효력을 발생한다.\n" +
                "제 4 조 (약관 외 준칙)\n" +
                "본 약관에 명시되지 않은 사항이 전기통신기본법, 전기통신사업법, 정보통신촉진법, ‘전자상거래등에서의 소비자 보호에 관한 법률’, ‘약관의 규제에관한법률’, ‘전자거래기본법’, ‘전자서명법’, ‘정보통신망 이용촉진등에 관한 법률’, ‘소비자보호법’ 등 기타 관계 법령에 규정되어 있을 경우에는 그 규정을 따르도록 한다.",
        "제 1 장 총칙\n" +
                "제 1 조 (목적)\n" +
                "본 약관은 (주)제이피 이노베이션(이하 “회사”라 합니다)이 운영하는 웹사이트 ‘어반런드렛’ (www.urbanlaunderette.com) (이하 “웹사이트”라 합니다)에서 제공하는 온라인 서비스(이하 “서비스”라 한다)를 이용함에 있어 사이버몰과 이용자의 권리, 의무 및 책임사항을 규정함을 목적으로 합니다.\n" +
                "\n" +
                "제 2 조 (용어의 정의)\n" +
                "본 약관에서 사용하는 용어는 다음과 같이 정의한다.\n" +
                "\n" +
                "“웹사이트”란 회사가 재화 또는 용역을 이용자에게 제공하기 위하여 컴퓨터 등 정보통신설비를 이용하여 재화 또는 용역을 거래 할 수 있도록 설정한 가상의 영업장을 말하며, 아울러 사이버몰을 운영하는 사업자의 의미로도 사용합니다.\n" +
                "“이용자”란 “웹사이트”에 접속하여 서비스를 이용하는 회원 및 비회원을 말합니다.\n" +
                "“회원”이라 함은 “웹사이트”에 개인정보를 제공하여 회원등록을 한 자로서, “웹사이트”의 정보를 지속적으로 제공받으며, “웹사이트”이 제공하는 서비스를 계속적으로 이용할 수 있는 자를 말합니다.\n" +
                "“비회원”이라 함은 회원에 가입하지 않고, “웹사이트”이 제공하는 서비스를 이용하는 자를 말합니다.\n" +
                "“ID”라 함은 이용자가 회원가입당시 등록한 사용자 “개인이용문자”를 말합니다.\n" +
                "“멤버십”이라 함은 회원등록을 한 자로서, 별도의 온/오프라인 상에서 추가 서비스를 제공 받을 수 있는 회원을 말합니다.\n" +
                "제 3 조 (약관의 공시 및 효력과 변경)\n" +
                "본 약관은 회원가입 화면에 게시하여 공시하며 회사는 사정변경 및 영업상 중요한 사유가 있을 경우 약관을 변경할 수 있으며 변경된 약관은 공지사항을 통해 공시한다\n" +
                "본 약관 및 차후 회사사정에 따라 변경된 약관은 이용자에게 공시함으로써 효력을 발생한다.\n" +
                "제 4 조 (약관 외 준칙)\n" +
                "본 약관에 명시되지 않은 사항이 전기통신기본법, 전기통신사업법, 정보통신촉진법, ‘전자상거래등에서의 소비자 보호에 관한 법률’, ‘약관의 규제에관한법률’, ‘전자거래기본법’, ‘전자서명법’, ‘정보통신망 이용촉진등에 관한 법률’, ‘소비자보호법’ 등 기타 관계 법령에 규정되어 있을 경우에는 그 규정을 따르도록 한다.",

    )

    
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Button(
            onClick = { showBottomSheet = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text(text = "BSsheet")
        }
    }

    CustomBottomSheet(
        onDismiss = { showBottomSheet = false }, 
        showBottomSheet = showBottomSheet,
        maxHeight = 0.9f,
        draggable = true
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ){
            Row (
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "약관동의")

                IconButton(
                    onClick = { showBottomSheet = false }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = ""
                    )
                }
            }

            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ){
                item {
                    HorizontalPager(
                        state = pagerState,
                        userScrollEnabled = false,
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        pageSpacing = 20.dp
                    ) {page ->
                        Text(text = examTextList[page])
                    }
                }
            }

            Row (
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Button(
                    onClick = { scope.launch { pagerState.animateScrollToPage(page = pagerState.currentPage-1, animationSpec = tween(500)) } },
                    modifier = Modifier
                        .weight(0.3f)
                        .wrapContentHeight(),
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "동의안함",
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.padding(horizontal = 6.dp))

                Button(
                    onClick = { scope.launch { pagerState.animateScrollToPage(page = pagerState.currentPage+1, animationSpec = tween(500)) } },
                    modifier = Modifier
                        .weight(0.7f)
                        .wrapContentHeight(),
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(Color.Black)
                ) {
                    Text(
                        text = "동의함",
                        modifier = Modifier
                            .padding(vertical = 12.dp),
                        color = Color.White
                    )
                }
            }
        }//col
    }
    
}