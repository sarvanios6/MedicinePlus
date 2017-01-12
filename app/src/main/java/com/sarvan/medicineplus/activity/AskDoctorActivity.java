package com.sarvan.medicineplus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Indexables;
import com.google.firebase.appindexing.builders.PersonBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.sarvan.medicineplus.R;
import com.sarvan.medicineplus.realm.ChatMessage;
import com.sarvan.medicineplus.realm.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

/**
 * AskDoctorActivity Class Definition
 */

public class AskDoctorActivity extends AppCompatActivity {
    private RecyclerView recyclerViewChat;
    private ArrayList<ChatMessage> messagesList;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> conversationAdapter;
    private String departmentName = "";
    private String adminUser = "";
    private ChatMessage chatMessage;
    private Message message;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private String currentUser = "";
    private String mUserName = "anonymous";
    private Realm realm;
    private Button sendButton;
    private EditText messageEditText;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    public static final String MESSAGES_CHILD = "messages";
    private static final String MESSAGE_URL = "http://friendlychat.firebase.google.com/message/";
    private static final String MESSAGE_SENT_EVENT = "message_sent";

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView messengerTextView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.chat_message_tv);
            messengerTextView = (TextView) itemView.findViewById(R.id.chat_messager_name);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent intent = getIntent();
//        if (intent != null) {
//            String userId = intent.getStringExtra("userId");
//            currentUser = intent.getStringExtra("currentUser") + "_" + userId;
//            departmentName = intent.getStringExtra("department");
//            adminUser = intent.getStringExtra("departmentName");
//        }

        setContentView(R.layout.activity_ask_doctor);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        realm = Realm.getDefaultInstance();
        message = new Message();
        messageEditText = (EditText) findViewById(R.id.chat_msg_edit_text);
        sendButton = (Button) findViewById(R.id.chatSendButton);
        ImageButton doctorSmiley = (ImageButton) findViewById(R.id.doctor_smiley);
        TextView departmentTv = (TextView) findViewById(R.id.chat_title_tv);
        departmentTv.setText(departmentName);
        sendButton.setClickable(true);
        recyclerViewChat = (RecyclerView) findViewById(R.id.recycler_view_ask_doctor);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
        // Testing
        conversationAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                Message.class,
                R.layout.chat_item,
                MessageViewHolder.class,
                databaseReference.child(MESSAGES_CHILD)) {
            @Override
            protected Message parseSnapshot(DataSnapshot snapshot) {
                Message message = super.parseSnapshot(snapshot);
                if (message != null) {
                    message.setId(snapshot.getKey());
                }
                return message;
            }

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Message message, int position) {
                viewHolder.messageTextView.setText(message.getMessage());
                viewHolder.messengerTextView.setText(message.getMessagerName());
//                if (message.getPhotoUrl() == null) {
//                    viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(AskDoctorActivity.this,
//                            R.drawable.ic_account_circle_black_36dp));
//                } else {
//                    Glide.with(MainActivity.this)
//                            .load(message.getPhotoUrl())
//                            .into(viewHolder.messengerImageView);
//                }

                // write this message to the on-device index
                FirebaseAppIndex.getInstance().update(getMessageIndexable(message));

                // log a view action on it
                FirebaseUserActions.getInstance().end(getMessageViewAction(message));
            }
        };
        conversationAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = conversationAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
//                 If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
//                 to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    recyclerViewChat.scrollToPosition(positionStart);
                }
            }
        });
        recyclerViewChat.setLayoutManager(mLinearLayoutManager);
        recyclerViewChat.setAdapter(conversationAdapter);
// Initialize Firebase Measurement.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Initialize Firebase Remote Config.
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // Define Firebase Remote Config Settings.
        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(true)
                        .build();

        // Define default config values. Defaults are used when fetched config values are not
        // available. Eg: if an error occurred fetching values from the server.
        Map<String, Object> defaultConfigMap = new HashMap<>();
        defaultConfigMap.put("friendly_msg_length", 10L);

        // Apply config settings and default values.
        mFirebaseRemoteConfig.setConfigSettings(firebaseRemoteConfigSettings);
        mFirebaseRemoteConfig.setDefaults(defaultConfigMap);

        // Fetch remote config.
        fetchConfig();
//        messageEditText.setFilters(new InputFilter[]{
//                new InputFilter.LengthFilter(mSharedPreferences
//                        .getInt(CodelabPreferences.FRIENDLY_MSG_LENGTH, DEFAULT_MSG_LENGTH_LIMIT))
//        });
//        mMessageEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.toString().trim().length() > 0) {
//                    mSendButton.setEnabled(true);
//                } else {
//                    mSendButton.setEnabled(false);
//                }
//            }

