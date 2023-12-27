package com.example.tavara;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class HistoryActivity extends AppCompatActivity {
    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@192.168.30.15:1521:xe";
    private static final String USERNAME = "HR";
    private static final String PASSWORD = "1234";
    private Connection connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        String name = getIntent().getStringExtra("이름");
        String cardNum = getIntent().getStringExtra("복지카드번호");
        String cardAccount = getIntent().getStringExtra("복지카드잔액");
        String account = getIntent().getStringExtra("잔액");
        String phone = getIntent().getStringExtra("전화번호");

        try{
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Spinner spinner = findViewById(R.id.spinner);
            String sql = "select * from reservation where username = ? and approval = 3 order by reservetime asc";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet resultSet = pstmt.executeQuery();
            String spin = "";
            while(resultSet.next()){
                spin = spin + (resultSet.getString(4) + ",") ;
            }
            String[] title = spin.toString().split(",");
            connection.close();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item,title);
            spinner.setAdapter(adapter);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void onClickRead(View view){
        Spinner spinner = findViewById(R.id.spinner);

        TextView historyDate = findViewById(R.id.historyDate);
        TextView historyDName = findViewById(R.id.historyDName);
        TextView historyCarNum = findViewById(R.id.historyCarNum);
        TextView historyPhone = findViewById(R.id.historyPhone);
        TextView historyDeparture = findViewById(R.id.historyDeparture);
        TextView historyarrival = findViewById(R.id.historyarrival);
        TextView historyPeople = findViewById(R.id.historyPeople);
        TextView historyWheel = findViewById(R.id.historyWheel);
        TextView historyApproval = findViewById(R.id.historyApproval);
        TextView historyPrice = findViewById(R.id.historyPrice);

        String text = spinner.getSelectedItem().toString();

        try{
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            String sql = "select * from reservation where reservetime = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1,text);
            ResultSet resultSet = pstmt.executeQuery();
            if(resultSet.next() == true){
                historyDate.setText(resultSet.getString(4).toString());
                historyDName.setText(resultSet.getString(8).toString());
                historyCarNum.setText(resultSet.getString(7).toString());
                historyPhone.setText(resultSet.getString(11).toString());
                historyDeparture.setText(resultSet.getString(2).toString());
                historyarrival.setText(resultSet.getString(3).toString());
                historyPeople.setText(resultSet.getString(6).toString());
                if(resultSet.getString(5).toString().equals("1")){
                    historyWheel.setText("있음");
                }else{
                    historyWheel.setText("없음");
                }
                historyApproval.setText("운행완료");
                historyPrice.setText(resultSet.getString(9).toString());
            }
            Toast myToast = Toast.makeText(this.getApplicationContext(),"조회가 완료되었습니다.", Toast.LENGTH_SHORT);
            myToast.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}