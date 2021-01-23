package com.jarvis.sprinkle.ui.base

import android.graphics.Color
import android.widget.EdgeEffect
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jarvis.sprinkle.R
import com.jarvis.sprinkle.util.forEachVisibleHolder

class BaseEdgeEffectFactory : RecyclerView.EdgeEffectFactory() {

    companion object {
        /** The magnitude of rotation while the list is scrolled. */
        private const val SCROLL_ROTATION_MAGNITUDE = 0.25f

        /** The magnitude of rotation while the list is over-scrolled. */
        private const val OVERSCROLL_ROTATION_MAGNITUDE = -10

        /** The magnitude of translation distance while the list is over-scrolled. */
        private const val OVERSCROLL_TRANSLATION_MAGNITUDE = 0.2f

        /** The magnitude of translation distance when the list reaches the edge on fling. */
        private const val FLING_TRANSLATION_MAGNITUDE = 0.3f

    }

    override fun createEdgeEffect(recyclerView: RecyclerView, direction: Int): EdgeEffect {

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                recyclerView.forEachVisibleHolder { holder: RecyclerView.ViewHolder ->
                    if (holder is BaseViewHolder)
                        holder.rotation
                            // Update the velocity.
                            // The velocity is calculated by the horizontal scroll offset.
                            .setStartVelocity(
                                holder.currentVelocity - dx * SCROLL_ROTATION_MAGNITUDE
                            )
                            // Start the animation. This does nothing if the animation is already running.
                            .start()
                }
            }
        })

        val edgeEffectFactory = object : EdgeEffect(recyclerView.context) {

            override fun onPull(deltaDistance: Float) {
                super.onPull(deltaDistance)
                handlePull(deltaDistance)
            }

            override fun onPull(deltaDistance: Float, displacement: Float) {
                super.onPull(deltaDistance, displacement)
                handlePull(deltaDistance)
            }

            private fun handlePull(deltaDistance: Float) {
                // This is called on every touch event while the list is scrolled with a finger.
                // We simply update the view properties without animation.
                val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                val rotationDelta = sign * deltaDistance * OVERSCROLL_ROTATION_MAGNITUDE
                val translationYDelta =
                    sign * recyclerView.width * deltaDistance * OVERSCROLL_TRANSLATION_MAGNITUDE
                recyclerView.forEachVisibleHolder { holder: BaseViewHolder ->
                    holder.rotation.cancel()
                    holder.translationY.cancel()
                    holder.itemView.rotation += 0
                    holder.itemView.translationY += translationYDelta
                }
            }

            override fun onRelease() {
                super.onRelease()
                // The finger is lifted. This is when we should start the animations to bring
                // the view property values back to their resting states.
                recyclerView.forEachVisibleHolder { holder: BaseViewHolder ->
                    holder.rotation.start()
                    holder.translationY.start()
                }
            }

            override fun onAbsorb(velocity: Int) {
                super.onAbsorb(velocity)
                val sign = if (direction == DIRECTION_BOTTOM) -1 else 1
                val translationVelocity = sign * velocity * FLING_TRANSLATION_MAGNITUDE
                recyclerView.forEachVisibleHolder { holder: BaseViewHolder ->
                    holder.translationY
                        .setStartVelocity(translationVelocity)
                        .start()
                }
            }
        }
        edgeEffectFactory.color = Color.TRANSPARENT
        return edgeEffectFactory
    }
}