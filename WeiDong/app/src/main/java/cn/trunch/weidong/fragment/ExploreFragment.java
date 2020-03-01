package cn.trunch.weidong.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.trunch.weidong.R;
import cn.trunch.weidong.activity.MainActivity;
import cn.trunch.weidong.http.ApiUtil;

public class ExploreFragment extends Fragment {
    private View view;
    private Context context;
    private MainActivity activity;

    private WebView exploreWebView;
    private FloatingActionButton exploreRefreshBtn;
    private FloatingActionButton exploreHomeBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_explore, container, false);
        context = getActivity();
        activity = (MainActivity) getActivity();

        init();
        initListener();

        return view;
    }

    private void init() {
        exploreWebView = view.findViewById(R.id.exploreWebView);
        exploreRefreshBtn = view.findViewById(R.id.exploreRefreshBtn);
        exploreHomeBtn = view.findViewById(R.id.exploreHomeBtn);

        //声明WebSettings子类
        WebSettings webSettings = exploreWebView.getSettings();

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
        String appCachePath = getActivity().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);

        webSettings.setAppCacheEnabled(true);

        //加载网页
//        exploreWebView.loadUrl("http://47.102.200.22");
//        exploreWebView.loadUrl("http://www.baidu.com/");
        exploreWebView.loadUrl("http://47.102.200.22/#/?token=" + ApiUtil.USER_TOKEN);

        //复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
//        exploreWebView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });
        exploreWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                view.setBackgroundColor(getResources().getColor(R.color.colorBack));
            }
        });
//        exploreWebView.setWebChromeClient(new WebChromeClient());
    }

    private void initListener() {
        exploreWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        //这里处理返回键事件
                        if (exploreWebView.canGoBack()) {
                            exploreWebView.goBack();
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        exploreRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exploreWebView.reload(); //刷新
            }
        });
        exploreHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exploreWebView.clearHistory();
                exploreWebView.loadUrl("http://47.102.200.22/#/?token=" + ApiUtil.USER_TOKEN);
            }
        });
    }
}
