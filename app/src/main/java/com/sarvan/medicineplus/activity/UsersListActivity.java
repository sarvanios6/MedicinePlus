package com.sarvan.medicineplus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sarvan.medicineplus.R;
import com.sarvan.medicineplus.adapter.UserListAdapter;
import com.sarvan.medicineplus.others.Helper;
import com.sarvan.medicineplus.realm.Users;
import com.sarvan.medicineplus.realm.UsersRealm;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by Sarvan on 29/12/16.
 */

public class UsersListActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private UsersRealm realmUser;
    private Realm realm;
    private RecyclerView recyclerViewUserList;
    private ArrayList<UsersRealm> userList;
    private String departmentName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        realmUser = new UsersRealm();
        realm = Realm.getDefaultInstance();
        Intent intent = getIntent();
        if (intent != null) {
            departmentName = intent.getStringExtra("department");
        }
        recyclerViewUserList = (RecyclerView) findViewById(R.id.recycler_user_list);
        ImageButton patientSmiley = (ImageButton) findViewById(R.id.user_patient_smiley);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewUserList.setLayoutManager(linearLayoutManager);
        userList = Helper.getAllUsers(departmentName);
        if (userList.size() > 0) {
            patientSmiley.setVisibility(View.INVISIBLE);
        } else {
            patientSmiley.setVisibility(View.VISIBLE);
        }
        UserListAdapter adapter = new UserListAdapter(this, userList,departmentName);
        recyclerViewUserList.setAdapter(adapter);
    }

    void addEventListener() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users users = dataSnapshot.getValue(Users.class);
                realmUser.setName(users.getName());
                realmUser.setEmail(users.getEmail());
                realmUser.setChannel(users.getChannel());
                realmUser.setPhotoUrl(users.getphotoUrl());
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(realmUser);
                realm.commitTransaction();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
