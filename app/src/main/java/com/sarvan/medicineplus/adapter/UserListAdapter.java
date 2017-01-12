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
import com.sarvan.medicineplus.R;
import com.sarvan.medicineplus.activity.AskDoctorActivity;
import com.sarvan.medicineplus.activity.SignInActivity;
import com.sarvan.medicineplus.realm.UsersRealm;

import java.util.ArrayList;

/**
 * DepartmentListAdapter Class Definition
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    private Context context;
    private ArrayList<UsersRealm> userList;
    private String departmentName;

    public UserListAdapter(Context context, ArrayList<UsersRealm> userList, String departmentName) {
        this.userList = userList;
        this.context = context;
        this.departmentName = departmentName;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_name_list_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.userName.setText(userList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView userName;

        public UserViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.user_name_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseUser == null) {
                        // Not signed in, launch the Sign In activity
                        context.startActivity(new Intent(context, SignInActivity.class));
                    } else {
                        String userId = firebaseUser.getUid();
                        String currentUser = userList.get(getAdapterPosition()).getName();
                        Intent intent = new Intent(context, AskDoctorActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("userId", userId);
                        intent.putExtra("currentUser", currentUser);
                        intent.putExtra("departmentName", departmentName);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
