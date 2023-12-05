package com.example.github.data.datasource

import androidx.room.withTransaction
import com.example.github.data.db.CommitDao
import com.example.github.data.db.GithubDao
import com.example.github.data.db.GithubDatabase
import com.example.github.data.model.CommitModelLocal
import com.example.github.data.model.RepoModelLocal
import kotlinx.coroutines.flow.Flow

class LocalDataSource(
    private val db: GithubDatabase,
    private val githubDao: GithubDao,
    private val commitDao: CommitDao
) {

    fun getReposFromDatabase(): Flow<List<RepoModelLocal>> =
        githubDao.getGithubRepos()

    fun getCommitsForARepoDatabase(repoName: String, noOfItem: Int): Flow<List<CommitModelLocal>> =
        commitDao.getCommitForARepo(repoName)

    suspend fun saveRepoListInDatabase(repoList: List<RepoModelLocal>) {
        db.withTransaction {
            githubDao.deleteAllRepos()
            githubDao.insertRepos(repoList)
        }
    }

    suspend fun saveCommitListInDatabase(repoList: List<CommitModelLocal>) {
        db.withTransaction {
            commitDao.insertCommits(repoList)
        }
    }
}