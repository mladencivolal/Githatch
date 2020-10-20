package com.example.githatch.custom

import android.animation.Animator
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.githatch.R
import com.example.githatch.helpers.pixelDensity
import com.example.githatch.helpers.visible
import kotlin.math.hypot


object RevealAnimator {
    var flag = true

    fun animate(
        btnInitiator: ImageView,
        firstFrame: View,
        secondFrame: View,
        secondFrameContent: View,
        context: Context
    ) {
        val alphaAnimation = AnimationUtils.loadAnimation(context, R.anim.alpha_anim)

        var x: Int = firstFrame.right
        val y: Int = firstFrame.bottom

        val density = context.pixelDensity
        x -= (28 * density + 16 * density).toInt()
        val hypotenuse =
            hypot(firstFrame.width.toDouble(), firstFrame.height.toDouble()).toInt()
        if (flag) {
            btnInitiator.setImageResource(R.drawable.ic_cancel)
            val parameters = secondFrame.layoutParams as FrameLayout.LayoutParams
            parameters.height = firstFrame.height
            secondFrame.layoutParams = parameters
            val anim =
                ViewAnimationUtils.createCircularReveal(secondFrame, x, y, 0f, hypotenuse.toFloat())
            anim.duration = 700
            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    secondFrameContent.visible(true)
                    secondFrameContent.startAnimation(alphaAnimation)
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
            secondFrame.visible(true)
            anim.start()
            flag = false
        } else {
            btnInitiator.setImageResource(R.drawable.ic_info)
            val anim =
                ViewAnimationUtils.createCircularReveal(
                    secondFrame,
                    x,
                    y,
                    hypotenuse.toFloat(),
                    0f
                )
            anim.duration = 400
            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    secondFrame.visible(false)
                    secondFrameContent.visible(false)
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
            anim.start()
            flag = true
        }
    }
}