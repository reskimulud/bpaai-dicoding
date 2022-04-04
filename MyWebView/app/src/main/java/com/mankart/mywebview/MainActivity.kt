package com.mankart.mywebview

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.web_view)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String?) {
                // Toast.makeText(this@MainActivity, "Page is finished loading", Toast.LENGTH_SHORT).show()
                view.loadUrl("javascript:alert('Page is finished loading')")
            }
        }

        webView.webChromeClient = object: WebChromeClient() {
            override fun onJsAlert(
                view: WebView,
                url: String,
                message: String,
                result: JsResult
            ): Boolean {
                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                result.confirm()
                return true
            }
        }

        webView.loadUrl("https://reskimulud.my.id")
    }
}