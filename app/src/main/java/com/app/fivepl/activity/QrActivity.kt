package com.app.fivepl.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import com.app.fivepl.R
import com.app.fivepl.databinding.ActivityQrBinding
import com.app.fivepl.helper.Constant
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class QrActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrBinding
    lateinit var activity: Activity
    lateinit var session: com.app.fivepl.helper.Session

    private val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 101
    private val FILE_PROVIDER_AUTHORITY = "com.app.pocketfarm.provider"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrBinding.inflate(layoutInflater)
        activity = this
        session = com.app.fivepl.helper.Session(activity)

        binding.ibBack.setOnClickListener {
            onBackPressed()
        }


        val qrimage = session.getData(Constant.QR_IMAGE)

        Glide.with(this).load(qrimage).placeholder(R.drawable.ic_launcher_foreground)
            .into(binding.ivQr)

        binding.btnDownload.setOnClickListener {
            if (isWritePermissionGranted()) {
                downloadAndSaveImage()
            } else {
                requestWritePermission()
            }
        }
        setContentView(binding.root)
    }

    private fun isWritePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    private fun requestWritePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
            WRITE_EXTERNAL_STORAGE_REQUEST_CODE
        )
    }

    private fun downloadAndSaveImage() {
        // Get the bitmap from ImageView
        val imageView = binding.ivQr
        val bitmap = (imageView.drawable).toBitmap()




        // Check if external storage is available
        val state = Environment.getExternalStorageState()
        if (state != Environment.MEDIA_MOUNTED) {
            Toast.makeText(this, "External storage is not available", Toast.LENGTH_SHORT).show()
            return
        }

        // Save the bitmap to external storage
        val fileName = "QR_Image.png"
        val directory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "YourAppName"
        )
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, fileName)
        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
            Toast.makeText(this, "Image saved to Downloads folder", Toast.LENGTH_SHORT).show()

            // Show the image in gallery
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = FileProvider.getUriForFile(
                this,
                FILE_PROVIDER_AUTHORITY,
                file
            )
            intent.setDataAndType(uri, "image/*")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                downloadAndSaveImage()
            } else {
                Toast.makeText(
                    this,
                    "Permission denied, cannot save image",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
