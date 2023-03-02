package com.example.core.presentation.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.core.R
import com.example.core.databinding.ItemImageMesssageReceiveBinding
import com.example.core.databinding.ItemImageMesssageSendBinding
import com.example.core.databinding.ItemMesssageReceiveBinding
import com.example.core.databinding.ItemMesssageSendBinding
import com.example.core.domain.entity.BaseMessage
import com.example.core.domain.entity.ImageMessage
import com.example.core.domain.entity.Message
import com.example.core.domain.entity.MessageType

class ChatListAdapter(val avatarUrl: String = "") :
    ListAdapter<BaseMessage, ChatListAdapter.ChatListViewHolder>(ChatListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        return when (viewType) {
            MessageType.Send.ordinal -> MessageSendListViewHolder.from(parent)
            MessageType.Receive.ordinal -> MessageReceiveListViewHolder.from(parent)
            MessageType.SendImg.ordinal -> ImageMessageSendListViewHolder.from(parent)
            MessageType.ReceiveImg.ordinal -> ImageMessageReceiveListViewHolder.from(parent)
            else -> MessageReceiveListViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is MessageSendListViewHolder -> {
                holder.bind(item as Message)
            }

            is MessageReceiveListViewHolder -> {
                holder.bind(item as Message, avatarUrl)
            }

            is ImageMessageSendListViewHolder -> {
                holder.bind(item as ImageMessage)
            }

            is ImageMessageReceiveListViewHolder -> {
                holder.bind(item as ImageMessage, avatarUrl)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).msgType) {
            MessageType.Send -> MessageType.Send.ordinal
            MessageType.Receive -> MessageType.Receive.ordinal
            MessageType.SendImg -> MessageType.SendImg.ordinal
            MessageType.ReceiveImg -> MessageType.ReceiveImg.ordinal
        }
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

            binding.imageViewItemMessageReceiveAvatar.apply {
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

    class ImageMessageSendListViewHolder private constructor(private val binding: ItemImageMesssageSendBinding) :
        ChatListViewHolder(binding) {
        fun bind(item: ImageMessage) {
            binding.imageViewItemImageMessageSendImage.apply {
                Glide
                    .with(this.context)
                    .load(item.image)
                    .into(this)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ImageMessageSendListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemImageMesssageSendBinding.inflate(layoutInflater, parent, false)
                return ImageMessageSendListViewHolder(binding)
            }
        }
    }

    class ImageMessageReceiveListViewHolder private constructor(private val binding: ItemImageMesssageReceiveBinding) :
        ChatListViewHolder(binding) {
        fun bind(item: ImageMessage, avatarUrl: String) {
            binding.imageViewItemImageMessageReceiveImage.apply {
                Glide
                    .with(this.context)
                    .load(item.image)
                    .centerCrop()
                    .into(this)
            }


            binding.imageViewItemImageMessageReceiveAvatar.apply {
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
            fun from(parent: ViewGroup): ImageMessageReceiveListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemImageMesssageReceiveBinding.inflate(layoutInflater, parent, false)
                return ImageMessageReceiveListViewHolder(binding)
            }
        }
    }

    sealed class ChatListViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    class ChatListDiffUtil : DiffUtil.ItemCallback<BaseMessage>() {
        override fun areContentsTheSame(oldItem: BaseMessage, newItem: BaseMessage) =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: BaseMessage, newItem: BaseMessage) =
            oldItem.hashCode() == newItem.hashCode()
    }

}