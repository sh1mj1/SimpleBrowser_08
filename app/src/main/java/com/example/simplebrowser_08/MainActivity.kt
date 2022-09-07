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
                계속 주소를 입력할 때 앞에 http:// 을 입력함
                웹뷰에서는 기본적으로 http:// 을 자동으로 붙여주는 것을 지원하지 않음.
                (자바 스크립트라 html 그 자체를 불러올 수도 있다.
                우리 같은 경우 web 브라우저 이기 때문에 http:// 을 붙이는 것이 디폴트임.
                그래서 url 을 입력했을 때 기본으로 http:// 로 접속되도록 해주어야 함.
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
        // 웹 페이지가 시작했을 때만 보이게ㅔ
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
            // 리다이렉팅
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