package com.example.appsample.framework.presentation.auth.di.factories.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.NavigationRes
import androidx.navigation.fragment.NavHostFragment
import com.example.appsample.framework.app.ui.MainNavController
import com.example.appsample.framework.base.presentation.BaseApplication
import com.example.appsample.framework.presentation.auth.di.AuthFragmentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AuthFragmentScope
class AuthNavHostFragment : NavHostFragment() {

    private val TAG: String = "AppDebug"

    @Inject
    lateinit var authFragmentFactory: AuthFragmentFactory

    lateinit var mainNavController: MainNavController


    override fun onAttach(context: Context) {
        ((activity?.application) as BaseApplication)
            .getAppComponent()
            .authComponent()
            .create()
            .inject(this)

        childFragmentManager.fragmentFactory = authFragmentFactory
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
        ): AuthNavHostFragment {
            var bundle: Bundle? = null
            if (graphId != 0) {
                bundle = Bundle()
                bundle.putInt(KEY_GRAPH_ID, graphId)
            }
            val result = AuthNavHostFragment()
            if (bundle != null) {
                result.arguments = bundle
            }
            return result
        }
    }
}








