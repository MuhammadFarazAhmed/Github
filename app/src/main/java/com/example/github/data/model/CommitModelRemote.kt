package com.example.github.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CommitModelRemote : ArrayList<CommitModelRemote.CommitModelItem>() {
    @Serializable
    data class CommitModelItem(
        @SerialName("sha")
        val sha: String,
        @SerialName("node_id")
        val nodeId: String? = "",
        @SerialName("commit")
        val commit: Commit? = null,
        @SerialName("url")
        val url: String? = null,
        @SerialName("html_url")
        val htmlUrl: String? = null,
        @SerialName("comments_url")
        val commentsUrl: String? = null,
        @SerialName("author")
        val author: Author? = null,
        @SerialName("committer")
        val committer: Committer? = null,
        @SerialName("parents")
        val parents: List<Parent>? = null
    ) {
        @Serializable
        data class Commit(
            @SerialName("author")
            val author: Author? = null,
            @SerialName("committer")
            val committer: Committer? = null,
            @SerialName("message")
            val message: String? = null,
            @SerialName("tree")
            val tree: Tree? = null,
            @SerialName("url")
            val url: String? = null,
            @SerialName("comment_count")
            val commentCount: Int? = null,
            @SerialName("verification")
            val verification: Verification? = null
        ) {
            @Serializable
            data class Author(
                @SerialName("name")
                val name: String? = null,
                @SerialName("email")
                val email: String? = null,
                @SerialName("date")
                val date: String? = null
            )

            @Serializable
            data class Committer(
                @SerialName("name")
                val name: String? = null,
                @SerialName("email")
                val email: String? = null,
                @SerialName("date")
                val date: String? = null
            )

            @Serializable
            data class Tree(
                @SerialName("sha")
                val sha: String? = null,
                @SerialName("url")
                val url: String? = null
            )

            @Serializable
            data class Verification(
                @SerialName("verified")
                val verified: Boolean? = null,
                @SerialName("reason")
                val reason: String? = null,
                @SerialName("signature")
                val signature: String? = null,
                @SerialName("payload")
                val payload: String? = null
            )
        }

        @Serializable
        data class Author(
            @SerialName("login")
            val login: String? = null,
            @SerialName("id")
            val id: Int,
            @SerialName("node_id")
            val nodeId: String? = null,
            @SerialName("avatar_url")
            val avatarUrl: String? = null,
            @SerialName("gravatar_id")
            val gravatarId: String? = null,
            @SerialName("url")
            val url: String? = null,
            @SerialName("html_url")
            val htmlUrl: String? = null,
            @SerialName("followers_url")
            val followersUrl: String? = null,
            @SerialName("following_url")
            val followingUrl: String? = null,
            @SerialName("gists_url")
            val gistsUrl: String? = null,
            @SerialName("starred_url")
            val starredUrl: String? = null,
            @SerialName("subscriptions_url")
            val subscriptionsUrl: String? = null,
            @SerialName("organizations_url")
            val organizationsUrl: String? = null,
            @SerialName("repos_url")
            val reposUrl: String? = null,
            @SerialName("events_url")
            val eventsUrl: String? = null,
            @SerialName("received_events_url")
            val receivedEventsUrl: String? = null,
            @SerialName("type")
            val type: String? = null,
            @SerialName("site_admin")
            val siteAdmin: Boolean? = null
        )

        @Serializable
        data class Committer(
            @SerialName("login")
            val login: String? = null,
            @SerialName("id")
            val id: Int? = null,
            @SerialName("node_id")
            val nodeId: String? = null,
            @SerialName("avatar_url")
            val avatarUrl: String? = null,
            @SerialName("gravatar_id")
            val gravatarId: String? = null,
            @SerialName("url")
            val url: String? = null,
            @SerialName("html_url")
            val htmlUrl: String? = null,
            @SerialName("followers_url")
            val followersUrl: String? = null,
            @SerialName("following_url")
            val followingUrl: String? = null,
            @SerialName("gists_url")
            val gistsUrl: String? = null,
            @SerialName("starred_url")
            val starredUrl: String? = null,
            @SerialName("subscriptions_url")
            val subscriptionsUrl: String? = null,
            @SerialName("organizations_url")
            val organizationsUrl: String? = null,
            @SerialName("repos_url")
            val reposUrl: String? = null,
            @SerialName("events_url")
            val eventsUrl: String? = null,
            @SerialName("received_events_url")
            val receivedEventsUrl: String? = null,
            @SerialName("type")
            val type: String? = null,
            @SerialName("site_admin")
            val siteAdmin: Boolean? = null
        )

        @Serializable
        data class Parent(
            @SerialName("sha")
            val sha: String? = null,
            @SerialName("url")
            val url: String? = null,
            @SerialName("html_url")
            val htmlUrl: String? = null
        )
    }
}

@Entity(tableName = "commit")
data class CommitModelLocal(
    @PrimaryKey val sha: String = "",
    val repoName: String = "",
    val message: String? = null,
)

fun CommitModelRemote.CommitModelItem.toLocalCommit(repoName: String) =
    CommitModelLocal(sha = sha, repoName, commit?.message ?: "")