package com.hussienfahmy.geologyofegyptformations.di

import android.content.Context
import androidx.room.Room
import com.hussienfahmy.geologyofegyptformations.database.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context.applicationContext,
        AppDataBase::class.java, "database"
    ).fallbackToDestructiveMigration()
        .createFromAsset("databases/database.db")
        .build()

    @Provides
    fun providePracticalFormationsDao(dataBase: AppDataBase) = dataBase.daoPracticalFormations

    @Provides
    fun provideEgyptFormationsDao(dataBase: AppDataBase) = dataBase.daoEgyptFormations

}