package com.example.core.domain.entity

sealed class BaseMessage(val msgType: MessageType)
data class Message(
    val message: String,
    val type: MessageType
): BaseMessage(type)

data class ImageMessage(
    val img: String,
    val type: MessageType
): BaseMessage(type)

enum class MessageType {
    Receive, Send, ReceiveImg, SendImg
}
