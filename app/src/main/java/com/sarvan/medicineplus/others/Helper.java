package com.sarvan.medicineplus.others;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sarvan.medicineplus.R;
import com.sarvan.medicineplus.activity.SignInActivity;
import com.sarvan.medicineplus.realm.ChatMessage;
import com.sarvan.medicineplus.realm.DoctorRealm;
import com.sarvan.medicineplus.realm.UsersRealm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Sarvan on 30/11/16.
 */

public class Helper {

    public static void stockInfoDialog(final Context context) {
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
                Toast.makeText(context, "Please enter your Email and Pwd", Toast.LENGTH_SHORT).show();
            }
        });
        // Set Data
        signInDialog.show();
    }

    /**
     * This method is used to get the time when the post is updated.
     *
     * @return timeStamp Time difference current time and date and date of post.
     */
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
    public static boolean isAdminUsers(String id) {
        Realm realm = Realm.getDefaultInstance();
        DoctorRealm adminUserRealm = realm.where(DoctorRealm.class).equalTo("channel",id).findFirst();
        if (null != adminUserRealm) {
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
}
