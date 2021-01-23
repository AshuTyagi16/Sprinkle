package com.jarvis.sprinkle.di.module.activity

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.sprinkle.di.mapkey.ViewModelKey
import com.jarvis.sprinkle.di.scope.PerActivityScope
import com.jarvis.sprinkle.ui.MainActivity
import com.jarvis.sprinkle.ui.MainActivityViewModel
import com.jarvis.sprinkle.ui.base.BaseEdgeEffectFactory
import com.jarvis.sprinkle.ui.base.SpaceItemDecoration
import com.jarvis.sprinkle.ui.products.ProductAdapter
import com.jarvis.sprinkle.util.Constants
import com.jarvis.sprinkle.util.dpToPx
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class MainActivityModule {

    companion object {

        @Provides
        @PerActivityScope
        fun gridLayoutManager(mainActivity: MainActivity): GridLayoutManager {
            return GridLayoutManager(
                mainActivity,
                Constants.SPAN_COUNT,
                RecyclerView.VERTICAL,
                false
            )
        }

        @Provides
        @PerActivityScope
        fun adapter(): ProductAdapter {
            return ProductAdapter()
        }

        @Provides
        @PerActivityScope
        fun spaceItemDecoration(): SpaceItemDecoration {
            return SpaceItemDecoration(10.dpToPx, 10.dpToPx)
        }

        @Provides
        @PerActivityScope
        fun edgeEffectFactory(): BaseEdgeEffectFactory {
            return BaseEdgeEffectFactory()
        }
    }

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

}