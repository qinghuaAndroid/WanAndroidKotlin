package com.wan.android.ui.browser

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.NestedScrollAgentWebView
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient
import com.wan.android.R
import com.wan.android.databinding.ActivityBrowserNormalBinding
import com.wan.baselib.base.BaseActivity
import com.wan.common.constant.Const

/**
 * @author cy
 * Create at 2020/4/8.
 */
class BrowserNormalActivity : BaseActivity<ActivityBrowserNormalBinding>() {
    private var title: String? = null
    private var url: String? = null
    private lateinit var mAgentWeb: AgentWeb
    private val mWebView: NestedScrollAgentWebView by lazy {
        NestedScrollAgentWebView(this)
    }

    override fun getLayoutId(): Int = R.layout.activity_browser_normal

    override fun initData(savedInstanceState: Bundle?) {
        title = intent.getStringExtra(Const.WEB_TITLE)
        url = intent.getStringExtra(Const.WEB_URL)
    }

    override fun initView() {
        title?.let { setTitle(it) }
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(binding.container, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setWebChromeClient(mWebChromeClient)
            .setWebViewClient(mWebViewClient)
            .setMainFrameErrorView(com.just.agentweb.R.layout.agentweb_error_page, -1)
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            .setWebView(mWebView)
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK) //打开其他应用时，弹窗咨询用户是否前往其他应用
            .interceptUnkownUrl() //拦截找不到相关页面的Scheme
            .createAgentWeb()
            .ready()
            .go(url)
    }

    private val mWebViewClient: WebViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            //do you  work
            Log.i("Info", "BaseWebActivity onPageStarted")
        }
    }

    private val mWebChromeClient: WebChromeClient =
        object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                //此处可获取网页标题
            }
        }

    override fun loadData() {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }


    override fun onDestroy() {
        super.onDestroy()
        mAgentWeb.webLifeCycle.onDestroy()
    }
}