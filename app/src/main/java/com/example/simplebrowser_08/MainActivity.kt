package com.example.simplebrowser_08

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    // Mark -Properties/////////////////////////////////////////
    private val webView: WebView by lazy { findViewById(R.id.webView_Wb) }
    private val addressBar: EditText by lazy { findViewById(R.id.addressBar) }
    private val goHomeBtn: ImageButton by lazy {findViewById(R.id.goHomeButton)}
    private val goBackBtn: ImageButton by lazy { findViewById(R.id.goBackButton) }
    private val goForwardBtn: ImageButton by lazy { findViewById(R.id.goForwardButton) }

    // Mark -LifeCycle/////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", "onCreate() called")
        initViews()
        bindViews()
    }

    override fun onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack()
        }else{
            super.onBackPressed()
        }
    }

    // Mark -Helper/////////////////////////////////////////
    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews(){
        webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(DEFAULT_URL)
        }

    }
    private fun bindViews(){
        addressBar.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                webView.loadUrl(v.text.toString())
            }
            return@setOnEditorActionListener false
        }
        goBackBtn.setOnClickListener {
            webView.goBack()
        }
        goForwardBtn.setOnClickListener {
            webView.goForward()
        }
        goHomeBtn.setOnClickListener {
            webView.loadUrl(DEFAULT_URL)
        }

    }

    companion object {
        private const val DEFAULT_URL = "http://www.google.com"
    }
}