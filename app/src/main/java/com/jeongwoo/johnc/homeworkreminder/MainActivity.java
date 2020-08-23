package com.jeongwoo.johnc.homeworkreminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Map;

import static com.jeongwoo.johnc.homeworkreminder.AlarmService.CHANNEL_ID;

public class MainActivity extends AppCompatActivity {
    public Context mContext = this;
    public LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));

        gettingFromSPref();
        Button addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddHwActivity.class);
                startActivity(intent);
            }
        });

        final Button completeBtn = findViewById(R.id.completeBtn);
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((View) (completeBtn.getParent()).getParent().get

                ((ViewGroup) (completeBtn.getParent()).getParent().getParent()).removeView((ViewGroup) (completeBtn.getParent().getParent()));
            }
        });
    }

    public void addToHWList(String name, String desc, String dueDate) {
        View parent = inflater.inflate(R.layout.each_homework_scroll, null);
        LinearLayout container = findViewById(R.id.hwListLinear);

        TextView nameHWText = parent.findViewById(R.id.nameOfHW);
        TextView descOfHWText = parent.findViewById(R.id.description);
        TextView dueDateText = parent.findViewById(R.id.dueDate);

        nameHWText.setText(name);
        descOfHWText.setText(desc);
        dueDateText.setText(dueDate);

        parent.setLayoutParams(new LinearLayout.LayoutParams(900, 300));

        container.addView(parent);
    }

    public void gettingFromSPref() {
        SharedPreferences pref = mContext.getSharedPreferences(AddHwActivity.TAG, Context.MODE_PRIVATE);

        Map<String,?> keys = pref.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            String[] arr = entry.getValue().toString().split(",");
            addToHWList(arr[0], arr[1], arr[2]);
        }
    }

    public void deletingFromSPref(String name, String desc) {
        SharedPreferences pref = mContext.getSharedPreferences(AddHwActivity.TAG, Context.MODE_PRIVATE);
        pref.edit().remove(name + desc);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
