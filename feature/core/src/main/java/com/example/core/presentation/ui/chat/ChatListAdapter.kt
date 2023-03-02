package com.example.core.presentation.ui.chat

import android.graphics.Canvas
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.common.presentation.utils.MarginItemDecoration
import com.example.core.R
import com.example.core.databinding.*
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
            MessageType.SendMultiImg.ordinal -> MultiImageMessageSendListViewHolder.from(parent)
            MessageType.ReceiveMultiImg.ordinal -> MultiImageMessageReceiveListViewHolder.from(
                parent
            )
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

            is MultiImageMessageSendListViewHolder -> {
                holder.bind(item as ImageMessage)
            }

            is MultiImageMessageReceiveListViewHolder -> {
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
            MessageType.ReceiveMultiImg -> MessageType.ReceiveMultiImg.ordinal
            MessageType.SendMultiImg -> MessageType.SendMultiImg.ordinal
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
                    .load(item.image[0])
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
                    .load(item.image[0])
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

    class MultiImageMessageSendListViewHolder private constructor(private val binding: ItemMultiImageMesssageSendBinding) :
        ChatListViewHolder(binding) {
        fun bind(item: ImageMessage) {
            binding.recyclerViewMultiImageMessageSend.apply {
                adapter = ChatImageListAdapter()
                if (item.image.size == 2) {
                    layoutManager = GridLayoutManager(this.context, 2)
                    addItemDecoration(
                        MarginItemDecoration(
                            2 * this.context.resources.displayMetrics.density.toInt(),
                            2
                        )
                    )
                } else {
                    layoutManager = GridLayoutManager(this.context, 3)
                    addItemDecoration(
                        MarginItemDecoration(
                            2 * this.context.resources.displayMetrics.density.toInt(),
                            3
                        )
                    )
                }
                (adapter as? ListAdapter<String, *>)?.submitList(item.image)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MultiImageMessageSendListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemMultiImageMesssageSendBinding.inflate(layoutInflater, parent, false)
                return MultiImageMessageSendListViewHolder(binding)
            }
        }
    }

    class MultiImageMessageReceiveListViewHolder private constructor(private val binding: ItemMultiImageMesssageReceiveBinding) :
        ChatListViewHolder(binding) {
        fun bind(item: ImageMessage, avatarUrl: String) {
            binding.recyclerViewMultiImageMessageReceive.apply {
                adapter = ChatImageListAdapter()
                if (item.image.size == 2) {
                    layoutManager = GridLayoutManager(this.context, 2)
                    addItemDecoration(
                        MarginItemDecoration(
                            2 * this.context.resources.displayMetrics.density.toInt(),
                            2
                        )
                    )
                } else {
                    layoutManager = GridLayoutManager(this.context, 3)
                    addItemDecoration(
                        MarginItemDecoration(
                            2 * this.context.resources.displayMetrics.density.toInt(),
                            3
                        )
                    )
                }
                (adapter as? ListAdapter<String, *>)?.submitList(item.image)

                val radius = 8 * this.context.resources.displayMetrics.density

                draw(Canvas().apply {
                    drawRoundRect(
                        0f,
                        0f,
                        this.width.toFloat(),
                        this.height.toFloat(),
                        radius,
                        radius,
                        Paint()
                    )
                })
            }

            binding.imageViewItemMultiImageMessageReceiveAvatar.apply {
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
            fun from(parent: ViewGroup): MultiImageMessageReceiveListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    ItemMultiImageMesssageReceiveBinding.inflate(layoutInflater, parent, false)
                return MultiImageMessageReceiveListViewHolder(binding)
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