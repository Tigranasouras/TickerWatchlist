package com.example.tickerwatchlist;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.tickerwatchlist.ui.InfoWebFragment;
import com.example.tickerwatchlist.ui.TickerListFragment;

public class MainActivity extends AppCompatActivity implements TickerListFragment.OnTickerSelected {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.list_container) == null) {
            if (savedInstanceState == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new TickerListFragment(), "list")
                        .commit();
            }
        }
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
}
