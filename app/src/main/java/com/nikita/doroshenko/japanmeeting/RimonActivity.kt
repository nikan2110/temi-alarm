//package com.nikita.doroshenko.japanmeeting
//
//import android.content.Intent
//import android.os.Bundle
//import android.webkit.WebView
//import android.webkit.WebViewClient
//import android.widget.Button
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.nikita.doroshenko.japanmeeting.utils.Constants
//
//class RimonActivity : AppCompatActivity() {
//
//    private lateinit var webViewBrowser: WebView
//
//    private lateinit var buttonBack: Button
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_rimon)
//
//        val extras = intent.extras
//        if (extras != null) {
//            val womanId = extras.getString("womanId")
//            Toast.makeText(this,womanId, Toast.LENGTH_LONG).show()
//        }
//
//        webViewBrowser = findViewById(R.id.wv_browser)
//        webViewBrowser.settings.javaScriptEnabled = true
//        webViewBrowser.settings.domStorageEnabled = true
//        webViewBrowser.settings.javaScriptCanOpenWindowsAutomatically = true
//        webViewBrowser.settings.databaseEnabled = true
//        webViewBrowser.webViewClient = WebViewClient()
//        webViewBrowser.loadUrl(Constants.RIMON_BASE_URL)
//
//        buttonBack = findViewById(R.id.btn_back)
//        buttonBack.setOnClickListener {
//            val destinationActivity = MenuActivity::class.java
//            val menuActivityIntent = Intent(this@RimonActivity, destinationActivity)
//            startActivity(menuActivityIntent)
//        }
//    }
//}