package com.hussienfahmy.geologyofegyptformations.ext

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("list")
fun <T> RecyclerView.bindData(
    list: List<T>?
) {
    adapter.doIfTypeIs<ListAdapter<T, *>> {
        submitList(list)
    }
}

@BindingAdapter("hideIfLoaded")
fun View.bindHideIfLoaded(list: List<Any>?) {
    // null means that the data is not loaded yet
    visibility = if (list == null) View.VISIBLE else View.GONE
}
