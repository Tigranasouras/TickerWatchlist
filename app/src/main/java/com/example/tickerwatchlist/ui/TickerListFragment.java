package com.example.tickerwatchlist.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public OnTickerSelected callback;

    private ArrayAdapter<String> adapter;
    private final java.util.ArrayList<String> data = new java.util.ArrayList<>();

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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ticker_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ListView listView = view.findViewById(R.id.ticker_list);

        data.clear();
        data.add("NEE");
        data.add("AAPL");
        data.add("DIS");

        adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, v, position, id) -> {
            String symbol = adapter.getItem(position);
            Toast.makeText(requireContext(), "Opening " + symbol, Toast.LENGTH_SHORT).show();
            if (callback != null) callback.onTickerSelected(symbol);
        });
    }

    public void addTickerFromSms(String symbol) {
        String up = symbol.toUpperCase();
        if (data.contains(up)) return;
        if (data.size() < 6) data.add(up);
        else data.set(5, up);
        if (adapter != null) adapter.notifyDataSetChanged();
    }
}
