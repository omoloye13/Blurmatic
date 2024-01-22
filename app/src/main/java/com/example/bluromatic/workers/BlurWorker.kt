package com.example.bluromatic.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.bluromatic.DELAY_TIME_MILLIS
import com.example.bluromatic.KEY_BLUR_LEVEL
import com.example.bluromatic.KEY_IMAGE_URI
import com.example.bluromatic.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val TAG = "BlurWorker"
class BlurWorker(context : Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    @Suppress("UNREACHABLE_CODE")
    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
        val resourceUri = inputData.getString(KEY_IMAGE_URI)

        val blurLevel = inputData.getInt(KEY_BLUR_LEVEL, 1)





        return withContext(Dispatchers.IO){
        //Actual blur image work is performed here
            return@withContext try {
                require(!resourceUri.isNullOrBlank()){
                    val errorMesssage = applicationContext.resources.getString(R.string.invalid_input_uri)
                    Log.e(TAG, errorMesssage)
                    errorMesssage
                }
                val resolver = applicationContext.contentResolver


                //Aan utility function added to emulate slower work
                delay(DELAY_TIME_MILLIS)
                val picture = BitmapFactory.decodeStream(
                    resolver.openInputStream(Uri.parse(resourceUri))

                )
                //blur the bitmap by calling the bitmap function an pass in picture variable and a value of 1 for the blur level parameter(opacity)
                //saving the result in a new variable named output
                val output = blurBitmap(picture, blurLevel)
                // write bitmap to a temporary file
                val outputUri = writeBitmapToFile(applicationContext, output)
                //To see when the worker executes you need to utilize worker utils make status notification function
                // The function easily lets you easily display a notification banner at the top of the screen
//                makeStatusNotification(
//                    "Output is $outputUri",
//        //            applicationContext.resources.getString(R.string.blurring_image),
//                    applicationContext
//                )
                val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())
                Result.success(outputData)
            } catch (throwable: Throwable) {
                Log.e(
                    TAG,
                    applicationContext.resources.getString(R.string.error_applying_blur),
                    throwable
                )
                Result.failure()
            }


    }
    }
}