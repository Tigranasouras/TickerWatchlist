package com.example.tickerwatchlist.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tickerwatchlist.R;

public class InfoWebFragment extends Fragment {
    private WebView web;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_web, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        web = view.findViewById(R.id.webView);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        web.setWebViewClient(new WebViewClient());
        web.setWebChromeClient(new WebChromeClient());


        web.loadUrl("https://seekingalpha.com");

        Bundle args = getArguments();
        if (args != null && args.containsKey("symbol")) {
            showTicker(args.getString("symbol"));
        }
    }

    public void showTicker(String symbol) {
        if (symbol != null) {
            web.loadUrl("https://seekingalpha.com/symbol/" + symbol);
        }
    }
}