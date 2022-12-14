package com.example.simplebrowser_08

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.ContentLoadingProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    // Mark -Properties/////////////////////////////////////////
    private val webView: WebView by lazy { findViewById(R.id.webView_Wb) }
    private val addressBar: EditText by lazy { findViewById(R.id.addressBar) }
    private val goHomeBtn: ImageButton by lazy { findViewById(R.id.goHomeButton) }
    private val goBackBtn: ImageButton by lazy { findViewById(R.id.goBackButton) }
    private val goForwardBtn: ImageButton by lazy { findViewById(R.id.goForwardButton) }
    private val refreshLayout: SwipeRefreshLayout by lazy { findViewById(R.id.refreshLayout_Rl) }
    private val progressBar: ContentLoadingProgressBar by lazy { findViewById(R.id.progressBar_Pb) }

    // Mark -LifeCycle/////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", "onCreate() called")
        initViews()
        bindViews()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    // Mark -Helper/////////////////////////////////////////
    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews() {
        webView.apply {
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            settings.javaScriptEnabled = true
            loadUrl(DEFAULT_URL)
        }

    }

    private fun bindViews() {
        addressBar.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                /*
                ?????? ????????? ????????? ??? ?????? http:// ??? ?????????
                ??????????????? ??????????????? http:// ??? ???????????? ???????????? ?????? ???????????? ??????.
                (?????? ??????????????? html ??? ????????? ????????? ?????? ??????.
                ?????? ?????? ?????? web ???????????? ?????? ????????? http:// ??? ????????? ?????? ????????????.
                ????????? url ??? ???????????? ??? ???????????? http:// ??? ??????????????? ???????????? ???.
                 */
                val loadingUrl = v.text.toString()
                if (URLUtil.isNetworkUrl(loadingUrl)) {
                    webView.loadUrl(v.text.toString())
                } else {
                    webView.loadUrl("http://$loadingUrl")
                }

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
        refreshLayout.setOnRefreshListener {
            webView.reload()
        }

    }

    inner class WebViewClient : android.webkit.WebViewClient() {
        // ??? ???????????? ???????????? ?????? ????????????
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            progressBar.show()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            refreshLayout.isRefreshing = false
            progressBar.hide()
            goBackBtn.isEnabled = webView.canGoBack()
            goForwardBtn.isEnabled = webView.canGoForward()
            // ???????????????
            addressBar.setText(url)
        }
    }

    inner class WebChromeClient : android.webkit.WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            progressBar.progress = newProgress

        }
    }

    companion object {
        private const val DEFAULT_URL = "http://www.google.com"
    }
}