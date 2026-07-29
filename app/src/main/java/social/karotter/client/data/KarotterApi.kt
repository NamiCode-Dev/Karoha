package social.karotter.client.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.UUID

private const val API_BASE = "https://api.karotter.com/api/"
private const val DM_API_BASE = "https://api.karotter.com/api/dm/"
private const val NETWORK_UNAVAILABLE_STATUS = -1
private const val CLIENT_DEVICE_NAME = "Karoha"
const val MEDIA_BASE = "https://karotter.com"

data class ApiUser(
    val id: Long,
    val username: String,
    val displayName: String,
    val avatarUrl: String?,
    val headerUrl: String?,
    val bio: String,
    val websiteUrl: String?,
    val showLikedPosts: Boolean,
    val followersCount: Int,
    val followingCount: Int,
    val postsCount: Int,
    val officialMark: String = "NONE",
    val isBotAccount: Boolean = false,
    val isParodyAccount: Boolean = false,
    val isFollowing: Boolean = false,
    val followRequestSent: Boolean = false,
    val level: Int? = null,
    val levelProgressPercent: Int? = null,
    val joinedAt: String? = null,
    val canReceiveDm: Boolean = true,
    val pinnedPost: ApiPost? = null,
    val isPrivate: Boolean = false,
    val isFollowedBy: Boolean = false,
    val location: String = "",
    val onlineStatus: String = "OFFLINE",
    val statusMessage: String = "",
    val subscriptionPlan: String = "FREE",
    val subscriptionStatus: String = "INACTIVE",
    val subscriptionBadgeColors: List<String> = emptyList(),
    val showSubscriptionBadges: Boolean = true,
    val showPlusBadge: Boolean = true,
    val showProBadge: Boolean = true,
    val premiumBadgeColor: String = "ORANGE",
    val showProfileDecoration: Boolean = true,
    val showCardDecoration: Boolean = true,
    val profileAccentColor: String? = null,
    val cardAccentColor: String? = null,
    val isPremium: Boolean = false,
    val pinnedPosts: List<ApiPost> = emptyList(),
    val officialMarks: List<String> = emptyList(),
    val isMuted: Boolean = false,
    val isBlocked: Boolean = false,
    val isBlockedBy: Boolean = false,
    val isPostNotificationsEnabled: Boolean = false,
    val isBanned: Boolean = false,
    val pinnedPostLimit: Int = 1,
    val questionsEnabled: Boolean = false,
    val profileMinimumAge: Int? = null,
    val profileMaximumAge: Int? = null,
    val hideProfileFromMinors: Boolean = false,
    val profileUnavailableReason: String? = null,
    val profileUnavailableDetails: List<String> = emptyList()
)

data class ApiMedia(
    val url: String,
    val type: String,
    val alt: String,
    val spoiler: Boolean = false
)
data class ApiReaction(val emoji: String, val count: Int, val reacted: Boolean)
data class ApiPollOption(
    val id: Long,
    val text: String,
    val votesCount: Int,
    val percentage: Int,
    val votedByMe: Boolean
)
data class ApiPoll(
    val id: Long,
    val expiresAt: String,
    val isExpired: Boolean,
    val isAnonymous: Boolean,
    val totalVotes: Int,
    val ownVoteOptionId: Long?,
    val options: List<ApiPollOption>
)
data class ApiUploadMedia(val fileName: String, val mimeType: String, val bytes: ByteArray)

data class ApiPost(
    val id: Long,
    val content: String,
    val createdAt: String,
    val author: ApiUser,
    val likesCount: Int,
    val repliesCount: Int,
    val rekarotsCount: Int,
    val liked: Boolean,
    val rekaroted: Boolean,
    val bookmarked: Boolean,
    val mediaUrl: String?,
    val media: List<ApiMedia> = emptyList(),
    val quotedPost: ApiPost? = null,
    val rekarotedBy: ApiUser? = null,
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
    val scheduledFor: String? = null,
    val expiresAt: String? = null,
    val viewsCount: Int = 0,
    val bookmarksCount: Int = 0,
    val quoteUsersCount: Int = 0,
    val parentId: Long? = null,
    val canQuote: Boolean = true
)

data class ApiStory(
    val id: Long,
    val author: ApiUser,
    val mediaUrl: String?,
    val caption: String,
    val viewed: Boolean,
    val liked: Boolean,
    val textOverlay: String? = null,
    val textOverlayStyle: ApiStoryTextStyle? = null,
    val viewsCount: Int = 0,
    val likesCount: Int = 0,
    val commentsCount: Int = 0
)

data class ApiStoryTextStyle(
    val x: Float = 50f,
    val y: Float = 72f,
    val size: Int = 28,
    val align: String = "center",
    val color: String = "#ffffff",
    val background: String = "soft"
)

data class ApiStoryComment(
    val id: Long,
    val storyId: Long,
    val content: String,
    val createdAt: String,
    val author: ApiUser?
)

data class ApiLinkPreview(
    val url: String,
    val title: String,
    val description: String,
    val imageUrl: String?,
    val siteName: String
)

data class ApiBoard(
    val slug: String,
    val name: String,
    val description: String,
    val threadCount: Int,
    val ownerId: Long? = null
)
data class ApiThread(val id: Long, val title: String, val content: String, val repliesCount: Int, val authorName: String)
data class ApiBoardReply(val id: Long, val number: Int, val content: String, val authorName: String, val createdAt: String, val media: List<ApiMedia>)
data class ApiThreadDetail(val thread: ApiThread, val media: List<ApiMedia>, val replies: List<ApiBoardReply>)
data class ApiArticle(val slug: String, val title: String, val summary: String, val category: String, val createdAt: String, val imageUrl: String?, val content: String = "")
data class ApiNotification(
    val id: String,
    val type: String,
    val actorName: String,
    val actorAvatarUrl: String?,
    val message: String,
    val createdAt: String,
    val post: ApiPost?,
    val actorUsername: String? = null
)
data class ApiQuestion(
    val id: Long,
    val content: String,
    val createdAt: String,
    val sender: ApiUser?,
    val answeredPost: ApiPost?
)
data class ApiPostPage(val posts: List<ApiPost>, val nextPage: Int?, val nextCursor: Long?, val hasNext: Boolean)
data class ApiUserPage(val users: List<ApiUser>, val nextPage: Int?, val hasNext: Boolean)
data class ApiFollowRequest(val id: Long, val user: ApiUser, val createdAt: String)
data class ApiFollowRequestPage(val requests: List<ApiFollowRequest>, val nextPage: Int?, val hasNext: Boolean)
data class ApiProPreferences(
    val plan: String,
    val status: String,
    val canCustomizeProfile: Boolean,
    val canCustomizeCards: Boolean,
    val pinnedPostLimit: Int,
    val premiumBadgeColor: String,
    val profileAccentColor: String?,
    val cardAccentColor: String?
)
data class ApiUserConnectionPage(val users: List<ApiUser>, val nextCursor: String?, val hasNext: Boolean)
data class ApiLevelRankingEntry(
    val rank: Int,
    val user: ApiUser,
    val experience: Int,
    val experienceInLevel: Int,
    val experienceRequiredForNextLevel: Int,
    val experienceToNextLevel: Int
)
data class ApiTrend(val label: String, val count: Int)
data class ApiCircle(val id: Long, val name: String, val memberCount: Int)
data class ApiCommunity(
    val id: Long,
    val name: String,
    val description: String,
    val headerImageUrl: String?,
    val ownerId: Long?,
    val joinType: String,
    val memberCount: Int,
    val isMember: Boolean,
    val isInvited: Boolean,
    val canPost: Boolean,
    val rules: List<String> = emptyList()
)
data class ApiCommunityGroups(
    val joined: List<ApiCommunity>,
    val owned: List<ApiCommunity>,
    val recommended: List<ApiCommunity>
)
data class ApiCommunityMember(val id: Long, val role: String, val joinedAt: String, val user: ApiUser)
data class ApiCommunityMemberPage(val members: List<ApiCommunityMember>, val nextPage: Int?, val hasNext: Boolean)
data class ApiSearchResult(val users: List<ApiUser>, val posts: List<ApiPost>)
data class ApiDmGroup(
    val id: Long,
    val members: List<ApiUser>,
    val lastMessage: String,
    val lastMessageAt: String,
    val canSend: Boolean,
    val isRequest: Boolean = false,
    val requestStatus: String = "",
    val sendDisabledReason: String = "",
    val unreadCount: Int = 0,
    val name: String = ""
)
data class ApiDmMessage(
    val id: Long,
    val groupId: Long,
    val senderId: Long,
    val content: String,
    val createdAt: String,
    val sender: ApiUser?,
    val media: List<ApiMedia> = emptyList()
)

sealed interface ApiResult<out T> {
    data class Success<T>(val value: T) : ApiResult<T>
    data class Failure(val message: String, val status: Int? = null) : ApiResult<Nothing>
}

sealed interface ApiLoginResult {
    data class Success(val user: ApiUser) : ApiLoginResult
    data class TwoFactorRequired(val token: String) : ApiLoginResult
    data class Failure(val message: String, val status: Int? = null) : ApiLoginResult
}

class SessionStore(context: Context) {
    private val prefs = context.getSharedPreferences("karotter_session_v1", Context.MODE_PRIVATE)
    val deviceId: String
        get() = prefs.getString("device_id", null) ?: UUID.randomUUID().toString().also {
            prefs.edit().putString("device_id", it).apply()
        }

    var accessToken: String?
        get() = prefs.getString("access_token", null)
        set(value) { prefs.edit().putString("access_token", value).apply() }

    var sessionId: String?
        get() = prefs.getString("session_id", null)
        set(value) {
            prefs.edit().apply {
                if (value.isNullOrBlank()) remove("session_id") else putString("session_id", value)
            }.apply()
        }

    var csrfToken: String?
        get() = prefs.getString("csrf_token", null)
        set(value) {
            prefs.edit().apply {
                if (value.isNullOrBlank()) remove("csrf_token") else putString("csrf_token", value)
            }.apply()
        }

    fun hasSession(): Boolean = accessToken != null || prefs.contains("cookie_karotter_rt")

    fun hasRefreshCookie(): Boolean = !prefs.getString("cookie_karotter_rt", null).isNullOrBlank()

    fun hasAuthenticationCookie(): Boolean =
        !prefs.getString("cookie_karotter_at", null).isNullOrBlank()

    fun cookieHeader(): String = listOf("karotter_at", "karotter_rt", "karotter_csrf")
        .mapNotNull { name -> prefs.getString("cookie_$name", null)?.let { "$name=$it" } }
        .joinToString("; ")

    fun saveCookies(headers: Map<String?, List<String>>) {
        val editor = prefs.edit()
        headers.entries.filter { it.key?.equals("Set-Cookie", true) == true }.flatMap { it.value }.forEach { raw ->
            val pair = raw.substringBefore(';').split('=', limit = 2)
            if (pair.size == 2 && pair[0] in setOf("karotter_at", "karotter_rt", "karotter_csrf")) {
                if (pair[1].isBlank()) editor.remove("cookie_${pair[0]}") else editor.putString("cookie_${pair[0]}", pair[1])
            }
        }
        editor.apply()
    }

    fun clear() {
        val id = deviceId
        prefs.edit().clear().putString("device_id", id).apply()
    }
}

class KarotterApi(context: Context) {
    private val appContext = context.applicationContext
    val session = SessionStore(context.applicationContext)
    private val credentials = CredentialVault(context.applicationContext)
    private val authenticationRecoveryLock = Any()
    private val linkPreviewCache = mutableMapOf<String, ApiLinkPreview>()
    private var lastAutomaticReloginAt = 0L
    var onAuthenticationLost: (() -> Unit)? = null
    var onDataAccessFailure: ((String) -> Unit)? = null

    fun hasNetworkConnection(): Boolean = appContext.hasValidatedInternet()

    fun beginLogin(
        identifier: String,
        password: String,
        rememberCredentials: Boolean = true
    ): ApiLoginResult {
        val body = JSONObject()
            .put("identifier", identifier)
            .put("password", password)
            .put("gender", "OTHER")
            .put("deviceId", session.deviceId)
            .put("clientType", "android")
            .put("deviceName", CLIENT_DEVICE_NAME)
        return when (val result = request("auth/login", "POST", body.toString(), authenticated = false, retryAuth = false)) {
            is ApiResult.Failure -> ApiLoginResult.Failure(
                buildString {
                    append(result.message.ifBlank { "ログインできませんでした" })
                    result.status?.let { append("（HTTP $it）") }
                },
                result.status
            )
            is ApiResult.Success -> {
                val json = result.value
                if (json.optBoolean("twoFactorRequired")) {
                    val token = json.optString("twoFactorToken").trim()
                    if (token.isBlank()) {
                        ApiLoginResult.Failure("二段階認証を開始できませんでした")
                    } else {
                        ApiLoginResult.TwoFactorRequired(token)
                    }
                } else {
                    finishLogin(json, identifier, password, rememberCredentials)
                }
            }
        }
    }

    fun completeTwoFactorLogin(
        identifier: String,
        password: String,
        twoFactorToken: String,
        code: String,
        rememberCredentials: Boolean = true
    ): ApiLoginResult {
        val normalizedCode = code.trim()
        if (!normalizedCode.matches(Regex("\\d{6}"))) {
            return ApiLoginResult.Failure("6桁の認証コードを入力してください")
        }
        val body = JSONObject()
            .put("twoFactorToken", twoFactorToken)
            .put("code", normalizedCode)
            .put("deviceId", session.deviceId)
            .put("clientType", "android")
            .put("deviceName", CLIENT_DEVICE_NAME)
        return when (
            val result = request(
                "auth/login/2fa",
                "POST",
                body.toString(),
                authenticated = false,
                retryAuth = false
            )
        ) {
            is ApiResult.Failure -> ApiLoginResult.Failure(
                when (result.status) {
                    400, 401 -> "認証コードが正しくありません"
                    else -> result.message.ifBlank { "二段階認証に失敗しました" }
                },
                result.status
            )
            is ApiResult.Success -> finishLogin(
                result.value,
                identifier,
                password,
                rememberCredentials
            )
        }
    }

