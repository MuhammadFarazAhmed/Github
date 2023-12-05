package com.example.github.data.repositories

import com.example.github.data.datasource.LocalDataSource
import com.example.github.data.datasource.RemoteDataSource
import com.example.github.data.db.GithubDao
import com.example.github.data.db.GithubDatabase
import com.example.github.data.model.ApiResult
import com.example.github.data.model.CommitModelLocal
import com.example.github.data.model.RepoModelLocal
import com.example.github.data.model.networkBoundResource
import com.example.github.data.model.toLocalCommit
import com.example.github.data.model.toLocalRepo
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow

class HomeRepositoryImp(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : HomeRepository {

    override suspend fun getGithubRepos(): Flow<ApiResult<List<RepoModelLocal>>> =
        networkBoundResource(query = {
            localDataSource.getReposFromDatabase()
        }, fetch = {
          remoteDataSource.getReposFromRemote().map { it.toLocalRepo() }
        }, saveFetchResult = { repoList ->
            if (repoList.isNotEmpty())
              localDataSource.saveRepoListInDatabase(repoList)
        })


    override suspend fun getCommitsForRepo(
        repositoryName: String,
        noOfItem: Int
    ): Flow<ApiResult<List<CommitModelLocal>>> =
        networkBoundResource(query = {
            localDataSource.getCommitsForARepoDatabase(repositoryName, noOfItem)
        }, fetch = {
            remoteDataSource.getCommitsForARepoRemote(repositoryName).map { it.toLocalCommit(repositoryName) }.take(noOfItem)
        }, saveFetchResult = { commitList ->
            if (commitList.isNotEmpty()) {
                localDataSource.saveCommitListInDatabase(commitList)
            }
        })
}