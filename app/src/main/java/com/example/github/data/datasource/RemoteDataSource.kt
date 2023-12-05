package com.example.github.data.datasource

import com.example.github.data.model.CommitModelRemote
import com.example.github.data.model.RepoModelRemote
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path

class RemoteDataSource(private val client: HttpClient) {

    suspend fun getReposFromRemote(): List<RepoModelRemote.GithubRepoModelItem> =
        client.get {
            url { path("/users/mralexgray/repos") }
        }.body<List<RepoModelRemote.GithubRepoModelItem>>()

    suspend fun getCommitsForARepoRemote(
        repoName: String,
    ): List<CommitModelRemote.CommitModelItem> =
        client.get {
            url { path("/repos/mralexgray/$repoName/commits") }
        }.body<List<CommitModelRemote.CommitModelItem>>()


}