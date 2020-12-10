package com.example.appsample.framework.app.ui

import androidx.navigation.NavController

interface MainNavController {

    fun navController(): NavController

    fun setNavController(navController: NavController)

    fun navProfile()

    fun navAuth()
}