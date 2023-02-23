package com.example.core.presentation.ui.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.entity.Message
import com.example.core.domain.entity.MessageType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    val chatMessage = MutableLiveData<String>()

    private val _listMessage = MutableLiveData<List<Message>>(listOf())
    val listMessage: LiveData<List<Message>> get() = _listMessage

    fun sendMsg(){
        if(!chatMessage.value.isNullOrEmpty()) {
            val temp = listMessage.value?.toMutableList()
            temp?.add(Message(chatMessage.value!!, MessageType.Send))
            _listMessage.value = temp?.toList()

            viewModelScope.launch{
                delay(1000)
                temp?.add(
                    Message(
                        "\"Xin chào bạn. Rất vui làm quen với bạn\"",
                        MessageType.Receive
                    )
                )

                _listMessage.value = temp?.toList()
            }
            chatMessage.value = ""
        }
    }

}