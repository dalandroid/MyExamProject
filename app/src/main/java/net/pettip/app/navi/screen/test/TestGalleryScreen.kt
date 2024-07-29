package net.pettip.app.navi.screen.test

import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import net.pettip.app.navi.R
import net.pettip.app.navi.componet.CustomBottomSheet
import net.pettip.app.navi.utils.function.LoadGallery

/**
 * @Project     : PetTip-Android
 * @FileName    : TestGalleryScreen
 * @Date        : 2024-07-09
 * @author      : CareBiz
 * @description : net.pettip.app.navi.screen.test
 * @see net.pettip.app.navi.screen.test.TestGalleryScreen
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TestGalleryScreen(){

    val context = LocalContext.current
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }

    var showBottomSheet by remember{ mutableStateOf(false) }
    var selectedIndex by remember{ mutableStateOf<Int?>(null) }

    val permissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )

    val galleryLauncherMulti =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetMultipleContents()
        ) {
            selectedImages = selectedImages + it
        }

    val galleryLauncherSingle =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            if (selectedIndex != null && uri != null) {
                selectedImages = selectedImages.toMutableList().apply {
                    set(selectedIndex!!, uri)
                }
                selectedIndex = null
            }
        }

    LaunchedEffect(Unit) {
        permissions.launchMultiplePermissionRequest()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .safeContentPadding()
    ){
        LazyRow(
            state = LazyListState(),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy((-2).dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(selectedImages.size) { index ->
                val painter = rememberAsyncImagePainter(
                    model = selectedImages[index],
                    filterQuality = FilterQuality.None,
                    contentScale = ContentScale.None
                )

                Box(
                    modifier = Modifier
                        .size(110.dp),
                    contentAlignment = Alignment.Center
                ){
                    Image(
                        painter = painter,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clickable {
                                selectedIndex = index
                                galleryLauncherSingle.launch("image/*")
                            }
                    )

                    Box(modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .clickable {
                            selectedImages = selectedImages.toMutableList().apply {
                                removeAt(index)
                            }
                        }
                        .align(Alignment.TopEnd)
                        .background(Color.White)
                        .border(width = 1.dp, color = Color.Black, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = ""
                        )
                    }
                }

            }

            item {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clickable {
                            galleryLauncherMulti.launch("image/*")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier
                        .size(100.dp)
                        .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription =""
                        )
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clickable {
                            showBottomSheet = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier
                        .size(100.dp)
                        .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription =""
                        )
                    }
                }
            }
        }

        CustomBottomSheet(
            onDismiss = { showBottomSheet = false },
            showBottomSheet = showBottomSheet,
            maxHeight = 0.9f,
            draggable = false
        ){

            var imageList by remember { mutableStateOf<List<Uri>>(emptyList()) }
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp
            val itemSize = (screenWidth/3).dp - 4.dp

            LaunchedEffect (Unit){
                if (showBottomSheet){
                    imageList = LoadGallery.loadPhotos(context)
                    Log.d("LOG", imageList.toString())
                }
            }

            Column (
                modifier = Modifier.fillMaxSize()
            ){
                Text(
                    text = "사진",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(100.dp),
                    contentPadding = PaddingValues(vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(imageList.size){index->
                        val painter = rememberAsyncImagePainter(
                            model = imageList[index],
                            filterQuality = FilterQuality.None,
                            contentScale = ContentScale.None
                        )

                        Image(
                            painter = painter,
                            contentDescription = "",
                            modifier = Modifier
                                .size(itemSize)
                                .clickable {

                                }
                            ,
                            contentScale = ContentScale.None
                        )
                    }
                }
            }
        }
    }

}