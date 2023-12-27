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
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:XE";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //보안정책
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
    }

    public void loginBtn(View view) {
        TextView et_name2 = findViewById(R.id.et_name2);
        TextView et_pass2 = findViewById(R.id.et_pass2);

        try{
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sql = "select * from manager where id = ? and password = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, et_name2.getText().toString());
            pstmt.setString(2,et_pass2.getText().toString());
            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next() == true){
                Toast myToast = Toast.makeText(this.getApplicationContext(),"로그인이 성공하였습니다.", Toast.LENGTH_SHORT);
                myToast.show();
                Intent intent = new Intent(this, Manager_menu.class);
                startActivity(intent);
            }else{
                Toast myToast = Toast.makeText(this.getApplicationContext(),"계정확인블가.", Toast.LENGTH_SHORT);
                myToast.show();
            }

        }catch (Exception e){
            Toast myToast = Toast.makeText(this.getApplicationContext(),"로그인 에러.", Toast.LENGTH_SHORT);
            myToast.show();
            e.printStackTrace();
        }
    }
}
