package com.example.core.domain.entity

data class Message(
    val message: String,
    val type: MessageType,
)

enum class MessageType {
    Receive, Send
}
