package com.example.core.presentation.ui.chat

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.core.domain.entity.Message

@BindingAdapter("app:messageChat")
fun messageChat(recyclerView: RecyclerView?, listMessage: List<Message>?) {
    if (recyclerView?.adapter != null && listMessage != null) {
        (recyclerView.adapter as? ListAdapter<Message, *>)?.submitList(listMessage)
    }
}