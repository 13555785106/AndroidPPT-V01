package com.telecom.photogallery;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoPageFragment extends Fragment {

    private static final String ARG_URI = "photo_page_url";
    private Uri mUri;
    private WebView mWebView;
    private ProgressBar mProgressBar;

    public static PhotoPageFragment newInstance(Uri uri) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_URI, uri);
        PhotoPageFragment fragment = new PhotoPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUri = getArguments().getParcelable(ARG_URI);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CookieManager cookieManager = (CookieManager)CookieHandler.getDefault();


        final android.webkit.CookieManager  webkitCookieManager = android.webkit.CookieManager.getInstance();
        webkitCookieManager.setAcceptCookie(true);
        webkitCookieManager.removeAllCookie();
        for(HttpCookie httpCookie : cookieManager.getCookieStore().getCookies()) {
            webkitCookieManager.setCookie(httpCookie.getDomain(),
                    httpCookie.getName() + "=" + httpCookie.getValue()+";path="+httpCookie.getPath());
        }

        View v = inflater.inflate(R.layout.fragment_photo_page, container, false);
        mProgressBar = v.findViewById(R.id.progress_bar);
        mProgressBar.setMax(100); // WebChromeClient reports in range 0-100

        mWebView = v.findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);


        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    if (mProgressBar.getVisibility() != View.VISIBLE)
                        mProgressBar.setVisibility(View.VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }

            public void onReceivedTitle(WebView webView, String title) {
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                activity.getSupportActionBar().setSubtitle(title);
            }
        });
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mUri.toString());
        return v;
    }

}
