package com.example.testbalinasoft.di

import android.app.Application
import androidx.room.Room
import com.example.testbalinasoft.data.MyDao
import com.example.testbalinasoft.data.MyDatabase
import com.example.testbalinasoft.repository.DataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) =
        Room.databaseBuilder(app, MyDatabase::class.java, "my_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideDao(db: MyDatabase) = db.roomDao()

    @Provides
    @Singleton
    fun provideRepository(myDao: MyDao) = DataRepository(myDao)

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}
@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope