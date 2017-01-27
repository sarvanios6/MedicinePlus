package com.sarvan.medicineplus.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sarvan.medicineplus.R;
import com.sarvan.medicineplus.adapter.NewsAdapter;

import java.util.ArrayList;

/**
 * NewsFragment Class definition
 */

public class NewsFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ArrayList<String> newsString = new ArrayList<>();
        newsString.add("TB Tales: 'Why should I spend money on wife's health when I can marry again'");
        newsString.add("A mode of healing that goes bone-deep to repair");
        newsString.add("Kashmir doctors remove 14 kg ovarian cyst from 19-year-old");
        newsString.add("Govt programme to prevent lifestyle diseases");
        newsString.add("Why HIV treatment may boost syphilis risk");
        newsString.add("Health ministry asks states to screen antibiotics sale");
        newsString.add("TB Tales: 'Why should I spend money on wife's health when I can marry again'");
        newsString.add("A mode of healing that goes bone-deep to repair");
        newsString.add("Kashmir doctors remove 14 kg ovarian cyst from 19-year-old");
        newsString.add("Govt programme to prevent lifestyle diseases");
        newsString.add("Why HIV treatment may boost syphilis risk");
        newsString.add("Health ministry asks states to screen antibiotics sale");
        newsString.add("Why HIV Treatment");
        newsString.add("Health");
        RecyclerView recyclerViewDoctorList = (RecyclerView) view.findViewById(R.id.news_recycler_view);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerViewDoctorList.setLayoutManager(linearLayoutManager);
        recyclerViewDoctorList.setItemAnimator(new DefaultItemAnimator());
        NewsAdapter newsListAdapter = new NewsAdapter(getActivity(), newsString);
        recyclerViewDoctorList.setAdapter(newsListAdapter);
        return view;
    }
}
