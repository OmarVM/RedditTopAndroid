package com.example.reddittop.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.reddittop.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImageFullScreenActivity : AppCompatActivity(){

    companion object {
        const val WRITE_PERMISSION = 23
    }

    private var imageUrl: String? = null
    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_img_full_screen)
        mContext = this

        val mImageView: ImageView = findViewById(R.id.img_detail_full)
            imageUrl = intent.getStringExtra(PostDetailFragment.ARG_POST_IMG).toString()
            Picasso.get().load(imageUrl).into(mImageView)

        mImageView.setOnLongClickListener{
            checkPermission()
            true
        }
    }

    private fun checkPermission() {
        val readExternalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (readExternalPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), WRITE_PERMISSION)
        }else{
            imageUrl?.let { saveImage(it) }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            WRITE_PERMISSION ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    imageUrl?.let { saveImage(it) }
                }
            }
        }
    }

    private fun saveImage(url: String){
            Picasso.get().load(url).into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    try {
                        val tempDir = File("${externalMediaDirs[0]}/reddit_temp")
                        if (!tempDir.exists()){
                            tempDir.mkdirs()
                        }
                        val fileUri = "${tempDir.absolutePath}${File.separator}${System.currentTimeMillis()}.jpg"
                        val outputStream = FileOutputStream(fileUri)
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        outputStream.apply {
                            flush()
                            close()
                        }
                        Toast.makeText(mContext, resources.getString(R.string.img_saved), Toast.LENGTH_LONG ).show()
                    }catch (e: IOException){
                        Log.e("OVM", "Error saving image... ${e.message}")
                    }
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    Toast.makeText(mContext, resources.getString(R.string.img_saved_error), Toast.LENGTH_LONG ).show()
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }

            })
    }
}