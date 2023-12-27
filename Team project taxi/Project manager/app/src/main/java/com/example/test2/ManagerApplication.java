package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ManagerApplication extends AppCompatActivity {
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:XE";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_application);

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        Intent intent = getIntent();
        String id = intent.getStringExtra("고객아이디");

        TextView textview58 = findViewById(R.id.textView58);
        TextView textview59 = findViewById(R.id.textView59);
        TextView textview60 = findViewById(R.id.textView60);
        TextView textview61 = findViewById(R.id.textView61);
        TextView textview62 = findViewById(R.id.textView62);

        try{
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sql = "select * from users where id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, id);
            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next()== true){
                textview58.setText(resultSet.getString(8));
                textview59.setText(resultSet.getString(1));
                textview60.setText(resultSet.getString(4));
                textview61.setText(resultSet.getString(7));
                textview62.setText(resultSet.getString(3));

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void checkBtn(View view){
        TextView textview58 = findViewById(R.id.textView58);

        try{
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sql = "update users set approval = 1 where id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1,textview58.getText().toString());
            pstmt.executeUpdate();
            Toast.makeText(getApplicationContext(),"고객 등록완료",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ManagerApplication.this, Manager_menu.class);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteBtn(View view){
        TextView textview58 = findViewById(R.id.textView58);

        try{
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sql = "delete from users where id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1,textview58.getText().toString());
            pstmt.executeUpdate();
            Toast.makeText(getApplicationContext(),"고객 등록거절",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ManagerApplication.this, Manager_menu.class);
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}