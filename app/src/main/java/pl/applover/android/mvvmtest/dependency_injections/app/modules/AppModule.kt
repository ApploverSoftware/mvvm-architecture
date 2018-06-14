package pl.applover.android.mvvmtest.dependency_injections.app.modules

import android.app.Application
import dagger.Binds
import dagger.Module
import pl.applover.android.mvvmtest.App
import javax.inject.Singleton


/**
 * Created by Janusz Hain on 2018-01-08.
 */
@Module(subcomponents = [])
abstract class AppModule {
    @Binds
    @Singleton
    internal abstract fun application(app: App): Application

}