package pl.applover.android.mvvmtest.dependency_injections.repositories.example

import dagger.Module
import dagger.Provides
import pl.applover.android.mvvmtest.data.example.database.ExampleRoomDatabase
import pl.applover.android.mvvmtest.data.example.database.dao.ExampleCityDao
import pl.applover.android.mvvmtest.data.example.internet.api_endpoints.ExampleCitiesApiEndpointsInterface
import pl.applover.android.mvvmtest.data.example.repositories.ExampleCitiesRepository
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton


@Module(includes = [ExampleRepositoriesModule.CarRepositoryModule::class])
class ExampleRepositoriesModule {

    @Module
    class CarRepositoryModule {
        @Provides
        @Singleton
        fun provideExampleCitiesApiEndpointsInterface(@Named("example") retrofit: Retrofit)
                : ExampleCitiesApiEndpointsInterface = retrofit.create(ExampleCitiesApiEndpointsInterface::class.java)

        @Provides
        @Singleton
        fun provideExampleCityDao(exampleRoomDatabase: ExampleRoomDatabase)
                : ExampleCityDao = exampleRoomDatabase.exampleCityDao()

        @Provides
        @Singleton
        fun provideExampleCitiesRepository(exampleCitiesApiEndpointsInterface: ExampleCitiesApiEndpointsInterface,
                                           exampleCityDao: ExampleCityDao)
                : ExampleCitiesRepository = ExampleCitiesRepository(exampleCitiesApiEndpointsInterface, exampleCityDao)
    }

}
