package com.example.github.domain

import com.example.github.data.model.ApiResult
import com.example.github.data.model.CommitModelLocal
import com.example.github.data.model.RepoModelLocal
import kotlinx.coroutines.flow.Flow

interface HomeUseCase {

    suspend fun getGithubRepos() : Flow<ApiResult<List<RepoModelLocal>>>

    suspend fun getCommitsForARepo(repositoryName:String,noOfItem: Int) : Flow<ApiResult<List<CommitModelLocal>>>
}