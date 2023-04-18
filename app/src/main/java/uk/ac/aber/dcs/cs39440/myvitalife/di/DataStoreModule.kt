package uk.ac.aber.dcs.cs39440.myvitalife.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.ac.aber.dcs.cs39440.myvitalife.datastore.MyDataStore
import javax.inject.Singleton

/**
 * Dagger module that provides a singleton instance of MyDataStore.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    /**
     * Provides a singleton instance of MyDataStore using the application context.
     *
     * @param context The application context used to create the MyDataStore instance.
     * @return A singleton instance of MyDataStore.
     */
    @Provides
    @Singleton
    fun provideDataStoreRepository(
        @ApplicationContext context: Context
    ) = MyDataStore(context = context)
}