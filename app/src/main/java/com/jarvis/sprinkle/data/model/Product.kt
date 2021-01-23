package com.jarvis.sprinkle.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Product(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("founder_name")
    val founder_name: String,
    @SerializedName("product_url")
    val product_url: String,
    @SerializedName("category")
    val category: Int,
    @SerializedName("upvotes")
    val upvotes: Int,
    val isBookmarked: Boolean,
    val isUpVoted: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (other is Product) {
            return (this.id == other.id &&
                    this.title == other.title &&
                    this.description == other.description &&
                    this.founder_name == other.founder_name &&
                    this.product_url == other.product_url &&
                    this.category == other.category &&
                    this.upvotes == other.upvotes)
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + founder_name.hashCode()
        result = 31 * result + product_url.hashCode()
        result = 31 * result + category
        result = 31 * result + upvotes
        return result
    }
}