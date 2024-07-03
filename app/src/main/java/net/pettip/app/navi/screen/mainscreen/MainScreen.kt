package net.pettip.app.navi.screen.mainscreen

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.ShapeCornerRadius
import kotlinx.coroutines.launch
import net.pettip.app.navi.R
import net.pettip.app.navi.componet.NavigationDrawer
import net.pettip.app.navi.screen.Screen
import net.pettip.app.navi.utils.singleton.MySharedPreference

/**
 * @Project     : PetTip-Android
 * @FileName    : MainScreen
 * @Date        : 2024-04-30
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen
 * @see net.pettip.app.navi.screen.MainScreen
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    val bottomNavController = rememberNavController()
    val scope = rememberCoroutineScope()

    val scaffoldState = rememberScaffoldState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    BackOnPressed()

    androidx.compose.material.Scaffold (
        backgroundColor = Color.Transparent,
        contentColor = Color.Transparent,
        modifier = Modifier
            .statusBarsPadding()
            .systemBarsPadding()
        ,
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(0.dp)
                    .heightIn(min = 0.dp, max = 45.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                scrollBehavior = scrollBehavior,
                title = {
                    Box(
                        modifier= modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth()
                    ){
                        Text(
                            text = "Pettip",
                            color = Color.Black,
                            fontSize = 20.sp,
                            modifier = modifier.align(Alignment.CenterStart)
                        )

                        Row (
                            modifier = modifier.align(Alignment.CenterEnd),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ){
                            Text(
                                text = "알림",
                                color = Color.Black,
                                fontSize = 20.sp
                            )

                            Text(
                                text = "메뉴",
                                color = Color.Black,
                                fontSize = 20.sp,
                                modifier = modifier.clickable {
                                    scope.launch {
                                        scaffoldState.drawerState.open()
                                    }
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationComponent(
                navController = bottomNavController
            )
        },
        drawerContent = {
            NavigationDrawer(navController = navController)
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
    ){innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ){
            NavHost(
                navController = bottomNavController,
                startDestination = BottomNav.HomeScreen.route,
                modifier = modifier
            ){
                composable(BottomNav.WalkScreen.route){
                    WalkScreen()
                }
                composable(BottomNav.TipStoryScreen.route){
                    TipStoryScreen(navController = navController)
                }
                composable(BottomNav.HomeScreen.route){
                    HomeScreen(
                        innerPadding = innerPadding,
                        navController = navController
                    )
                }
                composable(BottomNav.MallScreen.route){
                    MallScreen()
                }
                composable(BottomNav.MyScreen.route){
                    MyScreen(innerPadding = innerPadding)
                }
            }
        }

    }
}

@Composable
fun BottomNavigationComponent(
    modifier: Modifier = Modifier,
    navController: NavHostController
){
    val items = listOf(
        BottomNav.WalkScreen,
        BottomNav.TipStoryScreen,
        BottomNav.HomeScreen,
        BottomNav.MallScreen,
        BottomNav.MyScreen
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var selectedIndex by remember{ mutableStateOf(2) }

    currentDestination?.route.let { it ->
        when(it){
            BottomNav.WalkScreen.route -> {
                selectedIndex = 0
            }
            BottomNav.TipStoryScreen.route -> {
                selectedIndex = 1
            }
            BottomNav.HomeScreen.route -> {
                selectedIndex = 2
            }
            BottomNav.MallScreen.route -> {
                selectedIndex = 3
            }
            BottomNav.MyScreen.route -> {
                selectedIndex = 4
            }
        }
    }

    AnimatedNavigationBar(
        selectedIndex = selectedIndex,
        barColor = Color(0xFFF77D40),
        ballColor = Color.White,
        cornerRadius = ShapeCornerRadius(topLeft = 0f, topRight = 0f, bottomLeft = 0f, bottomRight = 0f),
        indentAnimation = Height(animationSpec = tween(300), indentWidth = 100.dp, indentHeight = 36.dp),
        ballAnimation = Parabolic(tween(300)),
        horizontalPadding = 30.dp,
        ballOffset = (-20).dp,
        bottomNavItem = items
    ) {
        items.forEach { bottomNav ->
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable {
                        navController.navigate(bottomNav.route) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Spacer(modifier = Modifier.padding(top = 12.dp))

                Icon(
                    imageVector = ImageVector.vectorResource(id = bottomNav.unSelectedIcon),
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = bottomNav.title,
                    color = Color.White,
                    modifier = Modifier
                        .padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.padding(top = 12.dp))
            }
        }
    }

    //BottomNavigation(
    //    backgroundColor = MaterialTheme.colorScheme.primary,
    //    elevation = 8.dp,
    //    modifier = modifier.height(60.dp)
    //){
    //    items.forEach {screen ->
    //        val isSelected = currentDestination?.hierarchy?.any {
    //            it.route == screen.route
    //        } == true
    //
    //        val targetFloat = if (screen == BottomNav.TipStoryScreen) 2f else 1.5f
    //        val animatedWeight by animateFloatAsState(targetValue = if (isSelected) targetFloat else 1f, label = "")
    //
    //        Box(
    //            modifier = Modifier
    //                .weight(animatedWeight)
    //                .height(60.dp)
    //                .background(Color.White)
    //                .clickable(
    //                    interactionSource = remember { MutableInteractionSource() },
    //                    indication = null
    //                ) {
    //                    navController.navigate(screen.route) {
    //                        navController.graph.startDestinationRoute?.let {
    //                            popUpTo(it) { saveState = true }
    //                        }
    //                        launchSingleTop = true
    //                        restoreState = true
    //                    }
    //                }
    //        ) {
    //            BottomNavItem(
    //                screen = screen,
    //                isSelected = isSelected
    //            )
    //        }
    //    }
    //}
}

sealed class BottomNav(val route: String, val title: String, val unSelectedIcon: Int, val selectedIcon: Int) {
    data object WalkScreen : BottomNav("walk", "산책", R.drawable.icon_naver, R.drawable.icon_naver)
    data object TipStoryScreen : BottomNav("tipstory", "팁스토리", R.drawable.icon_google, R.drawable.icon_google)
    data object HomeScreen : BottomNav("home", "홈", R.drawable.icon_kakao, R.drawable.icon_kakao)
    data object MallScreen : BottomNav("mall", "몰", R.drawable.home, R.drawable.home)
    data object MyScreen : BottomNav("my", "My", R.drawable.home, R.drawable.home)
}

@Composable
fun BackOnPressed(
) {
    val context = LocalContext.current
    var backPressedState by remember { mutableStateOf(true) }
    var backPressedTime = 0L
    val closeCmt = "한 번 더 누르시면 앱이 종료됩니다"

    BackHandler(enabled = backPressedState) {
        if(System.currentTimeMillis() - backPressedTime <= 1000L) {
            // 앱 종료
            (context as Activity).finish()
        } else {
            backPressedState = true
            Toast.makeText(context, closeCmt, Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}