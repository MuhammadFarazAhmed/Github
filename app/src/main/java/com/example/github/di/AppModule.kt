package com.example.github.di

import android.app.Application
import androidx.room.Room
import com.example.github.data.datasource.LocalDataSource
import com.example.github.data.datasource.RemoteDataSource
import com.example.github.data.db.CommitDao
import com.example.github.data.db.GithubDao
import com.example.github.data.db.GithubDatabase
import com.example.github.data.repositories.HomeRepository
import com.example.github.data.repositories.HomeRepositoryImp
import com.example.github.domain.HomeUseCase
import com.example.github.domain.HomeUseCaseImp
import com.example.github.ui.vm.HomeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import io.ktor.utils.io.readUTF8Line
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


fun appModule() = listOf(AppModule, LocalModule, NetworkModule)

fun featureModule() = listOf(HomeModule)

val AppModule = module {
}

val LocalModule = module {

    fun provideDataBase(application: Application): GithubDatabase {
        return Room.databaseBuilder(application, GithubDatabase::class.java, "github_database")
            .fallbackToDestructiveMigration().build()
    }

    fun provideGithubDao(dataBase: GithubDatabase): GithubDao {
        return dataBase.repoDao()
    }

    fun provideCommitDao(dataBase: GithubDatabase): CommitDao {
        return dataBase.commitDao()
    }

    single { provideDataBase(androidApplication()) }
    single { provideGithubDao(get()) }
    single { provideCommitDao(get()) }
}

val HomeModule = module {
    single { LocalDataSource(get(), get(), get()) }
    single { RemoteDataSource(get()) }
    single<HomeRepository> { HomeRepositoryImp(get(),get()) }
    single<HomeUseCase> { HomeUseCaseImp(get()) }
    viewModel { HomeViewModel(get(), get()) }
}

@OptIn(InternalAPI::class)
val NetworkModule = module {
    single {
        HttpClient(Android) {

            defaultRequest {
                url {
                    contentType(ContentType.Application.Json)
                    accept(ContentType.Application.Json)

                    protocol = URLProtocol.HTTPS
                    host = "api.github.com"
                }
            }


            install(Logging) { level = LogLevel.ALL }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            expectSuccess = true

            HttpResponseValidator {
                handleResponseExceptionWithRequest { cause, _ ->
                    when (cause) {
                        is ClientRequestException -> {
                            when (cause.response.status) {
                                HttpStatusCode.UnprocessableEntity -> {
                                    cause.response.content.readUTF8Line()?.let {
                                        throw Exception(
                                            "error",
                                            cause
                                        )
                                    }
                                }

                                NotFound -> {
                                    throw Exception(
                                        "Url not Found 404",
                                        cause
                                    )
                                }

                                Conflict -> {
                                    throw Exception(
                                        "Git Repository is Empty",
                                        cause
                                    )
                                }
                            }
                        }

                        else -> {
                            throw Exception(cause)
                        }
                    }
                }
            }
        }
    }
}


