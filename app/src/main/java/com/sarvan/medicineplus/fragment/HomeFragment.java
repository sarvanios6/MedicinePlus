package com.sarvan.medicineplus.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sarvan.medicineplus.R;
import com.sarvan.medicineplus.adapter.DepartmentListAdapter;
import com.sarvan.medicineplus.others.ConstDef;
import com.sarvan.medicineplus.realm.DepartmentModel;

import java.util.ArrayList;

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
//        String[] doctorList = {"Department_one", "Department_two", "Department_three", "Department_four"};
        String[] doctorDeptList = {ConstDef.GENERAL.getKey(), ConstDef.ALLERGIST.getKey(), ConstDef.ANESTHESIOLIST.getKey(), ConstDef.CARDIOLOGIST.getKey(), ConstDef.DERMATOLOGIST.getKey(), ConstDef.GASTROENTEROLOGIST.getKey()};
        String[] doctorDeptDesc = {ConstDef.GENERAL_DESC.getKey(), ConstDef.ALLERGISTDESC.getKey(), ConstDef.ANESTHESIOLISTDESC.getKey(), ConstDef.CARDIOLOGISTDESC.getKey(), ConstDef.DERMATOLOGISTDESC.getKey(), ConstDef.GASTROENTEROLOGISTDESC.getKey()};
        ArrayList<DepartmentModel> doctorsDept = new ArrayList<>();
        for (int i = 0; i < doctorDeptList.length; i++) {
            DepartmentModel departmentModel = new DepartmentModel(i, doctorDeptList[i], doctorDeptDesc[i]);
            doctorsDept.add(departmentModel);
        }
        RecyclerView recyclerViewDoctorList = (RecyclerView) view.findViewById(R.id.recycler_view_doctor_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewDoctorList.setLayoutManager(linearLayoutManager);
        recyclerViewDoctorList.setItemAnimator(new DefaultItemAnimator());
        DepartmentListAdapter departmentListAdapter = new DepartmentListAdapter(getActivity(), doctorsDept);
        recyclerViewDoctorList.setAdapter(departmentListAdapter);
        return view;
    }
}
