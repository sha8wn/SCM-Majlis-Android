package com.nibou.niboucustomer.activitys;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.databinding.ActivityUserCheckBinding;
import com.nibou.niboucustomer.utils.AppUtil;
import com.nibou.niboucustomer.utils.CustomTypefaceSpan;

public class UserCheckActivity extends BaseActivity {
    private ActivityUserCheckBinding binding;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_check);
        context = this;
        binding.oldUser.setOnClickListener(v -> {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
        });

        binding.newUser.setOnClickListener(v -> {
            openRegisterDialog();
        });
    }

    private void openRegisterDialog() {
        final Dialog dialog = new Dialog(context, R.style.FullScreenDialogStyle);
        dialog.setContentView(R.layout.dialog_start_register);
        TextView reset_pwd = dialog.findViewById(R.id.reset_pwd);
        ImageView back_arrow = dialog.findViewById(R.id.back_arrow);
        TextView des = dialog.findViewById(R.id.tvDesc);


        Typeface boldFont = ResourcesCompat.getFont(context, R.font.poppins_regular);
        SpannableStringBuilder ss = new SpannableStringBuilder(getString(R.string.membership));
        ss.setSpan(privacyclick, 125, 140, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new UnderlineSpan(), 125, 140, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new CustomTypefaceSpan("", boldFont), 125, 140, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), 125, 140, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        ss.setSpan(termiclick, 144, 163, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new UnderlineSpan(), 144, 163, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new CustomTypefaceSpan("", boldFont), 144, 163, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), 144, 163, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        des.setText(ss, TextView.BufferType.SPANNABLE);
        des.setMovementMethod(LinkMovementMethod.getInstance());
        des.setHighlightColor(Color.TRANSPARENT);

        back_arrow.setOnClickListener(view -> {
            dialog.dismiss();
        });
        reset_pwd.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(UserCheckActivity.this, SignupActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        dialog.show();
    }

    ClickableSpan privacyclick = new ClickableSpan() {
        @Override
        public void onClick(View textView) {
            AppUtil.hideKeyBoard(context);
            Intent intent = new Intent(context, PrivacyPolicyActivity.class);
            intent.putExtra("type", "privacy");
            startActivity(intent);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    };

    ClickableSpan termiclick = new ClickableSpan() {
        @Override
        public void onClick(View textView) {
            AppUtil.hideKeyBoard(context);
            Intent intent = new Intent(context, PrivacyPolicyActivity.class);
            intent.putExtra("type", "terms");
            startActivity(intent);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    };
}
