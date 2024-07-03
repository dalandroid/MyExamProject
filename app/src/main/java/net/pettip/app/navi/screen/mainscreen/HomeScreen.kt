package net.pettip.app.navi.screen.mainscreen

import android.content.Intent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import net.pettip.app.navi.activity.CameraActivity
import net.pettip.app.navi.activity.MainActivity
import net.pettip.app.navi.componet.CustomDialog
import net.pettip.app.navi.componet.CustomPagerWithOffset
import net.pettip.app.navi.componet.CustomTextField
import net.pettip.app.navi.componet.Linear
import net.pettip.app.navi.componet.modifier.bounceClick
import net.pettip.app.navi.componet.modifier.pressClickEffect
import net.pettip.app.navi.screen.Screen
import net.pettip.app.navi.utils.function.shimmerLoadingAnimation

/**
 * @Project     : PetTip-Android
 * @FileName    : WalkScreen
 * @Date        : 2024-04-30
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.mainscreen
 * @see net.pettip.app.navi.screen.mainscreen.WalkScreen
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    navController: NavHostController
) {

    val scrollState = rememberScrollState()

    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .verticalScroll(scrollState)
        .imePadding()
    ){
        Column (
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row (
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ){
                Button(
                    onClick = { navController.navigate(Screen.TestBSMapScreen.route) },
                    modifier = Modifier.pressClickEffect()
                ) {
                    Text(text = "BS Map")
                }

                Button(
                    onClick = { navController.navigate(Screen.TestPagerScreen.route) },
                    modifier = Modifier.pressClickEffect()
                ) {
                    Text(text = "Pager")
                }

                Button(
                    onClick = {
                        val intent = Intent(context,CameraActivity::class.java)
                        context.startActivity(intent) },
                    modifier = Modifier.pressClickEffect()
                ) {
                    Text(text = "Camera")
                }
            }

            Row (
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ){
                Button(
                    onClick = { navController.navigate(Screen.TestLazyVerticalGrid.route) },
                    modifier = Modifier.pressClickEffect()
                ) {
                    Text(text = "Lazy Vertical Grid")
                }

                Button(
                    onClick = { navController.navigate(Screen.TestBubbleScreen.route) },
                    modifier = Modifier.pressClickEffect()
                ) {
                    Text(text = "Bubble Screen")
                }
            }

            Row (
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ){
                Button(
                    onClick = { navController.navigate(Screen.TestShareScreen.route) },
                    modifier = Modifier.pressClickEffect()
                ) {
                    Text(text = "share")
                }

                Button(
                    onClick = { navController.navigate(Screen.TestAgreementScreen.route) },
                    modifier = Modifier.pressClickEffect()
                ) {
                    Text(text = "Agreement")
                }

                Button(
                    onClick = { navController.navigate(Screen.TestPetRegScreen.route) },
                    modifier = Modifier.pressClickEffect()
                ) {
                    Text(text = "Pet Reg")
                }
            }

            Row (
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ){
                Button(
                    onClick = { navController.navigate(Screen.LoginScreen.route) },
                    modifier = Modifier.pressClickEffect()
                ) {
                    Text(text = "login")
                }

                Button(
                    onClick = { navController.navigate(Screen.TestWalkScreen.route) },
                    modifier = Modifier.pressClickEffect()
                ) {
                    Text(text = "walk")
                }

                Button(
                    onClick = { },
                    modifier = Modifier.pressClickEffect()
                ) {
                    Text(text = "")
                }
            }
        }
    }
}


