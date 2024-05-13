package com.looker.kenko.data

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.UriHandler
import com.looker.kenko.R

class KenkoUriHandler(private val context: Context) : UriHandler {
    override fun openUri(uri: String) {
        try {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(uri)
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            error(context.getString(R.string.error_invalid_url))
        }
    }
}