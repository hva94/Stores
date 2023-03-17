package com.hvasoft.stores.di

import android.app.Application
import androidx.room.Room
import com.hvasoft.stores.data.StoresRepositoryImpl
import com.hvasoft.stores.data.database.StoresDatabase
import com.hvasoft.stores.domain.repository.StoresRepository
import com.hvasoft.stores.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideStoresDatabase(app: Application): StoresDatabase {
        return Room.databaseBuilder(
            app,
            StoresDatabase::class.java,
            StoresDatabase.DATABASE_NAME
        )
            .build()
    }

    @Provides
    @Singleton
    fun provideStoresRepository(
        db: StoresDatabase
    ): StoresRepository {
        return StoresRepositoryImpl(db.storesDao)
    }

    @Provides
    @Singleton
    fun provideStoresUseCases(
        repository: StoresRepository
    ): StoresUseCases {
        return StoresUseCases(
            getAllStores = GetAllStoresUseCase(repository),
            getSingleStore = GetSingleStoreUseCase(repository),
            insertStore = InsertStoreUseCase(repository),
            updateStore = UpdateStoreUseCase(repository),
            deleteStore = DeleteStoreUseCase(repository)
        )
    }

}