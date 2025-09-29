package com.example.tickerwatchlist.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tickerwatchlist.R;

public class TickerListFragment extends Fragment {

    public interface OnTickerSelected {
        void onTickerSelected(String symbol);
    }

    private OnTickerSelected callback;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnTickerSelected) {
            callback = (OnTickerSelected) context;
        } else {
            throw new IllegalStateException("Activity must implement OnTickerSelected");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ticker_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ListView listView = view.findViewById(R.id.ticker_list);

        final String[] defaults = {"NEE", "AAPL", "DIS"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_1,
                defaults
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
                String symbol = adapter.getItem(pos);
                Toast.makeText(requireContext(), "Opening " + symbol, Toast.LENGTH_SHORT).show();
                if (callback != null) callback.onTickerSelected(symbol);
            }
        });
    }
}