package com.jarvis.sprinkle.ui.detail

import androidx.lifecycle.*
import com.jarvis.sprinkle.data.db.repository.LocalDataSource
import com.jarvis.sprinkle.data.model.Product
import com.jarvis.sprinkle.data.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductDetailViewModel @Inject constructor(private val localDataSource: LocalDataSource) :
    ViewModel() {

    private val productIdLiveData = MutableLiveData<Int>()

    val productsDetailLiveData: LiveData<Product?> = Transformations.switchMap(productIdLiveData) {
        localDataSource.getProductForId(it)
    }

    fun getProductsDetail(id: Int) {
        productIdLiveData.value = id
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