//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//        conversationAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                super.onItemRangeInserted(positionStart, itemCount);
//                int friendlyMessageCount = conversationAdapter.getItemCount();
//                int lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition();
////                 If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
////                 to the bottom of the list to show the newly added message.
//                if (lastVisiblePosition == -1 ||
//                        (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
//                    mMessageRecyclerView.scrollToPosition(positionStart);
//                }
//            }
//        });
//        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
//        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
//        messagesList = Helper.getAllMessages(departmentName);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        linearLayoutManager.setStackFromEnd(true);
//        recyclerViewChat.setLayoutManager(linearLayoutManager);
//        recyclerViewChat.setItemAnimator(new DefaultItemAnimator());
//        conversationAdapter = new ConversationAdapter(this, messagesList, departmentName);
//        recyclerViewChat.setAdapter(conversationAdapter);
//        recyclerViewChat.smoothScrollToPosition(conversationAdapter.getItemCount());
//
//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (messageEditText.getText().length() > 0 && !TextUtils.isEmpty(adminUser)) {
//                    message.setMessage(messageEditText.getText().toString());
//                    message.setMessageDate(System.currentTimeMillis());
//                    message.setMessagerName(firebaseUser.getDisplayName());
//                    message.setDepartmentName(departmentName);
//                    databaseReference.child("Medicine_plus").child(adminUser).child("message").child(currentUser).setValue(message);
//                    messageEditText.setText("");
//                    addMesssageChangeListener();
//                } else if (messageEditText.getText().length() > 0) {
//                    currentUser = firebaseUser.getDisplayName() + "_" + firebaseUser.getUid();
//                    message.setMessage(messageEditText.getText().toString());
//                    message.setMessageDate(System.currentTimeMillis());
//                    message.setMessagerName(firebaseUser.getDisplayName());
//                    message.setDepartmentName(departmentName);
//                    databaseReference.child("Medicine_plus").child(departmentName).child("message").child(currentUser).setValue(message);
//                    messageEditText.setText("");
//                    addMesssageChangeListener();
//                }
//            }
//        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message(messageEditText.getText().toString(), mUserName);
                databaseReference.child(MESSAGES_CHILD).push().setValue(message);
                messageEditText.setText("");
                mFirebaseAnalytics.logEvent(MESSAGE_SENT_EVENT, null);
            }
        });

        doctorSmiley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.deleteAll();
                    }
                });
//                conversationAdapter.updateMessage();
                Intent homeIntent = new Intent(AskDoctorActivity.this, HomeActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();
            }
        });

    }

    /**
     * User data change listener
     */
//    private void addMesssageChangeListener() {
//        // User data change listener
//        databaseReference.child("Medicine_plus").child(departmentName).child(currentUser).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                message = dataSnapshot.getValue(Message.class);
//                if (message.getMessage().length() > 0) {
//                    chatMessage.setMessagerName(message.getMessage());
//                    chatMessage.setMessagerName(message.getMessagerName());
//                    chatMessage.setMessageDate(message.getMessageDate());
//                    Realm realm = Realm.getDefaultInstance();
//                    realm.beginTransaction();
//                    realm.copyToRealm(chatMessage);
//                    realm.commitTransaction();
//                    conversationAdapter.updateMessage();
//                    recyclerViewChat.smoothScrollToPosition(conversationAdapter.getItemCount());
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("AskDoctorActivity", databaseError.getMessage());
//            }
//        });
//    }
    private Action getMessageViewAction(Message friendlyMessage) {
        return new Action.Builder(Action.Builder.VIEW_ACTION)
                .setObject(friendlyMessage.getMessagerName(), MESSAGE_URL.concat(friendlyMessage.getId()))
                .setMetadata(new Action.Metadata.Builder().setUpload(false))
                .build();
    }

    private Indexable getMessageIndexable(Message friendlyMessage) {
        PersonBuilder sender = Indexables.personBuilder()
                .setIsSelf(mUserName == friendlyMessage.getMessagerName())
                .setName(friendlyMessage.getMessage())
                .setUrl(MESSAGE_URL.concat(friendlyMessage.getId() + "/sender"));

        PersonBuilder recipient = Indexables.personBuilder()
                .setName(mUserName)
                .setUrl(MESSAGE_URL.concat(friendlyMessage.getId() + "/recipient"));

        Indexable messageToIndex = Indexables.messageBuilder()
                .setName(friendlyMessage.getMessage())
                .setUrl(MESSAGE_URL.concat(friendlyMessage.getId()))
                .setSender(sender)
                .setRecipient(recipient)
                .build();

        return messageToIndex;
    }

    // Fetch the config to determine the allowed length of messages.
    public void fetchConfig() {
        long cacheExpiration = 3600; // 1 hour in seconds
        // If developer mode is enabled reduce cacheExpiration to 0 so that each fetch goes to the
        // server. This should not be used in release builds.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Make the fetched config available via FirebaseRemoteConfig get<type> calls.
                        mFirebaseRemoteConfig.activateFetched();
                        applyRetrievedLengthLimit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // There has been an error fetching the config
                        Log.w("AskDoctorActivity", "Error fetching config: " + e.getMessage());
                        applyRetrievedLengthLimit();
                    }
                });
    }

    /**
     * Apply retrieved length limit to edit text field. This result may be fresh from the server or it may be from
     * cached values.
     */
    private void applyRetrievedLengthLimit() {
        Long friendly_msg_length = mFirebaseRemoteConfig.getLong("friendly_msg_length");
        messageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(friendly_msg_length.intValue())});
        Log.d("AskDoctorActivity", "FML is: " + friendly_msg_length);
    }
}
