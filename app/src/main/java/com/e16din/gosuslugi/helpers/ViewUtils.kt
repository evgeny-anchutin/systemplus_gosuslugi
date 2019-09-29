package com.e16din.gosuslugi.helpers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager


object ViewUtils {
    fun clearAdapterOnViewDetached(view: View) {
        view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                // do nothing
            }

            override fun onViewDetachedFromWindow(v: View) {
                // NOTE: что бы устранить утечку памяти
                // https://stackoverflow.com/questions/35520946/leak-canary-recyclerview-leaking-madapter
                if (v is RecyclerView) {
                    v.adapter = null
                } else if (v is ViewPager) {
                    v.adapter = null
                }
            }
        })
    }
}