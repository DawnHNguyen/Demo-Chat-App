package com.example.core.presentation.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.core.databinding.ItemImageMessageOverTwoBinding
import com.example.core.databinding.ItemImageMessageTwoBinding

class ChatImageListAdapter :
    ListAdapter<String, ChatImageListAdapter.ChatImageListViewHolder>(ChatImageListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatImageListViewHolder {
        return when (viewType) {
            MultiImageType.Two.ordinal -> TwoImageListViewHolder.from(parent)
            MultiImageType.OverTwo.ordinal -> OverTwoImageListViewHolder.from(parent)
            else -> OverTwoImageListViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: ChatImageListViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is TwoImageListViewHolder -> {
                holder.bind(item)
            }

            is OverTwoImageListViewHolder -> {
                holder.bind(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemCount == 2) MultiImageType.Two.ordinal else MultiImageType.OverTwo.ordinal
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

    class OverTwoImageListViewHolder private constructor(private val binding: ItemImageMessageOverTwoBinding) :
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
            fun from(parent: ViewGroup): OverTwoImageListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemImageMessageOverTwoBinding.inflate(layoutInflater, parent, false)
                return OverTwoImageListViewHolder(binding)
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

    enum class MultiImageType {
        Two, OverTwo
    }

}