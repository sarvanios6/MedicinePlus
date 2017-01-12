package com.sarvan.medicineplus.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.sarvan.medicineplus.R;
import com.sarvan.medicineplus.activity.AskDoctorActivity;
import com.sarvan.medicineplus.activity.SignInActivity;
import com.sarvan.medicineplus.activity.UsersListActivity;

/**
 * DepartmentListAdapter Class Definition
 */

public class DepartmentListAdapter extends RecyclerView.Adapter<DepartmentListAdapter.DepartmentNameViewHolder> {
    private Context context;
    private String[] doctorNameList;

    public DepartmentListAdapter(Context context, String[] doctorNameList) {
        this.doctorNameList = doctorNameList;
        this.context = context;
    }

    @Override
    public DepartmentNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dep_name_list_item, parent, false);
        return new DepartmentNameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DepartmentNameViewHolder holder, int position) {
        holder.docterName.setText(doctorNameList[position]);
    }

    @Override
    public int getItemCount() {
        return doctorNameList.length;
    }

    public class DepartmentNameViewHolder extends RecyclerView.ViewHolder {
        private TextView docterName;

        public DepartmentNameViewHolder(View view) {
            super(view);
            docterName = (TextView) view.findViewById(R.id.doctor_name_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser == null) {
                        // Not signed in, launch the Sign In activity
                        context.startActivity(new Intent(context, SignInActivity.class));
                    } else if (firebaseUser.getDisplayName().equalsIgnoreCase("sarvan kumar")) {
                        String userId = firebaseUser.getUid();
                        Intent intent = new Intent(context, UsersListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("userId", userId);
                        intent.putExtra("department", doctorNameList[getAdapterPosition()]);
                        context.startActivity(intent);
                    } else {
                        String userId = firebaseUser.getUid();
                        Intent intent = new Intent(context, AskDoctorActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("userId", userId);
                        intent.putExtra("department", doctorNameList[getAdapterPosition()]);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
