package com.example.tickerwatchlist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.example.tickerwatchlist.ui.TickerListFragment;


import com.example.tickerwatchlist.ui.InfoWebFragment;

public class MainActivity extends AppCompatActivity implements TickerListFragment.OnTickerSelected {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.RECEIVE_SMS }, 101);
        }

        if (findViewById(R.id.list_container) == null) {
            if (savedInstanceState == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new TickerListFragment(), "list")
                        .commit();
            }
        }

        handleSmsIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleSmsIntent(intent);
    }

    @Override
    public void onTickerSelected(String symbol) {
        FragmentManager fm = getSupportFragmentManager();

        InfoWebFragment web = (InfoWebFragment) fm.findFragmentById(R.id.web_container);
        if (web != null) {
            web.showTicker(symbol);
            return;
        }

        InfoWebFragment info = new InfoWebFragment();
        Bundle args = new Bundle();
        args.putString("symbol", symbol);
        info.setArguments(args);

        fm.beginTransaction()
                .replace(R.id.fragment_container, info, "web")
                .addToBackStack(null)
                .commit();
    }

    private void handleSmsIntent(Intent intent) {
        if (intent == null) return;
        String result = intent.getStringExtra("sms_result");
        String ticker = intent.getStringExtra("sms_ticker");
        if (result == null) return;

        var fm = getSupportFragmentManager();

        if (findViewById(R.id.list_container) == null &&
                fm.findFragmentByTag("list") == null) {
            fm.beginTransaction()
                    .replace(R.id.fragment_container, new TickerListFragment(), "list")
                    .commit();
            fm.executePendingTransactions();
        }

        TickerListFragment list = (TickerListFragment)
                (findViewById(R.id.list_container) != null
                        ? fm.findFragmentById(R.id.list_container)
                        : fm.findFragmentByTag("list"));

        if ("valid".equals(result) && ticker != null && list != null) {
            list.addTickerFromSms(ticker);

            InfoWebFragment web = (InfoWebFragment) fm.findFragmentById(R.id.web_container);
            if (web != null) {
                web.showTicker(ticker);
            } else {
                InfoWebFragment info = new InfoWebFragment();
                Bundle args = new Bundle();
                args.putString("symbol", ticker);
                info.setArguments(args);
                fm.beginTransaction()
                        .replace(R.id.fragment_container, info, "web")
                        .addToBackStack(null)
                        .commit();
            }

        } else if ("no_match".equals(result)) {
            android.widget.Toast.makeText(this, "No valid watchlist entry found", android.widget.Toast.LENGTH_LONG).show();
        } else if ("invalid".equals(result)) {
            android.widget.Toast.makeText(this, "Invalid ticker: " + ticker, android.widget.Toast.LENGTH_LONG).show();
        }
    }
    }
