package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Manager_menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_menu);
    }

    public void driverRegisterBtn(View view) {
        // Intent를 사용하여 다음 액티비티로 이동
        Intent intent = new Intent(this, ManagerReservation.class);
        startActivity(intent);
    }

    public void registerBtn(View view) {
        // Intent를 사용하여 다음 액티비티로 이동
        Intent intent = new Intent(this, ManagerRegister.class);
        startActivity(intent);
    }

    public void AproveBtn(View view) {
        // Intent를 사용하여 다음 액티비티로 이동
        Intent intent = new Intent(this, CheckActivity.class);
        startActivity(intent);
    }

    public void usersBtn(View view) {
        // Intent를 사용하여 다음 액티비티로 이동
        Intent intent = new Intent(this, UserInfomationActivity.class);
        startActivity(intent);
    }

    public void DriverBtn(View view) {
        // Intent를 사용하여 다음 액티비티로 이동
        Intent intent = new Intent(Manager_menu.this, DriverInfomationActivity.class);
        startActivity(intent);
    }

}

