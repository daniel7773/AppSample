package com.example.appsample.framework.presentation.profile.screens.photo

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.appsample.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


class PhotoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_image, container, false)
        val arguments = arguments
        val imageUrl = arguments!!.getString(KEY_IMAGE_RES)
        val position = arguments.getInt(KEY_POSITION)

        val imageView = view.findViewById<ImageView>(R.id.image)
        imageView.transitionName = getString(R.string.photo_transition_name) + position
        val transition = LayoutTransition()
        transition.setAnimateParentHierarchy(false)
        container?.layoutTransition = transition
        container?.layoutTransition?.setAnimateParentHierarchy(false)  // to prevent bug of animation viewPager https://stackoverflow.com/questions/59660691/java-lang-illegalstateexception-page-can-only-be-offset-by-a-positive-amount


        if (imageView != null) {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_square_placeholder)
                .error(R.drawable.ic_error)
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        parentFragment?.startPostponedEnterTransition()
                    }

                    override fun onError(e: Exception?) {
                        parentFragment?.startPostponedEnterTransition()
                    }
                })
        }
        return view
    }

    companion object {
        private const val KEY_IMAGE_RES = "imageRes"
        private const val KEY_POSITION = "position"
        fun newInstance(drawableRes: String, position: Int): PhotoFragment {
            val fragment = PhotoFragment()
            val argument = Bundle()
            argument.putString(KEY_IMAGE_RES, drawableRes)
            argument.putInt(KEY_POSITION, position)
            fragment.arguments = argument
            return fragment
        }
    }
}