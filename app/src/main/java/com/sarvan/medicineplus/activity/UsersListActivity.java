package com.sarvan.medicineplus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sarvan.medicineplus.R;
import com.sarvan.medicineplus.adapter.UserListAdapter;
import com.sarvan.medicineplus.others.Helper;
import com.sarvan.medicineplus.realm.UsersRealm;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by Sarvan on 29/12/16.
 */

public class UsersListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewUserList;
    private ArrayList<UsersRealm> userList;
    private String departmentName;
    private FirebaseDatabase firebaseInstance;
    private DatabaseReference mFirebaseDatabaseRef;
    private Realm realm;
    private UsersRealm usersRealm;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private UserListAdapter adapter;
    public static final String MESSAGES_CHILD = "messages";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Intent intent = getIntent();
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        if (intent != null) {
            departmentName = intent.getStringExtra("department");
        }
        recyclerViewUserList = (RecyclerView) findViewById(R.id.recycler_user_list);
        ImageButton patientSmiley = (ImageButton) findViewById(R.id.user_patient_smiley);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewUserList.setLayoutManager(linearLayoutManager);
        userList = Helper.getAllUsers(departmentName);
//        if (userList.size() < 0) {
//            patientSmiley.setVisibility(View.INVISIBLE);
//        } else {
//            patientSmiley.setVisibility(View.VISIBLE);
//        }
        patientSmiley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.deleteAll();
                    }
                });
            }
        });
        adapter = new UserListAdapter(this, userList, departmentName);
        recyclerViewUserList.setAdapter(adapter);
        departmentUsers();
    }

    private void departmentUsers() {
        // Initialize Firebase Auth and database
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        String mUserName = mFirebaseUser.getDisplayName();
        String mUserID = mUserName + "_" + mFirebaseUser.getUid();
        firebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabaseRef = firebaseInstance.getReference(departmentName);
        mFirebaseDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    String usersWithMessage = noteDataSnapshot.getKey();
                    String name[] = usersWithMessage.split("_");
                    usersRealm = new UsersRealm(name[1], name[0],departmentName);
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(usersRealm);
                    realm.commitTransaction();
                }
                adapter.updateList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("UserListActivity", databaseError.getMessage());
            }
        });
    }
}
