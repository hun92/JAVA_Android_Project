package com.example.test2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ManagerReservation extends AppCompatActivity {

    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:XE";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_reservation);

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
    }

    public void driverRegisterBtn(View view){
        TextView driverEt1 = findViewById(R.id.driverEt1);
        TextView driverEt2 = findViewById(R.id.driverEt2);
        TextView driverEt3 = findViewById(R.id.driverEt3);
        TextView driverEt4 = findViewById(R.id.driverEt4);
        TextView driverEt5 = findViewById(R.id.driverEt5);
        TextView driverEt6 = findViewById(R.id.driverEt6);

        try{

            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            AlertDialog.Builder myAlertBuilder =
                    new AlertDialog.Builder(ManagerReservation.this);
            // alert의 title과 Messege 세팅
            myAlertBuilder.setTitle("Alert");
            myAlertBuilder.setMessage("기사등록을 하시겠습니까?");
            // 버튼 추가 (Ok 버튼과 Cancle 버튼 )
            myAlertBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog,int which){
                    // OK 버튼을 눌렸을 경우
                    try{

                        String sql = "insert into driver values(?, ?, ?, ?, ?, '미정', '미정', '미정', ?)";
                        PreparedStatement pstmt = connection.prepareStatement(sql);
                        pstmt.setString(1,driverEt1.getText().toString());
                        pstmt.setString(2,driverEt2.getText().toString());
                        pstmt.setString(3,driverEt3.getText().toString());
                        pstmt.setString(4,driverEt4.getText().toString());
                        pstmt.setString(5,driverEt5.getText().toString());
                        pstmt.setString(6,driverEt6.getText().toString());
                        pstmt.executeUpdate();

                        driverEt1.setText("");
                        driverEt2.setText("");
                        driverEt3.setText("");
                        driverEt4.setText("");
                        driverEt5.setText("");
                        driverEt6.setText("");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(),"등록완료",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ManagerReservation.this, Manager_menu.class);
                    startActivity(intent);
                }

            });
            myAlertBuilder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Cancle 버튼을 눌렸을 경우
                    Toast.makeText(getApplicationContext(),"취소",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ManagerReservation.this, Manager_menu.class);
                    startActivity(intent);
                }
            });
            // Alert를 생성해주고 보여주는 메소드(show를 선언해야 Alert가 생성됨)
            myAlertBuilder.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void backBtn2(View view) {
        // Intent를 사용하여 다음 액티비티로 이동
        Intent intent = new Intent(this, Manager_menu.class);
        startActivity(intent);
    }
}