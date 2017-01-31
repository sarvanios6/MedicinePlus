package com.sarvan.medicineplus.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sarvan.medicineplus.R;
import com.sarvan.medicineplus.activity.AskDoctorActivity;
import com.sarvan.medicineplus.activity.SignInActivity;
import com.sarvan.medicineplus.activity.UsersListActivity;
import com.sarvan.medicineplus.others.CircleTransForm;
import com.sarvan.medicineplus.others.Helper;
import com.sarvan.medicineplus.realm.DepartmentModel;

import java.util.ArrayList;

/**
 * DepartmentListAdapter Class Definition
 */

public class DepartmentListAdapter extends RecyclerView.Adapter<DepartmentListAdapter.DepartmentNameViewHolder> {
    private Context context;
    private ArrayList<DepartmentModel> doctorDepartmentList;

    public DepartmentListAdapter(Context context, ArrayList<DepartmentModel> doctorDepartmentList) {
        this.doctorDepartmentList = doctorDepartmentList;
        this.context = context;
    }

    @Override
    public DepartmentNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dep_name_list_item, parent, false);
        return new DepartmentNameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DepartmentNameViewHolder holder, int position) {
        holder.docterDeptName.setText(doctorDepartmentList.get(position).getDepartmentName());
        holder.docterDeptDesc.setText(doctorDepartmentList.get(position).getDepartmentDesc());

        // Loading profile image
//        Glide.with(context).load("https://lh3.googleusercontent.com/-0i6pp4Dn-yY/AAAAAAAAAAI/AAAAAAAAAPc/TkZZ3pC0RZ0/s96-c/photo.jpg")
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(R.drawable.medical_symbol)
                + '/' + context.getResources().getResourceTypeName(R.drawable.medical_symbol) + '/' + context.getResources().getResourceEntryName(R.drawable.medical_symbol));
        //"android.resource://com.sarvan.medicineplus/drawable/medical_symbol\""
        Glide.with(context).load(imageUri)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransForm(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.departmentImageView);
    }

    /**
     * Get item Count
     *
     * @return total size
     */
    @Override
    public int getItemCount() {
        return doctorDepartmentList.size();
    }

    // DepartmentName View Holder Class Definition
    public class DepartmentNameViewHolder extends RecyclerView.ViewHolder {
        private TextView docterDeptName;
        private TextView docterDeptDesc;
        private ImageView departmentImageView;

        // Constructor
        public DepartmentNameViewHolder(View view) {
            super(view);
            docterDeptName = (TextView) view.findViewById(R.id.doctor_name_tv);
            docterDeptDesc = (TextView) view.findViewById(R.id.doctor_desc_tv);
            departmentImageView = (ImageView) view.findViewById(R.id.dept_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser == null) {
                        // Not signed in, launch the Sign In activity
                        context.startActivity(new Intent(context, SignInActivity.class));
                    } else if (Helper.isAdminUsers(firebaseUser.getUid())) {
                        String userId = firebaseUser.getUid();
                        Intent intent = new Intent(context, UsersListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("userId", userId);
                        intent.putExtra("department", doctorDepartmentList.get(getAdapterPosition()).getDepartmentName());
                        context.startActivity(intent);
                    } else {
                        String userId = firebaseUser.getUid();
                        Intent intent = new Intent(context, AskDoctorActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("userId", userId);
                        intent.putExtra("department", doctorDepartmentList.get(getAdapterPosition()).getDepartmentName());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
