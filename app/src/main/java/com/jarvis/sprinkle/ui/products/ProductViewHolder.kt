package com.jarvis.sprinkle.ui.products

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jarvis.sprinkle.R
import com.jarvis.sprinkle.data.model.Product
import com.jarvis.sprinkle.ui.base.BaseViewHolder
import com.jarvis.sprinkle.util.dpToPx
import com.sackcentury.shinebuttonlib.ShineButton

class ProductViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private var onProductClickListener: OnProductClickListener? = null

    fun setProductInfo(product: Product) {
        val ivProduct = itemView.findViewById<AppCompatImageView>(R.id.ivProduct)
        Glide.with(itemView)
            .load(product.image)
            .placeholder(R.drawable.placeholder_small)
            .apply(
                RequestOptions().transform(
                    CenterCrop(),
                    GranularRoundedCorners(8.dpToPx.toFloat(), 8.dpToPx.toFloat(), 0f, 0f)
                )
            )
            .error(R.drawable.placeholder_small)
            .into(ivProduct)
        itemView.findViewById<AppCompatTextView>(R.id.tvTitle).text = product.title
        itemView.findViewById<AppCompatTextView>(R.id.tvDescription).text = product.description
        itemView.findViewById<AppCompatTextView>(R.id.tvNumUpVotes).text =
            product.upvotes.toString()

        val btnBookMark = itemView.findViewById<ShineButton>(R.id.btnBookMark)
        btnBookMark.isChecked = product.isBookmarked

        val btnUpVote = itemView.findViewById<ShineButton>(R.id.btnUpVote)
        btnUpVote.isChecked = product.isUpVoted

        itemView.findViewById<AppCompatImageView>(R.id.btnShare).setOnClickListener {
            onProductClickListener?.onShareClick(product)
        }

        btnUpVote.setOnClickListener {
            onProductClickListener?.onUpVoteClick(product)
        }

        btnBookMark.setOnClickListener {
            onProductClickListener?.onBookmarkClick(product)
        }

        ivProduct.setOnClickListener {
            onProductClickListener?.onOpenImageClick(bindingAdapterPosition, product, ivProduct)
        }
    }

    fun setProductClickListener(onProductClickListener: OnProductClickListener) {
        this.onProductClickListener = onProductClickListener
    }
}