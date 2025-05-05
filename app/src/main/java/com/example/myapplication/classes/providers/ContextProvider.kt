package com.example.myapplication.classes.providers

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import org.koin.java.KoinJavaComponent.inject
import kotlin.getValue


interface ContextProviderInterface {
    val applicationContext: Context
    val currentActivity: Activity?
    val activityStack: List<Activity>
}

class ContextProvider: ContextProviderInterface, Application.ActivityLifecycleCallbacks {

    private val navActivityStack: MutableList<Activity>
    private val application: Application by inject(Application::class.java)

    init {
        this.navActivityStack = ArrayList<Activity>()
        application.registerActivityLifecycleCallbacks(this)
    }

    override val applicationContext: Context = application


    override val currentActivity: Activity?
        get() {
            for (activity in navActivityStack) {
                if (!activity.isFinishing) {
                    return activity
                }
            }
            return navActivityStack.lastOrNull()
        }

    override val activityStack: List<Activity>
        get() = navActivityStack

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        this.navActivityStack.add(0, activity)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        this.navActivityStack.remove(activity)
    }
}