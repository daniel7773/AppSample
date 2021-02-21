package com.example.appsample.framework.presentation.profile.screens.photo.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appsample.framework.presentation.profile.screens.photo.PhotoFragment

class ImagePagerAdapter(private val fragment: Fragment, photoList: List<String>) : FragmentStateAdapter(fragment) {

    private var photoList: List<String> = emptyList()

    init {
        this.photoList = photoList
    }

    fun setPhotoList(photoList: List<String>, positionMoveTo: Int?) {
        this.photoList = photoList
        notifyDataSetChanged()
        if (positionMoveTo != null) {
            notifyItemMoved(0, positionMoveTo)
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun createFragment(position: Int): Fragment {
        return PhotoFragment.newInstance(photoList[position], position)
    }
}