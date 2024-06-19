package net.pettip.app.navi.screen

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import net.pettip.app.navi.screen.login.IntroScreen
import net.pettip.app.navi.screen.login.LoginScreen
import net.pettip.app.navi.screen.mainscreen.MainScreen
import net.pettip.app.navi.screen.map.NaverMapScreen
import net.pettip.app.navi.screen.test.TestBSMapScreen
import net.pettip.app.navi.screen.test.TestBubbleScreen
import net.pettip.app.navi.screen.test.TestLazyVerticalGrid
import net.pettip.app.navi.screen.test.TestPagerScreen
import net.pettip.app.navi.screen.test.TestShareScreen

/**
 * @Project     : PetTip-Android
 * @FileName    : MyApp
 * @Date        : 2024-04-30
 * @author      : CareBiz
 * @description : net.pettip.app.navi
 * @see net.pettip.app.navi.MyApp
 */
@Composable
fun MyApp(modifier: Modifier = Modifier){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "intro",
        modifier = modifier.background(MaterialTheme.colorScheme.primary)
    ){
        composable(
            route = "intro",
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "https://pettip.net"
                    action = Intent.ACTION_VIEW
                }
            )
        ) {
            IntroScreen(
                modifier= modifier,
                onLogin = {
                    navController.navigate(Screen.MainScreen.route){
                        popUpTo(Screen.Intro.route){inclusive = true}
                    } },
                unLogin = {
                    navController.navigate(Screen.LoginScreen.route){
                        popUpTo(Screen.Intro.route){inclusive = true}
                    } }
            )
        }
        
        composable("mainScreen"){
            MainScreen(navController = navController)
        }

        composable("loginScreen"){
            LoginScreen(navController = navController)
        }
        composable("naverMapScreen"){
            NaverMapScreen()
        }

        /** Test 용 */
        composable("testBSMapScreen"){
            TestBSMapScreen()
        }
        composable("testPagerScreen"){
            TestPagerScreen()
        }
        composable("testLazyVerticalGrid"){
            TestLazyVerticalGrid()
        }
        composable("testBubbleScreen"){
            TestBubbleScreen()
        }
        composable("testShareScreen"){
            TestShareScreen()
        }
        /** Test 용 */
    }
}

sealed class Screen(val route: String) {
    data object Intro : Screen("intro")
    data object MainScreen : Screen("mainScreen")
    data object LoginScreen : Screen("loginScreen")
    data object NaverMapScreen : Screen("naverMapScreen")




    data object TestBSMapScreen : Screen("testBSMapScreen")
    data object TestPagerScreen : Screen("testPagerScreen")
    data object TestLazyVerticalGrid : Screen("testLazyVerticalGrid")
    data object TestBubbleScreen : Screen("testBubbleScreen")
    data object TestShareScreen : Screen("testShareScreen")
}