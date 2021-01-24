package com.jarvis.sprinkle.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.forEachIndexed
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.jarvis.sprinkle.R
import com.jarvis.sprinkle.data.model.Product
import com.jarvis.sprinkle.data.model.Result
import com.jarvis.sprinkle.databinding.ActivityMainBinding
import com.jarvis.sprinkle.receiver.NetworkStateReceiver
import com.jarvis.sprinkle.ui.base.BaseEdgeEffectFactory
import com.jarvis.sprinkle.ui.base.SpaceItemDecoration
import com.jarvis.sprinkle.ui.products.OnProductClickListener
import com.jarvis.sprinkle.ui.products.ProductAdapter
import com.jarvis.sprinkle.util.*
import com.stfalcon.imageviewer.StfalconImageViewer
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity(), OnProductClickListener {

    @Inject
    lateinit var daggerViewModelFactory: DaggerViewModelFactory

    @Inject
    lateinit var productAdapter: ProductAdapter

    @Inject
    lateinit var baseEdgeEffectFactory: BaseEdgeEffectFactory

    @Inject
    lateinit var gridLayoutManager: GridLayoutManager

    @Inject
    lateinit var spaceItemDecoration: SpaceItemDecoration

    @Inject
    lateinit var sharedPreferenceUtil: SharedPreferenceUtil

    @Inject
    lateinit var networkUtil: NetworkUtil

    private var toast: Toast? = null

    private var images: List<String>? = null

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val mainActivityViewModel by viewModels<MainActivityViewModel> {
        daggerViewModelFactory
    }

    private val networkStateReceiver by lazy {
        object : NetworkStateReceiver(networkUtil) {
            override fun onConnectedToInternet() {
                mainActivityViewModel.reload()
            }
        }
    }

    private var currentSpanCount = Constants.SPAN_COUNT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUI()
        setupListeners()
        subscribeUI()
        syncLastState()
        registerReceiver(networkStateReceiver, NetworkStateReceiver.getIntentFilter())
    }

    private fun setupUI() {
        binding.rvProducts.layoutManager = gridLayoutManager
        binding.rvProducts.adapter = productAdapter
        productAdapter.setProductClickListener(this)
        binding.rvProducts.edgeEffectFactory = baseEdgeEffectFactory
        binding.rvProducts.addItemDecoration(spaceItemDecoration)
    }

    private fun setupListeners() {
        var position = 0
        binding.chips.setOnCheckedChangeListener { group, checkedId ->
            binding.chips.forEachIndexed { index, view ->
                if (view is Chip && view.id == checkedId)
                    position = index
                return@forEachIndexed
            }
            sharedPreferenceUtil.putInt(Constants.SELECTED_CATEGORY, checkedId)
            when (checkedId) {
                R.id.category_all -> mainActivityViewModel.filterForCategory(Constants.CATEGORY_ALL)
                R.id.category_bookmark -> mainActivityViewModel.filterForCategory(Constants.CATEGORY_BOOKMARKS)
                R.id.category_ed_tech -> mainActivityViewModel.filterForCategory(Constants.CATEGORY_ED_TECH)
                R.id.category_e_commerce -> mainActivityViewModel.filterForCategory(Constants.CATEGORY_E_COMMERCE)
                R.id.category_machine_learning -> mainActivityViewModel.filterForCategory(Constants.CATEGORY_MACHINE_LEARNING)
                R.id.category_medical_tech -> mainActivityViewModel.filterForCategory(Constants.CATEGORY_MEDICAL_TECH)
                else -> mainActivityViewModel.filterForCategory(Constants.CATEGORY_ALL)
            }
            binding.fab.backgroundTintList =
                ColorStateList.valueOf(ColorChipPalette.getColorFor(position, this))
        }

        binding.fab.setOnClickListener {
            if (productAdapter.currentList.isNullOrEmpty())
                if (networkUtil.isNetworkAvailable())
                    mainActivityViewModel.reload()
                else
                    showToast(getString(R.string.internet_not_connected))
            else
                updateSpanCount()
        }
    }

    private fun subscribeUI() {
        mainActivityViewModel.networkLiveData.observe(this, {
            when (it.status) {
                Result.Status.LOADING -> {
                    if (productAdapter.currentList.isNullOrEmpty()) {
                        binding.placeholderNoInternet.hide()
                        binding.clData.hide()
                        binding.placeholderNoResult.hide()
                        binding.progressBar.show()
                    }
                }
                Result.Status.SUCCESS -> {
                    binding.placeholderNoInternet.hide()
                    binding.clData.show()
                    binding.progressBar.hide()
                    if (!it.data.isNullOrEmpty()) {
                        binding.placeholderNoResult.hide()
                        binding.rvProducts.show()
                        val fabRes =
                            if (gridLayoutManager.spanCount == 1) R.drawable.ic_list else R.drawable.ic_grid
                        binding.fab.setImageResource(fabRes)
                        images = it.data.map { it.image }
                    } else {
                        binding.rvProducts.hide()
                    }
                }
                Result.Status.ERROR -> {
                    binding.clData.hide()
                    binding.progressBar.hide()
                    binding.placeholderNoResult.hide()
                    binding.placeholderNoInternet.show()
                    binding.fab.setImageResource(R.drawable.ic_refresh)
                    showToast(it.message)
                }
            }
        })

        mainActivityViewModel.filteredProductsLiveData.observe(this, {
            if (!it.isNullOrEmpty()) {
                images = it.map { it.image }
                binding.placeholderNoResult.hide()
                binding.rvProducts.show()
            } else {
                binding.rvProducts.hide()
                binding.placeholderNoResult.show()
            }
            productAdapter.submitList(it)
        })
    }

    private fun syncLastState() {
        mainActivityViewModel.filterForCategory(Constants.CATEGORY_ALL)
        val selectedCategory = sharedPreferenceUtil.getInt(Constants.SELECTED_CATEGORY, -1)
        if (selectedCategory != -1) {
            binding.chips.postDelayed({
                binding.chips.check(selectedCategory)
            }, 1000)
        }
    }

    private fun updateSpanCount() {
        if (currentSpanCount == 2) {
            currentSpanCount = 1
            binding.fab.setImageResource(R.drawable.ic_list)
        } else {
            currentSpanCount = 2
            binding.fab.setImageResource(R.drawable.ic_grid)
        }
        binding.rvProducts.post {
            TransitionManager.beginDelayedTransition(binding.rvProducts)
            gridLayoutManager.spanCount = currentSpanCount
        }
    }

    private fun showToast(message: String?) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private fun openImage(position: Int, imageView: AppCompatImageView) {
        if (!images.isNullOrEmpty()) {
            StfalconImageViewer.Builder(this, images) { view, image ->
                Glide.with(this)
                    .load(image)
                    .error(R.drawable.placeholder_small)
                    .placeholder(R.drawable.placeholder_small)
                    .into(view)
            }.withStartPosition(position)
                .withBackgroundColorResource(android.R.color.black)
                .withHiddenStatusBar(false)
                .allowZooming(true)
                .allowSwipeToDismiss(true)
                .withTransitionFrom(imageView)
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkStateReceiver)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override fun onShareClick(product: Product) {
        GlobalScope.launch {
            val image = withContext(Dispatchers.IO) {
                Glide.with(this@MainActivity).asBitmap().load(product.image).submit().get()
            }
            val uri = withContext(Dispatchers.IO) {
                Utils.getUriFromImage(this@MainActivity, image)
            }
            shareProduct(uri, product.id)
        }
    }

    override fun onBookmarkClick(product: Product) {
        mainActivityViewModel.toggleBookmarkState(product.id)
    }

    override fun onUpVoteClick(product: Product) {
        mainActivityViewModel.updateVoteState(product.id)
    }

    override fun onOpenImageClick(position: Int, product: Product, imageView: AppCompatImageView) {
        openImage(position, imageView)
    }

    private fun shareProduct(uri: Uri, id: Int) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link, id))
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.type = "image/*"
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}