package com.jarvis.sprinkle.ui.products

import androidx.appcompat.widget.AppCompatImageView
import com.jarvis.sprinkle.data.model.Product

interface OnProductClickListener {
    fun onShareClick(product: Product)
    fun onBookmarkClick(product: Product)
    fun onUpVoteClick(product: Product)
    fun onOpenImageClick(position: Int, product: Product, imageView: AppCompatImageView)
}