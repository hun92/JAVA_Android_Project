package com.example.projecttaxi;

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

public class Driver_info extends AppCompatActivity {

    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:xe";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;

    String D_Name;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_info);

        //보안정책
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        Intent intent = getIntent();
        this.D_Name = intent.getStringExtra("이름");

        TextView getName = findViewById(R.id.name);
        TextView getId = findViewById(R.id.id);
        TextView getPassword = findViewById(R.id.password);
        TextView getCar = findViewById(R.id.carNum);
        TextView getCard = findViewById(R.id.cardNum);
        TextView getPhone = findViewById(R.id.phone);


        try{
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            String sql = "select * from  DRIVER where name = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, D_Name);
            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next() == true ){
                getName.setText(resultSet.getString(1));
                getId.setText(resultSet.getString(2));
                getPassword.setText(resultSet.getString(3));
                getPhone.setText(resultSet.getString(9));
                getCar.setText(resultSet.getString(4));
                getCard.setText(resultSet.getString(5));

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void modifyBtn(View view){
        TextView getPassword = findViewById(R.id.password);
        TextView getCarNum = findViewById(R.id.carNum);
        TextView getPhone = findViewById(R.id.phone);

        Intent intent = getIntent();
        String id = intent.getStringExtra("아이디");

        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            String sql = "update DRIVER set password = ?, carnumber = ?, phone = ? where id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, getPassword.getText().toString());
            pstmt.setString(2,getCarNum.getText().toString());
            pstmt.setString(3,getPhone.getText().toString());
            pstmt.setString(4,id);
            pstmt.executeUpdate();
            Toast.makeText(getApplicationContext(),"수정완료", Toast.LENGTH_SHORT).show();

            Intent nextMenu = new Intent(Driver_info.this, Menu.class);
            nextMenu.putExtra("이름", D_Name);
            startActivity(nextMenu);



        }catch (Exception e){
            e.printStackTrace();
        }

    }
}