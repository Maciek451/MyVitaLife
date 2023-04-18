/**
 * The main Application class for the MyApplication app.
 * This class is annotated with @HiltAndroidApp to enable Hilt for dependency injection.
 *
 * @author Maciej Traczyk
 */
package uk.ac.aber.dcs.cs39440.myvitalife

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application()