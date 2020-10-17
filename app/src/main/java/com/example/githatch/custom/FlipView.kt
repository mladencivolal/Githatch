package com.example.githatch.custom

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.example.githatch.R

class FlipView(context: Context?, attrs: AttributeSet?) : FrameLayout(
    context!!, attrs
) {

    init {
        loadAnimation()
    }

    private var isFlipped = false
    private var frontView: View? = null
    private var backView: View? = null
    private var enterFlipAnim: Animator? = null
    private var exitFlipAnim: Animator? = null

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (childCount != 2) throw RuntimeException("FlipView should contain two children.")
        backView = getChildAt(0)
        frontView = getChildAt(1)
        setActiveView()
        changeCameraDistance()
    }

    private fun changeCameraDistance() {
        val distance = 8000
        val scale = resources.displayMetrics.density * distance
        frontView!!.cameraDistance = scale
        backView!!.cameraDistance = scale
    }

    private fun loadAnimation() {
        enterFlipAnim = AnimatorInflater.loadAnimator(context, R.animator.flip_enter_animator)
        exitFlipAnim = AnimatorInflater.loadAnimator(context, R.animator.flip_exit_animator)
    }

    fun flip() {
        if (enterFlipAnim!!.isRunning || exitFlipAnim!!.isRunning) {
            //Ignore when animation is running
            return
        }
        if (isFlipped) {
            isFlipped = false
            enterFlipAnim!!.setTarget(frontView)
            exitFlipAnim!!.setTarget(backView)
            enterFlipAnim!!.start()
            exitFlipAnim!!.start()
        } else {
            isFlipped = true
            enterFlipAnim!!.setTarget(backView)
            exitFlipAnim!!.setTarget(frontView)
            enterFlipAnim!!.start()
            exitFlipAnim!!.start()
        }
    }

    fun isFlipped(): Boolean {
        return isFlipped
    }

    fun setFlipped(flipped: Boolean) {
        isFlipped = flipped
        setActiveView()
    }

    private fun setActiveView() {
        if (frontView == null || backView == null) return
        frontView!!.rotationY = 0f
        backView!!.rotationY = 0f

        frontView!!.alpha = if (isFlipped) 0f else 1f
        backView!!.alpha = if (isFlipped) 1f else 0f
    }

}
