package com.example.core.presentation.ui.chat

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.common.presentation.BaseFragmentWithViewModel
import com.example.common.presentation.utils.MarginItemDecorationGridLayout
import com.example.common.utils.dpToPx
import com.example.common.utils.hideKeyboard
import com.example.core.BR
import com.example.core.R
import com.example.core.databinding.FragmentChatBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class ChatFragment : BaseFragmentWithViewModel<FragmentChatBinding, ChatViewModel>() {
    override fun getLayoutId(): Int = R.layout.fragment_chat

    override val viewModel: ChatViewModel by viewModels()

    override fun getViewModelBindingVariable(): Int = BR.viewModel

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var openCameraForResult: ActivityResultLauncher<Intent>

    private lateinit var openGalleryForResult: ActivityResultLauncher<Intent>

    private lateinit var bottomSheetGalleryBehavior: BottomSheetBehavior<ConstraintLayout>

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
                    val img = it.data?.extras?.get("data") as Bitmap
                    val imgUri = getImageUri(img).toString()

                    viewModel.sendImg(listOf(imgUri))
                }
            }

        openGalleryForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                    if (it.data!!.clipData != null) {
                        val listImage = mutableListOf<String>()
                        val clipData = it.data!!.clipData!!
                        val count = clipData.itemCount
                        for (i in 0 until count) {
                            listImage.add(clipData.getItemAt(i).uri.toString())
                        }
                        viewModel.sendImg(listImage.toList())
                    } else viewModel.sendImg(listOf(it.data!!.data!!.toString()))
                }
            }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chatParentView.setOnClickListener { hideKeyboard() }

        val tv = TypedValue()
        val actionBarHeight =
            if (requireContext().theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
            } else 0

        val param = binding.toolBarChatFragment.layoutParams as ViewGroup.MarginLayoutParams

        param.height = actionBarHeight * 3 / 2

        binding.toolBarChatFragment.layoutParams = param

        binding.toolBarChatFragment.setPadding(0, actionBarHeight / 2, 0, 0)

        requireActivity().window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)


        bottomSheetGalleryBehavior =
            BottomSheetBehavior.from(binding.bottomSheetGalleryImage.parentViewBottomSheetGalleryImage)

        setupBottomSheetGallery()

        viewModel.listMessage.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                delay(500)
                if (it.isNotEmpty()) {
                    binding.recyclerViewChat.smoothScrollToPosition(it.size)
                }
            }
        }

        binding.imageButtonChatSend.setOnClickListener {
            viewModel.sendMsg()
            viewModel.selectedGalleryImage.value?.apply {
                if (this.isNotEmpty()) {
                    viewModel.sendImg(this)
                    hideBottomSheetGallery()
                }
            }
            hideKeyboard()
        }

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
            checkPermission { showBottomSheetGallery() }
        }

        viewModel.selectedGalleryImage.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.bottomSheetGalleryImage.textViewBottomSheetImageGalleryImageCount.text =
                    it.size.toString()
                binding.bottomSheetGalleryImage.textViewBottomSheetImageGalleryImageCount.visibility =
                    View.VISIBLE
                binding.bottomSheetGalleryImage.imageButtonBottomSheetImageGalleryExpanseSend.isEnabled =
                    true
            } else {
                binding.bottomSheetGalleryImage.textViewBottomSheetImageGalleryImageCount.visibility =
                    View.GONE
                binding.bottomSheetGalleryImage.imageButtonBottomSheetImageGalleryExpanseSend.isEnabled =
                    false
            }
        }

        binding.imageButtonChatCollapseGalleryBottomSheet.setOnClickListener {
            hideBottomSheetGallery()
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
    }

    private fun takePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        openCameraForResult.launch(cameraIntent)
    }

    private fun setupBottomSheetGallery() {
        bottomSheetGalleryBehavior.apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        val bottomSheetBinding = binding.bottomSheetGalleryImage

        binding.parentViewChatBottomSheetGallery.minimumHeight =
            requireContext().resources.displayMetrics.heightPixels

        val tv = TypedValue()
        val actionBarHeight =
            if (requireContext().theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
            } else 0

        bottomSheetGalleryBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val slideOff = if (slideOffset < 0) 0f else slideOffset

                val paramHeader =
                    binding.bottomSheetGalleryImage.viewBottomSheetImageGalleryExpanseHeader.layoutParams
                paramHeader.height = (actionBarHeight * 3 / 2 * slideOff).toInt().inc()
                binding.bottomSheetGalleryImage.viewBottomSheetImageGalleryExpanseHeader.layoutParams =
                    paramHeader

                val paramIndicator =
                    binding.bottomSheetGalleryImage.viewBottomSheetImageGalleryIndicator.layoutParams
                paramIndicator.height = (20.dpToPx(requireContext()) * (1 - slideOff)).toInt().inc()
                binding.bottomSheetGalleryImage.viewBottomSheetImageGalleryIndicator.layoutParams =
                    paramIndicator

                if (slideOff == 1f) {
                    binding.bottomSheetGalleryImage.viewBottomSheetImageGalleryIndicator.visibility =
                        View.GONE
                    binding.bottomSheetGalleryImage.groupBottomSheetImageGalleryExpanseSendBox.visibility =
                        View.VISIBLE
                    if (viewModel.selectedGalleryImage.value.isNullOrEmpty())
                        binding.bottomSheetGalleryImage.textViewBottomSheetImageGalleryImageCount.visibility =
                            View.GONE
                } else {
                    binding.bottomSheetGalleryImage.viewBottomSheetImageGalleryIndicator.visibility =
                        View.VISIBLE
                    binding.bottomSheetGalleryImage.groupBottomSheetImageGalleryExpanseSendBox.visibility =
                        View.GONE
                }

                if (slideOff == 0f) binding.bottomSheetGalleryImage.groupBottomSheetImageGalleryHeader.visibility =
                    View.GONE
                else binding.bottomSheetGalleryImage.groupBottomSheetImageGalleryHeader.visibility =
                    View.VISIBLE
            }
        })

        bottomSheetGalleryBehavior.peekHeight =
            requireContext().resources.displayMetrics.heightPixels * 2 / 5

        bottomSheetBinding.viewModel = viewModel

        bottomSheetBinding.recyclerViewGalleryImage.apply {
            adapter = GalleryImageListAdapter(object : OnClickGalleryImageRecyclerView {
                override fun onClickGalleryImage(position: Int) {
                    viewModel.onClickGalleryImage(position)
                }
            })
            layoutManager = GridLayoutManager(requireContext(), 3)
            addItemDecoration(
                MarginItemDecorationGridLayout(
                    1.dpToPx(requireContext()),
                    3
                )
            )
        }

        bottomSheetBinding.imageButtonBottomSheetImageGalleryExpanseHeaderClose.setOnClickListener {
            hideBottomSheetGallery()
        }

        bottomSheetBinding.imageButtonBottomSheetImageGalleryExpanseSend.setOnClickListener {
            if (viewModel.listGalleryImageUIModel.value?.isNotEmpty() == true) {
                viewModel.sendImg(viewModel.selectedGalleryImage.value?.toList() ?: listOf(" "))
                hideBottomSheetGallery()
            }
        }

        viewModel.initListGalleryImageUIModel(getAllGalleryImage())
    }

    private fun hideBottomSheetGallery() {
        bottomSheetGalleryBehavior.isHideable = true
        bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        val param = binding.viewChatChatBox.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0)
        binding.groupChatChatBoxNotOpenGallery.visibility = View.VISIBLE
        binding.imageButtonChatCollapseGalleryBottomSheet.visibility = View.GONE
    }

    private fun showBottomSheetGallery() {
        bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetGalleryBehavior.isHideable = false
        val param = binding.viewChatChatBox.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(0, 0, 0, requireContext().resources.displayMetrics.heightPixels * 2 / 5)
        binding.groupChatChatBoxNotOpenGallery.visibility = View.GONE
        binding.imageButtonChatCollapseGalleryBottomSheet.visibility = View.VISIBLE
        viewModel.initListGalleryImageUIModel(getAllGalleryImage())
    }

    private fun getAllGalleryImage(): List<String> {

        val listImageURI = mutableListOf<String>()

        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val selection: String? = null
        val selectionArgs: Array<String>? = null

        requireContext().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                listImageURI.add(imageUri.toString())
            }
        }

        return listImageURI
    }

    private fun getImageUri(img: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
            img,
            "Title",
            null
        )
        return Uri.parse(path)
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