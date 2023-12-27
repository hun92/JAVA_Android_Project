package com.example.test2;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ManagerCurrent extends AppCompatActivity {
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:XE";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_current);

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        Intent intent = getIntent();
        String reserveNum = intent.getStringExtra("예약번호");
        String[] num = reserveNum.split("-");
        String reserveNumber = num[0];
        int number = Integer.parseInt(reserveNumber);

        try{
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Spinner spinner = findViewById(R.id.spinner);
            String sql = "select * from driver";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet resultSet = pstmt.executeQuery();
            String spin = "";
            while(resultSet.next()){
                spin = spin + (resultSet.getString(1) + ",") ;
            }
            String[] title = spin.toString().split(",");
            connection.close();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item,title);
            spinner.setAdapter(adapter);
            System.out.println(number);

        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            TextView tv_date = findViewById(R.id.tv_date);
            TextView tv_departure = findViewById(R.id.tv_departure);
            TextView tv_Arrival = findViewById(R.id.tv_Arrival);
            TextView tv_count = findViewById(R.id.tv_count);
            TextView tv_Check = findViewById(R.id.tv_Check);
            TextView tv_money = findViewById(R.id.tv_money);
            TextView textView46 = findViewById(R.id.textView46);

            Statement statement = connection.createStatement();
            String sql = "select * from reservation where num ="+number;
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next() == true){
                tv_date.setText(resultSet.getString(4));
                tv_departure.setText(resultSet.getString(2));
                tv_Arrival.setText(resultSet.getString(3));
                tv_count.setText(resultSet.getString(6));
                if(resultSet.getString(5).equals("1")){
                    tv_Check.setText("있음");
                }else{
                    tv_Check.setText("없음");
                }
                tv_money.setText(resultSet.getString(9));
                textView46.setText("접수대기");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void approvalBtn(View view){
        Spinner spinner = findViewById(R.id.spinner);
        String text = spinner.getSelectedItem().toString();
        TextView textView46 = findViewById(R.id.textView46);

        Intent intent = getIntent();
        String[] reserveNum = intent.getStringExtra("예약번호").split("-");
        String num = reserveNum[0];

        String carNumber = "";
        String driverPhone = "";

        try{
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sql = "select * from driver where name = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1,text);
            ResultSet resultSet = pstmt.executeQuery();

            if(resultSet.next() == true){
                carNumber = resultSet.getString(4);
                driverPhone = resultSet.getString(9);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sql = "update reservation set approval = 1, carnumber= ?, drivername=?, driverphone=? where num = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1,carNumber);
            pstmt.setString(2,text);
            pstmt.setString(3,driverPhone);
            pstmt.setString(4, num);
            pstmt.executeUpdate();

            textView46.setText("배차완료");

            intent = new Intent(ManagerCurrent.this, Manager_menu.class);
            startActivity(intent);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}