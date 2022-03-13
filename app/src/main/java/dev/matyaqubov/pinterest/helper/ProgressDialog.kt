package dev.matyaqubov.pinterest.helper

import android.app.Dialog
import android.content.Context
import android.view.Window
import dev.matyaqubov.pinterest.R

object ProgressDialog {
    lateinit var dialog: Dialog

    fun showProgress(context: Context) {
        dialog = Dialog(context);
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progessbar);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    fun dismissProgress() {
        dialog.dismiss()
    }


}