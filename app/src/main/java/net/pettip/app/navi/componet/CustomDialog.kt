package net.pettip.app.navi.componet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

/**
 * @Project     : PetTip-Android
 * @FileName    : CustomDialog
 * @Date        : 2024-06-12
 * @author      : CareBiz
 * @description : net.pettip.app.navi.componet
 * @see net.pettip.app.navi.componet.CustomDialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialog(
    onDismissRequest: () -> Unit,
    dismissOnBackPress : Boolean = true,
    dismissOnClickOutside :Boolean = true,
    usePlatformDefaultWidth: Boolean = false,
    horizontalPadding:Dp = 40.dp,
    oneButton:Boolean = false,
    title : String,
    positiveText:String = "",
    negativeText: String = "",
    onPositive:()->Unit = {},
    onNegative:()->Unit = {},
    content:@Composable () -> Unit
){
    BasicAlertDialog(
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnClickOutside,
            usePlatformDefaultWidth = usePlatformDefaultWidth
        ),
        onDismissRequest = onDismissRequest,
    ){
        Column (
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .fillMaxWidth()
                .background(Color.White)
        ){
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp),
                contentAlignment = Alignment.CenterStart
            ){
                Text(text = title)
            }

            Box(modifier = Modifier.fillMaxWidth()){
                content()
            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 6.dp)
            ){
                if (oneButton){
                    Button(
                        onClick = { onPositive() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = positiveText)
                    }
                }else{
                    Button(
                        onClick = { onNegative() },
                        modifier = Modifier.weight(0.35f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(width = 1.dp,color = Color.Black),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(text = negativeText)
                    }

                    Spacer(modifier = Modifier.padding(horizontal = 3.dp))

                    Button(
                        onClick = { onPositive() },
                        modifier = Modifier.weight(0.65f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = positiveText)
                    }
                }
            }
        }
    }
}

@Composable
fun measureTextWidth(text: String, style: TextStyle): Dp {
    val textMeasurer = rememberTextMeasurer()
    val widthInPixels = textMeasurer.measure(text, style).size.width
    return with(LocalDensity.current) { widthInPixels.toDp() }
}