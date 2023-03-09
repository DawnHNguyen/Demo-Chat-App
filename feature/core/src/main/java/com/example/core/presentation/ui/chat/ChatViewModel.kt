package com.example.core.presentation.ui.chat

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

    private val _listGalleryImageUIModel = MutableLiveData<List<GalleryImageUIModel>>(listOf())
    val listGalleryImageUIModel: LiveData<List<GalleryImageUIModel>> get() = _listGalleryImageUIModel

    private val _selectedGalleryImage = MutableLiveData<MutableList<String>>(mutableListOf())
    val selectedGalleryImage: LiveData<MutableList<String>> get() = _selectedGalleryImage

    fun sendMsg() {
        if (!chatMessage.value.isNullOrEmpty()) {
            val temp = listMessage.value?.toMutableList()
            temp?.add(Message(chatMessage.value!!).apply { setMsgType(MessageType.Send) })
            _listMessage.value = temp?.toList()

            viewModelScope.launch {
                delay(200)
                temp?.add(
                    Message(
                        "\"Xin chào bạn. Rất vui làm quen với bạn\""
                    ).apply { setMsgType(MessageType.Receive) }
                )
                _listMessage.value = temp?.toList()
            }
            chatMessage.value = ""
        }
    }

    fun sendImg(img: List<String>) {
        val temp = listMessage.value?.toMutableList()
        if (img.size > 1) temp?.add(ImageMessage(img).apply { setMsgType(MessageType.SendMultiImg) })
        else temp?.add(ImageMessage(img).apply { setMsgType(MessageType.SendImg) })
        _listMessage.value = temp?.toList()
    }

    fun initListGalleryImageUIModel(listImage: List<String>){
        val listUiModel = mutableListOf<GalleryImageUIModel>()
        listImage.forEach {
            listUiModel.add(GalleryImageUIModel(it, false))
        }
        _listGalleryImageUIModel.value = listUiModel.toList()
    }

    fun onClickGalleryImage(position: Int){
        if (listGalleryImageUIModel.value?.get(position)?.selected == true){
            _listGalleryImageUIModel.value?.get(position)?.selected = false
            _selectedGalleryImage.value?.remove(_listGalleryImageUIModel.value?.get(position)?.image ?: " ")
        } else {
            _listGalleryImageUIModel.value?.get(position)?.selected = true
            _selectedGalleryImage.value?.add(_listGalleryImageUIModel.value?.get(position)?.image ?: " ")
        }
    }
}