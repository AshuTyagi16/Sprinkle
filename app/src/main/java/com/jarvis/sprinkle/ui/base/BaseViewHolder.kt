package com.jarvis.sprinkle.ui.base

import android.view.View
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var currentVelocity = 0f

    val rotation: SpringAnimation = SpringAnimation(itemView, SpringAnimation.ROTATION)
            .setSpring(
                    SpringForce()
                            .setFinalPosition(0f)
                            .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)
                            .setStiffness(SpringForce.STIFFNESS_LOW)
                      )
            .addUpdateListener { _, _, velocity ->
                currentVelocity = velocity
            }

    val translationY: SpringAnimation = SpringAnimation(itemView, SpringAnimation.TRANSLATION_Y)
            .setSpring(
                    SpringForce()
                            .setFinalPosition(0f)
                            .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
                            .setStiffness(SpringForce.STIFFNESS_LOW)
                      )
}