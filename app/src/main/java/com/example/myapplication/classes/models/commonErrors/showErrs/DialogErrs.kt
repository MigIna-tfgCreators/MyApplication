package com.prueba.apphouse.classes.models.commonErrors.showErrs

import androidx.appcompat.app.AlertDialog
import com.example.myapplication.R
import com.example.myapplication.classes.providers.ContextProviderInterface

class DialogErrs(
    val contextProvider: ContextProviderInterface
): DialogErrsInterface {
    override fun showErrorLogin() {
        contextProvider.currentActivity?.let { activity ->
            val builder = AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.error_type))
                .setMessage(activity.getString(R.string.error_msg_login))
                .setPositiveButton(activity.getString(R.string.general_ok), null)
                .create()
            builder.show()
        }
    }

    override fun showErrorRegister() {
        contextProvider.currentActivity?.let { activity ->
            val builder = AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.error_type))
                .setMessage(activity.getString(R.string.error_msg_register))
                .setPositiveButton(activity.getString(R.string.general_ok), null)
                .create()
            builder.show()
        }
    }
}
