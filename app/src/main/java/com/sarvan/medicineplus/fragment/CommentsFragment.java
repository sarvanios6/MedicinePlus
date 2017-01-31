package com.sarvan.medicineplus.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sarvan.medicineplus.R;
import com.sarvan.medicineplus.adapter.CommentsAdapter;
import com.sarvan.medicineplus.others.Helper;
import com.sarvan.medicineplus.realm.CommentsModel;

import java.util.ArrayList;

/**
 * CommentsFragment class definition
 */

public class CommentsFragment extends DialogFragment {
    private ArrayList<CommentsModel> commentsList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseInstance;
    private CommentsAdapter commentsAdapter;
    private RecyclerView commentRV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        commentRV = (RecyclerView) view.findViewById(R.id.commets_recycler_view);
        Button commentButton = (Button) view.findViewById(R.id.post_comment_btn);
        firebaseInstance = FirebaseDatabase.getInstance();
        databaseReference = firebaseInstance.getReference("Comments");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        commentRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentsUpdate();
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.showCommentDialog(getActivity(), commentsAdapter, firebaseUser);
            }
        });
        return view;
    }

    private void commentsUpdate() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                commentsList.clear();
                for (DataSnapshot snapshots : dataSnapshot.getChildren()) {
                    CommentsModel commentsModel = snapshots.getValue(CommentsModel.class);
                    commentsList.add(commentsModel);
                }
                dialog.dismiss();
                commentsAdapter = new CommentsAdapter(getActivity(), commentsList);
                commentRV.setAdapter(commentsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Comments Fragment", databaseError.getMessage());
            }
        });
    }
}
