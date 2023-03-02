package com.example.core.presentation.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.core.R
import com.example.core.databinding.ItemImageMessageMoreThanTwoBinding
import com.example.core.databinding.ItemImageMessageTwoBinding
import com.example.core.databinding.ItemImageMesssageReceiveBinding
import com.example.core.databinding.ItemImageMesssageSendBinding
import com.example.core.domain.entity.ImageMessage
import com.example.core.domain.entity.Message
import com.example.core.domain.entity.MessageType

class ChatImageListAdapter :
    ListAdapter<String, ChatImageListAdapter.ChatImageListViewHolder>(ChatImageListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatImageListViewHolder {
        return when (viewType) {
            2 -> TwoImageListViewHolder.from(parent)
            else -> MoreThanTwoImageListViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: ChatImageListViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is TwoImageListViewHolder -> {
                holder.bind(item)
            }

            is MoreThanTwoImageListViewHolder -> {
                holder.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return itemCount
    }

    class TwoImageListViewHolder private constructor(private val binding: ItemImageMessageTwoBinding) :
        ChatImageListViewHolder(binding) {
        fun bind(item: String) {
            binding.imageItemImageMessageTwoImage.apply {
                Glide
                    .with(this.context)
                    .load(item)
                    .into(this)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): TwoImageListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemImageMessageTwoBinding.inflate(layoutInflater, parent, false)
                return TwoImageListViewHolder(binding)
            }
        }
    }

    class MoreThanTwoImageListViewHolder private constructor(private val binding: ItemImageMessageMoreThanTwoBinding) :
        ChatImageListViewHolder(binding) {
        fun bind(item: String) {
            binding.imageItemImageMessageMoreThanTwoImage.apply {
                Glide
                    .with(this.context)
                    .load(item)
                    .into(this)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MoreThanTwoImageListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemImageMessageMoreThanTwoBinding.inflate(layoutInflater, parent, false)
                return MoreThanTwoImageListViewHolder(binding)
            }
        }
    }

    sealed class ChatImageListViewHolder(binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ChatImageListDiffUtil : DiffUtil.ItemCallback<String>() {
        override fun areContentsTheSame(oldItem: String, newItem: String) =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: String, newItem: String) =
            oldItem.hashCode() == newItem.hashCode()
    }

}