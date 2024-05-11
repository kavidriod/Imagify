package com.ninja.editify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.ninja.editify.databinding.ActivityImageBinding
import com.ninja.editify.model.Photo
import com.ninja.editify.ui.ImagePagerAdapter

class ImagifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageBinding
    private val photoPagerAdapter by lazy {
        ImagePagerAdapter(getImageData(), this)
    }

    private fun getImageData(): List<Photo> {
         val photoList = arrayListOf<Photo>()

        photoList.add(
            Photo(
                getString(R.string.che_title),
                getString(R.string.che_url),
                "",
                getString(R.string.che_desc)
            )
        )
        photoList.add(
            Photo(
                getString(R.string.bangalore_name),
                getString(R.string.bangalore_url),
                getString(R.string.bangalore_url),
                getString(R.string.bangalore_desc)
            )
        )
        photoList.add(
            Photo(
                getString(R.string.kerala_name),
                getString(R.string.kerala_url),
                getString(R.string.kerala_url),
                getString(R.string.kerala_desc)
            )
        )
        photoList.add(
            Photo(
                getString(R.string.mangalore_name),
                getString(R.string.mangalore_url),
                getString(R.string.mangalore_url),
                getString(R.string.mangalore_desc)
            )
        )
        return photoList
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        with(binding) {
            viewPager.adapter = photoPagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->/* no-op */
                tab.text = when (position) {
                    0 -> getString(R.string.che_title)
                    1 -> getString(R.string.bangalore_name)
                    2 -> getString(R.string.kerala_name)
                    3 -> getString(R.string.mangalore_name)
                    else -> throw IllegalArgumentException("Illegal position argument: $position")
                }
            }.attach()
        }
    }
}