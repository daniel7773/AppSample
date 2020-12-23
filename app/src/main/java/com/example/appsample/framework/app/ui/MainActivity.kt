package com.example.appsample.framework.app.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.annotation.NavigationRes
import androidx.navigation.NavController
import com.example.appsample.R
import com.example.appsample.framework.base.presentation.BaseActivity
import com.example.appsample.framework.base.presentation.BaseApplication
import com.example.appsample.framework.presentation.auth.di.factories.fragments.AuthFragmentFactory
import com.example.appsample.framework.presentation.auth.di.factories.fragments.AuthNavHostFragment
import com.example.appsample.framework.presentation.common.model.AuthResource
import com.example.appsample.framework.presentation.profile.di.factories.fragments.ProfileFragmentFactory
import com.example.appsample.framework.presentation.profile.di.factories.fragments.ProfileNavHostFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/*

    val TAG = "MainActivity"

    @Inject
    lateinit var authFragmentFactory: AuthFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        ((this.application) as BaseApplication)
            .getAppComponent()
            .mainComponent()
            .create()
            .inject(this)

        supportFragmentManager.fragmentFactory = authFragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


 */

private const val NAV_HOST_ID = "NavHostFragment"
private const val FRAGMENT_FACTORY_NAME = "FragmentFactoryName"
private const val ENTERED_ID = "EnteredId"

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
class MainActivity : MainNavController, BaseActivity() {

    private val TAG: String = "MyMainActivity"

    private lateinit var navController: NavController

    private var fragmentFactoryName: String? = null
    private var graphId: Int? = null

    val supervisor = SupervisorJob()

    private fun createNavHost(@NavigationRes graphId: Int, fragmentFactoryName: String) {
        this.fragmentFactoryName = fragmentFactoryName
        this.graphId = graphId

        val newNavHostFragment = when (fragmentFactoryName) {

            AuthFragmentFactory.FRAGMENT_FACTORY_NAME -> {
                AuthNavHostFragment.create(
                    graphId
                )
            }

            ProfileFragmentFactory.FRAGMENT_FACTORY_NAME -> {
                ProfileNavHostFragment.create(
                    graphId
                )
            }

            else -> {
                AuthNavHostFragment.create(
                    graphId
                )
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.main_nav_host_container,
                newNavHostFragment,
                getString(R.string.NavHostFragmentTag)
            )
            .setPrimaryNavigationFragment(newNavHostFragment)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        observeSessionManager()
    }

    override fun onResume() {
        super.onResume()
        if (fragmentFactoryName == null || graphId == null) {
            navAuth()
        }
    }

    override fun navProfile() {
        createNavHost(
            R.navigation.nav_app_graph,
            ProfileFragmentFactory.FRAGMENT_FACTORY_NAME
        )
    }

    override fun navAuth() {
        createNavHost(
            R.navigation.auth_nav_graph,
            AuthFragmentFactory.FRAGMENT_FACTORY_NAME
        )
    }

    override fun navController() = navController

    override fun setNavController(navController: NavController) {
        this.navController = navController
    }

    private fun observeSessionManager() = runBlocking {
        launch(supervisor) {
            (application as BaseApplication).getAppComponent()
                .sessionManager().stateFlow.collectLatest { authResource ->
                    when (authResource) {
                        is AuthResource.Authenticated -> {
                            Log.d(TAG, "user authentication ------ Authenticated ------ ")
                            onAuthenticated()
                        }
                        is AuthResource.Loading -> {
                            Log.d(TAG, "user authentication ---------- Loading ---------- ")
                        }
                        is AuthResource.Error -> { // TODO: navigate to error screen
                            Log.d(TAG, "user authentication ---------- Error ---------- ")
                            onError()
                        }
                        is AuthResource.NotAuthenticated -> {
                            Log.d(TAG, "user authentication ----- NotAuthenticated ----- ")
                            Log.d(TAG, "Moving to Auth Fragment ")
                            onLogOut()
                        }
                    }
                }
        }
    }

    override fun onAuthenticated() = navProfile()

    override fun onLogOut() = navAuth()

    override fun onError() {
        // TODO: think about what to put here
    }
    
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        graphId = savedInstanceState.getInt(NAV_HOST_ID)
        fragmentFactoryName = savedInstanceState.getString(FRAGMENT_FACTORY_NAME)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        graphId?.let { outState.putInt(NAV_HOST_ID, it) }
        fragmentFactoryName?.let { outState.putString(FRAGMENT_FACTORY_NAME, it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        supervisor.cancel()
    }
}