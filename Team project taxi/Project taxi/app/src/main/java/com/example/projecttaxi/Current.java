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


//배차 검토
public class Current extends AppCompatActivity {

    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:xe";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;

    String D_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current);

        Intent intent = getIntent();
        String reserveTime = intent.getStringExtra("날짜");
        this.D_Name = intent.getStringExtra("이름");
        String[] reserveNum = reserveTime.split("\\)");
        String number = reserveNum[0].replaceAll("\\(", "");
        String num = number;
        System.out.println(num);
        System.out.println(D_Name);

        //보안정책
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        TextView date = findViewById(R.id.tv_date);
        TextView Gname = findViewById(R.id.tv_Gname);
        TextView Gphnum = findViewById(R.id.tv_Gphnum);
        TextView departure = findViewById(R.id.tv_departure);
        TextView Arrival = findViewById(R.id.tv_Arrival);
        TextView count = findViewById(R.id.tv_count);
        TextView Check1 = findViewById(R.id.tv_check1);
        TextView money = findViewById(R.id.tv_money);
        TextView Check2 = findViewById(R.id.tv_check2);


        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sql = "select * from reservation where drivername = ? and approval = 1 and num = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, D_Name);
            stmt.setString(2, num);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next() == true) {

                date.setText(resultSet.getString(4));
                Gname.setText(resultSet.getString(1));
                departure.setText(resultSet.getString(2));
                Arrival.setText(resultSet.getString(3));
                count.setText(resultSet.getString(6));

                if (resultSet.getString(5).toString().equals("1")) {
                    Check1.setText("있음");

                } else {
                    Check1.setText("없음");
                }
                Gphnum.setText(resultSet.getString(13));
                money.setText(resultSet.getString(9));

                if (resultSet.getString(12).toString().equals("1")) {
                    Check2.setText("승인 대기중");
                }
            }

        } catch (Exception e) {

            Toast mytoast = Toast.makeText(getApplicationContext(), "오류가 발생했습니다: " + e.getMessage(), Toast.LENGTH_LONG);
            mytoast.show();

        }
    }

    public void onClickedOk(View view) {

        TextView tvCheck2 = findViewById(R.id.tv_check2);
        String check = tvCheck2.getText().toString();


        Intent intent = getIntent();
        String reserveTime = intent.getStringExtra("날짜");
        this.D_Name = intent.getStringExtra("이름");
        String[] reserveNum = reserveTime.split("\\)");
        String number = reserveNum[0].replaceAll("\\(","");
        String num = number;
        System.out.println(num);
        System.out.println(D_Name);

        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            if (check.contains("대기")) {
                tvCheck2.setText("기사접수 완료");
                String sql = "UPDATE RESERVATION SET APPROVAL = 2 WHERE num = ?";

                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, num);
                pstmt.executeUpdate();


                Toast mytoast = Toast.makeText(getApplicationContext(), "배차 수락", Toast.LENGTH_LONG);
                mytoast.show();


                Intent nextMenu = new Intent(Current.this, Operation.class);
                nextMenu.putExtra("이름", D_Name);
                startActivity(nextMenu);

            }


        } catch (Exception e) {
            Toast mytoast = Toast.makeText(getApplicationContext(), "배차 실패", Toast.LENGTH_LONG);
            mytoast.show();


        }
    }

    public void onClickedCancel(View view) {
        TextView tvCheck2 = findViewById(R.id.tv_check2);
        String check = tvCheck2.getText().toString();


        Intent intent = getIntent();
        String reserveTime = intent.getStringExtra("날짜");
        this.D_Name = intent.getStringExtra("이름");
        String[] reserveNum = reserveTime.split("\\)");
        String number = reserveNum[0].replaceAll("\\(", "");
        String num = number;
        System.out.println(num);
        System.out.println(D_Name);


        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            if (check.contains("대기")) {
                tvCheck2.setText("기사접수 거절");
                String sql = "UPDATE RESERVATION SET APPROVAL =0 WHERE num = ?";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, num);
                pstmt.executeUpdate();

                Toast mytoast = Toast.makeText(getApplicationContext(), "배차 거절", Toast.LENGTH_LONG);
                mytoast.show();


                Intent nextMenu = new Intent(Current.this, Menu.class);
                nextMenu.putExtra("이름", D_Name);
                startActivity(nextMenu);

            }


        } catch (Exception e) {
            Toast mytoast = Toast.makeText(getApplicationContext(), "배차 실패", Toast.LENGTH_LONG);
            mytoast.show();

        }
    }

}
