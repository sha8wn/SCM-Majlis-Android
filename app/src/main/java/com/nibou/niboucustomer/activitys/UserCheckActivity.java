package com.nibou.niboucustomer.activitys;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
    }

    private void openRegisterDialog() {
        Dialog dialog = new Dialog(this, R.style.FullScreenDialogStyle);
        dialog.setContentView(R.layout.dialog_start_register);

        TextView reset_pwd = dialog.findViewById(R.id.reset_pwd);
        ImageView back_arrow = dialog.findViewById(R.id.back_arrow);

        back_arrow.setOnClickListener(view -> {
            onBackPressed();
        });

        reset_pwd.setOnClickListener(view -> {
            Intent intent = new Intent(UserCheckActivity.this, SignupActivity.class);
            startActivity(intent);
        });
        dialog.show();
    }
}
