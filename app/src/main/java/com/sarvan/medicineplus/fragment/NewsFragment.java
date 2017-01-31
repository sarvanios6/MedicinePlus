package com.sarvan.medicineplus.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sarvan.medicineplus.R;
import com.sarvan.medicineplus.adapter.NewsAdapter;
import com.sarvan.medicineplus.others.Helper;
import com.sarvan.medicineplus.realm.DoctorRealm;
import com.sarvan.medicineplus.realm.NewsModel;
import com.sarvan.medicineplus.realm.NewsRealm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;

/**
 * NewsFragment Class definition
 */
public class NewsFragment extends Fragment {
    // Database ref
    private DatabaseReference databaseReference;
    // NewsStringList
    private ArrayList<NewsModel> newsStringList = new ArrayList<>();
    // RecyclerView
    private RecyclerView newsRecyclerView;
    // newsPost layout
    private RelativeLayout newsPostLayout;
    // NewsAdapter
    private NewsAdapter newsadapter;
    // Realm DB
    private Realm realm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        realm = Realm.getDefaultInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("News");
        newsRecyclerView = (RecyclerView) view.findViewById(R.id.news_recycler_view);
        newsPostLayout = (RelativeLayout) view.findViewById(R.id.news_post_layout);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 1);
        newsRecyclerView.setLayoutManager(linearLayoutManager);
        newsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        DoctorRealm doctorRealm = Helper.getAdminUser();
        if (doctorRealm != null) {
            adminUser(view, doctorRealm.getName());
        }
        getNewsData();
        return view;
    }

    // GetNews Data from Firebase
    private void getNewsData() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Helper.clearNewsFromRealm();
                for (DataSnapshot snapshots : dataSnapshot.getChildren()) {
                    NewsModel newsModel = snapshots.getValue(NewsModel.class);
                    NewsRealm realmNews = new NewsRealm(newsModel.getId(), newsModel.getDrName(), newsModel.getNewsText(), newsModel.getTime());
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(realmNews);
                    realm.commitTransaction();
                }
                dialog.dismiss();
                newsStringList = Helper.getAllNewsFromRealm();
                newsadapter = new NewsAdapter(getActivity(), newsStringList,databaseReference);
                newsRecyclerView.setAdapter(newsadapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Comments Fragment", databaseError.getMessage());
            }
        });
    }

    /**
     * Show Admin user Layout
     */
    private void adminUser(final View view, final String name) {
        newsPostLayout.setVisibility(View.VISIBLE);
        final EditText newsEditText = (EditText) view.findViewById(R.id.news_edit_txt);
        Button postButton = (Button) view.findViewById(R.id.news_post_btn);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newsEditText.getText().toString();
                if (text.length() > 0) {
                    newsEditText.setText("");
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
                    String newsId = databaseReference.push().getKey();
                    final NewsModel newsModel = new NewsModel(newsId, name, text, timeStamp);
                    databaseReference.push().setValue(newsModel);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Helper.clearNewsFromRealm();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                NewsModel newsModel1 = snapshot.getValue(NewsModel.class);
                                NewsRealm realmNews = new NewsRealm(newsModel1.getId(), newsModel1.getDrName(), newsModel1.getNewsText(), newsModel1.getTime());
                                realm.beginTransaction();
                                realm.copyToRealmOrUpdate(realmNews);
                                realm.commitTransaction();
                                newsadapter.updateNewslist();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.i("News Fragment", databaseError.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "Please enter text", Toast.LENGTH_SHORT).show();
                }
                // Hide Keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }
}
