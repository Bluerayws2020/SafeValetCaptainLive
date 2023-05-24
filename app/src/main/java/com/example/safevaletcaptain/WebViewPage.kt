package com.example.safevaletcaptain
import androidx.activity.addCallback
import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.example.safevaletcaptain.databinding.ActivityWebViewPageBinding
import com.example.safevaletcaptain.helpers.HelperUtils
import com.example.safevaletcaptain.helpers.ViewUtils.hide
import com.example.safevaletcaptain.helpers.ViewUtils.show

class WebViewPage : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewPageBinding

    private var uploadCallback: ValueCallback<Array<Uri>>? = null
    private var mFileChooserParams: WebChromeClient.FileChooserParams? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val link  = intent.getStringExtra("link")

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        HelperUtils.setDefaultLanguage(this, HelperUtils.getLang(this))
        binding?.webView?.settings?.javaScriptEnabled = true
        binding?.webView?.settings?.domStorageEnabled = true
        binding?.webView?.settings?.allowFileAccess = true
        binding?.webView?.settings?.allowContentAccess = true
        binding?.webView?.settings?.loadWithOverviewMode = true
        binding?.webView?.settings?.useWideViewPort = true
        binding?.webView?.webViewClient = WebViewCustom()
        binding?.webView?.webChromeClient = MyCustomChromeClient()
 val  cwebUrl = link
            binding?.webView?.loadUrl(cwebUrl.toString())

    }



    inner class WebViewCustom : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            binding?.progressBarWeb?.show()
            binding?.webView?.hide()
        }

        override fun onPageFinished(view: WebView, url: String) {
            binding?.progressBarWeb?.hide()
            binding?.webView?.show()
            super.onPageFinished(view, url)
        }
    }


    private inner class MyCustomChromeClient : WebChromeClient() {

        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {
            uploadCallback = null
            mFileChooserParams = null
            uploadCallback = filePathCallback
            mFileChooserParams = fileChooserParams
            if (ActivityCompat.checkSelfPermission(
                    this@WebViewPage,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestStoragePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else
                importImageGallery()
            return true
        }
    }
    private fun importImageGallery() {
        val intent = mFileChooserParams?.createIntent()
        openGalleryActivity.launch(intent)
    }

    private val openGalleryActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                uploadCallback?.onReceiveValue(
                    WebChromeClient.FileChooserParams.parseResult(
                        result.resultCode,
                        result.data,
                    )
                )
            } else
                uploadCallback?.onReceiveValue(emptyArray())
        }

    private val requestStoragePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                showMessage("DONE")
                importImageGallery()
            } else
                showMessage("denid")
        }
    protected fun showMessage(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

}