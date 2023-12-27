package com.example.test2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserInfomationActivity extends AppCompatActivity {
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:XE";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infomation);
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

    }

    public void searchBtn(View view){
        TextView userNameEt = findViewById(R.id.userNameEt);

        TextView driverTv7 = findViewById(R.id.driverTv7);
        TextView driverTv8 = findViewById(R.id.driverTv8);
        TextView driverTv9= findViewById(R.id.driverTv9);
        TextView driverTv10 = findViewById(R.id.driverTv10);
        TextView driverTv11 = findViewById(R.id.driverTv11);

        try{
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sql = "select * from users where name = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, userNameEt.getText().toString());
            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next() == true){


                driverTv7.setText(resultSet.getString(1));
                driverTv8.setText(resultSet.getString(8));
                driverTv9.setText(resultSet.getString(4));
                driverTv10.setText(resultSet.getString(7));
                driverTv11.setText(resultSet.getString(3));


                Toast myToast = Toast.makeText(this.getApplicationContext(),"조회 완료되었습니다.", Toast.LENGTH_SHORT);
                myToast.show();
            }else{
                Toast myToast = Toast.makeText(this.getApplicationContext(),"없는 고객 입니다.", Toast.LENGTH_SHORT);
                myToast.show();
            }

        }catch (Exception e){
            Toast myToast = Toast.makeText(this.getApplicationContext(),"조회 에러.", Toast.LENGTH_SHORT);
            myToast.show();
            e.printStackTrace();
        }
    }
}
