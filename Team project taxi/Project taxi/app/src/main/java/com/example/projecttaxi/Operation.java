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

public class Operation extends AppCompatActivity {

    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:xe";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;

    String D_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);

        Intent intent = getIntent();
        this.D_Name = intent.getStringExtra("이름");


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

            String sql = "select * from reservation where drivername = ? and approval = 2";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, D_Name);
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
                if (resultSet.getString(12).toString().equals("2")) {
                    Check2.setText("기사접수완료");
                }
            }

        } catch (Exception e) {

            Toast mytoast = Toast.makeText(getApplicationContext(), "오류가 발생했습니다: " + e.getMessage(), Toast.LENGTH_LONG);
            mytoast.show();

        }
    }


    public void onClickedcheck(View view) {
        TextView tv_Gname = findViewById(R.id.tv_Gname);
        Intent intent = getIntent();
        String getId = intent.getStringExtra("아이디");
        try{
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sql = "select * from users where name = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, tv_Gname.getText().toString());
            ResultSet resultSet = pstmt.executeQuery();

            String account = "";
            String userName = "";

            if(resultSet.next() == true){
                account = account + resultSet.getString(6);
                userName = resultSet.getString(1);
            }

            int getAccount = Integer.parseInt(account);

            TextView tv_money = findViewById(R.id.tv_money);
            String money = tv_money.getText().toString();
            int getMoney = Integer.parseInt(money);

            getAccount = getAccount - getMoney;

            System.out.println(String.valueOf(getAccount));

            String sql2 = "update reservation set approval = 3 where username = ? and approval = 2";
            PreparedStatement pstmt2 = connection.prepareStatement(sql2);
            pstmt2.setString(1, userName);
            pstmt2.executeUpdate();
            pstmt2.close();

            String sql3 = "update users set account = ? where name = ?";
            PreparedStatement pstmt3 = connection.prepareStatement(sql3);
            pstmt3.setString(1,String.valueOf(getAccount));
            pstmt3.setString(2,userName);
            pstmt3.executeUpdate();

            Toast mytoast = Toast.makeText(getApplicationContext(), "운행이 완료되었습니다: ", Toast.LENGTH_LONG);
            mytoast.show();


            Intent nextMenu = new Intent(Operation.this, Menu.class);
            nextMenu.putExtra("이름", D_Name);
            nextMenu.putExtra("아이디", getId);
            startActivity(nextMenu);



        }catch (Exception e){
            e.printStackTrace();

            TextView tv = findViewById(R.id.textView8);
            tv.setText(e.getMessage());

        }
    }

}

