package com.example.core.presentation.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.core.R
import com.example.core.databinding.ItemMesssageReceiveBinding
import com.example.core.databinding.ItemMesssageSendBinding
import com.example.core.domain.entity.Message
import com.example.core.domain.entity.MessageType

class ChatListAdapter(val avatarUrl: String = ""): ListAdapter<Message, ChatListAdapter.ChatListViewHolder>(ChatListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        return when (viewType) {
            MessageType.Send.ordinal -> MessageSendListViewHolder.from(parent)
            MessageType.Receive.ordinal -> MessageReceiveListViewHolder.from(parent)
            else -> MessageReceiveListViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        if (holder is MessageSendListViewHolder) {
            val item = getItem(position)
            holder.bind(item)
        } else {
            val item = getItem(position)
            (holder as MessageReceiveListViewHolder).bind(item, avatarUrl)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).type == MessageType.Send) MessageType.Send.ordinal else MessageType.Receive.ordinal
    }

    class MessageSendListViewHolder private constructor(private val binding: ItemMesssageSendBinding) :
        ChatListViewHolder(binding) {
        fun bind(item: Message) {
            binding.message = item.message
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MessageSendListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMesssageSendBinding.inflate(layoutInflater, parent, false)
                return MessageSendListViewHolder(binding)
            }
        }
    }

    class MessageReceiveListViewHolder private constructor(private val binding: ItemMesssageReceiveBinding) :
        ChatListViewHolder(binding) {
        fun bind(item: Message, avatarUrl: String) {
            binding.message = item.message
            binding.imageViewMainAvatar.apply {
                Glide
                    .with(this.context)
                    .load(avatarUrl)
                    .centerCrop()
                    .placeholder(R.drawable.avatar)
                    .into(this)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MessageReceiveListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMesssageReceiveBinding.inflate(layoutInflater, parent, false)
                return MessageReceiveListViewHolder(binding)
            }
        }
    }

    sealed class ChatListViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    class ChatListDiffUtil : DiffUtil.ItemCallback<Message>() {
        override fun areContentsTheSame(oldItem: Message, newItem: Message) = oldItem == newItem
        override fun areItemsTheSame(oldItem: Message, newItem: Message) =
            oldItem.message == newItem.message
    }

}