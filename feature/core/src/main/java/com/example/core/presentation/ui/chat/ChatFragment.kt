package com.example.core.presentation.ui.chat

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.common.presentation.BaseFragment
import com.example.common.presentation.BaseFragmentWithViewModel
import com.example.common.utils.hideKeyboard
import com.example.core.BR
import com.example.core.R
import com.example.core.databinding.FragmentChatBinding

class ChatFragment : BaseFragmentWithViewModel<FragmentChatBinding, ChatViewModel>() {
    override fun getLayoutId(): Int = R.layout.fragment_chat

    override val viewModel: ChatViewModel by viewModels()

    override fun getViewModelBindingVariable(): Int = BR.viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chatParentView.setOnClickListener { hideKeyboard() }

        binding.imageButtonChatSend.setOnClickListener {
            viewModel.sendMsg()
            hideKeyboard()
        }

        binding.recyclerViewChat.apply {
            adapter = ChatListAdapter()
            layoutManager =LinearLayoutManager(requireContext())
            itemAnimator = null
        }
    }
}