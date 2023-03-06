package com.nikita.doroshenko.japanmeeting

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import java.io.File

class CustomerDetailsActivity : AppCompatActivity() {

    private lateinit var imageViewDetailPicture: ImageView

    private lateinit var buttonClose: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_details)

        imageViewDetailPicture = findViewById(R.id.iw_patient_details)

        buttonClose = findViewById(R.id.btn_close)
        buttonClose.setOnClickListener {
            val destinationActivity = PatientsActivity::class.java
            val patientsActivityIntent = Intent(this@CustomerDetailsActivity, destinationActivity)
            startActivity(patientsActivityIntent)
        }

        val extras = intent.extras
        if (extras != null) {
            val picturePath = extras.getString("picturePath")
            println(picturePath)
//            Glide.with(this)
//                .load(File(picturePath))
//                .into(imageViewDetailPicture)
            val picture = BitmapFactory.decodeFile(picturePath)
            imageViewDetailPicture.setImageBitmap(picture)
        }
    }

}