package com.nibou.niboucustomer.activitys;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nibou.niboucustomer.R;
import com.nibou.niboucustomer.databinding.ActivityHomeBinding;
import com.nibou.niboucustomer.fragments.EventFragment;
import com.nibou.niboucustomer.fragments.PromotionFragment;
import com.nibou.niboucustomer.fragments.SettingFragment;
import com.nibou.niboucustomer.utils.AppUtil;

public class HomeActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private long backPressedClickTime;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this);

        binding.bottomNavigationView.setSelectedItemId(R.id.action_event);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_event:
                if (getSupportFragmentManager().findFragmentByTag("eventFragment") == null)
                    AppUtil.setFragment(R.id.frameLayout_container, getSupportFragmentManager(), new EventFragment(), "eventFragment", false, true);
                break;

            case R.id.action_promotion:
                if (getSupportFragmentManager().findFragmentByTag("promotionFragment") == null)
                    AppUtil.setFragment(R.id.frameLayout_container, getSupportFragmentManager(), new PromotionFragment(), "promotionFragment", false, true);
                break;

            case R.id.action_setting:
                if (getSupportFragmentManager().findFragmentByTag("settingFragment") == null)
                    AppUtil.setFragment(R.id.frameLayout_container, getSupportFragmentManager(), new SettingFragment(), "settingFragment", false, true);
                break;

        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        checkForAppExit();
    }

    private void checkForAppExit() {
        if ((backPressedClickTime + 2000) > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, getResources().getString(R.string.press_back_message), Toast.LENGTH_SHORT).show();
        }
        backPressedClickTime = System.currentTimeMillis();
    }
}
