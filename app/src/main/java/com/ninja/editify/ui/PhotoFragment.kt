package com.ninja.editify.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.ninja.editify.databinding.FragmentPhotoBinding
import com.ninja.editify.model.Photo
import com.ninja.editify.utils.PhotoFilter
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import java.net.URL


class PhotoFragment : Fragment() {

    private  lateinit var binding: FragmentPhotoBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photo = arguments?.getParcelable<Photo>(PHOTO_KEY) as Photo
        binding?.let {
            with(it) {
                tutorialName.text = photo.name
                tutorialDesc.text = photo.description
                reload.setOnClickListener { downloadPhotoImages(photo) }
            }
        }
        downloadPhotoImages(photo)
    }

    private fun downloadPhotoImages(photo: Photo) {
        if (photo.imageTwoUrl.isBlank()) {
            downloadSingleImage(photo)
        } else {
            downloadTwoImages(photo)
        }
    }

    private fun downloadSingleImage(photo: Photo) {
      /*  lifecycleScope.launch {
            val originalBitmap = getOriginalBitmap(photo)
            val photoFilterBitmap = loadPhotoFilter(originalBitmap)
            loadImage(photoFilterBitmap)
        }*/

        photoLifecycleScope.launch {
            val originalBitmap = getOriginalBitmap(photo)
            val photoFilterBitmap = loadPhotoFilter(originalBitmap)
           // loadImage(photoFilterBitmap)
            loadImage(originalBitmap)
        }
    }

    private fun downloadTwoImages(photo: Photo) {
        lifecycleScope.launch {
            val deferredOne = lifecycleScope.async {
                getOriginalBitmap(photo)
            }

            val deferredTwo = lifecycleScope.async {
                val originalBitmap = getOriginalBitmap(photo)
                loadPhotoFilter(originalBitmap)
            }

            try {

                loadTwoImages(deferredOne.await(), deferredTwo.await())
            }
            catch (e: Exception) {
                showError("Try/catch: ${e.message}")
            }
        }
    }

    private suspend fun getOriginalBitmap(photo: Photo) : Bitmap =
        withContext(Dispatchers.IO) {
            //3
            URL(photo.imageUrl).openStream().use {
                return@withContext BitmapFactory.decodeStream(it)
            }
        }

    private fun loadImage(snowFilterBitmap: Bitmap) {
        binding?.let {
            with(it) {
                progressBar.visibility = View.GONE
                reload.isVisible = false
                errorMessage.isVisible = false
                snowFilterImage.setImageBitmap(snowFilterBitmap)
            }
        }
    }

    private fun loadTwoImages(imageOne: Bitmap, imageTwo: Bitmap) {
        binding?.let {
            with(it) {
                progressBar.visibility = View.GONE
                reload.isVisible = false
                errorMessage.isVisible = false
                snowFilterImage.setImageBitmap(imageOne)
                snowFilterSecondImage.setImageBitmap(imageTwo)
            }
        }
    }

/*    private fun loadPhotoFilter(originalBitmap: Bitmap): Bitmap {
        return PhotoFilter.applySnowEffect(originalBitmap)
    }*/

    private suspend fun loadPhotoFilter(originalBitmap: Bitmap): Bitmap =
        /**
         *    This task is more cpu intensive because it has to goes through every pixel of an image and does the task
         *    you can move this to
         */
        withContext(Dispatchers.Default) {
            PhotoFilter.applySnowEffect(originalBitmap)
        }


    private fun showError(message: String) {
        binding?.let {
            with(it) {
                errorMessage.isVisible = true
                reload.isVisible = true
                errorMessage.text = message
                progressBar.isVisible = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    companion object{
        const val PHOTO_KEY = "PHOTO_KEY"

        fun newInstance(photo: Photo) : PhotoFragment {
            return PhotoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PHOTO_KEY, photo)
                }
            }
        }
    }

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            showError("CoroutineExceptionHandler: ${throwable.message}")
            throwable.printStackTrace()
            println("Caught $throwable")
        }

    private val photoLifecycleScope = lifecycleScope + coroutineExceptionHandler

}