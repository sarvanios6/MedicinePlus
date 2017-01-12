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
import com.sarvan.medicineplus.realm.Users;
import com.sarvan.medicineplus.realm.UsersRealm;

import java.util.ArrayList;

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

    public static ArrayList<UsersRealm> getAllUsers(String departmentName) {
        ArrayList<UsersRealm> users = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<UsersRealm> allUsers = realm.where(UsersRealm.class).findAll();
        realm.commitTransaction();
        for (UsersRealm user : allUsers) {
            users.add(user);
        }
        return users;
    }
}
