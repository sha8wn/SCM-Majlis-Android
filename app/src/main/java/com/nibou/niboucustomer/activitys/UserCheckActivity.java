package com.nibou.niboucustomer.activitys;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.databinding.ActivityUserCheckBinding;

public class UserCheckActivity extends BaseActivity {
    private ActivityUserCheckBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_check);
        binding.oldUser.setOnClickListener(v -> {
            Intent intent = new Intent(UserCheckActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        binding.newUser.setOnClickListener(v -> {
            openRegisterDialog();
        });

        binding.tvFan.setOnClickListener(v -> {
            Intent intent = new Intent(UserCheckActivity.this, PastEventActivity.class);
            startActivity(intent);
        });
    }

    private void openRegisterDialog() {
        Dialog dialog = new Dialog(this, R.style.FullScreenDialogStyle);
        dialog.setContentView(R.layout.dialog_start_register);

        TextView startRegister = dialog.findViewById(R.id.startRegister);
        TextView tvDesc = dialog.findViewById(R.id.tvDesc);
        ImageView back_arrow = dialog.findViewById(R.id.back_arrow);

        back_arrow.setOnClickListener(view -> {
            dialog.dismiss();
        });

        String text = getString(R.string.membership);
        String text1 = "Completing this form not guarantee an SCM membersip. You will be notified if your application is successful. Please read our" + " ";
        String text2 = "privacy policy";
        String text3 = "and";
        String text4 = "terms & conditions";
        String text5 = "to know more about our selection process";

        SpannableString spannableString = new SpannableString(text1 + text2 + text3 + text4 + text5);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                // listen click here
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(Color.WHITE);
            }
        };

        spannableString.setSpan(clickableSpan, text1.length(), text1.length() + text2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvDesc.setText(spannableString);
        tvDesc.setMovementMethod(LinkMovementMethod.getInstance());
        tvDesc.setHighlightColor(Color.WHITE);

        startRegister.setOnClickListener(view -> {
            Intent intent = new Intent(UserCheckActivity.this, SignupActivity.class);
            startActivity(intent);
        });
        dialog.show();
    }
}
