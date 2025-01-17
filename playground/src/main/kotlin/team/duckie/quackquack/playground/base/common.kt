/*
 * Designed and developed by 2022 SungbinLand, Team Duckie
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/sungbinland/quack-quack/blob/main/LICENSE
 */

@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
)

package team.duckie.quackquack.playground.base

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.datastore.preferences.core.edit
import kotlin.math.roundToInt
import kotlin.reflect.KClass
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.launch
import team.duckie.quackquack.playground.theme.md_theme_light_primaryContainer
import team.duckie.quackquack.playground.util.PreferenceConfigs
import team.duckie.quackquack.playground.util.dataStore
import team.duckie.quackquack.playground.util.rememberToast
import team.duckie.quackquack.ui.animation.QuackAnimationMillis
import team.duckie.quackquack.ui.animation.QuackDefaultAnimationMillis
import team.duckie.quackquack.ui.textstyle.QuackDefaultFontScale
import team.duckie.quackquack.ui.textstyle.QuackFontScale

/**
 * 액티비티를 애니메이션과 함께 시작합니다.
 *
 * @param withFinish 액티비티를 시작할 때 기존 액티비티를 finish 할지 여부
 * @param intentBuilder 시작할 액티비티의 [Intent] 를 만드는 람다
 */
private inline fun Activity.startActivityWithAnimation(
    withFinish: Boolean = false,
    intentBuilder: () -> Intent,
) {
    startActivity(intentBuilder())
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    if (withFinish) {
        finish()
    }
}

/**
 * Playground 에서 표시할 액티비티들을 나열합니다.
 * 각각 액티비티들에는 표시할 디자인 컴포넌트들이 있습니다.
 *
 * @param title Playground 에 메인으로 표시될 액티비티의 타이틀
 * @param activities Playground 에 표시할 액티비티들
 */
@Composable
fun PlaygroundActivities(
    title: String = "QuackQuack Playground",
    activities: PersistentList<KClass<*>>,
) {
    var playgroundSettingAlertVisible by remember { mutableStateOf(false) }
    val currentActivity = LocalContext.current as Activity

    PlaygroundSettingAlert(
        visible = playgroundSettingAlertVisible,
        onDismissRequest = {
            playgroundSettingAlertVisible = !playgroundSettingAlertVisible
        },
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        modifier = Modifier.statusBarPadding(),
                        text = title,
                    )
                },
            )
        },
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    paddingValues = contentPadding,
                ),
            verticalArrangement = Arrangement.spacedBy(
                space = 8.dp,
            ),
            contentPadding = PaddingValues(
                all = 16.dp,
            ),
        ) {
            stickyHeader {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        playgroundSettingAlertVisible = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                    ),
                ) {
                    Text(
                        text = "Playground 설정",
                    )
                }
            }

            items(
                items = activities,
                key = { activity -> activity.simpleName ?: activity }
            ) { activity ->
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        currentActivity.startActivityWithAnimation {
                            Intent(
                                currentActivity,
                                activity.java,
                            )
                        }
                    },
                ) {
                    Text(
                        text = (activity.simpleName ?: activity.toString())
                            .removeSuffix(
                                suffix = "Playground",
                            ),
                    )
                }
            }
        }
    }
}

/**
 * [PreviewAlert] 로 표시할 여러 디자인 컴포넌트들을 나열합니다.
 *
 * @param title 표시할 디자인 컴포넌트들의 주제
 * @param items 표시할 디자인 컴포넌트들의 컴포저블 아이템
 */
@Composable
fun PlaygroundSection(
    title: String,
    items: PersistentList<Pair<String, @Composable () -> Unit>>,
) {
    var playgroundSettingAlertVisible by remember { mutableStateOf(false) }
    val previewVisibleStates = remember(
        key1 = items,
    ) {
        mutableStateListOf(*Array(items.size) { false })
    }

    PlaygroundSettingAlert(
        visible = playgroundSettingAlertVisible,
        onDismissRequest = {
            playgroundSettingAlertVisible = !playgroundSettingAlertVisible
        },
    )

    items.forEachIndexed { index, (_, composable) ->
        PreviewAlert(
            visible = previewVisibleStates[index],
            onBackPressed = {
                previewVisibleStates[index] = !previewVisibleStates[index]
            },
            content = composable,
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        modifier = Modifier.statusBarPadding(),
                        text = title,
                    )
                },
            )
        },
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    paddingValues = contentPadding,
                ),
            verticalArrangement = Arrangement.spacedBy(
                space = 8.dp,
            ),
            contentPadding = PaddingValues(
                all = 16.dp,
            ),
        ) {
            stickyHeader {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        playgroundSettingAlertVisible = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                    ),
                ) {
                    Text(
                        text = "Playground 설정",
                    )
                }
            }

            itemsIndexed(
                items = items,
                key = { index, _ -> index }
            ) { index, item ->
                val (contentTitle, _) = item

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        previewVisibleStates[index] = true
                    },
                ) {
                    Text(
                        text = contentTitle,
                    )
                }
            }
        }
    }
}

/**
 * StatusBar 의 height 만큼 패딩을 적용하는 확장 함수
 *
 * @return StatusBar 의 height 만큼 패딩이 적용된 [Modifier]
 */
