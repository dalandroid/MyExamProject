package net.pettip.app.navi.componet

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.ViewRootForInspector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * @Project     : PetTip-Android
 * @FileName    : CustomBottomSheet
 * @Date        : 2024-05-07
 * @author      : CareBiz
 * @description : net.pettip.app.navi.componet
 * @see net.pettip.app.navi.componet.CustomBottomSheet
 */

@Composable
fun CustomBottomSheet(
    onDismiss: () -> Unit,
    showBottomSheet: Boolean,
    maxHeight:Float = 0.5f,
    content: @Composable ColumnScope.() -> Unit  = {},
    contentColor: Color = Color.White,
    cornerShape: RoundedCornerShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    navigationBarPadding : Boolean = true
) {
    var sheetState by remember{ mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val focusManager = LocalFocusManager.current

    var bottomHeight = getAdaptiveHeight()
    var statusBarHeight = getStatusBarHeight()

    LaunchedEffect(showBottomSheet) {
        if (!sheetState){
            sheetState = showBottomSheet
            focusManager.clearFocus()
        }
    }

    if (showBottomSheet){
        FullScreenPopup{
            Scrim(
                color = androidx.compose.material.ModalBottomSheetDefaults.scrimColor,
                onDismiss = {
                    scope.launch {
                        sheetState = false
                        delay(250)
                        onDismiss()
                    }
                },
                visible =  sheetState
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(top = statusBarHeight),
                contentAlignment = Alignment.BottomCenter
            ) {
                // 바텀 시트 내용
                AnimatedVisibility(
                    visible = sheetState,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                ) {
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxHeight(maxHeight)
                                .fillMaxWidth()
                                .background(color = contentColor, shape = cornerShape)
                                .clip(cornerShape)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { }
                        ) {
                            Column (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .systemBarsPadding()
                                    .padding(bottom = bottomHeight)
                            ){
                                content()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getAdaptiveHeight(): Dp {
    val context = LocalContext.current
    val resources = context.resources
    val density = LocalDensity.current
    val view = LocalView.current
    val keyboardHeightState = remember { mutableStateOf(0.dp) }

    // 내비게이션 바 높이 가져오기
    val navigationBarHeight = remember {
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            with(density) {
                resources.getDimensionPixelSize(resourceId).toDp()
            }
        } else {
            0.dp
        }
    }

    DisposableEffect(view) {
        val listener = OnApplyWindowInsetsListener { _, insets ->
            val keyboardHeightPixels = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            if (keyboardHeightPixels > 0) {
                keyboardHeightState.value = with(density) { keyboardHeightPixels.toDp() }
            } else {
                keyboardHeightState.value = 0.dp
            }
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(view, listener)

        onDispose {
            ViewCompat.setOnApplyWindowInsetsListener(view, null)
        }
    }

    return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R){
        if (keyboardHeightState.value > 0.dp) {
            keyboardHeightState.value
        } else {
            navigationBarHeight
        }
    }else{
        0.dp
    }
}

@Composable
fun getStatusBarHeight(): Dp {
    val context = LocalContext.current
    val resources = context.resources
    val density = LocalDensity.current

    // 상태 표시줄 높이 가져오기
    val statusBarHeight = remember {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            with(density) {
                resources.getDimensionPixelSize(resourceId).toDp()
            }
        } else {
            0.dp
        }
    }

    return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R){
        statusBarHeight
    }else{
        0.dp
    }
}

@Composable
internal fun FullScreenPopup(
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    val id = rememberSaveable { UUID.randomUUID() }
    val parentComposition = rememberCompositionContext()
    val currentContent by rememberUpdatedState(content)
    val layoutDirection = LocalLayoutDirection.current
    val fullScreenWindow = remember {
        FullScreenWindow(
            composeView = view,
            saveId = id,
        ).apply {
            setCustomContent(
                parent = parentComposition,
                content = currentContent
            )
        }
    }

    DisposableEffect(fullScreenWindow) {
        fullScreenWindow.show()
        fullScreenWindow.superSetLayoutDirection(layoutDirection)
        onDispose {
            fullScreenWindow.disposeComposition()
            fullScreenWindow.dismiss()
        }
    }
}

@SuppressLint("ViewConstructor")
private class FullScreenWindow(
    private val composeView: View,
    saveId: UUID,
) : AbstractComposeView(composeView.context),
    ViewRootForInspector {

    init {
        id = android.R.id.content
        setViewTreeLifecycleOwner(composeView.findViewTreeLifecycleOwner())
        setViewTreeViewModelStoreOwner(composeView.findViewTreeViewModelStoreOwner())
        setViewTreeSavedStateRegistryOwner(composeView.findViewTreeSavedStateRegistryOwner())
        setTag(androidx.compose.ui.R.id.compose_view_saveable_id_tag, "Popup:$saveId")
        clipChildren = false
        consumeWindowInsets = false
    }

    private val windowManager =
        composeView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private val displayWidth: Int
        get() = context.resources.displayMetrics.widthPixels

    private val params: WindowManager.LayoutParams =
        WindowManager.LayoutParams().apply {
            gravity = Gravity.BOTTOM or Gravity.START
            type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
            width = displayWidth
            height = WindowManager.LayoutParams.MATCH_PARENT
            format = PixelFormat.TRANSLUCENT
            token = composeView.applicationWindowToken
            flags = flags or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            softInputMode = WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST and WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        }

    private var content: @Composable () -> Unit by mutableStateOf({})

    override var shouldCreateCompositionOnAttachedToWindow: Boolean = false
        private set

    @Composable
    override fun Content() {
        content()
    }

    fun setCustomContent(
        parent: CompositionContext? = null,
        content: @Composable () -> Unit
    ) {
        parent?.let { setParentCompositionContext(it) }
        this.content = content
        shouldCreateCompositionOnAttachedToWindow = true
    }

    fun show() {
        windowManager.addView(this, params)
    }

    fun dismiss() {
        setViewTreeLifecycleOwner(null)
        setViewTreeSavedStateRegistryOwner(null)
        windowManager.removeViewImmediate(this)
    }

    override fun setLayoutDirection(layoutDirection: Int) {
        // Do nothing. ViewRootImpl will call this method attempting to set the layout direction
        // from the context's locale, but we have one already from the parent composition.
    }

    // Sets the "real" layout direction for our content that we obtain from the parent composition.
    fun superSetLayoutDirection(layoutDirection: LayoutDirection) {
        val direction = when (layoutDirection) {
            LayoutDirection.Ltr -> android.util.LayoutDirection.LTR
            LayoutDirection.Rtl -> android.util.LayoutDirection.RTL
        }
        super.setLayoutDirection(direction)
    }

}

@Composable
private fun Scrim(
    color: Color,
    onDismiss: () -> Unit,
    visible: Boolean
) {
    if (color.isSpecified) {
        val alpha by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = TweenSpec(), label = ""
        )
        val closeSheet = 2
        val dismissModifier = if (visible) {
            Modifier
                .pointerInput(onDismiss) { detectTapGestures { onDismiss() } }
                .semantics(mergeDescendants = true) {
                    contentDescription = closeSheet.toString()
                }
        } else {
            Modifier
        }

        Canvas(
            Modifier
                .fillMaxSize()
                .then(dismissModifier)
        ) {
            drawRect(color = color, alpha = alpha)
        }
    }
}
