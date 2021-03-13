package com.example.appsample.framework.presentation.profile.screens.photo.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appsample.framework.presentation.profile.screens.photo.PhotoFragment

class ImagePagerAdapter(private val fragment: Fragment, photoList: List<String>, private val startPosition: Int? = null) :
    FragmentStateAdapter(fragment) {

    private var photoList: List<String> = emptyList()

    private var isFirstCreate = true

    init {
        this.photoList = photoList
    }

    override fun getItemCount(): Int {
        return photoList.size
    }



    override fun createFragment(position: Int): Fragment {
        return if (isFirstCreate && startPosition != null) {
            isFirstCreate = false
            PhotoFragment.newInstance(photoList[startPosition], startPosition)
        } else {
            PhotoFragment.newInstance(photoList[position], position)
        }
    }
}