package com.jeongwoo.johnc.homeworkreminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddHwActivity extends AppCompatActivity {
    public Context mContext = this;
    LayoutInflater inflater;
    public static String TAG = "TAG";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_for_new_hw);

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final Button setDateBtn = findViewById(R.id.dateInp);
        setDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = View.inflate(mContext, R.layout.date_time_picker, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();

                dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                        TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                        Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth(),
                                timePicker.getCurrentHour(),
                                timePicker.getCurrentMinute());

                        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm");
                        String dateString = formatter.format(calendar.getTimeInMillis());
                        alertDialog.dismiss();

                        setDateBtn.setText(dateString);
                        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
                        calendarView.setDate(calendar.getTimeInMillis());
                        calendarView.setFocusable(false);
                    }});
                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });

        final Button submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                SharedPreferences pref = mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();

                EditText nameEdit = findViewById(R.id.nameInp);
                EditText descEdit = findViewById(R.id.descInp);

                String[] arr = {nameEdit.getText().toString(), descEdit.getText().toString()
                        ,setDateBtn.getText().toString()};

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < arr.length; i++) {
                    sb.append(arr[i]).append(",");
                }
                editor.putString(nameEdit.getText().toString() + setDateBtn.getText().toString(), sb.toString());
                editor.commit();

                setAlarm(nameEdit.getText().toString(), setDateBtn.getText().toString(), setDateBtn.getText().toString());

                startActivity(intent);
            }
        });
    }

    private void setAlarm(String name, String desc, String date) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmService.class);
        String[] valArr = {name, desc, date};
        intent.putExtra("key", valArr);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        assert alarmManager != null;
        Date d = null;
        try {
            d = new SimpleDateFormat("MM/dd/yyyy hh:mm").parse(date);
            Log.d("Log Message", d.getTime() + "");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP, d.getTime(), pendingIntent);
        Toast.makeText(this, "We will remind you 12 hours before the assignment is due!", Toast.LENGTH_LONG).show();
    }
}
