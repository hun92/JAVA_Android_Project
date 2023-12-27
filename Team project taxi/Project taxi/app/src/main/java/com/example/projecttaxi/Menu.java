package com.example.projecttaxi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


// 매뉴
public class Menu extends AppCompatActivity {

    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:xe";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;

    String D_Name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        this.D_Name = intent.getStringExtra("이름");
        String getId = intent.getStringExtra("아이디");

        //보안정책
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        ImageView bt_reservation = findViewById(R.id.bt_reservation);
        ImageView bt_operation = findViewById(R.id.bt_operation);

        try{
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sql = "select * from reservation where drivername = ? and approval = 2";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1,D_Name);
            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next() == true){
                bt_reservation.setEnabled(false);
                Toast mytoast = Toast.makeText(getApplicationContext(), "배차를 받을수 없습니다.", Toast.LENGTH_LONG);
                mytoast.show();
            } else {
                bt_operation.setEnabled(false);
                Toast mytoast = Toast.makeText(getApplicationContext(), "운행중인 배차가 없습니다.", Toast.LENGTH_LONG);
                mytoast.show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sql = "select * from reservation where drivername = ? and approval = 1";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1,D_Name);
            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next() == true){
                bt_operation.setEnabled(false);
                Toast mytoast = Toast.makeText(getApplicationContext(), "운행 중입니다. 배차를 확인해주세요", Toast.LENGTH_LONG);
                mytoast.show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //배차 확인버튼
    public void onClickedRegister  (View view){
        Intent intent  = getIntent();
        String getId = intent.getStringExtra("아이디");
        Intent nextMenu = new Intent( Menu.this, Current_List.class);
        nextMenu.putExtra("이름", D_Name);
        nextMenu.putExtra("아이디", getId);
        startActivity(nextMenu);

    }
    //운행중
    public void onClickedOperation(View view){
        Intent intent  = getIntent();
        String getId = intent.getStringExtra("아이디");
        Intent nextMenu = new Intent(Menu.this, Operation.class);
        nextMenu.putExtra("아이디", getId);
        nextMenu.putExtra("이름", D_Name);
        startActivity(nextMenu);

    }
    public void onClickedComplete(View view){
        Intent intent  = getIntent();
        String getId = intent.getStringExtra("아이디");
        Intent nextMenu = new Intent(Menu.this, Complete_List.class);
        nextMenu.putExtra("아이디", getId);
        nextMenu.putExtra("이름", D_Name);
        startActivity(nextMenu);

    }

    public void onClickedDriver(View view){
        Intent intent  = getIntent();
        String getId = intent.getStringExtra("아이디");
        Intent nextMenu = new Intent(Menu.this, Driver_info.class);
        nextMenu.putExtra("아이디", getId);
        nextMenu.putExtra("이름", D_Name);
        startActivity(nextMenu);

    }

}