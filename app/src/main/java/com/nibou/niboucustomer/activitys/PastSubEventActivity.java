//package com.nibou.niboucustomer.activitys;
//
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//
//import com.nibou.niboucustomer.R;
//import com.nibou.niboucustomer.adapters.PastEventListAdapter;
//import com.nibou.niboucustomer.databinding.LayoutPastEventsBinding;
//import com.nibou.niboucustomer.models.PreviousExpertModel;
//
//public class PastSubEventActivity extends AppCompatActivity {
//    private LayoutPastEventsBinding binding;
//    private PastEventListAdapter mListAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        init();
//    }
//
//    private void init() {
//        binding = DataBindingUtil.setContentView(this, R.layout.layout_recyclerview);
//
//        binding.rvEvents.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        mListAdapter = new PastEventListAdapter(this, new PreviousExpertModel());
//        binding.rvEvents.setAdapter(mListAdapter);
//
//    }
//}
