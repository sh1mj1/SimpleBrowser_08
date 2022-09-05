package com.example.simplebrowser_08

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    // Mark -Properties/////////////////////////////////////////
    private val webView: WebView by lazy { findViewById(R.id.webView_Wb) }
    private val addressBar: EditText by lazy { findViewById(R.id.addressBar) }

    // Mark -LifeCycle/////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        bindViews()
    }


    // Mark -Helper/////////////////////////////////////////
    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews(){
        webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl("http://www.google.com")
        }

    }
    private fun bindViews(){
        addressBar.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                webView.loadUrl(v.text.toString())
            }
            return@setOnEditorActionListener false
        }
    }
}