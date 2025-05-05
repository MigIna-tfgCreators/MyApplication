package com.example.myapplication.classes.managers

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment

val Activity.navHostFragment: NavHostFragment?
    get() {
        val fragment = (this as? FragmentActivity)
            ?.supportFragmentManager
            ?.fragments
            ?.firstOrNull { it is NavHostFragment }

        return fragment as? NavHostFragment
    }