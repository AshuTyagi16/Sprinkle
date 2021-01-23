package com.jarvis.sprinkle.ui

import androidx.lifecycle.*
import com.jarvis.sprinkle.data.db.repository.LocalDataSource
import com.jarvis.sprinkle.data.model.Product
import com.jarvis.sprinkle.data.model.Result
import com.jarvis.sprinkle.data.network.repository.RemoteDataSource
import com.jarvis.sprinkle.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) :
    ViewModel() {

    private val _networkLiveData = MutableLiveData<Result<List<Product>>>()
    val networkLiveData: LiveData<Result<List<Product>>>
        get() = _networkLiveData

    init {
        reload()
    }

    private val categoryLiveData = MutableLiveData<Int>()

    val filteredProductsLiveData: LiveData<List<Product>> =
        Transformations.switchMap(categoryLiveData) {
            when (it) {
                Constants.CATEGORY_ALL -> localDataSource.getAllProducts()
                Constants.CATEGORY_BOOKMARKS -> localDataSource.getBookmarkedProducts()
                else -> localDataSource.getProductsForCategory(it)
            }
        }

    fun filterForCategory(category: Int) {
        categoryLiveData.value = category
    }

    fun reload() {
        viewModelScope.launch {
            _networkLiveData.postValue(Result.loading())
            val data = withContext(Dispatchers.IO) { localDataSource.getAllProductsSync() }
            _networkLiveData.postValue(Result.success(data))
            val responseStatus = remoteDataSource.getProducts()
            if (responseStatus.status == Result.Status.SUCCESS && responseStatus.data != null) {
                _networkLiveData.postValue(Result.success(responseStatus.data))
                localDataSource.insertAll(responseStatus.data)
            } else {
                _networkLiveData.postValue(Result.error(responseStatus.message!!))
                _networkLiveData.postValue(Result.success(data))
            }
        }
    }

    fun toggleBookmarkState(id: Int) {
        viewModelScope.launch {
            localDataSource.toggleBookmarkState(id)
        }
    }

    fun updateVoteState(id: Int) {
        viewModelScope.launch {
            val isAlreadyUpVoted =
                withContext(Dispatchers.IO) { localDataSource.isProductUpVoted(id) }
            if (isAlreadyUpVoted) {
                localDataSource.downVoteProduct(id)
            } else {
                localDataSource.upVoteProduct(id)
            }
        }
    }
}