package com.ninja.editify.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ninja.editify.model.Photo

class ImagePagerAdapter(
    private val photoList: List<Photo>,
    activity: FragmentActivity,
) : FragmentStateAdapter(activity) {
    override fun getItemCount() = photoList.size

    override fun createFragment(position: Int): Fragment {
        return PhotoFragment.newInstance(photoList[position])
    }

}