package com.example.github.data.db

import androidx.room.*
import com.example.github.data.model.CommitModelLocal
import com.example.github.data.model.RepoModelLocal
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubDao {

    @Query("SELECT * FROM repo")
    fun getGithubRepos(): Flow<List<RepoModelLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepos(repo: List<RepoModelLocal>)

    @Update
    suspend fun updateRepos(cars: List<RepoModelLocal>)

    @Query("DELETE FROM repo")
    suspend fun deleteAllRepos()

    @Transaction
    @Query("SELECT * FROM Repo")
    fun getRepoWithCommits(): List<RepoWithCommits>
}

@Dao
interface CommitDao {
    @Query("SELECT * FROM `commit` WHERE repoName = :repoName")
    fun getCommitForARepo(repoName: String): Flow<List<CommitModelLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommits(commit: List<CommitModelLocal>)

    @Update
    suspend fun updateCommits(commit: List<CommitModelLocal>)

    @Query("DELETE FROM `commit`")
    suspend fun deleteAllCommits()
}

data class RepoWithCommits(
    @Embedded val repo: RepoModelLocal,
    @Relation(
        parentColumn = "name",
        entityColumn = "repoName"
    )
    val commits: List<CommitModelLocal>
)