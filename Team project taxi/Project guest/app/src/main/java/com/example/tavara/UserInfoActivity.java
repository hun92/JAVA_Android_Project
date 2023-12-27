package com.example.tavara;

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

public class UserInfoActivity extends AppCompatActivity {
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:xe";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        Intent intent = getIntent();
        String id = intent.getStringExtra("아이디");

        TextView getName = findViewById(R.id.name);
        TextView getId = findViewById(R.id.id);
        TextView getPassword = findViewById(R.id.password);
        TextView getBirth = findViewById(R.id.birth);
        TextView getCardNum = findViewById(R.id.cardNum);
        TextView getPhone = findViewById(R.id.phone);
        TextView getApprove = findViewById(R.id.approve);

        try{
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            String sql = "select * from users where id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, id);
            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next() == true ){
                getName.setText(resultSet.getString(1));
                getId.setText(resultSet.getString(8));
                getPassword.setText(resultSet.getString(2));
                getBirth.setText(resultSet.getString(4));
                getCardNum.setText(resultSet.getString(3));
                getPhone.setText(resultSet.getString(7));
                if(resultSet.getString(5).toString().equals("0")){
                    getApprove.setText("고객미등록");
                }else{
                    getApprove.setText("고객등록완료");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void modifyBtn(View view){
        TextView getPassword = findViewById(R.id.password);
        TextView getCardNum = findViewById(R.id.cardNum);
        TextView getPhone = findViewById(R.id.phone);

        Intent intent = getIntent();
        String id = intent.getStringExtra("아이디");

        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            String sql = "update users set password = ?, card = ?, phone = ? where id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, getPassword.getText().toString());
            pstmt.setString(2,getCardNum.getText().toString());
            pstmt.setString(3,getPhone.getText().toString());
            pstmt.setString(4,id);
            pstmt.executeUpdate();
            Toast.makeText(getApplicationContext(),"수정완료",
                    Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}