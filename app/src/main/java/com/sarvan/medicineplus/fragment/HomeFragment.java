package com.sarvan.medicineplus.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sarvan.medicineplus.R;
import com.sarvan.medicineplus.adapter.DepartmentListAdapter;

/**
 * HomeFragment Class Definition
 */
public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        String[] doctorList = {"Department_one", "Department_two", "Department_three", "Department_four"};
        RelativeLayout doctorLayout = (RelativeLayout) view.findViewById(R.id.ask_doctor_layout);
        RecyclerView recyclerViewDoctorList = (RecyclerView) view.findViewById(R.id.recycler_view_doctor_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewDoctorList.setLayoutManager(linearLayoutManager);
        recyclerViewDoctorList.setItemAnimator(new DefaultItemAnimator());
        DepartmentListAdapter departmentListAdapter = new DepartmentListAdapter(getActivity(), doctorList);
        recyclerViewDoctorList.setAdapter(departmentListAdapter);
        return view;
    }
}
