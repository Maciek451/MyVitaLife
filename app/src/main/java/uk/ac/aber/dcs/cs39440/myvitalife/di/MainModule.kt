package uk.ac.aber.dcs.cs39440.myvitalife.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.ac.aber.dcs.cs39440.myvitalife.datastorage.DataStorage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    /**
     * Provides a built DataStorage object, so that it can be injected later on
     */
    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext context: Context
    ) = DataStorage(context = context)
}