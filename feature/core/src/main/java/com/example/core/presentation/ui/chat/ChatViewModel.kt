package com.example.core.presentation.ui.chat

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.entity.BaseMessage
import com.example.core.domain.entity.ImageMessage
import com.example.core.domain.entity.Message
import com.example.core.domain.entity.MessageType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val chatMessage = MutableLiveData<String>()

    private val _listMessage = MutableLiveData<List<BaseMessage>>(listOf())
    val listMessage: LiveData<List<BaseMessage>> get() = _listMessage

    fun sendMsg() {
        if (!chatMessage.value.isNullOrEmpty()) {
            val temp = listMessage.value?.toMutableList()
            temp?.add(Message(chatMessage.value!!).apply { getMsgType(MessageType.Send) })
            _listMessage.value = temp?.toList()

            viewModelScope.launch {
                delay(1000)
                temp?.add(
                    Message(
                        "\"Xin chào bạn. Rất vui làm quen với bạn\""
                    ).apply { getMsgType(MessageType.Receive) }
                )
                _listMessage.value = temp?.toList()
            }
            chatMessage.value = ""
        }
    }

    fun sendImg(img: String) {
        val temp = listMessage.value?.toMutableList()
        temp?.add(ImageMessage(img).apply { getMsgType(MessageType.SendImg) })
        _listMessage.value = temp?.toList()
    }

}