    fun login(identifier: String, password: String, rememberCredentials: Boolean = true): ApiResult<ApiUser> {
        return when (val result = beginLogin(identifier, password, rememberCredentials)) {
            is ApiLoginResult.Success -> ApiResult.Success(result.user)
            is ApiLoginResult.TwoFactorRequired ->
                ApiResult.Failure("二段階認証コードの入力が必要です", 401)
            is ApiLoginResult.Failure -> ApiResult.Failure(result.message, result.status)
        }
    }

    private fun finishLogin(
        json: JSONObject,
        identifier: String,
        password: String,
        rememberCredentials: Boolean
    ): ApiLoginResult {
        return try {
            session.accessToken = json.optString("accessToken").takeIf { it.isNotBlank() }
            session.sessionId = json.optString("sessionId").takeIf { it.isNotBlank() }
            val loggedInUser = parseUser(json.getJSONObject("user"))
            if (rememberCredentials) {
                credentials.save(identifier, password)
                credentials.saveSessionIdForActive(session.sessionId)
                credentials.saveProfileForActive(
                    loggedInUser.displayName,
                    loggedInUser.username,
                    loggedInUser.avatarUrl
                )
            }
            ApiLoginResult.Success(loggedInUser)
        } catch (_: Exception) {
            ApiLoginResult.Failure("ログイン応答を読み取れませんでした")
        }
    }

    fun savedAccounts(): List<SavedCredentialAccount> = credentials.accounts()
    fun activeAccountIdentifier(): String? = credentials.load()?.first
    fun hasRecoverableSession(): Boolean =
        session.hasSession() || !credentials.sessionIdForActive().isNullOrBlank()

    fun maintainSavedSessions(): ApiResult<Unit> {
        val sessionIds = (credentials.savedSessionIds() + listOfNotNull(session.sessionId)).distinct()
        if (sessionIds.isEmpty()) return ApiResult.Success(Unit)
        val body = JSONObject()
            .put("deviceId", session.deviceId)
            .put("sessionIds", JSONArray(sessionIds))
            .put("clientType", "android")
            .put("deviceName", CLIENT_DEVICE_NAME)
        return mapObject(
            request(
                "auth/session-unread-snapshots",
                "POST",
                body.toString(),
                needsCsrf = true
            )
        ) { Unit }
    }

    fun switchAccount(identifier: String): ApiResult<ApiUser> {
        val saved = credentials.select(identifier) ?: return ApiResult.Failure("保存済みのログイン情報が見つかりません")
        val recordedSessionId = credentials.sessionIdForActive()
        if (recordedSessionId != null) {
            when (val resumed = switchToRecordedSession(recordedSessionId)) {
                is ApiResult.Success -> return resumed
                is ApiResult.Failure -> Unit
            }
        }
        // Existing session reuse failed. Only now create a new login session.
        session.clear()
        return login(saved.first, saved.second, rememberCredentials = true)
    }

    fun prepareForAdditionalAccount() {
        session.clear()
    }

    fun me(): ApiResult<ApiUser> {
        val result = mapObject(request("auth/me")) { root ->
            val source = root.optJSONObject("user") ?: root
            mergeLevelData(source, root)
            parseUser(source)
        }
        if (result is ApiResult.Success) credentials.saveProfileForActive(result.value.displayName, result.value.username, result.value.avatarUrl)
        return result
    }

    fun user(username: String): ApiResult<ApiUser> = mapObject(request("users/${encode(username)}")) { root ->
        val source = root.optJSONObject("user") ?: root
        mergeLevelData(source, root)
        if (!source.has("pinnedPost") && root.optJSONObject("pinnedPost") != null) source.put("pinnedPost", root.optJSONObject("pinnedPost"))
        if (!source.has("pinnedPosts") && root.optJSONArray("pinnedPosts") != null) source.put("pinnedPosts", root.optJSONArray("pinnedPosts"))
        listOf("canSendDirectMessage", "canDirectMessage", "canMessage", "directMessagesEnabled", "dmRequestPolicy").forEach { key ->
            if (!source.has(key) && root.has(key)) source.put(key, root.opt(key))
        }
        if (!source.has("showLikedPosts") && root.has("showLikedPosts")) source.put("showLikedPosts", root.optBoolean("showLikedPosts"))
        val relationship = root.optJSONObject("relationship")
        if (relationship != null) {
            source.put("isFollowing", relationship.optBoolean("isFollowing", relationship.optBoolean("is_following")))
            source.put("isFollowedBy", relationship.optBoolean("isFollowedBy", relationship.optBoolean("is_followed_by")))
            source.put("followRequestSent", relationship.optBoolean("hasPendingRequest", relationship.optBoolean("follow_request_sent")))
            source.put("isMuted", relationship.optBoolean("isMuted"))
            source.put("isBlocked", relationship.optBoolean("isBlocked", relationship.optBoolean("hasBlocked")))
            source.put("isBlockedBy", relationship.optBoolean("isBlockedBy"))
            source.put("isPostNotificationsEnabled", relationship.optBoolean("isPostNotificationsEnabled"))
        }
        if (!source.has("isFollowing")) source.put("isFollowing", root.optBoolean("isFollowing", root.optBoolean("is_following")))
        if (!source.has("isFollowedBy")) source.put("isFollowedBy", root.optBoolean("isFollowedBy", root.optBoolean("is_followed_by")))
        if (root.has("hasPendingRequest")) {
            source.put("followRequestSent", root.optBoolean("hasPendingRequest"))
        } else if (!source.has("followRequestSent")) {
            source.put(
                "followRequestSent",
                root.optBoolean(
                    "hasPendingRequest",
                    root.optBoolean("followRequestSent", root.optBoolean("follow_request_sent"))
                )
            )
        }
        if (!source.has("isMuted")) source.put("isMuted", root.optBoolean("isMuted"))
        if (!source.has("isBlocked")) source.put("isBlocked", root.optBoolean("isBlocked", root.optBoolean("hasBlocked")))
        if (!source.has("isBlockedBy")) source.put("isBlockedBy", root.optBoolean("isBlockedBy"))
        if (!source.has("isPostNotificationsEnabled")) {
            source.put("isPostNotificationsEnabled", root.optBoolean("isPostNotificationsEnabled"))
        }
        listOf(
            "profileMinimumAge",
            "profileMaximumAge",
            "hideProfileFromMinors",
            "profileUnavailableReason",
            "profileUnavailableDetails"
        ).forEach { key ->
            if (!source.has(key) && root.has(key)) source.put(key, root.opt(key))
        }
        parseUser(source)
    }

    fun follow(userId: Long, desired: Boolean): ApiResult<String> {
        val method = if (desired) "POST" else "DELETE"
        val body = if (desired) "{}" else null
        val primary = request("follow/$userId", method, body, needsCsrf = true)
        val result = if (primary is ApiResult.Failure && primary.status == 404) {
            request("users/$userId/follow", method, body, needsCsrf = true)
        } else primary
        return mapObject(result) { it.optString("message") }
    }

    fun mute(userId: Long, desired: Boolean): ApiResult<Unit> =
        mapObject(
            request(
                "follow/mute/$userId",
                if (desired) "POST" else "DELETE",
                if (desired) "{}" else null,
                needsCsrf = true
            )
        ) { Unit }

    fun block(userId: Long, desired: Boolean): ApiResult<Unit> =
        mapObject(
            request(
                "follow/block/$userId",
                if (desired) "POST" else "DELETE",
                if (desired) "{}" else null,
                needsCsrf = true
            )
        ) { Unit }

    fun postNotifications(userId: Long, desired: Boolean): ApiResult<Unit> =
        mapObject(
            request(
                "follow/$userId/post-notify",
                if (desired) "POST" else "DELETE",
                if (desired) "{}" else null,
                needsCsrf = true
            )
        ) { Unit }

    fun deletePost(postId: Long): ApiResult<Unit> =
        mapObject(request("posts/$postId", "DELETE", needsCsrf = true)) { Unit }

    fun updatePost(
        postId: Long,
        content: String,
        visibility: String,
        viewerCircleId: Long?,
        replyRestriction: String,
        replyCircleId: Long?,
        minimumAge: Int?,
        maximumAge: Int?,
        isAiGenerated: Boolean,
        isPromotional: Boolean,
        isR18: Boolean,
        expiresAt: String?
    ): ApiResult<ApiPost> {
        val body = JSONObject()
            .put("content", content)
            .put("visibility", visibility)
            .put("viewerCircleId", viewerCircleId ?: JSONObject.NULL)
            .put("replyRestriction", if (replyRestriction == "FOLLOWERS") "FOLLOWING" else replyRestriction)
            .put("replyCircleId", replyCircleId ?: JSONObject.NULL)
            .put("minimumAge", minimumAge ?: JSONObject.NULL)
            .put("maximumAge", maximumAge ?: JSONObject.NULL)
            .put("isAiGenerated", isAiGenerated)
            .put("isPromotional", isPromotional)
            .put("isR18", isR18)
            .put("hideFromMinors", isR18)
            .put("expiresAt", expiresAt ?: JSONObject.NULL)
        val updated = mapObject(
            request(
                "posts/$postId",
                "PUT",
                body.toString(),
                needsCsrf = true
            )
        ) { root ->
            parsePost(root.optJSONObject("post") ?: root)
        }
        return when (updated) {
            is ApiResult.Failure -> updated
            is ApiResult.Success -> when (val refreshed = post(postId)) {
                is ApiResult.Success -> refreshed
                is ApiResult.Failure -> updated
            }
        }
    }

    fun pinPost(postId: Long?): ApiResult<Unit> =
        mapObject(
            request(
                "users/profile/pinned-post",
                "PATCH",
                if (postId == null) "{}" else JSONObject().put("postId", postId).toString(),
                needsCsrf = true
            )
        ) { Unit }

    fun updatePostPin(postId: Long, pinned: Boolean): ApiResult<Unit> =
        mapObject(
            request(
                "users/profile/pinned-post",
                "PATCH",
                JSONObject().put("postId", postId).put("pinned", pinned).toString(),
                needsCsrf = true
            )
        ) { Unit }

    fun timeline(mode: String = "latest", ranking: String = "latest"): ApiResult<List<ApiPost>> =
        when (val result = timelinePage(mode, 1, ranking = ranking)) {
            is ApiResult.Success -> ApiResult.Success(result.value.posts)
            is ApiResult.Failure -> result
        }

    fun levelRanking(limit: Int = 100): ApiResult<List<ApiLevelRankingEntry>> =
        mapObject(request("https://api.karotter.com/api/users/level-ranking?limit=${limit.coerceIn(1, 100)}")) { root ->
            val users = root.optJSONArray("users") ?: JSONArray()
            buildList {
                for (index in 0 until users.length()) {
                    val item = users.optJSONObject(index) ?: continue
                    add(
                        ApiLevelRankingEntry(
                            rank = item.optInt("rank", index + 1),
                            user = parseUser(item),
                            experience = item.optInt("experience"),
                            experienceInLevel = item.optInt("experienceInLevel"),
                            experienceRequiredForNextLevel = item.optInt("experienceRequiredForNextLevel"),
                            experienceToNextLevel = item.optInt("experienceToNextLevel")
                        )
                    )
                }
            }
        }

    fun timelinePage(mode: String = "latest", page: Int = 1, limit: Int = 30, ranking: String = "latest"): ApiResult<ApiPostPage> =
        mapObject(request(when (mode) {
            "latest" -> "https://api.karotter.com/api/posts/recommended?mode=${encode(ranking)}&limit=${(page * limit).coerceAtMost(300)}"
            "trending" -> "https://api.karotter.com/api/search/discover/topics?sort=${encode(ranking)}&limit=${(page * limit).coerceAtMost(300)}"
            "following" -> if (ranking == "recommended") {
                "https://api.karotter.com/api/posts/timeline?limit=$limit&mode=ranked&page=$page"
            } else {
                "posts/timeline?page=$page&limit=$limit&mode=following"
            }
            else -> "posts/timeline?page=$page&limit=$limit&mode=${encode(mode)}&sort=${encode(ranking)}"
        })) { root ->
            val posts = parsePosts(root.optJSONArray("posts") ?: root.optJSONArray("results") ?: root.optJSONArray("topics"))
            val pagination = root.optJSONObject("pagination")
            val requested = (page * limit).coerceAtMost(300)
            val hasNext = if (mode == "latest" || mode == "trending") {
                posts.size >= requested && requested < 300
            } else when {
                pagination?.has("hasNext") == true -> pagination.optBoolean("hasNext")
                pagination?.has("pages") == true -> page < pagination.optInt("pages")
                pagination?.has("total") == true -> page * limit < pagination.optInt("total")
                else -> posts.size >= limit
            }
            ApiPostPage(posts, if (hasNext) page + 1 else null, posts.lastOrNull()?.id, hasNext)
        }

    fun stories(): ApiResult<List<ApiStory>> =
        mapObject(request("social/stories")) { root ->
        val array = root.optJSONArray("stories") ?: root.optJSONArray("items") ?: JSONArray()
        buildList {
            for (i in 0 until array.length()) {
                val item = array.optJSONObject(i) ?: continue
                add(parseStory(item))
            }
        }
    }

