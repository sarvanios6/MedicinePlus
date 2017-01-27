package com.sarvan.medicineplus.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.sarvan.medicineplus.R;

/**
 * CommentsFragment class definition
 */

public class CommentsFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
//        final Dialog signInDialog = new Dialog(getActivity());
//        signInDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        signInDialog.setContentView(view);
//        signInDialog.setCanceledOnTouchOutside(false);
//        signInDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Get All Views
//        final EditText commentEditText = (EditText) view.findViewById(R.id.enter_comments_edit_txt);
//        final RelativeLayout editTextLayout = (RelativeLayout) view.findViewById(R.id.edit_comment_layout);
//        final Button postCmtBtn = (Button) view.findViewById(R.id.post_comment_btn);
//        commentEditText.setSelection(commentEditText.getText().toString().length());
//        editTextLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                commentEditText.requestFocus();
//                commentEditText.setFocusableInTouchMode(true);
//                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(commentEditText, InputMethodManager.SHOW_IMPLICIT);
//            }
//        });
//        postCmtBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                commentEditText.setText("");
//            }
//        });
        // Set Data
//        signInDialog.show();
        return view;
    }
}
