package com.example.core.presentation.ui.chat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.common.presentation.BaseFragmentWithViewModel
import com.example.common.utils.hideKeyboard
import com.example.core.BR
import com.example.core.R
import com.example.core.databinding.FragmentChatBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class ChatFragment : BaseFragmentWithViewModel<FragmentChatBinding, ChatViewModel>() {
    override fun getLayoutId(): Int = R.layout.fragment_chat

    override val viewModel: ChatViewModel by viewModels()

    override fun getViewModelBindingVariable(): Int = BR.viewModel

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var openCameraForResult: ActivityResultLauncher<Intent>

    private lateinit var openGalleryForResult: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    checkPermission {

                    }
                }
            }

        openCameraForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                    Toast.makeText(
                        requireContext(),
                        "Photo has been taken and sent",
                        Toast.LENGTH_SHORT
                    ).show()
//                    viewModel.sendImg(it.data!!.data)
                }
            }

        openGalleryForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                    Toast.makeText(
                        requireContext(),
                        "Photo has been chosen and sent",
                        Toast.LENGTH_SHORT
                    ).show()
//                    val bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri)

                    viewModel.sendImg(it.data!!.data!!)
                }
            }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chatParentView.setOnClickListener { hideKeyboard() }

        binding.imageButtonChatSend.setOnClickListener {
            viewModel.sendMsg()
            hideKeyboard()
        }

        requireActivity().window.statusBarColor = Color.parseColor("#DB5BC1D0")

        activity?.findViewById<AppBarLayout>(R.id.topAppBar_mainActivity)?.visibility = View.GONE

        activity?.findViewById<BottomNavigationView>(R.id.bottomNav_activityMain_bottomNav)?.visibility =
            View.GONE

        binding.imageButtonChatNavBack.setOnClickListener {
            findNavController().popBackStack()
            changeTopAppBar()
        }

        binding.imageButtonChatTakePhoto.setOnClickListener {
            checkPermission { takePhoto() }
        }

        binding.imageButtonChatGallery.setOnClickListener {
            checkPermission { openGallery() }
        }

        binding.recyclerViewChat.apply {
            adapter = ChatListAdapter()
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }
            itemAnimator = null
        }
    }

    private fun changeTopAppBar() {
        binding.toolBarChatFragment.visibility = View.GONE

        activity?.findViewById<AppBarLayout>(R.id.topAppBar_mainActivity)?.visibility =
            View.VISIBLE

        activity?.findViewById<BottomNavigationView>(R.id.bottomNav_activityMain_bottomNav)?.visibility =
            View.VISIBLE

        requireActivity().window.statusBarColor = Color.TRANSPARENT
    }

    private fun takePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        openCameraForResult.launch(cameraIntent)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        openGalleryForResult.launch(galleryIntent)
    }

    private fun checkPermission(onPermissionGranted: () -> Unit) {
        when {
            isPermissionGranted() -> {
                onPermissionGranted()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> Toast.makeText(
                requireContext(),
                "This app need camera permission to take photo",
                Toast.LENGTH_SHORT
            ).show()

            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> Toast.makeText(
                requireContext(),
                "This app need access to phone storage to open gallery",
                Toast.LENGTH_SHORT
            ).show()

            else -> {
                requestPermission()
            }
        }
    }

    private fun requestPermission() {
        REQUIRED_PERMISSIONS.forEach { requestPermissionLauncher.launch(it) }
    }

    private fun isPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}