package com.example.money;

//import static com.example.money.Detail.moneyInList;


import static com.example.money.ScarchFragement.moneyInList;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MoneyDateFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    //建立starsList存放更新方法中的資料
    private static MyMoneyDateRecyclerViewAdapter adapter;
    public MoneyDateFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MoneyDateFragment newInstance(int columnCount) {
        MoneyDateFragment fragment = new MoneyDateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter= new MyMoneyDateRecyclerViewAdapter(moneyInList);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }
    public  void update(){
        // 動態更新recyclerView方法 主程式呼叫

        adapter.notifyDataSetChanged(); //刷新
    }
}