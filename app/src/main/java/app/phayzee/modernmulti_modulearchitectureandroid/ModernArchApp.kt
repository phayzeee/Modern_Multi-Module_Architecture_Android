package app.phayzee.modernmulti_modulearchitectureandroid

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ModernArchApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}