    fun createStory(
        media: ApiUploadMedia,
        caption: String,
        visibility: String = "PUBLIC",
        textOverlay: String? = null,
        textOverlayStyle: ApiStoryTextStyle? = null
    ): ApiResult<ApiStory> {
        val boundary = "KarohaStory${UUID.randomUUID()}"
        val output = ByteArrayOutputStream()
        fun write(value: String) = output.write(value.toByteArray(StandardCharsets.UTF_8))
        val fields = linkedMapOf(
            "caption" to caption,
            "visibility" to visibility,
            "isR18" to "false",
            "hideFromMinors" to "false"
        )
        textOverlay?.takeIf(String::isNotBlank)?.let { fields["textOverlay"] = it }
        textOverlayStyle?.let { style ->
            fields["textOverlayStyle"] = JSONObject()
                .put("x", style.x)
                .put("y", style.y)
                .put("size", style.size)
                .put("align", style.align)
                .put("color", style.color)
                .put("background", style.background)
                .toString()
        }
        fields.forEach { (name, value) ->
            write("--$boundary\r\nContent-Disposition: form-data; name=\"$name\"\r\n\r\n$value\r\n")
        }
        val safeName = media.fileName.replace("\"", "_").replace("\r", "_").replace("\n", "_")
        write("--$boundary\r\n")
        write("Content-Disposition: form-data; name=\"media\"; filename=\"$safeName\"\r\n")
        write("Content-Type: ${media.mimeType}\r\n\r\n")
        output.write(media.bytes)
        write("\r\n--$boundary--\r\n")
        return mapObject(
            requestBytes(
                "social/stories",
                "POST",
                output.toByteArray(),
                "multipart/form-data; boundary=$boundary",
                true
            )
        ) { root ->
            val item = root.optJSONObject("story") ?: root
            parseStory(item)
        }
    }

    private fun parseStory(item: JSONObject): ApiStory {
        val author = item.optJSONObject("author") ?: item.optJSONObject("user") ?: JSONObject()
        val rawStyle = item.optJSONObject("textOverlayStyle")
        val style = rawStyle?.let {
            val alignment = it.optString("align", "center").lowercase()
                .takeIf { value -> value in setOf("left", "center", "right") }
                ?: "center"
            val background = when (it.optString("background", "soft").lowercase()) {
                "none" -> "none"
                "solid", "light", "white" -> "solid"
                else -> "soft"
            }
            ApiStoryTextStyle(
                x = it.optDouble("x", 50.0).toFloat().coerceIn(10f, 90f),
                y = it.optDouble("y", 72.0).toFloat().coerceIn(14f, 86f),
                size = it.optInt("size", 28).coerceIn(18, 44),
                align = alignment,
                color = it.optString("color", "#ffffff"),
                background = background
            )
        }
        val counts = item.optJSONObject("_count")
        return ApiStory(
            id = item.optLong("id"),
            author = parseUser(author),
            mediaUrl = absolute(item.optString("mediaUrl")),
            caption = item.optString("caption").takeUnless { it == "null" }.orEmpty(),
            viewed = item.optBoolean("hasViewed"),
            liked = item.optBoolean("liked"),
            textOverlay = item.optString("textOverlay").takeUnless { it.isBlank() || it == "null" },
            textOverlayStyle = style,
            viewsCount = item.optInt("viewsCount", counts?.optInt("views") ?: 0),
            likesCount = item.optInt("likesCount", counts?.optInt("likes") ?: 0),
            commentsCount = item.optInt("commentsCount", counts?.optInt("comments") ?: 0)
        )
    }

    fun boards(): ApiResult<List<ApiBoard>> = mapObject(request("boards")) { root ->
        val array = root.optJSONArray("boards") ?: JSONArray()
        buildList {
            for (i in 0 until array.length()) {
                val b = array.optJSONObject(i) ?: continue
                add(parseBoard(b))
            }
        }
    }

    fun createBoard(title: String, slug: String, description: String, minimumAge: Int): ApiResult<ApiBoard> =
        mapObject(
            request(
                "boards",
                "POST",
                JSONObject()
                    .put("title", title)
                    .put("slug", slug)
                    .put("description", description)
                    .put("minimumAge", minimumAge)
                    .toString(),
                needsCsrf = true
            )
        ) { root ->
            val board = root.optJSONObject("board") ?: root
            parseBoard(board, slug, title, description)
        }

    fun deleteBoard(slug: String): ApiResult<Unit> {
        val body = JSONObject().put("slug", slug).toString()
        val primary = mapObject(request("boards/api", "DELETE", body, needsCsrf = true)) { Unit }
        return if (primary is ApiResult.Failure && primary.status in setOf(400, 404, 405)) {
            mapObject(request("boards/${encode(slug)}", "DELETE", needsCsrf = true)) { Unit }
        } else primary
    }

    fun news(): ApiResult<List<ApiArticle>> = mapObject(request("news?limit=20")) { root ->
        val array = root.optJSONArray("articles") ?: JSONArray()
        buildList {
            for (i in 0 until array.length()) {
                val a = array.optJSONObject(i) ?: continue
                add(ApiArticle(
                    a.optString("slug", a.optString("id")), a.optString("title", "無題の記事"),
                    a.optString("summary", a.optString("description")), a.optString("category", "general"),
                    a.optString("createdAt"), absolute(a.optString("imageUrl", a.optString("coverImageUrl", a.optString("coverImage")))), a.optString("content")
                ))
            }
        }
    }

