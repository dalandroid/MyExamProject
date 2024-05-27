package net.pettip.app.navi.componet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Box (
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color.Gray)
        ){
            Text(
                text = "Drawer Head",
                color = Color.Black,
                fontSize = 20.sp,
                modifier = modifier.align(Alignment.Center)
            )
        }

        Box (
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
        ){
            Text(
                text = "Drawer Body",
                color = Color.Black,
                fontSize = 20.sp,
                modifier = modifier.align(Alignment.Center)
            )
        }
    }
}