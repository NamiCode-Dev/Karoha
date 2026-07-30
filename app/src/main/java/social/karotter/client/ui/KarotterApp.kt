package social.karotter.client.ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.drawable.Animatable as DrawableAnimatable
import android.media.MediaPlayer
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.os.SystemClock
import android.provider.Settings
import android.provider.OpenableColumns
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ReplacementSpan
import android.view.MotionEvent
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.zIndex
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.imageLoader
import java.net.URL
import io.noties.markwon.LinkResolver
import io.noties.markwon.Markwon
import io.noties.markwon.SoftBreakAddsNewLinePlugin
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Fill
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.fill.Bookmark as FilledBookmark
import com.adamglin.phosphoricons.fill.Heart as FilledHeart
import com.adamglin.phosphoricons.fill.House as FilledHouse
import com.adamglin.phosphoricons.fill.User as FilledUser
import com.adamglin.phosphoricons.regular.ArrowBendUpLeft
import com.adamglin.phosphoricons.regular.ArrowClockwise
import com.adamglin.phosphoricons.regular.ArrowLeft
import com.adamglin.phosphoricons.regular.ArrowRight
import com.adamglin.phosphoricons.regular.ArrowUp
import com.adamglin.phosphoricons.regular.Bell
import com.adamglin.phosphoricons.regular.Bookmark as RegularBookmark
import com.adamglin.phosphoricons.regular.CalendarBlank
import com.adamglin.phosphoricons.regular.CaretDown
import com.adamglin.phosphoricons.regular.CaretUp
import com.adamglin.phosphoricons.regular.ChatCircle
import com.adamglin.phosphoricons.regular.Check
import com.adamglin.phosphoricons.regular.DotsThreeVertical
import com.adamglin.phosphoricons.regular.EnvelopeSimple
import com.adamglin.phosphoricons.regular.Eye as RegularEye
import com.adamglin.phosphoricons.regular.Heart as RegularHeart
import com.adamglin.phosphoricons.regular.House as RegularHouse
import com.adamglin.phosphoricons.regular.Image as RegularImage
import com.adamglin.phosphoricons.regular.ListChecks
import com.adamglin.phosphoricons.regular.LinkSimple
import com.adamglin.phosphoricons.regular.Lock
import com.adamglin.phosphoricons.regular.MagnifyingGlass
import com.adamglin.phosphoricons.regular.MapPin
import com.adamglin.phosphoricons.regular.MaskHappy
import com.adamglin.phosphoricons.regular.MoonStars
import com.adamglin.phosphoricons.regular.Plus
import com.adamglin.phosphoricons.regular.Prohibit
import com.adamglin.phosphoricons.regular.PushPin
import com.adamglin.phosphoricons.regular.Repeat
import com.adamglin.phosphoricons.regular.Robot
import com.adamglin.phosphoricons.regular.Scroll
import com.adamglin.phosphoricons.regular.SealCheck
import com.adamglin.phosphoricons.regular.SignOut
import com.adamglin.phosphoricons.regular.SlidersHorizontal
import com.adamglin.phosphoricons.regular.SpeakerHigh
import com.adamglin.phosphoricons.regular.SpeakerSlash
import com.adamglin.phosphoricons.regular.Trash
import com.adamglin.phosphoricons.regular.Trophy
import com.adamglin.phosphoricons.regular.User as RegularUser
import com.adamglin.phosphoricons.regular.UserCircleGear
import com.adamglin.phosphoricons.regular.UserSwitch
import com.adamglin.phosphoricons.regular.UsersThree
import com.adamglin.phosphoricons.regular.Info
import com.adamglin.phosphoricons.regular.X
import social.karotter.client.data.ApiBoard
import social.karotter.client.data.ApiCircle
import social.karotter.client.data.ApiCommunity
import social.karotter.client.data.ApiCommunityGroups
import social.karotter.client.data.ApiCommunityMember
import social.karotter.client.data.ApiDmGroup
import social.karotter.client.data.ApiDmMessage
import social.karotter.client.data.ApiFollowRequest
import social.karotter.client.data.ApiMedia
import social.karotter.client.data.ApiLevelRankingEntry
import social.karotter.client.data.ApiLinkPreview
import social.karotter.client.data.ApiLoginResult
import social.karotter.client.data.ApiNotification
import social.karotter.client.data.ApiPost
import social.karotter.client.data.ApiPoll
import social.karotter.client.data.ApiProPreferences
import social.karotter.client.data.ApiQuestion
import social.karotter.client.data.ApiReaction
import social.karotter.client.data.ApiResult
import social.karotter.client.data.ApiSearchResult
import social.karotter.client.data.ApiStory
import social.karotter.client.data.ApiStoryComment
import social.karotter.client.data.ApiStoryTextStyle
import social.karotter.client.data.ApiThread
import social.karotter.client.data.ApiTrend
import social.karotter.client.data.ApiUser
import social.karotter.client.data.ApiUploadMedia
import social.karotter.client.data.KarotterApi
import social.karotter.client.data.SavedCredentialAccount
import social.karotter.client.data.hasValidatedInternet
import social.karotter.client.BackgroundNotificationManager
import social.karotter.client.AppVisibility
import social.karotter.client.AppUpdateInfo
import social.karotter.client.AppUpdateManager
import social.karotter.client.InstallApkResult
import social.karotter.client.NotificationNavigationTarget
import java.io.File
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

private const val AUTO_REFRESH_INTERVAL_MS = 15_000L

private data class AppPalette(
    val paper: Color,
    val surface: Color,
    val ink: Color,
    val muted: Color,
    val hairline: Color,
    val accent: Color,
    val paleAccent: Color,
    val mint: Color,
    val sky: Color,
    val lemon: Color,
    val strong: Color,
    val onStrong: Color
)
private val ThemePalettes = mapOf(
    "paper" to AppPalette(Color(0xFFFAFAF7), Color(0xFFFFFFFF), Color(0xFF171713), Color(0xFF77766E), Color(0xFFE7E6DF), Color(0xFFFF5A32), Color(0xFFFFE8DF), Color(0xFFDFF3E8), Color(0xFFE3EEFF), Color(0xFFFFF0B5), Color(0xFF171713), Color.White),
    "paper-dark" to AppPalette(Color(0xFF11110F), Color(0xFF1B1B18), Color(0xFFF4F3EC), Color(0xFFA6A49A), Color(0xFF32322D), Color(0xFFFF714E), Color(0xFF3A211C), Color(0xFF173329), Color(0xFF192B3E), Color(0xFF3A3217), Color(0xFFF4F3EC), Color(0xFF11110F)),
    "snow" to AppPalette(Color(0xFFFFFFFF), Color(0xFFF8FAFD), Color(0xFF101319), Color(0xFF68707C), Color(0xFFE5E9EF), Color(0xFF356DF3), Color(0xFFE5ECFF), Color(0xFFE1F5ED), Color(0xFFE7F0FF), Color(0xFFFFF2BF), Color(0xFF101319), Color.White),
    "snow-dark" to AppPalette(Color(0xFF0B0E14), Color(0xFF151A23), Color(0xFFF2F5FA), Color(0xFF9BA5B4), Color(0xFF293140), Color(0xFF6E98FF), Color(0xFF1C2D59), Color(0xFF14332B), Color(0xFF172C4C), Color(0xFF393117), Color(0xFFF2F5FA), Color(0xFF0B0E14)),
    "mist" to AppPalette(Color(0xFFF4F7F6), Color(0xFFFFFFFF), Color(0xFF14201D), Color(0xFF687873), Color(0xFFDDE6E2), Color(0xFF168A70), Color(0xFFDCF1EB), Color(0xFFDDF4E9), Color(0xFFE0EEF4), Color(0xFFFFF0B6), Color(0xFF14201D), Color.White),
    "mist-dark" to AppPalette(Color(0xFF0D1513), Color(0xFF16211E), Color(0xFFEAF3F0), Color(0xFF94A7A1), Color(0xFF293A35), Color(0xFF42C7A6), Color(0xFF17372F), Color(0xFF18382D), Color(0xFF18313A), Color(0xFF383116), Color(0xFFEAF3F0), Color(0xFF0D1513))
)
private var RelativeTimeReference by mutableStateOf(Instant.now())

@Composable
private fun rememberNetworkAvailable(retryKey: Int): Boolean {
    val context = LocalContext.current
    val manager = remember(context) {
        context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    val scope = rememberCoroutineScope()
    var available by remember { mutableStateOf(context.hasValidatedInternet()) }
    DisposableEffect(manager) {
        fun update() {
            scope.launch { available = context.hasValidatedInternet() }
        }
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) = update()
            override fun onLost(network: Network) = update()
            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) = update()
        }
        manager.registerDefaultNetworkCallback(callback)
        update()
        onDispose { runCatching { manager.unregisterNetworkCallback(callback) } }
    }
    LaunchedEffect(retryKey) {
        available = context.hasValidatedInternet()
    }
    return available
}
private var ActivePalette by mutableStateOf(ThemePalettes.getValue("paper"))
private val Paper get() = ActivePalette.paper
private val Surface get() = ActivePalette.surface
private val Ink get() = ActivePalette.ink
private val Muted get() = ActivePalette.muted
private val Hairline get() = ActivePalette.hairline
private val Carrot get() = ActivePalette.accent
private val PaleCarrot get() = ActivePalette.paleAccent
private val Mint get() = ActivePalette.mint
private val Sky get() = ActivePalette.sky
private val Lemon get() = ActivePalette.lemon
private val Strong get() = ActivePalette.strong
private val OnStrong get() = ActivePalette.onStrong
private val LocalMentionHandler = staticCompositionLocalOf<((String) -> Unit)?> { null }
private val LocalHashtagHandler = staticCompositionLocalOf<((String) -> Unit)?> { null }
private val LocalReactionHandler = staticCompositionLocalOf<((Post, String, Boolean) -> Unit)?> { null }
private val LocalPollVoteHandler = staticCompositionLocalOf<((Post, Long, (ApiPoll?) -> Unit) -> Unit)?> { null }
private val LocalRekarotStates = staticCompositionLocalOf<Map<Long, RekarotState>> { emptyMap() }
private val LocalPostInteractionStates = staticCompositionLocalOf<Map<Long, PostInteractionState>> { emptyMap() }
private val LocalPostMenuEnvironment = staticCompositionLocalOf<PostMenuEnvironment?> { null }
private val LocalPostMenuResultHandler = staticCompositionLocalOf<((PostMenuAction, Post) -> Unit)?> { null }
private val LocalNavigationActive = staticCompositionLocalOf { true }
private val LocalLinkPreviewApi = staticCompositionLocalOf<KarotterApi?> { null }
private val LocalViewerIsPro = staticCompositionLocalOf { false }

private data class RekarotState(val rekaroted: Boolean, val count: Int)
private data class PostInteractionState(
    val liked: Boolean? = null,
    val bookmarked: Boolean? = null
)

private data class HorizontalTabMotion(val shiftDp: Float, val alpha: Float)

private data class ThirdPartyLicense(
    val name: String,
    val version: String,
    val license: String,
    val copyright: String,
    val notice: String,
    val url: String
)

@Composable
private fun rememberHorizontalTabMotion(pageIndex: Int, resetKey: Any? = Unit): HorizontalTabMotion {
    val shift = remember(resetKey) { Animatable(0f) }
    var previousIndex by remember(resetKey) { mutableIntStateOf(pageIndex) }
    LaunchedEffect(resetKey, pageIndex) {
        if (previousIndex != pageIndex) {
            val forward = pageIndex >= previousIndex
            previousIndex = pageIndex
            shift.snapTo(if (forward) 44f else -44f)
            shift.animateTo(0f, tween(320, easing = FastOutSlowInEasing))
        }
    }
    return HorizontalTabMotion(
        shiftDp = shift.value,
        alpha = (1f - abs(shift.value) / 120f).coerceIn(.68f, 1f)
    )
}

private enum class PostMenuAction { EDIT, MUTE, BLOCK, DELETE, PIN }
private data class PostMenuEnvironment(
    val viewerId: Long?,
    val execute: (PostMenuAction, Post, (Boolean) -> Unit) -> Unit,
    val edit: (Post, ((Post) -> Unit)?) -> Unit
)

private enum class Section(val label: String) {
    HOME("ホーム"), SEARCH("見つける"), COMMUNITY("コミュニティ"), BOARD("掲示板"), DM("DM"), PROFILE("あなた")
}

private sealed interface Overlay {
    data class PostDetail(val post: Post) : Overlay
    data class UserDetail(
        val post: Post,
        val retainedState: UserDetailRetainedState = UserDetailRetainedState()
    ) : Overlay
    data class StoryDetail(val story: ApiStory) : Overlay
    data class BoardDetail(val board: ApiBoard) : Overlay
    data object BoardCreate : Overlay
    data class DmConversation(val group: ApiDmGroup) : Overlay
    data class HashtagSearch(val tag: String) : Overlay
    data class Notifications(
        val retainedState: NotificationsRetainedState = NotificationsRetainedState()
    ) : Overlay
}

private data class ComposeTarget(
    val parent: Post? = null,
    val quote: Post? = null,
    val community: ApiCommunity? = null,
    val question: ApiQuestion? = null
)
private data class ComposerMedia(val uri: Uri, val name: String, val mimeType: String)
private data class ComposerSettings(
    val communityId: Long? = null,
    val visibility: String = "PUBLIC",
    val replyRestriction: String = "EVERYONE",
    val viewerCircleId: Long? = null,
    val replyCircleId: Long? = null,
    val minimumAge: Int? = null,
    val maximumAge: Int? = null,
    val isAiGenerated: Boolean = false,
    val isPromotional: Boolean = false,
    val isR18: Boolean = false,
    val hideFromMinors: Boolean = false,
    val scheduledFor: String? = null,
    val expiresAt: String? = null
)

private data class Post(
    val authorId: Long = 0,
    val name: String,
    val handle: String,
    val time: String,
    val text: String,
    val avatar: Color,
    val avatarUrl: String? = null,
    val replies: Int,
    val likes: Int,
    val rekarots: Int = 0,
    val tag: String? = null,
    val featured: Boolean = false,
    val id: Long? = null,
    val initiallyLiked: Boolean = false,
    val initiallyRekaroted: Boolean = false,
    val initiallyBookmarked: Boolean = false,
    val quotedPost: Post? = null,
    val media: List<ApiMedia> = emptyList(),
    val mediaUrl: String? = null,
    val rekarotedBy: ApiUser? = null,
    val officialMark: String = "NONE",
    val officialMarks: List<String> = emptyList(),
    val isBotAccount: Boolean = false,
    val isParodyAccount: Boolean = false,
    val isPrivateAccount: Boolean = false,
    val subscriptionPlan: String = "FREE",
    val subscriptionStatus: String = "INACTIVE",
    val showSubscriptionBadges: Boolean = true,
    val showPlusBadge: Boolean = true,
    val showProBadge: Boolean = true,
    val premiumBadgeColor: String = "ORANGE",
    val showCardDecoration: Boolean = true,
    val cardAccentColor: String? = null,
    val createdAt: String = "",
    val reactions: List<ApiReaction> = emptyList(),
    val poll: ApiPoll? = null,
    val visibility: String = "PUBLIC",
    val viewerCircleId: Long? = null,
    val viewerCircleName: String? = null,
    val replyRestriction: String = "EVERYONE",
    val replyCircleId: Long? = null,
    val replyCircleName: String? = null,
    val minimumAge: Int? = null,
    val maximumAge: Int? = null,
    val isAiGenerated: Boolean = false,
    val isPromotional: Boolean = false,
    val isR18: Boolean = false,
    val expiresAt: String? = null,
    val viewsCount: Int = 0,
    val bookmarksCount: Int = 0,
    val quoteUsersCount: Int = 0,
    val parentId: Long? = null,
    val canQuote: Boolean = true
)

private class ProfilePageRetainedState {
    val selectedKind = mutableStateOf("posts")
    val userPosts = mutableStateOf<List<Post>>(emptyList())
    val nextPage = mutableIntStateOf(1)
    val nextCursor = mutableStateOf<Long?>(null)
    val hasNext = mutableStateOf(true)
    val loadingMore = mutableStateOf(false)
    val refreshing = mutableStateOf(false)
    val loadedKind = mutableStateOf<String?>(null)
    val listState = LazyListState()
}

private class UserDetailRetainedState {
    val profile = mutableStateOf<ApiUser?>(null)
    val reloadKey = mutableIntStateOf(0)
    val loadedReloadKey = mutableIntStateOf(-1)
    val page = ProfilePageRetainedState()
}

private class NotificationsRetainedState {
    val notifications = mutableStateOf<List<ApiNotification>>(emptyList())
    val loading = mutableStateOf(false)
    val nextPage = mutableIntStateOf(1)
    val hasMore = mutableStateOf(true)
    val error = mutableStateOf<String?>(null)
    val selectedType = mutableStateOf<String?>(null)
    val requestRevision = mutableIntStateOf(0)
    val initialized = mutableStateOf(false)
    val loadedType = mutableStateOf<String?>(null)
    val readMarked = mutableStateOf(false)
    val filterListState = LazyListState()
    val listState = LazyListState()
}

private fun postStableKey(post: Post): String =
    post.id?.let { "post:$it" }
        ?: "local:${post.authorId}:${post.createdAt}:${post.text.hashCode()}"

private val PostUrlPattern = Regex("""https?://[^\s<>()\[\]{}]+""", RegexOption.IGNORE_CASE)

private fun firstPostUrl(text: String): String? =
    PostUrlPattern.find(text)?.value
        ?.trimEnd('.', ',', '。', '、', ')', ']', '}', '"', '\'')
        ?.takeIf(String::isNotBlank)

@Composable
fun KarotterApp(
    notificationTarget: NotificationNavigationTarget? = null,
    onNotificationTargetConsumed: () -> Unit = {}
) {
    val context = LocalContext.current
    val api = remember { KarotterApi(context) }
    val systemDark = isSystemInDarkTheme()
    var networkRetryKey by remember { mutableIntStateOf(0) }
    val networkAvailable = rememberNetworkAvailable(networkRetryKey)
    val appearancePrefs = remember { context.getSharedPreferences("karotter_appearance_v1", android.content.Context.MODE_PRIVATE) }
    val onboardingPrefs = remember { context.getSharedPreferences("karoha_onboarding_v1", android.content.Context.MODE_PRIVATE) }
    var onboardingComplete by remember { mutableStateOf(onboardingPrefs.getBoolean("completed", false)) }
    var followSystemTheme by remember {
        mutableStateOf(
            if (!onboardingComplete) true
            else appearancePrefs.getBoolean("follow_system", !appearancePrefs.contains("theme"))
        )
    }
    var themeKey by remember {
        val fallback = if (systemDark) "snow-dark" else "snow"
        mutableStateOf(
            if (!onboardingComplete || appearancePrefs.getBoolean("follow_system", !appearancePrefs.contains("theme"))) fallback
            else appearancePrefs.getString("theme", fallback)?.takeIf(ThemePalettes::containsKey) ?: fallback
        )
    }
    LaunchedEffect(systemDark, followSystemTheme) {
        if (followSystemTheme) {
            val family = appearancePrefs.getString("theme_family", themeKey.removeSuffix("-dark"))
                ?.takeIf { ThemePalettes.containsKey(it) && ThemePalettes.containsKey("$it-dark") }
                ?: "snow"
            themeKey = family + if (systemDark) "-dark" else ""
            appearancePrefs.edit()
                .putString("theme", themeKey)
                .putString("theme_family", family)
                .putBoolean("follow_system", true)
                .apply()
        }
    }
    val targetPalette = ThemePalettes.getValue(themeKey)
    val paletteAnimation = tween<Color>(durationMillis = 360, easing = FastOutSlowInEasing)
    val animatedPaper by animateColorAsState(targetPalette.paper, paletteAnimation, label = "themePaper")
    val animatedSurface by animateColorAsState(targetPalette.surface, paletteAnimation, label = "themeSurface")
    val animatedInk by animateColorAsState(targetPalette.ink, paletteAnimation, label = "themeInk")
    val animatedMuted by animateColorAsState(targetPalette.muted, paletteAnimation, label = "themeMuted")
    val animatedHairline by animateColorAsState(targetPalette.hairline, paletteAnimation, label = "themeHairline")
    val animatedAccent by animateColorAsState(targetPalette.accent, paletteAnimation, label = "themeAccent")
    val animatedPaleAccent by animateColorAsState(targetPalette.paleAccent, paletteAnimation, label = "themePaleAccent")
    val animatedMint by animateColorAsState(targetPalette.mint, paletteAnimation, label = "themeMint")
    val animatedSky by animateColorAsState(targetPalette.sky, paletteAnimation, label = "themeSky")
    val animatedLemon by animateColorAsState(targetPalette.lemon, paletteAnimation, label = "themeLemon")
    val animatedStrong by animateColorAsState(targetPalette.strong, paletteAnimation, label = "themeStrong")
    val animatedOnStrong by animateColorAsState(targetPalette.onStrong, paletteAnimation, label = "themeOnStrong")
    val animatedPalette = AppPalette(
        animatedPaper, animatedSurface, animatedInk, animatedMuted, animatedHairline, animatedAccent,
        animatedPaleAccent, animatedMint, animatedSky, animatedLemon, animatedStrong, animatedOnStrong
    )
    val rootView = LocalView.current
    SideEffect {
        ActivePalette = animatedPalette
        val window = (rootView.context as? Activity)?.window ?: return@SideEffect
        window.statusBarColor = animatedPalette.paper.toArgb()
        window.navigationBarColor = animatedPalette.paper.toArgb()
        val useDarkIcons = animatedPalette.paper.luminance() > .5f
        WindowCompat.getInsetsController(window, rootView).isAppearanceLightStatusBars = useDarkIcons
        WindowCompat.getInsetsController(window, rootView).isAppearanceLightNavigationBars = useDarkIcons
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        while (true) {
            delay(AUTO_REFRESH_INTERVAL_MS)
            RelativeTimeReference = Instant.now()
        }
    }
    var checking by remember { mutableStateOf(true) }
    var initialLaunchAnimationVisible by remember { mutableStateOf(true) }
    var initialLaunchAnimationExiting by remember { mutableStateOf(false) }
    var initialLaunchMinimumPassed by remember { mutableStateOf(false) }
    var user by remember { mutableStateOf<ApiUser?>(null) }
    var returnAccountIdentifier by remember { mutableStateOf<String?>(null) }
    var dataFailure by remember { mutableStateOf<String?>(null) }
    var recoveryBusy by remember { mutableStateOf<String?>(null) }
    var updateCheckStarted by remember { mutableStateOf(false) }
    var availableUpdate by remember { mutableStateOf<AppUpdateInfo?>(null) }
    var updateDownloading by remember { mutableStateOf(false) }
    var updateProgress by remember { mutableStateOf(0f) }
    var updateError by remember { mutableStateOf<String?>(null) }
    var downloadedUpdateApk by remember { mutableStateOf<File?>(null) }
    val creatorPromptPrefs = remember {
        context.getSharedPreferences("karoha_creator_follow_prompt_v1", android.content.Context.MODE_PRIVATE)
    }
    var creatorProfile by remember { mutableStateOf<ApiUser?>(null) }
    var creatorFollowBusy by remember { mutableStateOf(false) }
    var creatorFollowError by remember { mutableStateOf<String?>(null) }
    val installPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val apk = downloadedUpdateApk
        if (apk != null) {
            when (val result = AppUpdateManager.launchInstaller(context, apk)) {
                InstallApkResult.Launched -> availableUpdate = null
                InstallApkResult.PermissionRequired ->
                    updateError = "このアプリからのインストールを許可すると更新できます"
                is InstallApkResult.Failure -> updateError = result.message
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(1_050L)
        initialLaunchMinimumPassed = true
    }
    LaunchedEffect(initialLaunchMinimumPassed, checking, onboardingComplete) {
        val destinationReady = !onboardingComplete || !checking
        if (initialLaunchMinimumPassed && destinationReady && initialLaunchAnimationVisible && !initialLaunchAnimationExiting) {
            initialLaunchAnimationExiting = true
            delay(320L)
            initialLaunchAnimationVisible = false
        }
    }

    LaunchedEffect(networkAvailable, initialLaunchAnimationVisible) {
        if (networkAvailable && !initialLaunchAnimationVisible && !updateCheckStarted) {
            updateCheckStarted = true
            val result = withContext(Dispatchers.IO) {
                AppUpdateManager.checkForUpdate(context)
            }
            result.getOrNull()?.let {
                availableUpdate = it
                updateError = null
            }
        }
    }

    LaunchedEffect(user?.id, networkAvailable) {
        val activeUser = user ?: return@LaunchedEffect
        if (!networkAvailable ||
            activeUser.username.equals("namicode", ignoreCase = true) ||
            creatorPromptPrefs.getBoolean("shown_${activeUser.id}", false)
        ) {
            creatorProfile = null
            return@LaunchedEffect
        }
        when (val result = withContext(Dispatchers.IO) { api.user("namicode") }) {
            is ApiResult.Success -> {
                if (result.value.isFollowing) {
                    creatorPromptPrefs.edit().putBoolean("shown_${activeUser.id}", true).apply()
                } else {
                    creatorProfile = result.value
                    creatorFollowError = null
                }
            }
            is ApiResult.Failure -> Unit
        }
    }

    DisposableEffect(api) {
        api.onAuthenticationLost = {
            scope.launch { dataFailure = "ログイン状態を確認できませんでした。アカウントを切り替えるか、ログインしなおしてください。" }
        }
        api.onDataAccessFailure = { message ->
            scope.launch { dataFailure = message }
        }
        onDispose {
            api.onAuthenticationLost = null
            api.onDataAccessFailure = null
        }
    }

    LaunchedEffect(networkAvailable, onboardingComplete) {
        if (!onboardingComplete) {
            checking = true
            return@LaunchedEffect
        }
        if (!networkAvailable) {
            checking = false
            return@LaunchedEffect
        }
        if (user != null || dataFailure != null) {
            checking = false
            return@LaunchedEffect
        }
        val launchStartedAt = SystemClock.elapsedRealtime()
        if (api.hasRecoverableSession()) {
            when (val result = withContext(Dispatchers.IO) { api.me() }) {
                is ApiResult.Success -> {
                    user = result.value
                    dataFailure = null
                    BackgroundNotificationManager.onLoginSucceeded(context)
                }
                is ApiResult.Failure -> {
                    if (networkAvailable) {
                        dataFailure = "ネット接続はありますが、データを取得できませんでした。\n${result.message}"
                    }
                }
            }
        }
        val remaining = 1350L - (SystemClock.elapsedRealtime() - launchStartedAt)
        if (remaining > 0) delay(remaining)
        checking = false
    }

    when {
        initialLaunchAnimationVisible -> LaunchScreen(exiting = initialLaunchAnimationExiting)
        !onboardingComplete -> WelcomeScreen(
            systemDark = systemDark,
            onThemeSelectionChange = { family, mode ->
                followSystemTheme = mode == "system"
                themeKey = when (mode) {
                    "dark" -> "$family-dark"
                    "light" -> family
                    else -> family + if (systemDark) "-dark" else ""
                }
                appearancePrefs.edit()
                    .putString("theme", themeKey)
                    .putString("theme_family", family)
                    .putBoolean("follow_system", followSystemTheme)
                    .apply()
            },
            onFinished = {
                onboardingPrefs.edit().putBoolean("completed", true).apply()
                onboardingComplete = true
            }
        )
        !networkAvailable -> NoNetworkScreen { networkRetryKey += 1 }
        checking -> LaunchScreen()
        dataFailure != null -> DataFailureRecoveryScreen(
            message = dataFailure.orEmpty(),
            accounts = api.savedAccounts().filterNot { it.identifier == api.activeAccountIdentifier() },
            busyIdentifier = recoveryBusy,
            onSwitchAccount = { identifier ->
                if (recoveryBusy == null) {
                    recoveryBusy = identifier
                    scope.launch {
                        when (val result = withContext(Dispatchers.IO) { api.switchAccount(identifier) }) {
                            is ApiResult.Success -> {
                                user = result.value
                                dataFailure = null
                                returnAccountIdentifier = null
                                BackgroundNotificationManager.onLoginSucceeded(context)
                            }
                            is ApiResult.Failure -> dataFailure = "アカウントを切り替えられませんでした。\n${result.message}"
                        }
                        recoveryBusy = null
                    }
                }
            },
            onRelogin = {
                api.prepareForAdditionalAccount()
                returnAccountIdentifier = null
                dataFailure = null
                user = null
            },
            onRetry = {
                if (recoveryBusy == null) {
                    recoveryBusy = "__retry__"
                    scope.launch {
                        when (val result = withContext(Dispatchers.IO) { api.me() }) {
                            is ApiResult.Success -> {
                                user = result.value
                                dataFailure = null
                                BackgroundNotificationManager.onLoginSucceeded(context)
                            }
                            is ApiResult.Failure -> {
                                dataFailure = "ネット接続はありますが、データを取得できませんでした。\n${result.message}"
                            }
                        }
                        recoveryBusy = null
                    }
                }
            }
        )
        user == null -> LoginScreen(
            onBack = returnAccountIdentifier?.let { identifier ->
                {
                    scope.launch {
                        when (val result = withContext(Dispatchers.IO) { api.switchAccount(identifier) }) {
                            is ApiResult.Success -> {
                                user = result.value
                                returnAccountIdentifier = null
                                BackgroundNotificationManager.onLoginSucceeded(context)
                            }
                            is ApiResult.Failure -> Unit
                        }
                    }
                }
            },
            onLogin = { identifier, password, done ->
                scope.launch {
                    val result = withContext(Dispatchers.IO) { api.beginLogin(identifier, password) }
                    if (result is ApiLoginResult.Success) {
                        user = result.user
                        returnAccountIdentifier = null
                        BackgroundNotificationManager.onLoginSucceeded(context)
                    }
                    done(result)
                }
            },
            onVerifyTwoFactor = { identifier, password, token, code, done ->
                scope.launch {
                    val result = withContext(Dispatchers.IO) {
                        api.completeTwoFactorLogin(identifier, password, token, code)
                    }
                    if (result is ApiLoginResult.Success) {
                        user = result.user
                        returnAccountIdentifier = null
                        BackgroundNotificationManager.onLoginSucceeded(context)
                    }
                    done(result)
                }
            }
        )
        else -> user?.let { activeUser -> androidx.compose.runtime.key(activeUser.id) { MainShell(
            api = api,
            currentUser = activeUser,
            notificationTarget = notificationTarget,
            onNotificationTargetConsumed = onNotificationTargetConsumed,
            followsSystemTheme = followSystemTheme,
            themeKey = themeKey,
            onThemeChange = { selected ->
                val systemSelection = selected.startsWith("system:")
                val requested = selected.removePrefix("system:")
                if (systemSelection && ThemePalettes.containsKey(requested) && ThemePalettes.containsKey("$requested-dark")) {
                    followSystemTheme = true
                    themeKey = requested + if (systemDark) "-dark" else ""
                    appearancePrefs.edit()
                        .putString("theme", themeKey)
                        .putString("theme_family", requested)
                        .putBoolean("follow_system", true)
                        .apply()
                } else if (ThemePalettes.containsKey(selected)) {
                    followSystemTheme = false
                    themeKey = selected
                    appearancePrefs.edit()
                        .putString("theme", selected)
                        .putString("theme_family", selected.removeSuffix("-dark"))
                        .putBoolean("follow_system", false)
                        .apply()
                }
            },
            onLogout = {
                scope.launch {
                    val nextAccount = withContext(Dispatchers.IO) {
                        api.logout()
                        api.savedAccounts().firstOrNull()?.let { account ->
                            api.switchAccount(account.identifier)
                        }
                    }
                    returnAccountIdentifier = null
                    if (nextAccount is ApiResult.Success) {
                        user = nextAccount.value
                        dataFailure = null
                        BackgroundNotificationManager.onLoginSucceeded(context)
                    } else {
                        user = null
                        BackgroundNotificationManager.onLoggedOut(context)
                    }
                }
            },
            onAccountChanged = {
                BackgroundNotificationManager.onLoginSucceeded(context)
                returnAccountIdentifier = null
                user = it
            },
            onAddAccount = {
                returnAccountIdentifier = api.activeAccountIdentifier()
                api.prepareForAdditionalAccount()
                user = null
            }
        ) } }
    }

    availableUpdate?.let { update ->
        AppUpdateDialog(
            update = update,
            downloading = updateDownloading,
            progress = updateProgress,
            error = updateError,
            onDismiss = {
                if (!updateDownloading) {
                    availableUpdate = null
                    updateError = null
                }
            },
            onInstall = {
                if (!updateDownloading) {
                    updateDownloading = true
                    updateProgress = 0f
                    updateError = null
                    scope.launch {
                        val result = withContext(Dispatchers.IO) {
                            AppUpdateManager.downloadApk(context, update) { progress ->
                                scope.launch { updateProgress = progress }
                            }
                        }
                        updateDownloading = false
                        result.fold(
                            onSuccess = { apk ->
                                downloadedUpdateApk = apk
                                when (val installResult = AppUpdateManager.launchInstaller(context, apk)) {
                                    InstallApkResult.Launched -> availableUpdate = null
                                    InstallApkResult.PermissionRequired -> {
                                        installPermissionLauncher.launch(
                                            AppUpdateManager.installPermissionIntent(context)
                                        )
                                    }
                                    is InstallApkResult.Failure -> updateError = installResult.message
                                }
                            },
                            onFailure = {
                                updateError = it.message ?: "更新をダウンロードできませんでした"
                            }
                        )
                    }
                }
            }
        )
    }

    if (availableUpdate == null) {
        creatorProfile?.let { profile ->
            CreatorFollowDialog(
                profile = profile,
                following = profile.isFollowing,
                busy = creatorFollowBusy,
                error = creatorFollowError,
                onDismiss = {
                    user?.id?.let { creatorPromptPrefs.edit().putBoolean("shown_$it", true).apply() }
                    creatorProfile = null
                },
                onFollow = {
                    if (!creatorFollowBusy) {
                        creatorFollowBusy = true
                        creatorFollowError = null
                        scope.launch {
                            when (val result = withContext(Dispatchers.IO) { api.follow(profile.id, true) }) {
                                is ApiResult.Success -> {
                                    creatorProfile = profile.copy(isFollowing = true)
                                    user?.id?.let {
                                        creatorPromptPrefs.edit().putBoolean("shown_$it", true).apply()
                                    }
                                }
                                is ApiResult.Failure -> creatorFollowError = result.message
                            }
                            creatorFollowBusy = false
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun MainShell(
    api: KarotterApi,
    currentUser: ApiUser?,
    notificationTarget: NotificationNavigationTarget?,
    onNotificationTargetConsumed: () -> Unit,
    followsSystemTheme: Boolean,
    themeKey: String,
    onThemeChange: (String) -> Unit,
    onLogout: () -> Unit,
    onAccountChanged: (ApiUser) -> Unit,
    onAddAccount: () -> Unit
) {
    val rekarotStates = remember { mutableStateMapOf<Long, RekarotState>() }
    val postInteractionStates = remember { mutableStateMapOf<Long, PostInteractionState>() }
    val context = LocalContext.current
    var section by remember { mutableStateOf(Section.HOME) }
    var sectionHistory by remember { mutableStateOf<List<Section>>(emptyList()) }
    var composerOpen by remember { mutableStateOf(false) }
    var composeTarget by remember { mutableStateOf(ComposeTarget()) }
    var editingPost by remember { mutableStateOf<Post?>(null) }
    var editResultHandler by remember { mutableStateOf<((Post) -> Unit)?>(null) }
    var storyCreatorOpen by remember { mutableStateOf(false) }
    var overlayStack by remember { mutableStateOf<List<Overlay>>(emptyList()) }
    val overlay = overlayStack.lastOrNull()
    var posts by remember { mutableStateOf<List<Post>>(emptyList()) }
    var stories by remember { mutableStateOf<List<ApiStory>>(emptyList()) }
    var storiesLoading by remember { mutableStateOf(true) }
    var storiesError by remember { mutableStateOf<String?>(null) }
    var storyTransitionDirection by remember { mutableIntStateOf(1) }
    var boards by remember { mutableStateOf<List<ApiBoard>>(emptyList()) }
    var boardsRefreshing by remember { mutableStateOf(false) }
    var boardsError by remember { mutableStateOf<String?>(null) }
    var unreadCount by remember { mutableIntStateOf(0) }
    var notificationToast by remember { mutableStateOf<ApiNotification?>(null) }
    var notificationToastVisible by remember { mutableStateOf(false) }
    var dmUnreadCount by remember { mutableIntStateOf(0) }
    var loading by remember { mutableStateOf(true) }
    var refreshing by remember { mutableStateOf(false) }
    var homeLoadingMore by remember { mutableStateOf(false) }
    var homeNextPage by remember { mutableIntStateOf(2) }
    var homeHasNext by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var homeMode by remember { mutableStateOf("latest") }
    var homeRanking by remember { mutableStateOf("latest") }
    var homeCommunities by remember { mutableStateOf<List<ApiCommunity>>(emptyList()) }
    var homePostCache by remember { mutableStateOf<Map<String, List<Post>>>(emptyMap()) }
    var dmConversationOpen by remember { mutableStateOf(false) }
    var searchRequest by remember { mutableStateOf<Pair<String, Long>?>(null) }
    var latestCreatedPost by remember { mutableStateOf<Post?>(null) }
    var latestCommunityPost by remember { mutableStateOf<Pair<Long, Post>?>(null) }
    val scope = rememberCoroutineScope()
    val safe = WindowInsets.safeDrawing.asPaddingValues()
    val lifecycleOwner = LocalLifecycleOwner.current
    var appResumed by remember(lifecycleOwner) {
        mutableStateOf(lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, _ ->
            appResumed = lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(appResumed, currentUser?.id) {
        if (!appResumed || currentUser == null) return@LaunchedEffect
        while (true) {
            if (api.hasNetworkConnection()) {
                withContext(Dispatchers.IO) { api.maintainSavedSessions() }
            }
            delay(60_000L)
        }
    }

    fun pushOverlay(next: Overlay) { overlayStack = overlayStack + next }
    fun popOverlay() { overlayStack = overlayStack.dropLast(1) }
    fun selectSection(next: Section) {
        overlayStack = emptyList()
        if (next != section) {
            sectionHistory = (sectionHistory + section).takeLast(12)
            section = next
        }
    }
    LaunchedEffect(notificationTarget?.nonce) {
        val target = notificationTarget ?: return@LaunchedEffect
        try {
            when {
                target.postId != null -> {
                    when (val result = withContext(Dispatchers.IO) { api.post(target.postId) }) {
                        is ApiResult.Success -> pushOverlay(Overlay.PostDetail(result.value.toUiPost()))
                        is ApiResult.Failure -> error = result.message
                    }
                }
                !target.username.isNullOrBlank() -> {
                    when (val result = withContext(Dispatchers.IO) { api.user(target.username) }) {
                        is ApiResult.Success -> pushOverlay(Overlay.UserDetail(result.value.toProfilePost()))
                        is ApiResult.Failure -> error = result.message
                    }
                }
            }
        } finally {
            onNotificationTargetConsumed()
        }
    }
    fun uniqueTimelinePosts(incoming: List<ApiPost>): List<Post> =
        incoming.map(ApiPost::toUiPost).distinctBy(::postStableKey)

    fun homeCommunityId(mode: String): Long? =
        mode.takeIf { it.startsWith("community:") }?.substringAfter(':')?.toLongOrNull()

    suspend fun fetchHomePage(mode: String, page: Int, ranking: String): ApiResult<social.karotter.client.data.ApiPostPage> {
        val communityId = homeCommunityId(mode)
        return if (communityId != null) {
            api.communityPosts(communityId, page, 20, tab = "latest")
        } else {
            api.timelinePage(mode, page, ranking = ranking)
        }
    }

    suspend fun fetchHomeLatest(mode: String, ranking: String): ApiResult<List<ApiPost>> =
        when (val result = fetchHomePage(mode, 1, ranking)) {
            is ApiResult.Success -> ApiResult.Success(result.value.posts)
            is ApiResult.Failure -> result
        }

    fun mergeTimeline(incoming: List<ApiPost>) {
        val refreshed = uniqueTimelinePosts(incoming)
        val refreshedById = refreshed.mapNotNull { post -> post.id?.let { it to post } }.toMap()
        refreshedById.forEach { (postId, freshPost) ->
            postInteractionStates[postId]?.let { pending ->
                val reconciled = pending.copy(
                    liked = pending.liked?.takeUnless { it == freshPost.initiallyLiked },
                    bookmarked = pending.bookmarked?.takeUnless { it == freshPost.initiallyBookmarked }
                )
                if (reconciled.liked == null && reconciled.bookmarked == null) {
                    postInteractionStates.remove(postId)
                } else {
                    postInteractionStates[postId] = reconciled
                }
            }
            rekarotStates[postId]?.let { pending ->
                if (pending.rekaroted == freshPost.initiallyRekaroted) {
                    rekarotStates.remove(postId)
                }
            }
        }
        val existingIds = posts.mapNotNullTo(hashSetOf()) { it.id }
        val additions = refreshed.filter { it.id == null || it.id !in existingIds }
        val updatedVisiblePosts = posts.map { current ->
            current.id?.let(refreshedById::get) ?: current
        }
        val merged = (additions + updatedVisiblePosts).distinctBy(::postStableKey)
        if (merged != posts) {
            posts = merged
            homePostCache = homePostCache + ("$homeMode:$homeRanking" to posts)
        }
    }

    fun loadAll() {
        scope.launch {
            loading = true
            val results = withContext(Dispatchers.IO) {
                listOf(api.timelinePage("latest", 1), api.stories(), api.boards())
            }
            when (val unread = withContext(Dispatchers.IO) { api.unreadCount() }) { is ApiResult.Success -> unreadCount = unread.value; is ApiResult.Failure -> Unit }
            when (val timelines = withContext(Dispatchers.IO) { api.homeCommunityTimelines() }) {
                is ApiResult.Success -> homeCommunities = timelines.value
                is ApiResult.Failure -> Unit
            }
            (results[0] as? ApiResult.Success<*>)?.value?.let { value ->
                @Suppress("UNCHECKED_CAST")
                if (homeMode == "latest") {
                    val page = value as social.karotter.client.data.ApiPostPage
                    if (posts.isEmpty()) {
                        posts = uniqueTimelinePosts(page.posts)
                        homePostCache = homePostCache + ("latest:latest" to posts)
                    } else mergeTimeline(page.posts)
                    homeNextPage = page.nextPage ?: 2
                    homeHasNext = page.hasNext
                }
            }
            (results[1] as? ApiResult.Success<*>)?.value?.let {
                @Suppress("UNCHECKED_CAST")
                val loadedStories = it as List<ApiStory>
                stories = loadedStories
            }
            storiesError = (results[1] as? ApiResult.Failure)?.message
            storiesLoading = false
            (results[2] as? ApiResult.Success<*>)?.value?.let { @Suppress("UNCHECKED_CAST") boards = it as List<ApiBoard> }
            if (homeMode == "latest") {
                error = results.filterIsInstance<ApiResult.Failure>().firstOrNull()?.message
                loading = false
            }
        }
    }

    fun loadTimeline(mode: String, ranking: String = homeRanking) {
        homePostCache = homePostCache + ("$homeMode:$homeRanking" to posts)
        homeMode = mode
        homeRanking = if (mode == "trending") "latest" else ranking
        val requestRanking = homeRanking
        posts = homePostCache["$mode:$requestRanking"].orEmpty()
        homeNextPage = 2
        homeHasNext = true
        homeLoadingMore = false
        error = null
        loading = posts.isEmpty()
        scope.launch {
            when (val result = withContext(Dispatchers.IO) { fetchHomePage(mode, 1, requestRanking) }) {
                is ApiResult.Success -> if (homeMode == mode && homeRanking == requestRanking) {
                    posts = uniqueTimelinePosts(result.value.posts)
                    homePostCache = homePostCache + ("$mode:$requestRanking" to posts)
                    homeNextPage = result.value.nextPage ?: 2
                    homeHasNext = result.value.hasNext
                    error = null
                }
                is ApiResult.Failure -> if (homeMode == mode && homeRanking == requestRanking) error = result.message
            }
            if (homeMode == mode && homeRanking == requestRanking) loading = false
        }
    }

    fun loadMoreHome() {
        if (homeLoadingMore || loading || !homeHasNext) return
        val requestedMode = homeMode
        val requestedRanking = homeRanking
        val requestedPage = homeNextPage
        homeLoadingMore = true
        scope.launch {
            when (val result = withContext(Dispatchers.IO) { fetchHomePage(requestedMode, requestedPage, requestedRanking) }) {
                is ApiResult.Success -> if (homeMode == requestedMode && homeRanking == requestedRanking) {
                    val incoming = uniqueTimelinePosts(result.value.posts).filter { next -> posts.none { postStableKey(it) == postStableKey(next) } }
                    posts = (posts + incoming).distinctBy(::postStableKey)
                    homePostCache = homePostCache + ("$requestedMode:$requestedRanking" to posts)
                    homeNextPage = result.value.nextPage ?: requestedPage + 1
                    homeHasNext = result.value.hasNext && incoming.isNotEmpty()
                }
                is ApiResult.Failure -> if (homeMode == requestedMode && homeRanking == requestedRanking) {
                    error = result.message
                    homeHasNext = false
                }
            }
            if (homeMode == requestedMode && homeRanking == requestedRanking) homeLoadingMore = false
        }
    }

    fun refreshBoards() {
        if (boardsRefreshing) return
        boardsRefreshing = true
        scope.launch {
            when (val result = withContext(Dispatchers.IO) { api.boards() }) {
                is ApiResult.Success -> {
                    boards = result.value
                    boardsError = null
                }
                is ApiResult.Failure -> boardsError = result.message
            }
            boardsRefreshing = false
        }
    }

    LaunchedEffect(Unit) { loadAll() }
    LaunchedEffect(section, appResumed) {
        if (section != Section.BOARD || !appResumed) return@LaunchedEffect
        while (true) {
            delay(AUTO_REFRESH_INTERVAL_MS)
            if (!boardsRefreshing) refreshBoards()
        }
    }
    LaunchedEffect(Unit) {
        while (true) {
            delay(AUTO_REFRESH_INTERVAL_MS)
            when (val result = withContext(Dispatchers.IO) { api.stories() }) {
                is ApiResult.Success -> {
                    val viewedIds = stories.filter { it.viewed }.mapTo(hashSetOf()) { it.id }
                    val refreshedStories = result.value.map { if (it.id in viewedIds) it.copy(viewed = true) else it }
                    stories = refreshedStories
                    storiesError = null
                }
                is ApiResult.Failure -> storiesError = result.message
            }
        }
    }
    LaunchedEffect(notificationToast?.let { "${it.id}:${it.createdAt}" }) {
        val toastKey = notificationToast?.let { "${it.id}:${it.createdAt}" }
        if (toastKey != null) {
            delay(5_000L)
            if (notificationToast?.let { "${it.id}:${it.createdAt}" } == toastKey) {
                notificationToastVisible = false
                delay(320L)
                if (notificationToast?.let { "${it.id}:${it.createdAt}" } == toastKey && !notificationToastVisible) {
                    notificationToast = null
                }
            }
        }
    }
    LaunchedEffect(currentUser?.id, appResumed) {
        if (!appResumed) return@LaunchedEffect
        var lastNotificationKey: String? = null
        while (true) {
            if (AppVisibility.isForeground) {
                when (val result = withContext(Dispatchers.IO) { api.notificationPage(1, 20) }) {
                    is ApiResult.Success -> {
                        val newest = result.value.firstOrNull { !it.suppressed }
                        val newestKey = newest?.let { "${it.id}:${it.type}:${it.createdAt}" }
                        if (
                            lastNotificationKey != null &&
                            newestKey != null &&
                            newestKey != lastNotificationKey
                        ) {
                            notificationToast = newest
                            notificationToastVisible = true
                            BackgroundNotificationManager.markForegroundNotificationShown(
                                context,
                                api.activeAccountIdentifier(),
                                newest
                            )
                        }
                        if (newestKey != null) lastNotificationKey = newestKey
                    }
                    is ApiResult.Failure -> Unit
                }
                when (val result = withContext(Dispatchers.IO) { api.unreadCount() }) {
                    is ApiResult.Success -> unreadCount = result.value
                    is ApiResult.Failure -> Unit
                }
            }
            delay(10_000L)
        }
    }
    LaunchedEffect(Unit) {
        while (true) {
            when (val result = withContext(Dispatchers.IO) { api.dmGroups() }) {
                is ApiResult.Success -> dmUnreadCount =
                    result.value.sumOf { it.unreadCount } + result.value.count { it.isRequest && it.unreadCount == 0 }
                is ApiResult.Failure -> Unit
            }
            delay(5_000)
        }
    }
    LaunchedEffect(homeMode, homeRanking) {
        while (true) {
            delay(AUTO_REFRESH_INTERVAL_MS)
            if (refreshing) continue
            refreshing = true
            when (val result = withContext(Dispatchers.IO) { fetchHomeLatest(homeMode, homeRanking) }) {
                is ApiResult.Success -> mergeTimeline(result.value)
                is ApiResult.Failure -> Unit
            }
            refreshing = false
        }
    }

    fun refreshHome() {
        if (refreshing) return
        refreshing = true
        scope.launch {
            when (val result = withContext(Dispatchers.IO) { fetchHomeLatest(homeMode, homeRanking) }) {
                is ApiResult.Success -> mergeTimeline(result.value)
                is ApiResult.Failure -> error = result.message
            }
            refreshing = false
        }
    }

    fun openStory(story: ApiStory) {
        stories = stories.map { if (it.id == story.id) it.copy(viewed = true) else it }
        pushOverlay(Overlay.StoryDetail(story.copy(viewed = true)))
    }

    fun nextStory(current: ApiStory) {
        val next = stories.getOrNull(stories.indexOfFirst { it.id == current.id } + 1)
        if (next == null) popOverlay()
        else {
            storyTransitionDirection = 1
            stories = stories.map { if (it.id == next.id) it.copy(viewed = true) else it }
            overlayStack = overlayStack.dropLast(1) + Overlay.StoryDetail(next.copy(viewed = true))
        }
    }

    fun previousStory(current: ApiStory) {
        val index = stories.indexOfFirst { it.id == current.id }
        val previous = stories.getOrNull(index - 1) ?: return
        storyTransitionDirection = -1
        stories = stories.map { if (it.id == previous.id) it.copy(viewed = true) else it }
        overlayStack = overlayStack.dropLast(1) + Overlay.StoryDetail(previous.copy(viewed = true))
    }

    BackHandler(enabled = !composerOpen && (overlayStack.isNotEmpty() || sectionHistory.isNotEmpty() || section != Section.HOME)) {
        when {
            overlayStack.isNotEmpty() -> popOverlay()
            sectionHistory.isNotEmpty() -> {
                section = sectionHistory.last()
                sectionHistory = sectionHistory.dropLast(1)
            }
            section != Section.HOME -> section = Section.HOME
        }
    }

    fun openComposer(parent: Post? = null, quote: Post? = null, community: ApiCommunity? = null, question: ApiQuestion? = null) {
        if (quote != null && (quote.isPrivateAccount || !quote.canQuote)) {
            Toast.makeText(context, "非公開アカウントの投稿は引用できません", Toast.LENGTH_SHORT).show()
            return
        }
        composeTarget = ComposeTarget(parent = parent, quote = quote, community = community, question = question)
        composerOpen = true
    }

    fun openPostEditor(post: Post, onEdited: ((Post) -> Unit)? = null) {
        editingPost = post
        editResultHandler = onEdited
    }

    fun like(post: Post, desired: Boolean) {
        val postId = post.id ?: return
        val previous = postInteractionStates[postId]
        postInteractionStates[postId] = (previous ?: PostInteractionState()).copy(liked = desired)
        scope.launch {
            val result = withContext(Dispatchers.IO) { api.like(postId, desired) }
            if (result is ApiResult.Failure) {
                if (previous == null) postInteractionStates.remove(postId)
                else postInteractionStates[postId] = previous
                error = result.message
            }
        }
    }

    fun bookmark(post: Post, desired: Boolean) {
        val postId = post.id ?: return
        val previous = postInteractionStates[postId]
        postInteractionStates[postId] = (previous ?: PostInteractionState()).copy(bookmarked = desired)
        scope.launch {
            val result = withContext(Dispatchers.IO) { api.bookmark(postId, desired) }
            if (result is ApiResult.Failure) {
                if (previous == null) postInteractionStates.remove(postId)
                else postInteractionStates[postId] = previous
                error = result.message
            }
        }
    }

    fun rekarot(post: Post, desired: Boolean) {
        val postId = post.id ?: return
        val previous = rekarotStates[postId]
        val wasRekaroted = previous?.rekaroted ?: post.initiallyRekaroted
        val previousCount = previous?.count ?: post.rekarots
        val nextCount = (
            previousCount + when {
                desired && !wasRekaroted -> 1
                !desired && wasRekaroted -> -1
                else -> 0
            }
        ).coerceAtLeast(0)
        rekarotStates[postId] = RekarotState(desired, nextCount)
        scope.launch {
            val result = withContext(Dispatchers.IO) { api.rekarot(postId, desired) }
            if (result is ApiResult.Failure) {
                if (previous == null) rekarotStates.remove(postId)
                else rekarotStates[postId] = previous
                error = result.message
            }
        }
    }

    fun react(post: Post, emoji: String, desired: Boolean) {
        scope.launch {
            val result = withContext(Dispatchers.IO) { api.react(post.id ?: return@withContext ApiResult.Failure("投稿IDがありません"), emoji, desired) }
            if (result is ApiResult.Failure) error = result.message
        }
    }

    fun votePoll(post: Post, optionId: Long, done: (ApiPoll?) -> Unit) {
        val postId = post.id ?: return done(null)
        scope.launch {
            when (val result = withContext(Dispatchers.IO) { api.votePoll(postId, optionId) }) {
                is ApiResult.Success -> {
                    posts = posts.map { if (it.id == postId) it.copy(poll = result.value) else it }
                    done(result.value)
                }
                is ApiResult.Failure -> {
                    error = result.message
                    done(null)
                }
            }
        }
    }

    fun executePostMenuAction(action: PostMenuAction, post: Post, done: (Boolean) -> Unit) {
        scope.launch {
            val result: ApiResult<Unit> = withContext(Dispatchers.IO) {
                when (action) {
                    PostMenuAction.EDIT -> return@withContext ApiResult.Failure("編集画面から変更してください")
                    PostMenuAction.MUTE -> api.mute(post.authorId, true)
                    PostMenuAction.BLOCK -> api.block(post.authorId, true)
                    PostMenuAction.DELETE -> {
                        val postId = post.id ?: return@withContext ApiResult.Failure("投稿IDがありません")
                        api.deletePost(postId)
                    }
                    PostMenuAction.PIN -> {
                        val postId = post.id ?: return@withContext ApiResult.Failure("投稿IDがありません")
                        when (val unpinResult = api.pinPost(null)) {
                            is ApiResult.Failure -> unpinResult
                            is ApiResult.Success -> api.pinPost(postId)
                        }
                    }
                }
            }
            when (result) {
                is ApiResult.Success -> {
                    when (action) {
                        PostMenuAction.EDIT -> Unit
                        PostMenuAction.MUTE, PostMenuAction.BLOCK ->
                            posts = posts.filterNot { it.authorId == post.authorId }
                        PostMenuAction.DELETE -> {
                            posts = posts.filterNot { it.id == post.id }
                            if ((overlayStack.lastOrNull() as? Overlay.PostDetail)?.post?.id == post.id) {
                                popOverlay()
                            }
                        }
                        PostMenuAction.PIN -> Unit
                    }
                    done(true)
                }
                is ApiResult.Failure -> {
                    error = result.message
                    done(false)
                }
            }
        }
    }

    fun startDirectMessage(user: ApiUser) {
        scope.launch {
            when (val result = withContext(Dispatchers.IO) { api.startDm(user.id) }) {
                is ApiResult.Success -> pushOverlay(Overlay.DmConversation(result.value))
                is ApiResult.Failure -> error = result.message
            }
        }
    }

    CompositionLocalProvider(
        LocalMentionHandler provides { username ->
        scope.launch {
            when (val result = withContext(Dispatchers.IO) { api.user(username) }) {
                is ApiResult.Success -> pushOverlay(Overlay.UserDetail(result.value.toProfilePost()))
                is ApiResult.Failure -> error = result.message
            }
        }
        },
        LocalHashtagHandler provides { tag ->
            if (section == Section.HOME) {
                pushOverlay(Overlay.HashtagSearch(tag))
            } else {
                overlayStack = emptyList()
                selectSection(Section.SEARCH)
                searchRequest = "#$tag" to System.nanoTime()
            }
        },
        LocalReactionHandler provides ::react,
        LocalPollVoteHandler provides ::votePoll,
        LocalRekarotStates provides rekarotStates,
        LocalPostInteractionStates provides postInteractionStates,
        LocalLinkPreviewApi provides api,
        LocalViewerIsPro provides (
            currentUser?.subscriptionPlan.equals("PRO", ignoreCase = true) &&
                currentUser?.subscriptionStatus.equals("ACTIVE", ignoreCase = true)
            ),
        LocalPostMenuEnvironment provides PostMenuEnvironment(currentUser?.id, ::executePostMenuAction, ::openPostEditor)
    ) {
    val bottomDockHazeState = remember { HazeState() }
    Box(Modifier.fillMaxSize().background(Paper).padding(top = safe.calculateTopPadding())) {
        AnimatedContent(
            targetState = section,
            modifier = Modifier.fillMaxSize().hazeSource(state = bottomDockHazeState, zIndex = 0f),
            transitionSpec = {
                val forward = targetState.ordinal >= initialState.ordinal
                (slideInHorizontally(tween(480, easing = FastOutSlowInEasing)) { if (forward) it / 4 else -it / 4 } + fadeIn(tween(280)))
                    .togetherWith(slideOutHorizontally(tween(360)) { if (forward) -it / 7 else it / 7 } + fadeOut(tween(180)))
            },
            label = "section"
        ) { current ->
            CompositionLocalProvider(LocalNavigationActive provides (current == section)) {
                when (current) {
                Section.HOME -> HomeScreen(
                    api, posts, stories, storiesLoading, storiesError, loading, homeLoadingMore, homeHasNext, error, currentUser,
                    onRetry = { if (homeMode == "latest" && homeRanking == "latest") loadAll() else loadTimeline(homeMode, homeRanking) },
                    selectedMode = homeMode,
                    onModeChange = { if (it != homeMode) loadTimeline(it) },
                    selectedRanking = homeRanking,
                    onRankingChange = { if (it != homeRanking) loadTimeline(homeMode, it) },
                    homeCommunities = homeCommunities,
                    onCommunityAdded = { community ->
                        homeCommunities = (homeCommunities + community).distinctBy(ApiCommunity::id)
                        loadTimeline("community:${community.id}", "latest")
                    },
                    onCommunityRemoved = { community ->
                        homeCommunities = homeCommunities.filterNot { it.id == community.id }
                        if (homeMode == "community:${community.id}") loadTimeline("latest", "latest")
                    },
                    onLoadMore = ::loadMoreHome,
                    unreadCount = unreadCount,
                    onNotifications = { unreadCount = 0; pushOverlay(Overlay.Notifications()) },
                    onCreateStory = { storyCreatorOpen = true },
                    onStory = ::openStory,
                    onOpen = { pushOverlay(Overlay.PostDetail(it)) },
                    onAuthor = { pushOverlay(Overlay.UserDetail(it)) },
                    onReply = { openComposer(parent = it) },
                    onQuote = { openComposer(quote = it) },
                    onRekarot = ::rekarot,
                    onLike = ::like,
                    onBookmark = ::bookmark
                )
                Section.BOARD -> BoardScreen(
                    boards,
                    refreshing = boardsRefreshing,
                    error = boardsError,
                    onRefresh = ::refreshBoards,
                    onOpen = { pushOverlay(Overlay.BoardDetail(it)) },
                    onCreate = { pushOverlay(Overlay.BoardCreate) }
                )
                Section.SEARCH -> DiscoverScreen(api, searchRequest, { openComposer() }, { pushOverlay(Overlay.PostDetail(it)) }, { pushOverlay(Overlay.UserDetail(it.toProfilePost())) }, { openComposer(parent = it) }, { openComposer(quote = it) }, ::rekarot, ::like, ::bookmark)
                Section.COMMUNITY -> CommunityScreen(
                    api = api,
                    currentUser = currentUser,
                    onPost = { pushOverlay(Overlay.PostDetail(it)) },
                    onUser = { pushOverlay(Overlay.UserDetail(it.toProfilePost())) },
                    onReply = { openComposer(parent = it) },
                    onQuote = { openComposer(quote = it) },
                    onRekarot = ::rekarot,
                    onLike = ::like,
                    onBookmark = ::bookmark,
                    onCompose = { openComposer(community = it) },
                    latestCommunityPost = latestCommunityPost
                )
                Section.DM -> DmScreen(api, currentUser) { dmConversationOpen = it }
                Section.PROFILE -> MyPageScreen(currentUser, api, themeKey, followsSystemTheme, onThemeChange, onLogout, onAccountChanged, onAddAccount, { unreadCount = 0; pushOverlay(Overlay.Notifications()) }, { pushOverlay(Overlay.PostDetail(it)) }, { openComposer(parent = it) }, { openComposer(quote = it) }, ::rekarot, ::like, ::bookmark, { openComposer() }, { openComposer(question = it) }, latestCreatedPost) { pushOverlay(Overlay.UserDetail(it.toProfilePost())) }
                }
            }
        }

        if (section == Section.HOME && overlay == null && !composerOpen) {
            Column(
                Modifier.align(Alignment.BottomEnd).padding(end = 20.dp, bottom = safe.calculateBottomPadding() + 78.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    Modifier.size(45.dp).background(Surface, RoundedCornerShape(16.dp)).border(1.dp, Hairline, RoundedCornerShape(16.dp)).clickable { refreshHome() },
                    contentAlignment = Alignment.Center
                ) {
                    if (refreshing) KText("…", 21, Muted, FontWeight.Bold)
                    else CustomIcon(IconType.REFRESH, Carrot, 20.dp)
                }
                Box(
                    Modifier.size(58.dp).background(Carrot, RoundedCornerShape(20.dp)).clickable {
                        val selectedCommunity = homeCommunityId(homeMode)
                            ?.let { id -> homeCommunities.firstOrNull { it.id == id } }
                        openComposer(community = selectedCommunity)
                    },
                    contentAlignment = Alignment.Center
                ) { CustomIcon(IconType.PLUS, Color.White, 25.dp) }
            }
        }

        val overlayKeepsBottomDock = overlay is Overlay.UserDetail || overlay is Overlay.HashtagSearch
        if (!(section == Section.DM && dmConversationOpen)) {
            BottomDock(
                selected = section,
                bottomPadding = safe.calculateBottomPadding(),
                dmHasUnread = dmUnreadCount > 0,
                hazeState = bottomDockHazeState,
                onSelect = ::selectSection,
                modifier = Modifier.align(Alignment.BottomCenter).zIndex(if (overlayKeepsBottomDock) 4f else 0f)
            )
        }

        editingPost?.let { target ->
            EditPostDialog(
                post = target,
                api = api,
                characterLimit = currentUser?.postCharacterLimit ?: 200,
                onDismiss = {
                    editingPost = null
                    editResultHandler = null
                },
                onUpdated = { updated ->
                    posts = posts.map { if (it.id == updated.id) updated else it }
                    homePostCache = homePostCache.mapValues { (_, cached) ->
                        cached.map { if (it.id == updated.id) updated else it }
                    }
                    overlayStack = overlayStack.map { item ->
                        when (item) {
                            is Overlay.PostDetail -> if (item.post.id == updated.id) Overlay.PostDetail(updated) else item
                            else -> item
                        }
                    }
                    if (latestCreatedPost?.id == updated.id) latestCreatedPost = updated
                    editResultHandler?.invoke(updated)
                    editingPost = null
                    editResultHandler = null
                }
            )
        }

        if (storyCreatorOpen) {
            StoryComposerDialog(
                api = api,
                onDismiss = { storyCreatorOpen = false },
                onCreated = { created ->
                    val normalized = if (created.author.id == 0L && currentUser != null) {
                        created.copy(author = currentUser)
                    } else created
                    stories = (listOf(normalized) + stories).distinctBy(ApiStory::id)
                    storiesError = null
                    storyCreatorOpen = false
                }
            )
        }

        AnimatedVisibility(
            visible = composerOpen,
            enter = fadeIn(tween(180)) + scaleIn(spring(dampingRatio = .82f), initialScale = .92f),
            exit = fadeOut(tween(150)) + scaleOut(tween(180), targetScale = .96f)
        ) {
            Composer(
                api = api,
                onClose = { composerOpen = false },
                currentUser = currentUser,
                parent = composeTarget.parent,
                quote = composeTarget.quote,
                community = composeTarget.community,
                question = composeTarget.question,
                onSubmit = { text, selectedMedia, pollOptions, pollDurationHours, settings, done ->
                    scope.launch {
                        val result = withContext(Dispatchers.IO) {
                            runCatching {
                                val uploads = selectedMedia.map { item ->
                                    val bytes = context.contentResolver.openInputStream(item.uri)?.use { it.readBytes() }
                                        ?: throw IllegalStateException("${item.name}を読み込めませんでした")
                                    ApiUploadMedia(item.name, item.mimeType, bytes)
                                }
                                api.createPost(
                                    content = text,
                                    questionId = composeTarget.question?.id,
                                    parentId = composeTarget.parent?.id,
                                    quotedPostId = composeTarget.quote?.id,
                                    communityId = settings.communityId,
                                    media = uploads,
                                    pollOptions = pollOptions,
                                    pollDurationHours = pollDurationHours,
                                    visibility = settings.visibility,
                                    replyRestriction = settings.replyRestriction,
                                    viewerCircleId = settings.viewerCircleId,
                                    replyCircleId = settings.replyCircleId,
                                    minimumAge = settings.minimumAge,
                                    maximumAge = settings.maximumAge,
                                    isAiGenerated = settings.isAiGenerated,
                                    isPromotional = settings.isPromotional,
                                    isR18 = settings.isR18,
                                    hideFromMinors = settings.hideFromMinors,
                                    scheduledFor = settings.scheduledFor,
                                    expiresAt = settings.expiresAt
                                )
                            }.getOrElse { ApiResult.Failure(it.message ?: "添付ファイルを読み込めませんでした") }
                        }
                        when (result) {
                            is ApiResult.Success -> {
                                val created = result.value.toUiPost()
                                if (composeTarget.parent == null && settings.scheduledFor == null) {
                                    val targetCommunityId = settings.communityId
                                    if (targetCommunityId == null) {
                                        posts = listOf(created) + posts
                                        latestCreatedPost = created
                                    } else {
                                        latestCommunityPost = targetCommunityId to created
                                        if (homeMode == "community:$targetCommunityId") {
                                            posts = (listOf(created) + posts).distinctBy(::postStableKey)
                                        }
                                    }
                                }
                                composerOpen = false
                                done(null)
                                if (composeTarget.parent != null) pushOverlay(Overlay.PostDetail(composeTarget.parent!!))
                            }
                            is ApiResult.Failure -> done(result.message)
                        }
                    }
                }
            )
        }

        AnimatedVisibility(
            visible = notificationToastVisible && notificationToast != null && !composerOpen,
            modifier = Modifier.align(Alignment.TopCenter).zIndex(12f),
            enter = slideInVertically(tween(320, easing = FastOutSlowInEasing)) { -it } + fadeIn(tween(180)),
            exit = slideOutVertically(tween(280, easing = FastOutSlowInEasing)) { -it } + fadeOut(tween(210))
        ) {
            notificationToast?.let { notification ->
                NotificationToastCard(
                    notification = notification,
                    onClick = {
                        notificationToastVisible = false
                        notification.post?.let { pushOverlay(Overlay.PostDetail(it.toUiPost())) }
                            ?: pushOverlay(Overlay.Notifications())
                    },
                    onDismiss = { notificationToastVisible = false }
                )
            }
        }

        AnimatedVisibility(
            visible = overlay != null && !composerOpen,
            modifier = Modifier.hazeSource(state = bottomDockHazeState, zIndex = 2f),
            enter = slideInHorizontally(tween(360)) { it } + fadeIn(),
            exit = slideOutHorizontally(tween(280)) { it } + fadeOut()
        ) {
            BackHandler(enabled = overlayStack.isNotEmpty()) { popOverlay() }
            when (val target = overlay) {
                is Overlay.PostDetail -> PostDetailScreen(target.post, api, ::popOverlay, { openComposer(parent = it) }, { openComposer(quote = it) }, ::rekarot, ::like, ::bookmark, { pushOverlay(Overlay.UserDetail(it)) })
                is Overlay.UserDetail -> UserDetailScreen(target.post, target.retainedState, api, currentUser?.id, ::popOverlay, { pushOverlay(Overlay.PostDetail(it)) }, { openComposer(parent = it) }, { openComposer(quote = it) }, ::rekarot, ::like, ::bookmark, { openComposer() }, ::startDirectMessage) { pushOverlay(Overlay.UserDetail(it.toProfilePost())) }
                is Overlay.StoryDetail -> StoryDetailScreen(
                    story = target.story,
                    transitionDirection = storyTransitionDirection,
                    api = api,
                    viewerUserId = currentUser?.id,
                    onBack = ::popOverlay,
                    onNext = { nextStory(target.story) },
                    onPrevious = { previousStory(target.story) },
                    onUser = { profile -> pushOverlay(Overlay.UserDetail(profile.toProfilePost())) },
                    onUpdated = { updated ->
                        stories = stories.map { if (it.id == updated.id) updated else it }
                    },
                    onDeleted = {
                        stories = stories.filterNot { it.id == target.story.id }
                        popOverlay()
                    }
                )
                is Overlay.BoardDetail -> BoardDetailScreen(
                    initial = target.board,
                    api = api,
                    viewerUserId = currentUser?.id,
                    onBack = ::popOverlay,
                    onDeleted = { slug ->
                        boards = boards.filterNot { it.slug == slug }
                        popOverlay()
                    }
                )
                Overlay.BoardCreate -> BoardCreateScreen(
                    api = api,
                    onBack = ::popOverlay,
                    onCreated = { created ->
                        boards = (boards + created).distinctBy { it.slug }
                        popOverlay()
                    }
                )
                is Overlay.DmConversation -> DmConversationScreen(
                    api = api,
                    group = target.group,
                    currentUser = currentUser,
                    onBack = ::popOverlay,
                    onLeft = ::popOverlay
                )
                is Overlay.HashtagSearch -> HashtagSearchScreen(target.tag, api, ::popOverlay, { pushOverlay(Overlay.PostDetail(it)) }, { openComposer(parent = it) }, { openComposer(quote = it) }, ::rekarot, ::like, ::bookmark, { pushOverlay(Overlay.UserDetail(it.toProfilePost())) }, { openComposer() })
                is Overlay.Notifications -> NotificationsScreen(api, target.retainedState, ::popOverlay) { pushOverlay(Overlay.PostDetail(it)) }
                null -> Unit
            }
        }
    }
    }
}

private fun ApiPost.toUiPost(): Post = Post(
    authorId = author.id,
    id = id,
    name = author.displayName,
    handle = "@${author.username}",
    time = relativeTime(createdAt),
    createdAt = createdAt,
    text = content,
    avatar = avatarColor(author.id),
    avatarUrl = author.avatarUrl,
    replies = repliesCount,
    likes = likesCount,
    rekarots = rekarotsCount,
    tag = null,
    featured = mediaUrl != null,
    initiallyLiked = liked,
    initiallyRekaroted = rekaroted,
    initiallyBookmarked = bookmarked,
    quotedPost = quotedPost?.toUiPost(),
    media = media,
    mediaUrl = mediaUrl,
    rekarotedBy = rekarotedBy,
    officialMark = author.officialMark,
    officialMarks = author.officialMarks,
    isBotAccount = author.isBotAccount,
    isParodyAccount = author.isParodyAccount,
    isPrivateAccount = author.isPrivate,
    subscriptionPlan = author.subscriptionPlan,
    subscriptionStatus = author.subscriptionStatus,
    showSubscriptionBadges = author.showSubscriptionBadges,
    showPlusBadge = author.showPlusBadge,
    showProBadge = author.showProBadge,
    premiumBadgeColor = author.premiumBadgeColor,
    showCardDecoration = author.showCardDecoration,
    cardAccentColor = author.cardAccentColor,
    reactions = reactions,
    poll = poll,
    visibility = visibility,
    viewerCircleId = viewerCircleId,
    viewerCircleName = viewerCircleName,
    replyRestriction = replyRestriction,
    replyCircleId = replyCircleId,
    replyCircleName = replyCircleName,
    minimumAge = minimumAge,
    maximumAge = maximumAge,
    isAiGenerated = isAiGenerated,
    isPromotional = isPromotional,
    isR18 = isR18,
    expiresAt = expiresAt,
    viewsCount = viewsCount,
    bookmarksCount = bookmarksCount,
    quoteUsersCount = quoteUsersCount,
    parentId = parentId,
    canQuote = canQuote
)

private fun ApiUser.toProfilePost() = Post(
    authorId = id, name = displayName.ifBlank { username }, handle = "@$username", time = "", text = bio,
    avatar = avatarColor(id), avatarUrl = avatarUrl, replies = 0, likes = 0, officialMark = officialMark, officialMarks = officialMarks,
    isBotAccount = isBotAccount, isParodyAccount = isParodyAccount, isPrivateAccount = isPrivate,
    subscriptionPlan = subscriptionPlan, subscriptionStatus = subscriptionStatus,
    showSubscriptionBadges = showSubscriptionBadges, showPlusBadge = showPlusBadge, showProBadge = showProBadge,
    premiumBadgeColor = premiumBadgeColor, showCardDecoration = showCardDecoration, cardAccentColor = cardAccentColor
)

private fun Post.toApiUser() = ApiUser(
    id = authorId, username = handle.removePrefix("@"), displayName = name, avatarUrl = avatarUrl,
    headerUrl = null, bio = "", websiteUrl = null, showLikedPosts = false, followersCount = 0, followingCount = 0, postsCount = 0, officialMark = officialMark,
    isBotAccount = isBotAccount, isParodyAccount = isParodyAccount, isPrivate = isPrivateAccount,
    subscriptionPlan = subscriptionPlan, subscriptionStatus = subscriptionStatus,
    showSubscriptionBadges = showSubscriptionBadges, showPlusBadge = showPlusBadge, showProBadge = showProBadge,
    premiumBadgeColor = premiumBadgeColor, showCardDecoration = showCardDecoration, cardAccentColor = cardAccentColor,
    officialMarks = officialMarks
)

private fun avatarColor(id: Long): Color {
    val colors = listOf(Color(0xFFFFC9B9), Color(0xFFCBE7DB), Color(0xFFC9DAFA), Color(0xFFE9D4EF), Lemon)
    return colors[Math.floorMod(id.hashCode(), colors.size)]
}
private fun parseTimestamp(value: String): Instant? = runCatching { Instant.parse(value) }.getOrNull()
    ?: runCatching { OffsetDateTime.parse(value).toInstant() }.getOrNull()

private fun relativeTime(iso: String): String {
    val instant = parseTimestamp(iso) ?: return if (iso.isBlank()) "いま" else iso.take(10).replace('-', '.')
    val seconds = Duration.between(instant, RelativeTimeReference).seconds.coerceAtLeast(0)
    return when {
        seconds < 60 -> "${seconds}秒前"
        seconds < 3_600 -> "${seconds / 60}分前"
        seconds < 86_400 -> "${seconds / 3_600}時間前"
        seconds < 604_800 -> "${seconds / 86_400}日前"
        seconds < 2_592_000 -> "${seconds / 604_800}週間前"
        seconds < 31_536_000 -> "${seconds / 2_592_000}か月前"
        else -> "${seconds / 31_536_000}年前"
    }
}

private fun absoluteTime(iso: String): String {
    val instant = parseTimestamp(iso) ?: return iso.take(10).replace('-', '.')
    return DateTimeFormatter.ofPattern("yyyy年M月d日 H:mm", Locale.JAPAN)
        .withZone(ZoneId.systemDefault())
        .format(instant)
}

private fun dmClockTime(iso: String): String {
    val instant = parseTimestamp(iso) ?: return ""
    return DateTimeFormatter.ofPattern("H:mm", Locale.JAPAN)
        .withZone(ZoneId.systemDefault())
        .format(instant)
}

private fun joinedDate(iso: String): String {
    val instant = parseTimestamp(iso)
    if (instant != null) {
        return DateTimeFormatter.ofPattern("yyyy年M月d日", Locale.JAPAN)
            .withZone(ZoneId.systemDefault())
            .format(instant)
    }
    val parts = iso.take(10).split('-')
    return if (parts.size == 3) {
        val year = parts[0].toIntOrNull()
        val month = parts[1].toIntOrNull()
        val day = parts[2].toIntOrNull()
        if (year != null && month != null && day != null) "${year}年${month}月${day}日" else iso.take(10)
    } else iso.take(10)
}

@Composable
private fun WelcomeScreen(
    systemDark: Boolean,
    onThemeSelectionChange: (String, String) -> Unit,
    onFinished: () -> Unit
) {
    val context = LocalContext.current
    var page by remember { mutableIntStateOf(0) }
    var direction by remember { mutableIntStateOf(1) }
    var selectedThemeMode by remember { mutableStateOf("system") }
    var selectedThemeFamily by remember { mutableStateOf("snow") }
    var backgroundNotifications by remember {
        mutableStateOf(BackgroundNotificationManager.isEnabled(context))
    }
    var notificationPermission by remember {
        mutableStateOf(
            Build.VERSION.SDK_INT < 33 ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        )
    }
    val powerManager = remember { context.getSystemService(PowerManager::class.java) }
    var backgroundPermission by remember {
        mutableStateOf(powerManager?.isIgnoringBatteryOptimizations(context.packageName) == true)
    }
    val backgroundPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        backgroundPermission = powerManager?.isIgnoringBatteryOptimizations(context.packageName) == true
    }
    fun requestBackgroundPermission() {
        val directRequest = Intent(
            Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            Uri.parse("package:${context.packageName}")
        )
        runCatching { backgroundPermissionLauncher.launch(directRequest) }
            .onFailure {
                backgroundPermissionLauncher.launch(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
            }
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        notificationPermission = granted
        if (granted) {
            requestBackgroundPermission()
        } else {
            backgroundNotifications = false
            BackgroundNotificationManager.setEnabled(context, false)
        }
    }
    fun setPage(next: Int) {
        direction = if (next >= page) 1 else -1
        page = next.coerceIn(0, 3)
    }
    fun toggleNotifications() {
        val desired = !backgroundNotifications
        backgroundNotifications = desired
        BackgroundNotificationManager.setEnabled(context, desired)
        if (desired && Build.VERSION.SDK_INT >= 33 && !notificationPermission) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else if (desired) {
            requestBackgroundPermission()
        }
    }

    BackHandler(enabled = page > 0) { setPage(page - 1) }
    Column(
        Modifier.fillMaxSize().background(Paper).statusBarsPadding().navigationBarsPadding()
            .padding(horizontal = 22.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KText("KAROHA", 12, Ink, FontWeight.Black, letterSpacing = 3.2f)
            Spacer(Modifier.weight(1f))
            KText("${page + 1} / 4", 10, Muted, FontWeight.Bold)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(4) { index ->
                Box(
                    Modifier.weight(1f).height(3.dp)
                        .background(if (index <= page) Carrot else Hairline, RoundedCornerShape(2.dp))
                )
            }
        }
        AnimatedContent(
            targetState = page,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            transitionSpec = {
                (slideInHorizontally(tween(420, easing = FastOutSlowInEasing)) { direction * it / 3 } + fadeIn(tween(240)))
                    .togetherWith(slideOutHorizontally(tween(300)) { -direction * it / 5 } + fadeOut(tween(170)))
            },
            label = "welcome-page"
        ) { currentPage ->
            when (currentPage) {
                0 -> Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                    KarohaAppLogo(92.dp)
                    Spacer(Modifier.height(30.dp))
                    KText("ようこそ、Karohaへ。", 31, Ink, FontWeight.Black, lineHeight = 39f)
                    Spacer(Modifier.height(13.dp))
                    KText(
                        "Karotterを、もっと軽やかに。\n投稿、ストーリー、掲示板、DMをひとつの場所で。",
                        14,
                        Muted,
                        FontWeight.Medium,
                        lineHeight = 23f
                    )
                    Spacer(Modifier.height(28.dp))
                    KText("UNOFFICIAL CLIENT  ·  BY NAMICODE", 9, Carrot, FontWeight.Black, letterSpacing = 1.4f)
                }
                1 -> Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                    KText("ひとつの流れに、\n必要なものを全部。", 28, Ink, FontWeight.Black, lineHeight = 36f)
                    Spacer(Modifier.height(25.dp))
                    listOf(
                        Triple(IconType.HOME, "タイムライン", "みんな・フォロー中・トレンドを滑らかに切り替え"),
                        Triple(IconType.DM, "メッセージ", "1対1もグループも、画像付きで会話"),
                        Triple(IconType.BOARD, "掲示板とストーリー", "Karotterの多彩な機能をそのまま")
                    ).forEach { (icon, title, text) ->
                        Row(
                            Modifier.fillMaxWidth().padding(vertical = 7.dp)
                                .background(Surface, RoundedCornerShape(19.dp))
                                .border(1.dp, Hairline, RoundedCornerShape(19.dp)).padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(Modifier.size(43.dp).background(PaleCarrot, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                                CustomIcon(icon, Carrot, 19.dp)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                KText(title, 13, Ink, FontWeight.Black)
                                Spacer(Modifier.height(3.dp))
                                KText(text, 10, Muted, lineHeight = 16f)
                            }
                        }
                    }
                }
                2 -> Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                    val previewDark = when (selectedThemeMode) {
                        "dark" -> true
                        "light" -> false
                        else -> systemDark
                    }
                    val previewPalette = ThemePalettes.getValue(selectedThemeFamily + if (previewDark) "-dark" else "")
                    KText("最初の色を選ぶ。", 28, Ink, FontWeight.Black)
                    Spacer(Modifier.height(8.dp))
                    KText("色と明るさは、あとからいつでも変更できます。", 12, Muted, lineHeight = 19f)
                    Spacer(Modifier.height(16.dp))
                    Box(
                        Modifier.fillMaxWidth().height(112.dp)
                            .background(previewPalette.paper, RoundedCornerShape(24.dp))
                            .border(1.dp, Hairline, RoundedCornerShape(24.dp)).padding(18.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(35.dp).background(previewPalette.accent, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                                    KText("K", 14, Color.White, FontWeight.Black)
                                }
                                Spacer(Modifier.width(10.dp))
                                Column {
                                    KText(selectedThemeFamily.replaceFirstChar { it.uppercase() }, 14, previewPalette.ink, FontWeight.Black)
                                    KText(if (previewDark) "Dark" else "Light", 9, previewPalette.muted)
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                        listOf("paper", "snow", "mist").forEach { family ->
                            val selected = selectedThemeFamily == family
                            val palette = ThemePalettes.getValue(family + if (previewDark) "-dark" else "")
                            Column(
                                Modifier.weight(1f).clip(RoundedCornerShape(15.dp))
                                    .background(if (selected) Strong else Surface)
                                    .border(1.dp, if (selected) Strong else Hairline, RoundedCornerShape(15.dp))
                                    .clickable {
                                        selectedThemeFamily = family
                                        onThemeSelectionChange(family, selectedThemeMode)
                                    }.padding(vertical = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy((-4).dp)) {
                                    listOf(palette.paper, palette.ink, palette.accent).forEach { color ->
                                        Box(Modifier.size(15.dp).background(color, CircleShape).border(1.dp, Hairline, CircleShape))
                                    }
                                }
                                Spacer(Modifier.height(5.dp))
                                KText(family.replaceFirstChar { it.uppercase() }, 9, if (selected) OnStrong else Ink, FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(Modifier.height(9.dp))
                    listOf(
                        Triple("system", "端末に合わせる", if (systemDark) "現在はダーク" else "現在はライト"),
                        Triple("light", "ライト", "常に明るく表示"),
                        Triple("dark", "ダーク", "常に暗く表示")
                    ).forEach { (mode, title, description) ->
                        val selected = selectedThemeMode == mode
                        Row(
                            Modifier.fillMaxWidth().padding(vertical = 5.dp)
                                .clip(RoundedCornerShape(17.dp))
                                .background(if (selected) Strong else Surface)
                                .border(1.dp, if (selected) Strong else Hairline, RoundedCornerShape(17.dp))
                                .clickable {
                                    selectedThemeMode = mode
                                    onThemeSelectionChange(selectedThemeFamily, mode)
                                }.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomIcon(if (mode == "dark") IconType.THEME else IconType.CHECK, if (selected) Carrot else Muted, 17.dp)
                            Spacer(Modifier.width(11.dp))
                            Column(Modifier.weight(1f)) {
                                KText(title, 12, if (selected) OnStrong else Ink, FontWeight.Bold)
                                KText(description, 9, if (selected) OnStrong.copy(.6f) else Muted)
                            }
                            if (selected) CustomIcon(IconType.CHECK, Carrot, 16.dp)
                        }
                    }
                }
                else -> Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                    KText("通知を、見逃さない。", 28, Ink, FontWeight.Black)
                    Spacer(Modifier.height(9.dp))
                    KText("アプリを閉じている間だけ、15秒ごとに新着を確認します。", 12, Muted, lineHeight = 19f)
                    Spacer(Modifier.height(25.dp))
                    Row(
                        Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(22.dp))
                            .border(1.dp, Hairline, RoundedCornerShape(22.dp))
                            .clickable { toggleNotifications() }.padding(17.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.size(45.dp).background(PaleCarrot, RoundedCornerShape(15.dp)), contentAlignment = Alignment.Center) {
                            CustomIcon(IconType.BELL, Carrot, 20.dp)
                        }
                        Spacer(Modifier.width(13.dp))
                        Column(Modifier.weight(1f)) {
                            KText("システム通知", 14, Ink, FontWeight.Black)
                            Spacer(Modifier.height(3.dp))
                            KText(
                                if (backgroundNotifications && notificationPermission) "バックグラウンド通知は有効です"
                                else if (backgroundNotifications) "通知の許可を確認しています"
                                else "あとからマイページでも設定できます",
                                10,
                                Muted,
                                lineHeight = 16f
                            )
                        }
                        NotificationToggle(backgroundNotifications && notificationPermission)
                    }
                    Spacer(Modifier.height(14.dp))
                    Row(
                        Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(19.dp))
                            .border(1.dp, Hairline, RoundedCornerShape(19.dp))
                            .clickable { requestBackgroundPermission() }.padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.size(40.dp).background(PaleCarrot, RoundedCornerShape(13.dp)), contentAlignment = Alignment.Center) {
                            CustomIcon(IconType.THEME, if (backgroundPermission) Carrot else Muted, 18.dp)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            KText("バックグラウンド動作", 13, Ink, FontWeight.Bold)
                            KText(
                                if (backgroundPermission) "バッテリー制限の対象外です" else "Androidの許可画面で制限を解除",
                                9,
                                if (backgroundPermission) Carrot else Muted
                            )
                        }
                        if (backgroundPermission) CustomIcon(IconType.CHECK, Carrot, 16.dp)
                        else CustomIcon(IconType.FORWARD, Muted, 16.dp)
                    }
                    Spacer(Modifier.height(14.dp))
                    Column(Modifier.fillMaxWidth().background(PaleCarrot, RoundedCornerShape(18.dp)).padding(15.dp)) {
                        KText("プライバシー", 10, Ink, FontWeight.Black)
                        Spacer(Modifier.height(5.dp))
                        KText(
                            "通知確認には現在ログイン中のアカウントを使用します。アプリを開いている間はバックグラウンド確認を停止します。",
                            10,
                            Muted,
                            lineHeight = 17f
                        )
                    }
                }
            }
        }
        Row(
            Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (page > 0) {
                Box(
                    Modifier.size(50.dp).border(1.dp, Hairline, RoundedCornerShape(17.dp))
                        .clickable { setPage(page - 1) },
                    contentAlignment = Alignment.Center
                ) { CustomIcon(IconType.BACK, Ink, 20.dp) }
                Spacer(Modifier.width(10.dp))
            }
            Box(
                Modifier.weight(1f).height(50.dp).background(Carrot, RoundedCornerShape(17.dp))
                    .clickable {
                        if (page < 3) setPage(page + 1)
                        else onFinished()
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    KText(if (page < 3) "次へ" else "Karohaをはじめる", 12, Color.White, FontWeight.Black)
                    Spacer(Modifier.width(8.dp))
                    CustomIcon(if (page < 3) IconType.FORWARD else IconType.CHECK, Color.White, 17.dp)
                }
            }
        }
    }
}

@Composable
private fun KarohaAppLogo(size: Dp) {
    val iconBackground = Color(0xFF0B0E14)
    val iconForeground = Color(0xFFF2F5FA)
    val iconAccent = Color(0xFF6E98FF)
    val darkTheme = Paper.luminance() < .5f
    val themeShadow = Carrot.copy(alpha = if (darkTheme) .72f else .30f)
    val shape = RoundedCornerShape(size * .3f)
    Canvas(
        Modifier
            .size(size)
            .shadow(
                elevation = if (darkTheme) 22.dp else 11.dp,
                shape = shape,
                ambientColor = themeShadow,
                spotColor = themeShadow
            )
            .clip(shape)
            .background(iconBackground)
            .border(
                width = 1.dp,
                color = Carrot.copy(alpha = if (darkTheme) .42f else .18f),
                shape = shape
            )
    ) {
        val strokeWidth = this.size.width * (5f / 108f)
        fun point(x: Float, y: Float) = Offset(
            this.size.width * (x / 108f),
            this.size.height * (y / 108f)
        )
        drawLine(
            iconForeground,
            point(42f, 36f),
            point(42f, 72f),
            strokeWidth,
            StrokeCap.Round
        )
        drawLine(
            iconForeground,
            point(68f, 38f),
            point(43f, 57f),
            strokeWidth,
            StrokeCap.Round
        )
        drawLine(
            iconAccent,
            point(52f, 51f),
            point(68f, 71f),
            strokeWidth,
            StrokeCap.Round
        )
        drawCircle(
            iconAccent,
            this.size.width * (3f / 108f),
            point(68f, 71f)
        )
    }
}

@Composable
private fun AppUpdateDialog(
    update: AppUpdateInfo,
    downloading: Boolean,
    progress: Float,
    error: String?,
    onDismiss: () -> Unit,
    onInstall: () -> Unit
) {
    Dialog(
        onDismissRequest = { if (!downloading) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(horizontal = 18.dp).navigationBarsPadding()
                .clip(RoundedCornerShape(28.dp)).background(Paper)
                .border(1.dp, Hairline, RoundedCornerShape(28.dp)).padding(22.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                KarohaAppLogo(52.dp)
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    KText("KAROHA UPDATE", 9, Carrot, FontWeight.Black, letterSpacing = 1.8f)
                    Spacer(Modifier.height(3.dp))
                    KText("新しいKarohaがあります", 19, Ink, FontWeight.Black)
                }
            }
            Spacer(Modifier.height(19.dp))
            Box(
                Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(18.dp))
                    .border(1.dp, Hairline, RoundedCornerShape(18.dp)).padding(15.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        KText(update.title.ifBlank { update.tagName }, 14, Ink, FontWeight.Black, modifier = Modifier.weight(1f))
                        if (update.apkSize > 0L) {
                            KText("%.1f MB".format(update.apkSize / 1024f / 1024f), 9, Muted, FontWeight.Bold)
                        }
                    }
                    if (update.notes.isNotBlank()) {
                        Spacer(Modifier.height(9.dp))
                        KText(update.notes, 10, Muted, lineHeight = 16f, maxLines = 5)
                    }
                }
            }
            if (downloading) {
                Spacer(Modifier.height(17.dp))
                val shownProgress = progress.coerceIn(0f, 1f)
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    KText("ダウンロード中", 10, Ink, FontWeight.Black, modifier = Modifier.weight(1f))
                    KText("${(shownProgress * 100).roundToInt()}%", 10, Carrot, FontWeight.Black)
                }
                Spacer(Modifier.height(8.dp))
                Box(Modifier.fillMaxWidth().height(7.dp).clip(CircleShape).background(Hairline)) {
                    Box(
                        Modifier.fillMaxWidth(shownProgress.coerceAtLeast(.015f)).fillMaxHeight()
                            .clip(CircleShape).background(Carrot)
                    )
                }
            }
            if (!error.isNullOrBlank()) {
                Spacer(Modifier.height(13.dp))
                KText(error, 10, Color(0xFFE05252), FontWeight.Bold, lineHeight = 16f)
            }
            Spacer(Modifier.height(19.dp))
            Box(
                Modifier.fillMaxWidth().height(50.dp).clip(RoundedCornerShape(16.dp))
                    .background(if (downloading) Muted.copy(alpha = .35f) else Strong)
                    .clickable(enabled = !downloading) { onInstall() },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CustomIcon(IconType.SEND, OnStrong, 18.dp)
                    Spacer(Modifier.width(8.dp))
                    KText(if (error == null) "ダウンロードして更新" else "もう一度試す", 11, OnStrong, FontWeight.Black)
                }
            }
            if (!downloading) {
                Spacer(Modifier.height(13.dp))
                KText(
                    "あとで", 10, Muted, FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally).clickable { onDismiss() }.padding(6.dp)
                )
            }
        }
    }
}

@Composable
private fun CreatorFollowDialog(
    profile: ApiUser,
    following: Boolean,
    busy: Boolean,
    error: String?,
    onDismiss: () -> Unit,
    onFollow: () -> Unit
) {
    Dialog(
        onDismissRequest = { if (!busy) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(horizontal = 18.dp).navigationBarsPadding()
                .clip(RoundedCornerShape(28.dp)).background(Paper)
                .border(1.dp, Hairline, RoundedCornerShape(28.dp)).padding(22.dp)
        ) {
            KText("KAROHA NEWS", 9, Carrot, FontWeight.Black, letterSpacing = 1.8f)
            Spacer(Modifier.height(7.dp))
            KText("Karohaの最新情報を受け取る", 21, Ink, FontWeight.Black)
            Spacer(Modifier.height(7.dp))
            KText("アップデートや新機能のお知らせを見逃さないよう、NamiCodeのフォローをおすすめします。", 10, Muted, lineHeight = 17f)
            Spacer(Modifier.height(18.dp))
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Surface)
                    .border(1.dp, Hairline, RoundedCornerShape(20.dp)).padding(15.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    Modifier.size(54.dp).clip(RoundedCornerShape(17.dp)).background(PaleCarrot),
                    contentAlignment = Alignment.Center
                ) {
                    if (!profile.avatarUrl.isNullOrBlank()) {
                        AsyncImage(profile.avatarUrl, profile.displayName, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    } else {
                        KText(profile.displayName.take(1), 18, Carrot, FontWeight.Black)
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        KText(profile.displayName, 14, Ink, FontWeight.Black, maxLines = 1)
                        AccountMarks(
                            profile.officialMarks.ifEmpty { listOf(profile.officialMark) },
                            profile.isBotAccount,
                            profile.isParodyAccount,
                            profile.isPrivate,
                            compact = true
                        )
                    }
                    KText("@${profile.username}", 10, Muted, FontWeight.Bold, maxLines = 1)
                    if (profile.bio.isNotBlank()) {
                        Spacer(Modifier.height(7.dp))
                        KText(profile.bio, 10, Ink, lineHeight = 15f, maxLines = 3)
                    }
                    Spacer(Modifier.height(7.dp))
                    KText("${profile.followersCount} フォロワー", 9, Muted, FontWeight.Bold)
                }
            }
            if (!error.isNullOrBlank()) {
                Spacer(Modifier.height(12.dp))
                KText(error, 10, Color(0xFFE05252), FontWeight.Bold, lineHeight = 16f)
            }
            Spacer(Modifier.height(18.dp))
            Box(
                Modifier.fillMaxWidth().height(50.dp).clip(RoundedCornerShape(16.dp))
                    .background(if (following) Surface else Strong)
                    .border(if (following) 1.dp else 0.dp, if (following) Hairline else Color.Transparent, RoundedCornerShape(16.dp))
                    .clickable(enabled = !busy && !following) { onFollow() },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CustomIcon(if (following) IconType.CHECK else IconType.PERSON, if (following) Ink else OnStrong, 18.dp)
                    Spacer(Modifier.width(8.dp))
                    KText(
                        when {
                            busy -> "フォローしています…"
                            following -> "フォロー中"
                            else -> "NamiCodeをフォロー"
                        },
                        11, if (following) Ink else OnStrong, FontWeight.Black
                    )
                }
            }
            Spacer(Modifier.height(13.dp))
            KText(
                if (following) "閉じる" else "今はしない", 10, Muted, FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .clickable(enabled = !busy) { onDismiss() }.padding(6.dp)
            )
        }
    }
}

@Composable
private fun LaunchScreen(exiting: Boolean = false) {
    val reveal = remember { Animatable(0f) }
    LaunchedEffect(exiting) {
        reveal.animateTo(
            if (exiting) 0f else 1f,
            tween(
                durationMillis = if (exiting) 320 else 430,
                easing = FastOutSlowInEasing
            )
        )
    }
    Box(Modifier.fillMaxSize().background(Paper), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .offset(y = ((1f - reveal.value) * (-9f)).dp)
                .scale(.86f + reveal.value * .14f)
                .alpha(reveal.value)
        ) {
            Canvas(Modifier.size(58.dp)) {
                val width = 5.dp.toPx()
                drawLine(Ink, Offset(size.width * .24f, size.height * .12f), Offset(size.width * .24f, size.height * .88f), width, StrokeCap.Round)
                drawLine(Ink, Offset(size.width * .75f, size.height * .15f), Offset(size.width * .25f, size.height * .56f), width, StrokeCap.Round)
                drawLine(Carrot, Offset(size.width * .42f, size.height * .43f), Offset(size.width * .78f, size.height * .88f), width, StrokeCap.Round)
                drawCircle(Carrot, 3.5.dp.toPx(), Offset(size.width * .78f, size.height * .88f))
            }
            Spacer(Modifier.height(22.dp))
            KText("KAROHA", 13, Ink, FontWeight.Black, letterSpacing = 4.4f)
            Spacer(Modifier.height(7.dp))
            KText("FOR KAROTTER", 8, Muted, FontWeight.Bold, letterSpacing = 1.8f)
            Spacer(Modifier.height(5.dp))
            KText("BY NAMICODE", 8, Carrot, FontWeight.Black, letterSpacing = 1.8f)
        }
    }
}

@Composable
private fun NoNetworkScreen(onRetry: () -> Unit) {
    Column(
        Modifier.fillMaxSize().background(Paper).padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier.size(70.dp).background(Surface, RoundedCornerShape(23.dp))
                .border(1.dp, Hairline, RoundedCornerShape(23.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(Modifier.size(34.dp)) {
                val stroke = Stroke(2.2.dp.toPx(), cap = StrokeCap.Round)
                drawArc(Muted, 220f, 100f, false, Offset(size.width*.12f, size.height*.2f), Size(size.width*.76f, size.height*.55f), style = stroke)
                drawArc(Muted, 220f, 100f, false, Offset(size.width*.28f, size.height*.4f), Size(size.width*.44f, size.height*.34f), style = stroke)
                drawCircle(Muted, 2.4.dp.toPx(), Offset(size.width*.5f, size.height*.82f))
                drawLine(Carrot, Offset(size.width*.13f, size.height*.12f), Offset(size.width*.87f, size.height*.88f), strokeWidth = 2.8.dp.toPx(), cap = StrokeCap.Round)
            }
        }
        Spacer(Modifier.height(24.dp))
        KText("ネット接続がありません", 21, Ink, FontWeight.Black)
        Spacer(Modifier.height(9.dp))
        KText("接続が戻るまでデータ取得は行いません。\nログイン情報はそのまま保持されます。", 12, Muted, lineHeight = 19f)
        Spacer(Modifier.height(22.dp))
        Row(
            Modifier.clip(RoundedCornerShape(15.dp)).background(Strong)
                .clickable { onRetry() }.padding(horizontal = 22.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomIcon(IconType.REFRESH, OnStrong, 17.dp)
            Spacer(Modifier.width(8.dp))
            KText("再試行", 11, OnStrong, FontWeight.Black)
        }
        Spacer(Modifier.height(13.dp))
        KText("接続の復旧は自動でも検出します", 9, Muted, FontWeight.Bold)
    }
}

@Composable
private fun DataFailureRecoveryScreen(
    message: String,
    accounts: List<SavedCredentialAccount>,
    busyIdentifier: String?,
    onSwitchAccount: (String) -> Unit,
    onRelogin: () -> Unit,
    onRetry: () -> Unit
) {
    LazyColumn(
        Modifier.fillMaxSize().background(Paper),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 34.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            KText("CONNECTION", 10, Carrot, FontWeight.Black, letterSpacing = 2f)
            Spacer(Modifier.height(13.dp))
            KText("データを取得\nできませんでした。", 29, Ink, FontWeight.Black, lineHeight = 36f)
            Spacer(Modifier.height(13.dp))
            Box(
                Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(17.dp))
                    .border(1.dp, Hairline, RoundedCornerShape(17.dp)).padding(15.dp)
            ) {
                KText(message, 11, Muted, lineHeight = 18f)
            }
        }
        if (accounts.isNotEmpty()) {
            item {
                KText("別のアカウントに切り替える", 12, Ink, FontWeight.Black, modifier = Modifier.padding(top = 12.dp, bottom = 2.dp))
            }
            items(accounts, key = { it.identifier }) { account ->
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(17.dp)).background(Surface)
                        .border(1.dp, Hairline, RoundedCornerShape(17.dp))
                        .clickable(enabled = busyIdentifier == null) { onSwitchAccount(account.identifier) }
                        .padding(13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.size(44.dp).background(PaleCarrot, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!account.avatarUrl.isNullOrBlank()) {
                            AsyncImage(account.avatarUrl, account.displayName, Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp)), contentScale = ContentScale.Crop)
                        } else KText(account.displayName.take(1).uppercase(), 15, Ink, FontWeight.Black)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        KText(account.displayName.ifBlank { account.identifier }, 13, Ink, FontWeight.Bold, maxLines = 1)
                        if (account.username.isNotBlank()) KText("@${account.username}", 10, Muted, maxLines = 1)
                    }
                    KText(if (busyIdentifier == account.identifier) "切替中…" else "切り替え", 10, Carrot, FontWeight.Black)
                }
            }
        }
        item {
            Spacer(Modifier.height(4.dp))
            Box(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(17.dp)).background(Strong)
                    .clickable(enabled = busyIdentifier == null) { onRelogin() }.padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                KText("ログインしなおす", 12, OnStrong, FontWeight.Black)
            }
            Spacer(Modifier.height(10.dp))
            Box(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(17.dp))
                    .border(1.dp, Hairline, RoundedCornerShape(17.dp))
                    .clickable(enabled = busyIdentifier == null) { onRetry() }.padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CustomIcon(IconType.REFRESH, if (busyIdentifier == "__retry__") Muted else Ink, 16.dp)
                    Spacer(Modifier.width(8.dp))
                    KText(if (busyIdentifier == "__retry__") "再試行中…" else "再試行", 12, if (busyIdentifier == "__retry__") Muted else Ink, FontWeight.Black)
                }
            }
            Spacer(Modifier.height(WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()))
        }
    }
}

@Composable
private fun LoginScreen(
    onBack: (() -> Unit)? = null,
    onLogin: (String, String, (ApiLoginResult) -> Unit) -> Unit,
    onVerifyTwoFactor: (String, String, String, String, (ApiLoginResult) -> Unit) -> Unit
) {
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var twoFactorToken by remember { mutableStateOf<String?>(null) }
    var twoFactorCode by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val awaitingTwoFactor = twoFactorToken != null
    val enabled = if (awaitingTwoFactor) {
        twoFactorCode.length == 6 && !busy
    } else {
        identifier.isNotBlank() && password.isNotBlank() && !busy
    }
    val goBack = {
        if (awaitingTwoFactor) {
            twoFactorToken = null
            twoFactorCode = ""
            error = null
        } else {
            onBack?.invoke()
        }
    }
    BackHandler(enabled = !busy && (awaitingTwoFactor || onBack != null)) { goBack() }

    Box(Modifier.fillMaxSize().background(Paper)) {
        Canvas(Modifier.fillMaxWidth().height(145.dp)) {
            drawRect(Strong)
            drawCircle(Carrot, size.width * .34f, Offset(size.width * .94f, size.height * .04f))
            drawCircle(OnStrong.copy(alpha = .1f), size.width * .14f, Offset(size.width * .12f, size.height * .82f))
        }
        KText(
            "K",
            28,
            OnStrong,
            FontWeight.Black,
            modifier = Modifier.padding(
                start = if (awaitingTwoFactor || onBack != null) 82.dp else 26.dp,
                top = 43.dp
            )
        )
        if (awaitingTwoFactor || onBack != null) {
            Box(
                Modifier.zIndex(3f).padding(start = 22.dp, top = 37.dp).size(42.dp).background(Color.White.copy(.14f), CircleShape)
                    .border(1.dp, Color.White.copy(.25f), CircleShape).clickable(enabled = !busy) { goBack() },
                contentAlignment = Alignment.Center
            ) { CustomIcon(IconType.BACK, Color.White, 20.dp) }
        }
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 26.dp, vertical = 32.dp)) {
            item {
                Spacer(Modifier.height(136.dp))
                KText(if (awaitingTwoFactor) "KAROHA SECURITY" else "KAROHA", 11, Carrot, FontWeight.Black, letterSpacing = 3f)
                Spacer(Modifier.height(13.dp))
                KText(
                    if (awaitingTwoFactor) "本人確認を\n完了しよう。" else "おかえり。\n話の続きをしよう。",
                    32,
                    Ink,
                    FontWeight.Black,
                    lineHeight = 40f
                )
                Spacer(Modifier.height(11.dp))
                KText(
                    if (awaitingTwoFactor) {
                        "認証アプリに表示されている6桁のコードを入力してください。"
                    } else {
                        "Karotterのアカウントでログイン"
                    },
                    12,
                    Muted,
                    lineHeight = 18f
                )
                Spacer(Modifier.height(34.dp))
                if (awaitingTwoFactor) {
                    LoginField(
                        "000000",
                        twoFactorCode,
                        { twoFactorCode = it.filter(Char::isDigit).take(6) },
                        password = false,
                        keyboardType = KeyboardType.Number
                    )
                    Spacer(Modifier.height(10.dp))
                    KText(
                        "コードは端末に保存されません。",
                        9,
                        Muted,
                        FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 3.dp)
                    )
                } else {
                    LoginField("ユーザー名 または メール", identifier, { identifier = it }, false)
                    Spacer(Modifier.height(12.dp))
                    LoginField("パスワード", password, { password = it }, true)
                }
                AnimatedVisibility(error != null, enter = expandVertically() + fadeIn(), exit = fadeOut()) {
                    KText(error.orEmpty(), 11, Carrot, FontWeight.Bold, modifier = Modifier.padding(top = 12.dp))
                }
                Spacer(Modifier.height(22.dp))
                Box(
                    Modifier.fillMaxWidth().height(54.dp).background(if (enabled) Strong else Hairline, RoundedCornerShape(18.dp))
                        .clickable(enabled = enabled) {
                            busy = true
                            error = null
                            val handleResult: (ApiLoginResult) -> Unit = { result ->
                                busy = false
                                when (result) {
                                    is ApiLoginResult.Success -> Unit
                                    is ApiLoginResult.TwoFactorRequired -> {
                                        twoFactorToken = result.token
                                        twoFactorCode = ""
                                    }
                                    is ApiLoginResult.Failure -> error = result.message
                                }
                            }
                            if (awaitingTwoFactor) {
                                onVerifyTwoFactor(
                                    identifier.trim(),
                                    password,
                                    twoFactorToken.orEmpty(),
                                    twoFactorCode,
                                    handleResult
                                )
                            } else {
                                onLogin(identifier.trim(), password, handleResult)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                        KText(
                            when {
                                busy -> if (awaitingTwoFactor) "確認しています…" else "接続しています…"
                                awaitingTwoFactor -> "認証してログイン"
                                else -> "ログイン"
                            },
                            13,
                            if (enabled) OnStrong else Muted,
                            FontWeight.Bold
                        )
                        if (!busy) CustomIcon(IconType.FORWARD, if (enabled) OnStrong else Muted, 15.dp)
                    }
                }
                Spacer(Modifier.height(16.dp))
                Spacer(Modifier.height(16.dp))
                KText(
                    if (awaitingTwoFactor) {
                        "認証コードを他人に共有しないでください。"
                    } else {
                        "ログイン情報はAndroid Keystoreで暗号化し、このアプリ専用領域に安全に保存します。"
                    },
                    10,
                    Muted,
                    lineHeight = 16f
                )
            }
        }
    }
}

@Composable
private fun LoginField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    password: Boolean,
    keyboardType: KeyboardType = if (password) KeyboardType.Password else KeyboardType.Text
) {
    BasicTextField(
        value = value,
        onValueChange = onChange,
        singleLine = true,
        visualTransformation = if (password) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Done
        ),
        textStyle = TextStyle(Ink, 15.sp, FontWeight.Medium),
        cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
        modifier = Modifier.fillMaxWidth().height(55.dp).background(Surface, RoundedCornerShape(17.dp)).border(1.5.dp, if (value.isNotEmpty()) Ink else Muted, RoundedCornerShape(17.dp)).padding(horizontal = 16.dp),
        decorationBox = { inner ->
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                if (value.isEmpty()) KText(label, 13, Muted, FontWeight.Medium)
                inner()
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    api: KarotterApi,
    posts: List<Post>,
    stories: List<ApiStory>,
    storiesLoading: Boolean,
    storiesError: String?,
    loading: Boolean,
    loadingMore: Boolean,
    hasNext: Boolean,
    error: String?,
    currentUser: ApiUser?,
    onRetry: () -> Unit,
    selectedMode: String,
    onModeChange: (String) -> Unit,
    selectedRanking: String,
    onRankingChange: (String) -> Unit,
    homeCommunities: List<ApiCommunity>,
    onCommunityAdded: (ApiCommunity) -> Unit,
    onCommunityRemoved: (ApiCommunity) -> Unit,
    onLoadMore: () -> Unit,
    unreadCount: Int,
    onNotifications: () -> Unit,
    onCreateStory: () -> Unit,
    onStory: (ApiStory) -> Unit,
    onOpen: (Post) -> Unit,
    onAuthor: (Post) -> Unit,
    onReply: (Post) -> Unit,
    onQuote: (Post) -> Unit,
    onRekarot: (Post, Boolean) -> Unit,
    onLike: (Post, Boolean) -> Unit,
    onBookmark: (Post, Boolean) -> Unit
) {
    val renderedPosts = remember(posts) { posts.distinctBy(::postStableKey) }
    var communityPickerOpen by remember { mutableStateOf(false) }
    var pendingCommunityRemoval by remember { mutableStateOf<ApiCommunity?>(null) }
    var removingCommunity by remember { mutableStateOf(false) }
    var communityRemovalError by remember { mutableStateOf<String?>(null) }
    val homeScope = rememberCoroutineScope()
    if (communityPickerOpen) {
        CommunityTimelinePicker(
            api = api,
            existingIds = homeCommunities.mapTo(hashSetOf(), ApiCommunity::id),
            onDismiss = { communityPickerOpen = false },
            onAdded = {
                communityPickerOpen = false
                onCommunityAdded(it)
            }
        )
    }
    pendingCommunityRemoval?.let { community ->
        Dialog(onDismissRequest = { if (!removingCommunity) pendingCommunityRemoval = null }) {
            Column(
                Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(22.dp))
                    .border(1.dp, Hairline, RoundedCornerShape(22.dp)).padding(20.dp)
            ) {
                KText("ホームから削除しますか？", 17, Ink, FontWeight.Black)
                Spacer(Modifier.height(7.dp))
                KText("${community.name}のタブをホームから削除します。コミュニティからは退出しません。", 11, Muted, lineHeight = 17f)
                communityRemovalError?.let {
                    Spacer(Modifier.height(9.dp))
                    ErrorText(it)
                }
                Spacer(Modifier.height(18.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    Box(
                        Modifier.weight(1f).border(1.dp, Hairline, RoundedCornerShape(13.dp))
                            .clickable(enabled = !removingCommunity) { pendingCommunityRemoval = null }
                            .padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) { KText("キャンセル", 10, Ink, FontWeight.Bold) }
                    Box(
                        Modifier.weight(1f).background(Color(0xFFD64045), RoundedCornerShape(13.dp))
                            .clickable(enabled = !removingCommunity) {
                                removingCommunity = true
                                communityRemovalError = null
                                homeScope.launch {
                                    when (val result = withContext(Dispatchers.IO) { api.removeCommunityHomeTimeline(community.id) }) {
                                        is ApiResult.Success -> {
                                            pendingCommunityRemoval = null
                                            onCommunityRemoved(community)
                                        }
                                        is ApiResult.Failure -> communityRemovalError = result.message
                                    }
                                    removingCommunity = false
                                }
                            }.padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) { KText(if (removingCommunity) "削除中…" else "削除", 10, Color.White, FontWeight.Black) }
                }
            }
        }
    }
    val swipeModes = remember(homeCommunities) {
        listOf("latest", "following", "trending") + homeCommunities.map { "community:${it.id}" }
    }
    val homeContentShift = remember { Animatable(0f) }
    var previousHomePage by remember { mutableStateOf(selectedMode to selectedRanking) }
    fun homePageIndex(mode: String, ranking: String): Int {
        val modeIndex = swipeModes.indexOf(mode).coerceAtLeast(0)
        val rankingIndex = if (ranking == "recommended") 1 else 0
        return modeIndex * 2 + rankingIndex
    }
    LaunchedEffect(selectedMode, selectedRanking, swipeModes) {
        val nextPage = selectedMode to selectedRanking
        if (previousHomePage != nextPage) {
            val oldIndex = homePageIndex(previousHomePage.first, previousHomePage.second)
            val newIndex = homePageIndex(nextPage.first, nextPage.second)
            previousHomePage = nextPage
            homeContentShift.snapTo(if (newIndex >= oldIndex) 44f else -44f)
            homeContentShift.animateTo(0f, tween(320, easing = FastOutSlowInEasing))
        }
    }
    val homeContentAlpha = (1f - abs(homeContentShift.value) / 120f).coerceIn(.68f, 1f)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(selectedMode, swipeModes) {
                var horizontalDistance = 0f
                detectHorizontalDragGestures(
                    onDragStart = { horizontalDistance = 0f },
                    onDragCancel = { horizontalDistance = 0f },
                    onDragEnd = {
                        val modes = swipeModes
                        val currentIndex = modes.indexOf(selectedMode).coerceAtLeast(0)
                        when {
                            horizontalDistance < -90f && currentIndex < modes.lastIndex ->
                                onModeChange(modes[currentIndex + 1])
                            horizontalDistance > 90f && currentIndex > 0 ->
                                onModeChange(modes[currentIndex - 1])
                        }
                        horizontalDistance = 0f
                    }
                ) { _, dragAmount -> horizontalDistance += dragAmount }
            },
        contentPadding = PaddingValues(bottom = 126.dp)
    ) {
        item { HomeHeader(currentUser, unreadCount, onNotifications) }
        item { Stories(stories, storiesLoading, storiesError, currentUser, onCreateStory, onStory) }
        stickyHeader(key = "home-filters") {
            FilterStrip(
                selectedMode,
                onModeChange,
                selectedRanking,
                onRankingChange,
                homeCommunities,
                onAddCommunity = { communityPickerOpen = true },
                onRemoveCommunity = {
                    communityRemovalError = null
                    pendingCommunityRemoval = it
                }
            )
        }
        if (error != null) item {
            Box(Modifier.offset(x = homeContentShift.value.dp).alpha(homeContentAlpha)) {
                ErrorStrip(error, onRetry)
            }
        }
        if (loading && renderedPosts.isEmpty()) items(4) {
            Box(Modifier.offset(x = homeContentShift.value.dp).alpha(homeContentAlpha)) {
                LoadingPost()
            }
        }
        items(
            items = renderedPosts,
            key = ::postStableKey
        ) { post ->
            Box(Modifier.offset(x = homeContentShift.value.dp).alpha(homeContentAlpha)) {
                PostCard(post, onOpen, onAuthor, onReply, onQuote, { desired -> onRekarot(post, desired) }, { desired -> onLike(post, desired) }, { desired -> onBookmark(post, desired) })
            }
        }
        if (hasNext && renderedPosts.isNotEmpty()) {
            item(key = "home-load-more-$selectedMode-${renderedPosts.size}") {
                LaunchedEffect(renderedPosts.size, selectedMode) { onLoadMore() }
                Box(Modifier.offset(x = homeContentShift.value.dp).alpha(homeContentAlpha)) {
                    LoadingPost()
                }
            }
        } else if (loadingMore) {
            item {
                Box(Modifier.offset(x = homeContentShift.value.dp).alpha(homeContentAlpha)) {
                    LoadingPost()
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(user: ApiUser?, unreadCount: Int, onNotifications: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 17.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            KText("KAROHA", 11, Ink, FontWeight.Black, letterSpacing = 2.8f)
            Spacer(Modifier.height(4.dp))
            KText("ホーム", 25, Ink, FontWeight.Bold)
            if (user != null) KText(user.displayName.ifBlank { "@${user.username}" }, 11, Muted, FontWeight.Medium)
        }
        Box(Modifier.size(48.dp), contentAlignment = Alignment.Center) {
            Box(
                Modifier.size(42.dp).clip(CircleShape).background(Strong).clickable { onNotifications() },
                contentAlignment = Alignment.Center
            ) {
                CustomIcon(IconType.BELL, OnStrong, 19.dp)
            }
            if (unreadCount > 0) {
                Box(
                    Modifier.align(Alignment.TopEnd)
                        .height(18.dp).widthIn(min = 18.dp)
                        .background(Carrot, RoundedCornerShape(9.dp))
                        .border(1.5.dp, Paper, RoundedCornerShape(9.dp))
                        .padding(horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    KText(if (unreadCount > 99) "99+" else unreadCount.toString(), 8, Color.White, FontWeight.Black, maxLines = 1)
                }
            }
        }
    }
}

@Composable
private fun NotificationToastCard(notification: ApiNotification, onClick: () -> Unit, onDismiss: () -> Unit) {
    var swipeOffsetPx by remember(notification.id) { mutableStateOf(0f) }
    val density = LocalDensity.current.density
    Row(
        Modifier.offset(y = (swipeOffsetPx / density).dp)
            .pointerInput(notification.id) {
                detectDragGestures(
                    onDragStart = { swipeOffsetPx = 0f },
                    onDragCancel = { swipeOffsetPx = 0f },
                    onDragEnd = {
                        if (swipeOffsetPx < -42.dp.toPx()) onDismiss()
                        else swipeOffsetPx = 0f
                    }
                ) { change, dragAmount ->
                    if (dragAmount.y < 0f || swipeOffsetPx < 0f) {
                        swipeOffsetPx = (swipeOffsetPx + dragAmount.y).coerceAtMost(0f)
                        change.consume()
                    }
                }
            }
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Surface)
            .border(1.dp, Hairline, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(42.dp).background(PaleCarrot, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
            if (notification.actorAvatarUrl != null) {
                AsyncImage(
                    notification.actorAvatarUrl,
                    notification.actorName,
                    Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                CustomIcon(IconType.BELL, Carrot, 19.dp)
            }
        }
        Spacer(Modifier.width(11.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                KText("新しい通知", 9, Carrot, FontWeight.Black, letterSpacing = .5f)
                Spacer(Modifier.weight(1f))
                KText(relativeTime(notification.createdAt), 9, Muted, FontWeight.Medium)
            }
            Spacer(Modifier.height(3.dp))
            KText(notification.message, 12, Ink, FontWeight.Bold, maxLines = 2)
        }
        Spacer(Modifier.width(8.dp))
        CustomIcon(IconType.FORWARD, Muted, 15.dp)
    }
}

private data class Story(val name: String, val color: Color, val seen: Boolean = false, val avatarUrl: String? = null)

@Composable
private fun Stories(
    apiStories: List<ApiStory>,
    loading: Boolean,
    error: String?,
    currentUser: ApiUser?,
    onCreate: () -> Unit,
    onStory: (ApiStory) -> Unit
) {
    val stories = apiStories.map {
        Story(it.author.displayName.ifBlank { it.author.username }, avatarColor(it.author.id), it.viewed, it.author.avatarUrl)
    }
    Column {
        Row(Modifier.fillMaxWidth().padding(horizontal = 22.dp), verticalAlignment = Alignment.CenterVertically) {
            KText("STORIES", 11, Muted, FontWeight.Bold, letterSpacing = 1.8f, modifier = Modifier.weight(1f))
            KText("24時間", 11, Muted)
        }
        Spacer(Modifier.height(13.dp))
        if (loading && stories.isEmpty()) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
                StoryBubble(Story("つくる", PaleCarrot, seen = true, avatarUrl = currentUser?.avatarUrl), onCreate)
                Spacer(Modifier.width(14.dp))
                KText("ストーリーを取得中…", 11, Muted)
            }
        } else LazyRow(contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            item(key = "create-story") {
                StoryBubble(Story("つくる", PaleCarrot, seen = true, avatarUrl = currentUser?.avatarUrl), onCreate)
            }
            itemsIndexed(stories) { index, story -> StoryBubble(story) { apiStories.getOrNull(index)?.let(onStory) } }
            if (stories.isEmpty()) {
                item {
                    KText(
                        if (error != null) "ストーリーを取得できませんでした：$error" else "新しいストーリーはありません",
                        11,
                        Muted,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Box(Modifier.fillMaxWidth().height(1.dp).background(Hairline))
    }
}

@Composable
private fun StoryBubble(story: Story, onClick: () -> Unit) {
    val pulse = rememberInfiniteTransition(label = "storyPulse")
    val rotation by pulse.animateFloat(
        initialValue = -2f, targetValue = 2f,
        animationSpec = infiniteRepeatable(tween(1700, easing = FastOutSlowInEasing), repeatMode = androidx.compose.animation.core.RepeatMode.Reverse),
        label = "ring"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(58.dp).clickable { onClick() }) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(58.dp).rotate(if (story.seen) 0f else rotation)) {
            Canvas(Modifier.fillMaxSize()) {
                drawArc(if (story.seen) Hairline else Carrot, -82f, 255f, false, style = Stroke(2.5.dp.toPx(), cap = StrokeCap.Round))
                drawArc(if (story.seen) Hairline else Ink, 188f, 67f, false, style = Stroke(2.5.dp.toPx(), cap = StrokeCap.Round))
            }
            Box(Modifier.size(47.dp).background(story.color, CircleShape), contentAlignment = Alignment.Center) {
                if (story.avatarUrl != null) AsyncImage(story.avatarUrl, story.name, Modifier.fillMaxSize().clip(CircleShape), contentScale = ContentScale.Crop)
                else if (story.name == "つくる") CustomIcon(IconType.PLUS, Ink, 22.dp)
                else KText(story.name.take(1).uppercase(), 16, Ink, FontWeight.Bold)
            }
            if (story.name == "つくる" && story.avatarUrl != null) {
                Box(
                    Modifier.align(Alignment.BottomEnd).size(21.dp).background(Carrot, CircleShape)
                        .border(2.dp, Paper, CircleShape),
                    contentAlignment = Alignment.Center
                ) { CustomIcon(IconType.PLUS, Color.White, 11.dp) }
            }
        }
        Spacer(Modifier.height(6.dp))
        KText(story.name, 11, if (story.seen) Muted else Ink, FontWeight.Medium)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FilterStrip(
    selectedMode: String,
    onModeChange: (String) -> Unit,
    selectedRanking: String,
    onRankingChange: (String) -> Unit,
    homeCommunities: List<ApiCommunity>,
    onAddCommunity: () -> Unit,
    onRemoveCommunity: (ApiCommunity) -> Unit
) {
    val options = listOf("みんな" to "latest", "フォロー中" to "following", "トレンド" to "trending") +
        homeCommunities.map { it.name to "community:${it.id}" }
    Column(Modifier.fillMaxWidth().background(Paper).padding(horizontal = 22.dp, vertical = 15.dp)) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item(key = "add-community-timeline") {
                Box(
                    Modifier.size(34.dp).clip(CircleShape).background(PaleCarrot)
                        .border(1.dp, Carrot.copy(.35f), CircleShape)
                        .clickable { onAddCommunity() },
                    contentAlignment = Alignment.Center
                ) {
                    CustomIcon(IconType.PLUS, Carrot, 16.dp)
                }
            }
            items(options, key = { it.second }) { option ->
                val (label, mode) = option
                val selected = selectedMode == mode
                val optionWidth by animateDpAsState(
                    if (selected) 100.dp else 96.dp,
                    tween(260, easing = FastOutSlowInEasing),
                    label = "homeModeWidth"
                )
                val optionBackground by animateColorAsState(
                    if (selected) Strong else Color.Transparent,
                    tween(240),
                    label = "homeModeBackground"
                )
                val optionBorder by animateColorAsState(
                    if (selected) Strong else Hairline,
                    tween(240),
                    label = "homeModeBorder"
                )
                val optionText by animateColorAsState(
                    if (selected) OnStrong else Muted,
                    tween(220),
                    label = "homeModeText"
                )
                val community = mode.takeIf { it.startsWith("community:") }?.substringAfter(':')?.toLongOrNull()
                    ?.let { id -> homeCommunities.firstOrNull { it.id == id } }
                Box(
                    Modifier.width(optionWidth).widthIn(max = 150.dp)
                        .height(34.dp).clip(RoundedCornerShape(18.dp))
                        .background(optionBackground)
                        .border(1.dp, optionBorder, RoundedCornerShape(18.dp))
                        .combinedClickable(
                            onClick = { onModeChange(mode) },
                            onLongClick = { community?.let(onRemoveCommunity) }
                        )
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) { KText(label, 12, optionText, FontWeight.Bold, maxLines = 1) }
            }
        }
        if (selectedMode != "trending" && !selectedMode.startsWith("community:")) {
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Row(
                    Modifier.clip(RoundedCornerShape(14.dp)).background(Surface)
                        .border(1.dp, Hairline, RoundedCornerShape(14.dp)).padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("最新" to "latest", "おすすめ" to "recommended").forEach { (label, value) ->
                        val selected = selectedRanking == value
                        val rankingBackground by animateColorAsState(
                            if (selected) Strong else Color.Transparent,
                            tween(220),
                            label = "homeRankingBackground"
                        )
                        val rankingText by animateColorAsState(
                            if (selected) OnStrong else Muted,
                            tween(220),
                            label = "homeRankingText"
                        )
                        Box(
                            Modifier.clip(RoundedCornerShape(10.dp))
                                .background(rankingBackground)
                                .clickable { onRankingChange(value) }
                                .padding(horizontal = 18.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            KText(label, 10, rankingText, FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CommunityTimelinePicker(
    api: KarotterApi,
    existingIds: Set<Long>,
    onDismiss: () -> Unit,
    onAdded: (ApiCommunity) -> Unit
) {
    var communities by remember { mutableStateOf<List<ApiCommunity>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var addingId by remember { mutableStateOf<Long?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        when (val result = withContext(Dispatchers.IO) { api.communities() }) {
            is ApiResult.Success -> communities =
                (result.value.owned + result.value.joined).distinctBy(ApiCommunity::id).filterNot { it.id in existingIds }
            is ApiResult.Failure -> error = result.message
        }
        loading = false
    }
    Dialog(onDismissRequest = { if (addingId == null) onDismiss() }) {
        Column(
            Modifier.fillMaxWidth().heightIn(max = 560.dp).background(Surface, RoundedCornerShape(24.dp))
                .border(1.dp, Hairline, RoundedCornerShape(24.dp)).padding(top = 18.dp)
        ) {
            Row(Modifier.padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    KText("ホームに追加", 18, Ink, FontWeight.Black)
                    KText("参加中のコミュニティをタイムラインに追加", 10, Muted)
                }
                Box(
                    Modifier.size(34.dp).clip(RoundedCornerShape(11.dp)).border(1.dp, Hairline, RoundedCornerShape(11.dp))
                        .clickable(enabled = addingId == null) { onDismiss() },
                    contentAlignment = Alignment.Center
                ) { CustomIcon(IconType.CLOSE, Ink, 15.dp) }
            }
            Spacer(Modifier.height(13.dp))
            Box(Modifier.fillMaxWidth().height(1.dp).background(Hairline))
            LazyColumn(Modifier.weight(1f, fill = false), contentPadding = PaddingValues(vertical = 8.dp)) {
                if (loading) items(4) { LoadingPost() }
                error?.let { item { ErrorText(it) } }
                if (!loading && error == null && communities.isEmpty()) {
                    item { KText("追加できるコミュニティはありません", 11, Muted, modifier = Modifier.padding(20.dp)) }
                }
                items(communities, key = ApiCommunity::id) { community ->
                    Row(
                        Modifier.fillMaxWidth().clickable(enabled = addingId == null) {
                            addingId = community.id
                            error = null
                            scope.launch {
                                when (val result = withContext(Dispatchers.IO) { api.addCommunityHomeTimeline(community.id) }) {
                                    is ApiResult.Success -> onAdded(community)
                                    is ApiResult.Failure -> {
                                        error = result.message
                                        addingId = null
                                    }
                                }
                            }
                        }.padding(horizontal = 20.dp, vertical = 11.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.size(42.dp).background(PaleCarrot, RoundedCornerShape(13.dp)), contentAlignment = Alignment.Center) {
                            if (community.headerImageUrl != null) {
                                AsyncImage(community.headerImageUrl, community.name, Modifier.fillMaxSize().clip(RoundedCornerShape(13.dp)), contentScale = ContentScale.Crop)
                            } else CustomIcon(IconType.COMMUNITY, Carrot, 19.dp)
                        }
                        Spacer(Modifier.width(11.dp))
                        Column(Modifier.weight(1f)) {
                            KText(community.name, 13, Ink, FontWeight.Bold, maxLines = 1)
                            KText("${community.memberCount}人", 9, Muted)
                        }
                        if (addingId == community.id) KText("追加中…", 9, Carrot, FontWeight.Black)
                        else CustomIcon(IconType.PLUS, Carrot, 16.dp)
                    }
                }
            }
        }
    }
}

@Composable
private fun Entrance(index: Int, content: @Composable () -> Unit) {
    val y = remember { Animatable(30f) }
    val alpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(index * 55L)
        alpha.animateTo(1f, tween(300))
        y.animateTo(0f, spring(dampingRatio = .78f, stiffness = 220f))
    }
    Box(Modifier.offset(y = y.value.dp).alpha(alpha.value)) { content() }
}

@Composable
private fun PostCard(
    post: Post,
    onOpen: ((Post) -> Unit)? = null,
    onAuthor: ((Post) -> Unit)? = null,
    onReply: ((Post) -> Unit)? = null,
    onQuote: ((Post) -> Unit)? = null,
    onRekarot: ((Boolean) -> Unit)? = null,
    onLike: ((Boolean) -> Unit)? = null,
    onBookmark: ((Boolean) -> Unit)? = null,
    showAbsoluteTime: Boolean = false,
    onQuotedOpen: ((Post) -> Unit)? = null
) {
    var localLiked by remember(post.id) { mutableStateOf(post.initiallyLiked) }
    var localRekaroted by remember(post.id, post.initiallyRekaroted) { mutableStateOf(post.initiallyRekaroted) }
    var localBookmarked by remember(post.id) { mutableStateOf(post.initiallyBookmarked) }
    var reactions by remember(post.id) { mutableStateOf(post.reactions) }
    var poll by remember(post.id) { mutableStateOf(post.poll) }
    var reactionPickerOpen by remember(post.id) { mutableStateOf(false) }
    var textExpanded by remember(post.id, post.text) { mutableStateOf(false) }
    var confirmRekarot by remember(post.id) { mutableStateOf(false) }
    var postMenuOpen by remember(post.id) { mutableStateOf(false) }
    var pendingMenuAction by remember(post.id) { mutableStateOf<PostMenuAction?>(null) }
    var menuActionBusy by remember(post.id) { mutableStateOf(false) }
    var hiddenByMenuAction by remember(post.id) { mutableStateOf(false) }
    val isLongPost = post.text.length > 240 || post.text.count { it == '\n' } >= 5
    val reactionHandler = LocalReactionHandler.current
    val pollVoteHandler = LocalPollVoteHandler.current
    val postMenuEnvironment = LocalPostMenuEnvironment.current
    val postMenuResultHandler = LocalPostMenuResultHandler.current
    val isOwnPost = postMenuEnvironment?.viewerId != null && postMenuEnvironment.viewerId == post.authorId
    val linkPreviewUrl = remember(post.text) { firstPostUrl(post.text) }
    val sharedInteractionState = post.id?.let { LocalPostInteractionStates.current[it] }
    val liked = sharedInteractionState?.liked ?: localLiked
    val bookmarked = sharedInteractionState?.bookmarked ?: localBookmarked
    val sharedRekarotState = post.id?.let { LocalRekarotStates.current[it] }
    val rekaroted = sharedRekarotState?.rekaroted ?: localRekaroted
    val rekarotsCount = sharedRekarotState?.count ?: (
        post.rekarots +
            if (localRekaroted && !post.initiallyRekaroted) 1
            else if (!localRekaroted && post.initiallyRekaroted) -1
            else 0
        ).coerceAtLeast(0)
    val likeScale by animateFloatAsState(if (liked) 1.18f else 1f, spring(dampingRatio = .38f), label = "like")
    val cardAccent = post.cardAccentColor.toAppColor()?.takeIf {
        post.showCardDecoration && post.subscriptionStatus.equals("ACTIVE", true)
    }
    if (hiddenByMenuAction) return
    if (postMenuOpen) {
        val actions = if (isOwnPost) {
            listOf(PostMenuAction.PIN, PostMenuAction.EDIT, PostMenuAction.DELETE)
        } else {
            listOf(PostMenuAction.MUTE, PostMenuAction.BLOCK)
        }
        val systemNavigationClearance =
            WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 36.dp
        Dialog(
            onDismissRequest = { postMenuOpen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Box(Modifier.fillMaxSize().clickable { postMenuOpen = false })
            AnimatedVisibility(
                visible = postMenuOpen,
                enter = slideInVertically(tween(300, easing = FastOutSlowInEasing)) { it } + fadeIn(tween(180)),
                exit = slideOutVertically(tween(220, easing = FastOutSlowInEasing)) { it } + fadeOut(tween(150))
            ) {
            Column(
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 27.dp, topEnd = 27.dp))
                    .background(Surface)
                    .border(1.dp, Hairline, RoundedCornerShape(topStart = 27.dp, topEnd = 27.dp))
                    .clickable { }
                    .padding(start = 18.dp, top = 10.dp, end = 18.dp, bottom = systemNavigationClearance)
            ) {
                Box(
                    Modifier.width(38.dp).height(4.dp).background(Hairline, RoundedCornerShape(3.dp))
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        KText(if (isOwnPost) "投稿を管理" else "@${post.handle.removePrefix("@")}", 16, Ink, FontWeight.Black)
                        KText(if (isOwnPost) "この投稿に対する操作" else "このユーザーに対する操作", 10, Muted)
                    }
                    Box(
                        Modifier.size(34.dp).border(1.dp, Hairline, CircleShape)
                            .clickable { postMenuOpen = false },
                        contentAlignment = Alignment.Center
                    ) {
                        CustomIcon(IconType.CLOSE, Ink, 16.dp)
                    }
                }
                Spacer(Modifier.height(14.dp))
                actions.forEach { action ->
                    val icon = when (action) {
                        PostMenuAction.EDIT -> IconType.PROFILE_EDIT
                        PostMenuAction.MUTE -> IconType.VOLUME_OFF
                        PostMenuAction.BLOCK -> IconType.BLOCK
                        PostMenuAction.DELETE -> IconType.TRASH
                        PostMenuAction.PIN -> IconType.PIN
                    }
                    val title = when (action) {
                        PostMenuAction.EDIT -> "投稿を編集"
                        PostMenuAction.MUTE -> "このユーザーをミュート"
                        PostMenuAction.BLOCK -> "このユーザーをブロック"
                        PostMenuAction.DELETE -> "投稿を削除"
                        PostMenuAction.PIN -> "プロフィールに固定"
                    }
                    val description = when (action) {
                        PostMenuAction.EDIT -> "投稿本文を変更"
                        PostMenuAction.MUTE -> "タイムラインに投稿を表示しない"
                        PostMenuAction.BLOCK -> "フォロー関係を解除して操作を制限"
                        PostMenuAction.DELETE -> "この投稿を完全に削除"
                        PostMenuAction.PIN -> "現在の固定を解除して、この投稿を固定"
                    }
                    Row(
                        Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp))
                            .clickable {
                                postMenuOpen = false
                                if (action == PostMenuAction.EDIT) {
                                    postMenuEnvironment?.edit(post) { updated ->
                                        postMenuResultHandler?.invoke(PostMenuAction.EDIT, updated)
                                    }
                                } else {
                                    pendingMenuAction = action
                                }
                            }.padding(horizontal = 10.dp, vertical = 11.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier.size(38.dp).background(
                                if (action == PostMenuAction.DELETE || action == PostMenuAction.BLOCK) PaleCarrot else Paper,
                                RoundedCornerShape(13.dp)
                            ),
                            contentAlignment = Alignment.Center
                        ) {
                            CustomIcon(
                                icon,
                                if (action == PostMenuAction.DELETE || action == PostMenuAction.BLOCK) Color(0xFFD64045) else Ink,
                                18.dp
                            )
                        }
                        Spacer(Modifier.width(11.dp))
                        Column(Modifier.weight(1f)) {
                            KText(title, 13, if (action == PostMenuAction.DELETE || action == PostMenuAction.BLOCK) Color(0xFFD64045) else Ink, FontWeight.Bold)
                            Spacer(Modifier.height(2.dp))
                            KText(description, 9, Muted)
                        }
                        CustomIcon(IconType.FORWARD, Muted, 15.dp)
                    }
                }
            }
            }
            }
        }
    }
    pendingMenuAction?.let { action ->
        val actionLabel = when (action) {
            PostMenuAction.EDIT -> "編集"
            PostMenuAction.MUTE -> "ミュート"
            PostMenuAction.BLOCK -> "ブロック"
            PostMenuAction.DELETE -> "削除"
            PostMenuAction.PIN -> "固定"
        }
        val explanation = when (action) {
            PostMenuAction.EDIT -> "投稿本文を編集します。"
            PostMenuAction.MUTE -> "${post.name}さんの投稿をタイムラインに表示しないようにします。相手には通知されません。"
            PostMenuAction.BLOCK -> "${post.name}さんとのフォロー関係を解除し、互いの操作を制限します。"
            PostMenuAction.DELETE -> "削除した投稿は元に戻せません。"
            PostMenuAction.PIN -> "現在固定している投稿を解除してから、この投稿をプロフィールに固定します。"
        }
        Dialog(onDismissRequest = { if (!menuActionBusy) pendingMenuAction = null }) {
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Surface)
                    .border(1.dp, Hairline, RoundedCornerShape(22.dp)).padding(21.dp)
            ) {
                KText("${actionLabel}しますか？", 17, Ink, FontWeight.Black)
                Spacer(Modifier.height(7.dp))
                KText(explanation, 12, Muted, lineHeight = 18f)
                Spacer(Modifier.height(18.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    Box(
                        Modifier.weight(1f).clip(RoundedCornerShape(13.dp))
                            .border(1.dp, Hairline, RoundedCornerShape(13.dp))
                            .clickable(enabled = !menuActionBusy) { pendingMenuAction = null }
                            .padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        KText("キャンセル", 11, Ink, FontWeight.Bold)
                    }
                    Box(
                        Modifier.weight(1f).clip(RoundedCornerShape(13.dp))
                            .background(
                                if (action == PostMenuAction.DELETE || action == PostMenuAction.BLOCK) Color(0xFFD64045) else Carrot
                            )
                            .clickable(enabled = !menuActionBusy) {
                                val environment = postMenuEnvironment ?: return@clickable
                                menuActionBusy = true
                                environment.execute(action, post) { success ->
                                    menuActionBusy = false
                                    if (success) {
                                        pendingMenuAction = null
                                        postMenuResultHandler?.invoke(action, post)
                                        if (action != PostMenuAction.PIN) hiddenByMenuAction = true
                                    }
                                }
                            }.padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        KText(if (menuActionBusy) "処理中…" else actionLabel, 11, Color.White, FontWeight.Black)
                    }
                }
            }
        }
    }
    if (confirmRekarot) {
        Dialog(onDismissRequest = { confirmRekarot = false }) {
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Surface).border(1.dp, Hairline, RoundedCornerShape(22.dp)).padding(21.dp)) {
                KText("リカロートしますか？", 17, Ink, FontWeight.Black)
                Spacer(Modifier.height(7.dp))
                KText("この投稿をあなたのフォロワーへ共有します。", 12, Muted, lineHeight = 18f)
                Spacer(Modifier.height(18.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    Box(Modifier.weight(1f).clip(RoundedCornerShape(13.dp)).border(1.dp, Hairline, RoundedCornerShape(13.dp)).clickable { confirmRekarot = false }.padding(vertical = 11.dp), contentAlignment = Alignment.Center) {
                        KText("キャンセル", 11, Ink, FontWeight.Bold)
                    }
                    Box(Modifier.weight(1f).clip(RoundedCornerShape(13.dp)).background(Carrot).clickable {
                        confirmRekarot = false
                        localRekaroted = true
                        onRekarot?.invoke(true)
                    }.padding(vertical = 11.dp), contentAlignment = Alignment.Center) {
                        KText("リカロート", 11, Color.White, FontWeight.Black)
                    }
                }
            }
        }
    }
    Column(
        Modifier.fillMaxWidth()
            .drawBehind { cardAccent?.let { drawRect(it, size = Size(3.dp.toPx(), size.height)) } }
            .clickable(enabled = onOpen != null) { onOpen?.invoke(post) }
            .padding(horizontal = 22.dp, vertical = 18.dp)
    ) {
        post.rekarotedBy?.let { user ->
            Row(Modifier.padding(start = 55.dp, bottom = 9.dp), verticalAlignment = Alignment.CenterVertically) {
                CustomIcon(IconType.REFRESH, Carrot, 13.dp)
                Spacer(Modifier.width(7.dp))
                KText("${user.displayName.ifBlank { "@${user.username}" }}さんがリカロートしました", 10, Muted, FontWeight.Bold, maxLines = 1)
            }
        }
        Row(verticalAlignment = Alignment.Top) {
            Box(Modifier.size(43.dp).background(post.avatar, RoundedCornerShape(15.dp)).clickable(enabled = onAuthor != null) { onAuthor?.invoke(post) }, contentAlignment = Alignment.Center) {
                if (post.avatarUrl != null) AsyncImage(post.avatarUrl, post.name, Modifier.fillMaxSize().clip(RoundedCornerShape(15.dp)), contentScale = ContentScale.Crop)
                else KText(post.name.take(1), 16, Ink, FontWeight.Black)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        Modifier.weight(1f).clickable(enabled = onAuthor != null) { onAuthor?.invoke(post) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                    KText(post.name, 14, Ink, FontWeight.Bold, modifier = Modifier.widthIn(max = 112.dp), maxLines = 1)
                    AccountMarks(post.officialMarks.ifEmpty { listOf(post.officialMark) }, post.isBotAccount, post.isParodyAccount, post.isPrivateAccount, compact = true)
                    SubscriptionBadge(
                        post.subscriptionPlan,
                        post.subscriptionStatus,
                        post.showSubscriptionBadges && if (post.subscriptionPlan.equals("PLUS", true)) post.showPlusBadge else post.showProBadge,
                        post.premiumBadgeColor,
                        compact = true
                    )
                    Spacer(Modifier.width(6.dp))
                    KText(post.handle, 12, Muted, modifier = Modifier.weight(1f), maxLines = 1)
                    }
                    Spacer(Modifier.width(5.dp))
                    Box(Modifier.width(if (showAbsoluteTime) 132.dp else 62.dp), contentAlignment = Alignment.CenterEnd) {
                        KText(if (showAbsoluteTime) absoluteTime(post.createdAt) else relativeTime(post.createdAt), 11, Muted, maxLines = 1)
                    }
                    if (post.id != null && postMenuEnvironment != null) {
                        Box(
                            Modifier.size(30.dp).clip(CircleShape).clickable { postMenuOpen = true },
                            contentAlignment = Alignment.Center
                        ) {
                            CustomIcon(IconType.MORE, Muted, 18.dp)
                        }
                    }
                }
                Spacer(Modifier.height(9.dp))
                RichContentText(
                    post.text,
                    15,
                    Ink,
                    lineHeight = 23f,
                    maxLines = if (isLongPost && !textExpanded) 5 else Int.MAX_VALUE,
                    onPlainTextClick = onOpen?.let { open -> { open(post) } }
                )
                if (isLongPost) {
                    Spacer(Modifier.height(3.dp))
                    KText(
                        if (textExpanded) "省略" else "もっと見る",
                        11,
                        Carrot,
                        FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { textExpanded = !textExpanded }
                            .padding(vertical = 5.dp)
                    )
                }
                PostAccessPanel(post)
                if (post.media.isNotEmpty()) {
                    Spacer(Modifier.height(14.dp))
                    MediaGallery(post.media)
                } else if (post.featured) {
                    Spacer(Modifier.height(14.dp))
                    FeaturedImage()
                }
                poll?.let { currentPoll ->
                    Spacer(Modifier.height(13.dp))
                    PostPoll(
                        poll = currentPoll,
                        enabled = post.id != null && pollVoteHandler != null,
                        onVote = { optionId, done ->
                            pollVoteHandler?.invoke(post, optionId) { updated ->
                                if (updated != null) poll = updated
                                done()
                            } ?: done()
                        }
                    )
                }
                post.quotedPost?.let { quoted ->
                    Spacer(Modifier.height(12.dp))
                    QuotedPostCard(quoted, (onQuotedOpen ?: onOpen)?.let { open -> { open(quoted) } })
                }
                linkPreviewUrl?.let { url ->
                    Spacer(Modifier.height(12.dp))
                    PostLinkPreviewCard(url)
                }
                if (reactions.isNotEmpty() || reactionHandler != null) {
                    Spacer(Modifier.height(10.dp))
                    ReactionStrip(
                        reactions = reactions,
                        pickerOpen = reactionPickerOpen,
                        onTogglePicker = { reactionPickerOpen = !reactionPickerOpen },
                        onReact = { emoji ->
                            val current = reactions.firstOrNull { it.emoji == emoji }
                            val desired = current?.reacted != true
                            reactions = if (current == null) {
                                reactions + ApiReaction(emoji, 1, true)
                            } else {
                                reactions.mapNotNull {
                                    if (it.emoji != emoji) it
                                    else {
                                        val count = (it.count + if (desired) 1 else -1).coerceAtLeast(0)
                                        if (count == 0) null else it.copy(count = count, reacted = desired)
                                    }
                                }
                            }
                            reactionPickerOpen = false
                            reactionHandler?.invoke(post, emoji, desired)
                        }
                    )
                }
                Spacer(Modifier.height(14.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(Modifier.clip(RoundedCornerShape(12.dp)).clickable(enabled = onReply != null) { onReply?.invoke(post) }.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Action(IconType.REPLY, post.replies.toString())
                    }
                    Spacer(Modifier.width(18.dp))
                    Row(Modifier.clip(RoundedCornerShape(12.dp)).clickable(enabled = onRekarot != null) {
                        if (rekaroted) {
                            localRekaroted = false
                            onRekarot?.invoke(false)
                        } else confirmRekarot = true
                    }.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
                        CustomIcon(IconType.REKAROT, if (rekaroted) Carrot else Muted, 18.dp)
                        Spacer(Modifier.width(5.dp))
                        KText(rekarotsCount.toString(), 11, if (rekaroted) Carrot else Muted, FontWeight.Medium)
                    }
                    Spacer(Modifier.width(9.dp))
                    val quoteAllowed = onQuote != null && post.canQuote && !post.isPrivateAccount
                    Box(
                        Modifier.clip(RoundedCornerShape(10.dp))
                            .clickable(enabled = quoteAllowed) { onQuote?.invoke(post) }
                            .padding(4.dp)
                    ) {
                        KText("❝", 17, if (quoteAllowed) Muted else Hairline, FontWeight.Bold)
                    }
                    Spacer(Modifier.width(9.dp))
                    Row(
                        Modifier.scale(likeScale).clip(RoundedCornerShape(12.dp)).clickable(enabled = onLike != null) {
                            localLiked = !liked
                            onLike?.invoke(!liked)
                        }.padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomIcon(IconType.HEART, if (liked) Carrot else Muted, 17.dp, filled = liked)
                        Spacer(Modifier.width(6.dp))
                        KText(
                            (
                                post.likes +
                                    if (liked && !post.initiallyLiked) 1
                                    else if (!liked && post.initiallyLiked) -1
                                    else 0
                            ).coerceAtLeast(0).toString(),
                            11,
                            if (liked) Carrot else Muted,
                            FontWeight.Medium
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    if (post.tag != null) {
                        Box(Modifier.background(PaleCarrot, RoundedCornerShape(8.dp)).padding(horizontal = 9.dp, vertical = 5.dp)) {
                            KText(post.tag, 10, Carrot, FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Box(Modifier.clip(RoundedCornerShape(10.dp)).clickable(enabled = onBookmark != null) {
                        localBookmarked = !bookmarked
                        onBookmark?.invoke(!bookmarked)
                    }.padding(4.dp)) {
                        CustomIcon(IconType.BOOKMARK, if (bookmarked) Carrot else Muted, 17.dp, filled = bookmarked)
                    }
                }
            }
        }
    }
    Box(Modifier.padding(start = 77.dp).fillMaxWidth().height(1.dp).background(Hairline))
}

@Composable
private fun PostAccessPanel(post: Post) {
    val visibilityLabel = when (post.visibility.uppercase()) {
        "PUBLIC" -> "全員"
        "FOLLOWERS", "FOLLOWING" -> "フォロワー"
        "CIRCLE" -> post.viewerCircleName ?: "サークル限定"
        "PRIVATE", "ONLY_ME", "SELF" -> "自分のみ"
        else -> post.visibility.ifBlank { "全員" }
    }
    val replyLabel = when (post.replyRestriction.uppercase()) {
        "EVERYONE", "PUBLIC" -> "全員"
        "FOLLOWERS", "FOLLOWING" -> "フォロワー"
        "MENTIONS", "MENTIONED" -> "メンション対象"
        "CIRCLE" -> post.replyCircleName ?: "サークル限定"
        "NONE", "DISABLED", "NO_ONE" -> "返信不可"
        else -> post.replyRestriction.ifBlank { "全員" }
    }
    val contentLabels = buildList<String> {
        if (post.isAiGenerated) add("AI生成")
        if (post.isPromotional) add("プロモーション")
        if (post.isR18) add("成人向け")
    }
    val items = buildList<Pair<String, String>> {
        val circleDestination = post.visibility.equals("CIRCLE", true) || post.viewerCircleId != null
        if (circleDestination) {
            add("投稿先" to (post.viewerCircleName?.let { "サークル・$it" } ?: "サークル"))
        } else if (!post.visibility.equals("PUBLIC", true)) {
            add("公開範囲" to visibilityLabel)
        }
        if (!post.replyRestriction.equals("EVERYONE", true) && !post.replyRestriction.equals("PUBLIC", true)) {
            add("返信" to replyLabel)
        }
        post.minimumAge?.let { add("最低年齢" to "${it}歳") }
        post.maximumAge?.let { add("最高年齢" to "${it}歳") }
        if (contentLabels.isNotEmpty()) add("コンテンツ" to contentLabels.joinToString("・"))
        post.expiresAt?.let { add("自動削除" to absoluteTime(it)) }
    }
    if (items.isEmpty()) return

    Spacer(Modifier.height(11.dp))
    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        items(items) { (label, value) ->
            PostAccessItem(label, value)
        }
    }
}

@Composable
private fun PostAccessItem(label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier.clip(RoundedCornerShape(10.dp)).background(Surface)
            .border(1.dp, Hairline, RoundedCornerShape(10.dp))
            .padding(horizontal = 9.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        KText(label, 8, Muted, FontWeight.Bold, letterSpacing = .35f, maxLines = 1)
        Spacer(Modifier.width(5.dp))
        KText(value, 9, Ink, FontWeight.Black, maxLines = 1)
    }
}

@Composable
private fun PostPoll(
    poll: ApiPoll,
    enabled: Boolean,
    onVote: (Long, () -> Unit) -> Unit
) {
    var busyOptionId by remember(poll.id) { mutableStateOf<Long?>(null) }
    val showResults = poll.isExpired || poll.ownVoteOptionId != null || poll.options.any { it.votedByMe }
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(17.dp)).background(Surface)
            .border(1.dp, Hairline, RoundedCornerShape(17.dp)).padding(11.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        poll.options.forEach { option ->
            val chosen = option.votedByMe || poll.ownVoteOptionId == option.id
            val progress by animateFloatAsState(
                targetValue = if (showResults) (option.percentage / 100f).coerceIn(0f, 1f) else 0f,
                animationSpec = tween(420, easing = FastOutSlowInEasing),
                label = "pollProgress"
            )
            Box(
                Modifier.fillMaxWidth().heightIn(min = 43.dp).clip(RoundedCornerShape(13.dp))
                    .background(Paper)
                    .border(1.dp, if (chosen) Carrot else Hairline, RoundedCornerShape(13.dp))
                    .clickable(enabled = enabled && !poll.isExpired && busyOptionId == null) {
                        busyOptionId = option.id
                        onVote(option.id) { busyOptionId = null }
                    }
            ) {
                if (progress > 0f) {
                    Box(
                        Modifier.fillMaxWidth(progress).fillMaxHeight()
                            .background(if (chosen) PaleCarrot else Hairline.copy(.65f))
                    )
                }
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 13.dp, vertical = 11.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.size(16.dp).border(1.5.dp, if (chosen) Carrot else Muted, CircleShape)
                            .background(if (chosen) Carrot else Color.Transparent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (chosen) Box(Modifier.size(5.dp).background(Color.White, CircleShape))
                    }
                    Spacer(Modifier.width(9.dp))
                    KText(option.text, 12, Ink, FontWeight.Bold, modifier = Modifier.weight(1f))
                    if (busyOptionId == option.id) KText("…", 11, Carrot, FontWeight.Black)
                    else if (showResults) KText("${option.percentage}%", 11, if (chosen) Carrot else Muted, FontWeight.Black)
                }
            }
        }
        Row(Modifier.fillMaxWidth().padding(horizontal = 3.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
            KText("${poll.totalVotes}票", 10, Muted, FontWeight.Bold)
            if (poll.isAnonymous) {
                Spacer(Modifier.width(8.dp))
                KText("匿名投票", 9, Muted)
            }
            Spacer(Modifier.weight(1f))
            KText(if (poll.isExpired) "投票終了" else "投票受付中", 9, if (poll.isExpired) Muted else Carrot, FontWeight.Bold)
        }
    }
}

private fun String.isOfficialMark(): Boolean = isNotBlank() && !equals("NONE", true) && this != "[]"

private fun String?.toAppColor(): Color? = this
    ?.takeIf { it.isNotBlank() && !it.equals("null", true) }
    ?.let { value -> runCatching { Color(android.graphics.Color.parseColor(value)) }.getOrNull() }

private fun namedPremiumColor(value: String): Color = when (value.uppercase()) {
    "BLACK" -> Color(0xFF17191C)
    "RED" -> Color(0xFFE94F55)
    "GREEN" -> Color(0xFF35A66F)
    "BLUE" -> Color(0xFF3689DC)
    "PINK" -> Color(0xFFE65E9A)
    "CORAL" -> Color(0xFFF06F61)
    "MAGENTA" -> Color(0xFFC43EB8)
    "LIME" -> Color(0xFF83B92E)
    "BROWN" -> Color(0xFF8A5A3C)
    "PURPLE" -> Color(0xFF8759D8)
    "YELLOW" -> Color(0xFFD6A900)
    "GRAY", "GREY" -> Color(0xFF72777F)
    else -> Carrot
}

private fun onlineStatusColor(status: String): Color = when (status.uppercase()) {
    "ONLINE" -> Color(0xFF36B37E)
    "IDLE" -> Color(0xFFE2A928)
    "DND" -> Color(0xFFE24C57)
    "INVISIBLE" -> Muted
    else -> Muted
}

private fun onlineStatusLabel(status: String): String = when (status.uppercase()) {
    "ONLINE" -> "オンライン"
    "IDLE" -> "退席中"
    "DND" -> "取り込み中"
    "INVISIBLE" -> "非表示"
    else -> "オフライン"
}

@Composable
@Suppress("UNUSED_PARAMETER")
private fun SubscriptionBadge(plan: String, status: String, visible: Boolean, colorName: String, compact: Boolean = false) = Unit

private val FrequentReactionEmojis = listOf("👈", "👍", "❤️", "😂", "😮", "😢", "🔥")

private val BmpReactionEmojis = listOf(
    "☀️", "☁️", "☂️", "☃️", "☄️", "☎️", "☑️", "☔", "☕", "☘️", "☝️", "☠️",
    "☢️", "☣️", "☦️", "☪️", "☮️", "☯️", "☸️", "☹️", "☺️", "♀️", "♂️", "♟️",
    "♠️", "♣️", "♥️", "♦️", "♨️", "♻️", "♾️", "⚒️", "⚔️", "⚕️", "⚖️", "⚗️",
    "⚙️", "⚛️", "⚜️", "⚠️", "⚡", "⚧️", "⚪", "⚫", "⚰️", "⚱️", "⛏️", "⛑️",
    "⛓️", "⛩️", "⛰️", "⛱️", "⛲", "⛳", "⛴️", "⛵", "⛷️", "⛸️", "⛹️", "⛺",
    "⛽", "✂️", "✅", "✈️", "✉️", "✊", "✋", "✌️", "✍️", "✏️", "✒️", "✔️",
    "✖️", "✝️", "✡️", "✨", "✳️", "✴️", "❄️", "❇️", "❌", "❎", "❓", "❔",
    "❕", "❗", "❣️", "❤️", "⭐", "⭕", "➕", "➖", "➗", "➡️", "➰", "➿"
)

private val AllReactionEmojis: List<String> by lazy {
    val joinedEmoji = listOf(
        "☺️", "☹️", "❤️", "❣️", "❤️‍🔥", "❤️‍🩹", "👁️‍🗨️", "🐻‍❄️",
        "🏳️‍🌈", "🏳️‍⚧️", "🏴‍☠️", "👨‍💻", "👩‍💻", "🧑‍💻", "👨‍🎨", "👩‍🎨",
        "🧑‍🎨", "👨‍🚀", "👩‍🚀", "🧑‍🚀", "👨‍🍳", "👩‍🍳", "🧑‍🍳", "👨‍⚕️",
        "👩‍⚕️", "🧑‍⚕️", "👨‍🏫", "👩‍🏫", "🧑‍🏫", "👨‍🔬", "👩‍🔬", "🧑‍🔬",
        "👨‍🚒", "👩‍🚒", "🧑‍🚒", "👨‍⚖️", "👩‍⚖️", "🧑‍⚖️", "👨‍✈️", "👩‍✈️",
        "🧑‍✈️", "👨‍🌾", "👩‍🌾", "🧑‍🌾", "👨‍🔧", "👩‍🔧", "🧑‍🔧",
        "👨‍👩‍👧", "👨‍👩‍👦", "👩‍👩‍👧", "👨‍👨‍👦", "👩‍❤️‍👩", "👨‍❤️‍👨",
        "👩‍❤️‍💋‍👩", "👨‍❤️‍💋‍👨"
    )
    val ranges = listOf(
        0x1F300..0x1F5FF,
        0x1F600..0x1F64F,
        0x1F680..0x1F6FF,
        0x1F900..0x1F9FF,
        0x1FA70..0x1FAFF
    )
    buildList {
        addAll(FrequentReactionEmojis)
        addAll(BmpReactionEmojis)
        addAll(joinedEmoji)
        ranges.forEach { range ->
            range.forEach { codePoint ->
                if (Character.isDefined(codePoint) && codePoint !in 0x1F3FB..0x1F3FF) {
                    val value = String(Character.toChars(codePoint))
                    add(value)
                }
            }
        }
        java.util.Locale.getISOCountries().forEach { country ->
            val first = Character.toChars(0x1F1E6 + (country[0] - 'A'))
            val second = Character.toChars(0x1F1E6 + (country[1] - 'A'))
            add(String(first) + String(second))
        }
    }.distinct()
}

private data class ReactionOption(
    val value: String,
    val label: String,
    val category: String,
    val isPro: Boolean = false,
    val searchKeywords: String = label
)

private val ProReactionOptions = listOf(
    "pro:ai" to "愛", "pro:arara" to "あらら", "pro:arigato" to "ありがとう",
    "pro:bananala" to "ばななぁ", "pro:bimi" to "美味", "pro:bimyou" to "微妙",
    "pro:critical" to "クリティカル", "pro:daijoubu" to "大丈夫？", "pro:daisuki" to "だいすき",
    "pro:dakara" to "だから", "pro:dame" to "だめ", "pro:desu.png" to "です",
    "pro:e" to "え？", "pro:ee" to "えぇ…", "pro:fanburu" to "ファンブル",
    "pro:furaidopotato" to "ふらいどぽてと", "pro:ganbare" to "がんばれ", "pro:gekiatsu" to "激アツ",
    "pro:gomen" to "ごめんね", "pro:hai-gimon" to "はい？", "pro:hai" to "はい",
    "pro:hiku" to "引", "pro:hosii" to "ほしい", "pro:hurokuu" to "風呂食う",
    "pro:igyo" to "偉業", "pro:iie" to "いいえ", "pro:iiyo" to "いいよ",
    "pro:kakkoyosugiru" to "かっこよすぎる", "pro:kanasii" to "悲しい", "pro:kandou" to "感動",
    "pro:kane" to "金", "pro:kansya" to "感謝", "pro:karoart" to "かろあーと",
    "pro:karoearth" to "かろあーす", "pro:karon" to "かろん", "pro:karotter" to "Karotter",
    "pro:kawaii" to "かわいい", "pro:kekkonsitai" to "結婚したい", "pro:kirei" to "綺麗",
    "pro:kore" to "これ", "pro:kowasugiru" to "怖すぎる", "pro:kurusii" to "苦しい",
    "pro:kusa" to "草", "pro:mazi" to "マジ？", "pro:mazide" to "マジで",
    "pro:medaka" to "めだか", "pro:medetai" to "めでたい", "pro:melonsoda" to "めろんそーだ",
    "pro:nani" to "なに？", "pro:odaizini" to "お大事に…", "pro:ohayo" to "おはよ",
    "pro:otsukaresama" to "おつかれ様", "pro:owari" to "終", "pro:owatta" to "おわった",
    "pro:oyasumi" to "おやすみ", "pro:sagidesu" to "詐欺です", "pro:saida-" to "さいだー",
    "pro:saikoukaryoku" to "最高火力", "pro:saikousugiru" to "最高すぎる", "pro:sayonara" to "さよなら",
    "pro:shihiro" to "しひろ", "pro:sinpaidayo" to "心配だよ", "pro:sonnnawake" to "そんなわけ",
    "pro:sorena" to "それな", "pro:sugoi" to "すごい", "pro:suki" to "すき",
    "pro:syogyomujo" to "諸行無常", "pro:syunkasyuutouasahiruban" to "春夏秋冬朝昼晩",
    "pro:takuan" to "たくあん", "pro:tasukaru" to "助かる", "pro:tasukete" to "たすけて",
    "pro:tensai" to "天才！", "pro:thinkkaron1" to "疑問（かろん）", "pro:this" to "これは",
    "pro:tigaimasu" to "違います", "pro:umai" to "うまい", "pro:uo-!!" to "うおー！！",
    "pro:urayamasii" to "羨ましい", "pro:wakaru" to "わかる", "pro:watashihakami" to "私は神",
    "pro:yamete" to "やめて", "pro:yasasii" to "やさしい", "pro:yoroshiku" to "よろしく",
    "pro:youkoso" to "ようこそ", "pro:yurusanai" to "許さない", "pro:yurushite" to "ゆるして",
    "pro:yuunousugiru" to "有能すぎる"
).map { (value, label) -> ReactionOption(value, label, "Pro", isPro = true, searchKeywords = "$label $value Pro プロ") }

private fun reactionCategory(value: String): String {
    if (value in FrequentReactionEmojis) return "よく使う"
    val codePoint = value.codePointAt(0)
    return when {
        codePoint in 0x1F600..0x1F64F || codePoint in 0x1F910..0x1F92F -> "表情"
        codePoint in 0x1F440..0x1F487 || codePoint in 0x1F90C..0x1F9B3 -> "人・ジェスチャー"
        codePoint in 0x1F400..0x1F43F || codePoint in 0x1F980..0x1F9AE ||
            codePoint in 0x1F330..0x1F343 -> "動物・自然"
        codePoint in 0x1F32D..0x1F37F || codePoint in 0x1F950..0x1F96F -> "食べ物"
        codePoint in 0x1F1E6..0x1F1FF -> "旗"
        else -> "記号・その他"
    }
}

private val StandardReactionOptions: List<ReactionOption> by lazy {
    AllReactionEmojis.filter(::isEmojiReaction).map {
        ReactionOption(it, it, reactionCategory(it), searchKeywords = emojiJapaneseKeywords(it))
    }
}

private val CommonEmojiJapaneseKeywords = mapOf(
    "👈" to "左 指差し 指 ゆび", "👍" to "いいね 親指 サムズアップ", "❤️" to "ハート 赤 愛 好き",
    "😂" to "笑い 泣き笑い 面白い 顔", "😮" to "驚き びっくり 顔", "😢" to "泣く 悲しい 涙 顔",
    "🔥" to "炎 火 熱い", "😀" to "笑顔 にこにこ 顔", "😃" to "笑顔 嬉しい 顔",
    "😄" to "笑顔 嬉しい 顔", "😁" to "笑顔 にやり 顔", "😊" to "笑顔 ほほえみ 顔",
    "😍" to "大好き ハート 目 顔", "🥰" to "大好き ハート 笑顔", "😘" to "キス 投げキッス 顔",
    "😎" to "サングラス かっこいい 顔", "🤔" to "考える 疑問 顔", "🙄" to "白目 顔",
    "😡" to "怒り 怒る 顔", "😭" to "大泣き 悲しい 涙 顔", "😱" to "恐怖 叫ぶ びっくり 顔",
    "🥳" to "お祝い パーティー 顔", "🤩" to "星 目 興奮 顔", "🤯" to "衝撃 爆発 びっくり 顔",
    "🙏" to "祈る お願い ありがとう 手", "👏" to "拍手 おめでとう 手", "🙌" to "万歳 やった 手",
    "👌" to "OK オーケー 手", "✌️" to "ピース 勝利 手", "🤝" to "握手 協力 手",
    "💪" to "筋肉 力 強い 腕", "👀" to "目 見る 注目", "💯" to "百点 満点",
    "✨" to "きらきら 輝き 星", "⭐" to "星 スター", "🎉" to "お祝い クラッカー",
    "🎊" to "お祝い くす玉", "🎂" to "誕生日 ケーキ", "🎁" to "プレゼント 贈り物",
    "💡" to "電球 アイデア ひらめき", "💬" to "会話 コメント 吹き出し", "💤" to "睡眠 眠い",
    "✅" to "チェック 完了 正解", "❌" to "バツ 不正解 だめ", "❓" to "疑問 質問 はてな",
    "❗" to "びっくり 注意 感嘆符", "⚠️" to "警告 注意", "🚀" to "ロケット 宇宙",
    "🌸" to "桜 花 春", "🌈" to "虹", "☀️" to "太陽 晴れ 天気", "☁️" to "雲 曇り 天気",
    "☔" to "傘 雨 天気", "❄️" to "雪 冬", "🐶" to "犬 いぬ 動物", "🐱" to "猫 ねこ 動物",
    "🐰" to "うさぎ 動物", "🐻" to "くま 熊 動物", "🍎" to "りんご 林檎 果物",
    "🍌" to "バナナ 果物", "🍓" to "いちご 苺 果物", "🍔" to "ハンバーガー 食べ物",
    "🍕" to "ピザ 食べ物", "🍣" to "寿司 すし 食べ物", "🍜" to "ラーメン 麺 食べ物",
    "🍰" to "ケーキ スイーツ 食べ物", "☕" to "コーヒー 飲み物", "🍺" to "ビール 酒 飲み物"
)

private fun emojiJapaneseKeywords(value: String): String {
    CommonEmojiJapaneseKeywords[value]?.let { return "$value $it" }
    val category = reactionCategory(value)
    val categoryKeywords = when (category) {
        "表情" -> "顔 表情 気持ち 感情"
        "人・ジェスチャー" -> "人 手 指 ジェスチャー 動作"
        "動物・自然" -> "動物 生き物 自然 植物 花"
        "食べ物" -> "食べ物 飲み物 料理 果物"
        "旗" -> "旗 国 国旗"
        "よく使う" -> "よく使う 人気"
        else -> "記号 物 道具 スポーツ 乗り物 絵文字"
    }
    return "$value $category $categoryKeywords"
}

private fun isEmojiReaction(value: String): Boolean {
    if (value.startsWith("pro:", ignoreCase = true)) return true
    if (value in FrequentReactionEmojis) return true
    val firstCodePoint = value.codePointAtOrNull(0) ?: return false
    return firstCodePoint in 0x1F300..0x1FAFF ||
        firstCodePoint in 0x1F1E6..0x1F1FF ||
        value in BmpReactionEmojis
}

private fun String.codePointAtOrNull(index: Int): Int? =
    takeIf { index in indices }?.let { Character.codePointAt(it, index) }

@Composable
private fun officialMarkColor(value: String): Color = when (value.uppercase()) {
    "BLACK" -> Color(0xFF17191C)
    "RED" -> Color(0xFFE5484D)
    "GREEN" -> Color(0xFF2DA66E)
    "ORANGE" -> Color(0xFFF27A2D)
    "PINK" -> Color(0xFFE85B9C)
    "CORAL" -> Color(0xFFF06F61)
    "MAGENTA" -> Color(0xFFC13DB5)
    "LIME" -> Color(0xFF83B92E)
    "BROWN" -> Color(0xFF8A5A3C)
    "YELLOW" -> Color(0xFFD5A900)
    "PURPLE" -> Color(0xFF7851C8)
    "BLUE" -> Color(0xFF347FD1)
    "GRAY", "GREY" -> Color(0xFF72777F)
    "WHITE" -> Color(0xFFF4F5F6)
    else -> Carrot
}

@Composable
private fun AccountMarks(officialMarks: List<String>, isBot: Boolean, isParody: Boolean, isPrivate: Boolean = false, compact: Boolean = false) {
    val marks = officialMarks.filter(String::isOfficialMark).distinct()
    if (marks.isEmpty() && !isBot && !isParody && !isPrivate) return
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(start = 4.dp)) {
        marks.forEach { mark ->
            val markColor = officialMarkColor(mark)
            Box(
                Modifier.size(if (compact) 16.dp else 18.dp),
                contentAlignment = Alignment.Center
            ) {
                CustomIcon(IconType.VERIFIED, markColor, if (compact) 16.dp else 18.dp)
            }
        }
        if (isBot) {
            Box(
                Modifier.size(if (compact) 16.dp else 18.dp).background(Surface, RoundedCornerShape(6.dp))
                    .border(1.dp, Hairline, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                CustomIcon(IconType.BOT, Muted, if (compact) 11.dp else 13.dp)
            }
        }
        if (isParody) {
            Box(
                Modifier.size(if (compact) 16.dp else 18.dp).background(PaleCarrot, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                CustomIcon(IconType.PARODY, Carrot, if (compact) 11.dp else 13.dp)
            }
        }
        if (isPrivate) CustomIcon(IconType.LOCK, Muted, if (compact) 14.dp else 16.dp)
    }
}

@Composable
private fun ReactionVisual(value: String, imageSize: Dp, emojiSize: Int) {
    val proName = value.takeIf { it.startsWith("pro:", ignoreCase = true) }
        ?.substringAfter(':')
        ?.removeSuffix(".png")
        ?.takeIf { it.matches(Regex("[A-Za-z0-9_!-]{1,80}")) }
    var imageFailed by remember(value) { mutableStateOf(false) }
    if (proName != null && !imageFailed) {
        AsyncImage(
            model = "https://karotter.com/reactions/pro/${Uri.encode(proName)}.png",
            contentDescription = "PROリアクション $proName",
            modifier = Modifier.height(imageSize).widthIn(min = imageSize, max = imageSize * 3),
            contentScale = ContentScale.Fit,
            onError = { imageFailed = true }
        )
    } else {
        KText(value, emojiSize, Ink, maxLines = 1)
    }
}

@Composable
private fun ReactionStrip(reactions: List<ApiReaction>, pickerOpen: Boolean, onTogglePicker: () -> Unit, onReact: (String) -> Unit) {
    val viewerIsPro = LocalViewerIsPro.current
    Column {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            item {
                Box(Modifier.size(30.dp).border(1.dp, Hairline, CircleShape).clickable { onTogglePicker() }, contentAlignment = Alignment.Center) {
                    CustomIcon(IconType.PLUS, Muted, 16.dp)
                }
            }
            items(reactions) { reaction ->
                val selectable = isEmojiReaction(reaction.emoji) &&
                    (!reaction.emoji.startsWith("pro:", ignoreCase = true) || viewerIsPro)
                Row(
                    Modifier.clip(RoundedCornerShape(12.dp))
                        .background(if (reaction.reacted) PaleCarrot else Surface)
                        .border(1.dp, if (reaction.reacted) Carrot else Hairline, RoundedCornerShape(12.dp))
                        .clickable(enabled = selectable) { onReact(reaction.emoji) }
                        .alpha(if (selectable) 1f else .52f)
                        .padding(horizontal = 9.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ReactionVisual(reaction.emoji, 22.dp, 14)
                    Spacer(Modifier.width(5.dp))
                    KText(reaction.count.toString(), 10, if (reaction.reacted) Carrot else Muted, FontWeight.Bold)
                }
            }
        }
        if (pickerOpen) {
            var searchQuery by remember { mutableStateOf("") }
            var selectedCategory by remember { mutableStateOf("よく使う") }
            val searchFocusRequester = remember { FocusRequester() }
            val softwareKeyboard = LocalSoftwareKeyboardController.current
            var searchFocused by remember { mutableStateOf(false) }
            val searchActive = searchFocused || searchQuery.isNotEmpty()
            val searchBorderColor by animateColorAsState(
                if (searchActive) Carrot else Hairline,
                tween(180),
                label = "reactionSearchBorder"
            )
            val searchUnderlineProgress by animateFloatAsState(
                if (searchFocused) 1f else 0f,
                tween(220, easing = FastOutSlowInEasing),
                label = "reactionSearchUnderline"
            )
            val categories = remember(viewerIsPro) {
                buildList {
                    addAll(listOf("すべて", "よく使う", "表情", "人・ジェスチャー", "動物・自然", "食べ物", "記号・その他", "旗"))
                    if (viewerIsPro) add("Pro")
                }
            }
            val availableOptions = remember(viewerIsPro) {
                if (viewerIsPro) StandardReactionOptions + ProReactionOptions else StandardReactionOptions
            }
            val visibleOptions = remember(availableOptions, searchQuery, selectedCategory) {
                val normalizedQuery = searchQuery.trim().lowercase()
                availableOptions.filter { option ->
                    if (normalizedQuery.isNotEmpty()) {
                        option.label.lowercase().contains(normalizedQuery) ||
                            option.value.lowercase().contains(normalizedQuery) ||
                            option.searchKeywords.lowercase().contains(normalizedQuery)
                    } else {
                        selectedCategory == "すべて" || option.category == selectedCategory
                    }
                }
            }
            Dialog(onDismissRequest = onTogglePicker) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .heightIn(max = 610.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Paper)
                        .border(1.dp, Hairline, RoundedCornerShape(24.dp))
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            KText("リアクション", 18, Ink, FontWeight.Black)
                            KText(if (viewerIsPro) "絵文字とProリアクションから選択" else "カテゴリや検索から選択", 10, Muted)
                        }
                        Box(Modifier.size(34.dp).border(1.dp, Hairline, CircleShape).clickable { onTogglePicker() }, contentAlignment = Alignment.Center) { CustomIcon(IconType.CLOSE, Ink, 17.dp) }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(
                        Modifier.fillMaxWidth().height(42.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Surface)
                            .border(if (searchFocused) 1.5.dp else 1.dp, searchBorderColor, RoundedCornerShape(14.dp))
                            .drawBehind {
                                if (searchUnderlineProgress > 0f) {
                                    val halfWidth = size.width * searchUnderlineProgress / 2f
                                    drawLine(
                                        color = Carrot,
                                        start = Offset(size.width / 2f - halfWidth, size.height - 1.dp.toPx()),
                                        end = Offset(size.width / 2f + halfWidth, size.height - 1.dp.toPx()),
                                        strokeWidth = 2.dp.toPx(),
                                        cap = StrokeCap.Round
                                    )
                                }
                            }
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier.size(28.dp).clickable {
                                searchFocusRequester.requestFocus()
                                softwareKeyboard?.show()
                            },
                            contentAlignment = Alignment.Center
                        ) {
                            CustomIcon(IconType.SEARCH, Muted, 16.dp)
                        }
                        Spacer(Modifier.width(9.dp))
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            singleLine = true,
                            textStyle = TextStyle(Ink, 12.sp, FontWeight.Medium),
                            cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
                            modifier = Modifier.weight(1f).fillMaxHeight()
                                .focusRequester(searchFocusRequester)
                                .onFocusChanged { searchFocused = it.isFocused },
                            decorationBox = { inner ->
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                                    if (searchQuery.isEmpty()) KText("日本語・絵文字・IDで検索", 11, Muted)
                                    inner()
                                }
                            }
                        )
                        if (searchQuery.isNotEmpty()) {
                            Box(Modifier.size(28.dp).clickable { searchQuery = "" }, contentAlignment = Alignment.Center) {
                                CustomIcon(IconType.CLOSE, Muted, 14.dp)
                            }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(categories, key = { it }) { category ->
                            val selected = searchQuery.isEmpty() && selectedCategory == category
                            Box(
                                Modifier.clip(RoundedCornerShape(11.dp))
                                    .background(if (selected) Strong else Surface)
                                    .border(1.dp, if (selected) Strong else Hairline, RoundedCornerShape(11.dp))
                                    .clickable {
                                        searchQuery = ""
                                        selectedCategory = category
                                    }
                                    .padding(horizontal = 11.dp, vertical = 7.dp)
                            ) {
                                KText(category, 9, if (selected) OnStrong else Muted, FontWeight.Black, maxLines = 1)
                            }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    if (visibleOptions.isEmpty()) {
                        Box(Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
                            KText("一致するリアクションがありません", 11, Muted, FontWeight.Bold)
                        }
                    } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier.fillMaxWidth().heightIn(min = 260.dp, max = 430.dp),
                        contentPadding = PaddingValues(bottom = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(visibleOptions, key = { it.value }) { option ->
                            Column(
                                Modifier.height(62.dp).clip(RoundedCornerShape(12.dp))
                                    .background(Surface)
                                    .clickable { onReact(option.value) }
                                    .padding(horizontal = 4.dp, vertical = 5.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                ReactionVisual(option.value, if (option.isPro) 28.dp else 27.dp, 21)
                                if (option.isPro) {
                                    Spacer(Modifier.height(3.dp))
                                    KText(option.label, 7, Muted, FontWeight.Bold, maxLines = 1)
                                }
                            }
                        }
                    }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuotedPostCard(post: Post, onOpen: (() -> Unit)?) {
    Column(
        Modifier.fillMaxWidth().border(1.dp, Hairline, RoundedCornerShape(15.dp)).clickable(enabled = onOpen != null) { onOpen?.invoke() }.padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(24.dp).background(post.avatar, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                if (post.avatarUrl != null) AsyncImage(post.avatarUrl, post.name, Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
                else KText(post.name.take(1), 10, Ink, FontWeight.Bold)
            }
            Spacer(Modifier.width(8.dp))
            KText(post.name, 12, Ink, FontWeight.Bold)
            AccountMarks(post.officialMarks.ifEmpty { listOf(post.officialMark) }, post.isBotAccount, post.isParodyAccount, post.isPrivateAccount, compact = true)
            Spacer(Modifier.width(5.dp))
            KText(post.handle, 10, Muted, maxLines = 1)
        }
        if (post.text.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            RichContentText(post.text, 13, Ink, lineHeight = 19f, maxLines = 5, onPlainTextClick = onOpen)
        }
        if (post.media.isNotEmpty()) {
            Spacer(Modifier.height(9.dp))
            MediaAttachment(post.media.first(), compact = true)
        }
    }
}

@Composable
private fun PostLinkPreviewCard(url: String) {
    val api = LocalLinkPreviewApi.current ?: return
    val context = LocalContext.current
    var preview by remember(url) { mutableStateOf<ApiLinkPreview?>(null) }
    var loading by remember(url) { mutableStateOf(true) }
    LaunchedEffect(url, api) {
        when (val result = withContext(Dispatchers.IO) { api.linkPreview(url) }) {
            is ApiResult.Success -> preview = result.value
            is ApiResult.Failure -> Unit
        }
        loading = false
    }
    val loaded = preview
    if (!loading && loaded == null) return
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(17.dp))
            .background(Surface)
            .border(1.dp, Hairline, RoundedCornerShape(17.dp))
            .clickable(enabled = loaded != null) {
                val destination = loaded?.url ?: url
                runCatching {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(destination)))
                }
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (loading) {
            Box(Modifier.fillMaxWidth().height(90.dp), contentAlignment = Alignment.Center) {
                KText("リンク情報を取得中…", 10, Muted, FontWeight.Bold)
            }
        } else if (loaded != null) {
            loaded.imageUrl?.let { imageUrl ->
                AsyncImage(
                    imageUrl,
                    loaded.title,
                    Modifier.width(104.dp).height(104.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column(Modifier.weight(1f).padding(horizontal = 13.dp, vertical = 11.dp)) {
                KText(
                    loaded.siteName.ifBlank { Uri.parse(loaded.url).host.orEmpty() },
                    9,
                    Muted,
                    FontWeight.Black,
                    letterSpacing = .6f,
                    maxLines = 1
                )
                if (loaded.title.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    KText(loaded.title, 12, Ink, FontWeight.Bold, lineHeight = 17f, maxLines = 2)
                }
                if (loaded.description.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    KText(loaded.description, 10, Muted, lineHeight = 15f, maxLines = 2)
                }
                if (loaded.title.isBlank() && loaded.description.isBlank()) {
                    Spacer(Modifier.height(4.dp))
                    KText(loaded.url, 10, Ink, FontWeight.Medium, maxLines = 2)
                }
            }
            Box(Modifier.padding(end = 10.dp).size(28.dp), contentAlignment = Alignment.Center) {
                CustomIcon(IconType.FORWARD, Muted, 14.dp)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MediaGallery(media: List<ApiMedia>) {
    if (media.isEmpty()) return
    val images = remember(media) {
        media.filter {
            val type = it.type.lowercase()
            "video" !in type && "audio" !in type
        }
    }
    var fullScreenImageIndex by remember(media) { mutableStateOf<Int?>(null) }
    fullScreenImageIndex?.let { initialPage ->
        FullScreenMediaGallery(
            media = images,
            initialPage = initialPage.coerceIn(0, images.lastIndex.coerceAtLeast(0)),
            onDismiss = { fullScreenImageIndex = null }
        )
    }
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        media.chunked(2).forEach { rowItems ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                rowItems.forEach { item ->
                    Box(Modifier.weight(1f)) {
                        val imageIndex = images.indexOfFirst { it.url == item.url }
                        MediaAttachment(
                            media = item,
                            onOpen = if (imageIndex >= 0) {
                                { fullScreenImageIndex = imageIndex }
                            } else null
                        )
                    }
                }
                if (rowItems.size == 1 && media.size > 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SpoilerAwareMediaThumbnail(
    media: ApiMedia,
    modifier: Modifier = Modifier,
    onOpen: () -> Unit
) {
    var spoilerRevealed by remember(media.url, media.spoiler) { mutableStateOf(!media.spoiler) }
    Box(
        modifier
            .clip(RoundedCornerShape(3.dp))
            .background(Hairline)
            .clickable {
                if (spoilerRevealed) onOpen() else spoilerRevealed = true
            },
        contentAlignment = Alignment.Center
    ) {
        if (spoilerRevealed) {
            AsyncImage(
                model = media.url,
                contentDescription = media.alt.ifBlank { "投稿メディア" },
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Column(
                Modifier.fillMaxSize().background(Strong).padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomIcon(IconType.EYE, OnStrong, 19.dp)
                Spacer(Modifier.height(5.dp))
                KText("センシティブなメディア", 8, OnStrong, FontWeight.Black, textAlign = TextAlign.Center)
                KText("タップして表示", 7, OnStrong.copy(alpha = .72f), FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FullScreenMediaGallery(
    media: List<ApiMedia>,
    initialPage: Int,
    onDismiss: () -> Unit
) {
    if (media.isEmpty()) return
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { media.size }
    )
    var zoomScale by remember { mutableStateOf(1f) }
    var zoomOffset by remember { mutableStateOf(Offset.Zero) }
    LaunchedEffect(pagerState.currentPage) {
        zoomScale = 1f
        zoomOffset = Offset.Zero
    }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Box(Modifier.fillMaxSize().background(Color(0xF20A0B0D))) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                pageSpacing = 12.dp,
                userScrollEnabled = zoomScale <= 1.01f,
                verticalAlignment = Alignment.CenterVertically
            ) { page ->
                val item = media[page]
                val isGif = "gif" in item.type.lowercase() ||
                    item.url.substringBefore('?').lowercase().endsWith(".gif")
                Box(
                    Modifier.fillMaxSize().padding(horizontal = 10.dp, vertical = 58.dp)
                        .graphicsLayer {
                            scaleX = zoomScale
                            scaleY = zoomScale
                            translationX = zoomOffset.x
                            translationY = zoomOffset.y
                        }
                        .pointerInput(item.url, pagerState.currentPage) {
                            awaitEachGesture {
                                awaitFirstDown(requireUnconsumed = false)
                                var transforming = zoomScale > 1.01f
                                do {
                                    val event = awaitPointerEvent()
                                    val pressedCount = event.changes.count { it.pressed }
                                    if (pressedCount >= 2 || transforming) {
                                        val nextScale = (zoomScale * event.calculateZoom()).coerceIn(1f, 5f)
                                        zoomScale = nextScale
                                        zoomOffset = if (nextScale <= 1.01f) {
                                            Offset.Zero
                                        } else {
                                            zoomOffset + event.calculatePan()
                                        }
                                        transforming = nextScale > 1.01f || pressedCount >= 2
                                        event.changes.forEach { it.consume() }
                                    }
                                } while (event.changes.any { it.pressed })
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (isGif) {
                        GifAttachment(
                            media = item,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        AsyncImage(
                            model = item.url,
                            contentDescription = item.alt.ifBlank { "全画面画像" },
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
            if (media.size > 1) {
                Box(
                    Modifier.align(Alignment.TopCenter).statusBarsPadding().padding(top = 14.dp)
                        .background(Color.Black.copy(.52f), RoundedCornerShape(18.dp))
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                ) {
                    KText(
                        "${pagerState.currentPage + 1} / ${media.size}",
                        11,
                        Color.White,
                        FontWeight.Bold
                    )
                }
            }
            Box(
                Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(bottom = 14.dp)
                    .background(Color.Black.copy(.52f), RoundedCornerShape(18.dp))
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                KText(
                    if (zoomScale > 1.01f) "ピンチで縮小すると画像を切り替えられます" else "ピンチで拡大",
                    10,
                    Color.White.copy(alpha = .82f),
                    FontWeight.Bold
                )
            }
            Box(
                Modifier.align(Alignment.TopEnd).statusBarsPadding().padding(14.dp).size(42.dp)
                    .background(Color.Black.copy(.55f), CircleShape)
                    .border(1.dp, Color.White.copy(.25f), CircleShape)
                    .clickable { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                CustomIcon(IconType.CLOSE, Color.White, 22.dp)
            }
        }
    }
}

private data class ViewportPlayback(val active: Boolean, val modifier: Modifier)

@Composable
private fun rememberViewportPlayback(): ViewportPlayback {
    val hostView = LocalView.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var inViewport by remember { mutableStateOf(false) }
    var resumed by remember(lifecycleOwner) {
        mutableStateOf(lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
    }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, _ ->
            resumed = lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    val positionModifier = Modifier.onGloballyPositioned { coordinates ->
        val bounds = coordinates.boundsInWindow()
        val viewportWidth = hostView.width.toFloat()
        val viewportHeight = hostView.height.toFloat()
        val visibleWidth = (minOf(bounds.right, viewportWidth) - maxOf(bounds.left, 0f)).coerceAtLeast(0f)
        val visibleHeight = (minOf(bounds.bottom, viewportHeight) - maxOf(bounds.top, 0f)).coerceAtLeast(0f)
        val widthRatio = if (bounds.width > 0f) visibleWidth / bounds.width else 0f
        val heightRatio = if (bounds.height > 0f) visibleHeight / bounds.height else 0f
        inViewport = widthRatio >= .35f && heightRatio >= .35f
    }
    return ViewportPlayback(resumed && inViewport, positionModifier)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MediaAttachment(
    media: ApiMedia,
    compact: Boolean = false,
    onOpen: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val kind = media.type.lowercase()
    val isGif = "gif" in kind || media.url.substringBefore('?').lowercase().endsWith(".gif")
    var fullScreen by remember(media.url) { mutableStateOf(false) }
    var imageMenuOpen by remember(media.url) { mutableStateOf(false) }
    var spoilerRevealed by remember(media.url, media.spoiler) { mutableStateOf(!media.spoiler) }
    val isImage = "video" !in kind && "audio" !in kind
    val saveMimeType = media.type.takeIf { it.startsWith("image/", ignoreCase = true) } ?: "image/*"
    val saveFileName = remember(media.url, saveMimeType) {
        val fallbackExtension = when {
            "gif" in saveMimeType -> "gif"
            "png" in saveMimeType -> "png"
            "webp" in saveMimeType -> "webp"
            else -> "jpg"
        }
        Uri.parse(media.url).lastPathSegment
            ?.substringBefore('?')
            ?.takeIf { it.isNotBlank() }
            ?.replace(Regex("[^A-Za-z0-9._-]"), "_")
            ?: "Karoha-image-${System.currentTimeMillis()}.$fallbackExtension"
    }
    val saveImageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument(saveMimeType)
    ) { destination ->
        if (destination != null) {
            scope.launch {
                val saved = withContext(Dispatchers.IO) {
                    runCatching {
                        URL(media.url).openConnection().apply {
                            connectTimeout = 15_000
                            readTimeout = 30_000
                            setRequestProperty("User-Agent", "Karoha")
                        }.getInputStream().use { input ->
                            context.contentResolver.openOutputStream(destination)?.use { output ->
                                input.copyTo(output)
                            } ?: error("Cannot open destination")
                        }
                    }.isSuccess
                }
                Toast.makeText(
                    context,
                    if (saved) "画像を保存しました" else "画像を保存できませんでした",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    val imageInteractionModifier = Modifier.combinedClickable(
        onClick = { onOpen?.invoke() ?: run { fullScreen = true } },
        onLongClick = { imageMenuOpen = true }
    )
    if (imageMenuOpen) {
        val systemNavigationClearance =
            WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 28.dp
        Dialog(
            onDismissRequest = { imageMenuOpen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Box(Modifier.fillMaxSize().clickable { imageMenuOpen = false })
                AnimatedVisibility(
                    visible = imageMenuOpen,
                    enter = slideInVertically(tween(280, easing = FastOutSlowInEasing)) { it } + fadeIn(tween(160)),
                    exit = slideOutVertically(tween(210, easing = FastOutSlowInEasing)) { it } + fadeOut(tween(140))
                ) {
                    Column(
                        Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 27.dp, topEnd = 27.dp))
                            .background(Surface)
                            .border(
                                1.dp,
                                Hairline,
                                RoundedCornerShape(topStart = 27.dp, topEnd = 27.dp)
                            )
                            .clickable { }
                            .padding(
                                start = 18.dp,
                                top = 10.dp,
                                end = 18.dp,
                                bottom = systemNavigationClearance
                            )
                    ) {
                        Box(
                            Modifier.width(38.dp).height(4.dp)
                                .background(Hairline, RoundedCornerShape(3.dp))
                                .align(Alignment.CenterHorizontally)
                        )
                        Spacer(Modifier.height(14.dp))
                        KText("画像", 16, Ink, FontWeight.Black)
                        KText("画像に対する操作を選択", 10, Muted, FontWeight.Medium)
                        Spacer(Modifier.height(14.dp))
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp))
                                .clickable {
                                    imageMenuOpen = false
                                    onOpen?.invoke() ?: run { fullScreen = true }
                                }
                                .padding(horizontal = 10.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                Modifier.size(40.dp).background(Paper, RoundedCornerShape(13.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                CustomIcon(IconType.EYE, Ink, 19.dp)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                KText("全画面で表示", 13, Ink, FontWeight.Bold)
                                KText("画像を拡大して表示します", 9, Muted)
                            }
                        }
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp))
                                .clickable {
                                    imageMenuOpen = false
                                    saveImageLauncher.launch(saveFileName)
                                }
                                .padding(horizontal = 10.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                Modifier.size(40.dp).background(PaleCarrot, RoundedCornerShape(13.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                CustomIcon(IconType.IMAGE, Carrot, 19.dp)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                KText("画像を保存", 13, Ink, FontWeight.Bold)
                                KText("端末内の保存先を選択します", 9, Muted)
                            }
                        }
                    }
                }
            }
        }
    }
    if (fullScreen && "video" !in kind && "audio" !in kind) {
        var zoomScale by remember(media.url) { mutableStateOf(1f) }
        var zoomOffset by remember(media.url) { mutableStateOf(Offset.Zero) }
        val zoomModifier = Modifier.fillMaxSize().padding(12.dp)
            .graphicsLayer {
                scaleX = zoomScale
                scaleY = zoomScale
                translationX = zoomOffset.x
                translationY = zoomOffset.y
            }
            .pointerInput(media.url) {
                detectTransformGestures { _, pan, zoom, _ ->
                    val nextScale = (zoomScale * zoom).coerceIn(1f, 5f)
                    zoomScale = nextScale
                    zoomOffset = if (nextScale == 1f) Offset.Zero else zoomOffset + pan
                }
            }
        Dialog(
            onDismissRequest = { fullScreen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
        ) {
            Box(
                Modifier.fillMaxSize().background(Color(0xF20A0B0D)).clickable { fullScreen = false },
                contentAlignment = Alignment.Center
            ) {
                if (isGif) {
                    GifAttachment(
                        media = media,
                        modifier = zoomModifier,
                        contentScale = ContentScale.Fit
                    )
                } else {
                    AsyncImage(
                        model = media.url,
                        contentDescription = media.alt.ifBlank { "全画面画像" },
                        modifier = zoomModifier,
                        contentScale = ContentScale.Fit
                    )
                }
                Box(
                    Modifier.align(Alignment.TopEnd).padding(20.dp).size(42.dp).background(Color.Black.copy(.55f), CircleShape).border(1.dp, Color.White.copy(.25f), CircleShape).clickable { fullScreen = false },
                    contentAlignment = Alignment.Center
                ) { CustomIcon(IconType.CLOSE, Color.White, 22.dp) }
            }
        }
    }
    if (!spoilerRevealed && isImage) {
        Box(
            Modifier.fillMaxWidth().height(if (compact) 110.dp else 180.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Strong)
                .clickable { spoilerRevealed = true },
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier.size(38.dp).background(OnStrong.copy(alpha = .12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    CustomIcon(IconType.EYE, OnStrong, 19.dp)
                }
                Spacer(Modifier.height(9.dp))
                KText("センシティブなメディア", 11, OnStrong, FontWeight.Black, letterSpacing = .6f)
                Spacer(Modifier.height(3.dp))
                KText("タップして表示", 9, OnStrong.copy(alpha = .7f), FontWeight.Bold)
            }
        }
        return
    }
    when {
        "video" in kind -> VideoAttachment(media, compact)
        "audio" in kind -> AudioAttachment(media)
        isGif -> GifAttachment(
            media = media,
            modifier = Modifier.fillMaxWidth().height(if (compact) 110.dp else 180.dp)
                .clip(RoundedCornerShape(16.dp)).background(Hairline).then(imageInteractionModifier),
            contentScale = ContentScale.Crop
        )
        else -> AsyncImage(
            media.url,
            media.alt.ifBlank { "投稿画像" },
            Modifier.fillMaxWidth().height(if (compact) 110.dp else 180.dp).clip(RoundedCornerShape(16.dp)).background(Hairline).then(imageInteractionModifier),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun GifAttachment(media: ApiMedia, modifier: Modifier, contentScale: ContentScale) {
    val playback = rememberViewportPlayback()
    var animation by remember(media.url) { mutableStateOf<DrawableAnimatable?>(null) }
    LaunchedEffect(playback.active, animation) {
        animation?.let { drawable ->
            if (playback.active) drawable.start() else drawable.stop()
        }
    }
    DisposableEffect(media.url) {
        onDispose { animation?.stop() }
    }
    AsyncImage(
        model = media.url,
        contentDescription = media.alt.ifBlank { "GIF画像" },
        modifier = playback.modifier.then(modifier),
        contentScale = contentScale,
        onState = { state ->
            if (state is AsyncImagePainter.State.Success) {
                animation = state.result.drawable as? DrawableAnimatable
            }
        }
    )
}

@Composable
private fun VideoAttachment(media: ApiMedia, compact: Boolean) {
    val playback = rememberViewportPlayback()
    var videoView by remember(media.url) { mutableStateOf<VideoView?>(null) }
    var player by remember(media.url) { mutableStateOf<MediaPlayer?>(null) }
    var prepared by remember(media.url) { mutableStateOf(false) }
    var userPaused by remember(media.url) { mutableStateOf(false) }
    var muted by remember(media.url) { mutableStateOf(true) }
    val shouldPlay = playback.active && prepared && !userPaused

    LaunchedEffect(shouldPlay, videoView) {
        videoView?.let { view ->
            if (shouldPlay) view.start() else if (view.isPlaying) view.pause()
        }
    }
    LaunchedEffect(muted, player) {
        player?.setVolume(if (muted) 0f else 1f, if (muted) 0f else 1f)
    }
    LaunchedEffect(playback.active) {
        if (playback.active) userPaused = false
    }
    DisposableEffect(media.url) {
        onDispose {
            videoView?.stopPlayback()
            videoView = null
            player = null
        }
    }

    Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color(0xFF111315))) {
        Box(
            playback.modifier.fillMaxWidth().height(if (compact) 120.dp else 210.dp)
        ) {
            AndroidView(
                factory = { context ->
                    VideoView(context).apply {
                        videoView = this
                        setVideoURI(Uri.parse(media.url))
                        setOnPreparedListener { preparedPlayer ->
                            player = preparedPlayer
                            prepared = true
                            preparedPlayer.isLooping = true
                            preparedPlayer.setVolume(if (muted) 0f else 1f, if (muted) 0f else 1f)
                        }
                        setOnClickListener {
                            userPaused = isPlaying
                            if (isPlaying) pause() else start()
                        }
                        setOnCompletionListener { if (playback.active && !userPaused) start() }
                    }
                },
                update = { current ->
                    videoView = current
                },
                modifier = Modifier.fillMaxSize()
            )
            Box(
                Modifier.align(Alignment.BottomEnd).padding(10.dp).size(36.dp)
                    .background(Color.Black.copy(.62f), CircleShape)
                    .border(1.dp, Color.White.copy(.2f), CircleShape)
                    .clickable { muted = !muted },
                contentAlignment = Alignment.Center
            ) {
                CustomIcon(if (muted) IconType.VOLUME_OFF else IconType.VOLUME_ON, Color.White, 18.dp)
            }
        }
        KText(
            when {
                !prepared -> "動画を読み込み中…"
                !playback.active -> "画面外のため停止中"
                userPaused -> "停止中 · タップで再生"
                else -> "自動再生中 · ${if (muted) "ミュート" else "音声あり"}"
            },
            10,
            Color.White.copy(.75f),
            FontWeight.Bold,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
private fun AudioAttachment(media: ApiMedia) {
    var ready by remember(media.url) { mutableStateOf(false) }
    var playing by remember(media.url) { mutableStateOf(false) }
    var failed by remember(media.url) { mutableStateOf(false) }
    val player = remember(media.url) {
        MediaPlayer().apply {
            setDataSource(media.url)
            setOnPreparedListener { ready = true }
            setOnCompletionListener { playing = false; seekTo(0) }
            setOnErrorListener { _, _, _ -> failed = true; true }
            prepareAsync()
        }
    }
    DisposableEffect(player) { onDispose { player.release() } }
    Row(
        Modifier.fillMaxWidth().height(70.dp).background(Color(0xFF111315), RoundedCornerShape(16.dp))
            .clickable(enabled = ready && !failed) { if (player.isPlaying) { player.pause(); playing = false } else { player.start(); playing = true } }
            .padding(horizontal = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(38.dp).background(Carrot, CircleShape), contentAlignment = Alignment.Center) { KText(if (playing) "Ⅱ" else "▶", 14, Color.White, FontWeight.Black) }
        Spacer(Modifier.width(12.dp))
        Column { KText(media.alt.ifBlank { "音声" }, 12, Color.White, FontWeight.Bold, maxLines = 1); KText(if (failed) "再生できません" else if (ready) "タップで再生・停止" else "読み込み中…", 10, Color.White.copy(.62f)) }
    }
}

@Composable
private fun FeaturedImage(url: String? = null) {
    if (url != null) {
        AsyncImage(
            model = url,
            contentDescription = "投稿画像",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(190.dp).clip(RoundedCornerShape(18.dp)).background(Sky)
        )
        return
    }
    Canvas(Modifier.fillMaxWidth().height(158.dp).clip(RoundedCornerShape(18.dp)).background(Sky)) {
        drawCircle(Color(0xFFFFD99A), radius = 32.dp.toPx(), center = Offset(size.width * .78f, size.height * .27f))
        val hills = Path().apply {
            moveTo(0f, size.height * .72f)
            quadraticBezierTo(size.width * .28f, size.height * .35f, size.width * .52f, size.height * .74f)
            quadraticBezierTo(size.width * .76f, size.height * .43f, size.width, size.height * .70f)
            lineTo(size.width, size.height); lineTo(0f, size.height); close()
        }
        drawPath(hills, Color(0xFFA8D7B7))
        drawLine(Color.White.copy(alpha = .85f), Offset(size.width * .45f, size.height), Offset(size.width * .58f, size.height * .64f), 4.dp.toPx(), StrokeCap.Round)
    }
}

@Composable
private fun Action(type: IconType, count: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        CustomIcon(type, Muted, 17.dp)
        Spacer(Modifier.width(6.dp))
        KText(count, 11, Muted, FontWeight.Medium)
    }
}

@Composable
private fun ErrorStrip(message: String, retry: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().background(PaleCarrot).clickable { retry() }.padding(horizontal = 22.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        KText(message, 11, Ink, FontWeight.Medium, modifier = Modifier.weight(1f), maxLines = 2)
        Spacer(Modifier.width(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            KText("再試行", 11, Carrot, FontWeight.Black)
            CustomIcon(IconType.REFRESH, Carrot, 13.dp)
        }
    }
}

@Composable
private fun ErrorText(message: String) {
    KText(message, 11, Carrot, FontWeight.Bold, modifier = Modifier.fillMaxWidth().background(PaleCarrot).padding(horizontal = 22.dp, vertical = 12.dp), maxLines = 3)
}

@Composable
private fun LoadingPost() {
    val pulse = rememberInfiniteTransition(label = "loading")
    val alpha by pulse.animateFloat(.35f, .8f, infiniteRepeatable(tween(800), repeatMode = androidx.compose.animation.core.RepeatMode.Reverse), label = "loadingAlpha")
    Row(Modifier.fillMaxWidth().padding(22.dp).alpha(alpha)) {
        Box(Modifier.size(43.dp).background(Hairline, RoundedCornerShape(15.dp)))
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Box(Modifier.width(120.dp).height(12.dp).background(Hairline, CircleShape))
            Spacer(Modifier.height(13.dp))
            Box(Modifier.fillMaxWidth().height(12.dp).background(Hairline, CircleShape))
            Spacer(Modifier.height(8.dp))
            Box(Modifier.fillMaxWidth(.7f).height(12.dp).background(Hairline, CircleShape))
        }
    }
}

@Composable
private fun CreateCommunityDialog(
    api: KarotterApi,
    onDismiss: () -> Unit,
    onCreated: (ApiCommunity) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var creating by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    Dialog(onDismissRequest = { if (!creating) onDismiss() }) {
        Column(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(Surface)
                .border(1.dp, Hairline, RoundedCornerShape(24.dp)).padding(22.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(42.dp).background(PaleCarrot, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                    CustomIcon(IconType.COMMUNITY, Carrot, 21.dp)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    KText("コミュニティを作成", 17, Ink, FontWeight.Black)
                    KText("KAROTTER PRO", 9, Carrot, FontWeight.Black, letterSpacing = 1.4f)
                }
                Box(
                    Modifier.size(34.dp).border(1.dp, Hairline, CircleShape)
                        .clickable(enabled = !creating) { onDismiss() },
                    contentAlignment = Alignment.Center
                ) { CustomIcon(IconType.CLOSE, Ink, 15.dp) }
            }
            Spacer(Modifier.height(19.dp))
            KText("名前", 10, Muted, FontWeight.Bold)
            Spacer(Modifier.height(7.dp))
            BasicTextField(
                value = name,
                onValueChange = { name = it.take(50); error = null },
                singleLine = true,
                textStyle = TextStyle(Ink, 14.sp, FontWeight.Bold),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
                modifier = Modifier.fillMaxWidth().height(48.dp).background(Paper, RoundedCornerShape(14.dp))
                    .border(1.dp, Hairline, RoundedCornerShape(14.dp)).padding(horizontal = 14.dp),
                decorationBox = { inner ->
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                        if (name.isBlank()) KText("コミュニティ名", 13, Muted)
                        inner()
                    }
                }
            )
            Spacer(Modifier.height(14.dp))
            KText("説明", 10, Muted, FontWeight.Bold)
            Spacer(Modifier.height(7.dp))
            BasicTextField(
                value = description,
                onValueChange = { description = it.take(300); error = null },
                textStyle = TextStyle(Ink, 13.sp, FontWeight.Medium, lineHeight = 20.sp),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
                modifier = Modifier.fillMaxWidth().height(112.dp).background(Paper, RoundedCornerShape(14.dp))
                    .border(1.dp, Hairline, RoundedCornerShape(14.dp)).padding(14.dp),
                decorationBox = { inner ->
                    Box {
                        if (description.isBlank()) KText("どんなコミュニティか説明してください", 12, Muted)
                        inner()
                    }
                }
            )
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth()) {
                KText("参加方式：公開", 9, Muted, FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                KText("${description.length} / 300", 9, Muted, FontWeight.Bold)
            }
            error?.let {
                Spacer(Modifier.height(10.dp))
                ErrorText(it)
            }
            Spacer(Modifier.height(18.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                Box(
                    Modifier.weight(1f).border(1.dp, Hairline, RoundedCornerShape(14.dp))
                        .clickable(enabled = !creating) { onDismiss() }.padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) { KText("キャンセル", 11, Ink, FontWeight.Bold) }
                val canCreate = name.isNotBlank() && description.isNotBlank() && !creating
                Box(
                    Modifier.weight(1f).background(if (canCreate) Carrot else Hairline, RoundedCornerShape(14.dp))
                        .clickable(enabled = canCreate) {
                            creating = true
                            error = null
                            scope.launch {
                                when (val result = withContext(Dispatchers.IO) {
                                    api.createCommunity(name.trim(), description.trim())
                                }) {
                                    is ApiResult.Success -> onCreated(result.value)
                                    is ApiResult.Failure -> {
                                        error = result.message
                                        creating = false
                                    }
                                }
                            }
                        }.padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    KText(if (creating) "作成中…" else "作成", 11, if (canCreate) Color.White else Muted, FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun CommunityScreen(
    api: KarotterApi,
    currentUser: ApiUser?,
    onPost: (Post) -> Unit,
    onUser: (ApiUser) -> Unit,
    onReply: (Post) -> Unit,
    onQuote: (Post) -> Unit,
    onRekarot: (Post, Boolean) -> Unit,
    onLike: (Post, Boolean) -> Unit,
    onBookmark: (Post, Boolean) -> Unit,
    onCompose: (ApiCommunity) -> Unit,
    latestCommunityPost: Pair<Long, Post>?
) {
    var groups by remember { mutableStateOf<ApiCommunityGroups?>(null) }
    var selected by remember { mutableStateOf<ApiCommunity?>(null) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var reloadKey by remember { mutableIntStateOf(0) }
    var createOpen by remember { mutableStateOf(false) }
    val canCreateCommunity = currentUser?.subscriptionPlan.equals("PRO", true) &&
        currentUser?.subscriptionStatus.equals("ACTIVE", true)
    if (createOpen) {
        CreateCommunityDialog(
            api = api,
            onDismiss = { createOpen = false },
            onCreated = { community ->
                val current = groups ?: ApiCommunityGroups(emptyList(), emptyList(), emptyList())
                groups = current.copy(
                    owned = (listOf(community.copy(isMember = true)) + current.owned).distinctBy(ApiCommunity::id)
                )
                createOpen = false
                selected = community.copy(isMember = true)
            }
        )
    }
    BackHandler(enabled = selected != null && LocalNavigationActive.current) {
        selected = null
        reloadKey += 1
    }
    LaunchedEffect(reloadKey) {
        loading = true
        when (val result = withContext(Dispatchers.IO) { api.communities() }) {
            is ApiResult.Success -> {
                groups = result.value
                error = null
            }
            is ApiResult.Failure -> error = result.message
        }
        loading = false
    }
    AnimatedContent(
        targetState = selected,
        modifier = Modifier.fillMaxSize().clipToBounds(),
        transitionSpec = {
            if (targetState != null) {
                (slideInHorizontally(tween(320, easing = FastOutSlowInEasing)) { it / 3 } + fadeIn(tween(220)))
                    .togetherWith(
                        slideOutHorizontally(tween(320, easing = FastOutSlowInEasing)) { -it / 4 } +
                            fadeOut(tween(180))
                    )
            } else {
                (slideInHorizontally(tween(320, easing = FastOutSlowInEasing)) { -it / 4 } + fadeIn(tween(220)))
                    .togetherWith(
                        slideOutHorizontally(tween(320, easing = FastOutSlowInEasing)) { it / 3 } +
                            fadeOut(tween(180))
                    )
            }
        },
        label = "communityNavigation"
    ) { targetCommunity ->
        if (targetCommunity != null) {
            CommunityDetailScreen(
                initial = targetCommunity,
                api = api,
                currentUser = currentUser,
                onBack = {
                    selected = null
                    reloadKey += 1
                },
                onPost = onPost,
                onUser = onUser,
                onReply = onReply,
                onQuote = onQuote,
                onRekarot = onRekarot,
                onLike = onLike,
                onBookmark = onBookmark,
                onCompose = onCompose,
                latestCommunityPost = latestCommunityPost
            )
        } else {
            val joined = groups?.joined.orEmpty().distinctBy(ApiCommunity::id)
            val owned = groups?.owned.orEmpty().distinctBy(ApiCommunity::id)
            val ownedIds = owned.mapTo(hashSetOf(), ApiCommunity::id)
            val joinedIds = joined.mapTo(hashSetOf(), ApiCommunity::id)
            val recommended = groups?.recommended.orEmpty()
                .filterNot { it.id in joinedIds || it.id in ownedIds }
                .distinctBy(ApiCommunity::id)
            LazyColumn(
                Modifier.fillMaxSize().background(Paper),
                contentPadding = PaddingValues(bottom = bottomDockContentInset() + 24.dp)
            ) {
        item {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    KText("COMMUNITIES", 10, Carrot, FontWeight.Black, letterSpacing = 1.8f)
                    Spacer(Modifier.height(4.dp))
                    KText("コミュニティ", 27, Ink, FontWeight.Black)
                    KText("参加している場所と、新しいつながり", 10, Muted)
                }
                if (canCreateCommunity) {
                    Box(
                        Modifier.size(40.dp).background(Carrot, RoundedCornerShape(13.dp))
                            .clickable { createOpen = true },
                        contentAlignment = Alignment.Center
                    ) {
                        CustomIcon(IconType.PLUS, Color.White, 18.dp)
                    }
                    Spacer(Modifier.width(8.dp))
                }
                Box(
                    Modifier.size(40.dp).background(Surface, RoundedCornerShape(13.dp))
                        .border(1.dp, Hairline, RoundedCornerShape(13.dp))
                        .clickable(enabled = !loading) { reloadKey += 1 },
                    contentAlignment = Alignment.Center
                ) {
                    CustomIcon(IconType.REFRESH, Ink, 17.dp)
                }
            }
        }
        if (loading && groups == null) items(5) { LoadingPost() }
        error?.let { message ->
            item {
                Column(Modifier.padding(horizontal = 22.dp, vertical = 12.dp)) {
                    ErrorText(message)
                    Spacer(Modifier.height(10.dp))
                    Box(
                        Modifier.clip(RoundedCornerShape(12.dp)).background(Carrot)
                            .clickable { reloadKey += 1 }.padding(horizontal = 16.dp, vertical = 9.dp)
                    ) { KText("再試行", 10, Color.White, FontWeight.Black) }
                }
            }
        }
        if (owned.isNotEmpty()) {
            item { CommunitySectionTitle("管理中", "${owned.size}件のコミュニティ") }
            items(owned, key = { "owned-${it.id}" }) { CommunityListCard(it, true) { selected = it } }
        }
        val joinedWithoutOwned = joined.filterNot { it.id in ownedIds }
        if (joinedWithoutOwned.isNotEmpty()) {
            item { CommunitySectionTitle("参加中", "${joinedWithoutOwned.size}件のコミュニティ") }
            items(joinedWithoutOwned, key = { "joined-${it.id}" }) { CommunityListCard(it, true) { selected = it } }
        }
        if (recommended.isNotEmpty()) {
            item { CommunitySectionTitle("おすすめ", "参加できるコミュニティ") }
            items(recommended, key = { "recommended-${it.id}" }) { CommunityListCard(it, false) { selected = it } }
        }
        if (!loading && error == null && owned.isEmpty() && joined.isEmpty() && recommended.isEmpty()) {
            item {
                Column(
                    Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 42.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomIcon(IconType.COMMUNITY, Muted, 32.dp)
                    Spacer(Modifier.height(12.dp))
                    KText("表示できるコミュニティはありません", 12, Muted, FontWeight.Bold)
                }
            }
        }
            }
        }
    }
}

@Composable
private fun CommunitySectionTitle(title: String, subtitle: String) {
    Column(Modifier.padding(start = 22.dp, top = 18.dp, end = 22.dp, bottom = 8.dp)) {
        KText(title, 13, Ink, FontWeight.Black)
        KText(subtitle, 9, Muted)
    }
}

@Composable
private fun CommunityListCard(community: ApiCommunity, member: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.padding(horizontal = 18.dp, vertical = 5.dp).fillMaxWidth()
            .clip(RoundedCornerShape(19.dp)).background(Surface)
            .border(1.dp, Hairline, RoundedCornerShape(19.dp))
            .clickable { onClick() }.padding(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(52.dp).clip(RoundedCornerShape(16.dp)).background(PaleCarrot),
            contentAlignment = Alignment.Center
        ) {
            if (community.headerImageUrl != null) {
                AsyncImage(community.headerImageUrl, community.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            } else CustomIcon(IconType.COMMUNITY, Carrot, 23.dp)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            KText(community.name, 14, Ink, FontWeight.Black, maxLines = 1)
            KText(community.description.ifBlank { "コミュニティの説明はありません" }, 10, Muted, maxLines = 2, lineHeight = 15f)
            Spacer(Modifier.height(5.dp))
            KText("${community.memberCount}人 · ${if (community.joinType == "OPEN") "公開" else "承認制"}", 8, Muted, FontWeight.Bold)
        }
        if (member) {
            Box(Modifier.background(PaleCarrot, RoundedCornerShape(8.dp)).padding(horizontal = 7.dp, vertical = 5.dp)) {
                KText("参加中", 8, Carrot, FontWeight.Black)
            }
        } else CustomIcon(IconType.FORWARD, Muted, 16.dp)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CommunityDetailScreen(
    initial: ApiCommunity,
    api: KarotterApi,
    currentUser: ApiUser?,
    onBack: () -> Unit,
    onPost: (Post) -> Unit,
    onUser: (ApiUser) -> Unit,
    onReply: (Post) -> Unit,
    onQuote: (Post) -> Unit,
    onRekarot: (Post, Boolean) -> Unit,
    onLike: (Post, Boolean) -> Unit,
    onBookmark: (Post, Boolean) -> Unit,
    onCompose: (ApiCommunity) -> Unit,
    latestCommunityPost: Pair<Long, Post>?
) {
    var community by remember(initial.id) { mutableStateOf(initial) }
    var selectedTab by remember(initial.id) { mutableStateOf("trending") }
    val communityTabs = remember { listOf("trending", "latest", "media", "members") }
    val communityTabMotion = rememberHorizontalTabMotion(
        communityTabs.indexOf(selectedTab).coerceAtLeast(0),
        initial.id
    )
    var posts by remember(initial.id) { mutableStateOf<List<Post>>(emptyList()) }
    var members by remember(initial.id) { mutableStateOf<List<ApiCommunityMember>>(emptyList()) }
    var nextPage by remember(initial.id) { mutableIntStateOf(1) }
    var hasNext by remember(initial.id) { mutableStateOf(true) }
    var loading by remember(initial.id) { mutableStateOf(false) }
    var refreshing by remember(initial.id) { mutableStateOf(false) }
    var membershipBusy by remember(initial.id) { mutableStateOf(false) }
    var confirmLeave by remember(initial.id) { mutableStateOf(false) }
    var membershipNotice by remember(initial.id) { mutableStateOf(false) }
    var error by remember(initial.id) { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val isOwner = currentUser?.id != null && community.ownerId == currentUser.id
    suspend fun loadNext() {
        if (loading || !hasNext) return
        val page = nextPage
        val requestedTab = selectedTab
        loading = true
        if (requestedTab == "members") {
            when (val result = withContext(Dispatchers.IO) { api.communityMembers(community.id, page, 20) }) {
                is ApiResult.Success -> if (selectedTab == requestedTab) {
                    val incoming = result.value.members.filter { next -> members.none { it.id == next.id } }
                    members = (members + incoming).distinctBy(ApiCommunityMember::id)
                    nextPage = result.value.nextPage ?: (page + 1)
                    hasNext = result.value.hasNext && incoming.isNotEmpty()
                    error = null
                }
                is ApiResult.Failure -> if (selectedTab == requestedTab) {
                    error = result.message
                    hasNext = false
                }
            }
        } else {
            when (val result = withContext(Dispatchers.IO) {
                api.communityPosts(community.id, page, 20, tab = requestedTab)
            }) {
                is ApiResult.Success -> if (selectedTab == requestedTab) {
                    val incoming = result.value.posts.map(ApiPost::toUiPost)
                        .filter { next -> posts.none { postStableKey(it) == postStableKey(next) } }
                    posts = (posts + incoming).distinctBy(::postStableKey)
                    nextPage = result.value.nextPage ?: (page + 1)
                    hasNext = result.value.hasNext && incoming.isNotEmpty()
                    error = null
                }
                is ApiResult.Failure -> if (selectedTab == requestedTab) {
                    error = result.message
                    hasNext = false
                }
            }
        }
        if (selectedTab == requestedTab) loading = false
    }
    fun changeMembership(join: Boolean) {
        if (membershipBusy) return
        membershipBusy = true
        error = null
        scope.launch {
            when (val result = withContext(Dispatchers.IO) { api.communityMembership(community.id, join) }) {
                is ApiResult.Success -> community = community.copy(
                    isMember = join,
                    memberCount = (community.memberCount + if (join) 1 else -1).coerceAtLeast(0)
                )
                is ApiResult.Failure -> error = result.message
            }
            membershipBusy = false
        }
    }
    suspend fun refreshCurrentTab() {
        if (refreshing || loading) return
        refreshing = true
        val requestedTab = selectedTab
        if (requestedTab == "members") {
            when (val result = withContext(Dispatchers.IO) { api.communityMembers(community.id, 1, 20) }) {
                is ApiResult.Success -> if (selectedTab == requestedTab) {
                    members = (result.value.members + members).distinctBy(ApiCommunityMember::id)
                    error = null
                }
                is ApiResult.Failure -> if (selectedTab == requestedTab) error = result.message
            }
        } else {
            when (val result = withContext(Dispatchers.IO) { api.communityPosts(community.id, 1, 20, requestedTab) }) {
                is ApiResult.Success -> if (selectedTab == requestedTab) {
                    val fresh = result.value.posts.map(ApiPost::toUiPost)
                    posts = (fresh + posts).distinctBy(::postStableKey)
                    error = null
                }
                is ApiResult.Failure -> if (selectedTab == requestedTab) error = result.message
            }
        }
        refreshing = false
    }
    LaunchedEffect(initial.id) {
        when (val result = withContext(Dispatchers.IO) { api.community(initial.id) }) {
            is ApiResult.Success -> community = result.value
            is ApiResult.Failure -> error = result.message
        }
    }
    LaunchedEffect(initial.id, selectedTab) {
        posts = emptyList()
        members = emptyList()
        nextPage = 1
        hasNext = true
        loading = false
        refreshing = false
        error = null
        listState.scrollToItem(0)
        loadNext()
    }
    LaunchedEffect(initial.id, selectedTab) {
        while (true) {
            delay(AUTO_REFRESH_INTERVAL_MS)
            refreshCurrentTab()
        }
    }
    LaunchedEffect(latestCommunityPost) {
        val (communityId, created) = latestCommunityPost ?: return@LaunchedEffect
        if (communityId != community.id) return@LaunchedEffect
        val visibleInTab = selectedTab != "members" && (selectedTab != "media" || created.media.isNotEmpty())
        if (visibleInTab && posts.none { postStableKey(it) == postStableKey(created) }) {
            posts = listOf(created) + posts
        }
    }
    LaunchedEffect(membershipNotice) {
        if (membershipNotice) {
            delay(3_000)
            membershipNotice = false
        }
    }
    InfiniteLoadEffect(listState, if (selectedTab == "members") members.size else posts.size, hasNext, loading) {
        scope.launch { loadNext() }
    }
    if (confirmLeave) {
        Dialog(onDismissRequest = { confirmLeave = false }) {
            Column(
                Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(22.dp))
                    .border(1.dp, Hairline, RoundedCornerShape(22.dp)).padding(20.dp)
            ) {
                KText("コミュニティから退出しますか？", 17, Ink, FontWeight.Black)
                Spacer(Modifier.height(7.dp))
                KText("${community.name}の投稿がコミュニティ一覧から外れます。", 11, Muted, lineHeight = 17f)
                Spacer(Modifier.height(18.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    Box(
                        Modifier.weight(1f).border(1.dp, Hairline, RoundedCornerShape(13.dp))
                            .clickable { confirmLeave = false }.padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) { KText("キャンセル", 10, Ink, FontWeight.Bold) }
                    Box(
                        Modifier.weight(1f).background(Color(0xFFD64045), RoundedCornerShape(13.dp))
                            .clickable {
                                confirmLeave = false
                                changeMembership(false)
                            }.padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) { KText("退出", 10, Color.White, FontWeight.Black) }
                }
            }
        }
    }
    Box(Modifier.fillMaxSize().background(Paper)) {
        Column(Modifier.fillMaxSize()) {
            OverlayHeader(community.name, onBack)
            LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).pointerInput(selectedTab) {
                var horizontalDistance = 0f
                detectHorizontalDragGestures(
                    onDragStart = { horizontalDistance = 0f },
                    onDragCancel = { horizontalDistance = 0f },
                    onDragEnd = {
                        val currentIndex = communityTabs.indexOf(selectedTab).coerceAtLeast(0)
                        when {
                            horizontalDistance < -90f && currentIndex < communityTabs.lastIndex -> selectedTab = communityTabs[currentIndex + 1]
                            horizontalDistance > 90f && currentIndex > 0 -> selectedTab = communityTabs[currentIndex - 1]
                        }
                        horizontalDistance = 0f
                    }
                ) { _, amount -> horizontalDistance += amount }
            },
            contentPadding = PaddingValues(bottom = bottomDockContentInset() + 22.dp)
        ) {
            item {
                Box(Modifier.fillMaxWidth().height(150.dp).background(Strong)) {
                    if (community.headerImageUrl != null) {
                        AsyncImage(community.headerImageUrl, community.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                        Box(Modifier.fillMaxSize().background(Strong.copy(.18f)))
                    }
                }
                Column(Modifier.padding(horizontal = 22.dp, vertical = 17.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            KText(community.name, 23, Ink, FontWeight.Black)
                            KText("${community.memberCount}人のメンバー", 10, Muted, FontWeight.Bold)
                        }
                        if (!isOwner) {
                            Box(
                                Modifier.clip(RoundedCornerShape(13.dp))
                                    .background(if (community.isMember) Surface else Strong)
                                    .border(1.dp, if (community.isMember) Hairline else Strong, RoundedCornerShape(13.dp))
                                    .clickable(enabled = !membershipBusy) {
                                        if (community.isMember) confirmLeave = true else changeMembership(true)
                                    }.padding(horizontal = 15.dp, vertical = 10.dp)
                            ) {
                                KText(
                                    if (membershipBusy) "…" else if (community.isMember) "参加中" else "参加する",
                                    10,
                                    if (community.isMember) Ink else OnStrong,
                                    FontWeight.Black
                                )
                            }
                        }
                    }
                    if (community.description.isNotBlank()) {
                        Spacer(Modifier.height(12.dp))
                        KText(community.description, 12, Ink, lineHeight = 18f)
                    }
                    if (community.rules.isNotEmpty()) {
                        Spacer(Modifier.height(14.dp))
                        Column(
                            Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(16.dp))
                                .border(1.dp, Hairline, RoundedCornerShape(16.dp)).padding(13.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CustomIcon(IconType.LICENSE, Carrot, 16.dp)
                                Spacer(Modifier.width(7.dp))
                                KText("コミュニティルール", 11, Ink, FontWeight.Black)
                            }
                            Spacer(Modifier.height(8.dp))
                            community.rules.forEachIndexed { index, rule ->
                                Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.Top) {
                                    Box(
                                        Modifier.size(20.dp).background(PaleCarrot, RoundedCornerShape(7.dp)),
                                        contentAlignment = Alignment.Center
                                    ) { KText("${index + 1}", 8, Carrot, FontWeight.Black) }
                                    Spacer(Modifier.width(8.dp))
                                    KText(rule, 10, Ink, lineHeight = 16f, modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                    error?.let {
                        Spacer(Modifier.height(10.dp))
                        ErrorText(it)
                    }
                }
                Box(Modifier.fillMaxWidth().height(1.dp).background(Hairline))
            }
            stickyHeader(key = "community-detail-tabs") {
                Row(Modifier.fillMaxWidth().background(Paper)) {
                    listOf(
                        "人気" to "trending",
                        "最新" to "latest",
                        "メディア" to "media",
                        "メンバー" to "members"
                    ).forEach { (label, tab) ->
                        val indicatorWidth by animateDpAsState(
                            if (selectedTab == tab) 30.dp else 0.dp,
                            spring(stiffness = Spring.StiffnessMediumLow),
                            label = "communityTabIndicator"
                        )
                        Column(
                            Modifier.weight(1f).clickable { selectedTab = tab }.padding(top = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            KText(label, 11, if (selectedTab == tab) Ink else Muted, FontWeight.Bold)
                            Spacer(Modifier.height(9.dp))
                            Box(
                                Modifier.width(indicatorWidth).height(3.dp)
                                    .background(if (selectedTab == tab) Carrot else Color.Transparent, CircleShape)
                            )
                        }
                    }
                }
                Box(Modifier.fillMaxWidth().height(1.dp).background(Hairline))
            }
            if (selectedTab == "media") {
                val gallery = posts.flatMap { post ->
                    post.media.filter { media ->
                        media.type.startsWith("image/", true) ||
                            Regex("\\.(jpg|jpeg|png|webp|gif|avif)$").containsMatchIn(media.url.substringBefore('?').lowercase())
                    }.map { post to it }
                }.distinctBy { (post, media) -> "${postStableKey(post)}:${media.url}" }
                items(gallery.chunked(3), key = { row -> row.joinToString("|") { "${postStableKey(it.first)}:${it.second.url}" } }) { row ->
                    Row(
                        Modifier.fillMaxWidth().height(126.dp)
                            .offset(x = communityTabMotion.shiftDp.dp)
                            .alpha(communityTabMotion.alpha)
                    ) {
                        row.forEach { (post, media) ->
                            AsyncImage(
                                model = media.url,
                                contentDescription = media.alt.ifBlank { "コミュニティの投稿画像" },
                                modifier = Modifier.weight(1f).fillMaxHeight().padding(1.dp)
                                    .clip(RoundedCornerShape(3.dp)).background(Hairline).clickable { onPost(post) },
                                contentScale = ContentScale.Crop
                            )
                        }
                        repeat(3 - row.size) { Spacer(Modifier.weight(1f).fillMaxHeight().padding(1.dp)) }
                    }
                }
            } else if (selectedTab == "members") {
                items(members, key = ApiCommunityMember::id) { member ->
                    Row(
                        Modifier.fillMaxWidth()
                            .offset(x = communityTabMotion.shiftDp.dp)
                            .alpha(communityTabMotion.alpha)
                            .clickable { onUser(member.user) }
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.size(44.dp).background(PaleCarrot, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                            if (member.user.avatarUrl != null) {
                                AsyncImage(member.user.avatarUrl, member.user.displayName, Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp)), contentScale = ContentScale.Crop)
                            } else KText(member.user.displayName.ifBlank { member.user.username }.take(1), 14, Ink, FontWeight.Black)
                        }
                        Spacer(Modifier.width(11.dp))
                        Column(Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                KText(member.user.displayName.ifBlank { member.user.username }, 13, Ink, FontWeight.Bold, maxLines = 1, modifier = Modifier.weight(1f, fill = false))
                                AccountMarks(member.user.officialMarks.ifEmpty { listOf(member.user.officialMark) }, member.user.isBotAccount, member.user.isParodyAccount, member.user.isPrivate, compact = true)
                            }
                            KText("@${member.user.username}", 9, Muted)
                        }
                        if (!member.role.equals("MEMBER", true)) {
                            Box(Modifier.background(PaleCarrot, RoundedCornerShape(8.dp)).padding(horizontal = 7.dp, vertical = 5.dp)) {
                                KText(member.role.uppercase(), 8, Carrot, FontWeight.Black)
                            }
                        }
                    }
                    Box(Modifier.padding(start = 75.dp).fillMaxWidth().height(1.dp).background(Hairline))
                }
            } else {
                items(posts, key = ::postStableKey) { post ->
                    Box(
                        Modifier.offset(x = communityTabMotion.shiftDp.dp)
                            .alpha(communityTabMotion.alpha)
                    ) {
                        PostCard(
                            post,
                            onOpen = onPost,
                            onAuthor = { onUser(it.toApiUser()) },
                            onReply = onReply,
                            onQuote = onQuote,
                            onRekarot = { onRekarot(post, it) },
                            onLike = { onLike(post, it) },
                            onBookmark = { onBookmark(post, it) }
                        )
                    }
                }
            }
            if (loading) item {
                Box(Modifier.offset(x = communityTabMotion.shiftDp.dp).alpha(communityTabMotion.alpha)) {
                    LoadingPost()
                }
            }
            if (!loading && error == null && ((selectedTab == "members" && members.isEmpty()) || (selectedTab != "members" && posts.isEmpty()))) {
                item {
                    KText(
                        if (selectedTab == "members") "表示できるメンバーはいません" else "このタブにはまだ投稿がありません",
                        12,
                        Muted,
                        modifier = Modifier.padding(22.dp)
                    )
                }
            }
            }
        }
        Column(
            Modifier.align(Alignment.BottomEnd).padding(end = 20.dp, bottom = 82.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AnimatedVisibility(
                visible = membershipNotice,
                enter = slideInHorizontally(tween(220)) { it / 2 } + fadeIn(tween(160)),
                exit = slideOutHorizontally(tween(180)) { it / 2 } + fadeOut(tween(130))
            ) {
                Box(
                    Modifier.background(Strong, RoundedCornerShape(13.dp))
                        .padding(horizontal = 12.dp, vertical = 9.dp)
                ) {
                    KText("参加するとコミュニティに投稿できます", 10, OnStrong, FontWeight.Bold)
                }
            }
            Box(
                Modifier.size(45.dp).clip(RoundedCornerShape(16.dp)).background(Surface)
                    .border(1.dp, Hairline, RoundedCornerShape(16.dp))
                    .clickable(enabled = !refreshing && !loading) {
                        scope.launch { refreshCurrentTab() }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (refreshing) KText("…", 18, Muted, FontWeight.Black)
                else CustomIcon(IconType.REFRESH, Carrot, 19.dp)
            }
            Box(
                Modifier.size(58.dp).clip(RoundedCornerShape(20.dp))
                    .background(if (community.isMember || isOwner) Carrot else Strong)
                    .clickable {
                        if (community.isMember || isOwner) onCompose(community)
                        else membershipNotice = true
                    },
                contentAlignment = Alignment.Center
            ) {
                CustomIcon(IconType.PLUS, Color.White, 24.dp)
            }
        }
    }
}

@Composable
private fun BoardScreen(
    boards: List<ApiBoard>,
    refreshing: Boolean,
    error: String?,
    onRefresh: () -> Unit,
    onOpen: (ApiBoard) -> Unit,
    onCreate: () -> Unit
) {
    val listState = rememberLazyListState()
    Box(Modifier.fillMaxSize()) {
        LazyColumn(state = listState, contentPadding = PaddingValues(bottom = 146.dp)) {
            item { EditorialHeader("BOARD", "話そう、\n好きなことを。", "アカウントで参加する掲示板。") }
            error?.let { message -> item { ErrorStrip(message, onRefresh) } }
            itemsIndexed(boards, key = { _, board -> board.slug }) { i, board ->
                Row(Modifier.fillMaxWidth().clickable { onOpen(board) }.padding(horizontal = 22.dp, vertical = 17.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(54.dp).background(listOf(Lemon, Mint, Sky, PaleCarrot)[i % 4], RoundedCornerShape(18.dp)), contentAlignment = Alignment.Center) {
                        CustomIcon(IconType.BOARD, Ink, 23.dp)
                    }
                    Spacer(Modifier.width(15.dp))
                    Column(Modifier.weight(1f)) {
                        KText(board.slug.uppercase(), 9, Carrot, FontWeight.Black, letterSpacing = 1.4f)
                        Spacer(Modifier.height(5.dp))
                        KText(board.name, 16, Ink, FontWeight.Bold)
                        Spacer(Modifier.height(5.dp))
                        KText(if (board.threadCount > 0) "${board.threadCount}件のスレッド" else board.description, 11, Muted)
                    }
                    KText("↗", 19, Ink, FontWeight.Normal)
                }
                Box(Modifier.padding(start = 91.dp).fillMaxWidth().height(1.dp).background(Hairline))
            }
        }
        Column(
            Modifier.align(Alignment.BottomEnd).padding(end = 20.dp, bottom = 82.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                Modifier.size(45.dp).clip(RoundedCornerShape(16.dp)).background(Surface)
                    .border(1.dp, Hairline, RoundedCornerShape(16.dp))
                    .clickable(enabled = !refreshing) { onRefresh() },
                contentAlignment = Alignment.Center
            ) {
                if (refreshing) KText("…", 20, Muted, FontWeight.Bold)
                else CustomIcon(IconType.REFRESH, Carrot, 20.dp)
            }
            Box(
                Modifier.size(58.dp).clip(RoundedCornerShape(20.dp)).background(Carrot).clickable { onCreate() },
                contentAlignment = Alignment.Center
            ) {
                CustomIcon(IconType.PLUS, Color.White, 24.dp)
            }
        }
    }
}

@Composable
private fun DiscoverScreen(api: KarotterApi, externalRequest: Pair<String, Long>?, onCompose: () -> Unit, onOpen: (Post) -> Unit, onUser: (ApiUser) -> Unit, onReply: (Post) -> Unit, onQuote: (Post) -> Unit, onRekarot: (Post, Boolean) -> Unit, onLike: (Post, Boolean) -> Unit, onBookmark: (Post, Boolean) -> Unit) {
    val context = LocalContext.current
    val historyPrefs = remember { context.getSharedPreferences("karotter_search_history_v1", android.content.Context.MODE_PRIVATE) }
    var query by remember { mutableStateOf("") }
    var submittedQuery by remember { mutableStateOf<String?>(null) }
    var advancedSearchOpen by remember { mutableStateOf(false) }
    var postSort by remember { mutableStateOf("latest") }
    var postResults by remember { mutableStateOf<List<Post>>(emptyList()) }
    var mediaResults by remember { mutableStateOf<List<Post>>(emptyList()) }
    var userResults by remember { mutableStateOf<List<ApiUser>>(emptyList()) }
    var nextPostPage by remember { mutableIntStateOf(1) }
    var nextMediaPage by remember { mutableIntStateOf(1) }
    var nextUserPage by remember { mutableIntStateOf(1) }
    var postHasNext by remember { mutableStateOf(true) }
    var mediaHasNext by remember { mutableStateOf(true) }
    var userHasNext by remember { mutableStateOf(true) }
    var postLoading by remember { mutableStateOf(false) }
    var postRefreshing by remember { mutableStateOf(false) }
    var mediaLoading by remember { mutableStateOf(false) }
    var mediaRefreshing by remember { mutableStateOf(false) }
    var userLoading by remember { mutableStateOf(false) }
    var trends by remember { mutableStateOf<List<ApiTrend>>(emptyList()) }
    var searchError by remember { mutableStateOf<String?>(null) }
    var mediaError by remember { mutableStateOf<String?>(null) }
    var loadedQuery by remember { mutableStateOf<String?>(null) }
    var searchHistory by remember {
        mutableStateOf(
            historyPrefs.getString("queries", "").orEmpty().split('\u001F').filter { it.isNotBlank() }.take(12)
        )
    }
    val scope = rememberCoroutineScope()
    fun submitSearch(raw: String) {
        val term = raw.trim()
        if (term.isBlank()) return
        if (!submittedQuery.orEmpty().equals(term, true)) postSort = "latest"
        query = term
        searchHistory = (listOf(term) + searchHistory.filterNot { it.equals(term, true) }).take(12)
        historyPrefs.edit().putString("queries", searchHistory.joinToString("\u001F")).apply()
        submittedQuery = term
    }
    fun closeResults() {
        submittedQuery = null
        query = ""
        loadedQuery = null
        postResults = emptyList()
        mediaResults = emptyList()
        userResults = emptyList()
        searchError = null
        mediaError = null
    }
    fun mergeRefreshedPosts(incoming: List<ApiPost>) {
        val fresh = incoming.map(ApiPost::toUiPost).distinctBy(::postStableKey)
        val existingIds = postResults.mapNotNullTo(hashSetOf()) { it.id }
        val additions = fresh.filter { it.id == null || it.id !in existingIds }
        if (additions.isNotEmpty()) {
            postResults = (additions + postResults).distinctBy(::postStableKey)
        }
    }
    fun refreshPostResults(showError: Boolean) {
        val term = submittedQuery?.trim().orEmpty()
        if (term.isBlank() || postSort == "media" || postLoading || postRefreshing) return
        val requestedSort = postSort
        postRefreshing = true
        scope.launch {
            when (val result = withContext(Dispatchers.IO) { api.searchPosts(term, 1, requestedSort) }) {
                is ApiResult.Success -> if (submittedQuery == term && postSort == requestedSort) {
                    mergeRefreshedPosts(result.value.posts)
                    searchError = null
                }
                is ApiResult.Failure -> if (showError && submittedQuery == term && postSort == requestedSort) {
                    searchError = result.message
                }
            }
            postRefreshing = false
        }
    }
    fun refreshMediaResults(showError: Boolean) {
        val term = submittedQuery?.trim().orEmpty()
        if (term.isBlank() || mediaLoading || mediaRefreshing) return
        mediaRefreshing = true
        scope.launch {
            when (
                val result = withContext(Dispatchers.IO) {
                    api.searchPosts(term, 1, sort = "latest", hasMedia = true)
                }
            ) {
                is ApiResult.Success -> if (submittedQuery == term) {
                    val fresh = result.value.posts.map(ApiPost::toUiPost).distinctBy(::postStableKey)
                    val existingIds = mediaResults.mapNotNullTo(hashSetOf()) { it.id }
                    val additions = fresh.filter { it.id == null || it.id !in existingIds }
                    if (additions.isNotEmpty()) {
                        mediaResults = (additions + mediaResults).distinctBy(::postStableKey)
                    }
                    mediaError = null
                }
                is ApiResult.Failure -> if (showError && submittedQuery == term) {
                    mediaError = result.message
                }
            }
            mediaRefreshing = false
        }
    }
    BackHandler(enabled = submittedQuery != null && LocalNavigationActive.current) { closeResults() }
    LaunchedEffect(Unit) {
        when (val result = withContext(Dispatchers.IO) { api.trendingTopics() }) { is ApiResult.Success -> trends = result.value; is ApiResult.Failure -> Unit }
    }
    LaunchedEffect(externalRequest) {
        externalRequest?.let { (requested, _) -> submitSearch(requested) }
    }
    LaunchedEffect(submittedQuery, postSort, api) {
        val term = submittedQuery?.trim().orEmpty()
        if (term.isBlank()) return@LaunchedEffect
        val queryChanged = loadedQuery != term
        if (queryChanged) {
            postResults = emptyList()
            userResults = emptyList()
            loadedQuery = term
            nextUserPage = 1
            userHasNext = true
        }
        if (postSort == "media") {
            postLoading = false
            userLoading = false
            return@LaunchedEffect
        }
        nextPostPage = 1
        postHasNext = true
        postLoading = true
        userLoading = queryChanged
        searchError = null
        val postResult = withContext(Dispatchers.IO) { api.searchPosts(term, 1, postSort) }
        when (val result = postResult) {
            is ApiResult.Success -> { postResults = result.value.posts.map(ApiPost::toUiPost); nextPostPage = result.value.nextPage ?: 2; postHasNext = result.value.hasNext }
            is ApiResult.Failure -> { postHasNext = false; searchError = result.message }
        }
        postLoading = false
        if (queryChanged) {
            when (val result = withContext(Dispatchers.IO) { api.searchUsers(term, 1) }) {
                is ApiResult.Success -> { userResults = result.value.users; nextUserPage = result.value.nextPage ?: 2; userHasNext = result.value.hasNext }
                is ApiResult.Failure -> { userHasNext = false; if (searchError == null) searchError = result.message }
            }
        }
        userLoading = false
    }
    LaunchedEffect(submittedQuery, api) {
        val term = submittedQuery?.trim().orEmpty()
        if (term.isBlank()) return@LaunchedEffect
        mediaResults = emptyList()
        nextMediaPage = 1
        mediaHasNext = true
        mediaLoading = true
        mediaError = null
        when (
            val result = withContext(Dispatchers.IO) {
                api.searchPosts(term, 1, sort = "latest", hasMedia = true)
            }
        ) {
            is ApiResult.Success -> if (submittedQuery == term) {
                mediaResults = result.value.posts.map(ApiPost::toUiPost).distinctBy(::postStableKey)
                nextMediaPage = result.value.nextPage ?: 2
                mediaHasNext = result.value.hasNext
                mediaError = null
            }
            is ApiResult.Failure -> if (submittedQuery == term) {
                mediaHasNext = false
                mediaError = result.message
            }
        }
        mediaLoading = false
    }
    LaunchedEffect(submittedQuery, postSort) {
        if (submittedQuery.isNullOrBlank()) return@LaunchedEffect
        while (true) {
            delay(AUTO_REFRESH_INTERVAL_MS)
            refreshPostResults(showError = false)
            refreshMediaResults(showError = false)
        }
    }
    fun loadMorePosts() {
        val term = submittedQuery ?: return
        if (postLoading || !postHasNext) return
        val page = nextPostPage
        postLoading = true
        scope.launch {
            when (val result = withContext(Dispatchers.IO) { api.searchPosts(term, page, postSort) }) {
                is ApiResult.Success -> {
                    val incoming = result.value.posts.map(ApiPost::toUiPost).distinctBy(::postStableKey)
                        .filter { p -> postResults.none { postStableKey(it) == postStableKey(p) } }
                    postResults = (postResults + incoming).distinctBy(::postStableKey)
                    nextPostPage = result.value.nextPage ?: page + 1
                    postHasNext = result.value.hasNext && incoming.isNotEmpty()
                }
                is ApiResult.Failure -> { postHasNext = false; searchError = result.message }
            }
            postLoading = false
        }
    }
    fun loadMoreUsers() {
        val term = submittedQuery ?: return
        if (userLoading || !userHasNext) return
        val page = nextUserPage
        userLoading = true
        scope.launch {
            when (val result = withContext(Dispatchers.IO) { api.searchUsers(term, page) }) {
                is ApiResult.Success -> {
                    val incoming = result.value.users.filter { u -> userResults.none { it.id == u.id } }
                    userResults = userResults + incoming; nextUserPage = result.value.nextPage ?: page + 1
                    userHasNext = result.value.hasNext && incoming.isNotEmpty()
                }
                is ApiResult.Failure -> { userHasNext = false; searchError = result.message }
            }
            userLoading = false
        }
    }
    fun loadMoreMedia() {
        val term = submittedQuery ?: return
        if (mediaLoading || !mediaHasNext) return
        val page = nextMediaPage
        mediaLoading = true
        scope.launch {
            when (
                val result = withContext(Dispatchers.IO) {
                    api.searchPosts(term, page, sort = "latest", hasMedia = true)
                }
            ) {
                is ApiResult.Success -> {
                    val incoming = result.value.posts.map(ApiPost::toUiPost)
                        .filter { candidate -> mediaResults.none { it.id == candidate.id } }
                    mediaResults = (mediaResults + incoming).distinctBy(::postStableKey)
                    nextMediaPage = result.value.nextPage ?: page + 1
                    mediaHasNext = result.value.hasNext && incoming.isNotEmpty()
                }
                is ApiResult.Failure -> {
                    mediaHasNext = false
                    mediaError = result.message
                }
            }
            mediaLoading = false
        }
    }
    if (advancedSearchOpen) {
        AdvancedSearchDialog(
            initialQuery = submittedQuery ?: query,
            onDismiss = { advancedSearchOpen = false },
            onSearch = {
                advancedSearchOpen = false
                submitSearch(it)
            }
        )
    }
    if (submittedQuery != null) {
        SearchResultsScreen(
            query = submittedQuery.orEmpty(), users = userResults, posts = postResults, mediaPosts = mediaResults, postSort = postSort, postLoading = postLoading, postRefreshing = postRefreshing, mediaLoading = mediaLoading, mediaRefreshing = mediaRefreshing, userLoading = userLoading, postHasNext = postHasNext, mediaHasNext = mediaHasNext, userHasNext = userHasNext, error = searchError, mediaError = mediaError,
            onBack = ::closeResults, onSearch = ::submitSearch, onAdvancedSearch = { advancedSearchOpen = true }, onUser = onUser, onCompose = onCompose,
            onRefreshPosts = { refreshPostResults(showError = true) },
            onRefreshMedia = { refreshMediaResults(showError = true) },
            onPostSortChange = {
                if (it != postSort) {
                    if (it != "media") {
                        postResults = emptyList()
                        nextPostPage = 1
                        postHasNext = true
                        postLoading = true
                        searchError = null
                    }
                    postSort = it
                }
            },
            onLoadMorePosts = ::loadMorePosts, onLoadMoreUsers = ::loadMoreUsers, onLoadMoreMedia = ::loadMoreMedia,
            onOpen = onOpen,
            onOpenMedia = { post ->
                scope.launch {
                    val opened = post.id?.let { postId ->
                        when (val result = withContext(Dispatchers.IO) { api.post(postId) }) {
                            is ApiResult.Success -> result.value.toUiPost()
                            is ApiResult.Failure -> null
                        }
                    } ?: post
                    onOpen(opened)
                }
            },
            onReply = onReply, onQuote = onQuote, onRekarot = onRekarot, onLike = onLike, onBookmark = onBookmark
        )
        return
    }
    val historySuggestions = searchHistory.filter { query.isBlank() || it.contains(query, ignoreCase = true) }.take(3)
    LazyColumn(contentPadding = PaddingValues(bottom = 126.dp)) {
        item { EditorialHeader("DISCOVER", "まだ知らない、\n好きなもの。", "人と話題を横断して検索") }
        item {
            Row(Modifier.padding(horizontal = 22.dp).fillMaxWidth().height(52.dp).border(1.dp, Ink, RoundedCornerShape(17.dp)).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                CustomIcon(IconType.SEARCH, Ink, 18.dp)
                Spacer(Modifier.width(12.dp))
                BasicTextField(
                    value = query, onValueChange = { query = it }, singleLine = true,
                    textStyle = TextStyle(color = Ink, fontSize = 15.sp, fontWeight = FontWeight.Medium),
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { submitSearch(query) }),
                    decorationBox = { inner -> if (query.isEmpty()) KText("キーワードを入力", 14, Muted) else inner() }
                )
                Box(
                    Modifier.size(34.dp).clip(RoundedCornerShape(10.dp))
                        .clickable { advancedSearchOpen = true },
                    contentAlignment = Alignment.Center
                ) {
                    CustomIcon(IconType.CONTROLS, Muted, 17.dp)
                }
                Box(Modifier.clip(RoundedCornerShape(10.dp)).clickable(enabled = query.isNotBlank()) { submitSearch(query) }.padding(8.dp)) { KText("検索", 11, Carrot, FontWeight.Black) }
            }
        }
        if (historySuggestions.isNotEmpty()) {
            item {
                Row(Modifier.fillMaxWidth().padding(start = 22.dp, top = 18.dp, end = 22.dp, bottom = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                    KText(if (query.isBlank()) "最近の検索" else "検索候補", 11, Muted, FontWeight.Black, modifier = Modifier.weight(1f))
                    if (query.isBlank()) KText("端末に最大12件保存", 9, Muted)
                }
            }
            items(historySuggestions) { history ->
                Row(Modifier.fillMaxWidth().clickable { submitSearch(history) }.padding(horizontal = 22.dp, vertical = 9.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(31.dp).background(Surface, CircleShape), contentAlignment = Alignment.Center) { KText("↗", 13, Muted, FontWeight.Bold) }
                    Spacer(Modifier.width(11.dp))
                    KText(history, 13, Ink, FontWeight.Medium, maxLines = 1, modifier = Modifier.weight(1f))
                    KText("検索", 9, Carrot, FontWeight.Bold)
                }
            }
        }
        item { KText("いま、伸びている話題", 17, Ink, FontWeight.Bold, modifier = Modifier.padding(22.dp, 28.dp, 22.dp, 12.dp)) }
        itemsIndexed(trends) { i, trend ->
            Row(Modifier.fillMaxWidth().clickable { submitSearch(trend.label) }.padding(horizontal = 22.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(37.dp).background(if (i == 0) Carrot else Hairline, CircleShape), contentAlignment = Alignment.Center) {
                    KText("${i + 1}", 12, if (i == 0) Color.White else Ink, FontWeight.Black)
                }
                Spacer(Modifier.width(13.dp))
                Column(Modifier.weight(1f)) { KText(trend.label, 15, Ink, FontWeight.Bold); KText("${trend.count}件の投稿", 10, Muted) }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    KText("検索", 10, Carrot, FontWeight.Bold)
                    CustomIcon(IconType.FORWARD, Carrot, 12.dp)
                }
            }
        }
    }
}

@Composable
private fun AdvancedSearchDialog(initialQuery: String, onDismiss: () -> Unit, onSearch: (String) -> Unit) {
    var words by remember { mutableStateOf(initialQuery) }
    var exact by remember { mutableStateOf("") }
    var anyWords by remember { mutableStateOf("") }
    var excludedWords by remember { mutableStateOf("") }
    var hashtags by remember { mutableStateOf("") }
    var excludedHashtags by remember { mutableStateOf("") }
    var fromUser by remember { mutableStateOf("") }
    var toUser by remember { mutableStateOf("") }
    var mentionUser by remember { mutableStateOf("") }
    var excludedMention by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("") }
    var links by remember { mutableIntStateOf(0) }
    var replies by remember { mutableIntStateOf(0) }
    var quotes by remember { mutableIntStateOf(0) }
    var media by remember { mutableIntStateOf(0) }
    var videos by remember { mutableIntStateOf(0) }
    var minReplies by remember { mutableStateOf("") }
    var minFaves by remember { mutableStateOf("") }
    var minRekarots by remember { mutableStateOf("") }
    var since by remember { mutableStateOf("") }
    var until by remember { mutableStateOf("") }

    fun account(value: String) = value.trim().removePrefix("@")
    fun terms(value: String) = value.split(Regex("[\\s,、]+")).filter { it.isNotBlank() }
    fun buildQuery(): String = buildList {
        words.trim().takeIf { it.isNotBlank() }?.let(::add)
        exact.trim().replace("\"", "").takeIf { it.isNotBlank() }?.let { add("\"$it\"") }
        terms(anyWords).takeIf { it.isNotEmpty() }?.let { add(it.joinToString(" OR ")) }
        terms(excludedWords).forEach { add("-$it") }
        terms(hashtags).forEach { add("#${it.removePrefix("#")}") }
        terms(excludedHashtags).forEach { add("-#${it.removePrefix("#").removePrefix("-")}") }
        account(fromUser).takeIf { it.isNotBlank() }?.let { add("from:$it") }
        account(toUser).takeIf { it.isNotBlank() }?.let { add("to:$it") }
        account(mentionUser).takeIf { it.isNotBlank() }?.let { add("@$it") }
        account(excludedMention).takeIf { it.isNotBlank() }?.let { add("-@$it") }
        language.takeIf { it.isNotBlank() }?.let { add("lang:$it") }
        listOf("links" to links, "replies" to replies, "quotes" to quotes, "media" to media, "videos" to videos).forEach { (name, state) ->
            if (state == 1) add("filter:$name") else if (state == 2) add("-filter:$name")
        }
        minReplies.toIntOrNull()?.coerceAtLeast(0)?.let { add("min_replies:$it") }
        minFaves.toIntOrNull()?.coerceAtLeast(0)?.let { add("min_faves:$it") }
        minRekarots.toIntOrNull()?.coerceAtLeast(0)?.let { add("min_rekarots:$it") }
        since.trim().takeIf { it.isNotBlank() }?.let { add("since:$it") }
        until.trim().takeIf { it.isNotBlank() }?.let { add("until:$it") }
    }.joinToString(" ")

    val activeConditionCount = listOf(
        words, exact, anyWords, excludedWords, hashtags, excludedHashtags,
        fromUser, toUser, mentionUser, excludedMention, language,
        minReplies, minFaves, minRekarots, since, until
    ).count { it.isNotBlank() } + listOf(links, replies, quotes, media, videos).count { it != 0 }

    fun resetConditions() {
        words = ""
        exact = ""
        anyWords = ""
        excludedWords = ""
        hashtags = ""
        excludedHashtags = ""
        fromUser = ""
        toUser = ""
        mentionUser = ""
        excludedMention = ""
        language = ""
        links = 0
        replies = 0
        quotes = 0
        media = 0
        videos = 0
        minReplies = ""
        minFaves = ""
        minRekarots = ""
        since = ""
        until = ""
    }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(
            Modifier.fillMaxWidth().fillMaxHeight(.94f).padding(horizontal = 12.dp)
                .clip(RoundedCornerShape(25.dp)).background(Paper).border(1.dp, Hairline, RoundedCornerShape(25.dp))
        ) {
            Box(
                Modifier.padding(top = 8.dp).width(34.dp).height(4.dp)
                    .background(Hairline, RoundedCornerShape(3.dp)).align(Alignment.CenterHorizontally)
            )
            Row(Modifier.fillMaxWidth().padding(20.dp, 12.dp, 16.dp, 13.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    KText("SEARCH BUILDER", 8, Carrot, FontWeight.Black, letterSpacing = 1.8f)
                    Spacer(Modifier.height(2.dp))
                    KText("高度な検索", 20, Ink, FontWeight.Black)
                }
                if (activeConditionCount > 0) {
                    Box(
                        Modifier.clip(RoundedCornerShape(10.dp)).background(PaleCarrot)
                            .border(1.dp, Carrot.copy(.28f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 9.dp, vertical = 6.dp)
                    ) {
                        KText("$activeConditionCount 条件", 9, Carrot, FontWeight.Black)
                    }
                    Spacer(Modifier.width(8.dp))
                }
                Box(Modifier.size(36.dp).border(1.dp, Hairline, CircleShape).clickable { onDismiss() }, contentAlignment = Alignment.Center) {
                    CustomIcon(IconType.CLOSE, Ink, 18.dp)
                }
            }
            Box(Modifier.fillMaxWidth().height(1.dp).background(Hairline))
            LazyColumn(
                Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                item {
                    AdvancedSearchSection("キーワード", "語句やハッシュタグを組み合わせる", IconType.SEARCH) {
                        AdvancedSearchField("すべて含む", words, { words = it }, "検索したい単語")
                        AdvancedSearchField("完全一致", exact, { exact = it }, "そのまま一致する文章")
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(Modifier.weight(1f)) { AdvancedSearchField("いずれか", anyWords, { anyWords = it }, "単語を区切る") }
                            Box(Modifier.weight(1f)) { AdvancedSearchField("除外", excludedWords, { excludedWords = it }, "含めない単語") }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(Modifier.weight(1f)) { AdvancedSearchField("ハッシュタグ", hashtags, { hashtags = it }, "#は省略可") }
                            Box(Modifier.weight(1f)) { AdvancedSearchField("タグを除外", excludedHashtags, { excludedHashtags = it }, "#は省略可") }
                        }
                    }
                }
                item {
                    AdvancedSearchSection("アカウント", "投稿者・返信先・メンションで絞る", IconType.PERSON) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(Modifier.weight(1f)) { AdvancedSearchField("投稿者", fromUser, { fromUser = it }, "@username") }
                            Box(Modifier.weight(1f)) { AdvancedSearchField("返信先", toUser, { toUser = it }, "@username") }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(Modifier.weight(1f)) { AdvancedSearchField("メンション", mentionUser, { mentionUser = it }, "@username") }
                            Box(Modifier.weight(1f)) { AdvancedSearchField("除外", excludedMention, { excludedMention = it }, "@username") }
                        }
                    }
                }
                item {
                    AdvancedSearchSection("言語", "投稿に使われている言語", IconType.INFO) {
                        val languages = listOf("" to "指定なし", "ja" to "日本語", "en" to "英語", "ko" to "韓国語", "zh-CN" to "中国語・簡体", "zh-TW" to "中国語・繁体", "es" to "スペイン語", "fr" to "フランス語", "de" to "ドイツ語", "pt-BR" to "ポルトガル語")
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(languages) { (code, label) ->
                                val selected = language == code
                                Box(
                                    Modifier.clip(RoundedCornerShape(11.dp)).background(if (selected) Strong else Paper)
                                        .border(1.dp, if (selected) Strong else Hairline, RoundedCornerShape(11.dp))
                                        .clickable { language = code }.padding(horizontal = 11.dp, vertical = 8.dp)
                                ) { KText(label, 9, if (selected) OnStrong else Muted, FontWeight.Bold) }
                            }
                        }
                    }
                }
                item {
                    AdvancedSearchSection("投稿の種類", "含める・除外する形式を選ぶ", IconType.CONTROLS) {
                        AdvancedFilterRow("リンク", links) { links = it }
                        AdvancedFilterRow("返信", replies) { replies = it }
                        AdvancedFilterRow("引用", quotes) { quotes = it }
                        AdvancedFilterRow("画像", media) { media = it }
                        AdvancedFilterRow("動画", videos) { videos = it }
                    }
                }
                item {
                    AdvancedSearchSection("反応の多さ", "指定数以上の反応がある投稿", IconType.HEART) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(Modifier.weight(1f)) { AdvancedSearchField("返信", minReplies, { minReplies = it.filter(Char::isDigit) }, "指定なし") }
                            Box(Modifier.weight(1f)) { AdvancedSearchField("いいね", minFaves, { minFaves = it.filter(Char::isDigit) }, "指定なし") }
                            Box(Modifier.weight(1f)) { AdvancedSearchField("リカロート", minRekarots, { minRekarots = it.filter(Char::isDigit) }, "指定なし") }
                        }
                    }
                }
                item {
                    AdvancedSearchSection("期間", "指定した期間に投稿されたもの", IconType.CALENDAR) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(Modifier.weight(1f)) { AdvancedSearchField("開始日", since, { since = it.take(10) }, "yyyy-mm-dd") }
                            Box(Modifier.weight(1f)) { AdvancedSearchField("終了日", until, { until = it.take(10) }, "yyyy-mm-dd") }
                        }
                    }
                }
                item { Spacer(Modifier.height(2.dp)) }
            }
            val generated = buildQuery()
            Column(Modifier.fillMaxWidth().background(Surface).navigationBarsPadding().padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        KText("検索式", 8, Muted, FontWeight.Black, letterSpacing = 1.1f)
                        KText(generated.ifBlank { "条件を入力してください" }, 10, if (generated.isBlank()) Muted else Ink, FontWeight.Medium, maxLines = 1)
                    }
                    if (activeConditionCount > 0) {
                        Box(
                            Modifier.clip(RoundedCornerShape(9.dp)).clickable { resetConditions() }
                                .padding(horizontal = 9.dp, vertical = 7.dp)
                        ) {
                            KText("すべてクリア", 9, Carrot, FontWeight.Bold)
                        }
                    }
                }
                Spacer(Modifier.height(11.dp))
                Box(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp))
                        .background(if (generated.isNotBlank()) Carrot else Hairline)
                        .clickable(enabled = generated.isNotBlank()) { onSearch(generated) }.padding(vertical = 13.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CustomIcon(IconType.SEARCH, if (generated.isNotBlank()) Color.White else Muted, 16.dp)
                        Spacer(Modifier.width(7.dp))
                        KText("この条件で検索", 11, if (generated.isNotBlank()) Color.White else Muted, FontWeight.Black)
                    }
                }
            }
        }
    }
}

@Composable
private fun AdvancedSearchSection(
    title: String,
    description: String,
    icon: IconType,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(Surface)
            .border(1.dp, Hairline, RoundedCornerShape(18.dp)).padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(34.dp).clip(RoundedCornerShape(11.dp)).background(PaleCarrot),
                contentAlignment = Alignment.Center
            ) {
                CustomIcon(icon, Carrot, 16.dp)
            }
            Spacer(Modifier.width(10.dp))
            Column {
                KText(title, 12, Ink, FontWeight.Black)
                KText(description, 9, Muted, FontWeight.Medium)
            }
        }
        content()
    }
}

@Composable
private fun AdvancedSearchField(label: String, value: String, onChange: (String) -> Unit, hint: String) {
    Column {
        KText(label, 9, Muted, FontWeight.Bold)
        Spacer(Modifier.height(5.dp))
        BasicTextField(
            value = value,
            onValueChange = onChange,
            singleLine = true,
            textStyle = TextStyle(Ink, 12.sp, FontWeight.Medium),
            cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
            modifier = Modifier.fillMaxWidth().height(42.dp).clip(RoundedCornerShape(12.dp))
                .background(Surface).border(1.dp, Hairline, RoundedCornerShape(12.dp)).padding(horizontal = 11.dp),
            decorationBox = { inner ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                    if (value.isBlank()) KText(hint, 10, Muted.copy(.72f))
                    inner()
                }
            }
        )
    }
}

@Composable
private fun AdvancedFilterRow(label: String, state: Int, onChange: (Int) -> Unit) {
    val options = listOf("指定なし", "のみ", "除外")
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        KText(label, 11, Ink, FontWeight.Bold, modifier = Modifier.weight(1f))
        Row(Modifier.background(Surface, RoundedCornerShape(11.dp)).padding(3.dp), horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            options.forEachIndexed { index, option ->
                val selected = state == index
                Box(
                    Modifier.clip(RoundedCornerShape(8.dp)).background(if (selected) Strong else Color.Transparent)
                        .clickable { onChange(index) }.padding(horizontal = 10.dp, vertical = 6.dp)
                ) { KText(option, 8, if (selected) OnStrong else Muted, FontWeight.Bold) }
            }
        }
    }
}

@Composable
private fun SearchResultsScreen(
    query: String,
    users: List<ApiUser>,
    posts: List<Post>,
    mediaPosts: List<Post>,
    postSort: String,
    postLoading: Boolean,
    postRefreshing: Boolean,
    mediaLoading: Boolean,
    mediaRefreshing: Boolean,
    userLoading: Boolean,
    postHasNext: Boolean,
    mediaHasNext: Boolean,
    userHasNext: Boolean,
    error: String?,
    mediaError: String?,
    onBack: () -> Unit,
    onSearch: (String) -> Unit,
    onAdvancedSearch: () -> Unit,
    onUser: (ApiUser) -> Unit,
    onCompose: () -> Unit,
    onRefreshPosts: () -> Unit,
    onRefreshMedia: () -> Unit,
    onPostSortChange: (String) -> Unit,
    onLoadMorePosts: () -> Unit,
    onLoadMoreUsers: () -> Unit,
    onLoadMoreMedia: () -> Unit,
    onOpen: (Post) -> Unit,
    onOpenMedia: (Post) -> Unit,
    onReply: (Post) -> Unit,
    onQuote: (Post) -> Unit,
    onRekarot: (Post, Boolean) -> Unit,
    onLike: (Post, Boolean) -> Unit,
    onBookmark: (Post, Boolean) -> Unit
) {
    var selectedTab by remember(query) { mutableIntStateOf(0) }
    var searchText by remember(query) { mutableStateOf(query) }
    val resultShift = remember { Animatable(0f) }
    var previousResultPage by remember(query) { mutableStateOf(selectedTab to postSort) }
    fun resultPageIndex(tab: Int, sort: String): Int = when (tab) {
        1 -> 4
        else -> when (sort) {
            "popular" -> 0
            "latest" -> 1
            "oldest" -> 2
            "media" -> 3
            else -> 1
        }
    }
    LaunchedEffect(selectedTab, postSort) {
        val nextPage = selectedTab to postSort
        if (previousResultPage != nextPage) {
            val oldIndex = resultPageIndex(previousResultPage.first, previousResultPage.second)
            val newIndex = resultPageIndex(selectedTab, postSort)
            previousResultPage = nextPage
            resultShift.snapTo(if (newIndex >= oldIndex) 42f else -42f)
            resultShift.animateTo(0f, tween(320, easing = FastOutSlowInEasing))
        }
    }
    val resultAlpha = (1f - abs(resultShift.value) / 120f).coerceIn(.68f, 1f)
    val visiblePosts = remember(posts, postSort) {
        val uniquePosts = posts.distinctBy(::postStableKey)
        when (postSort) {
            "latest" -> uniquePosts.sortedByDescending { it.id ?: 0L }
            "oldest" -> uniquePosts.sortedBy { it.id ?: Long.MAX_VALUE }
            else -> uniquePosts
        }
    }
    val mediaMode = selectedTab == 0 && postSort == "media"
    val initialPostsLoading =
        if (postSort == "media") mediaLoading && mediaPosts.isEmpty()
        else postLoading && posts.isEmpty()
    val postListState = rememberLazyListState()
    val userListState = rememberLazyListState()
    val mediaListState = rememberLazyListState()
    val listState = when (selectedTab) {
        1 -> userListState
        0 -> if (postSort == "media") mediaListState else postListState
        else -> postListState
    }
    val visibleMedia = remember(mediaPosts) {
        mediaPosts.distinctBy(::postStableKey).flatMap { post ->
            post.media.filter { media ->
                media.type.startsWith("image/", true) ||
                    Regex("\\.(jpg|jpeg|png|webp|gif|avif)$")
                        .containsMatchIn(media.url.substringBefore('?').lowercase())
            }.map { media -> post to media }
        }.distinctBy { (post, media) -> "${postStableKey(post)}:${media.url}" }
    }
    val safeBottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    Box(Modifier.fillMaxSize().background(Paper)) {
        Column(
            Modifier
                .fillMaxSize()
                .pointerInput(selectedTab, query, initialPostsLoading) {
                var horizontalDistance = 0f
                detectHorizontalDragGestures(
                    onDragStart = { horizontalDistance = 0f },
                    onDragCancel = { horizontalDistance = 0f },
                    onDragEnd = {
                        when {
                            horizontalDistance < -90f && selectedTab == 0 && !initialPostsLoading -> selectedTab = 1
                            horizontalDistance > 90f && selectedTab > 0 -> selectedTab -= 1
                        }
                        horizontalDistance = 0f
                    }
                ) { _, dragAmount -> horizontalDistance += dragAmount }
                }
        ) {
        Row(
            Modifier.fillMaxWidth().background(Paper).padding(horizontal = 18.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(38.dp).border(1.dp, Hairline, CircleShape).clickable { onBack() },
                contentAlignment = Alignment.Center
            ) { CustomIcon(IconType.BACK, Ink, 20.dp) }
            Spacer(Modifier.width(11.dp))
            Row(
                Modifier.weight(1f).height(44.dp).background(Surface, RoundedCornerShape(15.dp))
                    .border(1.dp, Hairline, RoundedCornerShape(15.dp)).padding(horizontal = 13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomIcon(IconType.SEARCH, Muted, 17.dp)
                Spacer(Modifier.width(10.dp))
                BasicTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    singleLine = true,
                    textStyle = TextStyle(color = Ink, fontSize = 14.sp, fontWeight = FontWeight.Medium),
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSearch(searchText) }),
                    decorationBox = { inner -> if (searchText.isBlank()) KText("キーワードを入力", 13, Muted) else inner() }
                )
                Box(
                    Modifier.size(32.dp).clip(RoundedCornerShape(9.dp)).clickable { onAdvancedSearch() },
                    contentAlignment = Alignment.Center
                ) {
                    CustomIcon(IconType.CONTROLS, Muted, 16.dp)
                }
                if (searchText.isNotBlank() && searchText != query) {
                    Box(
                        Modifier.clip(RoundedCornerShape(9.dp)).clickable { onSearch(searchText) }
                            .padding(horizontal = 7.dp, vertical = 6.dp)
                    ) { KText("検索", 10, Carrot, FontWeight.Black) }
                }
            }
        }
        Row(Modifier.fillMaxWidth().background(Surface)) {
            listOf("投稿", "ユーザー").forEachIndexed { index, label ->
                val enabled = index == 0 || !initialPostsLoading
                val indicatorWidth by animateDpAsState(
                    if (selectedTab == index) 48.dp else 0.dp,
                    tween(260, easing = FastOutSlowInEasing),
                    label = "searchTabIndicator"
                )
                Column(
                    Modifier.weight(1f).clickable(enabled = enabled) { selectedTab = index }.padding(top = 13.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    KText(label, 12, if (selectedTab == index) Ink else if (enabled) Muted else Muted.copy(.45f), FontWeight.Bold, maxLines = 1)
                    Spacer(Modifier.height(11.dp))
                    Box(Modifier.width(indicatorWidth).height(3.dp).background(if (selectedTab == index) Carrot else Color.Transparent, RoundedCornerShape(2.dp)))
                }
            }
        }
        Box(Modifier.fillMaxWidth().height(1.dp).background(Hairline))
        if (selectedTab == 0) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("人気" to "popular", "最新" to "latest", "古い" to "oldest", "メディア" to "media").forEach { (label, value) ->
                    Box(Modifier.clip(RoundedCornerShape(13.dp)).background(if (postSort == value) Strong else Surface).border(1.dp, if (postSort == value) Strong else Hairline, RoundedCornerShape(13.dp)).clickable { onPostSortChange(value) }.padding(horizontal = 18.dp, vertical = 8.dp)) {
                        KText(label, 11, if (postSort == value) OnStrong else Muted, FontWeight.Bold)
                    }
                }
            }
        }
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .offset(x = resultShift.value.dp)
                .alpha(resultAlpha),
            contentPadding = PaddingValues(bottom = 126.dp)
        ) {
            val tabLoading = when (selectedTab) {
                1 -> userLoading
                else -> if (mediaMode) mediaLoading else postLoading
            }
            val tabEmpty = when (selectedTab) {
                1 -> users.isEmpty()
                else -> if (mediaMode) mediaPosts.isEmpty() else posts.isEmpty()
            }
            val activeError = if (mediaMode) mediaError else error
            val noResults = when (selectedTab) {
                0 -> if (mediaMode) visibleMedia.isEmpty() && !mediaHasNext else posts.isEmpty()
                else -> tabEmpty
            }
            if (tabLoading && tabEmpty) items(3) { LoadingPost() }
            if (activeError != null) item { ErrorText(activeError) }
            if (!tabLoading && activeError == null && noResults) item { KText("このタブに一致する結果はありません", 12, Muted, modifier = Modifier.padding(22.dp)) }
            if (selectedTab == 1) {
                itemsIndexed(users, key = { _, user -> user.id }) { index, user ->
                    Row(Modifier.fillMaxWidth().clickable { onUser(user) }.padding(horizontal = 22.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(46.dp).background(PaleCarrot, RoundedCornerShape(15.dp)), contentAlignment = Alignment.Center) {
                            if (user.avatarUrl != null) AsyncImage(user.avatarUrl, user.displayName, Modifier.fillMaxSize().clip(RoundedCornerShape(15.dp)), contentScale = ContentScale.Crop) else KText(user.displayName.take(1), 16, Ink, FontWeight.Black)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                KText(user.displayName.ifBlank { user.username }, 14, Ink, FontWeight.Bold, maxLines = 1, modifier = Modifier.widthIn(max = 150.dp))
                                AccountMarks(user.officialMarks.ifEmpty { listOf(user.officialMark) }, user.isBotAccount, user.isParodyAccount, user.isPrivate, compact = true)
                            }
                            KText("@${user.username}", 11, Muted, maxLines = 1)
                        }
                    }
                }
            }
            if (selectedTab == 0 && !mediaMode) itemsIndexed(visiblePosts, key = { _, post -> postStableKey(post) }) { index, post ->
                PostCard(post, onOpen = onOpen, onAuthor = { onUser(it.toApiUser()) }, onReply = onReply, onQuote = onQuote, onRekarot = { onRekarot(post, it) }, onLike = { onLike(post, it) }, onBookmark = { onBookmark(post, it) })
            }
            if (mediaMode) {
                items(
                    visibleMedia.chunked(3),
                    key = { row ->
                        row.joinToString("|") { "${postStableKey(it.first)}:${it.second.url}" }
                    }
                ) { row ->
                    Row(Modifier.fillMaxWidth().height(126.dp)) {
                        row.forEach { (post, media) ->
                            SpoilerAwareMediaThumbnail(
                                media = media,
                                modifier = Modifier.weight(1f).fillMaxHeight().padding(1.dp),
                                onOpen = { onOpenMedia(post) }
                            )
                        }
                        repeat(3 - row.size) {
                            Spacer(Modifier.weight(1f).fillMaxHeight().padding(1.dp))
                        }
                    }
                }
            }
            val canLoadMore = when (selectedTab) {
                1 -> userHasNext
                else -> if (mediaMode) mediaHasNext else postHasNext
            }
            val resultCount = when (selectedTab) {
                1 -> users.size
                else -> if (mediaMode) mediaPosts.size else posts.size
            }
            if (canLoadMore && resultCount > 0) {
                item(key = "search-more-$selectedTab-$postSort-$resultCount") {
                    LaunchedEffect(selectedTab, postSort, resultCount) {
                        when (selectedTab) {
                            1 -> onLoadMoreUsers()
                            else -> if (mediaMode) onLoadMoreMedia() else onLoadMorePosts()
                        }
                    }
                    LoadingPost()
                }
            } else if (tabLoading && resultCount > 0) item { LoadingPost() }
        }
        }
        Column(
            Modifier.align(Alignment.BottomEnd).padding(end = 20.dp, bottom = safeBottom + 78.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val activeRefreshing = if (mediaMode) mediaRefreshing else postRefreshing
            val activeLoading = if (mediaMode) mediaLoading else postLoading
            Box(
                Modifier.size(45.dp).background(Surface, RoundedCornerShape(16.dp))
                    .border(1.dp, Hairline, RoundedCornerShape(16.dp))
                    .clickable(enabled = !activeRefreshing && !activeLoading) {
                        if (mediaMode) onRefreshMedia() else onRefreshPosts()
                    },
                contentAlignment = Alignment.Center
            ) {
                if (activeRefreshing) KText("…", 21, Muted, FontWeight.Bold)
                else CustomIcon(IconType.REFRESH, if (activeLoading) Muted else Carrot, 20.dp)
            }
            Box(
                Modifier.size(58.dp).background(Carrot, RoundedCornerShape(20.dp)).clickable { onCompose() },
                contentAlignment = Alignment.Center
            ) {
                CustomIcon(IconType.PLUS, Color.White, 25.dp)
            }
        }
    }
}

@Composable
private fun HashtagSearchScreen(tag: String, api: KarotterApi, onBack: () -> Unit, onOpen: (Post) -> Unit, onReply: (Post) -> Unit, onQuote: (Post) -> Unit, onRekarot: (Post, Boolean) -> Unit, onLike: (Post, Boolean) -> Unit, onBookmark: (Post, Boolean) -> Unit, onUser: (ApiUser) -> Unit, onCompose: () -> Unit) {
    val bottomDockInset = bottomDockContentInset()
    var posts by remember(tag) { mutableStateOf<List<Post>>(emptyList()) }
    var nextPage by remember(tag) { mutableIntStateOf(1) }
    var hasNext by remember(tag) { mutableStateOf(true) }
    var loading by remember(tag) { mutableStateOf(false) }
    var refreshing by remember(tag) { mutableStateOf(false) }
    var error by remember(tag) { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    suspend fun loadNext() {
        if (loading || !hasNext) return
        val requestedPage = nextPage
        loading = true
        when (val result = withContext(Dispatchers.IO) { api.searchPosts("#$tag", requestedPage, "latest") }) {
            is ApiResult.Success -> {
                val incoming = result.value.posts.map(ApiPost::toUiPost).distinctBy(::postStableKey)
                    .filter { candidate -> posts.none { postStableKey(it) == postStableKey(candidate) } }
                posts = (posts + incoming).distinctBy(::postStableKey)
                nextPage = result.value.nextPage ?: requestedPage + 1
                hasNext = result.value.hasNext && incoming.isNotEmpty()
            }
            is ApiResult.Failure -> {
                error = result.message
                hasNext = false
            }
        }
        loading = false
    }
    suspend fun refresh() {
        if (refreshing || loading && posts.isEmpty()) return
        refreshing = true
        when (val result = withContext(Dispatchers.IO) { api.searchPosts("#$tag", 1, "latest") }) {
            is ApiResult.Success -> {
                val incoming = result.value.posts.map(ApiPost::toUiPost).distinctBy(::postStableKey)
                val existingKeys = posts.mapTo(hashSetOf(), ::postStableKey)
                val additions = incoming.filter { postStableKey(it) !in existingKeys }
                if (additions.isNotEmpty()) posts = (additions + posts).distinctBy(::postStableKey)
                if (posts.isEmpty()) {
                    nextPage = result.value.nextPage ?: 2
                    hasNext = result.value.hasNext
                }
                error = null
            }
            is ApiResult.Failure -> error = result.message
        }
        refreshing = false
    }
    LaunchedEffect(tag) { loadNext() }
    InfiniteLoadEffect(listState, posts.size, hasNext, loading) { scope.launch { loadNext() } }
    Box(Modifier.fillMaxSize().background(Paper)) {
        Column(Modifier.fillMaxSize()) {
            OverlayHeader("#$tag", onBack)
            LazyColumn(state = listState, modifier = Modifier.weight(1f).fillMaxWidth(), contentPadding = PaddingValues(bottom = bottomDockInset + 132.dp)) {
            if (loading && posts.isEmpty()) items(4) { LoadingPost() }
            if (error != null) item { ErrorText(error.orEmpty()) }
            if (!loading && error == null && posts.isEmpty()) item { KText("このハッシュタグの投稿はありません", 12, Muted, modifier = Modifier.padding(22.dp)) }
            items(posts, key = { postStableKey(it) }) { post ->
                PostCard(post, onOpen = onOpen, onAuthor = { onUser(it.toApiUser()) }, onReply = onReply, onQuote = onQuote, onRekarot = { onRekarot(post, it) }, onLike = { onLike(post, it) }, onBookmark = { onBookmark(post, it) })
            }
                if (loading && posts.isNotEmpty()) item { LoadingPost() }
            }
        }
        Column(
            Modifier.align(Alignment.BottomEnd).padding(end = 20.dp, bottom = bottomDockInset + 18.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                Modifier.size(45.dp).background(Surface, RoundedCornerShape(16.dp))
                    .border(1.dp, Hairline, RoundedCornerShape(16.dp))
                    .clickable(enabled = !refreshing) { scope.launch { refresh() } },
                contentAlignment = Alignment.Center
            ) {
                if (refreshing) KText("…", 20, Muted, FontWeight.Bold)
                else CustomIcon(IconType.REFRESH, Carrot, 20.dp)
            }
            Box(
                Modifier.size(58.dp).background(Carrot, RoundedCornerShape(20.dp)).clickable { onCompose() },
                contentAlignment = Alignment.Center
            ) {
                CustomIcon(IconType.PLUS, Color.White, 25.dp)
            }
        }
    }
}

@Composable
private fun OverlayHeader(title: String, onBack: () -> Unit, trailing: (@Composable () -> Unit)? = null) {
    Row(Modifier.fillMaxWidth().background(Paper).padding(horizontal = 18.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(38.dp).border(1.dp, Hairline, CircleShape).clickable { onBack() }, contentAlignment = Alignment.Center) { CustomIcon(IconType.BACK, Ink, 20.dp) }
        Spacer(Modifier.width(13.dp))
        KText(title, 18, Ink, FontWeight.Black, modifier = Modifier.weight(1f))
        trailing?.invoke()
    }
    Box(Modifier.fillMaxWidth().height(1.dp).background(Hairline))
}

@Composable
private fun PostDetailScreen(
    initial: Post,
    api: KarotterApi,
    onBack: () -> Unit,
    onReply: (Post) -> Unit,
    onQuote: (Post) -> Unit,
    onRekarot: (Post, Boolean) -> Unit,
    onLike: (Post, Boolean) -> Unit,
    onBookmark: (Post, Boolean) -> Unit,
    onAuthor: (Post) -> Unit
) {
    val navigationBottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    var post by remember(initial.id) { mutableStateOf(initial) }
    var parentPost by remember(initial.id) { mutableStateOf<Post?>(null) }
    var postHistory by remember(initial.id) { mutableStateOf<List<Post>>(emptyList()) }
    var replies by remember(initial.id) { mutableStateOf<List<Post>>(emptyList()) }
    var loading by remember(initial.id) { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var showEngagementDetails by remember(initial.id) { mutableStateOf(false) }
    fun navigateBack() {
        if (postHistory.isNotEmpty()) {
            post = postHistory.last()
            postHistory = postHistory.dropLast(1)
        } else onBack()
    }
    fun openReplyDetail(reply: Post) {
        postHistory = postHistory + post
        post = reply
    }
    BackHandler(enabled = postHistory.isNotEmpty()) { navigateBack() }
    LaunchedEffect(post.id) {
        val id = post.id ?: return@LaunchedEffect
        loading = true
        error = null
        replies = emptyList()
        parentPost = null
        val results = withContext(Dispatchers.IO) { Triple(api.post(id), api.replies(id), api.postReactions(id)) }
        when (val result = results.first) {
            is ApiResult.Success -> {
                val loaded = result.value.toUiPost()
                post = loaded
                parentPost = loaded.parentId?.let { parentId ->
                    when (val parentResult = withContext(Dispatchers.IO) { api.post(parentId) }) {
                        is ApiResult.Success -> parentResult.value.toUiPost()
                        is ApiResult.Failure -> null
                    }
                }
            }
            is ApiResult.Failure -> error = result.message
        }
        when (val result = results.second) {
            is ApiResult.Success -> replies = result.value.map(ApiPost::toUiPost).distinctBy(::postStableKey)
            is ApiResult.Failure -> error = result.message
        }
        when (val result = results.third) { is ApiResult.Success -> if (result.value.isNotEmpty()) post = post.copy(reactions = result.value); is ApiResult.Failure -> Unit }
        loading = false
    }
    if (showEngagementDetails) {
        PostEngagementDetailScreen(
            post = post,
            api = api,
            onBack = { showEngagementDetails = false },
            onUser = { onAuthor(it.toProfilePost()) },
            onPost = ::openReplyDetail,
            onReply = onReply,
            onQuote = onQuote,
            onRekarot = onRekarot,
            onLike = onLike,
            onBookmark = onBookmark
        )
        return
    }
    Column(Modifier.fillMaxSize().background(Paper)) {
        OverlayHeader(if (postHistory.isEmpty()) "投稿" else "返信", ::navigateBack)
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = navigationBottom + 28.dp)) {
            parentPost?.let { parent ->
                item(key = "reply-parent-${parent.id}") {
                    Row(
                        Modifier.fillMaxWidth().background(PaleCarrot.copy(.42f))
                            .padding(horizontal = 22.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomIcon(IconType.REPLY, Carrot, 14.dp)
                        Spacer(Modifier.width(8.dp))
                        KText("返信先の投稿", 10, Carrot, FontWeight.Black)
                    }
                    PostCard(
                        parent,
                        onOpen = { openReplyDetail(parent) },
                        onAuthor = onAuthor,
                        onReply = onReply,
                        onQuote = onQuote,
                        onRekarot = { onRekarot(parent, it) },
                        onLike = { onLike(parent, it) },
                        onBookmark = { onBookmark(parent, it) }
                    )
                    Box(Modifier.fillMaxWidth().height(1.dp).background(Hairline))
                }
            }
            item { PostCard(post, onAuthor = onAuthor, onReply = onReply, onQuote = onQuote, onRekarot = { onRekarot(post, it) }, onLike = { onLike(post, it) }, onBookmark = { onBookmark(post, it) }, showAbsoluteTime = true, onQuotedOpen = ::openReplyDetail) }
            item {
                PostEngagementSummary(
                    viewsCount = post.viewsCount,
                    bookmarksCount = post.bookmarksCount,
                    onDetails = { showEngagementDetails = true }
                )
            }
            item {
                Row(Modifier.fillMaxWidth().background(Surface).padding(horizontal = 22.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
                    KText("返信", 14, Ink, FontWeight.Black, modifier = Modifier.weight(1f))
                    Box(Modifier.background(Carrot, RoundedCornerShape(14.dp)).clickable { onReply(post) }.padding(horizontal = 13.dp, vertical = 8.dp)) { KText("返信を書く", 10, Color.White, FontWeight.Bold) }
                }
            }
            if (error != null) item { ErrorText(error.orEmpty()) }
            if (loading) items(2) { LoadingPost() }
            if (!loading && replies.isEmpty()) item { KText("まだ返信はありません", 12, Muted, modifier = Modifier.padding(22.dp)) }
            items(replies) { reply ->
                PostCard(reply, onOpen = ::openReplyDetail, onAuthor = onAuthor, onReply = onReply, onQuote = onQuote, onRekarot = { onRekarot(reply, it) }, onLike = { onLike(reply, it) }, onBookmark = { onBookmark(reply, it) })
            }
        }
    }
}

@Composable
private fun PostEngagementSummary(viewsCount: Int, bookmarksCount: Int, onDetails: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().background(Surface)
            .border(width = 1.dp, color = Hairline)
            .padding(horizontal = 22.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomIcon(IconType.EYE, Muted, 17.dp)
        Spacer(Modifier.width(6.dp))
        KText("$viewsCount 表示", 11, Muted, FontWeight.Bold)
        Spacer(Modifier.width(18.dp))
        CustomIcon(IconType.BOOKMARK, Muted, 16.dp)
        Spacer(Modifier.width(6.dp))
        KText("$bookmarksCount ブックマーク", 11, Muted, FontWeight.Bold)
        Spacer(Modifier.weight(1f))
        Row(
            Modifier.clip(RoundedCornerShape(10.dp)).clickable(onClick = onDetails)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            KText("詳細", 11, Carrot, FontWeight.Black)
            Spacer(Modifier.width(4.dp))
            CustomIcon(IconType.FORWARD, Carrot, 14.dp)
        }
    }
}

@Composable
private fun PostEngagementDetailScreen(
    post: Post,
    api: KarotterApi,
    onBack: () -> Unit,
    onUser: (ApiUser) -> Unit,
    onPost: (Post) -> Unit,
    onReply: (Post) -> Unit,
    onQuote: (Post) -> Unit,
    onRekarot: (Post, Boolean) -> Unit,
    onLike: (Post, Boolean) -> Unit,
    onBookmark: (Post, Boolean) -> Unit
) {
    var selectedTab by remember(post.id) { mutableIntStateOf(0) }
    var selectedReaction by remember(post.id) { mutableStateOf(post.reactions.firstOrNull()?.emoji.orEmpty()) }
    var usersByTab by remember(post.id) { mutableStateOf(List(4) { emptyList<ApiUser>() }) }
    var quotePosts by remember(post.id) { mutableStateOf<List<Post>>(emptyList()) }
    var nextPages by remember(post.id) { mutableStateOf(List(4) { 1 }) }
    var hasNextByTab by remember(post.id) { mutableStateOf(List(4) { true }) }
    var loading by remember(post.id) { mutableStateOf(false) }
    var error by remember(post.id) { mutableStateOf<String?>(null) }
    val listStates = List(4) { rememberLazyListState() }
    val scope = rememberCoroutineScope()
    val safeBottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    BackHandler(onBack = onBack)
    suspend fun loadNext(tab: Int) {
        val postId = post.id ?: return
        if (tab == 3 && selectedReaction.isBlank()) {
            hasNextByTab = hasNextByTab.toMutableList().also { it[tab] = false }
            return
        }
        if (loading || !hasNextByTab.getOrElse(tab) { false }) return
        loading = true
        error = null
        val requestedPage = nextPages.getOrElse(tab) { 1 }
        if (tab == 2) {
            when (val result = withContext(Dispatchers.IO) { api.postQuotes(postId, requestedPage) }) {
                is ApiResult.Success -> {
                    val incoming = result.value.posts.map(ApiPost::toUiPost)
                        .filter { candidate -> quotePosts.none { postStableKey(it) == postStableKey(candidate) } }
                    quotePosts = (quotePosts + incoming).distinctBy(::postStableKey)
                    nextPages = nextPages.toMutableList().also {
                        it[tab] = result.value.nextPage ?: requestedPage + 1
                    }
                    hasNextByTab = hasNextByTab.toMutableList().also {
                        it[tab] = result.value.hasNext && incoming.isNotEmpty()
                    }
                }
                is ApiResult.Failure -> {
                    error = result.message
                    hasNextByTab = hasNextByTab.toMutableList().also { it[tab] = false }
                }
            }
            loading = false
            return
        }
        val result = withContext(Dispatchers.IO) {
            when (tab) {
                0 -> api.postLikes(postId, requestedPage)
                1 -> api.postRekarotUsers(postId, requestedPage)
                else -> api.postReactionUsers(postId, selectedReaction, requestedPage)
            }
        }
        when (result) {
            is ApiResult.Success -> {
                val current = usersByTab.getOrElse(tab) { emptyList() }
                val incoming = result.value.users.filter { candidate -> current.none { it.id == candidate.id } }
                usersByTab = usersByTab.toMutableList().also { it[tab] = current + incoming }
                nextPages = nextPages.toMutableList().also {
                    it[tab] = result.value.nextPage ?: requestedPage + 1
                }
                hasNextByTab = hasNextByTab.toMutableList().also {
                    it[tab] = result.value.hasNext && incoming.isNotEmpty()
                }
            }
            is ApiResult.Failure -> {
                error = result.message
                hasNextByTab = hasNextByTab.toMutableList().also { it[tab] = false }
            }
        }
        loading = false
    }
    LaunchedEffect(post.id, selectedTab, selectedReaction) {
        listStates[selectedTab].scrollToItem(0)
        val currentItemCount = if (selectedTab == 2) quotePosts.size
            else usersByTab.getOrElse(selectedTab) { emptyList() }.size
        if (currentItemCount == 0 &&
            hasNextByTab.getOrElse(selectedTab) { false }
        ) {
            loadNext(selectedTab)
        }
    }
    InfiniteLoadEffect(
        listStates[selectedTab],
        if (selectedTab == 2) quotePosts.size else usersByTab.getOrElse(selectedTab) { emptyList() }.size,
        hasNextByTab.getOrElse(selectedTab) { false },
        loading
    ) { scope.launch { loadNext(selectedTab) } }
    val engagementTabTravel = with(LocalDensity.current) { 44.dp.roundToPx() }
    Column(
        Modifier.fillMaxSize().background(Paper).pointerInput(post.id) {
            var horizontalDistance = 0f
            detectHorizontalDragGestures(
                onDragStart = { horizontalDistance = 0f },
                onDragCancel = { horizontalDistance = 0f },
                onDragEnd = {
                    when {
                        horizontalDistance < -90f && selectedTab < 3 -> selectedTab += 1
                        horizontalDistance > 90f && selectedTab > 0 -> selectedTab -= 1
                    }
                    horizontalDistance = 0f
                }
            ) { _, dragAmount -> horizontalDistance += dragAmount }
        }
    ) {
        OverlayHeader("反応したユーザー", onBack)
        Row(Modifier.fillMaxWidth().background(Surface)) {
            listOf(
                Triple("いいね", IconType.HEART, post.likes),
                Triple("リカロート", IconType.REKAROT, post.rekarots),
                Triple("引用", IconType.FORWARD, post.quoteUsersCount),
                Triple("反応", IconType.PLUS, post.reactions.sumOf { it.count })
            ).forEachIndexed { index, (label, icon, count) ->
                val indicatorWidth by animateDpAsState(
                    if (selectedTab == index) 42.dp else 0.dp,
                    tween(260, easing = FastOutSlowInEasing),
                    label = "engagementTabIndicator"
                )
                Column(
                    Modifier.weight(1f).clickable { selectedTab = index }.padding(top = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (index == 2) {
                            KText("❝", 16, if (selectedTab == index) Carrot else Muted, FontWeight.Black, maxLines = 1)
                        } else {
                            CustomIcon(icon, if (selectedTab == index) Carrot else Muted, 15.dp, filled = icon == IconType.HEART && selectedTab == index)
                        }
                        Spacer(Modifier.width(5.dp))
                        KText("$label $count", 10, if (selectedTab == index) Ink else Muted, FontWeight.Bold, maxLines = 1)
                    }
                    Spacer(Modifier.height(10.dp))
                    Box(
                        Modifier.width(indicatorWidth).height(3.dp)
                            .background(if (selectedTab == index) Carrot else Color.Transparent, RoundedCornerShape(2.dp))
                    )
                }
            }
        }
        Box(Modifier.fillMaxWidth().height(1.dp).background(Hairline))
        AnimatedContent(
            targetState = selectedTab,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            transitionSpec = {
                val forward = targetState > initialState
                (slideInHorizontally(tween(320, easing = FastOutSlowInEasing)) { if (forward) engagementTabTravel else -engagementTabTravel } + fadeIn(tween(220)))
                    .togetherWith(slideOutHorizontally(tween(260, easing = FastOutSlowInEasing)) { if (forward) -engagementTabTravel else engagementTabTravel } + fadeOut(tween(180)))
            },
            label = "engagementTabs"
        ) { currentTab ->
        Column(Modifier.fillMaxSize()) {
        if (currentTab == 3 && post.reactions.isNotEmpty()) {
            LazyRow(
                Modifier.fillMaxWidth().background(Paper).padding(vertical = 10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(post.reactions, key = { it.emoji }) { reaction ->
                    val selected = reaction.emoji == selectedReaction
                    Row(
                        Modifier.clip(RoundedCornerShape(12.dp))
                            .background(if (selected) PaleCarrot else Surface)
                            .border(1.dp, if (selected) Carrot else Hairline, RoundedCornerShape(12.dp))
                            .clickable {
                                if (!selected) {
                                    selectedReaction = reaction.emoji
                                    usersByTab = usersByTab.toMutableList().also { it[3] = emptyList() }
                                    nextPages = nextPages.toMutableList().also { it[3] = 1 }
                                    hasNextByTab = hasNextByTab.toMutableList().also { it[3] = true }
                                    error = null
                                }
                            }
                            .padding(horizontal = 10.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ReactionVisual(reaction.emoji, 20.dp, 13)
                        Spacer(Modifier.width(5.dp))
                        KText(reaction.count.toString(), 9, if (selected) Carrot else Muted, FontWeight.Black)
                    }
                }
            }
            Box(Modifier.fillMaxWidth().height(1.dp).background(Hairline))
        }
        LazyColumn(state = listStates[currentTab], modifier = Modifier.weight(1f).fillMaxWidth(), contentPadding = PaddingValues(bottom = safeBottom + 24.dp)) {
            val users = usersByTab.getOrElse(currentTab) { emptyList() }
            val activeEmpty = if (currentTab == 2) quotePosts.isEmpty() else users.isEmpty()
            if (loading && activeEmpty) items(5) { LoadingPost() }
            if (!loading && error != null) item { ErrorText(error.orEmpty()) }
            if (!loading && activeEmpty) {
                item {
                    KText(
                        if (currentTab == 2) "引用投稿はありません" else "該当するユーザーはいません",
                        12, Muted, modifier = Modifier.padding(22.dp)
                    )
                }
            }
            if (currentTab == 2) {
                items(quotePosts, key = ::postStableKey) { quoted ->
                    PostCard(
                        quoted,
                        onOpen = onPost,
                        onAuthor = { onUser(it.toApiUser()) },
                        onReply = onReply,
                        onQuote = onQuote,
                        onRekarot = { onRekarot(quoted, it) },
                        onLike = { onLike(quoted, it) },
                        onBookmark = { onBookmark(quoted, it) }
                    )
                }
            } else {
                items(users, key = { it.id }) { user ->
                    EngagementUserRow(user) { onUser(user) }
                }
            }
            if (loading && !activeEmpty) item { LoadingPost() }
        }
        }
        }
    }
}

@Composable
private fun EngagementUserRow(user: ApiUser, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 22.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(46.dp).background(PaleCarrot, RoundedCornerShape(15.dp)), contentAlignment = Alignment.Center) {
            if (user.avatarUrl != null) {
                AsyncImage(user.avatarUrl, user.displayName, Modifier.fillMaxSize().clip(RoundedCornerShape(15.dp)), contentScale = ContentScale.Crop)
            } else {
                KText(user.displayName.ifBlank { user.username }.take(1), 16, Ink, FontWeight.Black)
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                KText(
                    user.displayName.ifBlank { user.username },
                    14,
                    Ink,
                    FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Spacer(Modifier.width(5.dp))
                AccountMarks(user.officialMarks.ifEmpty { listOf(user.officialMark) }, user.isBotAccount, user.isParodyAccount, user.isPrivate, compact = true)
                SubscriptionBadge(
                    user.subscriptionPlan,
                    user.subscriptionStatus,
                    user.showSubscriptionBadges && if (user.subscriptionPlan.equals("PLUS", true)) user.showPlusBadge else user.showProBadge,
                    user.premiumBadgeColor,
                    compact = true
                )
            }
            KText("@${user.username}", 11, Muted, maxLines = 1)
        }
        CustomIcon(IconType.FORWARD, Muted, 16.dp)
    }
}

@Composable
private fun UserDetailScreen(initial: Post, retainedState: UserDetailRetainedState, api: KarotterApi, viewerUserId: Long?, onBack: () -> Unit, onOpen: (Post) -> Unit, onReply: (Post) -> Unit, onQuote: (Post) -> Unit, onRekarot: (Post, Boolean) -> Unit, onLike: (Post, Boolean) -> Unit, onBookmark: (Post, Boolean) -> Unit, onCompose: () -> Unit, onDm: (ApiUser) -> Unit, onUser: (ApiUser) -> Unit) {
    var profile by retainedState.profile
    var reloadKey by retainedState.reloadKey
    LaunchedEffect(initial.handle, reloadKey) {
        if (profile != null && retainedState.loadedReloadKey.intValue == reloadKey) return@LaunchedEffect
        val found = withContext(Dispatchers.IO) { api.user(initial.handle.removePrefix("@")) }
        profile = (found as? ApiResult.Success)?.value ?: initial.toApiUser()
        retainedState.loadedReloadKey.intValue = reloadKey
    }
    profile?.let { SharedProfilePage(it, false, api, onBack, null, onOpen, onReply, onQuote, onRekarot, onLike, onBookmark, onCompose = onCompose, onUser = onUser, viewerUserId = viewerUserId, onDm = onDm, onProfileReload = { reloadKey += 1 }, retainedState = retainedState.page) }
        ?: Column(Modifier.fillMaxSize().background(Paper)) { OverlayHeader(initial.name, onBack); repeat(3) { LoadingPost() } }
}

@Composable
private fun StoryVideoAttachment(url: String) {
    var videoView by remember(url) { mutableStateOf<VideoView?>(null) }
    var player by remember(url) { mutableStateOf<MediaPlayer?>(null) }
    var muted by remember(url) { mutableStateOf(false) }
    var prepared by remember(url) { mutableStateOf(false) }

    LaunchedEffect(muted, player) {
        player?.setVolume(if (muted) 0f else 1f, if (muted) 0f else 1f)
    }
    DisposableEffect(url) {
        onDispose {
            videoView?.stopPlayback()
            videoView = null
            player = null
        }
    }
    Box(Modifier.fillMaxSize().background(Color.Black).clipToBounds()) {
        AndroidView(
            factory = { context ->
                VideoView(context).apply {
                    videoView = this
                    setVideoURI(Uri.parse(url))
                    setOnPreparedListener { preparedPlayer ->
                        player = preparedPlayer
                        prepared = true
                        preparedPlayer.isLooping = true
                        preparedPlayer.setVolume(if (muted) 0f else 1f, if (muted) 0f else 1f)
                        post {
                            val videoWidth = preparedPlayer.videoWidth.toFloat().coerceAtLeast(1f)
                            val videoHeight = preparedPlayer.videoHeight.toFloat().coerceAtLeast(1f)
                            val viewWidth = width.toFloat().coerceAtLeast(1f)
                            val viewHeight = height.toFloat().coerceAtLeast(1f)
                            val videoRatio = videoWidth / videoHeight
                            val viewRatio = viewWidth / viewHeight
                            val centerCropScale = if (videoRatio > viewRatio) {
                                videoRatio / viewRatio
                            } else {
                                viewRatio / videoRatio
                            }
                            scaleX = centerCropScale
                            scaleY = centerCropScale
                            start()
                        }
                    }
                    setOnCompletionListener { start() }
                }
            },
            update = { videoView = it },
            modifier = Modifier.fillMaxSize()
        )
        if (!prepared) {
            KText("動画を読み込み中…", 11, Color.White.copy(.72f), FontWeight.Bold, modifier = Modifier.align(Alignment.Center))
        }
        Box(
            Modifier.align(Alignment.TopEnd).padding(top = 14.dp, end = 14.dp).size(42.dp)
                .background(Color.Black.copy(.58f), CircleShape)
                .border(1.dp, Color.White.copy(.25f), CircleShape)
                .clickable { muted = !muted },
            contentAlignment = Alignment.Center
        ) {
            CustomIcon(if (muted) IconType.VOLUME_OFF else IconType.VOLUME_ON, Color.White, 19.dp)
        }
    }
}

@Composable
private fun StoryDetailScreen(
    story: ApiStory,
    transitionDirection: Int,
    api: KarotterApi,
    viewerUserId: Long?,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onUser: (ApiUser) -> Unit,
    onUpdated: (ApiStory) -> Unit,
    onDeleted: () -> Unit
) {
    LaunchedEffect(story.id) { withContext(Dispatchers.IO) { api.viewStory(story.id) } }
    AnimatedContent(
        targetState = story,
        modifier = Modifier.fillMaxSize().background(Color(0xFF0B0C0E)).clipToBounds(),
        transitionSpec = {
            slideInVertically(tween(340, easing = FastOutSlowInEasing)) { transitionDirection * it }
                .togetherWith(slideOutVertically(tween(340, easing = FastOutSlowInEasing)) { -transitionDirection * it })
        },
        label = "storyMove"
    ) { shownStory ->
        var verticalDrag by remember(shownStory.id) { mutableStateOf(0f) }
        var liked by remember(shownStory.id) { mutableStateOf(shownStory.liked) }
        var likesCount by remember(shownStory.id) { mutableIntStateOf(shownStory.likesCount) }
        var commentsCount by remember(shownStory.id) { mutableIntStateOf(shownStory.commentsCount) }
        var commentText by remember(shownStory.id) { mutableStateOf("") }
        var likeBusy by remember(shownStory.id) { mutableStateOf(false) }
        var commentBusy by remember(shownStory.id) { mutableStateOf(false) }
        var showComments by remember(shownStory.id) { mutableStateOf(false) }
        var confirmDelete by remember(shownStory.id) { mutableStateOf(false) }
        var deleteBusy by remember(shownStory.id) { mutableStateOf(false) }
        var actionError by remember(shownStory.id) { mutableStateOf<String?>(null) }
        val scope = rememberCoroutineScope()
        val isOwnStory = viewerUserId != null && viewerUserId == shownStory.author.id

        fun submitComment() {
            val content = commentText.trim()
            if (content.isBlank() || commentBusy) return
            commentBusy = true
            actionError = null
            scope.launch {
                when (val result = withContext(Dispatchers.IO) { api.commentStory(shownStory.id, content) }) {
                    is ApiResult.Success -> {
                        commentText = ""
                        commentsCount += 1
                        onUpdated(shownStory.copy(liked = liked, likesCount = likesCount, commentsCount = commentsCount))
                    }
                    is ApiResult.Failure -> actionError = result.message
                }
                commentBusy = false
            }
        }

        if (showComments) {
            StoryCommentsDialog(
                story = shownStory,
                api = api,
                onDismiss = { showComments = false },
                onCommentAdded = {
                    commentsCount += 1
                    onUpdated(shownStory.copy(liked = liked, likesCount = likesCount, commentsCount = commentsCount))
                }
            )
        }
        if (confirmDelete) {
            Dialog(onDismissRequest = { if (!deleteBusy) confirmDelete = false }) {
                Column(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Surface)
                        .border(1.dp, Hairline, RoundedCornerShape(22.dp)).padding(21.dp)
                ) {
                    KText("ストーリーを削除しますか？", 17, Ink, FontWeight.Black)
                    Spacer(Modifier.height(7.dp))
                    KText("削除したストーリーは元に戻せません。", 12, Muted)
                    Spacer(Modifier.height(18.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                        Box(
                            Modifier.weight(1f).clip(RoundedCornerShape(13.dp))
                                .border(1.dp, Hairline, RoundedCornerShape(13.dp))
                                .clickable(enabled = !deleteBusy) { confirmDelete = false }
                                .padding(vertical = 11.dp),
                            contentAlignment = Alignment.Center
                        ) { KText("キャンセル", 11, Ink, FontWeight.Bold) }
                        Box(
                            Modifier.weight(1f).clip(RoundedCornerShape(13.dp)).background(Color(0xFFD64045))
                                .clickable(enabled = !deleteBusy) {
                                    deleteBusy = true
                                    scope.launch {
                                        when (val result = withContext(Dispatchers.IO) { api.deleteStory(shownStory.id) }) {
                                            is ApiResult.Success -> onDeleted()
                                            is ApiResult.Failure -> {
                                                actionError = result.message
                                                deleteBusy = false
                                                confirmDelete = false
                                            }
                                        }
                                    }
                                }.padding(vertical = 11.dp),
                            contentAlignment = Alignment.Center
                        ) { KText(if (deleteBusy) "削除中…" else "削除", 11, Color.White, FontWeight.Black) }
                    }
                }
            }
        }

        Column(
            Modifier.fillMaxSize().background(Color(0xFF0B0C0E)).pointerInput(shownStory.id) {
                detectDragGestures(
                    onDragStart = { verticalDrag = 0f },
                    onDragCancel = { verticalDrag = 0f },
                    onDragEnd = {
                        when {
                            verticalDrag < -110f -> onNext()
                            verticalDrag > 110f -> onPrevious()
                        }
                        verticalDrag = 0f
                    }
                ) { _, dragAmount -> verticalDrag += dragAmount.y }
            }
        ) {
            Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Row(
                    Modifier.weight(1f).clip(RoundedCornerShape(18.dp))
                        .clickable { onUser(shownStory.author) }.padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(38.dp).background(avatarColor(shownStory.author.id), CircleShape)) {
                        shownStory.author.avatarUrl?.let {
                            AsyncImage(it, shownStory.author.displayName, Modifier.fillMaxSize().clip(CircleShape), contentScale = ContentScale.Crop)
                        }
                    }
                    Spacer(Modifier.width(9.dp))
                    Column {
                        KText(shownStory.author.displayName.ifBlank { shownStory.author.username }, 13, Color.White, FontWeight.Bold, maxLines = 1)
                        KText("@${shownStory.author.username}", 9, Color.White.copy(.58f), maxLines = 1)
                    }
                }
                if (isOwnStory) {
                    Box(
                        Modifier.size(38.dp).clip(CircleShape).clickable { confirmDelete = true },
                        contentAlignment = Alignment.Center
                    ) { CustomIcon(IconType.TRASH, Color.White, 19.dp) }
                }
                Box(
                    Modifier.size(38.dp).background(Color.White.copy(.12f), CircleShape).clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) { CustomIcon(IconType.CLOSE, Color.White, 20.dp) }
            }
            var storyCanvasSize by remember(shownStory.id) { mutableStateOf(Size.Zero) }
            var overlaySize by remember(shownStory.id) { mutableStateOf(Size.Zero) }
            Box(
                Modifier.fillMaxSize().clipToBounds().onGloballyPositioned {
                    storyCanvasSize = Size(it.size.width.toFloat(), it.size.height.toFloat())
                },
                contentAlignment = Alignment.Center
            ) {
                shownStory.mediaUrl?.let { url ->
                    if (url.substringAfterLast('.').lowercase() in setOf("mp4", "webm", "mov", "m4v")) {
                        StoryVideoAttachment(url)
                    } else {
                        AsyncImage(
                            url,
                            shownStory.caption.ifBlank { "ストーリー" },
                            Modifier.fillMaxWidth().fillMaxHeight(.82f),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
                val overlayText = shownStory.textOverlay.orEmpty()
                if (overlayText.isNotBlank()) {
                    val style = shownStory.textOverlayStyle ?: ApiStoryTextStyle()
                    val selectedColor = style.color.toAppColor() ?: Color.White
                    val overlayColor = if (style.background == "solid") {
                        if (selectedColor.luminance() > .62f) Color(0xFF111827) else Color.White
                    } else selectedColor
                    val overlayBackground = when (style.background.lowercase(Locale.ROOT)) {
                        "soft", "dark", "black" -> Color(0x610F172A)
                        "solid", "light", "white" -> selectedColor
                        else -> Color.Transparent
                    }
                    val overlayShape = RoundedCornerShape(if (style.background == "none") 0.dp else 26.dp)
                    KText(
                        overlayText,
                        style.size.coerceIn(18, 44),
                        overlayColor,
                        FontWeight.Black,
                        textAlign = when (style.align.lowercase(Locale.ROOT)) {
                            "left" -> TextAlign.Left
                            "right" -> TextAlign.Right
                            else -> TextAlign.Center
                        },
                        modifier = Modifier.align(Alignment.TopStart).widthIn(max = 320.dp)
                            .graphicsLayer {
                                translationX = storyCanvasSize.width * style.x.coerceIn(10f, 90f) / 100f - overlaySize.width / 2f
                                translationY = storyCanvasSize.height * style.y.coerceIn(14f, 86f) / 100f - overlaySize.height / 2f
                            }
                            .onGloballyPositioned {
                                overlaySize = Size(it.size.width.toFloat(), it.size.height.toFloat())
                            }
                            .background(overlayBackground, overlayShape)
                            .then(
                                if (style.background == "none") Modifier
                                else Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                            )
                    )
                } else if (shownStory.caption.isNotBlank()) {
                    KText(
                        shownStory.caption,
                        15,
                        Color.White,
                        FontWeight.Bold,
                        modifier = Modifier.align(Alignment.BottomCenter)
                            .padding(start = 18.dp, end = 18.dp, bottom = 108.dp)
                            .background(Color(0xA60B0C0E), RoundedCornerShape(14.dp))
                            .border(1.dp, Color.White.copy(.18f), RoundedCornerShape(14.dp))
                            .padding(14.dp)
                    )
                }
                Column(
                    Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                        .background(Color.Black.copy(.52f))
                        .navigationBarsPadding()
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    actionError?.let {
                        KText(it, 9, Color(0xFFFFB4B4), FontWeight.Bold, maxLines = 2)
                        Spacer(Modifier.height(6.dp))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Row(
                            Modifier.weight(1f).heightIn(min = 46.dp)
                                .border(1.dp, Color.White.copy(.48f), RoundedCornerShape(24.dp))
                                .padding(horizontal = 15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BasicTextField(
                                value = commentText,
                                onValueChange = { commentText = it.take(280); actionError = null },
                                singleLine = true,
                                textStyle = TextStyle(Color.White, 13.sp, FontWeight.Medium),
                                cursorBrush = androidx.compose.ui.graphics.SolidColor(Color.White),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                keyboardActions = KeyboardActions(onSend = { submitComment() }),
                                modifier = Modifier.weight(1f),
                                decorationBox = { inner ->
                                    Box {
                                        if (commentText.isBlank()) KText("コメントを追加…", 12, Color.White.copy(.65f))
                                        inner()
                                    }
                                }
                            )
                            if (commentText.isNotBlank()) {
                                Box(
                                    Modifier.size(34.dp).clickable(enabled = !commentBusy) { submitComment() },
                                    contentAlignment = Alignment.Center
                                ) { CustomIcon(IconType.SEND, if (commentBusy) Color.White.copy(.4f) else Color.White, 18.dp) }
                            }
                        }
                        Spacer(Modifier.width(8.dp))
                        Column(
                            Modifier.clip(RoundedCornerShape(13.dp)).clickable { showComments = true }
                                .padding(horizontal = 7.dp, vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CustomIcon(IconType.BOARD, Color.White, 22.dp)
                            KText(commentsCount.toString(), 8, Color.White.copy(.75f), FontWeight.Bold)
                        }
                        Spacer(Modifier.width(4.dp))
                        Column(
                            Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CustomIcon(IconType.EYE, Color.White, 22.dp)
                            KText(shownStory.viewsCount.toString(), 8, Color.White.copy(.75f), FontWeight.Bold)
                        }
                        Spacer(Modifier.width(4.dp))
                        Column(
                            Modifier.clip(RoundedCornerShape(13.dp)).clickable(enabled = !likeBusy) {
                                val desired = !liked
                                val previousLiked = liked
                                val previousCount = likesCount
                                liked = desired
                                likesCount = (likesCount + if (desired) 1 else -1).coerceAtLeast(0)
                                likeBusy = true
                                actionError = null
                                scope.launch {
                                    when (val result = withContext(Dispatchers.IO) { api.likeStory(shownStory.id, desired) }) {
                                        is ApiResult.Success -> onUpdated(
                                            shownStory.copy(liked = liked, likesCount = likesCount, commentsCount = commentsCount)
                                        )
                                        is ApiResult.Failure -> {
                                            liked = previousLiked
                                            likesCount = previousCount
                                            actionError = result.message
                                        }
                                    }
                                    likeBusy = false
                                }
                            }.padding(horizontal = 7.dp, vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CustomIcon(IconType.HEART, if (liked) Color(0xFFFF4D67) else Color.White, 23.dp, filled = liked)
                            KText(likesCount.toString(), 8, Color.White.copy(.75f), FontWeight.Bold)
                        }
                    }
                }
                KText("上で次へ・下で前へ", 9, Color.White.copy(.55f), modifier = Modifier.align(Alignment.TopCenter).padding(top = 8.dp))
            }
        }
    }
}

@Composable
private fun StoryCommentsDialog(
    story: ApiStory,
    api: KarotterApi,
    onDismiss: () -> Unit,
    onCommentAdded: () -> Unit
) {
    var comments by remember(story.id) { mutableStateOf<List<ApiStoryComment>>(emptyList()) }
    var loading by remember(story.id) { mutableStateOf(true) }
    var content by remember(story.id) { mutableStateOf("") }
    var sending by remember(story.id) { mutableStateOf(false) }
    var error by remember(story.id) { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(story.id) {
        when (val result = withContext(Dispatchers.IO) { api.storyComments(story.id) }) {
            is ApiResult.Success -> comments = result.value
            is ApiResult.Failure -> error = result.message
        }
        loading = false
    }
    fun send() {
        val text = content.trim()
        if (text.isBlank() || sending) return
        sending = true
        error = null
        scope.launch {
            when (val result = withContext(Dispatchers.IO) { api.commentStory(story.id, text) }) {
                is ApiResult.Success -> {
                    comments = comments + result.value
                    content = ""
                    onCommentAdded()
                }
                is ApiResult.Failure -> error = result.message
            }
            sending = false
        }
    }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Box(Modifier.fillMaxSize().background(Color.Black.copy(.35f)).clickable { onDismiss() })
            Column(
                Modifier.fillMaxWidth().fillMaxHeight(.72f)
                    .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                    .background(Surface)
                    .navigationBarsPadding()
            ) {
                Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    KText("コメント", 17, Ink, FontWeight.Black, modifier = Modifier.weight(1f))
                    Box(Modifier.size(36.dp).clickable { onDismiss() }, contentAlignment = Alignment.Center) {
                        CustomIcon(IconType.CLOSE, Ink, 17.dp)
                    }
                }
                Box(Modifier.fillMaxWidth().height(1.dp).background(Hairline))
                LazyColumn(
                    Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(13.dp)
                ) {
                    if (loading) items(4) { LoadingPost() }
                    if (!loading && error != null) item { ErrorText(error.orEmpty()) }
                    if (!loading && error == null && comments.isEmpty()) {
                        item { KText("まだコメントはありません", 12, Muted, modifier = Modifier.padding(vertical = 20.dp)) }
                    }
                    items(comments, key = { it.id }) { comment ->
                        Row(verticalAlignment = Alignment.Top) {
                            Box(Modifier.size(36.dp).background(PaleCarrot, CircleShape), contentAlignment = Alignment.Center) {
                                comment.author?.avatarUrl?.let {
                                    AsyncImage(it, comment.author.displayName, Modifier.fillMaxSize().clip(CircleShape), contentScale = ContentScale.Crop)
                                } ?: KText(comment.author?.displayName?.take(1).orEmpty(), 12, Ink, FontWeight.Black)
                            }
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                KText(comment.author?.displayName?.ifBlank { "@${comment.author.username}" } ?: "ユーザー", 11, Ink, FontWeight.Bold)
                                Spacer(Modifier.height(3.dp))
                                KText(comment.content, 12, Ink, lineHeight = 18f)
                            }
                        }
                    }
                }
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp)
                        .border(1.dp, Hairline, RoundedCornerShape(22.dp)).padding(start = 14.dp, end = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = content,
                        onValueChange = { content = it.take(280); error = null },
                        singleLine = true,
                        textStyle = TextStyle(Ink, 13.sp, FontWeight.Medium),
                        cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = { send() }),
                        modifier = Modifier.weight(1f).padding(vertical = 13.dp),
                        decorationBox = { inner ->
                            Box {
                                if (content.isBlank()) KText("コメントを追加…", 12, Muted)
                                inner()
                            }
                        }
                    )
                    Box(
                        Modifier.size(38.dp).background(if (content.isNotBlank() && !sending) Carrot else Hairline, CircleShape)
                            .clickable(enabled = content.isNotBlank() && !sending) { send() },
                        contentAlignment = Alignment.Center
                    ) { CustomIcon(IconType.SEND, Color.White, 17.dp) }
                }
            }
        }
    }
}

@Composable
private fun BoardCreateScreen(
    api: KarotterApi,
    onBack: () -> Unit,
    onCreated: (ApiBoard) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var slug by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var minimumAge by remember { mutableIntStateOf(13) }
    var sending by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val slugValid = slug.length >= 2 && slug.all { it.isLowerCase() || it.isDigit() || it == '-' || it == '_' }
    val enabled = title.isNotBlank() && slugValid && description.isNotBlank() && !sending
    Column(Modifier.fillMaxSize().background(Paper).imePadding()) {
        OverlayHeader("新しい板を作成", onBack)
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp)) {
            item {
                KText("CREATE BOARD", 10, Carrot, FontWeight.Black, letterSpacing = 2f)
                Spacer(Modifier.height(8.dp))
                KText("話題の入口になる名前と説明を設定します。", 13, Muted, lineHeight = 20f)
                Spacer(Modifier.height(20.dp))
                BoardCreateField(
                    value = title,
                    onChange = { title = it.replace("\n", "").take(60); error = null },
                    label = "板の名前",
                    hint = "例：Android"
                )
                Spacer(Modifier.height(12.dp))
                BoardCreateField(
                    value = slug,
                    onChange = {
                        slug = it.lowercase(Locale.ROOT).filter { char ->
                            char.isLowerCase() || char.isDigit() || char == '-' || char == '_'
                        }.take(40)
                        error = null
                    },
                    label = "スラッグ",
                    hint = "例：android"
                )
                Spacer(Modifier.height(6.dp))
                KText("URLに使う英小文字・数字・ハイフン・アンダーバー", 9, if (slug.isNotEmpty() && !slugValid) Carrot else Muted)
                Spacer(Modifier.height(12.dp))
                BoardCreateField(
                    value = description,
                    onChange = { description = it.take(300); error = null },
                    label = "説明",
                    hint = "この板で話す内容",
                    singleLine = false,
                    minHeight = 128.dp
                )
                Spacer(Modifier.height(18.dp))
                KText("最低年齢", 11, Ink, FontWeight.Black)
                Spacer(Modifier.height(9.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(13, 16, 18).forEach { age ->
                        val selected = minimumAge == age
                        Box(
                            Modifier.clip(RoundedCornerShape(13.dp))
                                .background(if (selected) Strong else Surface)
                                .border(1.dp, if (selected) Strong else Hairline, RoundedCornerShape(13.dp))
                                .clickable { minimumAge = age }
                                .padding(horizontal = 18.dp, vertical = 9.dp)
                        ) { KText("${age}歳以上", 10, if (selected) OnStrong else Muted, FontWeight.Bold) }
                    }
                }
                if (error != null) {
                    Spacer(Modifier.height(14.dp))
                    ErrorText(error.orEmpty())
                }
                Spacer(Modifier.height(22.dp))
                Box(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(17.dp))
                        .background(if (enabled) Carrot else Hairline)
                        .clickable(enabled = enabled) {
                            sending = true
                            error = null
                            scope.launch {
                                when (val result = withContext(Dispatchers.IO) {
                                    api.createBoard(title.trim(), slug.trim(), description.trim(), minimumAge)
                                }) {
                                    is ApiResult.Success -> onCreated(result.value)
                                    is ApiResult.Failure -> {
                                        error = result.message
                                        sending = false
                                    }
                                }
                            }
                        }.padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    KText(if (sending) "作成中…" else "板を作成", 12, if (enabled) Color.White else Muted, FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun BoardCreateField(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    hint: String,
    singleLine: Boolean = true,
    minHeight: Dp = 58.dp
) {
    Column {
        KText(label, 11, Ink, FontWeight.Black)
        Spacer(Modifier.height(7.dp))
        BasicTextField(
            value = value,
            onValueChange = onChange,
            singleLine = singleLine,
            textStyle = TextStyle(Ink, 15.sp, FontWeight.Medium, lineHeight = 22.sp),
            cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
            modifier = Modifier.fillMaxWidth().heightIn(min = minHeight)
                .clip(RoundedCornerShape(17.dp)).background(Surface)
                .border(1.dp, Hairline, RoundedCornerShape(17.dp)).padding(15.dp),
            decorationBox = { inner ->
                Box {
                    if (value.isBlank()) KText(hint, 14, Muted)
                    inner()
                }
            }
        )
    }
}

@Composable
private fun BoardDetailScreen(
    initial: ApiBoard,
    api: KarotterApi,
    viewerUserId: Long?,
    onBack: () -> Unit,
    onDeleted: (String) -> Unit
) {
    var board by remember { mutableStateOf(initial) }
    var threads by remember { mutableStateOf<List<ApiThread>>(emptyList()) }
    var selectedThread by remember { mutableStateOf<ApiThread?>(null) }
    var creatingThread by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var reloadKey by remember { mutableIntStateOf(0) }
    var refreshing by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    BackHandler(enabled = selectedThread != null || creatingThread) {
        if (creatingThread) creatingThread = false else selectedThread = null
    }
    suspend fun refreshBoard() {
        if (refreshing) return
        refreshing = true
        when (val result = withContext(Dispatchers.IO) { api.board(initial.slug) }) {
            is ApiResult.Success -> {
                board = result.value.first
                threads = result.value.second
                error = null
            }
            is ApiResult.Failure -> error = result.message
        }
        refreshing = false
    }
    LaunchedEffect(initial.slug, reloadKey) {
        refreshBoard()
    }
    LaunchedEffect(initial.slug) {
        while (true) {
            delay(AUTO_REFRESH_INTERVAL_MS)
            refreshBoard()
        }
    }
    if (creatingThread) {
        BoardThreadComposer(
            board = board,
            onBack = { creatingThread = false },
            onSubmit = { title, content, done ->
                scope.launch {
                    when (val result = withContext(Dispatchers.IO) { api.createBoardThread(board.slug, title, content) }) {
                        is ApiResult.Success -> {
                            creatingThread = false
                            reloadKey += 1
                            done(null)
                        }
                        is ApiResult.Failure -> done(result.message)
                    }
                }
            }
        )
        return
    }
    selectedThread?.let { thread ->
        BoardThreadScreen(board, thread, api) { selectedThread = null }
        return
    }
    if (confirmDelete) {
        Dialog(onDismissRequest = { if (!deleting) confirmDelete = false }) {
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Surface)
                    .border(1.dp, Hairline, RoundedCornerShape(22.dp)).padding(21.dp)
            ) {
                KText("この板を削除しますか？", 17, Ink, FontWeight.Black)
                Spacer(Modifier.height(7.dp))
                KText("「${board.name}」と、その中のスレッドを削除します。この操作は元に戻せません。", 12, Muted, lineHeight = 18f)
                Spacer(Modifier.height(18.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    Box(
                        Modifier.weight(1f).clip(RoundedCornerShape(13.dp))
                            .border(1.dp, Hairline, RoundedCornerShape(13.dp))
                            .clickable(enabled = !deleting) { confirmDelete = false }.padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        KText("キャンセル", 11, Ink, FontWeight.Bold)
                    }
                    Box(
                        Modifier.weight(1f).clip(RoundedCornerShape(13.dp)).background(Color(0xFFD64045))
                            .clickable(enabled = !deleting) {
                                deleting = true
                                scope.launch {
                                    when (val result = withContext(Dispatchers.IO) { api.deleteBoard(board.slug) }) {
                                        is ApiResult.Success -> onDeleted(board.slug)
                                        is ApiResult.Failure -> {
                                            error = result.message
                                            deleting = false
                                            confirmDelete = false
                                        }
                                    }
                                }
                            }.padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        KText(if (deleting) "削除中…" else "削除", 11, Color.White, FontWeight.Black)
                    }
                }
            }
        }
    }
    Box(Modifier.fillMaxSize().background(Paper)) {
        Column(Modifier.fillMaxSize()) {
            OverlayHeader(
                board.name,
                onBack,
                trailing = if (viewerUserId != null && board.ownerId == viewerUserId) {
                    {
                        Box(
                            Modifier.size(38.dp).clip(CircleShape).border(1.dp, Hairline, CircleShape)
                                .clickable { confirmDelete = true },
                            contentAlignment = Alignment.Center
                        ) {
                            CustomIcon(IconType.TRASH, Color(0xFFD64045), 17.dp)
                        }
                    }
                } else null
            )
            LazyColumn(state = listState, contentPadding = PaddingValues(bottom = 104.dp)) {
                item { Column(Modifier.padding(22.dp)) { KText(board.description, 14, Ink, lineHeight = 21f); Spacer(Modifier.height(8.dp)); KText("${board.threadCount}件のスレッド", 10, Carrot, FontWeight.Bold) } }
                if (error != null) item { ErrorText(error.orEmpty()) }
                if (threads.isEmpty() && error == null) item { LoadingPost() }
                itemsIndexed(threads, key = { _, thread -> thread.id }) { i, thread ->
                    Column(Modifier.fillMaxWidth().clickable { selectedThread = thread }.padding(horizontal = 22.dp, vertical = 15.dp)) {
                        KText("${i + 1}  ·  ${thread.authorName}", 10, Muted, FontWeight.Bold)
                        Spacer(Modifier.height(5.dp)); KText(thread.title, 16, Ink, FontWeight.Bold)
                        if (thread.content.isNotBlank()) { Spacer(Modifier.height(6.dp)); RichContentText(thread.content, 12, Muted, maxLines = 3) }
                        Spacer(Modifier.height(7.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            KText("${thread.repliesCount}件の返信", 10, Carrot, FontWeight.Bold)
                            CustomIcon(IconType.FORWARD, Carrot, 12.dp)
                        }
                    }
                }
            }
        }
        Column(
            Modifier.align(Alignment.BottomEnd).padding(end = 20.dp, bottom = 22.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                Modifier.size(45.dp).clip(RoundedCornerShape(16.dp)).background(Surface)
                    .border(1.dp, Hairline, RoundedCornerShape(16.dp))
                    .clickable(enabled = !refreshing) { scope.launch { refreshBoard() } },
                contentAlignment = Alignment.Center
            ) {
                if (refreshing) KText("…", 20, Muted, FontWeight.Bold)
                else CustomIcon(IconType.REFRESH, Carrot, 20.dp)
            }
            Box(
                Modifier.size(58.dp).clip(RoundedCornerShape(20.dp)).background(Carrot)
                    .clickable { creatingThread = true },
                contentAlignment = Alignment.Center
            ) {
                CustomIcon(IconType.PLUS, Color.White, 24.dp)
            }
        }
    }
}

@Composable
private fun BoardThreadScreen(board: ApiBoard, initial: ApiThread, api: KarotterApi, onBack: () -> Unit) {
    val context = LocalContext.current
    var detail by remember(initial.id) { mutableStateOf<social.karotter.client.data.ApiThreadDetail?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var replyText by remember(initial.id) { mutableStateOf("") }
    var replyImages by remember(initial.id) { mutableStateOf<List<ComposerMedia>>(emptyList()) }
    var sending by remember(initial.id) { mutableStateOf(false) }
    var refreshing by remember(initial.id) { mutableStateOf(false) }
    var reloadKey by remember(initial.id) { mutableIntStateOf(0) }
    var initialBottomScrollDone by remember(initial.id) { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
        val picked = uris.take(4).map { uri ->
            val name = context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) cursor.getString(0) else null
            } ?: "board-${System.currentTimeMillis()}.jpg"
            ComposerMedia(uri, name, context.contentResolver.getType(uri).orEmpty().ifBlank { "image/jpeg" })
        }
        replyImages = (replyImages + picked).distinctBy { it.uri }.take(4)
    }
    suspend fun refreshThread() {
        if (refreshing) return
        refreshing = true
        when (val result = withContext(Dispatchers.IO) { api.boardThread(board.slug, initial.id) }) {
            is ApiResult.Success -> {
                detail = result.value
                error = null
            }
            is ApiResult.Failure -> error = result.message
        }
        refreshing = false
    }
    LaunchedEffect(initial.id, reloadKey) {
        refreshThread()
    }
    LaunchedEffect(initial.id) {
        while (true) {
            delay(AUTO_REFRESH_INTERVAL_MS)
            refreshThread()
        }
    }
    LaunchedEffect(detail?.replies?.size) {
        val loaded = detail ?: return@LaunchedEffect
        if (initialBottomScrollDone) return@LaunchedEffect
        delay(80L)
        listState.scrollToItem(loaded.replies.size + 1)
        initialBottomScrollDone = true
    }
    Column(Modifier.fillMaxSize().background(Paper).imePadding()) {
        OverlayHeader(board.name, onBack)
        LazyColumn(state = listState, modifier = Modifier.weight(1f), contentPadding = PaddingValues(bottom = 24.dp)) {
            item {
                Column(Modifier.fillMaxWidth().padding(22.dp)) {
                    KText(detail?.thread?.authorName ?: initial.authorName, 10, Carrot, FontWeight.Bold)
                    Spacer(Modifier.height(7.dp))
                    KText(detail?.thread?.title ?: initial.title, 23, Ink, FontWeight.Black, lineHeight = 30f)
                    Spacer(Modifier.height(13.dp))
                    RichContentText(detail?.thread?.content ?: initial.content, 15, Ink, lineHeight = 24f)
                    detail?.media?.takeIf { it.isNotEmpty() }?.let { Spacer(Modifier.height(14.dp)); MediaGallery(it) }
                }
            }
            if (error != null) item { ErrorText(error.orEmpty()) }
            if (detail == null && error == null) items(2) { LoadingPost() }
            detail?.let { value ->
                item { KText("返信 ${value.replies.size}", 13, Ink, FontWeight.Black, modifier = Modifier.fillMaxWidth().background(Surface).padding(22.dp, 14.dp)) }
                items(value.replies, key = { it.id }) { reply ->
                    Column(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 15.dp)) {
                        Row { KText("${reply.number}", 11, Carrot, FontWeight.Black); Spacer(Modifier.width(9.dp)); KText(reply.authorName, 11, Muted, FontWeight.Bold); Spacer(Modifier.weight(1f)); KText(relativeTime(reply.createdAt), 9, Muted) }
                        Spacer(Modifier.height(7.dp)); RichContentText(reply.content, 14, Ink, lineHeight = 22f)
                        if (reply.media.isNotEmpty()) { Spacer(Modifier.height(12.dp)); MediaGallery(reply.media) }
                    }
                    Box(Modifier.padding(start = 44.dp).fillMaxWidth().height(1.dp).background(Hairline))
                }
            }
        }
        Column(
            Modifier.fillMaxWidth().background(Surface)
                .drawBehind { drawLine(Hairline, Offset.Zero, Offset(size.width, 0f), 1.dp.toPx()) }
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            if (error != null && detail != null) {
                KText(error.orEmpty(), 10, Carrot, FontWeight.Bold)
                Spacer(Modifier.height(7.dp))
            }
            if (replyImages.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(7.dp), contentPadding = PaddingValues(bottom = 9.dp)) {
                    items(replyImages, key = { it.uri.toString() }) { image ->
                        Box(Modifier.size(64.dp).clip(RoundedCornerShape(13.dp)).background(Hairline)) {
                            AsyncImage(image.uri, image.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                            Box(
                                Modifier.align(Alignment.TopEnd).padding(4.dp).size(20.dp)
                                    .background(Paper.copy(.92f), CircleShape)
                                    .clickable { replyImages = replyImages.filterNot { it.uri == image.uri } },
                                contentAlignment = Alignment.Center
                            ) { CustomIcon(IconType.CLOSE, Ink, 11.dp) }
                        }
                    }
                }
            }
            Row(verticalAlignment = Alignment.Bottom) {
                Box(
                    Modifier.size(44.dp).clip(RoundedCornerShape(14.dp)).background(Paper)
                        .border(1.dp, Hairline, RoundedCornerShape(14.dp))
                        .clickable(enabled = !sending && replyImages.size < 4) { imagePicker.launch(arrayOf("image/*")) },
                    contentAlignment = Alignment.Center
                ) {
                    CustomIcon(IconType.IMAGE, if (replyImages.size < 4) Ink else Muted, 18.dp)
                }
                Spacer(Modifier.width(8.dp))
                BasicTextField(
                    value = replyText,
                    onValueChange = { replyText = it.take(1000); error = null },
                    textStyle = TextStyle(Ink, 14.sp, FontWeight.Medium, lineHeight = 21.sp),
                    cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
                    modifier = Modifier.weight(1f).heightIn(min = 44.dp, max = 112.dp)
                        .clip(RoundedCornerShape(15.dp)).background(Paper).border(1.dp, Hairline, RoundedCornerShape(15.dp))
                        .padding(horizontal = 13.dp, vertical = 11.dp),
                    decorationBox = { inner ->
                        Box {
                            if (replyText.isBlank()) KText("返信を書く", 13, Muted)
                            inner()
                        }
                    }
                )
                Spacer(Modifier.width(9.dp))
                Box(
                    Modifier.height(44.dp).clip(RoundedCornerShape(14.dp))
                        .background(if ((replyText.isNotBlank() || replyImages.isNotEmpty()) && !sending) Carrot else Hairline)
                        .clickable(enabled = (replyText.isNotBlank() || replyImages.isNotEmpty()) && !sending) {
                            sending = true
                            val content = replyText.trim()
                            val pendingImages = replyImages
                            scope.launch {
                                val result = withContext(Dispatchers.IO) {
                                    runCatching {
                                        val uploads = pendingImages.map { item ->
                                            val bytes = context.contentResolver.openInputStream(item.uri)?.use { it.readBytes() }
                                                ?: throw IllegalStateException("${item.name}を読み込めませんでした")
                                            ApiUploadMedia(item.name, item.mimeType, bytes)
                                        }
                                        api.createBoardReply(board.slug, initial.id, content, uploads)
                                    }.getOrElse { ApiResult.Failure(it.message ?: "画像を読み込めませんでした") }
                                }
                                when (result) {
                                    is ApiResult.Success -> {
                                        replyText = ""
                                        replyImages = emptyList()
                                        reloadKey += 1
                                        error = null
                                    }
                                    is ApiResult.Failure -> error = result.message
                                }
                                sending = false
                            }
                        }.padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    KText(if (sending) "送信中" else "送信", 11, if ((replyText.isNotBlank() || replyImages.isNotEmpty()) && !sending) Color.White else Muted, FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun BoardThreadComposer(
    board: ApiBoard,
    onBack: () -> Unit,
    onSubmit: (String, String, (String?) -> Unit) -> Unit
) {
    var title by remember(board.slug) { mutableStateOf("") }
    var content by remember(board.slug) { mutableStateOf("") }
    var sending by remember(board.slug) { mutableStateOf(false) }
    var error by remember(board.slug) { mutableStateOf<String?>(null) }
    val enabled = title.isNotBlank() && content.isNotBlank() && !sending
    Column(Modifier.fillMaxSize().background(Paper).imePadding()) {
        OverlayHeader("新しいスレッド", onBack)
        Column(Modifier.fillMaxSize().padding(20.dp)) {
            KText(board.name, 10, Carrot, FontWeight.Black, letterSpacing = 1.3f)
            Spacer(Modifier.height(18.dp))
            BasicTextField(
                value = title,
                onValueChange = { title = it.replace("\n", "").take(100); error = null },
                singleLine = true,
                textStyle = TextStyle(Ink, 18.sp, FontWeight.Black),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(17.dp)).background(Surface)
                    .border(1.dp, Hairline, RoundedCornerShape(17.dp)).padding(16.dp),
                decorationBox = { inner -> Box { if (title.isBlank()) KText("スレッドのタイトル", 16, Muted, FontWeight.Bold); inner() } }
            )
            Spacer(Modifier.height(12.dp))
            BasicTextField(
                value = content,
                onValueChange = { content = it.take(3000); error = null },
                textStyle = TextStyle(Ink, 16.sp, FontWeight.Medium, lineHeight = 25.sp),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
                modifier = Modifier.fillMaxWidth().weight(1f).clip(RoundedCornerShape(20.dp)).background(Surface)
                    .border(1.dp, Hairline, RoundedCornerShape(20.dp)).padding(16.dp),
                decorationBox = { inner -> Box(Modifier.fillMaxSize()) { if (content.isBlank()) KText("本文を書く", 15, Muted); inner() } }
            )
            if (error != null) {
                Spacer(Modifier.height(10.dp))
                KText(error.orEmpty(), 11, Carrot, FontWeight.Bold)
            }
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                KText("${content.length} / 3000", 10, Muted, FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Box(
                    Modifier.clip(RoundedCornerShape(15.dp)).background(if (enabled) Carrot else Hairline)
                        .clickable(enabled = enabled) {
                            sending = true
                            onSubmit(title.trim(), content.trim()) { message ->
                                sending = false
                                error = message
                            }
                        }.padding(horizontal = 22.dp, vertical = 12.dp)
                ) { KText(if (sending) "作成中…" else "スレッドを作成", 11, if (enabled) Color.White else Muted, FontWeight.Black) }
            }
        }
    }
}

@Composable
private fun NotificationsScreen(api: KarotterApi, retainedState: NotificationsRetainedState, onBack: () -> Unit, onPost: (Post) -> Unit) {
    var notifications by retainedState.notifications
    var loading by retainedState.loading
    var nextPage by retainedState.nextPage
    var hasMore by retainedState.hasMore
    var error by retainedState.error
    var selectedType by retainedState.selectedType
    var requestRevision by retainedState.requestRevision
    val notificationFilters = remember {
        listOf(
            null to "すべて",
            "LIKE" to "いいね",
            "REKAROT" to "リカロート",
            "REPLY" to "返信",
            "MENTION" to "メンション",
            "FOLLOW" to "フォロー",
            "FOLLOWED_POST" to "投稿通知",
            "FOLLOW_REQUEST" to "フォロー申請",
            "REACTION" to "リアクション",
            "DM" to "DM",
            "QUOTE" to "引用",
            "BOARD_NEW_THREAD,BOARD_THREAD_REPLY" to "掲示板",
            "COMMUNITY_INVITE,COMMUNITY_JOIN,COMMUNITY_REMOVAL" to "コミュニティ",
            "REPORT_UPDATE" to "報告",
            "SYSTEM" to "お知らせ"
        )
    }
    val listState = retainedState.listState
    val scope = rememberCoroutineScope()
    fun notificationKey(item: ApiNotification) = "${item.id}:${item.type}:${item.actorName}:${item.createdAt}"
    suspend fun loadNextPage() {
        if (loading || !hasMore) return
        val requestedPage = nextPage
        val requestedType = selectedType
        val revision = requestRevision
        loading = true
        when (val result = withContext(Dispatchers.IO) {
            api.notificationPage(requestedPage, limit = 30, types = requestedType)
        }) {
            is ApiResult.Success -> {
                if (revision != requestRevision || requestedType != selectedType) return
                val rawPageItems = result.value.distinctBy(::notificationKey)
                val pageItems = rawPageItems.filterNot(ApiNotification::suppressed)
                val existing = notifications.mapTo(hashSetOf(), ::notificationKey)
                val additions = pageItems.filter { notificationKey(it) !in existing }
                notifications = (notifications + additions).distinctBy(::notificationKey)
                nextPage = requestedPage + 1
                // Notifications can be grouped by the server, so a page may contain fewer
                // items than the requested limit even when another page exists.
                hasMore = rawPageItems.isNotEmpty()
                error = null
                if (pageItems.isEmpty() && rawPageItems.isNotEmpty()) {
                    loading = false
                    loadNextPage()
                    return
                }
            }
            is ApiResult.Failure -> {
                if (revision != requestRevision || requestedType != selectedType) return
                error = result.message
                hasMore = false
            }
        }
        if (revision == requestRevision && requestedType == selectedType) loading = false
    }
    LaunchedEffect(selectedType) {
        if (retainedState.initialized.value && retainedState.loadedType.value == selectedType) {
            return@LaunchedEffect
        }
        notifications = emptyList()
        nextPage = 1
        hasMore = true
        error = null
        loading = false
        retainedState.loadedType.value = selectedType
        retainedState.initialized.value = true
        listState.scrollToItem(0)
        loadNextPage()
    }
    LaunchedEffect(Unit) {
        if (!retainedState.readMarked.value) {
            withContext(Dispatchers.IO) { api.markNotificationsRead() }
            retainedState.readMarked.value = true
        }
    }
    InfiniteLoadEffect(listState, notifications.size, hasMore, loading) {
        scope.launch { loadNextPage() }
    }
    Column(Modifier.fillMaxSize().background(Paper)) {
        OverlayHeader("通知", onBack)
        LazyRow(
            state = retainedState.filterListState,
            modifier = Modifier.fillMaxWidth().background(Paper),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            items(notificationFilters, key = { it.first ?: "ALL" }) { (type, label) ->
                val selected = selectedType == type
                Box(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selected) Strong else Surface)
                        .border(1.dp, if (selected) Strong else Hairline, RoundedCornerShape(12.dp))
                        .clickable {
                            if (selectedType != type) {
                                requestRevision += 1
                                selectedType = type
                            }
                        }
                        .padding(horizontal = 13.dp, vertical = 9.dp),
                    contentAlignment = Alignment.Center
                ) {
                    KText(label, 10, if (selected) OnStrong else Muted, FontWeight.Black)
                }
            }
        }
        Box(Modifier.fillMaxWidth().height(1.dp).background(Hairline))
        LazyColumn(state = listState, modifier = Modifier.weight(1f).fillMaxWidth(), contentPadding = PaddingValues(bottom = 30.dp)) {
            if (loading && notifications.isEmpty()) items(3) { LoadingPost() }
            if (error != null) item { ErrorText(error.orEmpty()) }
            if (!loading && error == null && notifications.isEmpty()) item { KText("新しい通知はありません", 12, Muted, modifier = Modifier.padding(22.dp)) }
            items(notifications, key = ::notificationKey) { notice ->
                val kind = notice.type.uppercase()
                val accent = when (kind) {
                    "LIKE", "REACTION" -> Color(0xFFE65772)
                    "FOLLOW", "COMMUNITY_JOIN" -> Color(0xFF3D8B73)
                    "FOLLOW_REQUEST" -> Color(0xFFE0A12A)
                    "FOLLOWED_POST", "SYSTEM" -> Color(0xFF3979C9)
                    "REPLY", "MENTION" -> Color(0xFF3979C9)
                    "REKAROT", "QUOTE" -> Carrot
                    "DM", "COMMUNITY_INVITE" -> Color(0xFF7654C6)
                    "BOARD_NEW_THREAD", "BOARD_THREAD_REPLY" -> Color(0xFF168A70)
                    "COMMUNITY_REMOVAL" -> Color(0xFFD64045)
                    else -> Muted
                }
                Row(
                    Modifier
                        .padding(horizontal = 14.dp, vertical = 5.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(19.dp))
                        .background(Surface)
                        .border(1.dp, Hairline, RoundedCornerShape(19.dp))
                        .clickable(enabled = notice.post != null) { notice.post?.toUiPost()?.let(onPost) }
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(Modifier.size(48.dp)) {
                        Box(Modifier.size(43.dp).background(PaleCarrot, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                            if (notice.actorAvatarUrl != null) AsyncImage(notice.actorAvatarUrl, notice.actorName, Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp)), contentScale = ContentScale.Crop)
                            else KText(notice.actorName.take(1).ifBlank { notice.type.take(1) }, 13, Carrot, FontWeight.Black)
                        }
                        Box(Modifier.size(21.dp).align(Alignment.BottomEnd).background(accent, CircleShape).border(2.dp, Surface, CircleShape), contentAlignment = Alignment.Center) {
                            when (kind) {
                                "LIKE", "REACTION" -> CustomIcon(IconType.HEART, Color.White, 11.dp, filled = true)
                                "FOLLOW" -> CustomIcon(IconType.PERSON, Color.White, 11.dp, filled = true)
                                "FOLLOW_REQUEST" -> CustomIcon(IconType.PERSON, Color.White, 11.dp)
                                "FOLLOWED_POST" -> CustomIcon(IconType.BELL, Color.White, 11.dp)
                                "REPLY" -> CustomIcon(IconType.REPLY, Color.White, 11.dp)
                                "MENTION" -> KText("@", 10, Color.White, FontWeight.Black)
                                "REKAROT" -> CustomIcon(IconType.REKAROT, Color.White, 12.dp)
                                "QUOTE" -> KText("“", 13, Color.White, FontWeight.Black)
                                "DM" -> CustomIcon(IconType.DM, Color.White, 11.dp)
                                "BOARD_NEW_THREAD", "BOARD_THREAD_REPLY" -> CustomIcon(IconType.BOARD, Color.White, 11.dp)
                                "COMMUNITY_INVITE", "COMMUNITY_JOIN", "COMMUNITY_REMOVAL" ->
                                    CustomIcon(IconType.COMMUNITY, Color.White, 11.dp)
                                "REPORT_UPDATE" -> CustomIcon(IconType.INFO, Color.White, 11.dp)
                                else -> CustomIcon(IconType.BELL, Color.White, 10.dp)
                            }
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            KText(notice.actorName, 13, Ink, FontWeight.Bold, maxLines = 1, modifier = Modifier.weight(1f))
                            KText(relativeTime(notice.createdAt), 9, Muted)
                        }
                        Spacer(Modifier.height(3.dp))
                        KText(notice.message.ifBlank { notificationLabel(notice.type) }, 12, Ink.copy(.78f), lineHeight = 18f)
                        notice.post?.content?.takeIf { it.isNotBlank() }?.let {
                            Spacer(Modifier.height(8.dp))
                            Box(Modifier.fillMaxWidth().background(Paper, RoundedCornerShape(10.dp)).padding(horizontal = 10.dp, vertical = 8.dp)) {
                                KText(it, 10, Muted, lineHeight = 15f, maxLines = 2)
                            }
                        }
                    }
                }
            }
            if (hasMore && notifications.isNotEmpty()) {
                item(key = "notifications-more-${notifications.size}-$nextPage") {
                    LaunchedEffect(notifications.size, nextPage) { loadNextPage() }
                    LoadingPost()
                }
            } else if (loading && notifications.isNotEmpty()) item { LoadingPost() }
        }
    }
}

private fun notificationLabel(type: String) = when (type.uppercase()) {
    "LIKE" -> "あなたの投稿にいいねしました"
    "REACTION" -> "あなたの投稿にリアクションしました"
    "REPLY" -> "あなたの投稿に返信しました"
    "FOLLOW" -> "あなたをフォローしました"
    "FOLLOW_REQUEST" -> "フォローリクエストが届きました"
    "FOLLOWED_POST" -> "通知をオンにしたユーザーが投稿しました"
    "REKAROT" -> "あなたの投稿をリカロートしました"
    "QUOTE" -> "あなたの投稿を引用しました"
    "MENTION" -> "あなたをメンションしました"
    "DM" -> "新しいメッセージが届きました"
    "BOARD_NEW_THREAD" -> "フォロー中の掲示板に新しいスレッドが作成されました"
    "BOARD_THREAD_REPLY" -> "フォロー中のスレッドに返信がありました"
    "COMMUNITY_INVITE" -> "コミュニティへの招待が届きました"
    "COMMUNITY_JOIN" -> "コミュニティに新しい参加者がいます"
    "COMMUNITY_REMOVAL" -> "コミュニティから除外されました"
    "REPORT_UPDATE" -> "報告した内容に更新があります"
    "SYSTEM" -> "Karotterからのお知らせがあります"
    else -> "新しいお知らせがあります"
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DmScreen(api: KarotterApi, currentUser: ApiUser?, onConversationState: (Boolean) -> Unit) {
    val context = LocalContext.current
    val groupNamePrefs = remember { context.getSharedPreferences("karotter_dm_group_names_v1", android.content.Context.MODE_PRIVATE) }
    var groups by remember { mutableStateOf<List<ApiDmGroup>>(emptyList()) }
    var selected by remember { mutableStateOf<ApiDmGroup?>(null) }
    var managementGroup by remember { mutableStateOf<ApiDmGroup?>(null) }
    var creating by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var reloadKey by remember { mutableIntStateOf(0) }
    fun namesApplied(value: List<ApiDmGroup>) = value.map { group ->
        if (group.name.isNotBlank()) group
        else group.copy(name = groupNamePrefs.getString("group_${group.id}", "").orEmpty())
    }
    BackHandler(enabled = (selected != null || creating) && LocalNavigationActive.current) {
        if (creating) creating = false else selected = null
    }
    LaunchedEffect(selected, creating) { onConversationState(selected != null || creating) }
    DisposableEffect(Unit) { onDispose { onConversationState(false) } }
    LaunchedEffect(reloadKey) {
        loading = true
        when (val result = withContext(Dispatchers.IO) { api.dmGroups() }) {
            is ApiResult.Success -> groups = namesApplied(result.value)
            is ApiResult.Failure -> error = result.message
        }
        loading = false
    }
    LaunchedEffect(Unit) {
        while (true) {
            delay(5_000)
            when (val result = withContext(Dispatchers.IO) { api.dmGroups() }) {
                is ApiResult.Success -> {
                    groups = namesApplied(result.value)
                    error = null
                }
                is ApiResult.Failure -> if (groups.isEmpty()) error = result.message
            }
        }
    }
    if (creating) {
        DmNewConversationScreen(api, currentUser, { creating = false }) { group ->
            creating = false
            selected = group
            reloadKey += 1
        }
        return
    }
    selected?.let { group ->
        DmConversationScreen(
            api = api,
            group = group,
            currentUser = currentUser,
            onBack = { selected = null },
            onAccepted = {
                selected = it
                reloadKey += 1
            },
            onRejected = {
                selected = null
                reloadKey += 1
            },
            onCleared = {
                groups = groups.map { item ->
                    if (item.id == group.id) item.copy(lastMessage = "", lastMessageAt = "") else item
                }
                reloadKey += 1
            },
            onLeft = {
                groups = groups.filterNot { it.id == group.id }
                selected = null
                reloadKey += 1
            }
        )
        return
    }
    managementGroup?.let { target ->
        DmManagementSheet(
            api = api,
            group = target,
            currentUserId = currentUser?.id,
            onDismiss = { managementGroup = null },
            onCleared = {
                groups = groups.map { item ->
                    if (item.id == target.id) item.copy(lastMessage = "", lastMessageAt = "") else item
                }
                managementGroup = null
                reloadKey += 1
            },
            onLeft = {
                groups = groups.filterNot { it.id == target.id }
                managementGroup = null
                reloadKey += 1
            }
        )
    }
    val dmTabTravel = with(LocalDensity.current) { 44.dp.roundToPx() }
    Column(
        Modifier
            .fillMaxSize()
            .background(Paper)
            .pointerInput(selectedTab) {
                var horizontalDistance = 0f
                detectHorizontalDragGestures(
                    onDragStart = { horizontalDistance = 0f },
                    onDragCancel = { horizontalDistance = 0f },
                    onDragEnd = {
                        when {
                            horizontalDistance < -90f && selectedTab == 0 -> selectedTab = 1
                            horizontalDistance > 90f && selectedTab == 1 -> selectedTab = 0
                        }
                        horizontalDistance = 0f
                    }
                ) { _, dragAmount -> horizontalDistance += dragAmount }
            }
    ) {
        Box {
            EditorialHeader("DIRECT", "メッセージ", "大切な会話を、静かな場所で。")
            Box(
                Modifier.align(Alignment.TopEnd).padding(end = 20.dp, top = 24.dp).size(43.dp)
                    .clip(RoundedCornerShape(15.dp)).background(Carrot).clickable { creating = true },
                contentAlignment = Alignment.Center
                ) { CustomIcon(IconType.PLUS, Color.White, 20.dp) }
        }
        Row(Modifier.fillMaxWidth().background(Surface)) {
            listOf("メッセージ", "リクエスト").forEachIndexed { index, label ->
                val indicatorWidth by animateDpAsState(
                    targetValue = if (selectedTab == index) 42.dp else 0.dp,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "dmTabIndicator"
                )
                Column(
                    Modifier.weight(1f).clickable { selectedTab = index }.padding(top = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        KText(label, 12, if (selectedTab == index) Ink else Muted, FontWeight.Bold)
                        if (index == 1) {
                            val count = groups.count { it.isRequest }
                            if (count > 0) {
                                Spacer(Modifier.width(6.dp))
                                Box(Modifier.background(Carrot, CircleShape).padding(horizontal = 6.dp, vertical = 2.dp)) {
                                    KText(count.toString(), 8, Color.White, FontWeight.Black)
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Box(Modifier.width(indicatorWidth).height(3.dp).background(if (selectedTab == index) Carrot else Color.Transparent, CircleShape))
                }
            }
        }
        AnimatedContent(
            targetState = selectedTab,
            modifier = Modifier.weight(1f),
            transitionSpec = {
                val forward = targetState > initialState
                (slideInHorizontally(tween(320, easing = FastOutSlowInEasing)) { if (forward) dmTabTravel else -dmTabTravel } + fadeIn(tween(220)))
                    .togetherWith(slideOutHorizontally(tween(260, easing = FastOutSlowInEasing)) { if (forward) -dmTabTravel else dmTabTravel } + fadeOut(tween(180)))
            },
            label = "dmTabs"
        ) { currentTab ->
            val visibleGroups = groups.filter { if (currentTab == 0) !it.isRequest else it.isRequest }
            LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 126.dp)) {
                if (loading) items(4) { LoadingPost() }
                if (error != null) item { ErrorText(error.orEmpty()) }
                if (!loading && error == null && visibleGroups.isEmpty()) {
                    item { KText(if (currentTab == 0) "まだDMはありません" else "DMリクエストはありません", 12, Muted, modifier = Modifier.padding(22.dp)) }
                }
                items(visibleGroups, key = { it.id }) { group ->
                    val others = group.members.filter { it.id != currentUser?.id }
                    val title = group.name.takeIf { it.isNotBlank() }
                        ?: others.joinToString("、") { it.displayName.ifBlank { it.username } }.ifBlank { "DMグループ" }
                    val avatar = others.firstOrNull()
                    Row(
                        Modifier.fillMaxWidth()
                            .combinedClickable(
                                onClick = { selected = group },
                                onLongClick = { managementGroup = group }
                            )
                            .padding(horizontal = 22.dp, vertical = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.size(50.dp).background(PaleCarrot, RoundedCornerShape(17.dp)), contentAlignment = Alignment.Center) {
                            if (avatar?.avatarUrl != null) AsyncImage(avatar.avatarUrl, title, Modifier.fillMaxSize().clip(RoundedCornerShape(17.dp)), contentScale = ContentScale.Crop) else KText(title.take(1), 17, Ink, FontWeight.Black)
                        }
                        Spacer(Modifier.width(13.dp))
                        Column(Modifier.weight(1f)) {
                            KText(title, 14, Ink, FontWeight.Bold, maxLines = 1)
                            Spacer(Modifier.height(4.dp))
                            KText(group.lastMessage.ifBlank { if (group.isRequest) "メッセージリクエスト" else "会話を始めましょう" }, 11, Muted, maxLines = 1)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            if (group.lastMessageAt.isNotBlank()) KText(relativeTime(group.lastMessageAt), 9, Muted, maxLines = 1)
                            if (group.unreadCount > 0) {
                                Spacer(Modifier.height(6.dp))
                                Box(
                                    Modifier.background(Carrot, CircleShape)
                                        .padding(horizontal = 7.dp, vertical = 3.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    KText(if (group.unreadCount > 99) "99+" else group.unreadCount.toString(), 8, Color.White, FontWeight.Black)
                                }
                            }
                        }
                    }
                    Box(Modifier.padding(start = 85.dp).fillMaxWidth().height(1.dp).background(Hairline))
                }
            }
        }
    }
}

@Composable
private fun DmManagementSheet(
    api: KarotterApi,
    group: ApiDmGroup,
    currentUserId: Long?,
    onDismiss: () -> Unit,
    onCleared: () -> Unit,
    onLeft: () -> Unit
) {
    var showMembers by remember(group.id) { mutableStateOf(false) }
    var pendingAction by remember(group.id) { mutableStateOf<String?>(null) }
    var busy by remember(group.id) { mutableStateOf(false) }
    var error by remember(group.id) { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val isGroup = group.members.size > 2 || group.name.isNotBlank()
    val others = group.members.filter { it.id != currentUserId }
    val title = group.name.takeIf(String::isNotBlank)
        ?: others.joinToString("、") { it.displayName.ifBlank { it.username } }.ifBlank { "DM" }
    val systemNavigationClearance =
        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 36.dp

    Dialog(
        onDismissRequest = { if (!busy) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Box(Modifier.fillMaxSize().background(Color.Black.copy(.34f)).clickable(enabled = !busy) { onDismiss() })
            Column(
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 27.dp, topEnd = 27.dp))
                    .background(Surface)
                    .border(1.dp, Hairline, RoundedCornerShape(topStart = 27.dp, topEnd = 27.dp))
                    .clickable { }
                    .padding(
                        start = 18.dp,
                        top = 10.dp,
                        end = 18.dp,
                        bottom = systemNavigationClearance
                    )
            ) {
                Box(
                    Modifier.width(38.dp).height(4.dp).background(Hairline, RoundedCornerShape(3.dp))
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(13.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (showMembers) {
                        Box(
                            Modifier.size(36.dp).clip(CircleShape).clickable { showMembers = false },
                            contentAlignment = Alignment.Center
                        ) { CustomIcon(IconType.BACK, Ink, 18.dp) }
                        Spacer(Modifier.width(7.dp))
                    }
                    Column(Modifier.weight(1f)) {
                        KText(if (showMembers) "メンバー" else title, 16, Ink, FontWeight.Black, maxLines = 1)
                        KText(
                            if (showMembers) "${group.members.size}人"
                            else if (isGroup) "${group.members.size}人のグループ" else "会話の管理",
                            9,
                            Muted,
                            FontWeight.Bold
                        )
                    }
                    Box(
                        Modifier.size(36.dp).clip(CircleShape).clickable(enabled = !busy) { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) { CustomIcon(IconType.CLOSE, Ink, 17.dp) }
                }
                Spacer(Modifier.height(15.dp))
                if (showMembers) {
                    LazyColumn(
                        Modifier.fillMaxWidth().heightIn(max = 430.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        items(group.members, key = { it.id }) { member ->
                            Row(
                                Modifier.fillMaxWidth().padding(horizontal = 5.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    Modifier.size(43.dp).background(PaleCarrot, RoundedCornerShape(14.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (member.avatarUrl != null) {
                                        AsyncImage(
                                            member.avatarUrl,
                                            member.displayName,
                                            Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                    } else KText(member.displayName.ifBlank { member.username }.take(1), 14, Ink, FontWeight.Black)
                                }
                                Spacer(Modifier.width(11.dp))
                                Column(Modifier.weight(1f)) {
                                    KText(member.displayName.ifBlank { member.username }, 13, Ink, FontWeight.Bold, maxLines = 1)
                                    KText("@${member.username}", 10, Muted, maxLines = 1)
                                }
                                if (member.id == currentUserId) KText("自分", 9, Carrot, FontWeight.Black)
                            }
                        }
                    }
                } else {
                    @Composable
                    fun actionRow(
                        icon: IconType,
                        label: String,
                        description: String,
                        destructive: Boolean = false,
                        action: () -> Unit
                    ) {
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                                .clickable(enabled = !busy, onClick = action)
                                .padding(horizontal = 8.dp, vertical = 11.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                Modifier.size(41.dp).background(if (destructive) PaleCarrot else Paper, RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                CustomIcon(icon, if (destructive) Color(0xFFD64045) else Ink, 19.dp)
                            }
                            Spacer(Modifier.width(11.dp))
                            Column(Modifier.weight(1f)) {
                                KText(label, 13, if (destructive) Color(0xFFD64045) else Ink, FontWeight.Bold)
                                Spacer(Modifier.height(2.dp))
                                KText(description, 9, Muted)
                            }
                            CustomIcon(IconType.FORWARD, Muted, 15.dp)
                        }
                    }
                    if (isGroup) {
                        actionRow(IconType.PERSON, "メンバーを表示", "参加しているアカウントを確認") {
                            showMembers = true
                        }
                    }
                    actionRow(IconType.TRASH, "履歴を削除", "自分の画面からこの会話履歴を消去", destructive = true) {
                        pendingAction = "clear"
                    }
                    if (isGroup) {
                        actionRow(IconType.LOGOUT, "グループを退出", "このグループから退出", destructive = true) {
                            pendingAction = "leave"
                        }
                    }
                    error?.let {
                        Spacer(Modifier.height(8.dp))
                        ErrorText(it)
                    }
                }
            }
        }
    }
    pendingAction?.let { action ->
        Dialog(onDismissRequest = { if (!busy) pendingAction = null }) {
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Surface)
                    .border(1.dp, Hairline, RoundedCornerShape(22.dp)).padding(21.dp)
            ) {
                KText(if (action == "leave") "グループを退出しますか？" else "履歴を削除しますか？", 17, Ink, FontWeight.Black)
                Spacer(Modifier.height(7.dp))
                KText(
                    if (action == "leave") "退出すると、このグループでメッセージを送受信できなくなります。"
                    else "履歴は自分の画面から削除されます。",
                    12,
                    Muted,
                    lineHeight = 18f
                )
                Spacer(Modifier.height(18.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    Box(
                        Modifier.weight(1f).clip(RoundedCornerShape(13.dp))
                            .border(1.dp, Hairline, RoundedCornerShape(13.dp))
                            .clickable(enabled = !busy) { pendingAction = null }
                            .padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) { KText("キャンセル", 11, Ink, FontWeight.Bold) }
                    Box(
                        Modifier.weight(1f).clip(RoundedCornerShape(13.dp)).background(Color(0xFFD64045))
                            .clickable(enabled = !busy) {
                                busy = true
                                error = null
                                scope.launch {
                                    val result = withContext(Dispatchers.IO) {
                                        if (action == "leave") api.leaveDmGroup(group.id) else api.clearDmHistory(group.id)
                                    }
                                    when (result) {
                                        is ApiResult.Success -> {
                                            pendingAction = null
                                            if (action == "leave") onLeft() else onCleared()
                                        }
                                        is ApiResult.Failure -> {
                                            error = result.message
                                            busy = false
                                            pendingAction = null
                                        }
                                    }
                                }
                            }.padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        KText(
                            if (busy) "処理中…" else if (action == "leave") "退出" else "削除",
                            11,
                            Color.White,
                            FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DmConversationScreen(
    api: KarotterApi,
    group: ApiDmGroup,
    currentUser: ApiUser?,
    onBack: () -> Unit,
    onAccepted: (ApiDmGroup) -> Unit = {},
    onRejected: () -> Unit = {},
    onCleared: () -> Unit = {},
    onLeft: () -> Unit = {}
) {
    val context = LocalContext.current
    var messages by remember(group.id) { mutableStateOf<List<ApiDmMessage>>(emptyList()) }
    var text by remember(group.id) { mutableStateOf("") }
    var selectedImages by remember(group.id) { mutableStateOf<List<ComposerMedia>>(emptyList()) }
    var loading by remember(group.id) { mutableStateOf(true) }
    var sending by remember(group.id) { mutableStateOf(false) }
    var requestBusy by remember(group.id) { mutableStateOf(false) }
    var managementOpen by remember(group.id) { mutableStateOf(false) }
    var error by remember(group.id) { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
        val picked = uris.take(4).map { uri ->
            val name = context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) cursor.getString(0) else null
            } ?: "dm-image-${System.currentTimeMillis()}.jpg"
            ComposerMedia(uri, name, context.contentResolver.getType(uri).orEmpty().ifBlank { "image/jpeg" })
        }
        selectedImages = (selectedImages + picked).distinctBy { it.uri }.take(4)
    }
    val others = group.members.filter { it.id != currentUser?.id }
    val title = group.name.takeIf { it.isNotBlank() }
        ?: others.joinToString("、") { it.displayName.ifBlank { it.username } }.ifBlank { "DM" }
    if (managementOpen) {
        DmManagementSheet(
            api = api,
            group = group,
            currentUserId = currentUser?.id,
            onDismiss = { managementOpen = false },
            onCleared = {
                messages = emptyList()
                managementOpen = false
                onCleared()
            },
            onLeft = {
                managementOpen = false
                onLeft()
            }
        )
    }
    LaunchedEffect(group.id) {
        var firstLoad = true
        while (true) {
            val previousLastId = messages.lastOrNull()?.id
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val wasNearBottom = firstLoad || lastVisibleIndex >= messages.lastIndex - 1
            when (val result = withContext(Dispatchers.IO) { api.dmMessages(group.id) }) {
                is ApiResult.Success -> {
                    val updated = result.value.sortedBy { it.id }.distinctBy { it.id }
                    val hasNewMessage = updated.lastOrNull()?.id != previousLastId
                    messages = updated
                    error = null
                    if (updated.isNotEmpty() && (firstLoad || (hasNewMessage && wasNearBottom))) {
                        if (firstLoad) listState.scrollToItem(updated.lastIndex)
                        else listState.animateScrollToItem(updated.lastIndex)
                    }
                    if (firstLoad || hasNewMessage) withContext(Dispatchers.IO) { api.markDmRead(group.id) }
                }
                is ApiResult.Failure -> if (messages.isEmpty()) error = result.message
            }
            loading = false
            firstLoad = false
            delay(5_000)
        }
    }
    Column(
        Modifier.fillMaxSize()
            .background(Paper)
            .navigationBarsPadding()
            .imePadding()
    ) {
        Row(
            Modifier.fillMaxWidth().height(62.dp).background(Surface)
                .drawBehind { drawLine(Hairline, Offset(0f, size.height), Offset(size.width, size.height), 1.dp.toPx()) }
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(40.dp).clip(CircleShape).clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                CustomIcon(IconType.BACK, Ink, 20.dp)
            }
            Spacer(Modifier.width(4.dp))
            Box(
                Modifier.size(40.dp).background(PaleCarrot, RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) {
                val avatarUser = others.firstOrNull()
                if (avatarUser?.avatarUrl != null) {
                    AsyncImage(
                        avatarUser.avatarUrl,
                        avatarUser.displayName,
                        Modifier.fillMaxSize().clip(RoundedCornerShape(15.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    KText(title.take(1), 15, Ink, FontWeight.Black)
                }
            }
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) {
                KText(title, 15, Ink, FontWeight.Black, maxLines = 1)
                KText(
                    if (group.members.size > 2) "${group.members.size}人のグループ" else "ダイレクトメッセージ",
                    9,
                    Muted,
                    FontWeight.Medium,
                    maxLines = 1
                )
            }
            Box(
                Modifier.size(38.dp).clip(RoundedCornerShape(13.dp)).background(Paper)
                    .clickable { managementOpen = true },
                contentAlignment = Alignment.Center
            ) {
                CustomIcon(IconType.MORE, Muted, 18.dp)
            }
        }
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 13.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            if (loading) items(4) { LoadingPost() }
            if (error != null) item { ErrorText(error.orEmpty()) }
            items(messages, key = { it.id }) { message ->
                val mine = message.senderId == currentUser?.id
                val senderName = message.sender?.displayName?.ifBlank { message.sender.username } ?: "ユーザー"
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = if (mine) Arrangement.End else Arrangement.Start,
                    verticalAlignment = Alignment.Bottom
                ) {
                    if (!mine) {
                        Box(
                            Modifier.align(Alignment.Top).size(34.dp)
                                .background(PaleCarrot, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (message.sender?.avatarUrl != null) {
                                AsyncImage(
                                    message.sender.avatarUrl,
                                    senderName,
                                    Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                KText(senderName.take(1), 12, Ink, FontWeight.Black)
                            }
                        }
                        Spacer(Modifier.width(8.dp))
                    }
                    if (mine) {
                        KText(
                            dmClockTime(message.createdAt),
                            8,
                            Muted,
                            FontWeight.Medium,
                            modifier = Modifier.padding(end = 6.dp, bottom = 3.dp)
                        )
                    }
                    Column(
                        Modifier.widthIn(
                            min = if (message.media.isNotEmpty()) 176.dp else 44.dp,
                            max = 286.dp
                        )
                    ) {
                        if (!mine && group.members.size > 2) {
                            KText(senderName, 9, Muted, FontWeight.Bold, maxLines = 1, modifier = Modifier.padding(start = 5.dp, bottom = 4.dp))
                        }
                        Column(
                            Modifier.background(
                                    if (mine) Carrot else Surface,
                                    if (mine) RoundedCornerShape(18.dp, 6.dp, 18.dp, 18.dp)
                                    else RoundedCornerShape(6.dp, 18.dp, 18.dp, 18.dp)
                                )
                                .then(
                                    if (mine) Modifier
                                    else Modifier.border(1.dp, Hairline, RoundedCornerShape(6.dp, 18.dp, 18.dp, 18.dp))
                                )
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            if (message.content.isNotBlank()) {
                                KText(message.content, 14, if (mine) Color.White else Ink, lineHeight = 20f)
                            }
                            if (message.media.isNotEmpty()) {
                                if (message.content.isNotBlank()) Spacer(Modifier.height(8.dp))
                                MediaGallery(message.media)
                            }
                        }
                    }
                    if (!mine) {
                        KText(
                            dmClockTime(message.createdAt),
                            8,
                            Muted,
                            FontWeight.Medium,
                            modifier = Modifier.padding(start = 6.dp, bottom = 3.dp)
                        )
                    }
                }
            }
        }
        if (group.isRequest) {
            Row(
                Modifier.fillMaxWidth().background(Surface)
                    .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    Modifier.weight(1f).clip(RoundedCornerShape(15.dp)).border(1.dp, Hairline, RoundedCornerShape(15.dp))
                        .clickable(enabled = !requestBusy) {
                            requestBusy = true
                            scope.launch {
                                when (val result = withContext(Dispatchers.IO) { api.rejectDmRequest(group.id) }) {
                                    is ApiResult.Success -> onRejected()
                                    is ApiResult.Failure -> error = result.message
                                }
                                requestBusy = false
                            }
                        }.padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) { KText("拒否", 11, Ink, FontWeight.Black) }
                Box(
                    Modifier.weight(1f).clip(RoundedCornerShape(15.dp)).background(Carrot)
                        .clickable(enabled = !requestBusy) {
                            requestBusy = true
                            scope.launch {
                                when (val result = withContext(Dispatchers.IO) { api.acceptDmRequest(group.id) }) {
                                    is ApiResult.Success -> onAccepted(group.copy(isRequest = false, requestStatus = "ACCEPTED", canSend = true))
                                    is ApiResult.Failure -> error = result.message
                                }
                                requestBusy = false
                            }
                        }.padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) { KText(if (requestBusy) "処理中…" else "承認", 11, Color.White, FontWeight.Black) }
            }
        } else Column(
            Modifier.fillMaxWidth().background(Surface)
                .drawBehind { drawLine(Hairline, Offset.Zero, Offset(size.width, 0f), 1.dp.toPx()) }
                .padding(start = 12.dp, end = 12.dp, top = 9.dp, bottom = 10.dp)
        ) {
            if (selectedImages.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 9.dp)) {
                    items(selectedImages, key = { it.uri.toString() }) { image ->
                        Box(Modifier.size(70.dp).clip(RoundedCornerShape(15.dp)).background(Hairline)) {
                            AsyncImage(image.uri, image.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                            Box(
                                Modifier.align(Alignment.TopEnd).padding(4.dp).size(21.dp).background(Strong.copy(.84f), CircleShape)
                                    .clickable { selectedImages = selectedImages.filterNot { it.uri == image.uri } },
                                contentAlignment = Alignment.Center
                            ) { CustomIcon(IconType.CLOSE, OnStrong, 11.dp) }
                        }
                    }
                }
            }
            if (!group.canSend && group.sendDisabledReason.isNotBlank()) {
                KText(group.sendDisabledReason, 9, Color(0xFFD64045), FontWeight.Bold, modifier = Modifier.padding(start = 47.dp, bottom = 6.dp))
            }
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom) {
                Box(
                    Modifier.size(42.dp).clip(CircleShape)
                        .clickable(enabled = !sending && selectedImages.size < 4) {
                            imagePicker.launch(arrayOf("image/*"))
                        },
                    contentAlignment = Alignment.Center
                ) { CustomIcon(IconType.IMAGE, if (selectedImages.size < 4) Carrot else Muted, 21.dp) }
                Spacer(Modifier.width(4.dp))
                BasicTextField(
                    value = text,
                    onValueChange = { text = it.take(2000) },
                    textStyle = TextStyle(Ink, 14.sp, lineHeight = 20.sp),
                    cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
                    modifier = Modifier.weight(1f).heightIn(min = 42.dp, max = 112.dp)
                        .background(Paper, RoundedCornerShape(21.dp))
                        .border(1.dp, Hairline, RoundedCornerShape(21.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    decorationBox = { inner ->
                        Box {
                            if (text.isBlank()) KText("メッセージを入力", 13, Muted)
                            inner()
                        }
                    }
                )
                Spacer(Modifier.width(7.dp))
                val canSendMessage = (text.isNotBlank() || selectedImages.isNotEmpty()) && !sending && group.canSend
                Box(
                    Modifier.size(42.dp).background(if (canSendMessage) Carrot else Paper, CircleShape)
                        .border(1.dp, if (canSendMessage) Carrot else Hairline, CircleShape)
                        .clickable(enabled = canSendMessage) {
                            val body = text.trim()
                            val pendingImages = selectedImages
                            sending = true
                            scope.launch {
                                val result = withContext(Dispatchers.IO) {
                                    runCatching {
                                        val uploads = pendingImages.map { item ->
                                            val bytes = context.contentResolver.openInputStream(item.uri)?.use { it.readBytes() }
                                                ?: throw IllegalStateException("${item.name}を読み込めませんでした")
                                            ApiUploadMedia(item.name, item.mimeType, bytes)
                                        }
                                        api.sendDm(group.id, body, uploads)
                                    }.getOrElse { ApiResult.Failure(it.message ?: "画像を読み込めませんでした") }
                                }
                                when (result) {
                                    is ApiResult.Success -> {
                                        messages = (messages + result.value).distinctBy { it.id }
                                        text = ""
                                        selectedImages = emptyList()
                                        listState.animateScrollToItem(messages.lastIndex)
                                    }
                                    is ApiResult.Failure -> error = result.message
                                }
                                sending = false
                            }
                        },
                    contentAlignment = Alignment.Center
                ) { CustomIcon(IconType.SEND, if (canSendMessage) Color.White else Muted, 19.dp) }
            }
        }
    }
}

@Composable
private fun DmNewConversationScreen(
    api: KarotterApi,
    currentUser: ApiUser?,
    onBack: () -> Unit,
    onStarted: (ApiDmGroup) -> Unit
) {
    val context = LocalContext.current
    val groupNamePrefs = remember { context.getSharedPreferences("karotter_dm_group_names_v1", android.content.Context.MODE_PRIVATE) }
    var groupMode by remember { mutableStateOf(false) }
    var groupName by remember { mutableStateOf("") }
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<ApiUser>>(emptyList()) }
    var selectedUsers by remember { mutableStateOf<List<ApiUser>>(emptyList()) }
    var searching by remember { mutableStateOf(false) }
    var startingUserId by remember { mutableStateOf<Long?>(null) }
    var creatingGroup by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    fun search() {
        val term = query.trim()
        if (term.isBlank() || searching) return
        searching = true
        error = null
        scope.launch {
            when (val result = withContext(Dispatchers.IO) { api.searchUsers(term, 1) }) {
                is ApiResult.Success -> results = result.value.users.filter { it.id != currentUser?.id }
                is ApiResult.Failure -> error = result.message
            }
            searching = false
        }
    }
    Column(Modifier.fillMaxSize().background(Paper)) {
        OverlayHeader(if (groupMode) "新しいグループ" else "新しいメッセージ", onBack)
        Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp).background(Surface, RoundedCornerShape(16.dp)).padding(4.dp)) {
            listOf("1対1", "グループ").forEachIndexed { index, label ->
                val selected = (index == 1) == groupMode
                Box(
                    Modifier.weight(1f).clip(RoundedCornerShape(12.dp))
                        .background(if (selected) Paper else Color.Transparent)
                        .clickable(enabled = !creatingGroup && startingUserId == null) {
                            groupMode = index == 1
                            error = null
                        }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) { KText(label, 11, if (selected) Ink else Muted, FontWeight.Black) }
            }
        }
        if (groupMode) {
            BasicTextField(
                value = groupName,
                onValueChange = { groupName = it.take(60); error = null },
                singleLine = true,
                textStyle = TextStyle(Ink, 14.sp, FontWeight.Bold),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(17.dp)).background(Surface)
                    .border(1.dp, Hairline, RoundedCornerShape(17.dp)).padding(horizontal = 14.dp, vertical = 14.dp),
                decorationBox = { inner ->
                    Box {
                        if (groupName.isBlank()) KText("グループ名", 13, Muted)
                        inner()
                    }
                }
            )
            if (selectedUsers.isNotEmpty()) {
                LazyRow(
                    Modifier.fillMaxWidth().padding(top = 10.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedUsers, key = { it.id }) { user ->
                        Row(
                            Modifier.clip(RoundedCornerShape(13.dp)).background(PaleCarrot)
                                .clickable { selectedUsers = selectedUsers.filterNot { it.id == user.id } }
                                .padding(horizontal = 9.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            KText(user.displayName.ifBlank { "@${user.username}" }, 10, Ink, FontWeight.Bold, maxLines = 1)
                            Spacer(Modifier.width(6.dp))
                            CustomIcon(IconType.CLOSE, Carrot, 12.dp)
                        }
                    }
                }
            }
            val canCreate = groupName.isNotBlank() && selectedUsers.size >= 2 && !creatingGroup
            Box(
                Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (canCreate) Carrot else Hairline)
                    .clickable(enabled = canCreate) {
                        creatingGroup = true
                        error = null
                        scope.launch {
                            when (val result = withContext(Dispatchers.IO) {
                                api.createDmGroup(groupName, selectedUsers.map { it.id })
                            }) {
                                is ApiResult.Success -> {
                                    val namedGroup = result.value.copy(name = result.value.name.ifBlank { groupName.trim() })
                                    groupNamePrefs.edit().putString("group_${namedGroup.id}", namedGroup.name).apply()
                                    onStarted(namedGroup)
                                }
                                is ApiResult.Failure -> {
                                    error = result.message
                                    creatingGroup = false
                                }
                            }
                        }
                    }.padding(vertical = 13.dp),
                contentAlignment = Alignment.Center
            ) {
                KText(
                    if (creatingGroup) "作成中…"
                    else if (selectedUsers.size < 2) "メンバーを2人以上選択してください"
                    else "「${groupName.ifBlank { "グループ" }}」を作成",
                    11,
                    if (canCreate) Color.White else Muted,
                    FontWeight.Black,
                    maxLines = 1
                )
            }
        }
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp)
                .clip(RoundedCornerShape(17.dp)).background(Surface).border(1.dp, Hairline, RoundedCornerShape(17.dp))
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomIcon(IconType.SEARCH, Muted, 17.dp)
            Spacer(Modifier.width(10.dp))
            BasicTextField(
                value = query,
                onValueChange = { query = it; error = null },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { search() }),
                textStyle = TextStyle(Ink, 14.sp, FontWeight.Medium),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
                modifier = Modifier.weight(1f).padding(vertical = 14.dp),
                decorationBox = { inner -> if (query.isBlank()) KText("ユーザー名または名前", 13, Muted) else inner() }
            )
            KText("検索", 10, if (query.isBlank()) Muted else Carrot, FontWeight.Black, modifier = Modifier.clickable(enabled = query.isNotBlank()) { search() }.padding(7.dp))
        }
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding() + 24.dp)) {
            if (searching) items(3) { LoadingPost() }
            if (error != null) item { ErrorText(error.orEmpty()) }
            if (!searching && query.isNotBlank() && error == null && results.isEmpty()) {
                item { KText("一致するユーザーが見つかりません", 12, Muted, modifier = Modifier.padding(22.dp)) }
            }
            items(results, key = { it.id }) { user ->
                val userSelected = selectedUsers.any { it.id == user.id }
                Row(
                    Modifier.fillMaxWidth().clickable(enabled = startingUserId == null && !creatingGroup && user.canReceiveDm) {
                        if (groupMode) {
                            selectedUsers = if (userSelected) selectedUsers.filterNot { it.id == user.id }
                            else (selectedUsers + user).distinctBy { it.id }
                        } else {
                            startingUserId = user.id
                            error = null
                            scope.launch {
                                when (val result = withContext(Dispatchers.IO) { api.startDm(user.id) }) {
                                    is ApiResult.Success -> onStarted(result.value)
                                    is ApiResult.Failure -> {
                                        error = result.message
                                        startingUserId = null
                                    }
                                }
                            }
                        }
                    }.padding(horizontal = 22.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(46.dp).clip(RoundedCornerShape(15.dp)).background(PaleCarrot), contentAlignment = Alignment.Center) {
                        if (user.avatarUrl != null) AsyncImage(user.avatarUrl, user.displayName, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                        else KText(user.displayName.ifBlank { user.username }.take(1), 16, Ink, FontWeight.Black)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            KText(user.displayName.ifBlank { user.username }, 14, Ink, FontWeight.Bold, maxLines = 1)
                            AccountMarks(user.officialMarks.ifEmpty { listOf(user.officialMark) }, user.isBotAccount, user.isParodyAccount, user.isPrivate, compact = true)
                        }
                        KText("@${user.username}", 10, Muted)
                    }
                    KText(
                        when {
                            !user.canReceiveDm -> "DM拒否"
                            groupMode && userSelected -> "選択済み"
                            groupMode -> "追加"
                            startingUserId == user.id -> "開始中…"
                            else -> "メッセージ"
                        },
                        9,
                        if (user.canReceiveDm && (!groupMode || !userSelected)) Carrot else Muted,
                        FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun InfiniteLoadEffect(state: LazyListState, itemCount: Int, hasNext: Boolean, loading: Boolean, onLoadMore: () -> Unit) {
    LaunchedEffect(state, itemCount, hasNext, loading) {
        if (!hasNext || loading || itemCount == 0) return@LaunchedEffect
        snapshotFlow {
            val info = state.layoutInfo
            val lastVisible = info.visibleItemsInfo.lastOrNull()?.index ?: -1
            info.totalItemsCount > 0 && lastVisible >= info.totalItemsCount - 4
        }.distinctUntilChanged().filter { it }.collect { onLoadMore() }
    }
}

@OptIn(coil.annotation.ExperimentalCoilApi::class)
@Composable
private fun MyPageScreen(user: ApiUser?, api: KarotterApi, themeKey: String, followsSystemTheme: Boolean, onThemeChange: (String) -> Unit, onLogout: () -> Unit, onAccountChanged: (ApiUser) -> Unit, onAddAccount: () -> Unit, onNotifications: () -> Unit, onPost: (Post) -> Unit, onReply: (Post) -> Unit, onQuote: (Post) -> Unit, onRekarot: (Post, Boolean) -> Unit, onLike: (Post, Boolean) -> Unit, onBookmark: (Post, Boolean) -> Unit, onCompose: () -> Unit, onAnswerQuestion: (ApiQuestion) -> Unit, latestCreatedPost: Post?, onUser: (ApiUser) -> Unit) {
    if (user == null) { LoadingPost(); return }
    var page by remember { mutableStateOf("root") }
    var detailedProfileUser by remember(user.id) { mutableStateOf(user) }
    var profileReloadKey by remember(user.id) { mutableIntStateOf(0) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var cacheMessage by remember { mutableStateOf<String?>(null) }
    var confirmCacheClear by remember { mutableStateOf(false) }
    var confirmLogout by remember { mutableStateOf(false) }
    var switchingAccount by remember { mutableStateOf<String?>(null) }
    var accountError by remember { mutableStateOf<String?>(null) }
    val darkMode = themeKey.endsWith("-dark")
    val themeFamily = themeKey.removeSuffix("-dark")
    LaunchedEffect(page, user.id, user.username, profileReloadKey) {
        if (page != "profile" && page != "profileEdit") return@LaunchedEffect
        when (val result = withContext(Dispatchers.IO) { api.user(user.username) }) {
            is ApiResult.Success -> detailedProfileUser = result.value
            is ApiResult.Failure -> Unit
        }
    }
    BackHandler(enabled = page != "root" && LocalNavigationActive.current) { page = "root" }
    if (confirmCacheClear) {
        Dialog(onDismissRequest = { confirmCacheClear = false }) {
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(Surface).border(1.dp, Hairline, RoundedCornerShape(24.dp)).padding(22.dp)) {
                KText("キャッシュを削除しますか？", 18, Ink, FontWeight.Black)
                Spacer(Modifier.height(8.dp))
                KText("保存済みの画像を消去します。投稿やログイン情報は削除されません。", 12, Muted, lineHeight = 19f)
                Spacer(Modifier.height(20.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(Modifier.weight(1f).clip(RoundedCornerShape(14.dp)).border(1.dp, Hairline, RoundedCornerShape(14.dp)).clickable { confirmCacheClear = false }.padding(vertical = 12.dp), contentAlignment = Alignment.Center) { KText("キャンセル", 12, Ink, FontWeight.Bold) }
                    Box(Modifier.weight(1f).clip(RoundedCornerShape(14.dp)).background(Carrot).clickable {
                        confirmCacheClear = false
                        scope.launch {
                            withContext(Dispatchers.IO) { context.imageLoader.memoryCache?.clear(); context.imageLoader.diskCache?.clear() }
                            cacheMessage = "キャッシュを削除しました"
                        }
                    }.padding(vertical = 12.dp), contentAlignment = Alignment.Center) { KText("削除", 12, Color.White, FontWeight.Black) }
                }
            }
        }
    }
    if (confirmLogout) {
        Dialog(onDismissRequest = { confirmLogout = false }) {
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(Surface).border(1.dp, Hairline, RoundedCornerShape(24.dp)).padding(22.dp)) {
                KText("ログアウトしますか？", 18, Ink, FontWeight.Black)
                Spacer(Modifier.height(8.dp))
                KText("この端末のセッションを終了し、ログイン画面へ戻ります。", 12, Muted, lineHeight = 19f)
                Spacer(Modifier.height(20.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(Modifier.weight(1f).clip(RoundedCornerShape(14.dp)).border(1.dp, Hairline, RoundedCornerShape(14.dp)).clickable { confirmLogout = false }.padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                        KText("キャンセル", 12, Ink, FontWeight.Bold)
                    }
                    Box(Modifier.weight(1f).clip(RoundedCornerShape(14.dp)).background(Carrot).clickable {
                        confirmLogout = false
                        onLogout()
                    }.padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                        KText("ログアウト", 12, Color.White, FontWeight.Black)
                    }
                }
            }
        }
    }
    when (page) {
        "profile" -> {
            MyPageTransition("profile", true) {
                SharedProfilePage(
                    detailedProfileUser,
                    true,
                    api,
                    { page = "root" },
                    null,
                    onPost,
                    onReply,
                    onQuote,
                    onRekarot,
                    onLike,
                    onBookmark,
                    onCompose,
                    onUser,
                    newOwnPost = latestCreatedPost,
                    onProfileReload = { profileReloadKey += 1 },
                    onEditProfile = { page = "profileEdit" }
                )
            }
            return
        }
        "profileEdit" -> {
            MyPageTransition("profileEdit", true) {
                ProfileEditScreen(
                    user = detailedProfileUser,
                    api = api,
                    onBack = { page = "root" },
                    onSaved = { updated ->
                        detailedProfileUser = updated
                        onAccountChanged(updated)
                        page = "root"
                    }
                )
            }
            return
        }
        "ranking" -> {
            MyPageTransition("ranking", true) { LevelRankingScreen(api, { page = "root" }, onUser) }
            return
        }
        "followRequests" -> {
            MyPageTransition("followRequests", true) {
                FollowRequestsScreen(api, { page = "root" }, onUser)
            }
            return
        }
        "proSettings" -> {
            MyPageTransition("proSettings", true) {
                ProSettingsScreen(
                    user = detailedProfileUser,
                    api = api,
                    onBack = { page = "root" },
                    onSaved = { preferences ->
                        detailedProfileUser = detailedProfileUser.copy(
                            premiumBadgeColor = preferences.premiumBadgeColor,
                            profileAccentColor = preferences.profileAccentColor,
                            cardAccentColor = preferences.cardAccentColor
                        )
                        onAccountChanged(detailedProfileUser)
                        page = "root"
                    }
                )
            }
            return
        }
        "bookmarks" -> {
            MyPageTransition("bookmarks", true) {
                BookmarksScreen(user, api, { page = "root" }, onPost, onReply, onQuote, onRekarot, onLike, onBookmark, onUser)
            }
            return
        }
        "scheduledPosts" -> {
            MyPageTransition("scheduledPosts", true) {
                ScheduledPostsScreen(api = api, onBack = { page = "root" })
            }
            return
        }
        "questions" -> {
            MyPageTransition("questions", true) {
                QuestionsInboxScreen(
                    api = api,
                    onBack = { page = "root" },
                    onPost = onPost,
                    onUser = onUser,
                    onAnswer = onAnswerQuestion
                )
            }
            return
        }
        "appearance" -> {
            MyPageTransition("appearance", true) {
            Column(Modifier.fillMaxSize().background(Paper)) {
                OverlayHeader("外観テーマ", { page = "root" })
                LazyColumn(contentPadding = PaddingValues(horizontal = 22.dp, vertical = 22.dp)) {
                    item {
                        KText("3つの個性を、ライトとダークで。", 13, Muted, lineHeight = 20f)
                        Spacer(Modifier.height(16.dp))
                        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Surface).border(1.dp, Hairline, RoundedCornerShape(16.dp)).padding(4.dp)) {
                            listOf(
                                Triple("端末", "system", followsSystemTheme),
                                Triple("ライト", "light", !followsSystemTheme && !darkMode),
                                Triple("ダーク", "dark", !followsSystemTheme && darkMode)
                            ).forEach { (label, mode, selected) ->
                                Box(Modifier.weight(1f).clip(RoundedCornerShape(12.dp)).background(if (selected) Strong else Color.Transparent).clickable {
                                    onThemeChange(
                                        when (mode) {
                                            "system" -> "system:$themeFamily"
                                            "dark" -> "$themeFamily-dark"
                                            else -> themeFamily
                                        }
                                    )
                                }.padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                                    KText(label, 12, if (selected) OnStrong else Muted, FontWeight.Bold)
                                }
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                    }
                    items(listOf(Triple("paper", "Paper", "やわらかな白とキャロット"), Triple("snow", "Snow", "純白とクリアなブルー"), Triple("mist", "Mist", "静かな霧色とグリーン"))) { option ->
                        val selectedKey = option.first + if (darkMode) "-dark" else ""
                        val palette = ThemePalettes.getValue(selectedKey)
                        Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(Surface).border(2.dp, if (themeFamily == option.first) palette.accent else Hairline, RoundedCornerShape(20.dp)).clickable {
                            onThemeChange(if (followsSystemTheme) "system:${option.first}" else selectedKey)
                        }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Row(Modifier.width(72.dp), horizontalArrangement = Arrangement.spacedBy((-7).dp)) {
                                listOf(palette.paper, palette.ink, palette.accent).forEach { color -> Box(Modifier.size(28.dp).background(color, CircleShape).border(1.dp, Hairline, CircleShape)) }
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) { KText(option.second, 15, Ink, FontWeight.Black); KText(option.third, 10, Muted) }
                            if (themeFamily == option.first) CustomIcon(IconType.CHECK, Carrot, 17.dp)
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }
            }
            return
        }
        "notificationSettings" -> {
            MyPageTransition("notificationSettings", true) {
                SystemNotificationSettingsScreen(
                    user = user,
                    onBack = { page = "root" }
                )
            }
            return
        }
        "accounts" -> {
            val accounts = remember(user.id) { api.savedAccounts() }
            val activeIdentifier = remember(user.id) { api.activeAccountIdentifier() }
            MyPageTransition("accounts", true) {
            Column(Modifier.fillMaxSize().background(Paper)) {
                OverlayHeader("アカウントを切り替える", { page = "root" })
                LazyColumn(contentPadding = PaddingValues(horizontal = 22.dp, vertical = 20.dp)) {
                    item {
                        KText("保存済みアカウント", 11, Muted, FontWeight.Black, letterSpacing = 1.3f)
                        Spacer(Modifier.height(10.dp))
                    }
                    items(accounts) { account ->
                        val identifier = account.identifier
                        val active = identifier == activeIdentifier
                        val accountName = if (active && account.displayName == identifier) user.displayName.ifBlank { user.username } else account.displayName
                        val accountAvatar = account.avatarUrl ?: if (active) user.avatarUrl else null
                        val accountUsername = account.username.ifBlank { if (active) user.username else "" }
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(if (active) Strong else Surface)
                                .border(1.dp, if (active) Strong else Hairline, RoundedCornerShape(18.dp))
                                .clickable(enabled = !active && switchingAccount == null) {
                                    switchingAccount = identifier
                                    accountError = null
                                    scope.launch {
                                        when (val result = withContext(Dispatchers.IO) { api.switchAccount(identifier) }) {
                                            is ApiResult.Success -> onAccountChanged(result.value)
                                            is ApiResult.Failure -> accountError = result.message
                                        }
                                        switchingAccount = null
                                    }
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(Modifier.size(42.dp).background(if (active) Carrot else PaleCarrot, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                                if (accountAvatar != null) {
                                    AsyncImage(accountAvatar, accountName, Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp)), contentScale = ContentScale.Crop)
                                } else KText(accountName.take(1).uppercase(), 15, if (active) Color.White else Ink, FontWeight.Black)
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                KText(accountName, 13, if (active) OnStrong else Ink, FontWeight.Bold, maxLines = 1)
                                if (accountUsername.isNotBlank()) KText("@$accountUsername", 10, if (active) OnStrong.copy(.58f) else Muted, maxLines = 1)
                                KText(if (active) "現在使用中" else if (switchingAccount == identifier) "切り替え中…" else "タップして切り替え", 10, if (active) OnStrong.copy(.58f) else Muted)
                            }
                            if (active) CustomIcon(IconType.CHECK, Carrot, 16.dp)
                        }
                        Spacer(Modifier.height(9.dp))
                    }
                    if (accountError != null) item { ErrorText(accountError.orEmpty()) }
                    item {
                        Spacer(Modifier.height(8.dp))
                        Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).border(1.dp, Hairline, RoundedCornerShape(16.dp)).clickable { onAddAccount() }.padding(vertical = 13.dp), contentAlignment = Alignment.Center) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                CustomIcon(IconType.PLUS, Carrot, 15.dp)
                                KText("別のアカウントを追加", 12, Carrot, FontWeight.Black)
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        KText("ログイン情報はAndroid Keystoreの鍵で暗号化して、この端末内に保存されます。", 10, Muted, lineHeight = 16f)
                    }
                }
            }
            }
            return
        }
        "licenses" -> {
            val apacheNotice = """
                Licensed under the Apache License, Version 2.0 (the "License");
                you may not use this software except in compliance with the License.
                You may obtain a copy of the License at

                https://www.apache.org/licenses/LICENSE-2.0

                Unless required by applicable law or agreed to in writing, software
                distributed under the License is distributed on an "AS IS" BASIS,
                WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
                See the License for the specific language governing permissions and
                limitations under the License.
            """.trimIndent()
            val mitNotice = """
                Permission is hereby granted, free of charge, to any person obtaining a
                copy of this software and associated documentation files, to deal in
                the Software without restriction, including without limitation the
                rights to use, copy, modify, merge, publish, distribute, sublicense,
                and/or sell copies of the Software, subject to inclusion of the
                copyright and permission notices.

                THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
                EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
                MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
            """.trimIndent()
            val gplNotice = """
                This library is free software; you can redistribute it and/or modify
                it under the terms of the GNU General Public License, version 2.
                It is distributed in the hope that it will be useful, but WITHOUT
                ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
                or FITNESS FOR A PARTICULAR PURPOSE.

                GNU GPL v2の完全な条文は、下の「ライセンス全文」から確認できます。
            """.trimIndent()
            val licenses = remember {
                listOf(
                    ThirdPartyLicense("AndroidX / Jetpack Compose", "Compose BOM 2025.02.00", "Apache License 2.0", "Copyright The Android Open Source Project", apacheNotice, "https://www.apache.org/licenses/LICENSE-2.0"),
                    ThirdPartyLicense("Kotlin", "2.0.21", "Apache License 2.0", "Copyright JetBrains s.r.o. and Kotlin contributors", apacheNotice, "https://github.com/JetBrains/kotlin/blob/master/license/LICENSE.txt"),
                    ThirdPartyLicense("Coil", "2.7.0", "Apache License 2.0", "Copyright Coil contributors", apacheNotice, "https://github.com/coil-kt/coil/blob/2.7.0/LICENSE.txt"),
                    ThirdPartyLicense("Conscrypt", "2.5.3", "Apache License 2.0", "Copyright The Conscrypt Authors", apacheNotice, "https://github.com/google/conscrypt/blob/2.5.3/LICENSE"),
                    ThirdPartyLicense("Markwon", "4.6.2", "Apache License 2.0", "Copyright Dimitry Ivanov and Markwon contributors", apacheNotice, "https://github.com/noties/Markwon/blob/4.6.2/LICENSE"),
                    ThirdPartyLicense("JLatexMath Android", "0.2.0", "GNU GPL v2", "Copyright JLaTeXMath and jlatexmath-android contributors", gplNotice, "https://www.gnu.org/licenses/old-licenses/gpl-2.0.html"),
                    ThirdPartyLicense("Phosphor Icons / phosphor-icon", "Core icons / Compose library 1.0.0", "MIT License", "Copyright Phosphor Icons contributors and Adam Glin", mitNotice, "https://github.com/phosphor-icons/core/blob/main/LICENSE"),
                    ThirdPartyLicense("Haze", "1.4.0", "Apache License 2.0", "Copyright Chris Banes and Haze contributors", apacheNotice, "https://github.com/chrisbanes/haze/blob/1.4.0/LICENSE")
                )
            }
            var expandedLicense by remember { mutableStateOf<String?>(null) }
            MyPageTransition("licenses", true) {
            Column(Modifier.fillMaxSize().background(Paper)) {
                OverlayHeader("サードパーティーライセンス", { page = "root" })
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 18.dp,
                        top = 18.dp,
                        end = 18.dp,
                        bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding() + 28.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        KText("Karohaは、以下のオープンソースソフトウェアを使用しています。項目をタップすると著作権表示とライセンス条項を確認できます。", 12, Muted, lineHeight = 19f)
                        Spacer(Modifier.height(4.dp))
                    }
                    items(licenses) { item ->
                        val expanded = expandedLicense == item.name
                        Column(
                            Modifier.fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(Surface)
                                .border(1.dp, Hairline, RoundedCornerShape(18.dp))
                                .clickable { expandedLicense = if (expanded) null else item.name }
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column(Modifier.weight(1f)) {
                                    KText(item.name, 14, Ink, FontWeight.Bold)
                                    Spacer(Modifier.height(3.dp))
                                    KText(item.version, 10, Muted)
                                }
                                Box(
                                    Modifier.background(PaleCarrot, RoundedCornerShape(999.dp))
                                        .padding(horizontal = 9.dp, vertical = 5.dp)
                                ) {
                                    KText(item.license, 9, Carrot, FontWeight.Bold)
                                }
                                Spacer(Modifier.width(8.dp))
                                CustomIcon(if (expanded) IconType.CHEVRON_UP else IconType.CHEVRON_DOWN, Muted, 15.dp)
                            }
                            AnimatedVisibility(
                                visible = expanded,
                                enter = expandVertically(tween(260, easing = FastOutSlowInEasing)) + fadeIn(tween(180)),
                                exit = fadeOut(tween(130))
                            ) {
                                Column {
                                    Spacer(Modifier.height(14.dp))
                                    Box(Modifier.fillMaxWidth().height(1.dp).background(Hairline))
                                    Spacer(Modifier.height(14.dp))
                                    KText(item.copyright, 10, Ink, FontWeight.Bold, lineHeight = 16f)
                                    Spacer(Modifier.height(10.dp))
                                    KText(item.notice, 10, Muted, lineHeight = 16f)
                                    Spacer(Modifier.height(13.dp))
                                    Row(
                                        Modifier.clip(RoundedCornerShape(12.dp))
                                            .background(PaleCarrot)
                                            .clickable {
                                                runCatching {
                                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.url)))
                                                }
                                            }
                                            .padding(horizontal = 12.dp, vertical = 9.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(7.dp)
                                    ) {
                                        CustomIcon(IconType.LINK, Carrot, 14.dp)
                                        KText("ライセンス全文", 10, Carrot, FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                    item {
                        KText(
                            "各ソフトウェアの権利は、それぞれの著作権者に帰属します。KarohaおよびNamiCodeは、各プロジェクトの公式提供元ではありません。",
                            10,
                            Muted,
                            lineHeight = 16f,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            }
            }
            return
        }
        "about" -> {
            var openingKarotterProfile by remember { mutableStateOf(false) }
            var providerLinkError by remember { mutableStateOf<String?>(null) }
            val appVersion = remember {
                runCatching {
                    context.packageManager.getPackageInfo(context.packageName, 0).versionName
                }.getOrNull().orEmpty().ifBlank { "不明" }
            }
            val providerLinks = listOf(
                Triple("GitHub", "NamiCode-Dev", "https://github.com/NamiCode-Dev"),
                Triple("X", "@NamiCode_Dev", "https://x.com/NamiCode_Dev"),
                Triple("Karotter", "@namicode", null),
                Triple("Web", "namicode.f5.si", "https://namicode.f5.si/")
            )
            MyPageTransition("about", true) {
            Column(Modifier.fillMaxSize().background(Paper)) {
                OverlayHeader("Karohaについて", { page = "root" })
                LazyColumn(
                    Modifier.weight(1f),
                    contentPadding = PaddingValues(start = 22.dp, top = 22.dp, end = 22.dp, bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding() + 28.dp)
                ) {
                    item {
                        KarohaAppLogo(72.dp)
                        Spacer(Modifier.height(20.dp))
                        KText("Karoha for Android", 22, Ink, FontWeight.Black)
                        KText("Karotter非公式Androidクライアント · version $appVersion", 11, Muted)
                        Spacer(Modifier.height(22.dp))
                        KText(
                            "Karohaは、KarotterをAndroidで快適に利用するための非公式クライアントです。投稿、検索、ストーリー、掲示板、ダイレクトメッセージなど、Karotterの主要な機能をひとつのアプリから利用できます。",
                            13,
                            Ink,
                            lineHeight = 21f
                        )
                        Spacer(Modifier.height(13.dp))
                        KText(
                            "NamiCodeが独自に開発・提供しているアプリであり、Karotter公式が提供するアプリではありません。",
                            11,
                            Muted,
                            lineHeight = 18f
                        )
                        Spacer(Modifier.height(24.dp))
                        Column(
                            Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(18.dp))
                                .border(1.dp, Hairline, RoundedCornerShape(18.dp)).padding(16.dp)
                        ) {
                            KText("PROVIDED BY", 9, Carrot, FontWeight.Black, letterSpacing = 1.5f)
                            Spacer(Modifier.height(5.dp))
                            KText("NamiCode", 19, Ink, FontWeight.Black)
                            Spacer(Modifier.height(4.dp))
                            KText("設計・開発・配布をNamiCodeが行っています。", 11, Muted)
                        }
                        Spacer(Modifier.height(14.dp))
                    }
                    items(providerLinks) { (label, detail, url) ->
                        Row(
                            Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).clickable(enabled = !openingKarotterProfile) {
                                providerLinkError = null
                                if (url == null) {
                                    openingKarotterProfile = true
                                    scope.launch {
                                        when (val result = withContext(Dispatchers.IO) { api.user("namicode") }) {
                                            is ApiResult.Success -> onUser(result.value)
                                            is ApiResult.Failure -> providerLinkError = result.message
                                        }
                                        openingKarotterProfile = false
                                    }
                                } else {
                                    runCatching {
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                                    }
                                }
                            }.padding(horizontal = 14.dp, vertical = 13.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                KText(label, 13, Ink, FontWeight.Bold)
                                KText(
                                    if (url == null && openingKarotterProfile) "プロフィールを読み込み中…" else detail,
                                    9,
                                    Muted,
                                    maxLines = 1
                                )
                            }
                            CustomIcon(IconType.FORWARD, Carrot, 16.dp)
                        }
                    }
                    providerLinkError?.let { message ->
                        item {
                            Spacer(Modifier.height(6.dp))
                            ErrorText(message)
                        }
                    }
                }
            }
            }
            return
        }
    }
    MyPageTransition("root", false) {
    LazyColumn(Modifier.fillMaxSize().background(Paper), contentPadding = PaddingValues(bottom = 126.dp)) {
        item {
            Row(
                Modifier.fillMaxWidth().padding(start = 22.dp, top = 23.dp, end = 22.dp, bottom = 15.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(Modifier.weight(1f)) {
                    KText("YOU", 10, Carrot, FontWeight.Black, letterSpacing = 2.4f)
                    Spacer(Modifier.height(5.dp))
                    KText("マイページ", 29, Ink, FontWeight.Black)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    if (user.isPrivate) {
                        Box(
                            Modifier.size(42.dp).background(Surface, RoundedCornerShape(14.dp))
                                .border(1.dp, Hairline, RoundedCornerShape(14.dp))
                                .clickable { page = "followRequests" },
                            contentAlignment = Alignment.Center
                        ) {
                            CustomIcon(IconType.PERSON, Ink, 18.dp)
                        }
                    }
                    Box(
                        Modifier.size(42.dp).background(Surface, RoundedCornerShape(14.dp))
                            .border(1.dp, Hairline, RoundedCornerShape(14.dp))
                            .clickable { page = "accounts" },
                        contentAlignment = Alignment.Center
                    ) {
                        CustomIcon(IconType.ACCOUNT_SWITCH, Ink, 18.dp)
                    }
                }
            }
            Column(
                Modifier.padding(horizontal = 18.dp).fillMaxWidth().clip(RoundedCornerShape(26.dp))
                    .background(Strong).clickable { page = "profile" }.padding(18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(64.dp).background(PaleCarrot, RoundedCornerShape(22.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (user.avatarUrl != null) {
                            AsyncImage(user.avatarUrl, user.displayName, Modifier.fillMaxSize().clip(RoundedCornerShape(22.dp)), contentScale = ContentScale.Crop)
                        } else {
                            KText(user.displayName.ifBlank { user.username }.take(1), 22, Ink, FontWeight.Black)
                        }
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            KText(user.displayName.ifBlank { user.username }, 18, OnStrong, FontWeight.Black, maxLines = 1, modifier = Modifier.weight(1f, fill = false))
                            if (user.isPrivate) {
                                Spacer(Modifier.width(6.dp))
                                CustomIcon(IconType.LOCK, Carrot, 15.dp)
                            }
                        }
                        KText("@${user.username}", 11, OnStrong.copy(.62f), maxLines = 1)
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                            user.level?.let { level ->
                                KText("Lv.$level", 9, Carrot, FontWeight.Black, modifier = Modifier.background(OnStrong.copy(.1f), RoundedCornerShape(7.dp)).padding(horizontal = 7.dp, vertical = 4.dp))
                            }
                            KText(
                                if (user.isPrivate) "非公開" else "公開",
                                9,
                                OnStrong,
                                FontWeight.Bold,
                                modifier = Modifier.background(OnStrong.copy(.1f), RoundedCornerShape(7.dp)).padding(horizontal = 7.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Box(
                        Modifier.size(36.dp).background(OnStrong.copy(.1f), RoundedCornerShape(12.dp))
                            .clickable { page = "profileEdit" },
                        contentAlignment = Alignment.Center
                    ) {
                        CustomIcon(IconType.PROFILE_EDIT, OnStrong, 17.dp)
                    }
                    Spacer(Modifier.width(7.dp))
                    CustomIcon(IconType.FORWARD, OnStrong, 20.dp)
                }
                Spacer(Modifier.height(15.dp))
                Box(Modifier.fillMaxWidth().height(1.dp).background(OnStrong.copy(.12f)))
                Spacer(Modifier.height(13.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    KText("${user.postsCount} 投稿", 10, OnStrong.copy(.72f), FontWeight.Bold)
                    KText("${user.followingCount} フォロー", 10, OnStrong.copy(.72f), FontWeight.Bold)
                    KText("${user.followersCount} フォロワー", 10, OnStrong.copy(.72f), FontWeight.Bold)
                }
            }
        }
        item { MyPageSectionTitle("コンテンツ", "保存した投稿とKarotterでの活動") }
        item {
            MyPageGroupCard {
                MyPageRow("ブックマーク", "あとで読みたい投稿", "bookmark") { page = "bookmarks" }
                MyPageDivider()
                MyPageRow("予約投稿", "これから公開される投稿", "scheduled") { page = "scheduledPosts" }
                MyPageDivider()
                MyPageRow("質問箱", "届いた質問を確認", "question") { page = "questions" }
                MyPageDivider()
                MyPageRow("レベルランキング", "上位100人のレベルと経験値", "ranking") { page = "ranking" }
            }
        }
        item { MyPageSectionTitle("環境設定", "Karohaの表示とバックグラウンド動作") }
        item {
            MyPageGroupCard {
                MyPageRow("外観テーマ", "${themeFamily.replaceFirstChar { c -> c.uppercase() }} · ${if (followsSystemTheme) "端末に合わせる" else if (darkMode) "ダーク" else "ライト"}", "◐") { page = "appearance" }
                if (user.subscriptionPlan.equals("PRO", true) && user.subscriptionStatus.equals("ACTIVE", true)) {
                    MyPageDivider()
                    MyPageRow("Pro設定", "プロフィールと投稿カードの装飾", "proSettings") { page = "proSettings" }
                }
                MyPageDivider()
                MyPageRow("通知設定", "バックグラウンド通知とAndroidの設定", "bell") { page = "notificationSettings" }
                MyPageDivider()
                MyPageRow("ストレージとキャッシュ", cacheMessage ?: "一時保存された画像を管理", "trash") {
                    confirmCacheClear = true
                }
            }
        }
        item { MyPageSectionTitle("Karohaについて", "アプリ情報とオープンソース") }
        item {
            MyPageGroupCard {
                MyPageRow("このアプリについて", "NamiCode提供・バージョンと概要", "K") { page = "about" }
                MyPageDivider()
                MyPageRow("サードパーティーライセンス", "使用しているオープンソース", "§") { page = "licenses" }
            }
        }
        item { MyPageSectionTitle("セッション", "この端末でのログインを管理") }
        item {
            MyPageGroupCard {
                MyPageRow("ログアウト", "保存済みセッションを終了", "↪", danger = true) { confirmLogout = true }
            }
        }
    }
    }
}

@Composable
private fun SystemNotificationSettingsScreen(user: ApiUser, onBack: () -> Unit) {
    val context = LocalContext.current
    var monitoringEnabled by remember { mutableStateOf(BackgroundNotificationManager.isEnabled(context)) }
    var notificationPermissionGranted by remember {
        mutableStateOf(
            Build.VERSION.SDK_INT < 33 ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        )
    }
    var systemNotificationsEnabled by remember {
        mutableStateOf(NotificationManagerCompat.from(context).areNotificationsEnabled())
    }
    val powerManager = remember { context.getSystemService(PowerManager::class.java) }
    var batteryExempt by remember {
        mutableStateOf(powerManager?.isIgnoringBatteryOptimizations(context.packageName) == true)
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        notificationPermissionGranted = granted
        systemNotificationsEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
    val settingsLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        notificationPermissionGranted =
            Build.VERSION.SDK_INT < 33 ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        systemNotificationsEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
        batteryExempt = powerManager?.isIgnoringBatteryOptimizations(context.packageName) == true
    }

    fun requestSystemNotifications() {
        if (Build.VERSION.SDK_INT >= 33 && !notificationPermissionGranted) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            settingsLauncher.launch(
                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            )
        }
    }

    fun requestBatteryExemption() {
        val intent = Intent(
            Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            Uri.parse("package:${context.packageName}")
        )
        runCatching { settingsLauncher.launch(intent) }
            .onFailure {
                settingsLauncher.launch(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
            }
    }

    Column(Modifier.fillMaxSize().background(Paper)) {
        OverlayHeader("通知設定", onBack)
        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 22.dp,
                top = 20.dp,
                end = 22.dp,
                bottom = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding() + 30.dp
            )
        ) {
            item {
                Row(
                    Modifier.fillMaxWidth().background(Strong, RoundedCornerShape(22.dp)).padding(17.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(45.dp).background(Carrot, RoundedCornerShape(15.dp)), contentAlignment = Alignment.Center) {
                        CustomIcon(IconType.BELL, Color.White, 20.dp)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        KText(user.displayName.ifBlank { "@${user.username}" }, 14, OnStrong, FontWeight.Black, maxLines = 1)
                        KText("現在ログイン中のアカウント", 10, OnStrong.copy(.62f), FontWeight.Medium)
                    }
                }
                Spacer(Modifier.height(18.dp))
                KText("バックグラウンド通知", 11, Muted, FontWeight.Black, letterSpacing = 1.2f)
                Spacer(Modifier.height(9.dp))
                Row(
                    Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(19.dp))
                        .border(1.dp, Hairline, RoundedCornerShape(19.dp))
                        .clickable {
                            val desired = !monitoringEnabled
                            monitoringEnabled = desired
                            BackgroundNotificationManager.setEnabled(context, desired)
                            if (desired && Build.VERSION.SDK_INT >= 33 && !notificationPermissionGranted) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        KText("15秒ごとに確認", 14, Ink, FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        KText(
                            if (monitoringEnabled) "アプリがバックグラウンドの間だけ動作します" else "バックグラウンド通知は停止中です",
                            10,
                            Muted,
                            lineHeight = 16f
                        )
                    }
                    NotificationToggle(monitoringEnabled)
                }
                Spacer(Modifier.height(11.dp))
                NotificationSettingsAction(
                    icon = IconType.BELL,
                    title = "システム通知",
                    description = when {
                        !notificationPermissionGranted -> "権限が許可されていません"
                        !systemNotificationsEnabled -> "Androidの設定で無効になっています"
                        else -> "許可済み"
                    },
                    active = notificationPermissionGranted && systemNotificationsEnabled,
                    onClick = ::requestSystemNotifications
                )
                Spacer(Modifier.height(11.dp))
                NotificationSettingsAction(
                    icon = IconType.THEME,
                    title = "バッテリー制限",
                    description = if (batteryExempt) "最適化の対象外です" else "タップしてAndroidの除外許可を求めます",
                    active = batteryExempt,
                    onClick = ::requestBatteryExemption
                )
                Spacer(Modifier.height(18.dp))
                Column(
                    Modifier.fillMaxWidth().background(PaleCarrot, RoundedCornerShape(17.dp)).padding(15.dp)
                ) {
                    KText("動作について", 11, Ink, FontWeight.Black)
                    Spacer(Modifier.height(6.dp))
                    KText(
                        "アプリを閉じている間は、Androidの継続動作通知を表示しながら15秒ごとに確認します。アプリが前面に戻ると監視を停止し、定期的な通知確認は行いません。",
                        10,
                        Muted,
                        lineHeight = 17f
                    )
                    if (BackgroundNotificationManager.isAuthPaused(context)) {
                        Spacer(Modifier.height(9.dp))
                        KText("再ログインに失敗したため停止中です。ログインし直すと自動で再開できます。", 10, Carrot, FontWeight.Bold, lineHeight = 16f)
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationToggle(enabled: Boolean) {
    Box(
        Modifier.width(48.dp).height(28.dp)
            .background(if (enabled) Carrot else Hairline, RoundedCornerShape(14.dp))
            .padding(3.dp)
    ) {
        Box(
            Modifier.align(if (enabled) Alignment.CenterEnd else Alignment.CenterStart)
                .size(22.dp).background(Color.White, CircleShape)
        )
    }
}

@Composable
private fun NotificationSettingsAction(
    icon: IconType,
    title: String,
    description: String,
    active: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(19.dp))
            .border(1.dp, Hairline, RoundedCornerShape(19.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(40.dp).background(if (active) PaleCarrot else Paper, RoundedCornerShape(13.dp)),
            contentAlignment = Alignment.Center
        ) {
            CustomIcon(icon, if (active) Carrot else Muted, 18.dp)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            KText(title, 13, Ink, FontWeight.Bold)
            Spacer(Modifier.height(3.dp))
            KText(description, 10, if (active) Carrot else Muted, FontWeight.Medium)
        }
        CustomIcon(IconType.FORWARD, Muted, 16.dp)
    }
}

@Composable
private fun ProfileEditScreen(
    user: ApiUser,
    api: KarotterApi,
    onBack: () -> Unit,
    onSaved: (ApiUser) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var username by remember(user.id, user.username) { mutableStateOf(user.username) }
    var displayName by remember(user.id, user.displayName) { mutableStateOf(user.displayName) }
    var bio by remember(user.id, user.bio) { mutableStateOf(user.bio) }
    var location by remember(user.id, user.location) { mutableStateOf(user.location) }
    var onlineStatus by remember(user.id, user.onlineStatus) { mutableStateOf(user.onlineStatus.ifBlank { "OFFLINE" }.uppercase()) }
    var statusMessage by remember(user.id, user.statusMessage) { mutableStateOf(user.statusMessage) }
    var privateAccount by remember(user.id, user.isPrivate) { mutableStateOf(user.isPrivate) }
    var avatar by remember(user.id) { mutableStateOf<ComposerMedia?>(null) }
    var header by remember(user.id) { mutableStateOf<ComposerMedia?>(null) }
    var saving by remember(user.id) { mutableStateOf(false) }
    var error by remember(user.id) { mutableStateOf<String?>(null) }

    fun mediaFrom(uri: Uri, fallback: String): ComposerMedia {
        val name = context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) cursor.getString(0) else null
        } ?: fallback
        return ComposerMedia(uri, name, context.contentResolver.getType(uri).orEmpty().ifBlank { "image/jpeg" })
    }
    val avatarPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let { avatar = mediaFrom(it, "avatar.jpg"); error = null }
    }
    val headerPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let { header = mediaFrom(it, "header.jpg"); error = null }
    }
    val valid = username.trim().isNotBlank() && displayName.trim().isNotBlank() && !saving

    Column(Modifier.fillMaxSize().background(Paper).navigationBarsPadding().imePadding()) {
        OverlayHeader("プロフィールを編集", onBack)
        LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(bottom = 24.dp)) {
            item {
                Box(
                    Modifier.fillMaxWidth().height(170.dp).background(user.profileAccentColor.toAppColor() ?: Strong)
                        .clickable(enabled = !saving) { headerPicker.launch(arrayOf("image/*")) }
                ) {
                    val headerModel: Any? = header?.uri ?: user.headerUrl
                    if (headerModel != null) AsyncImage(headerModel, "ヘッダー", Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    Box(
                        Modifier.align(Alignment.BottomEnd).padding(12.dp).background(Paper.copy(.9f), RoundedCornerShape(11.dp))
                            .padding(horizontal = 11.dp, vertical = 7.dp)
                    ) { KText("ヘッダーを変更", 9, Ink, FontWeight.Bold) }
                }
                Row(Modifier.fillMaxWidth().padding(horizontal = 22.dp).offset(y = (-30).dp), verticalAlignment = Alignment.Bottom) {
                    Box(
                        Modifier.size(78.dp).background(PaleCarrot, RoundedCornerShape(25.dp))
                            .border(4.dp, Paper, RoundedCornerShape(27.dp))
                            .clickable(enabled = !saving) { avatarPicker.launch(arrayOf("image/*")) },
                        contentAlignment = Alignment.Center
                    ) {
                        val avatarModel: Any? = avatar?.uri ?: user.avatarUrl
                        if (avatarModel != null) AsyncImage(avatarModel, "アイコン", Modifier.fillMaxSize().clip(RoundedCornerShape(23.dp)), contentScale = ContentScale.Crop)
                        else KText(displayName.take(1), 25, Ink, FontWeight.Black)
                    }
                    Spacer(Modifier.width(13.dp))
                    KText("画像をタップして変更", 10, Muted, FontWeight.Bold, modifier = Modifier.padding(bottom = 9.dp))
                }
            }
            item {
                Column(Modifier.padding(horizontal = 22.dp)) {
                    ProfileEditField("ユーザー名", "@を除いて入力", username, 30, false) { username = it.filterNot(Char::isWhitespace) }
                    Spacer(Modifier.height(14.dp))
                    ProfileEditField("表示名", "プロフィールに表示する名前", displayName, 50, false) { displayName = it }
                    Spacer(Modifier.height(14.dp))
                    ProfileEditField("自己紹介", "自分について書く", bio, 500, true) { bio = it }
                    Spacer(Modifier.height(14.dp))
                    ProfileEditField("場所", "例：兵庫", location, 80, false) { location = it }
                    Spacer(Modifier.height(14.dp))
                    ProfileEditField("ステータスメッセージ", "いま何をしているか", statusMessage, 100, false) { statusMessage = it }
                    Spacer(Modifier.height(19.dp))
                    Row(
                        Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(17.dp))
                            .border(1.dp, Hairline, RoundedCornerShape(17.dp))
                            .clickable(enabled = !saving) { privateAccount = !privateAccount }
                            .padding(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier.size(40.dp).background(PaleCarrot, RoundedCornerShape(13.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            CustomIcon(IconType.LOCK, if (privateAccount) Carrot else Muted, 18.dp)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            KText("非公開アカウント", 13, Ink, FontWeight.Bold)
                            Spacer(Modifier.height(3.dp))
                            KText("承認したフォロワーだけが投稿を閲覧できます", 9, Muted, FontWeight.Medium)
                        }
                        NotificationToggle(privateAccount)
                    }
                    Spacer(Modifier.height(19.dp))
                    KText("オンラインステータス", 10, Muted, FontWeight.Black, letterSpacing = 1.1f)
                    Spacer(Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                        items(listOf("ONLINE", "IDLE", "DND", "OFFLINE", "INVISIBLE")) { status ->
                            val selected = onlineStatus == status
                            Row(
                                Modifier.clip(RoundedCornerShape(12.dp))
                                    .background(if (selected) Strong else Surface)
                                    .border(1.dp, if (selected) Strong else Hairline, RoundedCornerShape(12.dp))
                                    .clickable { onlineStatus = status }
                                    .padding(horizontal = 11.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(Modifier.size(7.dp).background(onlineStatusColor(status), CircleShape))
                                Spacer(Modifier.width(6.dp))
                                KText(onlineStatusLabel(status), 10, if (selected) OnStrong else Ink, FontWeight.Bold)
                            }
                        }
                    }
                    if (user.subscriptionStatus.equals("ACTIVE", true) && !user.subscriptionPlan.equals("FREE", true)) {
                        Spacer(Modifier.height(20.dp))
                        Row(
                            Modifier.fillMaxWidth().background(user.profileAccentColor.toAppColor() ?: PaleCarrot, RoundedCornerShape(16.dp))
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SubscriptionBadge(user.subscriptionPlan, user.subscriptionStatus, true, user.premiumBadgeColor)
                            Spacer(Modifier.width(9.dp))
                            KText("PRO装飾はプロフィールに自動反映されます", 10, Ink, FontWeight.Bold)
                        }
                    }
                    error?.let { Spacer(Modifier.height(14.dp)); ErrorText(it) }
                }
            }
        }
        Box(
            Modifier.fillMaxWidth().padding(start = 18.dp, end = 18.dp, top = 10.dp, bottom = 84.dp)
                .clip(RoundedCornerShape(16.dp)).background(if (valid) Carrot else Hairline)
                .clickable(enabled = valid) {
                    saving = true
                    error = null
                    val requestedUsername = username.trim().removePrefix("@")
                    val requestedAvatar = avatar
                    val requestedHeader = header
                    scope.launch {
                        val failure = withContext(Dispatchers.IO) {
                            val operations = mutableListOf<() -> ApiResult<Unit>>()
                            operations += { api.updateProfile(displayName.trim(), bio.trim(), user.websiteUrl.orEmpty(), location.trim()) }
                            operations += { api.updateStatus(onlineStatus, statusMessage.trim()) }
                            if (privateAccount != user.isPrivate) operations += { api.updatePrivateAccount(privateAccount) }
                            if (requestedUsername != user.username) operations += { api.updateUsername(requestedUsername) }
                            fun upload(item: ComposerMedia, kind: String): ApiResult<Unit> = runCatching {
                                val bytes = context.contentResolver.openInputStream(item.uri)?.use { it.readBytes() }
                                    ?: throw IllegalStateException("${item.name}を読み込めませんでした")
                                api.uploadProfileImage(kind, ApiUploadMedia(item.name, item.mimeType, bytes))
                            }.getOrElse { ApiResult.Failure(it.message ?: "画像を読み込めませんでした") }
                            requestedAvatar?.let { item -> operations += { upload(item, "avatar") } }
                            requestedHeader?.let { item -> operations += { upload(item, "header") } }
                            operations.asSequence().map { it() }.filterIsInstance<ApiResult.Failure>().firstOrNull()
                        }
                        if (failure != null) {
                            error = failure.message
                            saving = false
                        } else {
                            when (val refreshed = withContext(Dispatchers.IO) { api.user(requestedUsername) }) {
                                is ApiResult.Success -> onSaved(refreshed.value)
                                is ApiResult.Failure -> {
                                    error = refreshed.message
                                    saving = false
                                }
                            }
                        }
                    }
                }.padding(vertical = 13.dp),
            contentAlignment = Alignment.Center
        ) {
            KText(if (saving) "保存中…" else "変更を保存", 12, if (valid) Color.White else Muted, FontWeight.Black)
        }
    }
}

@Composable
private fun ProfileEditField(
    label: String,
    placeholder: String,
    value: String,
    limit: Int,
    multiline: Boolean,
    onValueChange: (String) -> Unit
) {
    Column {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            KText(label, 10, Muted, FontWeight.Black, letterSpacing = 1.1f)
            Spacer(Modifier.weight(1f))
            KText("${value.length}/$limit", 9, if (value.length >= limit) Carrot else Muted, FontWeight.Bold)
        }
        Spacer(Modifier.height(7.dp))
        BasicTextField(
            value = value,
            onValueChange = { onValueChange(it.take(limit)) },
            singleLine = !multiline,
            textStyle = TextStyle(Ink, 14.sp, lineHeight = 21.sp),
            cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
            modifier = Modifier.fillMaxWidth().heightIn(min = if (multiline) 112.dp else 48.dp)
                .clip(RoundedCornerShape(15.dp)).background(Surface).border(1.dp, Hairline, RoundedCornerShape(15.dp))
                .padding(horizontal = 14.dp, vertical = 13.dp),
            decorationBox = { inner ->
                Box {
                    if (value.isBlank()) KText(placeholder, 13, Muted)
                    inner()
                }
            }
        )
    }
}

@Composable
private fun MyPageTransition(pageKey: String, forward: Boolean, content: @Composable () -> Unit) {
    var visible by remember(pageKey) { mutableStateOf(false) }
    LaunchedEffect(pageKey) { visible = true }
    AnimatedVisibility(
        visible = visible,
        modifier = Modifier.fillMaxSize(),
        enter = slideInHorizontally(tween(360, easing = FastOutSlowInEasing)) { width ->
            if (forward) width / 3 else -width / 3
        } + fadeIn(tween(230)),
        exit = fadeOut(tween(120))
    ) {
        content()
    }
}

@Composable
private fun MyPageSectionTitle(text: String, description: String = "") {
    Column(Modifier.padding(start = 22.dp, top = 24.dp, end = 22.dp, bottom = 8.dp)) {
        KText(text, 11, Ink, FontWeight.Black, letterSpacing = .5f)
        if (description.isNotBlank()) {
            Spacer(Modifier.height(3.dp))
            KText(description, 9, Muted, FontWeight.Medium)
        }
    }
}

@Composable
private fun MyPageGroupCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.padding(horizontal = 18.dp).fillMaxWidth()
            .background(Surface, RoundedCornerShape(22.dp))
            .border(1.dp, Hairline, RoundedCornerShape(22.dp))
            .padding(vertical = 4.dp),
        content = content
    )
}

@Composable
private fun MyPageDivider() {
    Box(Modifier.padding(start = 69.dp, end = 14.dp).fillMaxWidth().height(1.dp).background(Hairline))
}

@Composable
private fun MyPageRow(title: String, subtitle: String, symbol: String, danger: Boolean = false, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 14.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(42.dp).background(if (danger) PaleCarrot else Paper, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
            val icon = when (symbol) {
                "bell" -> IconType.BELL
                "trash" -> IconType.TRASH
                "account" -> IconType.ACCOUNT_SWITCH
                "profileEdit" -> IconType.PROFILE_EDIT
                "proSettings" -> IconType.THEME
                "followRequests" -> IconType.PERSON
                "ranking" -> IconType.TROPHY
                "bookmark" -> IconType.BOOKMARK
                "scheduled" -> IconType.CALENDAR
                "question" -> IconType.QUESTION
                "◐" -> IconType.THEME
                "§" -> IconType.LICENSE
                "K" -> IconType.INFO
                "↪" -> IconType.LOGOUT
                else -> IconType.INFO
            }
            CustomIcon(icon, if (danger || symbol == "profileEdit") Carrot else Ink, 18.dp)
        }
        Spacer(Modifier.width(13.dp))
        Column(Modifier.weight(1f)) { KText(title, 14, if (danger) Carrot else Ink, FontWeight.Bold); Spacer(Modifier.height(2.dp)); KText(subtitle, 10, Muted, maxLines = 1) }
        CustomIcon(IconType.FORWARD, Muted, 17.dp)
    }
}

@Composable
private fun ProSettingsScreen(
    user: ApiUser,
    api: KarotterApi,
    onBack: () -> Unit,
    onSaved: (ApiProPreferences) -> Unit
) {
    var preferences by remember(user.id) { mutableStateOf<ApiProPreferences?>(null) }
    var badgeColor by remember(user.id) { mutableStateOf(user.premiumBadgeColor.ifBlank { "ORANGE" }) }
    var profileColor by remember(user.id) { mutableStateOf(user.profileAccentColor.orEmpty()) }
    var cardColor by remember(user.id) { mutableStateOf(user.cardAccentColor.orEmpty()) }
    var pinnedPosts by remember(user.id) {
        mutableStateOf((user.pinnedPosts + listOfNotNull(user.pinnedPost)).distinctBy(ApiPost::id))
    }
    var ownPosts by remember(user.id) { mutableStateOf<List<ApiPost>>(emptyList()) }
    var pinBusyId by remember(user.id) { mutableStateOf<Long?>(null) }
    var replaceTargetId by remember(user.id) { mutableStateOf<Long?>(null) }
    var pinError by remember(user.id) { mutableStateOf<String?>(null) }
    var loading by remember(user.id) { mutableStateOf(true) }
    var saving by remember(user.id) { mutableStateOf(false) }
    var error by remember(user.id) { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val bottomInset = bottomDockContentInset()
    val hexPattern = remember { Regex("^#[0-9A-Fa-f]{6}$") }
    val profileColorValid = profileColor.isBlank() || hexPattern.matches(profileColor)
    val cardColorValid = cardColor.isBlank() || hexPattern.matches(cardColor)
    val active = preferences?.let {
        it.status.equals("ACTIVE", true) && !it.plan.equals("FREE", true)
    } ?: (user.subscriptionStatus.equals("ACTIVE", true) && !user.subscriptionPlan.equals("FREE", true))
    val canCustomizeProfile = preferences?.canCustomizeProfile ?: user.subscriptionPlan.equals("PRO", true)
    val canCustomizeCards = preferences?.canCustomizeCards ?: user.subscriptionPlan.equals("PRO", true)
    val pinnedPostLimit = preferences?.pinnedPostLimit ?: user.pinnedPostLimit.coerceAtLeast(1)
    val canSave = active && profileColorValid && cardColorValid && !loading && !saving

    LaunchedEffect(user.id) {
        when (val result = withContext(Dispatchers.IO) { api.proPreferences() }) {
            is ApiResult.Success -> {
                preferences = result.value
                badgeColor = result.value.premiumBadgeColor.ifBlank { "ORANGE" }
                profileColor = result.value.profileAccentColor.orEmpty()
                cardColor = result.value.cardAccentColor.orEmpty()
                error = null
            }
            is ApiResult.Failure -> error = result.message
        }
        when (val result = withContext(Dispatchers.IO) { api.user(user.username) }) {
            is ApiResult.Success -> {
                pinnedPosts = (result.value.pinnedPosts + listOfNotNull(result.value.pinnedPost))
                    .distinctBy(ApiPost::id)
            }
            is ApiResult.Failure -> if (error == null) pinError = result.message
        }
        when (val result = withContext(Dispatchers.IO) {
            api.userPosts(user.id, page = 1, kind = "posts", limit = 50)
        }) {
            is ApiResult.Success -> ownPosts = result.value.posts.distinctBy(ApiPost::id)
            is ApiResult.Failure -> if (error == null) pinError = result.message
        }
        loading = false
    }

    fun updatePin(post: ApiPost, pinned: Boolean) {
        if (pinBusyId != null) return
        pinBusyId = post.id
        pinError = null
        scope.launch {
            val replacingId = replaceTargetId.takeIf { pinned }
            val result = withContext(Dispatchers.IO) {
                if (replacingId != null) {
                    when (val removed = api.updatePostPin(replacingId, false)) {
                        is ApiResult.Failure -> removed
                        is ApiResult.Success -> when (val added = api.updatePostPin(post.id, true)) {
                            is ApiResult.Success -> added
                            is ApiResult.Failure -> {
                                api.updatePostPin(replacingId, true)
                                added
                            }
                        }
                    }
                } else {
                    api.updatePostPin(post.id, pinned)
                }
            }
            when (result) {
                is ApiResult.Success -> {
                    pinnedPosts = if (!pinned) {
                        pinnedPosts.filterNot { it.id == post.id }
                    } else if (replacingId != null) {
                        pinnedPosts.map { if (it.id == replacingId) post else it }.distinctBy(ApiPost::id)
                    } else {
                        (pinnedPosts + post).distinctBy(ApiPost::id)
                    }
                    replaceTargetId = null
                }
                is ApiResult.Failure -> pinError = result.message
            }
            pinBusyId = null
        }
    }

    Column(Modifier.fillMaxSize().background(Paper)) {
        OverlayHeader("Pro設定", onBack)
        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 22.dp, top = 20.dp, end = 22.dp, bottom = bottomInset + 28.dp)
        ) {
            item {
                Row(
                    Modifier.fillMaxWidth().background(Strong, RoundedCornerShape(22.dp)).padding(17.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.size(46.dp).background(Carrot, RoundedCornerShape(15.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomIcon(IconType.THEME, Color.White, 21.dp)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        KText("PROFILE STUDIO", 9, Carrot, FontWeight.Black, letterSpacing = 1.7f)
                        Spacer(Modifier.height(3.dp))
                        KText(
                            preferences?.plan?.let { "Karotter ${it.uppercase()}" }
                                ?: "Karotter ${user.subscriptionPlan.uppercase()}",
                            15, OnStrong, FontWeight.Black, maxLines = 1
                        )
                        KText(
                            if (active) "Pro向け装飾を編集できます" else "有効なProプランが必要です",
                            10, OnStrong.copy(.64f), maxLines = 1
                        )
                    }
                }
                if (loading) {
                    Spacer(Modifier.height(18.dp))
                    LoadingPost()
                }
                if (!loading && !active) {
                    Spacer(Modifier.height(16.dp))
                    KText("このアカウントではPro装飾を変更できません。", 11, Muted, FontWeight.Bold)
                }
                error?.let {
                    Spacer(Modifier.height(14.dp))
                    ErrorText(it)
                }

                Spacer(Modifier.height(22.dp))
                KText("バッジカラー", 11, Ink, FontWeight.Black)
                Spacer(Modifier.height(5.dp))
                KText("Karotterで使われるバッジカラーを設定します。", 9, Muted, lineHeight = 15f)
                Spacer(Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listOf("ORANGE", "PINK", "MAGENTA", "RED", "GREEN", "BLUE", "PURPLE", "BLACK")) { colorName ->
                        val selected = badgeColor.equals(colorName, true)
                        Box(
                            Modifier.size(38.dp).background(namedPremiumColor(colorName), CircleShape)
                                .border(if (selected) 3.dp else 1.dp, if (selected) Ink else Hairline, CircleShape)
                                .clickable(enabled = active && !saving) { badgeColor = colorName },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selected) CustomIcon(IconType.CHECK, Color.White, 15.dp)
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
                ProAccentField(
                    label = "プロフィールアクセント",
                    description = if (canCustomizeProfile) "プロフィールヘッダー周辺の色" else "このプランでは変更できません",
                    value = profileColor,
                    enabled = active && canCustomizeProfile && !saving,
                    valid = profileColorValid,
                    onValueChange = { profileColor = it }
                )
                Spacer(Modifier.height(18.dp))
                ProAccentField(
                    label = "投稿カードアクセント",
                    description = if (canCustomizeCards) "投稿カード左端のアクセント色" else "このプランでは変更できません",
                    value = cardColor,
                    enabled = active && canCustomizeCards && !saving,
                    valid = cardColorValid,
                    onValueChange = { cardColor = it }
                )

                Spacer(Modifier.height(24.dp))
                ProPinnedPostsManager(
                    pinnedPosts = pinnedPosts,
                    candidatePosts = ownPosts.filter { candidate -> pinnedPosts.none { it.id == candidate.id } },
                    limit = pinnedPostLimit,
                    enabled = active && !loading && pinBusyId == null,
                    busyPostId = pinBusyId,
                    replaceTargetId = replaceTargetId,
                    error = pinError,
                    onStartReplace = { replaceTargetId = if (replaceTargetId == it.id) null else it.id },
                    onCancelReplace = { replaceTargetId = null },
                    onAdd = { updatePin(it, true) },
                    onRemove = { updatePin(it, false) }
                )

                Spacer(Modifier.height(24.dp))
                Box(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(17.dp))
                        .background(if (canSave) Carrot else Hairline)
                        .clickable(enabled = canSave) {
                            saving = true
                            error = null
                            scope.launch {
                                when (val result = withContext(Dispatchers.IO) {
                                    api.updateProPreferences(
                                        badgeColor,
                                        profileColor.takeIf(String::isNotBlank),
                                        cardColor.takeIf(String::isNotBlank)
                                    )
                                }) {
                                    is ApiResult.Success -> onSaved(
                                        ApiProPreferences(
                                            plan = preferences?.plan ?: user.subscriptionPlan,
                                            status = preferences?.status ?: user.subscriptionStatus,
                                            canCustomizeProfile = canCustomizeProfile,
                                            canCustomizeCards = canCustomizeCards,
                                            pinnedPostLimit = preferences?.pinnedPostLimit ?: user.pinnedPostLimit,
                                            premiumBadgeColor = badgeColor,
                                            profileAccentColor = profileColor.takeIf(String::isNotBlank),
                                            cardAccentColor = cardColor.takeIf(String::isNotBlank)
                                        )
                                    )
                                    is ApiResult.Failure -> {
                                        error = result.message
                                        saving = false
                                    }
                                }
                            }
                        }.padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    KText(if (saving) "保存中…" else "Pro設定を保存", 12, if (canSave) Color.White else Muted, FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun ProPinnedPostsManager(
    pinnedPosts: List<ApiPost>,
    candidatePosts: List<ApiPost>,
    limit: Int,
    enabled: Boolean,
    busyPostId: Long?,
    replaceTargetId: Long?,
    error: String?,
    onStartReplace: (ApiPost) -> Unit,
    onCancelReplace: () -> Unit,
    onAdd: (ApiPost) -> Unit,
    onRemove: (ApiPost) -> Unit
) {
    Column(
        Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(20.dp))
            .border(1.dp, Hairline, RoundedCornerShape(20.dp)).padding(15.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(36.dp).background(PaleCarrot, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                CustomIcon(IconType.PIN, Carrot, 17.dp)
            }
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                KText("固定投稿", 13, Ink, FontWeight.Black)
                KText("Proでは最大${limit}件までプロフィールに固定できます", 9, Muted)
            }
            KText("${pinnedPosts.size}/$limit", 11, Carrot, FontWeight.Black)
        }
        Spacer(Modifier.height(13.dp))
        if (pinnedPosts.isEmpty()) {
            KText("現在固定している投稿はありません。", 10, Muted, modifier = Modifier.padding(vertical = 8.dp))
        } else {
            pinnedPosts.forEachIndexed { index, post ->
                val replacing = replaceTargetId == post.id
                Row(
                    Modifier.fillMaxWidth().background(
                        if (replacing) PaleCarrot else Paper,
                        RoundedCornerShape(14.dp)
                    ).border(
                        1.dp,
                        if (replacing) Carrot.copy(.55f) else Hairline,
                        RoundedCornerShape(14.dp)
                    ).padding(11.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.size(27.dp).background(if (replacing) Carrot else Strong, RoundedCornerShape(9.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        KText("${index + 1}", 9, if (replacing) Color.White else OnStrong, FontWeight.Black)
                    }
                    Spacer(Modifier.width(9.dp))
                    Column(Modifier.weight(1f)) {
                        KText(
                            post.content.ifBlank { if (post.media.isNotEmpty()) "メディア付き投稿" else "本文のない投稿" },
                            10,
                            Ink,
                            FontWeight.Bold,
                            maxLines = 2,
                            lineHeight = 15f
                        )
                        KText(relativeTime(post.createdAt), 8, Muted)
                    }
                    if (candidatePosts.isNotEmpty()) {
                        Box(
                            Modifier.clip(RoundedCornerShape(9.dp))
                                .background(if (replacing) Carrot else Surface)
                                .border(1.dp, if (replacing) Carrot else Hairline, RoundedCornerShape(9.dp))
                                .clickable(enabled = enabled) { onStartReplace(post) }
                                .padding(horizontal = 8.dp, vertical = 7.dp)
                        ) {
                            KText(if (replacing) "選択中" else "変更", 8, if (replacing) Color.White else Ink, FontWeight.Black)
                        }
                        Spacer(Modifier.width(5.dp))
                    }
                    Box(
                        Modifier.size(30.dp).clip(RoundedCornerShape(9.dp))
                            .background(Surface).border(1.dp, Hairline, RoundedCornerShape(9.dp))
                            .clickable(enabled = enabled) { onRemove(post) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (busyPostId == post.id) KText("…", 10, Muted, FontWeight.Black)
                        else CustomIcon(IconType.TRASH, Color(0xFFD64045), 14.dp)
                    }
                }
                if (index != pinnedPosts.lastIndex) Spacer(Modifier.height(7.dp))
            }
        }
        error?.let {
            Spacer(Modifier.height(10.dp))
            ErrorText(it)
        }
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                KText(if (replaceTargetId != null) "変更先の投稿" else "固定する投稿を追加", 11, Ink, FontWeight.Black)
                KText(
                    if (replaceTargetId != null) "下の投稿を選ぶと固定位置を入れ替えます"
                    else if (pinnedPosts.size >= limit) "変更する固定投稿を先に選択してください"
                    else "最近の自分の投稿から選択できます",
                    9,
                    Muted
                )
            }
            if (replaceTargetId != null) {
                Box(
                    Modifier.clip(RoundedCornerShape(9.dp)).border(1.dp, Hairline, RoundedCornerShape(9.dp))
                        .clickable { onCancelReplace() }.padding(horizontal = 9.dp, vertical = 7.dp)
                ) { KText("キャンセル", 8, Muted, FontWeight.Black) }
            }
        }
        Spacer(Modifier.height(9.dp))
        if (candidatePosts.isEmpty()) {
            KText("追加できる投稿はありません。", 10, Muted, modifier = Modifier.padding(vertical = 7.dp))
        } else if (pinnedPosts.size < limit || replaceTargetId != null) {
            candidatePosts.take(30).forEachIndexed { index, post ->
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(13.dp))
                        .clickable(enabled = enabled) { onAdd(post) }
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        KText(
                            post.content.ifBlank { if (post.media.isNotEmpty()) "メディア付き投稿" else "本文のない投稿" },
                            10,
                            Ink,
                            FontWeight.Bold,
                            maxLines = 2,
                            lineHeight = 15f
                        )
                        KText(relativeTime(post.createdAt), 8, Muted)
                    }
                    if (busyPostId == post.id) KText("…", 10, Carrot, FontWeight.Black)
                    else CustomIcon(if (replaceTargetId != null) IconType.REKAROT else IconType.PLUS, Carrot, 15.dp)
                }
                if (index != candidatePosts.take(30).lastIndex) {
                    Box(Modifier.padding(start = 10.dp).fillMaxWidth().height(1.dp).background(Hairline))
                }
            }
        }
    }
}

@Composable
private fun ProAccentField(
    label: String,
    description: String,
    value: String,
    enabled: Boolean,
    valid: Boolean,
    onValueChange: (String) -> Unit
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(22.dp).background(value.toAppColor() ?: Hairline, RoundedCornerShape(7.dp))
                    .border(1.dp, Hairline, RoundedCornerShape(7.dp))
            )
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                KText(label, 12, Ink, FontWeight.Black)
                KText(description, 9, Muted, maxLines = 1)
            }
            if (value.isNotBlank()) {
                KText(
                    "なし",
                    9,
                    if (enabled) Carrot else Muted,
                    FontWeight.Bold,
                    modifier = Modifier.clip(RoundedCornerShape(9.dp))
                        .clickable(enabled = enabled) { onValueChange("") }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }
        }
        Spacer(Modifier.height(9.dp))
        BasicTextField(
            value = value,
            onValueChange = { raw ->
                val normalized = raw.uppercase().filter { it == '#' || it in '0'..'9' || it in 'A'..'F' }.take(7)
                onValueChange(normalized)
            },
            enabled = enabled,
            singleLine = true,
            textStyle = TextStyle(if (enabled) Ink else Muted, 14.sp, FontWeight.Bold),
            cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
            modifier = Modifier.fillMaxWidth().height(50.dp)
                .clip(RoundedCornerShape(15.dp)).background(Surface)
                .border(1.dp, if (valid) Hairline else Color(0xFFD64045), RoundedCornerShape(15.dp))
                .padding(horizontal = 14.dp),
            decorationBox = { inner ->
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                    if (value.isBlank()) KText("色なし、または #RRGGBB", 12, Muted)
                    inner()
                }
            }
        )
        if (!valid) {
            Spacer(Modifier.height(5.dp))
            KText("#RRGGBB形式で入力してください", 9, Color(0xFFD64045), FontWeight.Bold)
        }
        Spacer(Modifier.height(9.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            items(listOf("#FF7A45", "#3689DC", "#35A66F", "#E65E9A", "#8759D8", "#17191C")) { preset ->
                Box(
                    Modifier.size(30.dp).background(preset.toAppColor() ?: Hairline, CircleShape)
                        .border(if (value == preset) 3.dp else 1.dp, if (value == preset) Ink else Hairline, CircleShape)
                        .clickable(enabled = enabled) { onValueChange(preset) }
                )
            }
        }
    }
}

@Composable
private fun FollowRequestsScreen(
    api: KarotterApi,
    onBack: () -> Unit,
    onUser: (ApiUser) -> Unit
) {
    var requests by remember { mutableStateOf<List<ApiFollowRequest>>(emptyList()) }
    var nextPage by remember { mutableIntStateOf(1) }
    var hasNext by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) }
    var refreshing by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var busyId by remember { mutableStateOf<Long?>(null) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val bottomInset = bottomDockContentInset()

    suspend fun loadNext() {
        if (loading || !hasNext) return
        loading = true
        val requestedPage = nextPage
        when (val result = withContext(Dispatchers.IO) { api.followRequests(requestedPage) }) {
            is ApiResult.Success -> {
                val incoming = result.value.requests.filter { candidate ->
                    requests.none { it.id == candidate.id }
                }
                requests = requests + incoming
                nextPage = result.value.nextPage ?: requestedPage + 1
                hasNext = result.value.hasNext && incoming.isNotEmpty()
                error = null
            }
            is ApiResult.Failure -> {
                error = result.message
                hasNext = false
            }
        }
        loading = false
    }

    suspend fun refreshFirstPage() {
        if (refreshing || loading) return
        refreshing = true
        when (val result = withContext(Dispatchers.IO) { api.followRequests(1) }) {
            is ApiResult.Success -> {
                requests = (result.value.requests + requests).distinctBy(ApiFollowRequest::id)
                error = null
            }
            is ApiResult.Failure -> error = result.message
        }
        refreshing = false
    }

    fun respond(request: ApiFollowRequest, accept: Boolean) {
        if (busyId != null) return
        busyId = request.id
        scope.launch {
            when (val result = withContext(Dispatchers.IO) {
                api.respondFollowRequest(request.id, accept)
            }) {
                is ApiResult.Success -> requests = requests.filterNot { it.id == request.id }
                is ApiResult.Failure -> error = result.message
            }
            busyId = null
        }
    }

    LaunchedEffect(Unit) {
        loadNext()
        while (true) {
            delay(AUTO_REFRESH_INTERVAL_MS)
            refreshFirstPage()
        }
    }
    InfiniteLoadEffect(listState, requests.size, hasNext, loading) { scope.launch { loadNext() } }

    Column(Modifier.fillMaxSize().background(Paper)) {
        OverlayHeader("フォローリクエスト", onBack)
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = bottomInset + 24.dp)
        ) {
            item {
                Column(Modifier.padding(horizontal = 22.dp, vertical = 18.dp)) {
                    KText("REQUESTS", 10, Carrot, FontWeight.Black, letterSpacing = 2.2f)
                    Spacer(Modifier.height(7.dp))
                    KText("あなたをフォローしたいユーザー", 14, Muted)
                }
            }
            if (loading && requests.isEmpty()) items(5) { LoadingPost() }
            error?.let { message -> item { ErrorText(message) } }
            items(requests, key = { it.id }) { request ->
                Row(
                    Modifier.fillMaxWidth().clickable { onUser(request.user) }
                        .padding(horizontal = 20.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.size(48.dp).background(PaleCarrot, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (request.user.avatarUrl != null) {
                            AsyncImage(
                                request.user.avatarUrl,
                                request.user.displayName,
                                Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            KText(
                                request.user.displayName.ifBlank { request.user.username }.take(1),
                                16, Ink, FontWeight.Black
                            )
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            KText(
                                request.user.displayName.ifBlank { request.user.username },
                                13, Ink, FontWeight.Bold, maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                            AccountMarks(
                                request.user.officialMarks.ifEmpty { listOf(request.user.officialMark) },
                                request.user.isBotAccount,
                                request.user.isParodyAccount,
                                request.user.isPrivate,
                                compact = true
                            )
                        }
                        KText("@${request.user.username}", 10, Muted, maxLines = 1)
                    }
                    Spacer(Modifier.width(8.dp))
                    Box(
                        Modifier.size(34.dp).background(PaleCarrot, RoundedCornerShape(11.dp))
                            .clickable(enabled = busyId == null) { respond(request, false) },
                        contentAlignment = Alignment.Center
                    ) {
                        CustomIcon(IconType.CLOSE, Muted, 15.dp)
                    }
                    Spacer(Modifier.width(6.dp))
                    Box(
                        Modifier.size(34.dp).background(Carrot, RoundedCornerShape(11.dp))
                            .clickable(enabled = busyId == null) { respond(request, true) },
                        contentAlignment = Alignment.Center
                    ) {
                        CustomIcon(IconType.CHECK, Color.White, 15.dp)
                    }
                }
                Box(Modifier.padding(start = 80.dp).fillMaxWidth().height(1.dp).background(Hairline))
            }
            if (loading && requests.isNotEmpty()) item { LoadingPost() }
            if (!loading && error == null && requests.isEmpty()) {
                item { KText("届いているフォローリクエストはありません", 12, Muted, modifier = Modifier.padding(22.dp)) }
            }
        }
    }
}

@Composable
private fun QuestionsInboxScreen(
    api: KarotterApi,
    onBack: () -> Unit,
    onPost: (Post) -> Unit,
    onUser: (ApiUser) -> Unit,
    onAnswer: (ApiQuestion) -> Unit
) {
    var questions by remember { mutableStateOf<List<ApiQuestion>>(emptyList()) }
    var selectedFilter by remember { mutableStateOf("unanswered") }
    var loading by remember { mutableStateOf(true) }
    var refreshing by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val filterTabs = remember { listOf("unanswered", "answered") }
    val questionTabMotion = rememberHorizontalTabMotion(
        filterTabs.indexOf(selectedFilter).coerceAtLeast(0),
        "questions"
    )

    suspend fun refresh() {
        if (refreshing) return
        refreshing = true
        when (val result = withContext(Dispatchers.IO) { api.questionsInbox() }) {
            is ApiResult.Success -> {
                questions = result.value.distinctBy(ApiQuestion::id)
                error = null
            }
            is ApiResult.Failure -> error = result.message
        }
        loading = false
        refreshing = false
    }

    LaunchedEffect(Unit) {
        refresh()
        while (true) {
            delay(AUTO_REFRESH_INTERVAL_MS)
            refresh()
        }
    }

    val visibleQuestions = questions.filter {
        if (selectedFilter == "answered") it.answeredPost != null else it.answeredPost == null
    }
    Column(
        Modifier.fillMaxSize().background(Paper).pointerInput(selectedFilter) {
            var horizontalDistance = 0f
            detectHorizontalDragGestures(
                onDragStart = { horizontalDistance = 0f },
                onDragCancel = { horizontalDistance = 0f },
                onDragEnd = {
                    when {
                        horizontalDistance < -90f && selectedFilter == "unanswered" ->
                            selectedFilter = "answered"
                        horizontalDistance > 90f && selectedFilter == "answered" ->
                            selectedFilter = "unanswered"
                    }
                    horizontalDistance = 0f
                }
            ) { _, dragAmount -> horizontalDistance += dragAmount }
        }
    ) {
        OverlayHeader("質問箱", onBack)
        LazyColumn(contentPadding = PaddingValues(bottom = 126.dp)) {
            item {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        KText("INBOX", 10, Carrot, FontWeight.Black, letterSpacing = 2.2f)
                        Spacer(Modifier.height(6.dp))
                        KText("${questions.size}件の質問", 13, Muted)
                    }
                    Box(
                        Modifier.size(42.dp).background(Surface, RoundedCornerShape(14.dp))
                            .border(1.dp, Hairline, RoundedCornerShape(14.dp))
                            .clickable(enabled = !refreshing) { scope.launch { refresh() } },
                        contentAlignment = Alignment.Center
                    ) {
                        if (refreshing) KText("…", 16, Muted, FontWeight.Black)
                        else CustomIcon(IconType.REFRESH, Carrot, 18.dp)
                    }
                }
            }
            item {
                Row(
                    Modifier.padding(horizontal = 18.dp).fillMaxWidth()
                        .background(Surface, RoundedCornerShape(16.dp))
                        .border(1.dp, Hairline, RoundedCornerShape(16.dp)).padding(4.dp)
                ) {
                    listOf(
                        "unanswered" to "未回答 (${questions.count { it.answeredPost == null }})",
                        "answered" to "回答済み (${questions.count { it.answeredPost != null }})"
                    ).forEach { (value, label) ->
                        val selected = selectedFilter == value
                        val filterBackground by animateColorAsState(
                            if (selected) Strong else Color.Transparent,
                            tween(220),
                            label = "questionFilterBackground"
                        )
                        val filterText by animateColorAsState(
                            if (selected) OnStrong else Muted,
                            tween(220),
                            label = "questionFilterText"
                        )
                        Box(
                            Modifier.weight(1f).background(filterBackground, RoundedCornerShape(12.dp))
                                .clickable { selectedFilter = value }.padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            KText(label, 10, filterText, FontWeight.Black)
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
            }
            error?.let { message ->
                item {
                    Column(
                        Modifier.offset(x = questionTabMotion.shiftDp.dp)
                            .alpha(questionTabMotion.alpha)
                            .padding(horizontal = 22.dp, vertical = 8.dp)
                    ) {
                        ErrorText(message)
                    }
                }
            }
            if (loading && questions.isEmpty()) {
                items(4) {
                    Box(Modifier.offset(x = questionTabMotion.shiftDp.dp).alpha(questionTabMotion.alpha)) {
                        LoadingPost()
                    }
                }
            }
            items(visibleQuestions, key = ApiQuestion::id) { question ->
                Column(
                    Modifier.offset(x = questionTabMotion.shiftDp.dp)
                        .alpha(questionTabMotion.alpha)
                        .padding(horizontal = 18.dp, vertical = 6.dp).fillMaxWidth()
                        .background(Surface, RoundedCornerShape(20.dp))
                        .border(1.dp, Hairline, RoundedCornerShape(20.dp)).padding(15.dp)
                ) {
                    question.sender?.let { sender ->
                        Row(
                            Modifier.fillMaxWidth().clickable { onUser(sender) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                Modifier.size(40.dp).background(PaleCarrot, RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (sender.avatarUrl != null) {
                                    AsyncImage(
                                        sender.avatarUrl,
                                        sender.displayName,
                                        Modifier.fillMaxSize().clip(RoundedCornerShape(14.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    KText(sender.displayName.ifBlank { sender.username }.take(1), 14, Ink, FontWeight.Black)
                                }
                            }
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    KText(sender.displayName.ifBlank { sender.username }, 12, Ink, FontWeight.Black, maxLines = 1)
                                    AccountMarks(
                                        sender.officialMarks.ifEmpty { listOf(sender.officialMark) },
                                        sender.isBotAccount,
                                        sender.isParodyAccount,
                                        sender.isPrivate,
                                        compact = true
                                    )
                                }
                                KText("@${sender.username}", 9, Muted, maxLines = 1)
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    } ?: run {
                        KText("質問者不明", 9, Muted, FontWeight.Black)
                        Spacer(Modifier.height(10.dp))
                    }
                    KText(question.content, 14, Ink, lineHeight = 21f)
                    Spacer(Modifier.height(10.dp))
                    KText(absoluteTime(question.createdAt), 9, Muted)
                    val answered = question.answeredPost
                    if (answered == null) {
                        Spacer(Modifier.height(12.dp))
                        Box(
                            Modifier.background(Carrot, RoundedCornerShape(12.dp))
                                .clickable { onAnswer(question) }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CustomIcon(IconType.REPLY, Color.White, 14.dp)
                                Spacer(Modifier.width(7.dp))
                                KText("回答する", 10, Color.White, FontWeight.Black)
                            }
                        }
                    } else {
                        Spacer(Modifier.height(12.dp))
                        Box(
                            Modifier.background(PaleCarrot, RoundedCornerShape(12.dp))
                                .border(1.dp, Carrot.copy(.28f), RoundedCornerShape(12.dp))
                                .clickable { onPost(answered.toUiPost()) }
                                .padding(horizontal = 12.dp, vertical = 9.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CustomIcon(IconType.CHECK, Carrot, 14.dp)
                                Spacer(Modifier.width(7.dp))
                                KText("回答を見る", 10, Carrot, FontWeight.Black)
                            }
                        }
                    }
                }
            }
            if (!loading && error == null && visibleQuestions.isEmpty()) {
                item {
                    KText(
                        if (selectedFilter == "answered") "回答済みの質問はありません" else "未回答の質問はありません",
                        12,
                        Muted,
                        modifier = Modifier.offset(x = questionTabMotion.shiftDp.dp)
                            .alpha(questionTabMotion.alpha)
                            .padding(horizontal = 22.dp, vertical = 24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ScheduledPostsScreen(
    api: KarotterApi,
    onBack: () -> Unit
) {
    var posts by remember { mutableStateOf<List<ApiPost>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var refreshing by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var pendingDelete by remember { mutableStateOf<ApiPost?>(null) }
    var deletingPostId by remember { mutableStateOf<Long?>(null) }
    val scope = rememberCoroutineScope()

    suspend fun refresh(showLoading: Boolean) {
        if (refreshing) return
        refreshing = true
        if (showLoading && posts.isEmpty()) loading = true
        when (val result = withContext(Dispatchers.IO) { api.scheduledPosts() }) {
            is ApiResult.Success -> {
                posts = result.value.filterNot { it.id == deletingPostId }.distinctBy(ApiPost::id).sortedBy {
                    it.scheduledFor?.let(::parseTimestamp) ?: Instant.MAX
                }
                error = null
            }
            is ApiResult.Failure -> error = result.message
        }
        loading = false
        refreshing = false
    }

    LaunchedEffect(Unit) {
        refresh(showLoading = true)
        while (true) {
            delay(AUTO_REFRESH_INTERVAL_MS)
            refresh(showLoading = false)
        }
    }

    pendingDelete?.let { post ->
        Dialog(onDismissRequest = { if (deletingPostId == null) pendingDelete = null }) {
            Column(
                Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(22.dp))
                    .border(1.dp, Hairline, RoundedCornerShape(22.dp)).padding(20.dp)
            ) {
                KText("予約投稿を削除しますか？", 17, Ink, FontWeight.Black)
                Spacer(Modifier.height(7.dp))
                KText("削除した予約投稿は元に戻せません。", 11, Muted, lineHeight = 17f)
                Spacer(Modifier.height(18.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    Box(
                        Modifier.weight(1f).border(1.dp, Hairline, RoundedCornerShape(13.dp))
                            .clickable(enabled = deletingPostId == null) { pendingDelete = null }
                            .padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        KText("キャンセル", 10, Ink, FontWeight.Bold)
                    }
                    Box(
                        Modifier.weight(1f).background(Color(0xFFD64045), RoundedCornerShape(13.dp))
                            .clickable(enabled = deletingPostId == null) {
                                deletingPostId = post.id
                                error = null
                                scope.launch {
                                    when (val result = withContext(Dispatchers.IO) { api.deleteScheduledPost(post.id) }) {
                                        is ApiResult.Success -> {
                                            posts = posts.filterNot { it.id == post.id }
                                            pendingDelete = null
                                        }
                                        is ApiResult.Failure -> error = result.message
                                    }
                                    deletingPostId = null
                                }
                            }.padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        KText(if (deletingPostId == post.id) "削除中…" else "削除", 10, Color.White, FontWeight.Black)
                    }
                }
            }
        }
    }

    Column(Modifier.fillMaxSize().background(Paper)) {
        OverlayHeader("予約投稿", onBack)
        LazyColumn(contentPadding = PaddingValues(bottom = 126.dp)) {
            item {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        KText("SCHEDULED", 10, Carrot, FontWeight.Black, letterSpacing = 2.2f)
                        Spacer(Modifier.height(6.dp))
                        KText("指定した日時に公開される投稿", 13, Muted)
                    }
                    Box(
                        Modifier.size(42.dp).background(Surface, RoundedCornerShape(14.dp))
                            .border(1.dp, Hairline, RoundedCornerShape(14.dp))
                            .clickable(enabled = !refreshing) {
                                scope.launch { refresh(showLoading = false) }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (refreshing) KText("…", 16, Muted, FontWeight.Black)
                        else CustomIcon(IconType.REFRESH, Carrot, 18.dp)
                    }
                }
            }
            error?.let { message ->
                item {
                    Column(Modifier.padding(horizontal = 22.dp, vertical = 8.dp)) {
                        ErrorText(message)
                    }
                }
            }
            if (loading && posts.isEmpty()) {
                items(4) { LoadingPost() }
            }
            items(posts, key = ApiPost::id) { post ->
                Column(
                    Modifier.padding(horizontal = 18.dp, vertical = 6.dp).fillMaxWidth()
                        .background(Surface, RoundedCornerShape(20.dp))
                        .border(1.dp, Hairline, RoundedCornerShape(20.dp))
                        .padding(15.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(32.dp).background(PaleCarrot, RoundedCornerShape(11.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            CustomIcon(IconType.CALENDAR, Carrot, 16.dp)
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            KText("公開予定", 8, Muted, FontWeight.Black, letterSpacing = 1.1f)
                            KText(
                                post.scheduledFor?.let(::absoluteTime) ?: "日時未設定",
                                12,
                                Ink,
                                FontWeight.Black
                            )
                        }
                        Box(
                            Modifier.size(36.dp).background(Paper, RoundedCornerShape(12.dp))
                                .border(1.dp, Hairline, RoundedCornerShape(12.dp))
                                .clickable(enabled = deletingPostId == null) { pendingDelete = post },
                            contentAlignment = Alignment.Center
                        ) {
                            CustomIcon(IconType.TRASH, Color(0xFFD64045), 17.dp)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    KText(
                        post.content.ifBlank { if (post.media.isNotEmpty()) "メディア付き投稿" else "本文なし" },
                        13,
                        Ink,
                        lineHeight = 20f
                    )
                    post.media.firstOrNull()?.let { media ->
                        Spacer(Modifier.height(11.dp))
                        AsyncImage(
                            model = media.url,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(15.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            if (!loading && error == null && posts.isEmpty()) {
                item {
                    KText(
                        "予約投稿はありません",
                        12,
                        Muted,
                        modifier = Modifier.padding(horizontal = 22.dp, vertical = 20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BookmarksScreen(
    currentUser: ApiUser,
    api: KarotterApi,
    onBack: () -> Unit,
    onPost: (Post) -> Unit,
    onReply: (Post) -> Unit,
    onQuote: (Post) -> Unit,
    onRekarot: (Post, Boolean) -> Unit,
    onLike: (Post, Boolean) -> Unit,
    onBookmark: (Post, Boolean) -> Unit,
    onUser: (ApiUser) -> Unit
) {
    var posts by remember(currentUser.id) { mutableStateOf<List<Post>>(emptyList()) }
    var nextPage by remember(currentUser.id) { mutableIntStateOf(1) }
    var hasNext by remember(currentUser.id) { mutableStateOf(true) }
    var loading by remember(currentUser.id) { mutableStateOf(false) }
    var error by remember(currentUser.id) { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    suspend fun loadNext() {
        if (loading || !hasNext) return
        loading = true
        val requestedPage = nextPage
        when (val result = withContext(Dispatchers.IO) { api.userPosts(currentUser.id, requestedPage, null, "bookmarks") }) {
            is ApiResult.Success -> {
                val incoming = result.value.posts.map(ApiPost::toUiPost).distinctBy(::postStableKey)
                    .filter { next -> posts.none { postStableKey(it) == postStableKey(next) } }
                posts = (posts + incoming).distinctBy(::postStableKey)
                nextPage = result.value.nextPage ?: requestedPage + 1
                hasNext = result.value.hasNext && incoming.isNotEmpty()
                error = null
            }
            is ApiResult.Failure -> {
                error = result.message
                hasNext = false
            }
        }
        loading = false
    }
    LaunchedEffect(currentUser.id) {
        posts = emptyList()
        nextPage = 1
        hasNext = true
        error = null
        loadNext()
    }
    InfiniteLoadEffect(listState, posts.size, hasNext, loading) { scope.launch { loadNext() } }
    Column(Modifier.fillMaxSize().background(Paper)) {
        OverlayHeader("ブックマーク", onBack)
        LazyColumn(state = listState, contentPadding = PaddingValues(bottom = 126.dp)) {
            item {
                Column(Modifier.padding(horizontal = 22.dp, vertical = 18.dp)) {
                    KText("SAVED", 10, Carrot, FontWeight.Black, letterSpacing = 2.2f)
                    Spacer(Modifier.height(7.dp))
                    KText("あとで読み返したい投稿", 14, Muted)
                }
            }
            error?.let { message ->
                item {
                    Column(Modifier.padding(horizontal = 22.dp, vertical = 12.dp)) {
                        ErrorText(message)
                        Spacer(Modifier.height(10.dp))
                        Box(
                            Modifier.clip(RoundedCornerShape(13.dp)).background(Carrot).clickable {
                                hasNext = true
                                scope.launch { loadNext() }
                            }.padding(horizontal = 18.dp, vertical = 10.dp)
                        ) { KText("もう一度読み込む", 11, Color.White, FontWeight.Black) }
                    }
                }
            }
            items(
                items = posts,
                key = { post -> post.id ?: "bookmark:${post.authorId}:${post.createdAt}:${post.text.hashCode()}" }
            ) { post ->
                PostCard(
                    post,
                    onOpen = onPost,
                    onAuthor = { onUser(it.toApiUser()) },
                    onReply = onReply,
                    onQuote = onQuote,
                    onRekarot = { onRekarot(post, it) },
                    onLike = { onLike(post, it) },
                    onBookmark = { onBookmark(post, it) }
                )
            }
            if (loading) item { LoadingPost() }
            if (!loading && error == null && posts.isEmpty()) {
                item { KText("保存した投稿はまだありません", 12, Muted, modifier = Modifier.padding(22.dp)) }
            }
        }
    }
}

@Composable
private fun LevelRankingScreen(api: KarotterApi, onBack: () -> Unit, onUser: (ApiUser) -> Unit) {
    var entries by remember { mutableStateOf<List<ApiLevelRankingEntry>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var reloadKey by remember { mutableIntStateOf(0) }
    LaunchedEffect(reloadKey) {
        loading = true
        error = null
        when (val result = withContext(Dispatchers.IO) { api.levelRanking(100) }) {
            is ApiResult.Success -> entries = result.value.sortedBy { it.rank }
            is ApiResult.Failure -> error = result.message
        }
        loading = false
    }
    Column(Modifier.fillMaxSize().background(Paper)) {
        OverlayHeader("レベルランキング", onBack)
        LazyColumn(contentPadding = PaddingValues(bottom = 126.dp)) {
            item {
                Column(Modifier.padding(horizontal = 22.dp, vertical = 18.dp)) {
                    KText("TOP 100", 10, Carrot, FontWeight.Black, letterSpacing = 2.2f)
                    Spacer(Modifier.height(7.dp))
                    KText("Karotterで最もレベルの高いユーザー", 14, Muted, lineHeight = 21f)
                }
            }
            if (loading && entries.isEmpty()) {
                items(6) { LoadingPost() }
            }
            error?.let { message ->
                item {
                    Column(Modifier.padding(horizontal = 22.dp, vertical = 16.dp)) {
                        ErrorText(message)
                        Spacer(Modifier.height(12.dp))
                        Box(
                            Modifier.clip(RoundedCornerShape(13.dp)).background(Carrot)
                                .clickable { reloadKey += 1 }.padding(horizontal = 18.dp, vertical = 10.dp)
                        ) { KText("もう一度読み込む", 11, Color.White, FontWeight.Black) }
                    }
                }
            }
            items(entries, key = { "${it.rank}:${it.user.id}:${it.user.username}" }) { entry ->
                LevelRankingRow(entry) { onUser(entry.user) }
            }
        }
    }
}

@Composable
private fun LevelRankingRow(entry: ApiLevelRankingEntry, onClick: () -> Unit) {
    val user = entry.user
    val progress = ((user.levelProgressPercent ?: 0).coerceIn(0, 100)) / 100f
    val rankColor = when (entry.rank) {
        1 -> Carrot
        2 -> Color(0xFF8C9AA6)
        3 -> Color(0xFFB8784E)
        else -> Muted
    }
    Row(
        Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 18.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(38.dp).clip(RoundedCornerShape(13.dp))
                .background(if (entry.rank <= 3) rankColor else Surface)
                .border(1.dp, if (entry.rank <= 3) rankColor else Hairline, RoundedCornerShape(13.dp)),
            contentAlignment = Alignment.Center
        ) {
            KText(entry.rank.toString(), 12, if (entry.rank <= 3) Color.White else Ink, FontWeight.Black)
        }
        Spacer(Modifier.width(11.dp))
        Box(Modifier.size(48.dp).clip(RoundedCornerShape(16.dp)).background(PaleCarrot), contentAlignment = Alignment.Center) {
            if (user.avatarUrl != null) {
                AsyncImage(user.avatarUrl, user.displayName, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            } else {
                KText(user.displayName.ifBlank { user.username }.take(1), 16, Ink, FontWeight.Black)
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                KText(user.displayName.ifBlank { user.username }, 14, Ink, FontWeight.Black, maxLines = 1, modifier = Modifier.weight(1f, fill = false))
                AccountMarks(user.officialMarks.ifEmpty { listOf(user.officialMark) }, user.isBotAccount, user.isParodyAccount, user.isPrivate)
            }
            KText("@${user.username}", 10, Muted, maxLines = 1)
            Spacer(Modifier.height(7.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                KText("LEVEL ${user.level ?: 0}", 10, Ink, FontWeight.Black)
                Spacer(Modifier.width(8.dp))
                KText("${entry.experience} EXP", 9, Muted, FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                KText("${user.levelProgressPercent ?: 0}%", 9, Carrot, FontWeight.Black)
            }
            Spacer(Modifier.height(5.dp))
            Box(Modifier.fillMaxWidth().height(5.dp).clip(CircleShape).background(Hairline)) {
                Box(Modifier.fillMaxWidth(progress).fillMaxHeight().background(Carrot, CircleShape))
            }
            if (entry.experienceToNextLevel > 0) {
                Spacer(Modifier.height(4.dp))
                KText("次のレベルまで ${entry.experienceToNextLevel} EXP", 9, Muted)
            }
        }
        Spacer(Modifier.width(7.dp))
        CustomIcon(IconType.FORWARD, Muted, 16.dp)
    }
    Box(Modifier.padding(start = 119.dp).fillMaxWidth().height(1.dp).background(Hairline))
}

@Composable
private fun ProfileScreen(user: ApiUser?, api: KarotterApi, onLogout: () -> Unit, onPost: (Post) -> Unit, onReply: (Post) -> Unit, onQuote: (Post) -> Unit, onRekarot: (Post, Boolean) -> Unit, onLike: (Post, Boolean) -> Unit, onBookmark: (Post, Boolean) -> Unit) {
    if (user == null) { LoadingPost(); return }
    var detailedUser by remember(user.id) { mutableStateOf(user) }
    LaunchedEffect(user.id, user.username) {
        when (val result = withContext(Dispatchers.IO) { api.user(user.username) }) {
            is ApiResult.Success -> detailedUser = result.value
            is ApiResult.Failure -> Unit
        }
    }
    SharedProfilePage(detailedUser, true, api, null, null, onPost, onReply, onQuote, onRekarot, onLike, onBookmark)
}

@Composable
private fun ProfileMoreActionRow(
    icon: IconType,
    title: String,
    description: String,
    destructive: Boolean = false,
    active: Boolean = false,
    onClick: () -> Unit
) {
    val actionColor = when {
        destructive -> Color(0xFFD64045)
        active -> Carrot
        else -> Ink
    }
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp))
            .clickable(onClick = onClick).padding(horizontal = 10.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(38.dp).background(if (destructive || active) PaleCarrot else Paper, RoundedCornerShape(13.dp)),
            contentAlignment = Alignment.Center
        ) { CustomIcon(icon, actionColor, 18.dp) }
        Spacer(Modifier.width(11.dp))
        Column(Modifier.weight(1f)) {
            KText(title, 13, actionColor, FontWeight.Bold)
            Spacer(Modifier.height(2.dp))
            KText(description, 9, Muted)
        }
        CustomIcon(IconType.FORWARD, Muted, 15.dp)
    }
}

@Composable
private fun SendQuestionDialog(
    user: ApiUser,
    api: KarotterApi,
    onDismiss: () -> Unit
) {
    var content by remember(user.id) { mutableStateOf("") }
    var sending by remember(user.id) { mutableStateOf(false) }
    var error by remember(user.id) { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val remaining = 280 - content.length
    val canSend = content.trim().isNotEmpty() && remaining >= 0 && !sending

    Dialog(onDismissRequest = { if (!sending) onDismiss() }) {
        Column(
            Modifier.fillMaxWidth().background(Surface, RoundedCornerShape(24.dp))
                .border(1.dp, Hairline, RoundedCornerShape(24.dp))
                .imePadding().padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(38.dp).background(PaleCarrot, RoundedCornerShape(13.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    CustomIcon(IconType.QUESTION, Carrot, 18.dp)
                }
                Spacer(Modifier.width(11.dp))
                Column(Modifier.weight(1f)) {
                    KText("${user.displayName.ifBlank { user.username }}さんに質問", 16, Ink, FontWeight.Black, maxLines = 1)
                    KText("QUESTION BOX", 8, Muted, FontWeight.Black, letterSpacing = 1.3f)
                }
                Box(
                    Modifier.size(34.dp).border(1.dp, Hairline, CircleShape)
                        .clickable(enabled = !sending) { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    CustomIcon(IconType.CLOSE, Ink, 16.dp)
                }
            }
            Spacer(Modifier.height(16.dp))
            BasicTextField(
                value = content,
                onValueChange = { content = it.take(280) },
                textStyle = TextStyle(Ink, 14.sp, lineHeight = 21.sp),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
                modifier = Modifier.fillMaxWidth().heightIn(min = 140.dp)
                    .background(Paper, RoundedCornerShape(17.dp))
                    .border(1.dp, Hairline, RoundedCornerShape(17.dp))
                    .padding(14.dp),
                decorationBox = { inner ->
                    Box {
                        if (content.isEmpty()) KText("質問を入力", 13, Muted)
                        inner()
                    }
                }
            )
            Spacer(Modifier.height(9.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                KText("質問者名は相手に表示されます", 9, Muted)
                Spacer(Modifier.weight(1f))
                KText("${content.length} / 280", 10, if (remaining <= 20) Carrot else Muted, FontWeight.Black)
            }
            error?.let {
                Spacer(Modifier.height(9.dp))
                ErrorText(it)
            }
            Spacer(Modifier.height(16.dp))
            Box(
                Modifier.fillMaxWidth().background(if (canSend) Carrot else Hairline, RoundedCornerShape(15.dp))
                    .clickable(enabled = canSend) {
                        sending = true
                        error = null
                        scope.launch {
                            when (val result = withContext(Dispatchers.IO) { api.sendQuestion(user.username, content) }) {
                                is ApiResult.Success -> onDismiss()
                                is ApiResult.Failure -> error = result.message
                            }
                            sending = false
                        }
                    }.padding(vertical = 13.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    CustomIcon(IconType.SEND, Color.White, 15.dp)
                    KText(if (sending) "送信中…" else "質問を送る", 11, Color.White, FontWeight.Black)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SharedProfilePage(user: ApiUser, isOwn: Boolean, api: KarotterApi, onBack: (() -> Unit)?, onLogout: (() -> Unit)?, onPost: (Post) -> Unit, onReply: (Post) -> Unit, onQuote: (Post) -> Unit, onRekarot: (Post, Boolean) -> Unit, onLike: (Post, Boolean) -> Unit, onBookmark: (Post, Boolean) -> Unit, onCompose: (() -> Unit)? = null, onUser: ((ApiUser) -> Unit)? = null, viewerUserId: Long? = null, onDm: ((ApiUser) -> Unit)? = null, newOwnPost: Post? = null, onProfileReload: (() -> Unit)? = null, onEditProfile: (() -> Unit)? = null, retainedState: ProfilePageRetainedState? = null) {
    val bottomDockInset = bottomDockContentInset()
    val displayName = user.displayName.ifBlank { user.username }
    val username = user.username
    var pinnedProfilePosts by remember(user.id, user.pinnedPosts, user.pinnedPost?.id) {
        mutableStateOf((user.pinnedPosts + listOfNotNull(user.pinnedPost)).distinctBy { it.id }.map(ApiPost::toUiPost))
    }
    val profileAccent = user.profileAccentColor.toAppColor()?.takeIf {
        user.showProfileDecoration && user.subscriptionStatus.equals("ACTIVE", true)
    }
    var following by remember(user.id, user.isFollowing) { mutableStateOf(user.isFollowing) }
    var followRequestSent by remember(user.id, user.followRequestSent) { mutableStateOf(user.followRequestSent) }
    val privateProfileLocked = !isOwn && user.isPrivate && !following
    val ageRestrictedProfile = !isOwn &&
        user.profileUnavailableReason.equals("FILTERED", ignoreCase = true) &&
        user.profileUnavailableDetails.any { it.equals("MINOR_RESTRICTED", ignoreCase = true) }
    val profileContentLocked = privateProfileLocked || user.isBanned || ageRestrictedProfile
    val profileTabs = buildList {
        add("投稿" to "posts")
        add("返信" to "replies")
        add("メディア" to "media")
        if (isOwn || user.showLikedPosts || profileContentLocked) add("いいね" to "likes")
    }
    val localPageState = remember(user.id) { ProfilePageRetainedState() }
    val pageState = retainedState ?: localPageState
    var selectedKind by pageState.selectedKind
    val profileTabMotion = rememberHorizontalTabMotion(
        profileTabs.indexOfFirst { it.second == selectedKind }.coerceAtLeast(0),
        user.id
    )
    var connectionKind by remember(user.id) { mutableStateOf<String?>(null) }
    var userPosts by pageState.userPosts
    var nextPage by pageState.nextPage
    var nextCursor by pageState.nextCursor
    var hasNext by pageState.hasNext
    var loadingMore by pageState.loadingMore
    var refreshingProfile by pageState.refreshing
    var followBusy by remember(user.id) { mutableStateOf(false) }
    var confirmUnfollow by remember(user.id) { mutableStateOf(false) }
    var muted by remember(user.id, user.isMuted) { mutableStateOf(user.isMuted) }
    var blocked by remember(user.id, user.isBlocked) { mutableStateOf(user.isBlocked) }
    var safetyBusy by remember(user.id) { mutableStateOf(false) }
    var pendingSafetyAction by remember(user.id) { mutableStateOf<Pair<String, Boolean>?>(null) }
    var safetyError by remember(user.id) { mutableStateOf<String?>(null) }
    var postNotificationsEnabled by remember(user.id, user.isPostNotificationsEnabled) {
        mutableStateOf(user.isPostNotificationsEnabled)
    }
    var notificationBusy by remember(user.id) { mutableStateOf(false) }
    var pendingNotificationChange by remember(user.id) { mutableStateOf<Boolean?>(null) }
    var notificationError by remember(user.id) { mutableStateOf<String?>(null) }
    var questionDialogOpen by remember(user.id) { mutableStateOf(false) }
    var profileMoreOpen by remember(user.id) { mutableStateOf(false) }
    var visibleFollowers by remember(user.id) { mutableIntStateOf(user.followersCount) }
    val listState = pageState.listState
    val profileScope = rememberCoroutineScope()
    val profileActionSize = 36.dp
    suspend fun loadPage() {
        if (loadingMore || !hasNext) return
        loadingMore = true
        val requestedKind = selectedKind
        val requestedPage = nextPage
        val requestedCursor = nextCursor
        when (val result = withContext(Dispatchers.IO) { api.userPosts(user.id, requestedPage, requestedCursor, requestedKind) }) {
            is ApiResult.Success -> {
                if (selectedKind == requestedKind) {
                    val incoming = result.value.posts.map(ApiPost::toUiPost).distinctBy(::postStableKey)
                        .filter { p -> userPosts.none { postStableKey(it) == postStableKey(p) } }
                    userPosts = (userPosts + incoming).distinctBy(::postStableKey)
                    nextPage = result.value.nextPage ?: (requestedPage + 1)
                    nextCursor = result.value.nextCursor
                    hasNext = result.value.hasNext && incoming.isNotEmpty()
                }
            }
            is ApiResult.Failure -> if (selectedKind == requestedKind) hasNext = false
        }
        if (selectedKind == requestedKind) loadingMore = false
    }
    suspend fun refreshPage() {
        if (refreshingProfile || loadingMore) return
        refreshingProfile = true
        val requestedKind = selectedKind
        when (val result = withContext(Dispatchers.IO) { api.userPosts(user.id, 1, null, requestedKind) }) {
            is ApiResult.Success -> if (selectedKind == requestedKind) {
                val incoming = result.value.posts.map(ApiPost::toUiPost)
                val existingIds = userPosts.mapNotNullTo(hashSetOf()) { it.id }
                val additions = incoming.filter { it.id == null || it.id !in existingIds }
                if (additions.isNotEmpty()) userPosts = (additions + userPosts).distinctBy(::postStableKey)
            }
            is ApiResult.Failure -> Unit
        }
        if (selectedKind == requestedKind) refreshingProfile = false
    }
    fun changeFollow(desired: Boolean) {
        if (followBusy) return
        val wasFollowing = following
        followBusy = true
        profileScope.launch {
            when (val followResult = withContext(Dispatchers.IO) { api.follow(user.id, desired) }) {
                is ApiResult.Success -> {
                    val pending = desired && (user.isPrivate || followResult.value.contains("申請"))
                    following = desired && !pending
                    followRequestSent = pending
                    if (!pending && desired) visibleFollowers += 1
                    else if (!desired && wasFollowing) visibleFollowers = (visibleFollowers - 1).coerceAtLeast(0)
                }
                is ApiResult.Failure -> Unit
            }
            followBusy = false
        }
    }
    fun changeSafetySetting(kind: String, desired: Boolean) {
        if (safetyBusy) return
        safetyBusy = true
        safetyError = null
        profileScope.launch {
            val result = withContext(Dispatchers.IO) {
                if (kind == "mute") api.mute(user.id, desired) else api.block(user.id, desired)
            }
            when (result) {
                is ApiResult.Success -> {
                    if (kind == "mute") {
                        muted = desired
                    } else {
                        blocked = desired
                        if (desired) {
                            if (following) visibleFollowers = (visibleFollowers - 1).coerceAtLeast(0)
                            following = false
                            followRequestSent = false
                        }
                    }
                }
                is ApiResult.Failure -> safetyError = result.message
            }
            safetyBusy = false
        }
    }
    fun changePostNotifications(desired: Boolean) {
        if (notificationBusy) return
        notificationBusy = true
        notificationError = null
        profileScope.launch {
            when (val result = withContext(Dispatchers.IO) { api.postNotifications(user.id, desired) }) {
                is ApiResult.Success -> postNotificationsEnabled = desired
                is ApiResult.Failure -> notificationError = result.message
            }
            notificationBusy = false
        }
    }
    LaunchedEffect(user.id, selectedKind, profileContentLocked) {
        if (pageState.loadedKind.value == selectedKind) return@LaunchedEffect
        userPosts = emptyList(); nextPage = 1; nextCursor = null; hasNext = true; loadingMore = false
        pageState.loadedKind.value = selectedKind
        listState.scrollToItem(0)
        if (profileContentLocked) {
            hasNext = false
            return@LaunchedEffect
        }
        loadPage()
    }
    LaunchedEffect(user.id, selectedKind, profileContentLocked) {
        if (profileContentLocked) return@LaunchedEffect
        while (true) {
            delay(AUTO_REFRESH_INTERVAL_MS)
            refreshPage()
        }
    }
    LaunchedEffect(newOwnPost?.id) {
        val created = newOwnPost ?: return@LaunchedEffect
        if (isOwn && selectedKind == "posts" && created.authorId == user.id && userPosts.none { it.id == created.id }) {
            userPosts = listOf(created) + userPosts
        }
    }
    InfiniteLoadEffect(listState, userPosts.size, hasNext, loadingMore) { profileScope.launch { loadPage() } }
    connectionKind?.let { kind ->
        ProfileConnectionsScreen(
            profileUser = user,
            kind = kind,
            locked = profileContentLocked,
            showMutualFollowers = !isOwn,
            api = api,
            onBack = { connectionKind = null },
            onUser = { selected -> onUser?.invoke(selected) }
        )
        return
    }
    if (confirmUnfollow) {
        Dialog(onDismissRequest = { confirmUnfollow = false }) {
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Surface).border(1.dp, Hairline, RoundedCornerShape(22.dp)).padding(21.dp)) {
                KText("フォローを解除しますか？", 17, Ink, FontWeight.Black)
                Spacer(Modifier.height(7.dp))
                KText("${displayName}さんの投稿は、フォロー中タイムラインに表示されなくなります。", 12, Muted, lineHeight = 18f)
                Spacer(Modifier.height(18.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    Box(Modifier.weight(1f).clip(RoundedCornerShape(13.dp)).border(1.dp, Hairline, RoundedCornerShape(13.dp)).clickable { confirmUnfollow = false }.padding(vertical = 11.dp), contentAlignment = Alignment.Center) {
                        KText("キャンセル", 11, Ink, FontWeight.Bold)
                    }
                    Box(Modifier.weight(1f).clip(RoundedCornerShape(13.dp)).background(Carrot).clickable {
                        confirmUnfollow = false
                        changeFollow(false)
                    }.padding(vertical = 11.dp), contentAlignment = Alignment.Center) {
                        KText("フォロー解除", 11, Color.White, FontWeight.Black)
                    }
                }
            }
        }
    }
    if (questionDialogOpen) {
        SendQuestionDialog(
            user = user,
            api = api,
            onDismiss = { questionDialogOpen = false }
        )
    }
    if (profileMoreOpen) {
        val systemNavigationClearance =
            WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 36.dp
        Dialog(
            onDismissRequest = { profileMoreOpen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                Box(Modifier.fillMaxSize().clickable { profileMoreOpen = false })
                Column(
                    Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 27.dp, topEnd = 27.dp))
                        .background(Surface)
                        .border(1.dp, Hairline, RoundedCornerShape(topStart = 27.dp, topEnd = 27.dp))
                        .clickable { }
                        .padding(start = 18.dp, top = 10.dp, end = 18.dp, bottom = systemNavigationClearance)
                ) {
                    Box(
                        Modifier.width(38.dp).height(4.dp).background(Hairline, RoundedCornerShape(3.dp))
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            KText("@${user.username}", 16, Ink, FontWeight.Black)
                            KText("その他の操作", 10, Muted)
                        }
                        Box(
                            Modifier.size(34.dp).border(1.dp, Hairline, CircleShape)
                                .clickable { profileMoreOpen = false },
                            contentAlignment = Alignment.Center
                        ) { CustomIcon(IconType.CLOSE, Ink, 16.dp) }
                    }
                    Spacer(Modifier.height(14.dp))
                    if (!blocked && user.questionsEnabled) {
                        ProfileMoreActionRow(
                            icon = IconType.QUESTION,
                            title = "質問箱",
                            description = "${displayName}さんへ質問を送る"
                        ) {
                            profileMoreOpen = false
                            questionDialogOpen = true
                        }
                    }
                    ProfileMoreActionRow(
                        icon = IconType.VOLUME_OFF,
                        title = if (muted) "ミュートを解除" else "ミュート",
                        description = if (muted) "このユーザーの投稿を再び表示" else "このユーザーの投稿をタイムラインに表示しない",
                        active = muted
                    ) {
                        profileMoreOpen = false
                        pendingSafetyAction = "mute" to !muted
                    }
                    ProfileMoreActionRow(
                        icon = IconType.BLOCK,
                        title = if (blocked) "ブロックを解除" else "ブロック",
                        description = if (blocked) "このユーザーとの操作制限を解除" else "フォロー関係を解除して操作を制限",
                        destructive = !blocked,
                        active = blocked
                    ) {
                        profileMoreOpen = false
                        pendingSafetyAction = "block" to !blocked
                    }
                }
            }
        }
    }
    pendingSafetyAction?.let { (kind, desired) ->
        val isMuteAction = kind == "mute"
        val actionName = if (isMuteAction) "ミュート" else "ブロック"
        Dialog(onDismissRequest = { pendingSafetyAction = null }) {
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Surface)
                    .border(1.dp, Hairline, RoundedCornerShape(22.dp)).padding(21.dp)
            ) {
                KText(
                    if (desired) "${displayName}さんを${actionName}しますか？" else "${actionName}を解除しますか？",
                    17, Ink, FontWeight.Black
                )
                Spacer(Modifier.height(7.dp))
                KText(
                    when {
                        isMuteAction && desired -> "このユーザーの投稿をタイムラインに表示しないようにします。相手には通知されません。"
                        isMuteAction -> "このユーザーの投稿が再びタイムラインに表示されるようになります。"
                        desired -> "フォロー関係が解除され、互いの操作が制限されます。"
                        else -> "このユーザーとの操作制限を解除します。"
                    },
                    12, Muted, lineHeight = 18f
                )
                Spacer(Modifier.height(18.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    Box(
                        Modifier.weight(1f).clip(RoundedCornerShape(13.dp))
                            .border(1.dp, Hairline, RoundedCornerShape(13.dp))
                            .clickable { pendingSafetyAction = null }.padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        KText("キャンセル", 11, Ink, FontWeight.Bold)
                    }
                    Box(
                        Modifier.weight(1f).clip(RoundedCornerShape(13.dp))
                            .background(if (!isMuteAction && desired) Color(0xFFD64045) else Carrot)
                            .clickable {
                                pendingSafetyAction = null
                                changeSafetySetting(kind, desired)
                            }.padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        KText(if (desired) actionName else "${actionName}解除", 11, Color.White, FontWeight.Black)
                    }
                }
            }
        }
    }
    pendingNotificationChange?.let { desired ->
        Dialog(onDismissRequest = { if (!notificationBusy) pendingNotificationChange = null }) {
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(22.dp)).background(Surface)
                    .border(1.dp, Hairline, RoundedCornerShape(22.dp)).padding(21.dp)
            ) {
                KText(if (desired) "投稿通知をオンにしますか？" else "投稿通知をオフにしますか？", 17, Ink, FontWeight.Black)
                Spacer(Modifier.height(7.dp))
                KText(
                    if (desired) "${displayName}さんが投稿したときに通知を受け取ります。"
                    else "${displayName}さんの新しい投稿を通知しないようにします。",
                    12,
                    Muted,
                    lineHeight = 18f
                )
                Spacer(Modifier.height(18.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    Box(
                        Modifier.weight(1f).clip(RoundedCornerShape(13.dp))
                            .border(1.dp, Hairline, RoundedCornerShape(13.dp))
                            .clickable(enabled = !notificationBusy) { pendingNotificationChange = null }
                            .padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        KText("キャンセル", 11, Ink, FontWeight.Bold)
                    }
                    Box(
                        Modifier.weight(1f).clip(RoundedCornerShape(13.dp)).background(Carrot)
                            .clickable(enabled = !notificationBusy) {
                                changePostNotifications(desired)
                                pendingNotificationChange = null
                            }.padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        KText(if (desired) "オンにする" else "オフにする", 11, Color.White, FontWeight.Black)
                    }
                }
            }
        }
    }
    CompositionLocalProvider(
        LocalPostMenuResultHandler provides { action, affectedPost ->
            if (isOwn && affectedPost.authorId == user.id) {
                when (action) {
                    PostMenuAction.EDIT -> {
                        userPosts = userPosts.map { if (it.id == affectedPost.id) affectedPost else it }
                        pinnedProfilePosts = pinnedProfilePosts.map { if (it.id == affectedPost.id) affectedPost else it }
                    }
                    PostMenuAction.DELETE -> {
                        userPosts = userPosts.filterNot { it.id == affectedPost.id }
                        pinnedProfilePosts = pinnedProfilePosts.filterNot { it.id == affectedPost.id }
                    }
                    PostMenuAction.PIN -> pinnedProfilePosts = listOf(affectedPost)
                    else -> Unit
                }
                onProfileReload?.invoke()
            }
        }
    ) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Paper)
            .pointerInput(user.id, selectedKind) {
                var horizontalDistance = 0f
                detectHorizontalDragGestures(
                    onDragStart = { horizontalDistance = 0f },
                    onDragCancel = { horizontalDistance = 0f },
                    onDragEnd = {
                        val index = profileTabs.indexOfFirst { it.second == selectedKind }.coerceAtLeast(0)
                        when {
                            horizontalDistance < -90f && index < profileTabs.lastIndex -> selectedKind = profileTabs[index + 1].second
                            horizontalDistance > 90f && index > 0 -> selectedKind = profileTabs[index - 1].second
                        }
                        horizontalDistance = 0f
                    }
                ) { _, dragAmount -> horizontalDistance += dragAmount }
            }
    ) {
    Column(Modifier.fillMaxSize()) {
    if (onBack != null) OverlayHeader(displayName, onBack)
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(
            bottom = when {
                onCompose != null -> bottomDockInset + 142.dp
                onProfileReload != null -> bottomDockInset + 82.dp
                else -> bottomDockInset + 24.dp
            }
        )
    ) {
        item {
            Box(Modifier.fillMaxWidth().height(214.dp).background(profileAccent ?: Strong)) {
                if (user.headerUrl != null) {
                    AsyncImage(user.headerUrl, "プロフィールヘッダー", Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    Box(Modifier.fillMaxSize().background(Strong.copy(.22f)))
                } else {
                    Canvas(Modifier.fillMaxSize()) {
                        drawCircle(Carrot, size.width * .31f, Offset(size.width * .9f, size.height * .05f))
                        drawCircle(Color.White.copy(.08f), size.width * .22f, Offset(size.width * .12f, size.height * .8f))
                    }
                }
            }
            Column(Modifier.padding(horizontal = 22.dp).offset(y = (-38).dp)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Box(
                        Modifier.size(84.dp).clip(RoundedCornerShape(30.dp))
                            .background(profileAccent ?: Paper).padding(5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            Modifier.fillMaxSize().clip(RoundedCornerShape(25.dp)).background(PaleCarrot),
                            contentAlignment = Alignment.Center
                        ) {
                            if (user.avatarUrl != null) AsyncImage(user.avatarUrl, displayName, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                            else KText(displayName.take(1), 28, Ink, FontWeight.Black)
                        }
                    }
                    Spacer(Modifier.weight(1f))
                    if (isOwn && onEditProfile != null) {
                        Box(
                            Modifier
                                .height(profileActionSize)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Surface)
                                .border(1.dp, Hairline, RoundedCornerShape(14.dp))
                                .clickable { onEditProfile() }
                                .padding(horizontal = 13.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                CustomIcon(IconType.PROFILE_EDIT, Ink, 15.dp)
                                KText("プロフィールを編集", 10, Ink, FontWeight.Black)
                            }
                        }
                    } else if (!isOwn && user.id != viewerUserId) {
                        Box(
                            Modifier.size(profileActionSize).clip(RoundedCornerShape(12.dp))
                                .background(if (muted || blocked) PaleCarrot else Surface)
                                .border(1.dp, if (muted || blocked) Carrot.copy(.55f) else Hairline, RoundedCornerShape(12.dp))
                                .clickable { profileMoreOpen = true },
                            contentAlignment = Alignment.Center
                        ) {
                            CustomIcon(IconType.MORE, if (muted || blocked) Carrot else Ink, 17.dp)
                        }
                        Spacer(Modifier.width(5.dp))
                        Box(
                            Modifier.size(profileActionSize).clip(RoundedCornerShape(12.dp))
                                .background(if (postNotificationsEnabled) PaleCarrot else Surface)
                                .border(
                                    1.dp,
                                    if (postNotificationsEnabled) Carrot.copy(.55f) else Hairline,
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable(enabled = !notificationBusy && !blocked) {
                                    pendingNotificationChange = !postNotificationsEnabled
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            CustomIcon(IconType.BELL, if (postNotificationsEnabled) Carrot else Ink, 16.dp)
                        }
                        Spacer(Modifier.width(5.dp))
                        if (!blocked && user.canReceiveDm && onDm != null) {
                            Box(
                                Modifier.size(profileActionSize).clip(RoundedCornerShape(12.dp))
                                    .background(Surface).border(1.dp, Hairline, RoundedCornerShape(12.dp))
                                    .clickable { onDm(user) },
                                contentAlignment = Alignment.Center
                            ) {
                                CustomIcon(IconType.DM, Ink, 16.dp)
                            }
                            Spacer(Modifier.width(5.dp))
                        }
                        Box(
                            Modifier
                                .height(profileActionSize)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (following) Surface else Strong)
                                .border(1.dp, if (following) Hairline else Strong, RoundedCornerShape(14.dp))
                                .clickable(enabled = !followBusy && !blocked) {
                                    if (following) confirmUnfollow = true
                                    else changeFollow(!followRequestSent)
                                }
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            KText(
                                if (blocked) "ブロック中"
                                else if (followBusy) "…"
                                else if (following) "フォロー中"
                                else if (followRequestSent) "申請済み"
                                else if (user.isPrivate) "フォローリクエスト"
                                else "フォロー",
                                10,
                                if (following) Ink else OnStrong,
                                FontWeight.Black
                            )
                        }
                    }
                }
                Spacer(Modifier.height(13.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    KText(displayName, 25, Ink, FontWeight.Black, maxLines = 1, modifier = Modifier.weight(1f, fill = false))
                    AccountMarks(user.officialMarks.ifEmpty { listOf(user.officialMark) }, user.isBotAccount, user.isParodyAccount, user.isPrivate)
                    SubscriptionBadge(
                        user.subscriptionPlan,
                        user.subscriptionStatus,
                        user.showSubscriptionBadges && if (user.subscriptionPlan.equals("PLUS", true)) user.showPlusBadge else user.showProBadge,
                        user.premiumBadgeColor
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    KText("@$username", 12, Muted)
                    if (!isOwn && user.isFollowedBy) {
                        Spacer(Modifier.width(8.dp))
                        Box(
                            Modifier.background(Surface, RoundedCornerShape(7.dp))
                                .border(1.dp, Hairline, RoundedCornerShape(7.dp))
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            KText("フォローされています", 8, Muted, FontWeight.Bold)
                        }
                    }
                }
                if (user.isBanned) {
                    Spacer(Modifier.height(10.dp))
                    Row(
                        Modifier.fillMaxWidth().background(Color(0xFFD64045).copy(.12f), RoundedCornerShape(13.dp))
                            .border(1.dp, Color(0xFFD64045).copy(.35f), RoundedCornerShape(13.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomIcon(IconType.BLOCK, Color(0xFFD64045), 17.dp)
                        Spacer(Modifier.width(8.dp))
                        KText("このアカウントはBANされています", 11, Color(0xFFD64045), FontWeight.Black, maxLines = 1)
                    }
                }
                if (ageRestrictedProfile) {
                    Spacer(Modifier.height(10.dp))
                    Row(
                        Modifier.fillMaxWidth()
                            .background(Color(0xFFF08A24).copy(.12f), RoundedCornerShape(13.dp))
                            .border(1.dp, Color(0xFFF08A24).copy(.42f), RoundedCornerShape(13.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomIcon(IconType.LOCK, Color(0xFFE27612), 17.dp)
                        Spacer(Modifier.width(8.dp))
                        KText(
                            "年齢制限により投稿を表示できません",
                            11,
                            Color(0xFFE27612),
                            FontWeight.Black
                        )
                    }
                }
                if (!isOwn && user.id != viewerUserId) {
                    (safetyError ?: notificationError)?.let {
                        Spacer(Modifier.height(7.dp))
                        KText(it, 10, Color(0xFFD64045), FontWeight.Medium)
                    }
                }
                val hasVisibleStatus = user.onlineStatus.isNotBlank() && !user.onlineStatus.equals("null", true)
                if (hasVisibleStatus || user.level != null) {
                    Spacer(Modifier.height(9.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        user.level?.let { level ->
                            ProfileLevelProgress(level, user.levelProgressPercent ?: 0)
                        }
                        if (hasVisibleStatus) {
                            ProfileStatusBadge(user.onlineStatus, user.statusMessage, Modifier.weight(1f))
                        }
                    }
                }
                Spacer(Modifier.height(14.dp))
                RichContentText(user.bio.ifBlank { "まだ自己紹介はありません。" }.replace("\r\n", "\n").replace('\r', '\n').replace("\n", "  \n"), 14, Ink, lineHeight = 21f)
                user.websiteUrl?.takeIf { it.isNotBlank() }?.let {
                    Spacer(Modifier.height(9.dp))
                    Row(verticalAlignment = Alignment.Top) {
                        CustomIcon(IconType.LINK, Muted, 14.dp)
                        Spacer(Modifier.width(7.dp))
                        RichContentText(it, 12, Carrot, modifier = Modifier.weight(1f))
                    }
                }
                if (user.location.isNotBlank()) {
                    Spacer(Modifier.height(9.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CustomIcon(IconType.MAP_PIN, Muted, 14.dp)
                        Spacer(Modifier.width(7.dp))
                        KText(user.location, 11, Muted, FontWeight.Medium)
                    }
                }
                user.joinedAt?.takeIf { it.isNotBlank() }?.let {
                    Spacer(Modifier.height(9.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CustomIcon(IconType.CALENDAR, Muted, 14.dp)
                        Spacer(Modifier.width(6.dp))
                        KText("参加日  ${joinedDate(it)}", 11, Muted, FontWeight.Medium)
                    }
                }
                Spacer(Modifier.height(17.dp))
                Row {
                    Stat(
                        user.followingCount.toString(),
                        "フォロー",
                        if (profileContentLocked) null else ({ connectionKind = "following" })
                    )
                    Spacer(Modifier.width(26.dp))
                    Stat(
                        visibleFollowers.toString(),
                        "フォロワー",
                        if (profileContentLocked) null else ({ connectionKind = "followers" })
                    )
                    Spacer(Modifier.width(26.dp))
                    Stat(user.postsCount.toString(), "投稿")
                }
            }
        }
        stickyHeader(key = "profile-tabs") {
            Row(Modifier.fillMaxWidth().background(Surface)) {
                profileTabs.forEach { (label, kind) ->
                    val indicatorWidth by animateDpAsState(
                        if (selectedKind == kind) 34.dp else 0.dp,
                        tween(260, easing = FastOutSlowInEasing),
                        label = "profileTabIndicator"
                    )
                    Column(Modifier.weight(1f).clickable { selectedKind = kind }.padding(top = 12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        KText(label, 11, if (selectedKind == kind) Ink else Muted, FontWeight.Bold, maxLines = 1)
                        Spacer(Modifier.height(10.dp))
                        Box(Modifier.width(indicatorWidth).height(3.dp).background(if (selectedKind == kind) Carrot else Color.Transparent, RoundedCornerShape(2.dp)))
                    }
                }
            }
            Box(Modifier.fillMaxWidth().height(1.dp).background(Hairline))
        }
        if (profileContentLocked) {
            item(key = "profile-restricted-notice-$selectedKind") {
                val restrictionColor = when {
                    user.isBanned -> Color(0xFFD64045)
                    ageRestrictedProfile -> Color(0xFFE27612)
                    else -> Carrot
                }
                Column(
                    Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 36.dp)
                        .background(
                            if (user.isBanned || ageRestrictedProfile) restrictionColor.copy(.08f) else Surface,
                            RoundedCornerShape(22.dp)
                        )
                        .border(
                            1.dp,
                            if (user.isBanned || ageRestrictedProfile) restrictionColor.copy(.38f) else Hairline,
                            RoundedCornerShape(22.dp)
                        )
                        .padding(horizontal = 22.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        Modifier.size(48.dp).background(
                            if (user.isBanned || ageRestrictedProfile) restrictionColor.copy(.14f) else PaleCarrot,
                            RoundedCornerShape(16.dp)
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomIcon(
                            if (user.isBanned) IconType.BLOCK else IconType.LOCK,
                            restrictionColor,
                            22.dp
                        )
                    }
                    Spacer(Modifier.height(14.dp))
                    KText(
                        when {
                            user.isBanned -> "このアカウントはBANされています"
                            ageRestrictedProfile -> "年齢制限のあるプロフィールです"
                            else -> "このアカウントは非公開です"
                        },
                        15,
                        if (user.isBanned || ageRestrictedProfile) restrictionColor else Ink,
                        FontWeight.Black
                    )
                    Spacer(Modifier.height(7.dp))
                    KText(
                        when {
                            user.isBanned ->
                                "このアカウントは利用を制限されているため、\n投稿やアクティビティを表示できません。"
                            ageRestrictedProfile ->
                                "相手が設定した年齢条件を満たしていないため、\n投稿、返信、メディア、いいねを表示できません。"
                            else ->
                                "投稿、返信、メディア、いいねを見るには\nフォローリクエストを送信してください。"
                        },
                        11,
                        Muted,
                        FontWeight.Medium,
                        lineHeight = 18f,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        if (!profileContentLocked && selectedKind == "posts" && pinnedProfilePosts.isNotEmpty()) {
            items(pinnedProfilePosts, key = { "pinned:${postStableKey(it)}" }) { pinned ->
                Column(
                    Modifier.offset(x = profileTabMotion.shiftDp.dp)
                        .alpha(profileTabMotion.alpha)
                ) {
                    Row(
                        Modifier.fillMaxWidth().background(PaleCarrot.copy(.42f)).padding(horizontal = 22.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomIcon(IconType.PIN, Carrot, 14.dp)
                        Spacer(Modifier.width(8.dp))
                        KText("固定された投稿", 10, Carrot, FontWeight.Black)
                    }
                    PostCard(
                        pinned,
                        onOpen = onPost,
                        onAuthor = onUser?.let { openUser -> { authorPost -> if (authorPost.authorId != user.id) openUser(authorPost.toApiUser()) } },
                        onReply = onReply,
                        onQuote = onQuote,
                        onRekarot = { onRekarot(pinned, it) },
                        onLike = { onLike(pinned, it) },
                        onBookmark = { onBookmark(pinned, it) }
                    )
                }
            }
        }
        if (!profileContentLocked && selectedKind == "media") {
            val gallery = userPosts.distinctBy(::postStableKey).flatMap { post ->
                post.media.filter { media ->
                    media.type.startsWith("image/", true) ||
                        Regex("\\.(jpg|jpeg|png|webp|gif|avif)$").containsMatchIn(media.url.substringBefore('?').lowercase())
                }.map { media -> post to media }
            }.distinctBy { (post, media) -> "${postStableKey(post)}:${media.url}" }
            items(gallery.chunked(3), key = { row -> row.joinToString("|") { "${postStableKey(it.first)}:${it.second.url}" } }) { row ->
                Row(
                    Modifier.fillMaxWidth().height(126.dp)
                        .offset(x = profileTabMotion.shiftDp.dp)
                        .alpha(profileTabMotion.alpha)
                ) {
                    row.forEach { (post, media) ->
                        SpoilerAwareMediaThumbnail(
                            media = media,
                            modifier = Modifier.weight(1f).fillMaxHeight().padding(1.dp),
                            onOpen = {
                                    profileScope.launch {
                                        val opened = post.id?.let { postId ->
                                            when (val result = withContext(Dispatchers.IO) { api.post(postId) }) {
                                                is ApiResult.Success -> result.value.toUiPost()
                                                is ApiResult.Failure -> null
                                            }
                                        } ?: post
                                        onPost(opened)
                                    }
                                }
                        )
                    }
                    repeat(3 - row.size) { Spacer(Modifier.weight(1f).fillMaxHeight().padding(1.dp)) }
                }
            }
            if (!loadingMore && gallery.isEmpty()) {
                item { KText("表示できる画像がありません", 12, Muted, modifier = Modifier.padding(22.dp)) }
            }
        } else if (!profileContentLocked) {
            items(userPosts.distinctBy(::postStableKey), key = ::postStableKey) { post ->
                Box(
                    Modifier.offset(x = profileTabMotion.shiftDp.dp)
                        .alpha(profileTabMotion.alpha)
                ) {
                    PostCard(
                        post,
                        onOpen = onPost,
                        onAuthor = onUser?.let { openUser -> { authorPost -> if (authorPost.authorId != user.id) openUser(authorPost.toApiUser()) } },
                        onReply = onReply,
                        onQuote = onQuote,
                        onRekarot = { onRekarot(post, it) },
                        onLike = { onLike(post, it) },
                        onBookmark = { onBookmark(post, it) }
                    )
                }
            }
        }
        if (!profileContentLocked && loadingMore) item {
            Box(Modifier.offset(x = profileTabMotion.shiftDp.dp).alpha(profileTabMotion.alpha)) {
                LoadingPost()
            }
        }
        if (!profileContentLocked && selectedKind != "media" && !loadingMore && userPosts.isEmpty()) item { KText("このタブにはまだ表示できる投稿がありません", 12, Muted, modifier = Modifier.padding(22.dp)) }
    }
    }
    if (onProfileReload != null || onCompose != null) {
        Column(
            Modifier.align(Alignment.BottomEnd).padding(end = 20.dp, bottom = bottomDockInset + 18.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                Modifier.size(45.dp).background(Surface, RoundedCornerShape(16.dp)).border(1.dp, Hairline, RoundedCornerShape(16.dp)).clickable(enabled = !refreshingProfile && !loadingMore) {
                    onProfileReload?.invoke()
                    profileScope.launch { refreshPage() }
                },
                contentAlignment = Alignment.Center
            ) {
                if (refreshingProfile) KText("…", 21, Muted, FontWeight.Bold)
                else CustomIcon(IconType.REFRESH, Carrot, 20.dp)
            }
            if (onCompose != null) {
                Box(
                    Modifier.size(58.dp).background(Carrot, RoundedCornerShape(20.dp)).clickable { onCompose() },
                    contentAlignment = Alignment.Center
                ) { CustomIcon(IconType.PLUS, Color.White, 25.dp) }
            }
        }
    }
    }
}
}

@Composable
private fun ProfileConnectionsScreen(
    profileUser: ApiUser,
    kind: String,
    locked: Boolean,
    showMutualFollowers: Boolean,
    api: KarotterApi,
    onBack: () -> Unit,
    onUser: (ApiUser) -> Unit
) {
    if (locked) {
        Column(Modifier.fillMaxSize().background(Paper)) {
            OverlayHeader("${profileUser.displayName.ifBlank { profileUser.username }}のつながり", onBack)
            Column(
                Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomIcon(IconType.LOCK, Muted, 32.dp)
                Spacer(Modifier.height(12.dp))
                KText("このアカウントのフォロー情報は非公開です", 13, Ink, FontWeight.Black)
                Spacer(Modifier.height(6.dp))
                KText("フォローが承認されると表示できます", 10, Muted)
            }
        }
        return
    }
    val bottomDockInset = bottomDockContentInset()
    val tabs = remember(showMutualFollowers) {
        if (showMutualFollowers) listOf("mutual", "followers", "following")
        else listOf("followers", "following")
    }
    var selectedKind by remember(profileUser.id, showMutualFollowers) {
        mutableStateOf(kind.takeIf { it in tabs } ?: "followers")
    }
    var users by remember(profileUser.id, selectedKind) { mutableStateOf<List<ApiUser>>(emptyList()) }
    var nextCursor by remember(profileUser.id, selectedKind) { mutableStateOf<String?>(null) }
    var hasNext by remember(profileUser.id, selectedKind) { mutableStateOf(true) }
    var loading by remember(profileUser.id, selectedKind) { mutableStateOf(true) }
    var error by remember(profileUser.id, selectedKind) { mutableStateOf<String?>(null) }
    val listState = remember(selectedKind) { LazyListState() }
    val scope = rememberCoroutineScope()

    suspend fun loadNext() {
        if (loading || !hasNext) return
        loading = true
        val requestedKind = selectedKind
        when (val result = withContext(Dispatchers.IO) { api.userConnections(profileUser.id, requestedKind, nextCursor) }) {
            is ApiResult.Success -> {
                if (selectedKind == requestedKind) {
                    val incoming = result.value.users.filter { candidate -> users.none { it.id == candidate.id } }
                    users = users + incoming
                    nextCursor = result.value.nextCursor
                    hasNext = result.value.hasNext && incoming.isNotEmpty()
                    error = null
                }
            }
            is ApiResult.Failure -> {
                if (selectedKind == requestedKind) {
                    error = result.message
                    hasNext = false
                }
            }
        }
        if (selectedKind == requestedKind) loading = false
    }

    LaunchedEffect(profileUser.id, selectedKind) {
        loading = false
        loadNext()
    }
    InfiniteLoadEffect(listState, users.size, hasNext, loading) { scope.launch { loadNext() } }
    BackHandler(enabled = LocalNavigationActive.current) { onBack() }
    val connectionTabTravel = with(LocalDensity.current) { 44.dp.roundToPx() }

    Column(
        Modifier.fillMaxSize().background(Paper)
            .pointerInput(profileUser.id, selectedKind) {
                var horizontalDistance = 0f
                detectHorizontalDragGestures(
                    onDragStart = { horizontalDistance = 0f },
                    onDragCancel = { horizontalDistance = 0f },
                    onDragEnd = {
                        val index = tabs.indexOf(selectedKind).coerceAtLeast(0)
                        val target = when {
                            horizontalDistance < -90f && index < tabs.lastIndex -> tabs[index + 1]
                            horizontalDistance > 90f && index > 0 -> tabs[index - 1]
                            else -> selectedKind
                        }
                        if (target != selectedKind) selectedKind = target
                        horizontalDistance = 0f
                    }
                ) { _, dragAmount -> horizontalDistance += dragAmount }
            }
    ) {
        OverlayHeader("${profileUser.displayName.ifBlank { profileUser.username }}のつながり", onBack)
        Row(Modifier.fillMaxWidth().background(Surface).padding(horizontal = 14.dp, vertical = 9.dp)) {
            tabs.forEach { tab ->
                val selected = selectedKind == tab
                val tabBackground by animateColorAsState(
                    if (selected) Strong else Color.Transparent,
                    tween(220),
                    label = "connectionTabBackground"
                )
                val tabText by animateColorAsState(
                    if (selected) OnStrong else Muted,
                    tween(220),
                    label = "connectionTabText"
                )
                val label = when (tab) {
                    "mutual" -> "知り合いのフォロワー"
                    "following" -> "フォロー中"
                    else -> "フォロワー"
                }
                Box(
                    Modifier.weight(if (tab == "mutual") 1.45f else 1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(tabBackground)
                        .clickable { if (!selected) selectedKind = tab }
                        .padding(horizontal = 5.dp, vertical = 9.dp),
                    contentAlignment = Alignment.Center
                ) {
                    KText(label, 9, tabText, FontWeight.Black, maxLines = 1)
                }
            }
        }
        AnimatedContent(
            targetState = selectedKind,
            modifier = Modifier.fillMaxSize(),
            transitionSpec = {
                val forward = tabs.indexOf(targetState) >= tabs.indexOf(initialState)
                (slideInHorizontally(tween(320, easing = FastOutSlowInEasing)) { if (forward) connectionTabTravel else -connectionTabTravel } +
                    fadeIn(tween(220)))
                    .togetherWith(
                        slideOutHorizontally(tween(260, easing = FastOutSlowInEasing)) { if (forward) -connectionTabTravel else connectionTabTravel } +
                            fadeOut(tween(180))
                    )
            },
            label = "profileConnectionsTab"
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = bottomDockInset + 24.dp)
            ) {
                if (loading && users.isEmpty()) items(4) { LoadingPost() }
                error?.let { message -> item { ErrorText(message) } }
                items(users, key = { it.id }) { listedUser ->
                    Row(
                        Modifier.fillMaxWidth().clickable { onUser(listedUser) }
                            .padding(horizontal = 20.dp, vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(Modifier.size(48.dp).background(PaleCarrot, RoundedCornerShape(16.dp)), contentAlignment = Alignment.Center) {
                            if (listedUser.avatarUrl != null) {
                                AsyncImage(listedUser.avatarUrl, listedUser.displayName, Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)), contentScale = ContentScale.Crop)
                            } else KText(listedUser.displayName.ifBlank { listedUser.username }.take(1), 16, Ink, FontWeight.Black)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                KText(listedUser.displayName.ifBlank { listedUser.username }, 14, Ink, FontWeight.Bold, maxLines = 1, modifier = Modifier.widthIn(max = 180.dp))
                                AccountMarks(listedUser.officialMarks.ifEmpty { listOf(listedUser.officialMark) }, listedUser.isBotAccount, listedUser.isParodyAccount, listedUser.isPrivate, compact = true)
                            }
                            KText("@${listedUser.username}", 10, Muted, maxLines = 1)
                            if (listedUser.bio.isNotBlank()) {
                                Spacer(Modifier.height(3.dp))
                                KText(listedUser.bio.replace('\n', ' '), 10, Muted, maxLines = 1)
                            }
                        }
                        CustomIcon(IconType.FORWARD, Muted, 16.dp)
                    }
                    Box(Modifier.padding(start = 80.dp).fillMaxWidth().height(1.dp).background(Hairline))
                }
                if (loading && users.isNotEmpty()) item { LoadingPost() }
                if (!loading && error == null && users.isEmpty()) {
                    item {
                        val emptyLabel = when (selectedKind) {
                            "mutual" -> "知り合いのフォロワーはいません"
                            "following" -> "フォロー中のユーザーはいません"
                            else -> "フォロワーはいません"
                        }
                        KText(emptyLabel, 12, Muted, modifier = Modifier.padding(22.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileLevelProgress(level: Int, percent: Int) {
    val safePercent = percent.coerceIn(0, 100)
    val animatedProgress by animateFloatAsState(
        targetValue = safePercent / 100f,
        animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing),
        label = "levelProgress"
    )
    Row(
        Modifier.clip(RoundedCornerShape(13.dp)).background(PaleCarrot)
            .border(1.dp, Carrot.copy(.28f), RoundedCornerShape(13.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        KText("LV $level", 10, Ink, FontWeight.Black, maxLines = 1)
        Spacer(Modifier.width(7.dp))
        Box(Modifier.width(38.dp).height(5.dp).clip(RoundedCornerShape(3.dp)).background(Hairline)) {
            Box(
                Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .background(Carrot, RoundedCornerShape(3.dp))
            )
        }
        Spacer(Modifier.width(6.dp))
        KText("$safePercent%", 9, Carrot, FontWeight.Black, maxLines = 1)
    }
}

@Composable
private fun ProfileStatusBadge(status: String, message: String, modifier: Modifier = Modifier) {
    Row(
        modifier.clip(RoundedCornerShape(13.dp)).background(Surface)
            .border(1.dp, Hairline, RoundedCornerShape(13.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(9.dp).background(onlineStatusColor(status), CircleShape))
        Spacer(Modifier.width(7.dp))
        KText(onlineStatusLabel(status), 9, Ink, FontWeight.Black, maxLines = 1)
        if (message.isNotBlank()) {
            Spacer(Modifier.width(6.dp))
            Box(Modifier.width(1.dp).height(12.dp).background(Hairline))
            Spacer(Modifier.width(6.dp))
            KText(message, 9, Muted, FontWeight.Medium, maxLines = 1, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun Stat(value: String, label: String, onClick: (() -> Unit)? = null) {
    Row(
        Modifier.clip(RoundedCornerShape(8.dp)).clickable(enabled = onClick != null) { onClick?.invoke() }.padding(vertical = 3.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        KText(value, 15, Ink, FontWeight.Black)
        Spacer(Modifier.width(5.dp))
        KText(label, 10, Muted)
    }
}

@Composable
private fun EditorialHeader(kicker: String, title: String, subtitle: String) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 20.dp)) {
        KText(kicker, 10, Carrot, FontWeight.Black, letterSpacing = 2f)
        Spacer(Modifier.height(13.dp))
        KText(title, 31, Ink, FontWeight.Black, lineHeight = 38f)
        Spacer(Modifier.height(8.dp))
        KText(subtitle, 11, Muted)
    }
}

@Composable
private fun bottomDockContentInset(): Dp =
    WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding().coerceAtLeast(10.dp) + 55.dp

@Composable
private fun BottomDock(
    selected: Section,
    bottomPadding: Dp,
    dmHasUnread: Boolean,
    hazeState: HazeState,
    onSelect: (Section) -> Unit,
    modifier: Modifier = Modifier
) {
    val glassOpacity = if (Paper.luminance() > .5f) .78f else .84f
    Box(
        modifier.fillMaxWidth()
            .hazeEffect(state = hazeState) {
                backgroundColor = Paper
                blurRadius = 24.dp
                noiseFactor = .08f
            }
            .background(
                Brush.verticalGradient(
                    listOf(
                        Paper.copy((glassOpacity - .30f).coerceAtLeast(.38f)),
                        Paper.copy(glassOpacity - .22f),
                        Paper.copy((glassOpacity - .14f).coerceAtMost(.76f))
                    )
                )
            )
            .drawBehind {
                drawLine(Color.White.copy(if (Paper.luminance() > .5f) .66f else .13f), Offset.Zero, Offset(size.width, 0f), 1.dp.toPx())
                drawLine(Hairline.copy(.72f), Offset(0f, 1.dp.toPx()), Offset(size.width, 1.dp.toPx()), 1.dp.toPx())
            }
            // Consume taps in the dock's empty/system-navigation clearance so
            // controls from the screen behind it can never receive them.
            .clickable { }
    ) {
        Row(
            Modifier.fillMaxWidth()
            .padding(
                start = 9.dp,
                end = 9.dp,
                top = 9.dp,
                bottom = bottomPadding.coerceAtLeast(10.dp)
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Section.entries.forEach { DockItem(it, selected == it, it == Section.DM && dmHasUnread) { onSelect(it) } }
        }
    }
}

@Composable
private fun RowScope.DockItem(section: Section, selected: Boolean, hasBadge: Boolean, action: () -> Unit) {
    val lift by animateDpAsState(if (selected) (-4).dp else 0.dp, spring(dampingRatio = .58f), label = "dockLift")
    Column(
        Modifier.weight(1f).offset(y = lift).clip(RoundedCornerShape(14.dp)).clickable { action() }.padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.size(24.dp), contentAlignment = Alignment.Center) {
            CustomIcon(
                when (section) {
                    Section.HOME -> IconType.HOME
                    Section.SEARCH -> IconType.SEARCH
                    Section.COMMUNITY -> IconType.COMMUNITY
                    Section.BOARD -> IconType.BOARD
                    Section.DM -> IconType.DM
                    Section.PROFILE -> IconType.PERSON
                },
                if (selected) Ink else Muted, 20.dp, selected
            )
            if (hasBadge) {
                Box(
                    Modifier.align(Alignment.TopEnd).size(8.dp)
                        .background(Carrot, CircleShape)
                        .border(1.5.dp, Paper, CircleShape)
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Box(Modifier.width(if (selected) 12.dp else 0.dp).height(2.dp).background(if (selected) Carrot else Color.Transparent, CircleShape))
    }
}

@Composable
private fun EditPostDialog(
    post: Post,
    api: KarotterApi,
    characterLimit: Int,
    onDismiss: () -> Unit,
    onUpdated: (Post) -> Unit
) {
    var content by remember(post.id) { mutableStateOf(post.text) }
    var saving by remember(post.id) { mutableStateOf(false) }
    var error by remember(post.id) { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val changed = content.trim() != post.text.trim()
    val effectiveCharacterLimit = characterLimit.coerceAtLeast(1)
    val canSave = content.isNotBlank() && content.length <= effectiveCharacterLimit && changed && !saving
    Dialog(
        onDismissRequest = { if (!saving) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            Modifier.fillMaxSize().background(Paper).navigationBarsPadding().imePadding()
        ) {
            Row(
                Modifier.fillMaxWidth().background(Surface)
                    .drawBehind { drawLine(Hairline, Offset(0f, size.height), Offset(size.width, size.height), 1.dp.toPx()) }
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.size(40.dp).clip(RoundedCornerShape(14.dp))
                        .border(1.dp, Hairline, RoundedCornerShape(14.dp))
                        .clickable(enabled = !saving) { onDismiss() },
                    contentAlignment = Alignment.Center
                ) { CustomIcon(IconType.CLOSE, Ink, 19.dp) }
                Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
                    KText("投稿を編集", 16, Ink, FontWeight.Black)
                    KText("EDIT POST", 9, Muted, FontWeight.Black, letterSpacing = 1.6f)
                }
                Box(
                    Modifier.clip(RoundedCornerShape(14.dp))
                        .background(if (canSave) Carrot else Hairline)
                        .clickable(enabled = canSave) {
                            val postId = post.id ?: return@clickable
                            saving = true
                            error = null
                            scope.launch {
                                when (val result = withContext(Dispatchers.IO) {
                                    api.updatePost(
                                        postId = postId,
                                        content = content.trim(),
                                        visibility = post.visibility,
                                        viewerCircleId = post.viewerCircleId,
                                        replyRestriction = post.replyRestriction,
                                        replyCircleId = post.replyCircleId,
                                        minimumAge = post.minimumAge,
                                        maximumAge = post.maximumAge,
                                        isAiGenerated = post.isAiGenerated,
                                        isPromotional = post.isPromotional,
                                        isR18 = post.isR18,
                                        expiresAt = post.expiresAt
                                    )
                                }) {
                                    is ApiResult.Success -> onUpdated(result.value.toUiPost())
                                    is ApiResult.Failure -> {
                                        error = result.message
                                        saving = false
                                    }
                                }
                            }
                        }.padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    KText(if (saving) "保存中…" else "保存", 11, if (canSave) Color.White else Muted, FontWeight.Black)
                }
            }
            Column(Modifier.fillMaxSize().padding(22.dp)) {
                BasicTextField(
                    value = content,
                    onValueChange = { content = it.take(effectiveCharacterLimit); error = null },
                    textStyle = TextStyle(Ink, 19.sp, FontWeight.Medium, lineHeight = 29.sp),
                    cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
                    modifier = Modifier.fillMaxWidth().weight(1f)
                )
                error?.let {
                    ErrorText(it)
                    Spacer(Modifier.height(10.dp))
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    KText("投稿文字数", 10, Muted, FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    KText(
                        "${content.length} / $effectiveCharacterLimit",
                        12,
                        if (content.length >= effectiveCharacterLimit - 20) Carrot else Ink,
                        FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun StoryComposerDialog(
    api: KarotterApi,
    onDismiss: () -> Unit,
    onCreated: (ApiStory) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var media by remember { mutableStateOf<ComposerMedia?>(null) }
    var caption by remember { mutableStateOf("") }
    var captionX by remember { mutableStateOf(50f) }
    var captionY by remember { mutableStateOf(72f) }
    var captionSize by remember { mutableIntStateOf(28) }
    var captionColor by remember { mutableStateOf("#ffffff") }
    var captionBackground by remember { mutableStateOf("soft") }
    var captionAlign by remember { mutableStateOf("center") }
    var previewSize by remember { mutableStateOf(Size.Zero) }
    var previewCaptionSize by remember { mutableStateOf(Size.Zero) }
    var sending by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        val mime = context.contentResolver.getType(uri).orEmpty().ifBlank { "application/octet-stream" }
        val name = context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) cursor.getString(0) else null
        } ?: "story-${System.currentTimeMillis()}"
        media = ComposerMedia(uri, name, mime)
        error = null
    }
    Dialog(
        onDismissRequest = { if (!sending) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(Modifier.fillMaxSize().background(Paper).navigationBarsPadding().imePadding()) {
            Row(
                Modifier.fillMaxWidth().background(Surface)
                    .drawBehind { drawLine(Hairline, Offset(0f, size.height), Offset(size.width, size.height), 1.dp.toPx()) }
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.size(40.dp).clip(RoundedCornerShape(14.dp))
                        .border(1.dp, Hairline, RoundedCornerShape(14.dp))
                        .clickable(enabled = !sending) { onDismiss() },
                    contentAlignment = Alignment.Center
                ) { CustomIcon(IconType.CLOSE, Ink, 19.dp) }
                Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
                    KText("ストーリーを作成", 16, Ink, FontWeight.Black)
                    KText("NEW STORY · 24 HOURS", 9, Muted, FontWeight.Black, letterSpacing = 1.4f)
                }
                Box(
                    Modifier.clip(RoundedCornerShape(14.dp))
                        .background(if (media != null && !sending) Carrot else Hairline)
                        .clickable(enabled = media != null && !sending) {
                            val selected = media ?: return@clickable
                            sending = true
                            error = null
                            scope.launch {
                                val result = withContext(Dispatchers.IO) {
                                    runCatching {
                                        val bytes = context.contentResolver.openInputStream(selected.uri)?.use { it.readBytes() }
                                            ?: throw IllegalStateException("メディアを読み込めませんでした")
                                        val overlay = caption.trim().takeIf(String::isNotBlank)
                                        api.createStory(
                                            media = ApiUploadMedia(selected.name, selected.mimeType, bytes),
                                            caption = "",
                                            textOverlay = overlay,
                                            textOverlayStyle = overlay?.let {
                                                ApiStoryTextStyle(
                                                    x = captionX,
                                                    y = captionY,
                                                    size = captionSize,
                                                    align = captionAlign,
                                                    color = captionColor,
                                                    background = captionBackground
                                                )
                                            }
                                        )
                                    }.getOrElse { ApiResult.Failure(it.message ?: "ストーリーを送信できませんでした") }
                                }
                                when (result) {
                                    is ApiResult.Success -> onCreated(result.value)
                                    is ApiResult.Failure -> {
                                        error = result.message
                                        sending = false
                                    }
                                }
                            }
                        }.padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    KText(if (sending) "投稿中…" else "投稿", 11, if (media != null && !sending) Color.White else Muted, FontWeight.Black)
                }
            }
            Column(
                Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp, vertical = 18.dp)
            ) {
                Box(
                    Modifier.fillMaxWidth().height(460.dp).clip(RoundedCornerShape(24.dp))
                        .background(Strong)
                        .border(1.dp, Hairline, RoundedCornerShape(24.dp))
                        .onGloballyPositioned {
                            previewSize = Size(it.size.width.toFloat(), it.size.height.toFloat())
                        }
                        .clickable(enabled = !sending) { picker.launch(arrayOf("image/*", "video/*")) },
                    contentAlignment = Alignment.Center
                ) {
                    val selected = media
                    if (selected == null) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(Modifier.size(58.dp).background(Paper.copy(.14f), CircleShape), contentAlignment = Alignment.Center) {
                                CustomIcon(IconType.IMAGE, OnStrong, 27.dp)
                            }
                            Spacer(Modifier.height(13.dp))
                            KText("画像または動画を選択", 14, OnStrong, FontWeight.Black)
                            KText("タップしてメディアを選んでください", 10, OnStrong.copy(.65f))
                        }
                    } else if (selected.mimeType.startsWith("image/")) {
                        AsyncImage(selected.uri, selected.name, Modifier.fillMaxSize(), contentScale = ContentScale.Fit)
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CustomIcon(IconType.IMAGE, OnStrong, 31.dp)
                            Spacer(Modifier.height(10.dp))
                            KText(selected.name, 12, OnStrong, FontWeight.Bold, maxLines = 2)
                            KText("動画を選択済み", 9, OnStrong.copy(.65f))
                        }
                    }
                    if (selected != null) {
                        Box(
                            Modifier.align(Alignment.TopEnd).padding(12.dp)
                                .background(Paper.copy(.9f), RoundedCornerShape(12.dp))
                                .clickable(enabled = !sending) { picker.launch(arrayOf("image/*", "video/*")) }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) { KText("変更", 10, Ink, FontWeight.Black) }
                    }
                    if (caption.isNotBlank()) {
                        val selectedColor = captionColor.toAppColor() ?: Color.White
                        val textColor = if (captionBackground == "solid") {
                            if (selectedColor.luminance() > .62f) Color(0xFF111827) else Color.White
                        } else selectedColor
                        val backgroundColor = when (captionBackground) {
                            "soft" -> Color(0x610F172A)
                            "solid" -> selectedColor
                            else -> Color.Transparent
                        }
                        val captionShape = RoundedCornerShape(if (captionBackground == "none") 0.dp else 26.dp)
                        KText(
                            caption,
                            captionSize,
                            textColor,
                            FontWeight.Black,
                            textAlign = when (captionAlign) {
                                "left" -> TextAlign.Left
                                "right" -> TextAlign.Right
                                else -> TextAlign.Center
                            },
                            modifier = Modifier.align(Alignment.TopStart)
                                .widthIn(max = 320.dp)
                                .graphicsLayer {
                                    translationX = previewSize.width * captionX / 100f - previewCaptionSize.width / 2f
                                    translationY = previewSize.height * captionY / 100f - previewCaptionSize.height / 2f
                                }
                                .onGloballyPositioned {
                                    previewCaptionSize = Size(it.size.width.toFloat(), it.size.height.toFloat())
                                }
                                .background(backgroundColor, captionShape)
                                .then(
                                    if (captionBackground == "none") Modifier
                                    else Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                                )
                                .pointerInput(previewSize, previewCaptionSize) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        if (previewSize.width > 0f && previewSize.height > 0f) {
                                            captionX = (captionX + dragAmount.x / previewSize.width * 100f).coerceIn(10f, 90f)
                                            captionY = (captionY + dragAmount.y / previewSize.height * 100f).coerceIn(14f, 86f)
                                        }
                                    }
                                }
                        )
                    }
                    if (caption.isNotBlank()) {
                        Row(
                            Modifier.align(Alignment.BottomCenter).padding(12.dp)
                                .background(Color.Black.copy(.42f), RoundedCornerShape(13.dp))
                                .border(1.dp, Color.White.copy(.18f), RoundedCornerShape(13.dp))
                                .padding(horizontal = 11.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomIcon(IconType.CONTROLS, Color.White, 14.dp)
                            Spacer(Modifier.width(6.dp))
                            KText("文字をドラッグして配置", 9, Color.White, FontWeight.Bold)
                        }
                    }
                }
                Spacer(Modifier.height(14.dp))
                Box(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier.fillMaxWidth()
                            .background(Surface, RoundedCornerShape(22.dp))
                            .border(1.dp, Hairline, RoundedCornerShape(22.dp))
                            .padding(13.dp)
                    ) {
                        BasicTextField(
                            value = caption,
                            onValueChange = { caption = it.take(120) },
                            textStyle = TextStyle(Ink, 14.sp, FontWeight.Medium, lineHeight = 21.sp),
                            cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
                            modifier = Modifier.fillMaxWidth().heightIn(min = 58.dp)
                                .background(Paper, RoundedCornerShape(14.dp))
                                .border(1.dp, Hairline, RoundedCornerShape(14.dp)).padding(12.dp),
                            decorationBox = { inner ->
                                Box {
                                    if (caption.isEmpty()) KText("自由配置テキスト（任意）", 13, Muted)
                                    inner()
                                }
                            }
                        )
                        Spacer(Modifier.height(11.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            KText("サイズ", 9, Muted, FontWeight.Black)
                            Spacer(Modifier.width(8.dp))
                            Box(
                                Modifier.clip(RoundedCornerShape(10.dp)).background(Paper.copy(.65f))
                                    .clickable { captionSize = (captionSize - 1).coerceAtLeast(18) }
                                    .padding(horizontal = 11.dp, vertical = 7.dp)
                            ) { KText("−", 10, Ink, FontWeight.Black) }
                            KText("${captionSize}px", 9, Ink, FontWeight.Black, modifier = Modifier.padding(horizontal = 9.dp))
                            Box(
                                Modifier.clip(RoundedCornerShape(10.dp)).background(Paper.copy(.65f))
                                    .clickable { captionSize = (captionSize + 1).coerceAtMost(44) }
                                    .padding(horizontal = 11.dp, vertical = 7.dp)
                            ) { KText("+", 10, Ink, FontWeight.Black) }
                            Spacer(Modifier.weight(1f))
                            listOf("#ffffff", "#f97316", "#facc15", "#4ade80", "#38bdf8", "#a78bfa", "#f472b6", "#111827").forEach { color ->
                                Box(
                                    Modifier.padding(start = 3.dp).size(20.dp)
                                        .background(color.toAppColor() ?: Color.White, CircleShape)
                                        .border(2.dp, if (captionColor == color) Carrot else Hairline, CircleShape)
                                        .clickable { captionColor = color }
                                )
                            }
                        }
                        Spacer(Modifier.height(9.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            KText("背景", 9, Muted, FontWeight.Black)
                            Spacer(Modifier.width(8.dp))
                            listOf("none" to "なし", "soft" to "ぼかし", "solid" to "塗り").forEach { (value, label) ->
                                val selected = captionBackground == value
                                Box(
                                    Modifier.padding(end = 6.dp).clip(RoundedCornerShape(10.dp))
                                        .background(if (selected) Ink else Paper.copy(.65f))
                                        .clickable { captionBackground = value }
                                        .padding(horizontal = 11.dp, vertical = 7.dp)
                                ) {
                                    KText(label, 9, if (selected) Paper else Muted, FontWeight.Bold)
                                }
                            }
                            Spacer(Modifier.weight(1f))
                            KText("${caption.length} / 120", 10, Muted, FontWeight.Bold)
                        }
                        Spacer(Modifier.height(9.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            KText("配置", 9, Muted, FontWeight.Black)
                            Spacer(Modifier.width(8.dp))
                            listOf("left" to "左", "center" to "中央", "right" to "右").forEach { (value, label) ->
                                val selected = captionAlign == value
                                Box(
                                    Modifier.padding(end = 6.dp).clip(RoundedCornerShape(10.dp))
                                        .background(if (selected) Ink else Paper.copy(.65f))
                                        .clickable { captionAlign = value }
                                        .padding(horizontal = 11.dp, vertical = 7.dp)
                                ) {
                                    KText(label, 9, if (selected) Paper else Muted, FontWeight.Bold)
                                }
                            }
                            Spacer(Modifier.weight(1f))
                            Box(
                                Modifier.clip(RoundedCornerShape(10.dp)).background(Paper.copy(.65f))
                                    .clickable {
                                        captionX = 50f
                                        captionY = 72f
                                    }.padding(horizontal = 10.dp, vertical = 7.dp)
                            ) { KText("位置を中央へ", 9, Ink, FontWeight.Bold) }
                        }
                    }
                }
                error?.let {
                    Spacer(Modifier.height(7.dp))
                    ErrorText(it)
                }
            }
        }
    }
}

@Composable
private fun Composer(
    api: KarotterApi,
    currentUser: ApiUser?,
    parent: Post?,
    quote: Post?,
    community: ApiCommunity?,
    question: ApiQuestion?,
    onClose: () -> Unit,
    onSubmit: (String, List<ComposerMedia>, List<String>, Int, ComposerSettings, (String?) -> Unit) -> Unit
) {
    val context = LocalContext.current
    var text by remember(question?.id) {
        mutableStateOf(
            question?.let { item ->
                val mention = item.sender?.username?.let { "@$it " }.orEmpty()
                "${mention}\n\nQ. ${item.content}\nA. "
            }.orEmpty()
        )
    }
    var selectedMedia by remember { mutableStateOf<List<ComposerMedia>>(emptyList()) }
    var pollEnabled by remember { mutableStateOf(false) }
    var pollOptions by remember { mutableStateOf(listOf("", "")) }
    var pollDurationHours by remember { mutableIntStateOf(24) }
    var settings by remember(community?.id) { mutableStateOf(ComposerSettings(communityId = community?.id)) }
    var settingsOpen by remember { mutableStateOf(false) }
    var circles by remember { mutableStateOf<List<ApiCircle>>(emptyList()) }
    var communities by remember { mutableStateOf<List<ApiCommunity>>(emptyList()) }
    var sending by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var confirmClose by remember { mutableStateOf(false) }
    val mediaPicker = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
        if (uris.isEmpty()) return@rememberLauncherForActivityResult
        val picked = uris.map { uri ->
            val mime = context.contentResolver.getType(uri).orEmpty().ifBlank { "application/octet-stream" }
            val name = context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) cursor.getString(0) else null
            } ?: "media-${System.currentTimeMillis()}"
            ComposerMedia(uri, name, mime)
        }
        val combined = (selectedMedia + picked).distinctBy { it.uri }
        val hasVideo = combined.any { it.mimeType.startsWith("video/") }
        val hasImage = combined.any { it.mimeType.startsWith("image/") }
        when {
            hasVideo && hasImage -> error = "画像と動画は同じ投稿に添付できません"
            hasVideo && combined.size > 1 -> error = "動画は1件まで添付できます"
            !hasVideo && combined.size > 4 -> error = "画像は4件まで添付できます"
            else -> {
                selectedMedia = combined
                error = null
            }
        }
    }
    val postCharacterLimit = currentUser?.postCharacterLimit?.coerceAtLeast(1) ?: 200
    val remaining = postCharacterLimit - text.length
    val validPollOptions = pollOptions.map(String::trim).filter(String::isNotBlank)
    val pollValid = !pollEnabled || validPollOptions.size >= 2
    val hasPostBody = text.isNotBlank() || selectedMedia.isNotEmpty()
    val canSubmit = hasPostBody && remaining >= 0 && pollValid && !sending
    LaunchedEffect(Unit) {
        val circleResult = withContext(Dispatchers.IO) { api.circles() }
        if (circleResult is ApiResult.Success) circles = circleResult.value
        val communityResult = withContext(Dispatchers.IO) { api.communities() }
        if (communityResult is ApiResult.Success) {
            communities = (communityResult.value.joined + communityResult.value.owned).distinctBy(ApiCommunity::id)
        }
    }
    if (settingsOpen) {
        ComposerSettingsDialog(
            initial = settings,
            circles = circles,
            communities = communities,
            onDismiss = { settingsOpen = false },
            onSave = {
                settings = it
                settingsOpen = false
            }
        )
    }
    BackHandler {
        when {
            confirmClose -> confirmClose = false
            !sending -> confirmClose = true
        }
    }
    if (confirmClose) {
        Dialog(onDismissRequest = { confirmClose = false }) {
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp))
                    .background(Surface)
                    .border(1.dp, Hairline, RoundedCornerShape(24.dp))
                    .padding(22.dp)
            ) {
                KText("投稿画面を閉じますか？", 18, Ink, FontWeight.Black)
                Spacer(Modifier.height(8.dp))
                KText(
                    "入力中の文章、添付メディア、投票、詳細設定は破棄されます。",
                    12,
                    Muted,
                    lineHeight = 19f
                )
                Spacer(Modifier.height(20.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        Modifier.weight(1f).clip(RoundedCornerShape(14.dp))
                            .border(1.dp, Hairline, RoundedCornerShape(14.dp))
                            .clickable { confirmClose = false }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        KText("編集を続ける", 12, Ink, FontWeight.Bold)
                    }
                    Box(
                        Modifier.weight(1f).clip(RoundedCornerShape(14.dp))
                            .background(Carrot)
                            .clickable {
                                confirmClose = false
                                onClose()
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        KText("破棄して閉じる", 12, Color.White, FontWeight.Black)
                    }
                }
            }
        }
    }
    Box(Modifier.fillMaxSize().background(Paper)) {
        Column(Modifier.fillMaxSize().navigationBarsPadding().imePadding()) {
            Row(
                Modifier.fillMaxWidth().background(Surface)
                    .drawBehind { drawLine(Hairline, Offset(0f, size.height), Offset(size.width, size.height), 1.dp.toPx()) }
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.size(40.dp).clip(RoundedCornerShape(14.dp)).border(1.dp, Hairline, RoundedCornerShape(14.dp))
                        .clickable(enabled = !sending) { confirmClose = true },
                    contentAlignment = Alignment.Center
                ) { CustomIcon(IconType.CLOSE, Ink, 20.dp) }
                Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
                    KText(
                        when {
                            question != null -> "質問に回答"
                            parent != null -> "返信を作成"
                            quote != null -> "引用して投稿"
                            community != null -> "${community.name}に投稿"
                            else -> "新しい投稿"
                        },
                        15,
                        Ink,
                        FontWeight.Black
                    )
                    KText(
                        when {
                            question != null -> "QUESTION ANSWER"
                            parent != null -> "REPLY"
                            quote != null -> "QUOTE"
                            community != null -> "COMMUNITY"
                            else -> "NEW POST"
                        },
                        9,
                        Muted,
                        FontWeight.Black,
                        letterSpacing = 1.8f
                    )
                }
                Box(
                    Modifier.clip(RoundedCornerShape(15.dp))
                        .background(if (canSubmit) Carrot else Hairline)
                        .clickable(enabled = canSubmit) {
                            sending = true
                            error = null
                            onSubmit(
                                text.trim(),
                                selectedMedia,
                                if (pollEnabled) validPollOptions else emptyList(),
                                pollDurationHours,
                                settings
                            ) { message ->
                                sending = false
                                error = message
                            }
                        }
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    KText(if (sending) "送信中…" else "投稿", 11, if (canSubmit) Color.White else Muted, FontWeight.Black)
                }
            }
            (parent ?: quote)?.let { target ->
                Row(
                    Modifier.padding(horizontal = 18.dp, vertical = 14.dp).fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(if (parent != null) Sky else PaleCarrot)
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(Modifier.width(3.dp).height(48.dp).background(Carrot, CircleShape))
                    Spacer(Modifier.width(11.dp))
                    Column(Modifier.weight(1f)) {
                        KText(
                            if (parent != null) "@${target.handle.removePrefix("@")} への返信" else "${target.name}さんの投稿を引用",
                            10,
                            Carrot,
                            FontWeight.Black
                        )
                        Spacer(Modifier.height(5.dp))
                        KText(target.text.ifBlank { "メディア付き投稿" }, 12, Ink, FontWeight.Medium, maxLines = 3)
                    }
                }
            }
            question?.let { item ->
                Row(
                    Modifier.padding(horizontal = 18.dp, vertical = 14.dp).fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp)).background(PaleCarrot).padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    CustomIcon(IconType.QUESTION, Carrot, 18.dp)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        KText("受け取った質問", 10, Carrot, FontWeight.Black)
                        Spacer(Modifier.height(5.dp))
                        KText(item.content, 12, Ink, FontWeight.Medium, maxLines = 4)
                    }
                }
            }
            Row(Modifier.weight(1f).padding(horizontal = 20.dp, vertical = if (parent != null || quote != null || question != null) 8.dp else 22.dp)) {
                Box(
                    Modifier.size(48.dp).clip(RoundedCornerShape(17.dp)).background(PaleCarrot),
                    contentAlignment = Alignment.Center
                ) {
                    if (currentUser?.avatarUrl != null) {
                        AsyncImage(currentUser.avatarUrl, currentUser.displayName, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    } else {
                        KText(currentUser?.displayName?.take(1) ?: "K", 17, Ink, FontWeight.Black)
                    }
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f).fillMaxHeight()) {
                    KText(currentUser?.displayName?.ifBlank { currentUser.username } ?: "Karoha", 11, Ink, FontWeight.Black)
                    Spacer(Modifier.height(8.dp))
                    BasicTextField(
                        value = text,
                        onValueChange = { text = it.take(postCharacterLimit); error = null },
                        textStyle = TextStyle(Ink, 19.sp, FontWeight.Medium, lineHeight = 29.sp),
                        cursorBrush = androidx.compose.ui.graphics.SolidColor(Carrot),
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        decorationBox = { inner ->
                            Box(Modifier.fillMaxSize()) {
                                if (text.isEmpty()) {
                                    KText(
                                        if (parent != null) "返信を入力" else if (quote != null) "コメントを追加" else "あなたの「いま」を残そう。",
                                        19,
                                        Muted,
                                        FontWeight.Medium
                                    )
                                }
                                inner()
                            }
                        }
                    )
                    if (selectedMedia.isNotEmpty()) {
                        Spacer(Modifier.height(10.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(selectedMedia, key = { it.uri.toString() }) { item ->
                                Box(
                                    Modifier.size(82.dp).clip(RoundedCornerShape(14.dp))
                                        .background(Strong)
                                ) {
                                    if (item.mimeType.startsWith("image/")) {
                                        AsyncImage(item.uri, item.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                                    } else {
                                        Column(
                                            Modifier.fillMaxSize().padding(9.dp),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            KText("▶", 19, OnStrong, FontWeight.Black)
                                            Spacer(Modifier.height(5.dp))
                                            KText(item.name, 8, OnStrong.copy(.72f), maxLines = 2)
                                        }
                                    }
                                    Box(
                                        Modifier.align(Alignment.TopEnd).padding(5.dp).size(22.dp)
                                            .background(Paper.copy(.9f), CircleShape)
                                            .clickable { selectedMedia = selectedMedia.filterNot { it.uri == item.uri } },
                                        contentAlignment = Alignment.Center
                                    ) { CustomIcon(IconType.CLOSE, Ink, 12.dp) }
                                }
                            }
                        }
                    }
                    if (pollEnabled) {
                        Spacer(Modifier.height(10.dp))
                        ComposerPollEditor(
                            options = pollOptions,
                            durationHours = pollDurationHours,
                            onOptionsChange = { pollOptions = it },
                            onDurationChange = { pollDurationHours = it },
                            onClose = {
                                pollEnabled = false
                                pollOptions = listOf("", "")
                            }
                        )
                    }
                }
            }
            if (error != null) {
                ErrorText(error.orEmpty())
            }
            Column(
                Modifier.fillMaxWidth().background(Surface)
                    .drawBehind { drawLine(Hairline, Offset.Zero, Offset(size.width, 0f), 1.dp.toPx()) }
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    KText("投稿文字数", 9, Muted, FontWeight.Bold)
                    Spacer(Modifier.weight(1f))
                    KText(
                        "${text.length} / $postCharacterLimit",
                        11,
                        if (remaining <= 20) Carrot else Ink,
                        FontWeight.Black
                    )
                }
                Spacer(Modifier.height(7.dp))
                Box(Modifier.fillMaxWidth().height(3.dp).clip(CircleShape).background(Hairline)) {
                    Box(
                        Modifier.fillMaxWidth((text.length / postCharacterLimit.toFloat()).coerceIn(0f, 1f)).fillMaxHeight()
                            .background(if (remaining <= 20) Carrot else Ink, CircleShape)
                    )
                }
                Spacer(Modifier.height(11.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        Modifier.clip(RoundedCornerShape(12.dp)).background(Paper)
                            .border(1.dp, Hairline, RoundedCornerShape(12.dp))
                            .clickable(enabled = !sending) {
                                mediaPicker.launch(arrayOf("image/*", "video/*"))
                            }.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomIcon(IconType.IMAGE, Ink, 15.dp)
                        Spacer(Modifier.width(6.dp))
                        KText("画像・動画", 9, Ink, FontWeight.Bold)
                    }
                    Spacer(Modifier.width(7.dp))
                    Row(
                        Modifier.clip(RoundedCornerShape(12.dp))
                            .background(if (pollEnabled) PaleCarrot else Paper)
                            .border(1.dp, if (pollEnabled) Carrot else Hairline, RoundedCornerShape(12.dp))
                            .clickable(enabled = !sending) { pollEnabled = !pollEnabled }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomIcon(IconType.POLL, if (pollEnabled) Carrot else Ink, 14.dp)
                        Spacer(Modifier.width(6.dp))
                        KText("投票", 9, if (pollEnabled) Carrot else Ink, FontWeight.Bold)
                    }
                    Spacer(Modifier.width(7.dp))
                    Row(
                        Modifier.clip(RoundedCornerShape(12.dp))
                            .background(if (settings != ComposerSettings()) Sky else Paper)
                            .border(1.dp, if (settings != ComposerSettings()) Ink else Hairline, RoundedCornerShape(12.dp))
                            .clickable(enabled = !sending) { settingsOpen = true }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomIcon(IconType.CONTROLS, Ink, 14.dp)
                        Spacer(Modifier.width(6.dp))
                        KText("詳細", 9, Ink, FontWeight.Bold)
                    }
                    Spacer(Modifier.weight(1f))
                    KText("あと ${remaining}文字", 10, if (remaining <= 20) Carrot else Muted, FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun ComposerPollEditor(
    options: List<String>,
    durationHours: Int,
    onOptionsChange: (List<String>) -> Unit,
    onDurationChange: (Int) -> Unit,
    onClose: () -> Unit
) {
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Surface)
            .border(1.dp, Hairline, RoundedCornerShape(16.dp)).padding(11.dp),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            KText("アンケート", 11, Ink, FontWeight.Black, modifier = Modifier.weight(1f))
            Box(Modifier.clickable { onClose() }.padding(4.dp)) { CustomIcon(IconType.CLOSE, Muted, 15.dp) }
        }
        options.forEachIndexed { index, value ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = value,
                    onValueChange = { next ->
                        onOptionsChange(options.toMutableList().also { it[index] = next.take(50) })
                    },
                    singleLine = true,
                    textStyle = TextStyle(Ink, 12.sp, FontWeight.Medium),
                    modifier = Modifier.weight(1f).height(39.dp).background(Paper, RoundedCornerShape(11.dp))
                        .border(1.dp, Hairline, RoundedCornerShape(11.dp)).padding(horizontal = 11.dp),
                    decorationBox = { inner ->
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                            if (value.isBlank()) KText("選択肢 ${index + 1}", 11, Muted)
                            inner()
                        }
                    }
                )
                if (options.size > 2) {
                    Spacer(Modifier.width(7.dp))
                    Box(Modifier.clickable {
                        onOptionsChange(options.filterIndexed { optionIndex, _ -> optionIndex != index })
                    }.padding(5.dp)) { CustomIcon(IconType.CLOSE, Muted, 13.dp) }
                }
            }
        }
        if (options.size < 4) {
            Row(
                Modifier.clickable { onOptionsChange(options + "") }.padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                CustomIcon(IconType.PLUS, Carrot, 13.dp)
                KText("選択肢を追加", 10, Carrot, FontWeight.Bold)
            }
        }
        KText("投票期間", 9, Muted, FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(1 to "1時間", 24 to "1日", 72 to "3日", 168 to "7日").forEach { (hours, label) ->
                Box(
                    Modifier.clip(RoundedCornerShape(10.dp))
                        .background(if (durationHours == hours) Strong else Paper)
                        .border(1.dp, if (durationHours == hours) Strong else Hairline, RoundedCornerShape(10.dp))
                        .clickable { onDurationChange(hours) }.padding(horizontal = 9.dp, vertical = 6.dp)
                ) {
                    KText(label, 8, if (durationHours == hours) OnStrong else Muted, FontWeight.Bold)
                }
            }
        }
    }
}

private val ComposerDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm", Locale.JAPAN)

private fun formatComposerDate(instant: Instant): String =
    ComposerDateFormatter.format(instant.atZone(ZoneId.systemDefault()))

private fun parseComposerDate(value: String): Instant? =
    runCatching { LocalDateTime.parse(value.trim(), ComposerDateFormatter).atZone(ZoneId.systemDefault()).toInstant() }.getOrNull()

@Composable
private fun ComposerSettingsDialog(
    initial: ComposerSettings,
    circles: List<ApiCircle>,
    communities: List<ApiCommunity>,
    onDismiss: () -> Unit,
    onSave: (ComposerSettings) -> Unit
) {
    var communityId by remember { mutableStateOf(initial.communityId) }
    var visibility by remember { mutableStateOf(initial.visibility) }
    var viewerCircleId by remember { mutableStateOf(initial.viewerCircleId) }
    var replyRestriction by remember { mutableStateOf(initial.replyRestriction) }
    var replyCircleId by remember { mutableStateOf(initial.replyCircleId) }
    var minimumAge by remember { mutableStateOf(initial.minimumAge) }
    var maximumAge by remember { mutableStateOf(initial.maximumAge) }
    var isAiGenerated by remember { mutableStateOf(initial.isAiGenerated) }
    var isPromotional by remember { mutableStateOf(initial.isPromotional) }
    var isR18 by remember { mutableStateOf(initial.isR18) }
    var hideFromMinors by remember { mutableStateOf(initial.hideFromMinors) }
    var scheduledEnabled by remember { mutableStateOf(initial.scheduledFor != null) }
    var scheduledText by remember {
        mutableStateOf(initial.scheduledFor?.let(::parseTimestamp)?.let(::formatComposerDate) ?: formatComposerDate(Instant.now().plusSeconds(3600)))
    }
    var expiresEnabled by remember { mutableStateOf(initial.expiresAt != null) }
    var expiresText by remember {
        mutableStateOf(initial.expiresAt?.let(::parseTimestamp)?.let(::formatComposerDate) ?: formatComposerDate(Instant.now().plusSeconds(86400)))
    }
    var validationError by remember { mutableStateOf<String?>(null) }

    fun save() {
        val now = Instant.now()
        val scheduled = if (scheduledEnabled) parseComposerDate(scheduledText) else null
        val expires = if (expiresEnabled) parseComposerDate(expiresText) else null
        validationError = when {
            visibility == "CIRCLE" && viewerCircleId == null -> "公開するサークルを選択してください"
            replyRestriction == "CIRCLE" && replyCircleId == null -> "返信可能なサークルを選択してください"
            minimumAge != null && maximumAge != null && minimumAge!! > maximumAge!! -> "年齢制限の上限は下限以上にしてください"
            scheduledEnabled && scheduled == null -> "予約日時を yyyy/MM/dd HH:mm 形式で入力してください"
            scheduled != null && !scheduled.isAfter(now) -> "予約日時は現在より後にしてください"
            expiresEnabled && expires == null -> "自動削除日時を yyyy/MM/dd HH:mm 形式で入力してください"
            expires != null && !expires.isAfter(scheduled ?: now) -> "自動削除は投稿日時より後にしてください"
            else -> null
        }
        if (validationError == null) {
            onSave(
                ComposerSettings(
                    communityId = communityId,
                    visibility = visibility,
                    replyRestriction = replyRestriction,
                    viewerCircleId = if (visibility == "CIRCLE") viewerCircleId else null,
                    replyCircleId = if (replyRestriction == "CIRCLE") replyCircleId else null,
                    minimumAge = minimumAge,
                    maximumAge = maximumAge,
                    isAiGenerated = isAiGenerated,
                    isPromotional = isPromotional,
                    isR18 = isR18,
                    hideFromMinors = hideFromMinors,
                    scheduledFor = scheduled?.toString(),
                    expiresAt = expires?.toString()
                )
            )
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Column(
            Modifier.fillMaxSize()
                .background(Paper)
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(36.dp).border(1.dp, Hairline, CircleShape).clickable { onDismiss() }, contentAlignment = Alignment.Center) {
                    CustomIcon(IconType.CLOSE, Ink, 18.dp)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    KText("投稿の詳細設定", 16, Ink, FontWeight.Black)
                    KText("POST CONTROLS", 8, Muted, FontWeight.Black, letterSpacing = 1.5f)
                }
            }
            Box(Modifier.fillMaxWidth().height(1.dp).background(Hairline))
            LazyColumn(
                Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 15.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    SettingsPanel {
                        SettingsSectionTitle("投稿先", "どこに投稿するか")
                        SettingsChoiceRow(listOf("通常のタイムライン" to "TIMELINE"), if (communityId == null) "TIMELINE" else "") {
                            communityId = null
                        }
                        if (communities.isNotEmpty()) {
                            Spacer(Modifier.height(9.dp))
                            KText("参加中のコミュニティ", 9, Muted, FontWeight.Bold)
                            Spacer(Modifier.height(7.dp))
                            CommunityDestinationChoices(communities, communityId) { communityId = it }
                        } else {
                            Spacer(Modifier.height(8.dp))
                            KText("参加中のコミュニティはありません", 9, Muted)
                        }
                    }
                }
                item {
                    SettingsPanel {
                        SettingsSectionTitle("公開可能範囲", "投稿を閲覧できるユーザー")
                        SettingsChoiceRow(
                            listOf("全員" to "PUBLIC", "フォロワー" to "FOLLOWERS", "サークル" to "CIRCLE"),
                            visibility
                        ) {
                            visibility = it
                            if (it != "CIRCLE") viewerCircleId = null
                        }
                        if (visibility == "CIRCLE") {
                            Spacer(Modifier.height(9.dp))
                            CircleChoices(circles, viewerCircleId) { viewerCircleId = it }
                        }
                    }
                }
                item {
                    SettingsPanel {
                        SettingsSectionTitle("返信可能ユーザー", "この投稿へ返信できる範囲")
                        SettingsChoiceRow(
                            listOf("全員" to "EVERYONE", "フォロワー" to "FOLLOWERS", "メンション" to "MENTIONS", "サークル" to "CIRCLE"),
                            replyRestriction
                        ) { replyRestriction = it }
                        if (replyRestriction == "CIRCLE") {
                            Spacer(Modifier.height(9.dp))
                            CircleChoices(circles, replyCircleId) { replyCircleId = it }
                        }
                    }
                }
                item {
                    SettingsPanel {
                        SettingsSectionTitle("年齢制限", "閲覧できる年齢の下限・上限")
                        KText("最低年齢", 9, Muted, FontWeight.Bold)
                        SettingsNullableNumberRow(listOf(13, 16, 18, 20), minimumAge) { minimumAge = it }
                        Spacer(Modifier.height(8.dp))
                        KText("最高年齢", 9, Muted, FontWeight.Bold)
                        SettingsNullableNumberRow(listOf(17, 19, 29, 39, 59), maximumAge) { maximumAge = it }
                    }
                }
                item {
                    SettingsPanel {
                        SettingsSectionTitle("コンテンツ表示", "投稿内容に適用する表示情報")
                        SettingsToggleRow("AI生成コンテンツ", "AIで生成・編集した内容", isAiGenerated) { isAiGenerated = it }
                        SettingsToggleRow("プロモーション", "広告・宣伝を含む内容", isPromotional) { isPromotional = it }
                        SettingsToggleRow("成人向け", "成人向けとして明示", isR18) {
                            isR18 = it
                            if (it) hideFromMinors = true
                        }
                        SettingsToggleRow("未成年には表示しない", "18歳未満から投稿を隠す", hideFromMinors) { hideFromMinors = it }
                    }
                }
                item {
                    SettingsPanel {
                        SettingsSectionTitle("予約投稿", "指定した日時に投稿")
                        SettingsToggleRow("予約する", "端末のタイムゾーンで指定", scheduledEnabled) { scheduledEnabled = it }
                        if (scheduledEnabled) {
                            Spacer(Modifier.height(8.dp))
                            SettingsDateField(scheduledText) { scheduledText = it }
                            Spacer(Modifier.height(7.dp))
                            SettingsTimePresets { seconds -> scheduledText = formatComposerDate(Instant.now().plusSeconds(seconds)) }
                        }
                    }
                }
                item {
                    SettingsPanel {
                        SettingsSectionTitle("自動削除", "指定した日時に投稿を削除")
                        SettingsToggleRow("自動削除する", "投稿後に自動で削除", expiresEnabled) { expiresEnabled = it }
                        if (expiresEnabled) {
                            Spacer(Modifier.height(8.dp))
                            SettingsDateField(expiresText) { expiresText = it }
                            Spacer(Modifier.height(7.dp))
                            SettingsTimePresets { seconds ->
                                val base = if (scheduledEnabled) parseComposerDate(scheduledText) ?: Instant.now() else Instant.now()
                                expiresText = formatComposerDate(base.plusSeconds(seconds))
                            }
                        }
                    }
                }
                validationError?.let { message ->
                    item { ErrorText(message) }
                }
            }
            Box(Modifier.fillMaxWidth().height(1.dp).background(Hairline))
            Column(
                Modifier.fillMaxWidth().background(Paper)
                    .padding(start = 18.dp, top = 12.dp, end = 18.dp, bottom = 56.dp)
            ) {
                Box(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp)).background(Carrot)
                        .clickable { save() }.padding(vertical = 13.dp),
                    contentAlignment = Alignment.Center
                ) {
                    KText("この設定を適用", 11, Color.White, FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun SettingsPanel(content: @Composable () -> Unit) {
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(Surface)
            .border(1.dp, Hairline, RoundedCornerShape(18.dp)).padding(14.dp)
    ) {
        content()
    }
}

@Composable
private fun SettingsSectionTitle(title: String, description: String) {
    KText(title, 13, Ink, FontWeight.Black)
    KText(description, 9, Muted)
    Spacer(Modifier.height(9.dp))
}

@Composable
private fun SettingsChoiceRow(options: List<Pair<String, String>>, selected: String, onSelect: (String) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        items(options) { (label, value) ->
            val active = selected == value
            Box(
                Modifier.clip(RoundedCornerShape(11.dp)).background(if (active) Strong else Surface)
                    .border(1.dp, if (active) Strong else Hairline, RoundedCornerShape(11.dp))
                    .clickable { onSelect(value) }.padding(horizontal = 12.dp, vertical = 8.dp)
            ) { KText(label, 9, if (active) OnStrong else Muted, FontWeight.Bold) }
        }
    }
}

@Composable
private fun CircleChoices(circles: List<ApiCircle>, selectedId: Long?, onSelect: (Long) -> Unit) {
    if (circles.isEmpty()) {
        KText("利用できるサークルがありません", 10, Muted)
    } else {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            items(circles, key = { it.id }) { circle ->
                val active = selectedId == circle.id
                Box(
                    Modifier.clip(RoundedCornerShape(11.dp)).background(if (active) Strong else Surface)
                        .border(1.dp, if (active) Strong else Hairline, RoundedCornerShape(11.dp))
                        .clickable { onSelect(circle.id) }.padding(horizontal = 12.dp, vertical = 8.dp)
                ) { KText(circle.name, 9, if (active) OnStrong else Muted, FontWeight.Bold) }
            }
        }
    }
}

@Composable
private fun CommunityDestinationChoices(
    communities: List<ApiCommunity>,
    selectedId: Long?,
    onSelect: (Long) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        items(communities, key = { it.id }) { community ->
            val active = selectedId == community.id
            Box(
                Modifier.clip(RoundedCornerShape(11.dp)).background(if (active) Strong else Surface)
                    .border(1.dp, if (active) Strong else Hairline, RoundedCornerShape(11.dp))
                    .clickable { onSelect(community.id) }.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                KText(community.name, 9, if (active) OnStrong else Muted, FontWeight.Bold, maxLines = 1)
            }
        }
    }
}

@Composable
private fun SettingsNullableNumberRow(values: List<Int>, selected: Int?, onSelect: (Int?) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        item {
            val active = selected == null
            Box(
                Modifier.clip(RoundedCornerShape(10.dp)).background(if (active) Strong else Surface)
                    .border(1.dp, if (active) Strong else Hairline, RoundedCornerShape(10.dp))
                    .clickable { onSelect(null) }.padding(horizontal = 10.dp, vertical = 7.dp)
            ) { KText("なし", 8, if (active) OnStrong else Muted, FontWeight.Bold) }
        }
        items(values) { age ->
            val active = selected == age
            Box(
                Modifier.clip(RoundedCornerShape(10.dp)).background(if (active) Strong else Surface)
                    .border(1.dp, if (active) Strong else Hairline, RoundedCornerShape(10.dp))
                    .clickable { onSelect(age) }.padding(horizontal = 10.dp, vertical = 7.dp)
            ) { KText("${age}歳", 8, if (active) OnStrong else Muted, FontWeight.Bold) }
        }
    }
}

@Composable
private fun SettingsToggleRow(title: String, description: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable { onChange(!checked) }.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            KText(title, 11, Ink, FontWeight.Bold)
            KText(description, 8, Muted)
        }
        Box(
            Modifier.width(42.dp).height(24.dp).clip(CircleShape)
                .background(if (checked) Carrot else Hairline).padding(3.dp)
        ) {
            Box(
                Modifier.align(if (checked) Alignment.CenterEnd else Alignment.CenterStart)
                    .size(18.dp).background(Color.White, CircleShape)
            )
        }
    }
}

@Composable
private fun SettingsDateField(value: String, onChange: (String) -> Unit) {
    BasicTextField(
        value = value,
        onValueChange = { onChange(it.take(16)) },
        singleLine = true,
        textStyle = TextStyle(Ink, 12.sp, FontWeight.Medium),
        modifier = Modifier.fillMaxWidth().height(42.dp).background(Surface, RoundedCornerShape(12.dp))
            .border(1.dp, Hairline, RoundedCornerShape(12.dp)).padding(horizontal = 12.dp),
        decorationBox = { inner ->
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                if (value.isBlank()) KText("yyyy/MM/dd HH:mm", 11, Muted)
                inner()
            }
        }
    )
}

@Composable
private fun SettingsTimePresets(onSelect: (Long) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        listOf(3600L to "1時間後", 10800L to "3時間後", 86400L to "1日後", 604800L to "7日後").forEach { (seconds, label) ->
            Box(
                Modifier.clip(RoundedCornerShape(9.dp)).background(Surface)
                    .border(1.dp, Hairline, RoundedCornerShape(9.dp))
                    .clickable { onSelect(seconds) }.padding(horizontal = 8.dp, vertical = 6.dp)
            ) { KText(label, 8, Muted, FontWeight.Bold) }
        }
    }
}

private enum class IconType {
    HOME, BOARD, SEARCH, COMMUNITY, BOT, PARODY, DM, PERSON, HEART, REPLY, BOOKMARK, BELL, IMAGE, TRASH, TROPHY,
    LOCK, PIN, POLL, CONTROLS, CALENDAR, REFRESH, CHEVRON_UP, CHEVRON_DOWN,
    PLUS, BACK, CLOSE, SEND, CHECK, REKAROT, FORWARD, EYE,
    THEME, LICENSE, INFO, LOGOUT, PROFILE_EDIT, ACCOUNT_SWITCH, VOLUME_ON, VOLUME_OFF, BLOCK, MORE, VERIFIED,
    LINK, MAP_PIN, QUESTION
}

@Composable
private fun CustomIcon(type: IconType, color: Color, size: Dp, filled: Boolean = false) {
    val vector: ImageVector = when (type) {
        IconType.HOME -> if (filled) PhosphorIcons.Fill.FilledHouse else PhosphorIcons.Regular.RegularHouse
        IconType.BOARD -> PhosphorIcons.Regular.ChatCircle
        IconType.SEARCH -> PhosphorIcons.Regular.MagnifyingGlass
        IconType.COMMUNITY -> PhosphorIcons.Regular.UsersThree
        IconType.BOT -> PhosphorIcons.Regular.Robot
        IconType.PARODY -> PhosphorIcons.Regular.MaskHappy
        IconType.DM -> PhosphorIcons.Regular.EnvelopeSimple
        IconType.PERSON -> if (filled) PhosphorIcons.Fill.FilledUser else PhosphorIcons.Regular.RegularUser
        IconType.HEART -> if (filled) PhosphorIcons.Fill.FilledHeart else PhosphorIcons.Regular.RegularHeart
        IconType.REPLY -> PhosphorIcons.Regular.ArrowBendUpLeft
        IconType.BOOKMARK -> if (filled) PhosphorIcons.Fill.FilledBookmark else PhosphorIcons.Regular.RegularBookmark
        IconType.BELL -> PhosphorIcons.Regular.Bell
        IconType.IMAGE -> PhosphorIcons.Regular.RegularImage
        IconType.TRASH -> PhosphorIcons.Regular.Trash
        IconType.TROPHY -> PhosphorIcons.Regular.Trophy
        IconType.LOCK -> PhosphorIcons.Regular.Lock
        IconType.PIN -> PhosphorIcons.Regular.PushPin
        IconType.POLL -> PhosphorIcons.Regular.ListChecks
        IconType.CONTROLS -> PhosphorIcons.Regular.SlidersHorizontal
        IconType.CALENDAR -> PhosphorIcons.Regular.CalendarBlank
        IconType.REFRESH -> PhosphorIcons.Regular.ArrowClockwise
        IconType.CHEVRON_UP -> PhosphorIcons.Regular.CaretUp
        IconType.CHEVRON_DOWN -> PhosphorIcons.Regular.CaretDown
        IconType.PLUS -> PhosphorIcons.Regular.Plus
        IconType.BACK -> PhosphorIcons.Regular.ArrowLeft
        IconType.CLOSE -> PhosphorIcons.Regular.X
        IconType.SEND -> PhosphorIcons.Regular.ArrowUp
        IconType.CHECK -> PhosphorIcons.Regular.Check
        IconType.REKAROT -> PhosphorIcons.Regular.Repeat
        IconType.FORWARD -> PhosphorIcons.Regular.ArrowRight
        IconType.EYE -> PhosphorIcons.Regular.RegularEye
        IconType.THEME -> PhosphorIcons.Regular.MoonStars
        IconType.LICENSE -> PhosphorIcons.Regular.Scroll
        IconType.INFO -> PhosphorIcons.Regular.Info
        IconType.LOGOUT -> PhosphorIcons.Regular.SignOut
        IconType.PROFILE_EDIT -> PhosphorIcons.Regular.UserCircleGear
        IconType.ACCOUNT_SWITCH -> PhosphorIcons.Regular.UserSwitch
        IconType.VOLUME_ON -> PhosphorIcons.Regular.SpeakerHigh
        IconType.VOLUME_OFF -> PhosphorIcons.Regular.SpeakerSlash
        IconType.BLOCK -> PhosphorIcons.Regular.Prohibit
        IconType.MORE -> PhosphorIcons.Regular.DotsThreeVertical
        IconType.VERIFIED -> PhosphorIcons.Regular.SealCheck
        IconType.LINK -> PhosphorIcons.Regular.LinkSimple
        IconType.MAP_PIN -> PhosphorIcons.Regular.MapPin
        IconType.QUESTION -> PhosphorIcons.Regular.ChatCircle
    }
    Image(
        imageVector = vector,
        contentDescription = null,
        modifier = Modifier.size(size),
        colorFilter = ColorFilter.tint(color)
    )
    return
    Canvas(Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = Stroke(w * .09f, cap = StrokeCap.Round)
        when (type) {
            IconType.HOME -> {
                val p = Path().apply { moveTo(w * .12f, h * .48f); lineTo(w * .5f, h * .15f); lineTo(w * .88f, h * .48f); lineTo(w * .82f, h * .88f); lineTo(w * .18f, h * .88f); close() }
                drawPath(p, color, style = if (filled) androidx.compose.ui.graphics.drawscope.Fill else stroke)
            }
            IconType.BOARD -> { drawRoundRect(color, Offset(w*.12f,h*.16f), Size(w*.76f,h*.61f), CornerRadius(w*.13f), style = stroke); drawLine(color, Offset(w*.3f,h*.78f), Offset(w*.2f,h*.92f), strokeWidth = stroke.width, cap = StrokeCap.Round) }
            IconType.SEARCH -> { drawCircle(color, w*.3f, Offset(w*.43f,h*.42f), style=stroke); drawLine(color, Offset(w*.65f,h*.65f), Offset(w*.88f,h*.88f), strokeWidth=stroke.width, cap=StrokeCap.Round) }
            IconType.DM -> {
                drawRoundRect(
                    color,
                    Offset(w * .09f, h * .2f),
                    Size(w * .82f, h * .62f),
                    CornerRadius(w * .11f),
                    style = stroke
                )
                drawLine(color, Offset(w*.14f,h*.27f), Offset(w*.5f,h*.57f), strokeWidth=stroke.width, cap=StrokeCap.Round)
                drawLine(color, Offset(w*.86f,h*.27f), Offset(w*.5f,h*.57f), strokeWidth=stroke.width, cap=StrokeCap.Round)
                drawLine(color, Offset(w*.14f,h*.76f), Offset(w*.38f,h*.55f), strokeWidth=stroke.width, cap=StrokeCap.Round)
                drawLine(color, Offset(w*.86f,h*.76f), Offset(w*.62f,h*.55f), strokeWidth=stroke.width, cap=StrokeCap.Round)
            }
            IconType.PERSON -> { drawCircle(color, w*.2f, Offset(w*.5f,h*.3f), style=stroke); drawArc(color, 195f, 150f, false, Offset(w*.15f,h*.48f), Size(w*.7f,h*.48f), style=stroke) }
            IconType.HEART -> {
                val p = Path().apply { moveTo(w*.5f,h*.86f); cubicTo(w*.2f,h*.67f,w*.08f,h*.48f,w*.17f,h*.28f); cubicTo(w*.25f,h*.1f,w*.43f,h*.14f,w*.5f,h*.3f); cubicTo(w*.57f,h*.14f,w*.75f,h*.1f,w*.83f,h*.28f); cubicTo(w*.92f,h*.48f,w*.8f,h*.67f,w*.5f,h*.86f); close() }
                drawPath(p,color,style=if(filled) androidx.compose.ui.graphics.drawscope.Fill else stroke)
            }
            IconType.REPLY -> { drawArc(color, 190f, 230f, false, Offset(w*.18f,h*.2f), Size(w*.65f,h*.57f), style=stroke); drawLine(color, Offset(w*.23f,h*.22f), Offset(w*.12f,h*.48f), strokeWidth=stroke.width, cap=StrokeCap.Round); drawLine(color, Offset(w*.12f,h*.48f), Offset(w*.39f,h*.49f), strokeWidth=stroke.width, cap=StrokeCap.Round) }
            IconType.BOOKMARK -> { val p=Path().apply{moveTo(w*.23f,h*.12f);lineTo(w*.77f,h*.12f);lineTo(w*.77f,h*.88f);lineTo(w*.5f,h*.7f);lineTo(w*.23f,h*.88f);close()};drawPath(p,color,style=if(filled) androidx.compose.ui.graphics.drawscope.Fill else stroke) }
            IconType.BELL -> { drawArc(color,180f,180f,false,Offset(w*.22f,h*.2f),Size(w*.56f,h*.58f),style=stroke); drawLine(color,Offset(w*.16f,h*.75f),Offset(w*.84f,h*.75f),strokeWidth=stroke.width,cap=StrokeCap.Round); drawCircle(color,w*.05f,Offset(w*.5f,h*.9f)) }
            IconType.IMAGE -> { drawRoundRect(color,Offset(w*.1f,h*.14f),Size(w*.8f,h*.72f),CornerRadius(w*.1f),style=stroke); drawCircle(color,w*.09f,Offset(w*.34f,h*.38f),style=stroke); val p=Path().apply{moveTo(w*.18f,h*.74f);lineTo(w*.42f,h*.52f);lineTo(w*.58f,h*.66f);lineTo(w*.73f,h*.47f);lineTo(w*.87f,h*.66f)};drawPath(p,color,style=stroke) }
            IconType.TRASH -> { drawRoundRect(color,Offset(w*.25f,h*.28f),Size(w*.5f,h*.58f),CornerRadius(w*.06f),style=stroke); drawLine(color,Offset(w*.17f,h*.22f),Offset(w*.83f,h*.22f),strokeWidth=stroke.width,cap=StrokeCap.Round); drawLine(color,Offset(w*.39f,h*.12f),Offset(w*.61f,h*.12f),strokeWidth=stroke.width,cap=StrokeCap.Round); drawLine(color,Offset(w*.4f,h*.4f),Offset(w*.4f,h*.72f),strokeWidth=stroke.width,cap=StrokeCap.Round); drawLine(color,Offset(w*.6f,h*.4f),Offset(w*.6f,h*.72f),strokeWidth=stroke.width,cap=StrokeCap.Round) }
            IconType.TROPHY -> {
                drawRoundRect(color, Offset(w*.25f,h*.1f), Size(w*.5f,h*.45f), CornerRadius(w*.08f), style=stroke)
                drawArc(color, 90f, 180f, false, Offset(w*.07f,h*.16f), Size(w*.3f,h*.34f), style=stroke)
                drawArc(color, 270f, 180f, false, Offset(w*.63f,h*.16f), Size(w*.3f,h*.34f), style=stroke)
                drawLine(color, Offset(w*.5f,h*.55f), Offset(w*.5f,h*.78f), strokeWidth=stroke.width, cap=StrokeCap.Round)
                drawLine(color, Offset(w*.31f,h*.88f), Offset(w*.69f,h*.88f), strokeWidth=stroke.width, cap=StrokeCap.Round)
            }
            IconType.LOCK -> {
                drawRoundRect(color, Offset(w*.18f, h*.43f), Size(w*.64f, h*.46f), CornerRadius(w*.1f), style = stroke)
                drawArc(color, 180f, 180f, false, Offset(w*.3f, h*.1f), Size(w*.4f, h*.55f), style = stroke)
            }
            IconType.PIN -> {
                drawRoundRect(color, Offset(w*.28f, h*.1f), Size(w*.44f, h*.3f), CornerRadius(w*.08f), style = stroke)
                drawLine(color, Offset(w*.34f, h*.4f), Offset(w*.23f, h*.62f), strokeWidth=stroke.width, cap=StrokeCap.Round)
                drawLine(color, Offset(w*.66f, h*.4f), Offset(w*.77f, h*.62f), strokeWidth=stroke.width, cap=StrokeCap.Round)
                drawLine(color, Offset(w*.2f, h*.62f), Offset(w*.8f, h*.62f), strokeWidth=stroke.width, cap=StrokeCap.Round)
                drawLine(color, Offset(w*.5f, h*.62f), Offset(w*.5f, h*.92f), strokeWidth=stroke.width, cap=StrokeCap.Round)
            }
            IconType.POLL -> {
                listOf(.24f, .5f, .76f).forEach { y ->
                    drawCircle(color, w*.07f, Offset(w*.17f, h*y), style=stroke)
                    drawLine(color, Offset(w*.34f, h*y), Offset(w*.88f, h*y), strokeWidth=stroke.width, cap=StrokeCap.Round)
                }
            }
            IconType.CONTROLS -> {
                drawLine(color, Offset(w*.12f, h*.25f), Offset(w*.88f, h*.25f), strokeWidth=stroke.width, cap=StrokeCap.Round)
                drawLine(color, Offset(w*.12f, h*.5f), Offset(w*.88f, h*.5f), strokeWidth=stroke.width, cap=StrokeCap.Round)
                drawLine(color, Offset(w*.12f, h*.75f), Offset(w*.88f, h*.75f), strokeWidth=stroke.width, cap=StrokeCap.Round)
                drawCircle(Surface, w*.1f, Offset(w*.35f, h*.25f))
                drawCircle(color, w*.1f, Offset(w*.35f, h*.25f), style=stroke)
                drawCircle(Surface, w*.1f, Offset(w*.67f, h*.5f))
                drawCircle(color, w*.1f, Offset(w*.67f, h*.5f), style=stroke)
                drawCircle(Surface, w*.1f, Offset(w*.46f, h*.75f))
                drawCircle(color, w*.1f, Offset(w*.46f, h*.75f), style=stroke)
            }
            IconType.CALENDAR -> {
                drawRoundRect(color, Offset(w*.12f, h*.19f), Size(w*.76f, h*.69f), CornerRadius(w*.1f), style=stroke)
                drawLine(color, Offset(w*.12f, h*.4f), Offset(w*.88f, h*.4f), strokeWidth=stroke.width, cap=StrokeCap.Round)
                drawLine(color, Offset(w*.31f, h*.1f), Offset(w*.31f, h*.28f), strokeWidth=stroke.width, cap=StrokeCap.Round)
                drawLine(color, Offset(w*.69f, h*.1f), Offset(w*.69f, h*.28f), strokeWidth=stroke.width, cap=StrokeCap.Round)
            }
            IconType.REFRESH -> {
                drawArc(color, 36f, 286f, false, Offset(w*.13f, h*.13f), Size(w*.74f, h*.74f), style=stroke)
                drawLine(color, Offset(w*.77f, h*.13f), Offset(w*.88f, h*.34f), strokeWidth=stroke.width, cap=StrokeCap.Round)
                drawLine(color, Offset(w*.77f, h*.13f), Offset(w*.58f, h*.18f), strokeWidth=stroke.width, cap=StrokeCap.Round)
            }
            IconType.CHEVRON_UP -> {
                drawLine(color, Offset(w*.18f, h*.66f), Offset(w*.5f, h*.34f), strokeWidth=stroke.width, cap=StrokeCap.Round)
                drawLine(color, Offset(w*.5f, h*.34f), Offset(w*.82f, h*.66f), strokeWidth=stroke.width, cap=StrokeCap.Round)
            }
            IconType.CHEVRON_DOWN -> {
                drawLine(color, Offset(w*.18f, h*.34f), Offset(w*.5f, h*.66f), strokeWidth=stroke.width, cap=StrokeCap.Round)
                drawLine(color, Offset(w*.5f, h*.66f), Offset(w*.82f, h*.34f), strokeWidth=stroke.width, cap=StrokeCap.Round)
            }
            else -> Unit
        }
    }
}

private val MentionPattern = Regex("(?<![\\w@])@([A-Za-z0-9_]{1,30})")
private val HashtagPattern = Regex("(?<![\\w/#])#([\\p{L}\\p{N}_]{1,50})")
private val SingleDollarMathPattern = Regex("(?<!\\$)\\$([^\\n$]+)\\$(?!\\$)")
private val InlineLatexPattern = Regex("\\\\\\((.+?)\\\\\\)")
private val BlockLatexPattern = Regex("\\\\\\[([\\s\\S]+?)\\\\]")
private val ProtectedMarkdownPattern = Regex("(?s)(```.*?```|`[^`\\n]*`|\\$\\$.*?\\$\\$)")
private val ProtectedMarkdownTokenPattern = Regex("\\uE000(\\d+)\\uE001")
private val QuotedRubyPattern = Regex("\"([^\"\\n]{1,60})\"《([^《》\\n]{1,80})》")
private val BarRubyPattern = Regex("[|｜]([^《\\n]{1,60})《([^《》\\n]{1,80})》")

private fun richMarkdown(source: String): String {
    var value = source.take(20_000)
        .replace("\r\n", "\n")
        .replace('\r', '\n')
    value = BlockLatexPattern.replace(value) { match ->
        "\$\$\n${match.groupValues[1].trim()}\n\$\$"
    }
    value = InlineLatexPattern.replace(value) { match ->
        "\$\$${match.groupValues[1]}\$\$"
    }
    value = SingleDollarMathPattern.replace(value) { "\$\$${it.groupValues[1]}\$\$" }
    val protected = mutableListOf<String>()
    value = ProtectedMarkdownPattern.replace(value) { match ->
        protected += match.value
        "\uE000${protected.lastIndex}\uE001"
    }
    value = HashtagPattern.replace(value) { match ->
        val tag = match.groupValues[1]
        "[#$tag](karotter://tag/${Uri.encode(tag)})"
    }
    value = MentionPattern.replace(value) { match ->
        val username = match.groupValues[1]
        "[@$username](karotter://user/$username)"
    }
    return ProtectedMarkdownTokenPattern.replace(value) { match ->
        protected.getOrNull(match.groupValues[1].toIntOrNull() ?: -1).orEmpty()
    }
}

private fun applyRubySpans(view: TextView, textColor: Int, textSizePx: Float) {
    val source = view.text.toString()
    val matches = (QuotedRubyPattern.findAll(source) + BarRubyPattern.findAll(source))
        .distinctBy { it.range }
        .sortedByDescending { it.range.first }
        .toList()
    if (matches.isEmpty()) return
    val builder = SpannableStringBuilder(view.text)
    matches.forEach { match ->
        val base = match.groupValues[1]
        val ruby = match.groupValues[2]
        val start = match.range.first
        val endExclusive = match.range.last + 1
        builder.replace(start, endExclusive, base)
        builder.setSpan(
            RubySpan(ruby, textSizePx * .48f, textColor),
            start,
            start + base.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    view.text = builder
}

private class RubySpan(
    private val ruby: String,
    private val rubyTextSize: Float,
    private val rubyColor: Int
) : ReplacementSpan() {
    private fun rubyPaint(base: Paint): Paint = Paint(base).apply {
        textSize = rubyTextSize
        color = rubyColor
        isFakeBoldText = false
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        val rubyPaint = rubyPaint(paint)
        val width = maxOf(
            paint.measureText(text, start, end),
            rubyPaint.measureText(ruby)
        )
        fm?.let {
            val baseMetrics = paint.fontMetricsInt
            val rubyMetrics = rubyPaint.fontMetricsInt
            val rubyHeight = rubyMetrics.descent - rubyMetrics.ascent
            it.ascent = baseMetrics.ascent - rubyHeight - 2
            it.top = minOf(baseMetrics.top, it.ascent)
            it.descent = baseMetrics.descent
            it.bottom = baseMetrics.bottom
            it.leading = baseMetrics.leading
        }
        return width.toInt().coerceAtLeast(1)
    }

    override fun draw(
        canvas: android.graphics.Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val rubyPaint = rubyPaint(paint)
        val baseWidth = paint.measureText(text, start, end)
        val rubyWidth = rubyPaint.measureText(ruby)
        val boxWidth = maxOf(baseWidth, rubyWidth)
        canvas.drawText(ruby, x + (boxWidth - rubyWidth) / 2f, y + paint.ascent() - rubyPaint.descent() - 1f, rubyPaint)
        canvas.drawText(text, start, end, x + (boxWidth - baseWidth) / 2f, y.toFloat(), paint)
    }
}

@Composable
private fun RichContentText(
    text: String,
    size: Int,
    color: Color,
    lineHeight: Float = size * 1.35f,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    onPlainTextClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val mentionHandler = LocalMentionHandler.current
    val hashtagHandler = LocalHashtagHandler.current
    val density = LocalDensity.current
    val textPx = with(density) { size.sp.toPx() }
    val lineExtra = with(density) { (lineHeight - size).coerceAtLeast(0f).sp.toPx() }
    val markwon = remember(context, textPx, color, mentionHandler, hashtagHandler) {
        Markwon.builder(context)
            .usePlugin(MarkwonInlineParserPlugin.create())
            .usePlugin(SoftBreakAddsNewLinePlugin.create())
            .usePlugin(LinkifyPlugin.create())
            .usePlugin(io.noties.markwon.ext.strikethrough.StrikethroughPlugin.create())
            .usePlugin(io.noties.markwon.ext.tables.TablePlugin.create(context))
            .usePlugin(io.noties.markwon.ext.tasklist.TaskListPlugin.create(context))
            .usePlugin(JLatexMathPlugin.create(textPx) { builder ->
                builder.inlinesEnabled(true)
                builder.blocksEnabled(true)
                builder.blocksLegacy(false)
                builder.theme().textColor(color.toArgb())
            })
            .usePlugin(object : io.noties.markwon.AbstractMarkwonPlugin() {
                override fun configureConfiguration(builder: io.noties.markwon.MarkwonConfiguration.Builder) {
                    builder.linkResolver(LinkResolver { _, link ->
                        if (link.startsWith("karotter://user/") && mentionHandler != null) {
                            mentionHandler.invoke(link.substringAfterLast('/'))
                        } else if (link.startsWith("karotter://tag/") && hashtagHandler != null) {
                            hashtagHandler.invoke(Uri.decode(link.substringAfterLast('/')))
                        } else {
                            runCatching { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
                        }
                    })
                }
            })
            .build()
    }
    AndroidView(
        modifier = modifier,
        factory = { ctx -> TextView(ctx).apply { includeFontPadding = false; setBackgroundColor(android.graphics.Color.TRANSPARENT) } },
        update = { view ->
            view.setTextColor(color.toArgb())
            view.setLinkTextColor(Carrot.toArgb())
            view.textSize = size.toFloat()
            view.setLineSpacing(lineExtra, 1f)
            view.maxLines = maxLines
            view.ellipsize = if (maxLines == Int.MAX_VALUE) null else TextUtils.TruncateAt.END
            runCatching {
                markwon.setMarkdown(view, richMarkdown(text))
                applyRubySpans(view, color.toArgb(), textPx)
            }
                .onFailure { view.text = text.take(20_000) }
            if (onPlainTextClick != null) {
                val current = view.movementMethod
                if (current is RichContentMovementMethod) {
                    current.onPlainTextClick = onPlainTextClick
                } else {
                    view.movementMethod = RichContentMovementMethod(onPlainTextClick)
                }
            }
        }
    )
}

private class RichContentMovementMethod(
    var onPlainTextClick: (() -> Unit)?
) : LinkMovementMethod() {
    override fun onTouchEvent(widget: TextView, buffer: Spannable, event: MotionEvent): Boolean {
        val layout = widget.layout
        val x = (event.x - widget.totalPaddingLeft + widget.scrollX).toInt()
        val y = (event.y - widget.totalPaddingTop + widget.scrollY).toInt()
        val hasLink = if (layout != null && x >= 0 && y >= 0 && y <= layout.height) {
            val line = layout.getLineForVertical(y)
            val offset = layout.getOffsetForHorizontal(line, x.toFloat())
            buffer.getSpans(offset, offset, ClickableSpan::class.java).isNotEmpty()
        } else {
            false
        }
        if (hasLink) return super.onTouchEvent(widget, buffer, event)
        if (event.action == MotionEvent.ACTION_UP) onPlainTextClick?.invoke()
        return true
    }
}

@Composable
private fun KText(
    text: String,
    size: Int,
    color: Color,
    weight: FontWeight = FontWeight.Normal,
    lineHeight: Float = size * 1.35f,
    letterSpacing: Float = 0f,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign = TextAlign.Unspecified
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = TextStyle(
            color = color,
            fontSize = size.sp,
            fontWeight = weight,
            lineHeight = lineHeight.sp,
            letterSpacing = letterSpacing.sp,
            fontFamily = FontFamily.SansSerif,
            textAlign = textAlign
        ),
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}
