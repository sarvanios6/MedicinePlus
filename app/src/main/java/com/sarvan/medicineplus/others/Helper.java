package com.sarvan.medicineplus.others;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sarvan.medicineplus.R;
import com.sarvan.medicineplus.activity.SignInActivity;
import com.sarvan.medicineplus.adapter.CommentsAdapter;
import com.sarvan.medicineplus.realm.ChatMessage;
import com.sarvan.medicineplus.realm.CommentsModel;
import com.sarvan.medicineplus.realm.DoctorRealm;
import com.sarvan.medicineplus.realm.NewsModel;
import com.sarvan.medicineplus.realm.NewsRealm;
import com.sarvan.medicineplus.realm.UsersRealm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Helper Class use for some default methods
 */

public class Helper {

    /**
     * SignIN Dialogue
     *
     * @param context context
     */
    public static void signInDialog(final Context context) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            // Not signed in, launch the Sign In activity
            context.startActivity(new Intent(context, SignInActivity.class));
            return;
        }
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.sign_in_layout, null);
        final Dialog signInDialog = new Dialog(context);
        signInDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        signInDialog.setContentView(view);
        signInDialog.setCanceledOnTouchOutside(false);
        signInDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Get All Views
        final EditText inputEmail = (EditText) view.findViewById(R.id.input_email);
        final EditText inputPwd = (EditText) view.findViewById(R.id.input_password);
        Button loginButton = (Button) view.findViewById(R.id.btn_login);
        Button skipButton = (Button) view.findViewById(R.id.btn_skip);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInDialog.dismiss();
            }
        });
        // Set ClickListener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputEmail.getText().length() < 0 && inputPwd.getText().length() < 0) {
                    Toast.makeText(context, "Please enter your Email and Pwd", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Set Data
        signInDialog.show();
    }

    /**
     * ShowComment Dialogue
     *
     * @param context context
     */
    public static void showCommentDialog(final Context context, final CommentsAdapter adapter, final FirebaseUser mFirebaseUser) {
        FirebaseDatabase firebaseInstance = FirebaseDatabase.getInstance();
        final DatabaseReference mFirebaseDatabaseRef = firebaseInstance.getReference("Comments");
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.comments_dialog_layout, null);
        final Dialog showDialog = new Dialog(context);
        showDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        showDialog.setContentView(view);
        showDialog.setCanceledOnTouchOutside(false);
        showDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Get All Views
        final EditText commentEditText = (EditText) view.findViewById(R.id.enter_comments_edit_txt);
        final RelativeLayout editTextLayout = (RelativeLayout) view.findViewById(R.id.edit_comment_layout);
        final Button postCmtBtn = (Button) view.findViewById(R.id.post_comment_btn);
        commentEditText.setSelection(commentEditText.getText().toString().length());
        editTextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentEditText.requestFocus();
                commentEditText.setFocusableInTouchMode(true);
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(commentEditText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        postCmtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = commentEditText.getText().toString();
                if (text.length() > 0) {
                    String userID = null;
                    if (mFirebaseUser != null) {
                        userID = mFirebaseUser.getUid();
                        String timeStamps = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
                        CommentsModel commentsModel = new CommentsModel(text, timeStamps, mFirebaseUser.getDisplayName(), userID);
                        mFirebaseDatabaseRef.push().setValue(commentsModel);
                    }
                    mFirebaseDatabaseRef.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            adapter.clearComments();
                            for (DataSnapshot snapshots : dataSnapshot.getChildren()) {
                                CommentsModel commentsModel = snapshots.getValue(CommentsModel.class);
                                adapter.updateCommets(commentsModel);
                            }
                            adapter.notifyDataSetChanged();
                            showDialog.dismiss();
                            commentEditText.setText("");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.i("CommentsFragment", databaseError.getMessage());
                        }
                    });
                }
            }
        });
        // Set Data
        showDialog.show();
    }

    /**
     * This method is used to get the time when the post is updated.
     *
     * @return timeStamp Time difference current time and date and date of post.
     */
    @SuppressLint("SimpleDateFormat")
    public static String getChatTimeStamp(String timeStamp) {
        // Getting date and time from JSON
        TimeZone currentTimeZone = TimeZone.getDefault();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date utcToLocalDate = new Date();
        Date currentDate = new Date();
        Date localTime = new Date();
        String currentTime = dateFormat.format(new Date());
        String utcToLocalDateString = null;
        try {
            // Set Current Time Zone Format
            SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            localDateFormat.setTimeZone(currentTimeZone);
            utcToLocalDate = localDateFormat.parse(timeStamp);
            String utcToLocalString = localDateFormat.format(utcToLocalDate);
            // Set UTC time zone and convert to Local date
            SimpleDateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            localTime = utcDateFormat.parse(utcToLocalString);
            utcToLocalDateString = localDateFormat.format(utcToLocalDate);
            // Getting current date and time
            currentDate = dateFormat.parse(currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //in milliseconds
        if (currentDate != null && utcToLocalDate != null) {
            long timeDiff = currentDate.getTime() - utcToLocalDate.getTime();
            long diffDays = timeDiff / (24 * 60 * 60 * 1000);
            if (diffDays >= 2) {
                return new SimpleDateFormat("dd MMM yyyy hh:mm").format(localTime);
            } else if (diffDays >= 1) {
                String[] times = utcToLocalDateString.split(" ");
                String time = times[1];
                int lastIndex = time.lastIndexOf(":");
                String hMTime = time.substring(0, lastIndex);
                return "Yesterday " + hMTime;
            } else {
                String[] times = utcToLocalDateString.split(" ");
                String time = times[1];
                int lastIndex = time.lastIndexOf(":");
                String hMTime = time.substring(0, lastIndex);
                return "Today " + hMTime;
            }
        } else {
            return "";
        }
    }

    public static ArrayList<ChatMessage> getAllMessages(String departmentName) {
        ArrayList<ChatMessage> messages = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<ChatMessage> allMessages = realm.where(ChatMessage.class).contains("departmentName", departmentName).findAll();
        realm.commitTransaction();
        for (ChatMessage message : allMessages) {
            messages.add(message);
        }
        return messages;
    }

    // Get Admin User
    public static boolean isAdminUsers() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DoctorRealm> adminUserRealm = realm.where(DoctorRealm.class).findAll();
        if (null != adminUserRealm && adminUserRealm.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    // Get Admin User
    public static boolean isUsers(String userId) {
        Realm realm = Realm.getDefaultInstance();
        UsersRealm adminUserRealm = realm.where(UsersRealm.class).equalTo("channel", userId).findFirst();
        if (null != adminUserRealm) {
            return true;
        } else {
            return false;
        }
    }

    // Get All users
    public static ArrayList<UsersRealm> getAllUsers(String departmentName) {
        ArrayList<UsersRealm> users = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<UsersRealm> allUsers = realm.where(UsersRealm.class).equalTo("departmentName", departmentName).findAll();
        for (UsersRealm user : allUsers) {
            users.add(user);
        }
        return users;
    }

    /**
     * This method is used to get the time when the post is updated.
     *
     * @return timeStamp Time difference current time and date and date of post.
     */
    public static String getTimeStamp(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat millSecFormat = new SimpleDateFormat("HH:mm");
        Date currentDate = new Date();
        String currentTime = sdf.format(new Date());
        Date resultdate = new Date(timeStamp);
        // Getting current date and time
        try {
            currentDate = sdf.parse(currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (currentDate != null) {
            return "Today " + millSecFormat.format(resultdate);
        } else {
            return sdf.format(resultdate);
        }
    }

    /**
     * Get user Realm
     *
     * @return userData
     */
    public static DoctorRealm getAdminUser() {
        Realm realm = Realm.getDefaultInstance();
        return realm.where(DoctorRealm.class).findFirst();
    }

    public static ArrayList<NewsModel> getAllNewsFromRealm() {
        ArrayList<NewsModel> allNews = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NewsRealm> newsListRealm = realm.where(NewsRealm.class).findAll();
        for (NewsRealm model : newsListRealm) {
            NewsModel newsModle = new NewsModel(model.getId(), model.getDrName(), model.getNewsText(), model.getTime());
            allNews.add(newsModle);
        }
        return allNews;
    }

    /**
     * Clear data from Realm
     */
    public static void clearNewsFromRealm() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(NewsRealm.class).findAll().deleteAllFromRealm();
            }
        });
    }
}