@Stable
private fun Modifier.statusBarPadding() = composed {
    windowInsetsPadding(
        insets = WindowInsets.systemBars.only(
            sides = WindowInsetsSides.Top,
        ),
    )
}

/**
 * Playground 에서 표시할 디자인 컴포넌트의 세부 사항을 설정합니다.
 * 애니메이션의 지속 시간과 font scale 을 설정할 수 있습니다.
 *
 * @param visible Playground 설정 다이얼로그의 visible 여부
 * @param onDismissRequest Playground 설정 다이얼로그를 닫을 때 호출되는 콜백
 */
@Composable
private fun PlaygroundSettingAlert(
    visible: Boolean,
    onDismissRequest: () -> Unit,
) {
    if (visible) {
        val toast = rememberToast()
        val context = LocalContext.current.applicationContext
        val coroutineScope = rememberCoroutineScope()

        fun Int.msToSecondString() = (toDouble() / 1000.toDouble()).toString()

        var animationDurationInputState by remember {
            mutableStateOf(QuackAnimationMillis.msToSecondString())
        }
        var fontScaleInputState by remember {
            mutableStateOf(QuackFontScale.toString())
        }

        fun dismiss(reset: Boolean = false) {
            coroutineScope.launch {
                if (animationDurationInputState.isEmpty()) {
                    animationDurationInputState = QuackDefaultAnimationMillis.msToSecondString()
                }
                if (fontScaleInputState.isEmpty()) {
                    fontScaleInputState = QuackDefaultFontScale.toString()
                }

                context.dataStore.edit { preference ->
                    preference[PreferenceConfigs.AnimationDurationKey] = when (reset) {
                        true -> QuackDefaultAnimationMillis
                        else -> (animationDurationInputState.toDouble() * 1000.toDouble()).roundToInt()
                    }.also { newAnimationMillis ->
                        QuackAnimationMillis = newAnimationMillis.coerceAtLeast(minimumValue = 0)
                    }
                    preference[PreferenceConfigs.FontScaleKey] = when (reset) {
                        true -> QuackDefaultFontScale
                        else -> fontScaleInputState.toDouble()
                    }.also { newFontScale ->
                        QuackFontScale = newFontScale.coerceAtLeast(minimumValue = 0.0)
                    }
                }
            }
            onDismissRequest()
        }

        AlertDialog(
            modifier = Modifier.wrapContentSize(),
            title = {
                Text(
                    text = "Playground 설정",
                )
            },
            text = {
                Column(
                    modifier = Modifier.wrapContentSize(),
                ) {
                    Text(
                        text = "애니메이션 지속 시간",
                    )
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 4.dp,
                            ),
                        value = animationDurationInputState,
                        onValueChange = { newAnimationDuration ->
                            animationDurationInputState = newAnimationDuration
                        },
                        singleLine = true,
                        trailingIcon = {
                            Text(
                                modifier = Modifier.padding(
                                    end = 16.dp,
                                ),
                                text = "단위: 초",
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),
                    )
                    Text(
                        modifier = Modifier.padding(
                            top = 20.dp,
                        ),
                        text = "Font Scale",
                    )
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 4.dp,
                            ),
                        value = fontScaleInputState,
                        onValueChange = { newFontScale ->
                            fontScaleInputState = newFontScale
                        },
                        singleLine = true,
                        trailingIcon = {
                            Text(
                                modifier = Modifier.padding(
                                    end = 16.dp,
                                ),
                                text = "배수",
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        ),
                    )
                }
            },
            onDismissRequest = ::dismiss,
            confirmButton = {
                Button(
                    onClick = ::dismiss,
                ) {
                    Text(
                        text = "저장",
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        dismiss(reset = true)
                        toast("Playground 설정을 초기화 했어요")
                    },
                ) {
                    Text(
                        text = "초기화",
                    )
                }
            },
        )
    }
}

/**
 * 디자인 컴포넌트의 Preview 를 다이얼로그 형태로 표시합니다.
 *
 * 다이얼로그를 표시해야 하는 디자인 컴포넌트가 있어서 내부에서 [AlertDialog] 를
 * 사용하지 않고, [Box] 를 이용해 다이얼로그 형태로 보여줍니다.
 *
 * @param visible preview dialog 의 visible 상태
 * @param onBackPressed preview dialog 의 back button 이 눌렸을 때 호출될 콜백
 * @param content preview dialog 로 표시할 디자인 컴포넌트
 */
@Composable
private fun PreviewAlert(
    visible: Boolean,
    onBackPressed: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (visible) {
        val alertShape = remember {
            RoundedCornerShape(
                size = 15.dp,
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(
                    zIndex = 1.0f,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .border(
                        width = 5.dp,
                        color = md_theme_light_primaryContainer,
                        shape = alertShape,
                    )
                    .clip(
                        shape = alertShape,
                    )
                    .fillMaxWidth(
                        fraction = 0.8f,
                    )
                    .fillMaxHeight(
                        fraction = 0.7f,
                    )
                    .background(
                        color = Color.White,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                content()
                Icon(
                    modifier = Modifier
                        .size(
                            size = 48.dp,
                        )
                        .clickable(
                            onClick = onBackPressed,
                        )
                        .align(
                            alignment = Alignment.TopStart,
                        )
                        .padding(
                            start = 16.dp,
                            top = 16.dp,
                        ),
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "back"
                )
            }
        }
    }
}
