package com.example.projecttaxi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Complete_List extends AppCompatActivity {

    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:xe";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;

    String D_Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_list);


        Intent intent = getIntent();
        this.D_Name = intent.getStringExtra("이름");
        String getId = intent.getStringExtra("아이디");

        //보안정책
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        ListView list = findViewById(R.id.list_text);

        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sql = "select reservetime,num,departure from reservation where drivername = ? and approval = 3";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setString(1, this.D_Name);
            ResultSet resultSet = stmt.executeQuery();
            String reserveTime = "";


            while (resultSet.next()) {
                reserveTime = reserveTime +"("+resultSet.getString(2)+")" +resultSet.getString(1)+"  - "+resultSet.getString(3)+"&";
            }
            connection.close();

            String[] docArr = reserveTime.toString().split("&");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, docArr);
            list.setAdapter(adapter);


            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    String selectedItem = docArr[position];
                    Intent nextMenu = new Intent(Complete_List.this, Complete.class);
                    nextMenu.putExtra("날짜", selectedItem);
                    nextMenu.putExtra("이름", D_Name);
                    nextMenu.putExtra("아이디", getId);
                    startActivity(nextMenu);

                }
            });

        } catch (Exception e) {
            Toast mytoast = Toast.makeText(getApplicationContext(), "오류가 발생했습니다: " + e.getMessage(), Toast.LENGTH_LONG);
            mytoast.show();

        }
    }
}
