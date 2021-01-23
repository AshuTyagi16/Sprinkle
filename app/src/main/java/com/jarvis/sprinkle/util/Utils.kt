package com.jarvis.sprinkle.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.core.content.FileProvider
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object Utils {

    @WorkerThread
    fun getUriFromImage(context: Context, bitmap: Bitmap): Uri {
        val imagesFolder = File(context.cacheDir, "images")
        var uri: Uri? = null
        try {
            imagesFolder.mkdirs()
            val file = File(imagesFolder, "shared_image.png")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
            uri = FileProvider.getUriForFile(context, "com.jarvis.fileprovider", file)
        } catch (e: IOException) {
            Timber.d("IOException while trying to write file for sharing: ${e.message}")
        }
        return uri!!
    }
}