package com.dongchyeon.simplechatapp.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.dongchyeon.simplechatapp.BuildConfig
import com.dongchyeon.simplechatapp.util.RealPathUtil.getRealPathFromURI_API19
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class TakePictureFromCameraOrGallery : ActivityResultContract<Unit, String?>() {
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri

    private lateinit var ctx: Context  // scanFile 메소드를 쓰기위한 컨텍스트 변수

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun createIntent(context: Context, input: Unit): Intent {
        ctx = context
        return openImageIntent(context)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        if (resultCode != Activity.RESULT_OK) return null

        if (intent?.data == null) {
            scanFile(ctx, photoFile)

            return photoFile.absolutePath
        } else {
            return getRealPathFromURI_API19(ctx, intent.data!!)
        } // 갤러리에서 선택한 intent.data 가 비어있으면 카메라로 찍어서 얻은 파일경로를 반환
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun openImageIntent(context: Context): Intent {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoUri = getUriFromTakenPhoto(context)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

        val galleryIntent = Intent(MediaStore.ACTION_PICK_IMAGES)
        galleryIntent.type = "image/*"

        val intentList = arrayListOf<Intent>()

        val packageManger = context.packageManager
        if (cameraIntent.resolveActivity(packageManger) != null) intentList.add(cameraIntent)
        if (galleryIntent.resolveActivity(packageManger) != null) intentList.add(galleryIntent)

        val chooser = Intent.createChooser(galleryIntent, "이미지를 불러올 앱을 선택해주세요.")
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toTypedArray())

        return chooser
    }

    private fun createPhoto(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(Date())
        val imageFileName = "IMG_$timeStamp.jpg"
        val storageDir =
            File(Environment.getExternalStorageDirectory().toString() + "/Pictures/SimpleChat")
        if (!storageDir.exists()) storageDir.mkdirs()

        return File(storageDir, imageFileName)
    }

    private fun getUriFromTakenPhoto(context: Context): Uri {
        photoFile = createPhoto()

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".fileProvider",
                photoFile
            )
        } else {
            Uri.fromFile(photoFile)
        }
    }

    private fun scanFile(context: Context, file: File) {
        MediaScannerConnection.scanFile(
            context, arrayOf(file.absolutePath), null
        ) { _, uri -> photoUri = uri }
    }
}