package com.example.github.ui.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.data.model.ApiResult
import com.example.github.data.model.ApiStatus.*
import com.example.github.data.model.CommitModelLocal
import com.example.github.data.model.RepoModelLocal
import com.example.github.domain.HomeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    application: Application,
    private val homeUseCase: HomeUseCase
) : AndroidViewModel(application) {

    val firstFiveRepos: MutableStateFlow<List<RepoModelLocal>> =
        MutableStateFlow(
            emptyList()
        )

    val bottomList: MutableStateFlow<List<RepoModelLocal>> = MutableStateFlow(
        emptyList()
    )

    init {
        viewModelScope.launch {
            homeUseCase.getGithubRepos().collectLatest {
                when (it.status) {
                    IDLE -> {

                    }
                    SUCCESS -> {
                        firstFiveRepos.value = it.data?.take(5) ?: emptyList()
                        bottomList.value = it.data?.drop(5) ?: emptyList()
                    }

                    ERROR -> {
                        //show Error
                    }
                    LOADING -> {
                        //Loading Status
                    }
                }

            }
        }
    }

    suspend fun getCommitsForRepo(
        repo: String,
        noOfItems: Int
    ): Flow<ApiResult<List<CommitModelLocal>>> {
        return homeUseCase.getCommitsForARepo(repo, noOfItems).stateIn(viewModelScope)
    }
}