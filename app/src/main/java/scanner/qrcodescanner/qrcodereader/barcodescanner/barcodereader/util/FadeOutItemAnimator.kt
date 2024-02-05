package scanner.qrcodescanner.qrcodereader.barcodescanner.barcodereader.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class FadeOutItemAnimator: DefaultItemAnimator() {
    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        holder?.let {
            val view = it.itemView
            view.animate()
                .alpha(0f)
                .setDuration(300) // Customize the animation duration as needed
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        dispatchRemoveFinished(holder)
                        view.alpha = 1f
                    }
                })
                .start()
        }

        return super.animateRemove(holder)
    }
}