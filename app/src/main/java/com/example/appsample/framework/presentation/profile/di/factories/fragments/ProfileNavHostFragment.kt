package com.example.appsample.framework.presentation.profile.di.factories.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.NavigationRes
import androidx.navigation.fragment.NavHostFragment
import com.example.appsample.framework.app.ui.MainNavController
import com.example.appsample.framework.base.presentation.BaseApplication
import com.example.appsample.framework.presentation.profile.di.ProfileFragmentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@ProfileFragmentScope
class ProfileNavHostFragment : NavHostFragment() {

    private val TAG: String = "AppDebug"

    lateinit var mainNavController: MainNavController

    @Inject
    lateinit var profileFragmentFactory: ProfileFragmentFactory

    override fun onAttach(context: Context) {
        ((activity?.application) as BaseApplication)
            .getAppComponent()
            .profileComponent()
            .create()
            .inject(this)
        childFragmentManager.fragmentFactory = profileFragmentFactory
        try {
            mainNavController = context as MainNavController
        } catch (e: ClassCastException) {
            Log.e(TAG, "$context must implement MainNavController")
        }
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainNavController.setNavController(this.navController)
    }

    companion object {

        const val KEY_GRAPH_ID = "android-support-nav:fragment:graphId"

        @JvmStatic
        fun create(
            @NavigationRes graphId: Int = 0
        ): ProfileNavHostFragment {
            var bundle: Bundle? = null
            if (graphId != 0) {
                bundle = Bundle()
                bundle.putInt(KEY_GRAPH_ID, graphId)
            }
            val result = ProfileNavHostFragment()
            if (bundle != null) {
                result.arguments = bundle
            }
            return result
        }
    }

}