    fun search(query: String): ApiResult<ApiSearchResult> = mapObject(request("search?q=${encode(query.trim().removePrefix("#"))}&limit=30")) { root ->
        val usersArray = root.optJSONArray("users") ?: JSONArray()
        val users = buildList { for (i in 0 until usersArray.length()) usersArray.optJSONObject(i)?.let { add(parseUser(it)) } }
        ApiSearchResult(users, parsePosts(root.optJSONArray("posts") ?: root.optJSONArray("results")))
    }

    fun searchPosts(
        query: String,
        page: Int,
        sort: String = "latest",
        limit: Int = 10,
        hasMedia: Boolean = false
    ): ApiResult<ApiPostPage> = mapObject(
        request(
            "https://api.karotter.com/api/search/posts?q=${encode(query.trim().removePrefix("#"))}" +
                "&sort=${when (sort) { "oldest" -> "oldest"; "popular" -> "topics"; else -> "latest" }}" +
                (if (hasMedia) "&hasMedia=true" else "") +
                "&limit=${(page * limit).coerceAtMost(300)}"
        )
    ) { root ->
        val posts = parsePosts(root.optJSONArray("posts") ?: root.optJSONArray("results"))
        val requested = (page * limit).coerceAtMost(300)
        val hasNext = posts.size >= requested && requested < 300
        ApiPostPage(posts, if (hasNext) page + 1 else null, null, hasNext)
    }

    fun searchUsers(query: String, page: Int, limit: Int = 15): ApiResult<ApiUserPage> = mapObject(
        request("search?q=${encode(query.trim().removePrefix("#"))}&page=$page&limit=$limit")
    ) { root ->
        val array = root.optJSONArray("users") ?: JSONArray()
        val users = buildList { for (i in 0 until array.length()) array.optJSONObject(i)?.let { add(parseUser(it)) } }
        val pagination = root.optJSONObject("pagination")
        val hasNext = when {
            pagination?.has("hasNext") == true -> pagination.optBoolean("hasNext")
            pagination?.has("pages") == true -> page < pagination.optInt("pages")
            pagination?.has("total") == true -> page * limit < pagination.optInt("total")
            else -> users.size >= limit
        }
        ApiUserPage(users, if (hasNext) page + 1 else null, hasNext)
    }

    fun trendingTopics(): ApiResult<List<ApiTrend>> = mapObject(request("search/trending/topics?limit=8")) { root ->
        val array = root.optJSONArray("trends") ?: JSONArray()
        buildList { for (i in 0 until array.length()) array.optJSONObject(i)?.let { add(ApiTrend(it.optString("label", it.optString("token")), it.optInt("postCount"))) } }
    }

    fun circles(): ApiResult<List<ApiCircle>> = mapObject(request("social/circles")) { root ->
        val array = root.optJSONArray("circles") ?: root.optJSONArray("items") ?: JSONArray()
        buildList {
            for (index in 0 until array.length()) {
                val item = array.optJSONObject(index) ?: continue
                add(
                    ApiCircle(
                        id = item.optLong("id"),
                        name = item.optString("name", item.optString("title", "サークル")),
                        memberCount = item.optInt("memberCount", item.optInt("membersCount"))
                    )
                )
            }
        }
    }

    fun communities(): ApiResult<ApiCommunityGroups> = mapObject(request("communities")) { root ->
        ApiCommunityGroups(
            joined = parseCommunities(root.optJSONArray("joined"), forceMember = true),
            owned = parseCommunities(root.optJSONArray("owned"), forceMember = true),
            recommended = parseCommunities(root.optJSONArray("recommended"))
        )
    }

    fun createCommunity(name: String, description: String): ApiResult<ApiCommunity> {
        val boundary = "KarohaCommunity${UUID.randomUUID()}"
        val output = ByteArrayOutputStream()
        fun write(value: String) = output.write(value.toByteArray(StandardCharsets.UTF_8))
        linkedMapOf(
            "name" to name,
            "description" to description,
            "joinType" to "OPEN"
        ).forEach { (field, value) ->
            write("--$boundary\r\nContent-Disposition: form-data; name=\"$field\"\r\n\r\n$value\r\n")
        }
        write("--$boundary--\r\n")
        return mapObject(
            requestBytes(
                "communities",
                "POST",
                output.toByteArray(),
                "multipart/form-data; boundary=$boundary",
                true
            )
        ) { root ->
            parseCommunity(root.optJSONObject("community") ?: root)
        }
    }

    fun community(id: Long): ApiResult<ApiCommunity> = mapObject(request("communities/$id")) { root ->
        parseCommunity(root.optJSONObject("community") ?: root)
    }

    fun communityPosts(id: Long, page: Int = 1, limit: Int = 20, tab: String = "latest"): ApiResult<ApiPostPage> =
        mapObject(request("communities/$id/posts?page=$page&limit=$limit&tab=${encode(tab)}")) { root ->
            val posts = parsePosts(root.optJSONArray("posts"))
            val pagination = root.optJSONObject("pagination")
            val hasNext = pagination?.optBoolean("hasNext", posts.size >= limit) ?: (posts.size >= limit)
            ApiPostPage(posts, if (hasNext) page + 1 else null, posts.lastOrNull()?.id, hasNext)
        }

    fun communityMembers(id: Long, page: Int = 1, limit: Int = 20): ApiResult<ApiCommunityMemberPage> =
        mapObject(request("communities/$id/members?page=$page&limit=$limit")) { root ->
            val array = root.optJSONArray("members") ?: JSONArray()
            val members = buildList {
                for (index in 0 until array.length()) {
                    val item = array.optJSONObject(index) ?: continue
                    val user = item.optJSONObject("user") ?: continue
                    add(
                        ApiCommunityMember(
                            id = item.optLong("id"),
                            role = item.optString("role", "MEMBER"),
                            joinedAt = item.optString("joinedAt"),
                            user = parseUser(user)
                        )
                    )
                }
            }
            val pagination = root.optJSONObject("pagination")
            val hasNext = pagination?.optBoolean("hasNext", members.size >= limit) ?: (members.size >= limit)
            ApiCommunityMemberPage(members, if (hasNext) page + 1 else null, hasNext)
        }

    fun homeCommunityTimelines(): ApiResult<List<ApiCommunity>> =
        mapObject(request("communities/home-timelines")) { root ->
            val timelines = root.optJSONArray("timelines") ?: JSONArray()
            buildList {
                for (index in 0 until timelines.length()) {
                    val item = timelines.optJSONObject(index) ?: continue
                    val community = item.optJSONObject("community") ?: continue
                    add(parseCommunity(community).copy(isMember = true))
                }
            }.distinctBy(ApiCommunity::id)
        }

    fun addCommunityHomeTimeline(id: Long): ApiResult<Unit> =
        mapObject(request("communities/$id/home-timeline", "POST", "{}", needsCsrf = true)) { Unit }

    fun removeCommunityHomeTimeline(id: Long): ApiResult<Unit> =
        mapObject(request("communities/$id/home-timeline", "DELETE", needsCsrf = true)) { Unit }

    fun communityMembership(id: Long, join: Boolean): ApiResult<Unit> =
        mapObject(
            request(
                "communities/$id/${if (join) "join" else "leave"}",
                "POST",
                "{}",
                needsCsrf = true
            )
        ) { Unit }

    private fun parseCommunities(array: JSONArray?, forceMember: Boolean = false): List<ApiCommunity> = buildList {
        val source = array ?: JSONArray()
        for (index in 0 until source.length()) {
            source.optJSONObject(index)?.let { item ->
                val community = parseCommunity(item)
                add(if (forceMember) community.copy(isMember = true) else community)
            }
        }
    }

    private fun parseCommunity(json: JSONObject): ApiCommunity {
        val permissions = json.optJSONObject("permissions")
        val rules = buildList {
            val array = json.optJSONArray("rules") ?: JSONArray()
            for (index in 0 until array.length()) {
                val value = when (val item = array.opt(index)) {
                    is JSONObject -> item.optString("text", item.optString("content"))
                    else -> item?.toString().orEmpty()
                }
                value.takeIf { it.isNotBlank() && !it.equals("null", true) }?.let(::add)
            }
        }
        return ApiCommunity(
            id = json.optLong("id"),
            name = json.optString("name", "コミュニティ"),
            description = json.optString("description").takeUnless { it.equals("null", true) }.orEmpty(),
            headerImageUrl = absolute(json.optString("headerImage", json.optString("headerImageUrl"))),
            ownerId = json.optLong("ownerId").takeIf { it > 0L },
            joinType = json.optString("joinType", "OPEN"),
            memberCount = json.optInt("memberCount", json.optInt("membersCount")),
            isMember = json.optBoolean("isMember") || json.optJSONObject("membership") != null,
            isInvited = json.optBoolean("isInvited"),
            canPost = permissions?.optBoolean("canPost") ?: json.optBoolean("canPost"),
            rules = rules
        )
    }

    fun post(id: Long): ApiResult<ApiPost> = mapObject(request("posts/$id")) { root -> parsePost(root.optJSONObject("post") ?: root) }

    fun proPreferences(): ApiResult<ApiProPreferences> =
        mapObject(request("subscriptions/me")) { root ->
            val summary = root.optJSONObject("summary") ?: JSONObject()
            val entitlements = root.optJSONObject("entitlements") ?: JSONObject()
            ApiProPreferences(
                plan = summary.optString("plan", entitlements.optString("plan", "FREE")),
                status = summary.optString("status", "INACTIVE"),
                canCustomizeProfile = entitlements.optBoolean("canCustomizeProfile"),
                canCustomizeCards = entitlements.optBoolean("canCustomizeCards"),
                pinnedPostLimit = entitlements.optInt(
                    "pinnedPostLimit",
                    when (summary.optString("plan", entitlements.optString("plan", "FREE")).uppercase()) {
                        "PRO" -> 5
                        "PLUS" -> 3
                        else -> 1
                    }
                ).coerceAtLeast(1),
                premiumBadgeColor = summary.optString("premiumBadgeColor", "ORANGE"),
                profileAccentColor = summary.optString("profileAccentColor")
                    .takeIf { it.isNotBlank() && !it.equals("null", true) },
                cardAccentColor = summary.optString("cardAccentColor")
                    .takeIf { it.isNotBlank() && !it.equals("null", true) }
            )
        }

    fun updateProPreferences(
        premiumBadgeColor: String,
        profileAccentColor: String?,
        cardAccentColor: String?
    ): ApiResult<Unit> {
        val body = JSONObject()
            .put("premiumBadgeColor", premiumBadgeColor)
            .put("profileAccentColor", profileAccentColor ?: JSONObject.NULL)
            .put("cardAccentColor", cardAccentColor ?: JSONObject.NULL)
            .toString()
        return mapObject(
            request("subscriptions/preferences", "PATCH", body, needsCsrf = true)
        ) { Unit }
    }

    fun replies(id: Long): ApiResult<List<ApiPost>> = mapObject(request("posts/$id/replies?limit=100")) { root ->
        parsePosts(root.optJSONArray("replies") ?: root.optJSONArray("posts"))
    }

    fun postLikes(id: Long, page: Int = 1, limit: Int = 30): ApiResult<ApiUserPage> =
        engagementUsers("posts/$id/likes?page=$page&limit=$limit", page, limit, false)

    fun postRekarotUsers(id: Long, page: Int = 1, limit: Int = 30): ApiResult<ApiUserPage> =
        engagementUsers("posts/$id/rekarots?page=$page&limit=$limit", page, limit, false)

    fun postQuoteUsers(id: Long, page: Int = 1, limit: Int = 30): ApiResult<ApiUserPage> =
        engagementUsers("posts/$id/quotes?page=$page&limit=$limit", page, limit, true)

    fun postQuotes(id: Long, page: Int = 1, limit: Int = 20): ApiResult<ApiPostPage> =
        mapObject(request("posts/$id/quotes?page=$page&limit=$limit")) { root ->
            val posts = parsePosts(root.optJSONArray("quotes") ?: root.optJSONArray("posts"))
            val pagination = root.optJSONObject("pagination")
            val totalPages = pagination?.optInt("pages", pagination.optInt("totalPages", 0)) ?: 0
            val hasNext = when {
                pagination?.has("hasNext") == true -> pagination.optBoolean("hasNext")
                totalPages > 0 -> page < totalPages
                else -> posts.size >= limit
            }
            ApiPostPage(posts, if (hasNext) page + 1 else null, posts.lastOrNull()?.id, hasNext)
        }

    fun postReactionUsers(id: Long, emoji: String, page: Int = 1, limit: Int = 30): ApiResult<ApiUserPage> =
        engagementUsers(
            "posts/$id/react/${encode(emoji)}/users?page=$page&limit=$limit",
            page,
            limit,
            false
        )

    private fun engagementUsers(path: String, page: Int, limit: Int, postsAsUsers: Boolean): ApiResult<ApiUserPage> =
        mapObject(request(path)) { root ->
            val users = if (postsAsUsers) {
                parsePosts(root.optJSONArray("quotes") ?: root.optJSONArray("posts"))
                    .map(ApiPost::author)
                    .distinctBy(ApiUser::id)
            } else {
                val array = root.optJSONArray("users")
                    ?: root.optJSONArray("likes")
                    ?: root.optJSONArray("rekarots")
                    ?: JSONArray()
                buildList {
                    for (index in 0 until array.length()) {
                        val item = array.optJSONObject(index) ?: continue
                        val user = item.optJSONObject("user")
                            ?: item.optJSONObject("actor")
                            ?: item
                        add(parseUser(user))
                    }
                }.distinctBy(ApiUser::id)
            }
            val pagination = root.optJSONObject("pagination")
            val totalPages = pagination?.optInt("pages", pagination.optInt("totalPages", 0)) ?: 0
            val hasNext = when {
                pagination?.has("hasNext") == true -> pagination.optBoolean("hasNext")
                totalPages > 0 -> page < totalPages
                else -> users.size >= limit
            }
            ApiUserPage(users, if (hasNext) page + 1 else null, hasNext)
        }

    fun followRequests(page: Int = 1, limit: Int = 30): ApiResult<ApiFollowRequestPage> {
        val primary = request("follow/requests/pending?page=$page&limit=$limit")
        val response = if (primary is ApiResult.Failure) {
            request("follow-requests?page=$page&limit=$limit")
        } else primary
        return mapObject(response) { root ->
            val array = root.optJSONArray("requests")
                ?: root.optJSONArray("followRequests")
                ?: root.optJSONArray("users")
                ?: JSONArray()
            val requests = buildList {
                for (index in 0 until array.length()) {
                    val item = array.optJSONObject(index) ?: continue
                    val userJson = item.optJSONObject("requester")
                        ?: item.optJSONObject("fromUser")
                        ?: item.optJSONObject("user")
                        ?: item
                    val user = parseUser(userJson)
                    val requestId = item.optLong("requestId", item.optLong("id", user.id))
                    add(ApiFollowRequest(requestId, user, item.optString("createdAt")))
                }
            }.distinctBy(ApiFollowRequest::id)
            val pagination = root.optJSONObject("pagination")
            val totalPages = pagination?.optInt("pages", pagination.optInt("totalPages", 0)) ?: 0
            val hasNext = when {
                pagination?.has("hasNext") == true -> pagination.optBoolean("hasNext")
                totalPages > 0 -> page < totalPages
                else -> requests.size >= limit
            }
            ApiFollowRequestPage(requests, if (hasNext) page + 1 else null, hasNext)
        }
    }

    fun respondFollowRequest(requestId: Long, accept: Boolean): ApiResult<Unit> {
        val action = if (accept) "accept" else "reject"
        val primary = request("follow/requests/$requestId/$action", "POST", "{}", needsCsrf = true)
        val response = if (primary is ApiResult.Failure) {
            request("follow-requests/$requestId/$action", "POST", "{}", needsCsrf = true)
        } else primary
        return when (response) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Failure -> response
        }
    }

    fun userPosts(userId: Long, page: Int = 1, cursor: Long? = null, kind: String = "posts", limit: Int = 15): ApiResult<ApiPostPage> {
        val pageResult = mapObject(
            if (kind == "bookmarks") {
            val primary = request("posts/me/bookmarks?page=$page&limit=$limit")
            if (primary is ApiResult.Failure) request("bookmarks?page=$page&limit=$limit") else primary
            } else if (kind == "replies") {
                request("https://api.karotter.com/api/users/$userId/replies?page=$page&limit=$limit")
            } else request("users/$userId/${kind.takeIf { it in setOf("posts", "media", "likes") } ?: "posts"}?page=$page&limit=$limit${cursor?.let { "&cursor=$it" }.orEmpty()}")
        ) { root ->
            val posts = if (root.optJSONArray("posts") != null || root.optJSONArray("replies") != null) {
                parsePosts(root.optJSONArray("posts") ?: root.optJSONArray("replies"))
            } else {
                val bookmarks = root.optJSONArray("bookmarks") ?: JSONArray()
                buildList { for (i in 0 until bookmarks.length()) bookmarks.optJSONObject(i)?.let { item -> runCatching { parsePost(item.optJSONObject("post") ?: item) }.getOrNull()?.let(::add) } }
            }
            val pagination = root.optJSONObject("pagination")
            val hasNext = pagination?.optBoolean("hasNext", posts.size >= limit) ?: (posts.size >= limit)
            ApiPostPage(posts, if (hasNext) page + 1 else null, posts.lastOrNull()?.id, hasNext)
        }
        return pageResult
    }

    fun scheduledPosts(): ApiResult<List<ApiPost>> =
        mapObject(request("posts/scheduled/me")) { root ->
            parsePosts(root.optJSONArray("scheduledPosts") ?: root.optJSONArray("posts"))
        }

    fun deleteScheduledPost(postId: Long): ApiResult<Unit> =
        when (val result = request("posts/scheduled/$postId", "DELETE", needsCsrf = true)) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Failure -> result
        }

