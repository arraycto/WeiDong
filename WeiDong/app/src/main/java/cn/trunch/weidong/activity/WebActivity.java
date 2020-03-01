package cn.trunch.weidong.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import cn.trunch.weidong.R;
import cn.trunch.weidong.util.StatusBarUtil;

public class WebActivity extends AppCompatActivity {

    private String title;
    private String href;

    private ImageView webCloseBtn;
    private TextView webTitle;
    private WebView webContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        StatusBarUtil.setStatusBarMode(this, true, R.color.colorLight);

        title = getIntent().getStringExtra("title");
        href = getIntent().getStringExtra("href");

        init();
        initListener();
    }

    private void init() {
        webCloseBtn = findViewById(R.id.webCloseBtn);
        webTitle = findViewById(R.id.webTitle);
        webContent = findViewById(R.id.webContent);
        webTitle.setText(title);

        //声明WebSettings子类
        WebSettings webSettings = webContent.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); //默认 根据cache-control决定是否从网络上取数据。
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        webSettings.setDomStorageEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);

        webSettings.setAppCacheEnabled(true);

        //加载网页
        webContent.loadUrl(href);

        //复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        webContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
//        webContent.setWebViewClient(new WebViewClient());
//        webContent.setWebChromeClient(new WebChromeClient());
    }

    private void initListener() {
        webCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
