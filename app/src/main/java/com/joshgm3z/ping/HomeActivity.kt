package com.joshgm3z.ping

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.joshgm3z.ping.service.PingService
import com.joshgm3z.ping.ui.PingAppContainer
import com.joshgm3z.ping.ui.navChat
import com.joshgm3z.ping.ui.navHome
import com.joshgm3z.ping.ui.navSignIn
import com.joshgm3z.ping.ui.theme.PingTheme
import com.joshgm3z.ping.utils.Logger
import com.joshgm3z.ping.utils.DataStoreUtil
import org.koin.android.ext.android.get
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!PingService.isRunning) {
            Logger.debug("PingService.isRunning = [${PingService.isRunning}]")
            startService(Intent(this, PingService::class.java))
        }
        setContent {
            PingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val dataStore: DataStoreUtil = get()
                    val isUserSignedIn = dataStore.isUserSignedIn()
                    val navController = rememberNavController()
                    PingAppContainer(
                        navController = navController,
                        startDestination = if (isUserSignedIn) navHome else navSignIn,
                        userViewModel = get(),
                        homeViewModel = get(),
                        chatViewModel = get(),
                        signInViewModel = get(),
                    )
                    if (intent.hasExtra(OPEN_CHAT_USER)) {
                        val startRoute = "$navChat/${intent.getStringExtra(OPEN_CHAT_USER)}"
                        Logger.debug("startRoute = [${startRoute}]")
                        navController.navigate(startRoute)
                        intent.removeExtra(OPEN_CHAT_USER)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
//            imageView.setImageBitmap(imageBitmap)
        }
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("PingCapture_yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    ex.printStackTrace()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.joshgm3z.ping.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    companion object {
        const val OPEN_CHAT_USER: String = "open_chat_user_id"
        const val REQUEST_IMAGE_CAPTURE = 12
    }
}