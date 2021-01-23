package com.jarvis.sprinkle.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jarvis.sprinkle.R
import com.jarvis.sprinkle.data.model.Product
import com.jarvis.sprinkle.databinding.ActivityProductDetailBinding
import com.jarvis.sprinkle.util.*
import com.stfalcon.imageviewer.StfalconImageViewer
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductDetailActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var daggerViewModelFactory: DaggerViewModelFactory

    private val binding by lazy {
        ActivityProductDetailBinding.inflate(layoutInflater)
    }

    private val productViewModel by viewModels<ProductDetailViewModel> {
        daggerViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUI()
        getIntentData()
        subscribeUI()
    }

    private fun setupUI() {
        binding.clData.hide()
        binding.placeholderNoResult.hide()
        binding.progressBar.show()
    }

    private fun getIntentData() {
        intent?.data?.getQueryParameter("id")?.let {
            productViewModel.getProductsDetail(it.toInt())
        }
    }

    private fun subscribeUI() {
        productViewModel.productsDetailLiveData.observe(this, {
            it?.let {
                binding.placeholderNoResult.hide()
                binding.progressBar.hide()
                binding.clData.show()
                setProductData(it)
            } ?: run {
                binding.progressBar.hide()
                binding.clData.hide()
                binding.placeholderNoResult.show()
            }
        })
    }

    private fun setProductData(product: Product) {
        Glide.with(this)
            .load(product.image)
            .placeholder(R.drawable.placeholder_small)
            .apply(
                RequestOptions().transform(
                    CenterCrop(),
                    GranularRoundedCorners(8.dpToPx.toFloat(), 8.dpToPx.toFloat(), 0f, 0f)
                )
            )
            .error(R.drawable.placeholder_small)
            .into(binding.ivProduct)

        binding.tvTitle.text = product.title
        binding.tvDescription.text = product.description
        binding.tvNumUpVotes.text = product.upvotes.toString()

        binding.btnBookMark.isChecked = product.isBookmarked

        binding.btnUpVote.isChecked = product.isUpVoted

        binding.btnShare.setOnClickListener {
            GlobalScope.launch {
                val image = withContext(Dispatchers.IO) {
                    Glide.with(this@ProductDetailActivity)
                        .asBitmap()
                        .load(product.image)
                        .submit()
                        .get()
                }
                val uri = withContext(Dispatchers.IO) {
                    Utils.getUriFromImage(this@ProductDetailActivity, image)
                }
                shareProduct(uri, product.id)
            }
        }

        binding.ivProduct.setOnClickListener {
            openImage(product.image, binding.ivProduct)
        }

        binding.btnUpVote.setOnClickListener {
            productViewModel.updateVoteState(product.id)
        }

        binding.btnBookMark.setOnClickListener {
            productViewModel.toggleBookmarkState(product.id)
        }
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

    private fun openImage(url: String, imageView: AppCompatImageView) {
        StfalconImageViewer.Builder(this, listOf(url)) { view, image ->
            Glide.with(this)
                .load(image)
                .error(R.drawable.placeholder_small)
                .placeholder(R.drawable.placeholder_small)
                .into(view)
        }.withStartPosition(0)
            .withBackgroundColorResource(android.R.color.black)
            .withHiddenStatusBar(false)
            .allowZooming(true)
            .allowSwipeToDismiss(true)
            .withTransitionFrom(imageView)
            .show()
    }
}