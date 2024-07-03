package net.pettip.app.navi.screen.test

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import net.pettip.app.navi.componet.ToolTip
import net.pettip.app.navi.screen.map.NaverMapComponent

/**
 * @Project     : PetTip-Android
 * @FileName    : TestScreen
 * @Date        : 2024-06-13
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen
 * @see net.pettip.app.navi.screen.TestScreen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestBSMapScreen(
){

    /** */

    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState()
    )

    val scope = rememberCoroutineScope()

    var isExpanded by remember{ mutableStateOf(false) }
    val rotationIcon = animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "", animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessMedium))
    val navigationBarsHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    var pickArea by remember{ mutableStateOf<String?>(null) }

    BackHandler(
        enabled = sheetState.bottomSheetState.currentValue == SheetValue.Expanded
    ) {
        if (sheetState.bottomSheetState.currentValue == SheetValue.Expanded) {
            isExpanded = false
            scope.launch { sheetState.bottomSheetState.partialExpand() }
        }
    }

    BottomSheetScaffold(
        modifier = Modifier.navigationBarsPadding(),
        scaffoldState = sheetState,
        sheetPeekHeight = 60.dp+navigationBarsHeight,
        sheetSwipeEnabled = false,
        sheetContent =  {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .background(Color.White)
                    .navigationBarsPadding()
            ) {
                Box(modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                ){
                    if(isExpanded){
                        Text(
                            text = "내 동네 설정",
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }else{
                        Text(
                            text = "우리동네 구경도 하고\n나만의 맞춤지도 만들어요",
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "",
                        tint = Color.Black,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .clip(CircleShape)
                            .clickable {
                                if (sheetState.bottomSheetState.currentValue == SheetValue.PartiallyExpanded) {
                                    isExpanded = true
                                    scope.launch { sheetState.bottomSheetState.expand() }
                                } else {
                                    isExpanded = false
                                    scope.launch { sheetState.bottomSheetState.partialExpand() }
                                }
                            }
                            .graphicsLayer {
                                rotationZ = rotationIcon.value
                            }
                    )
                }

                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(screenWidthDp)
                ){
                    NaverMapComponent(
                        pickArea = {newValue -> pickArea = newValue},
                        modifier = Modifier
                    )
                }

                Spacer(modifier = Modifier.padding(top = 20.dp))

                AnimatedVisibility(
                    visible = !pickArea.isNullOrBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("현 위치 ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("\"${pickArea ?: ""}\"") // null 처리
                            }
                            append("을 내 동네로\n선택하시겠어요?")
                        },
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.padding(top = 20.dp))

                Row (
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ){
                    ToolTip(
                        content = { Text(text = "본문영역본문영역\n본문영역본문영역")}
                    )

                    Spacer(modifier = Modifier.padding(6.dp))

                    Text(
                        text = "내 동네로 선택시"
                        +"\n1.파매상품 등록할 경우"
                        +"\n선택된 동네 위치 인증후 등록 가능해요"
                        +"\n2.상품 구매할 경우"
                        +"\n선택된 동네위치로 상품조회가 가능해요",
                        color = Color.Black,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.padding(top = 20.dp))
            }// col
        },
        sheetDragHandle = { }
    ) {

    }
}