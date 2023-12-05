package com.example.github.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.github.data.model.CommitModelLocal
import com.example.github.data.model.RepoModelLocal

@Database(
    entities = [RepoModelLocal::class, CommitModelLocal::class],
    version = 8
)
abstract class GithubDatabase : RoomDatabase() {
    abstract fun repoDao(): GithubDao
    abstract fun commitDao(): CommitDao
}
