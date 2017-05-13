package com.artoymdmitriev.bseuschedulemk3.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.artoymdmitriev.bseuschedulemk3.R;
import com.artoymdmitriev.bseuschedulemk3.logic.DBHelper;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();*/
        setContentView(R.layout.activity_start);


        DBHelper dbHelper = new DBHelper(this);
        if(dbHelper.getCustomScheduleInfoCount() > 0) {
            Intent i = new Intent(this, FullSchedule.class);
            startActivity(i);

        } else {
            if(checkNetworkConnection()) {
                Intent i = new Intent(this, SelectFacultyActivity.class);
                i.setAction("StartActivity");
                startActivity(i);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Ошибка!")
                        .setMessage("Похоже, что Ваш телефон не подключен к Интернету. Это требуется, чтобы" +
                                " скачать акутальное расписание занятий")
                        .setPositiveButton("Обязуюсь включить Интернет", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        })

                        .setIcon(R.drawable.error)
                        .show();
            }
        }
    }

    private boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
