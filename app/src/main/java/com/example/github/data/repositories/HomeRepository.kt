package com.example.github.data.repositories

import com.example.github.data.model.ApiResult
import com.example.github.data.model.CommitModelLocal
import com.example.github.data.model.RepoModelLocal
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    suspend fun getGithubRepos() : Flow<ApiResult<List<RepoModelLocal>>>

    suspend fun getCommitsForRepo(repositoryName: String,noOfItem: Int): Flow<ApiResult<List<CommitModelLocal>>>

}