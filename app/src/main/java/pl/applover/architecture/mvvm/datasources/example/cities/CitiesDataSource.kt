package pl.applover.architecture.mvvm.datasources.example.cities

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import pl.applover.architecture.mvvm.data.example.internet.apiendpoints.ExampleCitiesApiEndpointsInterface
import pl.applover.architecture.mvvm.models.example.ExampleCityModel
import pl.applover.architecture.mvvm.util.architecture.datasource.ItemKeyedDataSourceWithState
import pl.applover.architecture.mvvm.util.architecture.network.NetworkState
import pl.applover.architecture.mvvm.util.architecture.retrofit.mapResponseList
import retrofit2.Response
import timber.log.Timber

/**
 * Created by Janusz Hain on 2018-06-18.
 */
class CitiesDataSource(private val apiCities: ExampleCitiesApiEndpointsInterface, private val compositeDisposable: CompositeDisposable) : ItemKeyedDataSourceWithState<String, ExampleCityModel>(compositeDisposable) {


    /**
     * Loading for first time for paged list
     */
    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<ExampleCityModel>) {
        networkStateSubject.onNext(NetworkState.LOADING)
        initialStateSubject.onNext(NetworkState.LOADING)
        compositeDisposable.add(
                initialPagedCitiesFromNetwork().subscribe({ response: Response<List<ExampleCityModel>> ->
                    response.body()?.let {
                        setRetry(null)
                        callback.onResult(it)
                        networkStateSubject.onNext(NetworkState.LOADED)
                        initialStateSubject.onNext(NetworkState.LOADED)
                    } ?: let {
                        setRetry(Action { loadInitial(params, callback) })
                        networkStateSubject.onNext(NetworkState.error(response.code(), response.errorBody()))
                        initialStateSubject.onNext(NetworkState.error(response.code(), response.errorBody()))
                    }
                }, { throwable: Throwable ->
                    Timber.e(throwable.message)
                    setRetry(Action { loadInitial(params, callback) })
                    networkStateSubject.onNext(NetworkState.throwable(throwable))
                    initialStateSubject.onNext(NetworkState.throwable(throwable))
                })
        )
    }

    private fun initialPagedCitiesFromNetwork() = apiCities.getPagedCitiesList("null").mapResponseList({ ExampleCityModel(it) })

    /**
     * Loading next pages after the first load
     */
    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<ExampleCityModel>) {
        networkStateSubject.onNext(NetworkState.LOADING)
        compositeDisposable.add(
                afterPagedCitiesFromNetwork(params.key).subscribe({ response: Response<List<ExampleCityModel>> ->
                    response.body()?.let {
                        setRetry(null)
                        callback.onResult(it)
                        networkStateSubject.onNext(NetworkState.LOADED)
                    } ?: let {
                        setRetry(Action { loadAfter(params, callback) })
                        networkStateSubject.onNext(NetworkState.error(response.code(), response.errorBody()))
                    }
                }, { throwable: Throwable ->
                    Timber.e(throwable.message)
                    setRetry(Action { loadAfter(params, callback) })
                    networkStateSubject.onNext(NetworkState.throwable(throwable))
                })
        )
    }

    private fun afterPagedCitiesFromNetwork(lastId: String) = apiCities.getPagedCitiesList(lastId).mapResponseList({ ExampleCityModel(it) })

    /**
     * Loading previous pages after the first load
     */
    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<ExampleCityModel>) {
        //in this example we will do paging list only for "loadAfter"
    }

    override fun getKey(item: ExampleCityModel): String = item.id

}