    fun sendQuestion(username: String, content: String): ApiResult<Unit> {
        val body = JSONObject().put("content", content.trim()).toString()
        return when (
            val result = request(
                "social/questions/${encode(username)}",
                "POST",
                body,
                needsCsrf = true
            )
        ) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Failure -> result
        }
    }

    fun questionsInbox(): ApiResult<List<ApiQuestion>> =
        mapObject(request("social/questions/inbox")) { root ->
            val array = root.optJSONArray("questions") ?: JSONArray()
            buildList {
                for (index in 0 until array.length()) {
                    val item = array.optJSONObject(index) ?: continue
                    add(
                        ApiQuestion(
                            id = item.optLong("id"),
                            content = item.optString("content"),
                            createdAt = item.optString("createdAt"),
                            sender = item.optJSONObject("sender")?.let { sender ->
                                runCatching { parseUser(sender) }.getOrNull()
                            },
                            answeredPost = item.optJSONObject("answeredPost")?.let { post ->
                                runCatching { parsePost(post) }.getOrNull()
                            }
                        )
                    )
                }
            }
        }

    fun userConnections(userId: Long, kind: String, cursor: String? = null, limit: Int = 30): ApiResult<ApiUserConnectionPage> {
        val connectionKind = when (kind) {
            "following" -> "following"
            "mutual" -> "mutual-followers"
            else -> "followers"
        }
        val cursorQuery = cursor?.takeIf(String::isNotBlank)?.let { "&cursor=${encode(it)}" }.orEmpty()
        return mapObject(request("users/$userId/$connectionKind?limit=$limit$cursorQuery")) { root ->
            val array = root.optJSONArray("users")
                ?: root.optJSONArray(connectionKind)
                ?: JSONArray()
            val users = buildList {
                for (index in 0 until array.length()) {
                    val item = array.optJSONObject(index) ?: continue
                    add(parseUser(item.optJSONObject("user") ?: item))
                }
            }
            val pagination = root.optJSONObject("pagination")
            val nextCursor = pagination?.opt("nextCursor")?.takeUnless { it == JSONObject.NULL }?.toString()?.takeIf(String::isNotBlank)
            val hasNext = when {
                pagination?.has("hasNext") == true -> pagination.optBoolean("hasNext")
                nextCursor != null -> true
                else -> users.size >= limit
            }
            ApiUserConnectionPage(users, nextCursor, hasNext)
        }
    }

    fun dmGroups(): ApiResult<List<ApiDmGroup>> = mapObject(request("${DM_API_BASE}groups")) { root ->
        val array = root.optJSONArray("groups") ?: JSONArray()
        buildList {
            for (i in 0 until array.length()) {
                val group = array.optJSONObject(i) ?: continue
                add(parseDmGroup(group))
            }
        }
    }

    fun startDm(targetUserId: Long): ApiResult<ApiDmGroup> =
        mapObject(
            request(
                "${DM_API_BASE}start",
                "POST",
                JSONObject().put("targetUserId", targetUserId).toString(),
                needsCsrf = true
            )
        ) { root -> parseDmGroup(root.optJSONObject("group") ?: root.optJSONObject("dmGroup") ?: root) }

    fun createDmGroup(name: String, userIds: List<Long>): ApiResult<ApiDmGroup> {
        val members = userIds.distinct().filter { it > 0L }
        if (members.size < 2) return ApiResult.Failure("グループには2人以上のメンバーを選択してください")
        val requestedName = name.trim()
        val payload = JSONObject()
            .put("userIds", JSONArray(members))
            .put("name", JSONObject.NULL)
            .put("isGroup", true)
        val createResult = request(
            "${DM_API_BASE}groups",
            "POST",
            payload.toString(),
            needsCsrf = true
        )
        return when (
            val result = createResult
        ) {
            is ApiResult.Failure -> result
            is ApiResult.Success -> {
                val root = result.value
                val source = root.optJSONObject("group")
                    ?: root.optJSONObject("dmGroup")
                    ?: root.optJSONObject("data")?.optJSONObject("group")
                    ?: root.optJSONObject("data")
                    ?: root
                if (source.optLong("id") <= 0L) {
                    val returnedId = sequenceOf(
                        root.optLong("groupId"),
                        root.optJSONObject("data")?.optLong("groupId") ?: 0L
                    ).firstOrNull { it > 0L }
                    returnedId?.let { source.put("id", it) }
                }
                val created = parseDmGroup(source).copy(name = requestedName)
                if (created.id > 0L) {
                    ApiResult.Success(created)
                } else {
                    // Some deployments only return a success message. Resolve the
                    // newly-created group from the canonical group list.
                    when (val listed = dmGroups()) {
                        is ApiResult.Failure -> ApiResult.Failure("グループは作成されましたが、作成結果を取得できませんでした")
                        is ApiResult.Success -> listed.value
                            .filter { group -> members.all { id -> group.members.any { it.id == id } } }
                            .maxByOrNull { it.id }
                            ?.copy(name = requestedName)
                            ?.let { ApiResult.Success(it) }
                            ?: ApiResult.Failure("作成したグループを一覧から確認できませんでした")
                    }
                }
            }
        }
    }

    fun acceptDmRequest(groupId: Long): ApiResult<Unit> =
        mapObject(request("${DM_API_BASE}groups/$groupId/request/accept", "POST", "{}", needsCsrf = true)) { Unit }

    fun rejectDmRequest(groupId: Long): ApiResult<Unit> =
        mapObject(request("${DM_API_BASE}groups/$groupId/request/reject", "POST", "{}", needsCsrf = true)) { Unit }

    fun clearDmHistory(groupId: Long): ApiResult<Unit> =
        mapObject(request("${DM_API_BASE}groups/$groupId/clear", "POST", "{}", needsCsrf = true)) { Unit }

    fun leaveDmGroup(groupId: Long): ApiResult<Unit> =
        mapObject(request("${DM_API_BASE}groups/$groupId/leave", "POST", "{}", needsCsrf = true)) { Unit }

    fun dmMessages(groupId: Long, page: Int = 1): ApiResult<List<ApiDmMessage>> = mapObject(request("${DM_API_BASE}groups/$groupId/messages?page=$page&limit=50")) { root ->
        val array = root.optJSONArray("messages") ?: JSONArray()
        buildList { for (i in 0 until array.length()) array.optJSONObject(i)?.let { message -> add(parseDmMessage(message)) } }
    }

    fun sendDm(groupId: Long, content: String, attachments: List<ApiUploadMedia> = emptyList()): ApiResult<ApiDmMessage> {
        val boundary = "KarotterDm${UUID.randomUUID()}"
        val fields = linkedMapOf(
            "content" to content,
            "attachmentAlts" to JSONArray(attachments.map { "" }).toString(),
            "attachmentSpoilerFlags" to JSONArray(attachments.map { false }).toString(),
            "attachmentR18Flags" to JSONArray(attachments.map { false }).toString()
        )
        val output = ByteArrayOutputStream()
        fun write(value: String) = output.write(value.toByteArray(StandardCharsets.UTF_8))
        fields.forEach { (name, value) ->
            write("--$boundary\r\nContent-Disposition: form-data; name=\"$name\"\r\n\r\n$value\r\n")
        }
        attachments.forEach { item ->
            val safeName = item.fileName.replace("\"", "_").replace("\r", "_").replace("\n", "_")
            write("--$boundary\r\n")
            write("Content-Disposition: form-data; name=\"attachments\"; filename=\"$safeName\"\r\n")
            write("Content-Type: ${item.mimeType}\r\n\r\n")
            output.write(item.bytes)
            write("\r\n")
        }
        write("--$boundary--\r\n")
        val bytes = output.toByteArray()
        return mapObject(requestBytes("${DM_API_BASE}groups/$groupId/messages", "POST", bytes, "multipart/form-data; boundary=$boundary", true)) { root ->
            parseDmMessage(root.optJSONObject("message") ?: root)
        }
    }

    fun markDmRead(groupId: Long): ApiResult<Unit> = mapObject(request("${DM_API_BASE}groups/$groupId/read", "POST", "{}", needsCsrf = true)) { Unit }

    fun notifications(limit: Int = 30): ApiResult<List<ApiNotification>> = mapObject(request("notifications?limit=${limit.coerceIn(1, 1000)}")) { root ->
        parseNotifications(root)
    }

    fun notificationPage(page: Int, limit: Int = 30, types: String? = null): ApiResult<List<ApiNotification>> = mapObject(
        request(
            buildString {
                append("notifications?page=$page&limit=${limit.coerceIn(1, 100)}")
                types?.takeIf { it.isNotBlank() }?.let { append("&types=${encode(it)}") }
            }
        )
    ) { root ->
        parseNotifications(root)
    }

    private fun parseNotifications(root: JSONObject): List<ApiNotification> {
        val array = root.optJSONArray("notifications") ?: JSONArray()
        return buildList {
            for (i in 0 until array.length()) {
                val n = array.optJSONObject(i) ?: continue
                val actor = n.optJSONObject("actor") ?: n.optJSONArray("actors")?.optJSONObject(0)
                val postJson = n.optJSONObject("post") ?: n.optJSONArray("posts")?.optJSONObject(0)
                val actorName = actor?.optString("displayName", actor.optString("username"))?.takeIf { it.isNotBlank() } ?: "Karotter"
                val type = n.optString("type", "SYSTEM")
                add(ApiNotification(
                    n.optString("id", n.optJSONArray("notificationIds")?.optString(0).orEmpty()), type, actorName,
                    absolute(actor?.optString("avatarUrl")),
                    n.optString("message").takeIf { it.isNotBlank() && it != "null" } ?: notificationMessage(type, actorName, n.optInt("actorCount", 1)),
                    n.optString("createdAt"),
                    postJson?.let { runCatching { parsePost(it) }.getOrNull() },
                    actor?.optString("username")?.takeIf { it.isNotBlank() && it != "null" }
                ))
            }
        }
    }

    fun unreadCount(): ApiResult<Int> = mapObject(request("notifications/unread/count")) { it.optInt("count") }

    fun markNotificationsRead(): ApiResult<Unit> = mapObject(request("notifications/read-all", "PATCH", "{}", needsCsrf = true)) { Unit }

    fun board(slug: String): ApiResult<Pair<ApiBoard, List<ApiThread>>> = mapObject(request("boards/${encode(slug)}")) { root ->
        val b = root.optJSONObject("board") ?: JSONObject().put("slug", slug)
        val board = parseBoard(b, slug)
        val array = root.optJSONArray("threads") ?: JSONArray()
        val threads = buildList {
            for (i in 0 until array.length()) {
                val t = array.optJSONObject(i) ?: continue
                val author = t.optJSONObject("author")
                add(ApiThread(t.optLong("id"), t.optString("title", t.optString("subject", "スレッド")), t.optString("content"), t.optInt("repliesCount", t.optInt("replyCount")), author?.optString("displayName", author.optString("username")) ?: t.optString("authorName", "Karotterユーザー")))
            }
        }
        board to threads
    }

    private fun parseBoard(
        json: JSONObject,
        fallbackSlug: String = "",
        fallbackName: String = "掲示板",
        fallbackDescription: String = ""
    ): ApiBoard {
        val owner = json.optJSONObject("owner")
            ?: json.optJSONObject("creator")
            ?: json.optJSONObject("author")
        val ownerId = sequenceOf("ownerId", "creatorId", "authorId", "userId")
            .map { json.optLong(it) }
            .firstOrNull { it > 0L }
            ?: owner?.optLong("id")?.takeIf { it > 0L }
        return ApiBoard(
            slug = json.optString("slug", fallbackSlug.ifBlank { json.optString("id") }),
            name = json.optString("name", json.optString("title", fallbackName)),
            description = json.optString("description", fallbackDescription),
            threadCount = json.optInt("threadsCount", json.optInt("threadCount")),
            ownerId = ownerId
        )
    }

    fun boardThread(slug: String, threadId: Long): ApiResult<ApiThreadDetail> = mapObject(request("boards/${encode(slug)}/threads/$threadId")) { root ->
        val t = root.optJSONObject("thread") ?: JSONObject()
        val author = t.optJSONObject("author")
        val thread = ApiThread(t.optLong("id"), t.optString("title"), t.optString("content"), t.optInt("replyCount"), author?.optString("displayName", author.optString("username")) ?: "Karotterユーザー")
        val repliesArray = root.optJSONArray("replies") ?: JSONArray()
        val replies = buildList {
            for (i in 0 until repliesArray.length()) {
                val r = repliesArray.optJSONObject(i) ?: continue
                val replyAuthor = r.optJSONObject("author")
                add(ApiBoardReply(r.optLong("id"), r.optInt("replyNumber", i + 1), r.optString("content"), replyAuthor?.optString("displayName", replyAuthor.optString("username")) ?: "Karotterユーザー", r.optString("createdAt"), parseGenericMedia(r, "imageUrls", "imageTypes")))
            }
        }
        ApiThreadDetail(thread, parseGenericMedia(t, "imageUrls", "imageTypes"), replies)
    }

    fun createBoardThread(slug: String, title: String, content: String): ApiResult<Unit> =
        mapObject(
            request(
                "boards/${encode(slug)}/threads",
                "POST",
                JSONObject().put("title", title).put("content", content).toString(),
                needsCsrf = true
            )
        ) { Unit }

    fun createBoardReply(boardSlug: String, threadId: Long, content: String, images: List<ApiUploadMedia> = emptyList()): ApiResult<Unit> {
        if (images.isEmpty()) {
            val currentApi = request(
                "boards/${encode(boardSlug)}/threads/$threadId/replies",
                "POST",
                JSONObject().put("content", content).toString(),
                needsCsrf = true
            )
            if (currentApi !is ApiResult.Failure || currentApi.status !in setOf(400, 404, 405, 415)) {
                return mapObject(currentApi) { Unit }
            }
        }
        fun multipart(path: String, fileField: String): ApiResult<JSONObject> {
            val boundary = "KarotterBoard${UUID.randomUUID()}"
            val output = ByteArrayOutputStream()
            fun write(value: String) = output.write(value.toByteArray(StandardCharsets.UTF_8))
            write("--$boundary\r\nContent-Disposition: form-data; name=\"content\"\r\n\r\n$content\r\n")
            images.take(4).forEach { item ->
                val safeName = item.fileName.replace("\"", "_").replace("\r", "_").replace("\n", "_")
                write("--$boundary\r\n")
                write("Content-Disposition: form-data; name=\"$fileField\"; filename=\"$safeName\"\r\n")
                write("Content-Type: ${item.mimeType}\r\n\r\n")
                output.write(item.bytes)
                write("\r\n")
            }
            write("--$boundary--\r\n")
            return requestBytes(
                path,
                "POST",
                output.toByteArray(),
                "multipart/form-data; boundary=$boundary",
                true
            )
        }
        if (images.isNotEmpty()) {
            val attempts = listOf(
                "boards/api/threads/$threadId/replies" to "images",
                "boards/${encode(boardSlug)}/threads/$threadId/replies" to "images",
                "boards/api/threads/$threadId/replies" to "media",
                "boards/${encode(boardSlug)}/threads/$threadId/replies" to "media"
            )
            var lastFailure: ApiResult.Failure? = null
            attempts.forEach { (path, field) ->
                when (val result = multipart(path, field)) {
                    is ApiResult.Success -> return ApiResult.Success(Unit)
                    is ApiResult.Failure -> {
                        lastFailure = result
                        val compatibleFailure =
                            result.status == null ||
                                result.status in setOf(400, 404, 405, 415, 422) ||
                                (result.status ?: 0) >= 500
                        if (!compatibleFailure) return result
                    }
                }
            }
            return lastFailure ?: ApiResult.Failure("画像付き返信を送信できませんでした")
        }
        val legacyResult = multipart("boards/api/threads/$threadId/replies", "images")
        val finalResult = if (legacyResult is ApiResult.Failure && legacyResult.status in setOf(400, 404, 405, 415)) {
            request(
                "boards/threads/$threadId/replies",
                "POST",
                JSONObject().put("content", content).toString(),
                needsCsrf = true
            )
        } else legacyResult
        return mapObject(finalResult) { Unit }
    }

    fun updatePrivateAccount(isPrivate: Boolean): ApiResult<Unit> =
        mapObject(
            request(
                "users/settings",
                "PATCH",
                JSONObject().put("isPrivate", isPrivate).toString(),
                needsCsrf = true
            )
        ) { Unit }

    fun updateProfile(displayName: String, bio: String, websiteUrl: String, location: String): ApiResult<Unit> =
        mapObject(
            request(
                "users/profile",
                "PATCH",
                JSONObject()
                    .put("displayName", displayName)
                    .put("bio", bio)
                    .put("websiteUrl", websiteUrl)
                    .put("location", location)
                    .toString(),
                needsCsrf = true
            )
        ) { Unit }

    fun updateUsername(username: String): ApiResult<Unit> =
        mapObject(
            request(
                "users/username",
                "PATCH",
                JSONObject().put("username", username).toString(),
                needsCsrf = true
            )
        ) { Unit }

    fun updateStatus(status: String, statusMessage: String): ApiResult<Unit> =
        mapObject(
            request(
                "users/status",
                "PATCH",
                JSONObject().put("status", status).put("statusMessage", statusMessage).toString(),
                needsCsrf = true
            )
        ) { Unit }

    fun uploadProfileImage(kind: String, image: ApiUploadMedia): ApiResult<Unit> {
        require(kind == "avatar" || kind == "header")
        val boundary = "KarotterProfile${UUID.randomUUID()}"
        val output = ByteArrayOutputStream()
        fun write(value: String) = output.write(value.toByteArray(StandardCharsets.UTF_8))
        val safeName = image.fileName.replace("\"", "_").replace("\r", "_").replace("\n", "_")
        write("--$boundary\r\n")
        write("Content-Disposition: form-data; name=\"$kind\"; filename=\"$safeName\"\r\n")
        write("Content-Type: ${image.mimeType}\r\n\r\n")
        output.write(image.bytes)
        write("\r\n--$boundary--\r\n")
        return mapObject(
            requestBytes(
                "profile/$kind",
                "POST",
                output.toByteArray(),
                "multipart/form-data; boundary=$boundary",
                true
            )
        ) { Unit }
    }

    fun article(slug: String): ApiResult<ApiArticle> = mapObject(request("news/${encode(slug)}")) { root ->
        parseArticle(root.optJSONObject("article") ?: root)
    }

    fun viewStory(id: Long): ApiResult<Unit> = mapObject(request("social/stories/$id/views", "POST", "{}", needsCsrf = true)) { Unit }

    fun likeStory(id: Long, like: Boolean): ApiResult<Unit> = mapObject(
        request(
            "social/stories/$id/like",
            if (like) "POST" else "DELETE",
            body = if (like) "{}" else null,
            needsCsrf = true
        )
    ) { Unit }

    fun storyComments(id: Long): ApiResult<List<ApiStoryComment>> =
        mapObject(request("social/stories/$id/comments")) { root ->
            val array = root.optJSONArray("comments") ?: root.optJSONArray("items") ?: JSONArray()
            buildList {
                for (index in 0 until array.length()) {
                    val item = array.optJSONObject(index) ?: continue
                    val author = item.optJSONObject("author") ?: item.optJSONObject("user")
                    add(
                        ApiStoryComment(
                            id = item.optLong("id"),
                            storyId = item.optLong("storyId", id),
                            content = item.optString("content"),
                            createdAt = item.optString("createdAt"),
                            author = author?.let { runCatching { parseUser(it) }.getOrNull() }
                        )
                    )
                }
            }
        }

    fun commentStory(id: Long, content: String): ApiResult<ApiStoryComment> =
        mapObject(
            request(
                "social/stories/$id/comments",
                "POST",
                JSONObject().put("content", content).toString(),
                needsCsrf = true
            )
        ) { root ->
            val item = root.optJSONObject("comment") ?: root
            val author = item.optJSONObject("author") ?: item.optJSONObject("user")
            ApiStoryComment(
                id = item.optLong("id"),
                storyId = item.optLong("storyId", id),
                content = item.optString("content", content),
                createdAt = item.optString("createdAt"),
                author = author?.let { runCatching { parseUser(it) }.getOrNull() }
            )
        }

    fun deleteStory(id: Long): ApiResult<Unit> =
        mapObject(request("social/stories/$id", "DELETE", needsCsrf = true)) { Unit }

    fun linkPreview(url: String): ApiResult<ApiLinkPreview> {
        synchronized(linkPreviewCache) { linkPreviewCache[url]?.let { return ApiResult.Success(it) } }
        val result = mapObject(request("social/link-preview?url=${encode(url)}")) { root ->
            val item = root.optJSONObject("preview") ?: root
            ApiLinkPreview(
                url = item.optString("url", url).ifBlank { url },
                title = item.optString("title"),
                description = item.optString("description"),
                imageUrl = absolute(item.optString("image")),
                siteName = item.optString("siteName")
            )
        }
        if (result is ApiResult.Success) {
            synchronized(linkPreviewCache) { linkPreviewCache[url] = result.value }
        }
        return result
    }

    fun like(postId: Long, like: Boolean): ApiResult<Unit> = mapObject(
        request("posts/$postId/like", if (like) "POST" else "DELETE", body = if (like) "{}" else null, needsCsrf = true)
    ) { Unit }

    fun bookmark(postId: Long, bookmark: Boolean): ApiResult<Unit> = mapObject(
        request("posts/$postId/bookmark", if (bookmark) "POST" else "DELETE", body = if (bookmark) "{}" else null, needsCsrf = true)
    ) { Unit }

    fun rekarot(postId: Long, rekarot: Boolean): ApiResult<Unit> = mapObject(
        request("posts/$postId/rekarot", if (rekarot) "POST" else "DELETE", body = if (rekarot) "{}" else null, needsCsrf = true)
    ) { Unit }

    fun react(postId: Long, emoji: String, reacted: Boolean): ApiResult<Unit> = mapObject(
        if (reacted) request("posts/$postId/react", "POST", JSONObject().put("emoji", emoji).toString(), needsCsrf = true)
        else request("posts/$postId/react/${encode(emoji)}", "DELETE", needsCsrf = true)
    ) { Unit }

    fun postReactions(postId: Long): ApiResult<List<ApiReaction>> = mapObject(request("posts/$postId/react")) { root ->
        parseReactions(root.optJSONArray("reactions"))
    }

    fun votePoll(postId: Long, optionId: Long): ApiResult<ApiPoll> {
        when (val result = request(
            "posts/$postId/poll/vote",
            "POST",
            JSONObject().put("optionId", optionId).toString(),
            needsCsrf = true
        )) {
            is ApiResult.Failure -> return result
            is ApiResult.Success -> {
                val inline = result.value.optJSONObject("poll")
                    ?: result.value.optJSONObject("post")?.optJSONObject("poll")
                if (inline != null) return ApiResult.Success(parsePoll(inline))
            }
        }
        return when (val refreshed = post(postId)) {
            is ApiResult.Success -> refreshed.value.poll?.let { ApiResult.Success(it) }
                ?: ApiResult.Failure("投票結果を取得できませんでした")
            is ApiResult.Failure -> refreshed
        }
    }

    fun createPost(
        content: String,
        questionId: Long? = null,
        parentId: Long? = null,
        quotedPostId: Long? = null,
        communityId: Long? = null,
        media: List<ApiUploadMedia> = emptyList(),
        pollOptions: List<String> = emptyList(),
        pollDurationHours: Int = 24,
        visibility: String = "PUBLIC",
        replyRestriction: String = "EVERYONE",
        viewerCircleId: Long? = null,
        replyCircleId: Long? = null,
        minimumAge: Int? = null,
        maximumAge: Int? = null,
        isAiGenerated: Boolean = false,
        isPromotional: Boolean = false,
        isR18: Boolean = false,
        hideFromMinors: Boolean = false,
        scheduledFor: String? = null,
        expiresAt: String? = null
    ): ApiResult<ApiPost> {
        val boundary = "KarotterAndroid${UUID.randomUUID()}"
        val fields = linkedMapOf(
            "content" to content,
            "mediaAlts" to JSONArray(media.map { "" }).toString(),
            "mediaSpoilerFlags" to JSONArray(media.map { false }).toString(),
            "mediaR18Flags" to JSONArray(media.map { false }).toString(),
            "isAiGenerated" to isAiGenerated.toString(),
            "isPromotional" to isPromotional.toString(),
            "isR18" to isR18.toString(),
            "hideFromMinors" to hideFromMinors.toString(),
            "visibility" to visibility,
            // The current server uses FOLLOWING for the follower-only reply gate,
            // while older API references called the same setting FOLLOWERS.
            "replyRestriction" to if (replyRestriction == "FOLLOWERS") "FOLLOWING" else replyRestriction
        )
        questionId?.let { fields["questionId"] = it.toString() }
        parentId?.let { fields["parentId"] = it.toString() }
        quotedPostId?.let { fields["quotedPostId"] = it.toString() }
        communityId?.let { fields["communityId"] = it.toString() }
        viewerCircleId?.let { fields["viewerCircleId"] = it.toString() }
        replyCircleId?.let { fields["replyCircleId"] = it.toString() }
        minimumAge?.let { fields["minimumAge"] = it.toString() }
        maximumAge?.let { fields["maximumAge"] = it.toString() }
        scheduledFor?.takeIf(String::isNotBlank)?.let { fields["scheduledFor"] = it }
        expiresAt?.takeIf(String::isNotBlank)?.let { fields["expiresAt"] = it }
        pollOptions.map(String::trim).filter(String::isNotBlank).takeIf { it.size >= 2 }?.let { options ->
            fields["pollOptions"] = JSONArray(options).toString()
            fields["pollDurationHours"] = pollDurationHours.coerceIn(1, 168).toString()
        }
        val output = ByteArrayOutputStream()
        fun write(value: String) = output.write(value.toByteArray(StandardCharsets.UTF_8))
        fields.forEach { (name, value) ->
            write("--$boundary\r\nContent-Disposition: form-data; name=\"$name\"\r\n\r\n$value\r\n")
        }
        media.forEach { item ->
            val safeName = item.fileName.replace("\"", "_").replace("\r", "_").replace("\n", "_")
            write("--$boundary\r\n")
            write("Content-Disposition: form-data; name=\"media\"; filename=\"$safeName\"\r\n")
            write("Content-Type: ${item.mimeType}\r\n\r\n")
            output.write(item.bytes)
            write("\r\n")
        }
        write("--$boundary--\r\n")
        val bytes = output.toByteArray()
        return mapObject(requestBytes("posts", "POST", bytes, "multipart/form-data; boundary=$boundary", true)) { root ->
            parsePost(root.optJSONObject("post") ?: root.optJSONObject("scheduledPost") ?: root)
        }
    }

    fun logout() {
        request("auth/logout", "POST", "{}", needsCsrf = true)
        session.clear()
        credentials.clear()
    }

    private fun csrfToken(requestPath: String, forceRefresh: Boolean = false): String? {
        if (!forceRefresh) session.csrfToken?.takeIf { it.isNotBlank() }?.let { return it }
        // Cookie authentication avoids an expired bearer token masking a still
        // reusable refresh/session cookie.
        val csrfPath = if (requestPath.startsWith("http://") || requestPath.startsWith("https://")) {
            runCatching {
                val target = URL(requestPath)
                "${target.protocol}://${target.authority}/api/auth/csrf-token"
            }.getOrDefault("auth/csrf-token")
        } else {
            "auth/csrf-token"
        }
        val bearerResult = request(csrfPath, authenticated = true, retryAuth = false)
        val result = if (bearerResult is ApiResult.Success) {
            bearerResult
        } else {
            request(csrfPath, authenticated = false, retryAuth = false)
        }
        return (result as? ApiResult.Success)?.value
            ?.optString("csrfToken")
            ?.takeIf { it.isNotBlank() }
            ?.also { session.csrfToken = it }
    }

    private fun extendSession(): Boolean = synchronized(authenticationRecoveryLock) {
        extendSessionWithoutLock()
    }

    private fun extendSessionWithoutLock(): Boolean {
        if (!session.hasRefreshCookie()) return false
        val refreshBody = JSONObject()
            .put("deviceId", session.deviceId)
            .put("clientType", "android")
            .put("deviceName", CLIENT_DEVICE_NAME)
            .toString()
        fun refresh(): ApiResult<JSONObject> = request(
            "auth/refresh-token",
            "POST",
            refreshBody,
            authenticated = false,
            needsCsrf = true,
            retryAuth = false
        )
        var result = refresh()
        if (result is ApiResult.Failure && result.status == 409) {
            Thread.sleep(350L)
            result = refresh()
        }
        if (result is ApiResult.Success) {
            // The API reference only guarantees that the refresh cookie is sent.
            // Some server versions return a new bearer token in JSON, while others
            // renew the HttpOnly authentication cookies through Set-Cookie.
            val token = result.value.optString("accessToken").takeIf { it.isNotBlank() }
            token?.let { session.accessToken = it }
            result.value.optString("sessionId").takeIf { it.isNotBlank() }?.let { renewedSessionId ->
                session.sessionId = renewedSessionId
                credentials.saveSessionIdForActive(renewedSessionId)
            }
            return token != null || session.hasAuthenticationCookie()
        }
        return false
    }

    private fun switchToRecordedSession(recordedSessionId: String): ApiResult<ApiUser> {
        // Refresh CSRF before switching because the current cookie can belong to
        // another saved account.
        csrfToken("auth/switch-session", forceRefresh = true)
        val body = JSONObject()
            .put("sessionId", recordedSessionId)
            .put("deviceId", session.deviceId)
            .put("clientType", "android")
            .put("deviceName", CLIENT_DEVICE_NAME)
        var result = request(
                "auth/switch-session",
                "POST",
                body.toString(),
                authenticated = false,
                needsCsrf = true,
                retryAuth = false
            )
        if (result is ApiResult.Failure && result.status == 403) {
            session.csrfToken = null
            csrfToken("auth/switch-session", forceRefresh = true)
            result = request(
                "auth/switch-session",
                "POST",
                body.toString(),
                authenticated = false,
                needsCsrf = true,
                retryAuth = false,
                retryCsrf = false
            )
        }
        return when (result) {
            is ApiResult.Failure -> result
            is ApiResult.Success -> runCatching {
                val root = result.value
                val user = parseUser(root.getJSONObject("user"))
                session.accessToken = root.optString("accessToken").takeIf { it.isNotBlank() }
                session.sessionId = root.optString("sessionId").takeIf { it.isNotBlank() } ?: recordedSessionId
                credentials.saveSessionIdForActive(session.sessionId)
                credentials.saveProfileForActive(user.displayName, user.username, user.avatarUrl)
                ApiResult.Success(user)
            }.getOrElse { ApiResult.Failure("保存済みセッションを再開できませんでした") }
        }
    }

    private fun resumeRecordedSession(): Boolean {
        val recordedSessionId = credentials.sessionIdForActive() ?: session.sessionId ?: return false
        return switchToRecordedSession(recordedSessionId) is ApiResult.Success
    }

    private fun relogin(): Boolean {
        val now = android.os.SystemClock.elapsedRealtime()
        if (now - lastAutomaticReloginAt < 60_000L) return false
        lastAutomaticReloginAt = now
        val (identifier, password) = credentials.load() ?: return false
        return login(identifier, password, rememberCredentials = true) is ApiResult.Success
    }

    private fun recoverSession(failedAccessToken: String?): Boolean = synchronized(authenticationRecoveryLock) {
        if (
            !failedAccessToken.isNullOrBlank() &&
            !session.accessToken.isNullOrBlank() &&
            session.accessToken != failedAccessToken
        ) {
            true
        } else {
            extendSessionWithoutLock() || resumeRecordedSession() || relogin()
        }
    }

    private fun request(
        path: String,
        method: String = "GET",
        body: String? = null,
        authenticated: Boolean = true,
        needsCsrf: Boolean = false,
        retryAuth: Boolean = true,
        retryCsrf: Boolean = true
    ): ApiResult<JSONObject> = requestBytes(
        path,
        method,
        body?.toByteArray(StandardCharsets.UTF_8),
        "application/json; charset=utf-8",
        needsCsrf,
        authenticated,
        retryAuth,
        retryCsrf
    )

    private fun requestBytes(
        path: String,
        method: String,
        bytes: ByteArray?,
        contentType: String,
        needsCsrf: Boolean,
        authenticated: Boolean = true,
        retryAuth: Boolean = true,
        retryCsrf: Boolean = true
    ): ApiResult<JSONObject> {
        if (!hasNetworkConnection()) return ApiResult.Failure("ネット接続がありません", NETWORK_UNAVAILABLE_STATUS)
        var connection: HttpURLConnection? = null
        return try {
            connection = URL(if (path.startsWith("http://") || path.startsWith("https://")) path else API_BASE + path.trimStart('/')).openConnection() as HttpURLConnection
            connection.requestMethod = method
            connection.connectTimeout = 15_000
            connection.readTimeout = 25_000
            connection.instanceFollowRedirects = false
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("Accept-Language", "ja-JP,ja;q=0.9")
            connection.setRequestProperty("User-Agent", CLIENT_DEVICE_NAME)
            connection.setRequestProperty("X-Client-Type", "android")
            connection.setRequestProperty("X-Device-Id", session.deviceId)
            connection.setRequestProperty("X-Device-Name", CLIENT_DEVICE_NAME)
            session.cookieHeader().takeIf { it.isNotBlank() }?.let { connection.setRequestProperty("Cookie", it) }
            val accessTokenAtRequest = session.accessToken
            if (authenticated) accessTokenAtRequest?.let { connection.setRequestProperty("Authorization", "Bearer $it") }
            if (needsCsrf) {
                val token = csrfToken(path)
                    ?: return ApiResult.Failure("書き込み認証を確認できませんでした。もう一度お試しください", 401)
                connection.setRequestProperty("X-CSRF-Token", token)
            }
            if (bytes != null) {
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", contentType)
                connection.setFixedLengthStreamingMode(bytes.size)
                BufferedOutputStream(connection.outputStream).use { it.write(bytes) }
            }
            val status = connection.responseCode
            session.saveCookies(connection.headerFields)
            val stream = if (status in 200..299) connection.inputStream else connection.errorStream
            val text = stream?.bufferedReader(StandardCharsets.UTF_8)?.use { it.readText() }.orEmpty()
            val json = if (text.isBlank()) JSONObject() else runCatching { JSONObject(text) }.getOrElse { JSONObject().put("raw", text) }
            json.optString("csrfToken").takeIf { it.isNotBlank() }?.let { session.csrfToken = it }
            if (status == 401 && authenticated && retryAuth) {
                if (recoverSession(accessTokenAtRequest)) {
                    requestBytes(path, method, bytes, contentType, needsCsrf, authenticated, false, retryCsrf)
                }
                else {
                    onAuthenticationLost?.invoke()
                    ApiResult.Failure("セッションの再認証に失敗しました", 401)
                }
            } else if (
                status == 403 &&
                needsCsrf &&
                retryCsrf &&
                json.optString("error", json.optString("message")).contains("CSRF", ignoreCase = true)
            ) {
                session.csrfToken = null
                if (csrfToken(path, forceRefresh = true) != null) {
                    requestBytes(path, method, bytes, contentType, needsCsrf, authenticated, retryAuth, false)
                } else {
                    ApiResult.Failure("書き込み認証を更新できませんでした", 403)
                }
            } else if (status !in 200..299) {
                val message = json.optString("error", json.optString("message", "通信に失敗しました"))
                if (authenticated && method == "GET" && status >= 500 && retryAuth) {
                    Thread.sleep(400L)
                    requestBytes(path, method, bytes, contentType, needsCsrf, authenticated, false, retryCsrf)
                } else {
                    if (authenticated && method == "GET" && status >= 500) {
                        onDataAccessFailure?.invoke("サーバーからデータを取得できませんでした（HTTP $status）")
                    }
                    ApiResult.Failure(message, status)
                }
            } else ApiResult.Success(json)
        } catch (e: Exception) {
            if (!hasNetworkConnection()) {
                ApiResult.Failure("ネット接続がありません", NETWORK_UNAVAILABLE_STATUS)
            } else if (authenticated && method == "GET" && retryAuth) {
                Thread.sleep(400L)
                requestBytes(path, method, bytes, contentType, needsCsrf, authenticated, false, retryCsrf)
            } else {
                val message = if (e.message.isNullOrBlank()) "サーバーからデータを取得できませんでした" else "データ取得エラー: ${e.message}"
                if (authenticated && method == "GET") onDataAccessFailure?.invoke(message)
                ApiResult.Failure(message)
            }
        } finally { connection?.disconnect() }
    }

    private fun parsePosts(array: JSONArray?): List<ApiPost> = buildList {
        if (array == null) return@buildList
        for (i in 0 until array.length()) {
            val raw = array.optJSONObject(i) ?: continue
            val post = raw.optJSONObject("post") ?: raw.optJSONObject("originalPost") ?: raw
            val rekaroterJson = sequenceOf(
                raw.optJSONObject("rekarotedBy"),
                raw.optJSONObject("rekarotUser"),
                raw.optJSONObject("rekarotAuthor"),
                if (raw.optString("type").equals("REKAROT", true)) raw.optJSONObject("actor") else null,
                post.optJSONObject("rekarotedBy"),
                post.optJSONObject("rekarotUser")
            ).filterNotNull().firstOrNull()
            val rekaroter = rekaroterJson?.let { runCatching { parseUser(it) }.getOrNull() }
            runCatching {
                val parsed = parsePost(post, rekaroter)
                if (parsed.content.isNotBlank()) parsed
                else {
                    val wrapperContent = sequenceOf("content", "text", "body", "caption")
                        .map { raw.optString(it) }
                        .firstOrNull { it.isNotBlank() && it != "null" }
                        .orEmpty()
                    parsed.copy(content = wrapperContent)
                }
            }.getOrNull()?.let(::add)
        }
    }

    private fun parsePost(json: JSONObject, rekarotedBy: ApiUser? = null, depth: Int = 0): ApiPost {
        val mediaUrls = json.optJSONArray("mediaUrls")
        val mediaTypes = json.optJSONArray("mediaTypes")
        val mediaAlts = json.optJSONArray("mediaAlts")
        val mediaSpoilerFlags = json.optJSONArray("mediaSpoilerFlags")
        val media = buildList {
            if (mediaUrls != null) for (i in 0 until mediaUrls.length()) {
                val url = absolute(mediaUrls.optString(i)) ?: continue
                val declared = mediaTypes?.optString(i).orEmpty()
                val extension = url.substringBefore('?').substringAfterLast('.', "").lowercase()
                val inferred = when (extension) {
                    "mp4", "webm", "mov", "m4v" -> "video"
                    "mp3", "m4a", "aac", "wav", "ogg", "flac" -> "audio"
                    else -> "image"
                }
                add(
                    ApiMedia(
                        url,
                        declared.ifBlank { inferred },
                        mediaAlts?.optString(i).orEmpty(),
                        mediaSpoilerFlags?.optBoolean(i) == true
                    )
                )
            }
        }
        val content = sequenceOf("content", "text", "body", "caption")
            .map { json.optString(it) }
            .firstOrNull { it.isNotBlank() && it != "null" }
            .orEmpty()
        val viewerCircle = json.optJSONObject("viewerCircle")
        val replyCircle = json.optJSONObject("replyCircle")
        return ApiPost(
            id = json.optLong("id"),
            content = content,
            createdAt = json.optString("createdAt", json.optString("time")),
            author = parseUser(json.optJSONObject("author") ?: JSONObject()),
            likesCount = json.optInt("likesCount"),
            repliesCount = json.optInt("repliesCount"),
            rekarotsCount = json.optInt("rekarotsCount"),
            liked = json.optBoolean("liked"),
            rekaroted = json.optBoolean("rekaroted"),
            bookmarked = json.optBoolean("bookmarked"),
            mediaUrl = media.firstOrNull()?.url,
            media = media,
            quotedPost = json.optJSONObject("quotedPost")?.takeIf { depth < 2 }?.let { quoted ->
                runCatching { parsePost(quoted, depth = depth + 1) }.getOrNull()
            },
            rekarotedBy = rekarotedBy,
            reactions = parseReactions(json.optJSONArray("reactionSummary") ?: json.optJSONArray("reactions")),
            poll = json.optJSONObject("poll")?.let(::parsePoll),
            visibility = json.optString("visibility", "PUBLIC"),
            viewerCircleId = json.optNullableLong("viewerCircleId"),
            viewerCircleName = viewerCircle?.let { it.optString("name", it.optString("title")).takeIf(String::isNotBlank) },
            replyRestriction = json.optString("replyRestriction", "EVERYONE"),
            replyCircleId = json.optNullableLong("replyCircleId"),
            replyCircleName = replyCircle?.let { it.optString("name", it.optString("title")).takeIf(String::isNotBlank) },
            minimumAge = json.optNullableInt("minimumAge"),
            maximumAge = json.optNullableInt("maximumAge"),
            isAiGenerated = json.optBoolean("isAiGenerated"),
            isPromotional = json.optBoolean("isPromotional"),
            isR18 = json.optBoolean("isR18") || json.optBoolean("adminForceR18"),
            scheduledFor = json.optString("scheduledFor").takeIf { it.isNotBlank() && it != "null" },
            expiresAt = json.optString("expiresAt").takeIf { it.isNotBlank() && it != "null" },
            viewsCount = json.optInt("viewsCount"),
            bookmarksCount = json.optInt("bookmarksCount", json.optJSONObject("_count")?.optInt("bookmarks") ?: 0),
            quoteUsersCount = json.optInt("quoteUsersCount"),
            parentId = json.optNullableLong("parentId"),
            canQuote = json.optBoolean(
                "canQuote",
                !json.optJSONObject("author")
                    ?.optBoolean("isPrivate", false)
                    .orFalse()
            )
        )
    }

    private fun parseUsers(array: JSONArray?): List<ApiUser> = buildList {
        if (array == null) return@buildList
        for (i in 0 until array.length()) {
            val item = array.optJSONObject(i) ?: continue
            val user = item.optJSONObject("user") ?: item.optJSONObject("author") ?: item
            runCatching { parseUser(user) }.getOrNull()?.let(::add)
        }
    }.distinctBy(ApiUser::id)

    private fun JSONObject.optNullableInt(name: String): Int? = when (val value = opt(name)) {
        is Number -> value.toInt()
        is String -> value.toIntOrNull()
        else -> null
    }

    private fun JSONObject.optNullableLong(name: String): Long? = when (val value = opt(name)) {
        is Number -> value.toLong()
        is String -> value.toLongOrNull()
        else -> null
    }

    private fun parsePoll(json: JSONObject): ApiPoll {
        val optionsArray = json.optJSONArray("options") ?: JSONArray()
        val options = buildList {
            for (index in 0 until optionsArray.length()) {
                val option = optionsArray.optJSONObject(index) ?: continue
                add(option.optInt("position", index) to
                    ApiPollOption(
                        id = option.optLong("id"),
                        text = option.optString("text"),
                        votesCount = option.optInt("votesCount", option.optInt("votes")),
                        percentage = option.optInt("percentage"),
                        votedByMe = option.optBoolean("votedByMe")
                    )
                )
            }
        }.sortedBy { it.first }.map { it.second }
        val ownVote = json.opt("ownVoteOptionId").let {
            when (it) {
                is Number -> it.toLong()
                is String -> it.toLongOrNull()
                else -> null
            }
        }
        return ApiPoll(
            id = json.optLong("id"),
            expiresAt = json.optString("expiresAt"),
            isExpired = json.optBoolean("isExpired"),
            isAnonymous = json.optBoolean("isAnonymous"),
            totalVotes = json.optInt("totalVotes", options.sumOf(ApiPollOption::votesCount)),
            ownVoteOptionId = ownVote,
            options = options
        )
    }

    private fun parseReactions(array: JSONArray?): List<ApiReaction> {
        if (array == null) return emptyList()
        val grouped = linkedMapOf<String, ApiReaction>()
        for (i in 0 until array.length()) {
            val item = array.optJSONObject(i) ?: continue
            val emoji = item.optString("emoji").takeIf { it.isNotBlank() && it != "null" } ?: continue
            val previous = grouped[emoji]
            val explicitCount = item.optInt("count", 0)
            grouped[emoji] = ApiReaction(
                emoji,
                if (explicitCount > 0) explicitCount else (previous?.count ?: 0) + 1,
                item.optBoolean("reacted") || previous?.reacted == true
            )
        }
        return grouped.values.toList()
    }

    private fun parseGenericMedia(json: JSONObject, urlsKey: String, typesKey: String): List<ApiMedia> {
        val urls = json.optJSONArray(urlsKey) ?: return emptyList()
        val types = json.optJSONArray(typesKey)
        return buildList {
            for (i in 0 until urls.length()) {
                val url = absolute(urls.optString(i)) ?: continue
                add(ApiMedia(url, types?.optString(i).orEmpty().ifBlank { "image" }, ""))
            }
        }
    }

    private fun parseArticle(a: JSONObject) = ApiArticle(
        a.optString("slug", a.optString("id")), a.optString("title", "無題の記事"),
        a.optString("summary", a.optString("description")), a.optString("category", "general"),
        a.optString("createdAt"), absolute(a.optString("imageUrl", a.optString("coverImageUrl", a.optString("coverImage")))), a.optString("content")
    )

    private fun notificationMessage(type: String, actor: String, actorCount: Int): String {
        val others = if (actorCount > 1) "ほか${actorCount - 1}人が" else "が"
        return when (type) {
            "LIKE" -> "${actor}${others}あなたの投稿にいいねしました"
            "REPLY" -> "${actor}${others}あなたの投稿に返信しました"
            "MENTION" -> "${actor}${others}あなたをメンションしました"
            "FOLLOW" -> "${actor}${others}あなたをフォローしました"
            "FOLLOW_REQUEST" -> "${actor}${others}フォローをリクエストしました"
            "FOLLOWED_POST" -> "${actor}${others}新しく投稿しました"
            "REKAROT" -> "${actor}${others}あなたの投稿をリカロートしました"
            "QUOTE" -> "${actor}${others}あなたの投稿を引用しました"
            "DM" -> "${actor}${others}メッセージを送りました"
            "BOARD_NEW_THREAD" -> "フォロー中の掲示板に新しいスレッドがあります"
            "BOARD_THREAD_REPLY" -> "フォロー中のスレッドに返信があります"
            "COMMUNITY_INVITE" -> "コミュニティへの招待があります"
            "COMMUNITY_JOIN" -> "コミュニティに新しい参加者がいます"
            "COMMUNITY_REMOVAL" -> "コミュニティから除外されました"
            "REPORT_UPDATE" -> "報告した内容に更新があります"
            "SYSTEM" -> "Karotterからのお知らせ"
            else -> "Karotterからのお知らせ"
        }
    }

    private fun parseDmMessage(json: JSONObject): ApiDmMessage {
        val senderJson = json.optJSONObject("sender")
        val urls = json.optJSONArray("attachmentUrls")
        val types = json.optJSONArray("attachmentTypes")
        val alts = json.optJSONArray("attachmentAlts")
        val media = buildList {
            if (urls != null) for (index in 0 until urls.length()) {
                val url = absolute(urls.optString(index)) ?: continue
                add(ApiMedia(url, types?.optString(index).orEmpty().ifBlank { "image" }, alts?.optString(index).orEmpty()))
            }
        }
        return ApiDmMessage(
            json.optLong("id"), json.optLong("groupId"), json.optLong("senderId"),
            json.optString("content"), json.optString("createdAt"), senderJson?.let { runCatching { parseUser(it) }.getOrNull() },
            media
        )
    }

    private fun parseDmGroup(group: JSONObject): ApiDmGroup {
        val membersArray = group.optJSONArray("members") ?: JSONArray()
        val members = buildList {
            for (index in 0 until membersArray.length()) {
                membersArray.optJSONObject(index)?.let { member ->
                    add(parseUser(member.optJSONObject("user") ?: member))
                }
            }
        }
        val last = group.optJSONObject("lastMessage")
        val requestStatus = sequenceOf("requestStatus", "dmRequestStatus", "status")
            .map { group.optString(it) }
            .firstOrNull { it.isNotBlank() && it != "null" }
            .orEmpty()
        val isRequest = group.optBoolean("isRequest") ||
            group.optBoolean("isMessageRequest") ||
            group.optBoolean("isPendingRequest") ||
            requestStatus.equals("PENDING", true) ||
            requestStatus.equals("REQUEST", true)
        return ApiDmGroup(
            id = group.optLong("id", group.optLong("groupId")),
            members = members,
            lastMessage = last?.optString("content").orEmpty(),
            lastMessageAt = last?.optString("createdAt").orEmpty(),
            canSend = group.optBoolean("canSend", !isRequest),
            isRequest = isRequest,
            requestStatus = requestStatus,
            sendDisabledReason = group.optString("sendDisabledReason"),
            unreadCount = sequenceOf("unreadCount", "unreadMessagesCount", "unreadMessageCount", "newMessageCount")
                .firstNotNullOfOrNull { name ->
                    if (group.has(name) && !group.isNull(name)) group.optInt(name).coerceAtLeast(0) else null
                }
                ?: if (group.optBoolean("hasUnread") || group.optBoolean("isUnread")) 1 else 0,
            name = sequenceOf("name", "groupName", "title")
                .map { group.optString(it) }
                .firstOrNull { it.isNotBlank() && it != "null" }
                .orEmpty()
        )
    }

    private fun parseUser(json: JSONObject): ApiUser {
        val rawMark = json.opt("officialMark")
        val officialMarks = when (rawMark) {
            is JSONArray -> buildList {
                for (index in 0 until rawMark.length()) {
                    val value = rawMark.optString(index).ifBlank {
                        val markObject = rawMark.optJSONObject(index)
                        markObject?.optString("type", markObject.optString("name")).orEmpty()
                    }
                    value.takeIf { it.isNotBlank() && !it.equals("NONE", true) }?.let(::add)
                }
            }
            is JSONObject -> listOf(rawMark.optString("type", rawMark.optString("name")))
            null, JSONObject.NULL -> emptyList()
            else -> listOf(rawMark.toString())
        }.filter { it.isNotBlank() && !it.equals("NONE", true) && it != "[]" }.distinct()
        val officialMark = officialMarks.firstOrNull() ?: "NONE"
        val levelObject = listOf("levelInfo", "levelStats", "levelProgress", "experienceInfo")
            .firstNotNullOfOrNull { json.optJSONObject(it) }
            ?: (json.opt("level") as? JSONObject)
        fun number(vararg names: String): Double? {
            for (source in listOfNotNull(levelObject, json)) {
                for (name in names) {
                    if (!source.has(name) || source.isNull(name)) continue
                    val parsed = when (val value = source.opt(name)) {
                        is Number -> value.toDouble()
                        is String -> value.toDoubleOrNull()
                        else -> null
                    }
                    if (parsed != null && parsed.isFinite()) return parsed
                }
            }
            return null
        }
        val level = when (val value = json.opt("level")) {
            is Number -> value.toInt()
            is String -> value.toIntOrNull()
            else -> number("currentLevel", "userLevel")?.toInt()
        }?.coerceAtLeast(0)
        val directProgress = number("levelProgress", "progressPercentage", "progressPercent", "percentage", "percent", "progress")
        val currentExperience = number("experienceInLevel", "currentLevelExperience", "currentLevelExp", "xpInLevel", "expInLevel", "currentXp", "currentXP", "experience", "exp", "xp")
        val requiredExperience = number("experienceRequiredForNextLevel", "experienceForNextLevel", "expForNextLevel", "xpForNextLevel", "requiredExperience", "requiredExp", "requiredXp", "nextLevelRequirement")
        val totalExperience = number("totalExperience", "totalExp", "totalXp")
        val levelStartExperience = number("currentLevelThreshold", "levelStartExperience", "levelStartExp")
        val nextLevelExperience = number("nextLevelExperience", "nextLevelExp", "nextLevelXp", "nextLevelThreshold")
        val calculatedProgress = when {
            directProgress != null -> if (directProgress <= 1.0) directProgress * 100.0 else directProgress
            currentExperience != null && requiredExperience != null && requiredExperience > 0.0 ->
                currentExperience / requiredExperience * 100.0
            totalExperience != null && levelStartExperience != null && nextLevelExperience != null && nextLevelExperience > levelStartExperience ->
                (totalExperience - levelStartExperience) / (nextLevelExperience - levelStartExperience) * 100.0
            currentExperience != null && nextLevelExperience != null && nextLevelExperience > 0.0 ->
                currentExperience / nextLevelExperience * 100.0
            else -> null
        }
        val levelProgressPercent = calculatedProgress?.coerceIn(0.0, 100.0)?.toInt()
        val dmPolicy = json.optString("dmRequestPolicy").uppercase()
        val canReceiveDm = when {
            json.has("canSendDirectMessage") -> json.optBoolean("canSendDirectMessage")
            json.has("canDirectMessage") -> json.optBoolean("canDirectMessage")
            json.has("canMessage") -> json.optBoolean("canMessage")
            json.has("directMessagesEnabled") -> json.optBoolean("directMessagesEnabled")
            dmPolicy in setOf("NOBODY", "NONE", "DISABLED") -> false
            else -> true
        }
        val pinnedPost = json.optJSONObject("pinnedPost")?.let { pinned ->
            runCatching { parsePost(pinned) }.getOrNull()
        }
        val pinnedPosts = buildList {
            val array = json.optJSONArray("pinnedPosts") ?: JSONArray()
            for (index in 0 until array.length()) {
                array.optJSONObject(index)?.let { item ->
                    runCatching { parsePost(item) }.getOrNull()?.let(::add)
                }
            }
            if (isEmpty() && pinnedPost != null) add(pinnedPost)
        }.distinctBy { it.id }
        val badgeColors = buildList {
            val array = json.optJSONArray("subscriptionBadgeColors") ?: JSONArray()
            for (index in 0 until array.length()) array.optString(index).takeIf(String::isNotBlank)?.let(::add)
        }
        val profileUnavailableDetails = buildList {
            val array = json.optJSONArray("profileUnavailableDetails") ?: JSONArray()
            for (index in 0 until array.length()) {
                array.optString(index).takeIf(String::isNotBlank)?.let(::add)
            }
        }
        return ApiUser(
            json.optLong("id"), json.optString("username"), json.optString("displayName", json.optString("username", "Karotter user")),
            absolute(json.optString("avatarUrl")), absolute(json.optString("headerUrl")), json.optString("bio"), absolute(json.optString("websiteUrl")), json.optBoolean("showLikedPosts", false),
            json.optInt("followersCount"), json.optInt("followingCount"), json.optInt("postsCount"), officialMark,
            json.optBoolean("isBotAccount", json.optBoolean("isBot")),
            json.optBoolean("isParodyAccount", json.optBoolean("isParody")),
            json.optBoolean("isFollowing", json.optBoolean("is_following")),
            json.optBoolean(
                "hasPendingRequest",
                json.optBoolean("followRequestSent", json.optBoolean("follow_request_sent"))
            ),
            level,
            levelProgressPercent,
            sequenceOf("createdAt", "joinedAt", "registeredAt")
                .map { json.optString(it) }
                .firstOrNull { it.isNotBlank() && it != "null" },
            canReceiveDm,
            pinnedPost,
            json.optBoolean("isPrivate", json.optBoolean("private", json.optBoolean("isProtected"))),
            json.optBoolean("isFollowedBy", json.optBoolean("is_followed_by")),
            json.optString("location").takeUnless { it.equals("null", true) }.orEmpty(),
            json.optString("onlineStatus", "OFFLINE"),
            json.optString("statusMessage").takeUnless { it.equals("null", true) }.orEmpty(),
            json.optString("subscriptionPlan", "FREE"),
            json.optString("subscriptionStatus", "INACTIVE"),
            badgeColors,
            json.optBoolean("showSubscriptionBadges", true),
            json.optBoolean("showPlusBadge", true),
            json.optBoolean("showProBadge", true),
            json.optString("premiumBadgeColor", "ORANGE"),
            json.optBoolean("showProfileDecoration", true),
            json.optBoolean("showCardDecoration", true),
            json.optString("profileAccentColor").takeIf { it.isNotBlank() && it != "null" },
            json.optString("cardAccentColor").takeIf { it.isNotBlank() && it != "null" },
            json.optBoolean("isPremium", json.optString("subscriptionStatus") == "ACTIVE"),
            pinnedPosts,
            officialMarks,
            json.optBoolean("isMuted"),
            json.optBoolean("isBlocked", json.optBoolean("hasBlocked")),
            json.optBoolean("isBlockedBy"),
            json.optBoolean("isPostNotificationsEnabled"),
            json.optBoolean("isBanned", json.optBoolean("banned", json.optBoolean("adminForceBanned"))),
            json.optInt(
                "pinnedPostLimit",
                when (json.optString("subscriptionPlan", "FREE").uppercase()) {
                    "PRO" -> 5
                    "PLUS" -> 3
                    else -> 1
                }
            ).coerceAtLeast(1),
            json.optBoolean("questionsEnabled"),
            json.optNullableInt("profileMinimumAge"),
            json.optNullableInt("profileMaximumAge"),
            json.optBoolean("hideProfileFromMinors"),
            json.optString("profileUnavailableReason").takeIf { it.isNotBlank() && it != "null" },
            profileUnavailableDetails
        )
    }

    private fun Boolean?.orFalse(): Boolean = this == true

    private fun mergeLevelData(user: JSONObject, envelope: JSONObject) {
        if (user === envelope) return
        listOf(
            "level",
            "levelProgress",
            "levelInfo",
            "levelStats",
            "experienceInfo",
            "currentLevel",
            "currentLevelExperience",
            "experienceInLevel",
            "experienceForNextLevel",
            "experienceRequiredForNextLevel",
            "totalExperience",
            "nextLevelExperience"
        ).forEach { key ->
            if (!user.has(key) && envelope.has(key)) user.put(key, envelope.opt(key))
        }
    }

    private fun absolute(path: String?): String? = path?.takeIf { it.isNotBlank() && it != "null" }?.let { if (it.startsWith("http")) it else "$MEDIA_BASE${if (it.startsWith('/')) it else "/$it"}" }
    private fun encode(value: String): String = URLEncoder.encode(value, "UTF-8")
    private inline fun <T> mapObject(result: ApiResult<JSONObject>, transform: (JSONObject) -> T): ApiResult<T> = when (result) {
        is ApiResult.Failure -> result
        is ApiResult.Success -> try { ApiResult.Success(transform(result.value)) } catch (_: Exception) { ApiResult.Failure("サーバー応答の形式が想定と異なります") }
    }
}
