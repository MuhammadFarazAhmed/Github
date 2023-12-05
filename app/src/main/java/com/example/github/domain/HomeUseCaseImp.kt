package com.example.github.domain

import com.example.github.data.model.ApiResult
import com.example.github.data.model.CommitModelLocal
import com.example.github.data.model.RepoModelLocal
import com.example.github.data.repositories.HomeRepository
import kotlinx.coroutines.flow.Flow

class HomeUseCaseImp(private val homeRepository: HomeRepository) : HomeUseCase {

    override suspend fun getGithubRepos(): Flow<ApiResult<List<RepoModelLocal>>> {
        return homeRepository.getGithubRepos()
    }

    override suspend fun getCommitsForARepo(repositoryName: String,noOfItem:Int): Flow<ApiResult<List<CommitModelLocal>>> {
        return homeRepository.getCommitsForRepo(repositoryName,noOfItem)
    }

}