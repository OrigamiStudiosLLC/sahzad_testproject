package com.nytimes.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.nytimes.R;
import com.nytimes.model.Result;
import com.nytimes.utalities.Constants;
import com.nytimes.utalities.Util;

public class DetailFragment extends Fragment {

    private Result mItem;
    private WebView webView;
    private String mURL = "";
    private Dialog progressBar;
    private Intent mIntent = null;

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(Constants.ARG_ITEM)) {
            mItem = (Result) getArguments().getSerializable(Constants.ARG_ITEM);
            mURL = mItem.getUrl();
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getSource());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        initialize(rootView);
        loadUrl();
        return rootView;
    }

    private void initialize(View rootView) {
        progressBar = Util.loadingDialog(getActivity());
        progressBar.show();
        webView = rootView.findViewById(R.id.webView);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress > 60) {
                    progressBar.dismiss();
                }
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadUrl() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(mURL);
    }
}
