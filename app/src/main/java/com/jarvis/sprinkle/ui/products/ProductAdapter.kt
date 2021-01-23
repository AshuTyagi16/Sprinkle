package com.jarvis.sprinkle.ui.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.jarvis.sprinkle.R
import com.jarvis.sprinkle.data.model.Product

class ProductAdapter : ListAdapter<Product, ProductViewHolder>(productDiffItemCallback),
    OnProductClickListener {

    private var onProductClickListener: OnProductClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_product, parent, false)
        return ProductViewHolder(view).apply {
            setProductClickListener(this@ProductAdapter)
        }
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position)?.let {
            holder.setProductInfo(it)
        }
    }

    fun setProductClickListener(onProductClickListener: OnProductClickListener) {
        this.onProductClickListener = onProductClickListener
    }

    override fun onShareClick(product: Product) {
        onProductClickListener?.onShareClick(product)
    }

    override fun onBookmarkClick(product: Product) {
        onProductClickListener?.onBookmarkClick(product)
    }

    override fun onUpVoteClick(product: Product) {
        onProductClickListener?.onUpVoteClick(product)
    }

    override fun onOpenImageClick(position: Int, product: Product, imageView: AppCompatImageView) {
        onProductClickListener?.onOpenImageClick(position, product, imageView)
    }
}

private val productDiffItemCallback = object : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

}