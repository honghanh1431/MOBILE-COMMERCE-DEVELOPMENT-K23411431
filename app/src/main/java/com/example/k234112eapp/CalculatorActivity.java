package com.example.k234112eapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalculatorActivity extends AppCompatActivity {
    EditText edtFormula;
    Button btnDel, btnEqual;
    TextView txtMC, tXtMR, txtMPlus, txtMMinus, txtMS, txtM;

    View.OnClickListener m_onclick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculator);
        addViews();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addEvents() {
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get current data
                String current_data=edtFormula.getText().toString();
                //remove last character
                String new_value="";
                if(current_data.length()>1)
                {
                   new_value=current_data.substring(0,current_data.length()-1);
                }
                //set new value
                edtFormula.setText(new_value);
            }
        });

        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //step 1: get data(formular)
                String formular=edtFormula.getText().toString();
                //step 2: invoke library for formular(find internet)...
                String result="";
                //result=library_nào_đó(formular)
                //step 3:
                edtFormula.setText(result);
            }
        });

        m_onclick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.equals(txtM))
                {
                    //khách hàng nhấn txtM
                }
                else if (view.equals(txtMMinus))
                {
                    //khách hàng nhấn txtMMinus
                }//không dùng dấu == để so sánh vì nó ko hiểu so sánh ô nhớ khi dùng ==
            }
        };
        //m_onclick là biến có khả năng sinh sự kiện (variable as listener)
        //thường dùng để sharing sự kiện (từ 2 view trở lên)
        txtM.setOnClickListener(m_onclick);
        txtMMinus.setOnClickListener(m_onclick);
        tXtMR.setOnClickListener(m_onclick);
        txtMS.setOnClickListener(m_onclick);
        txtMPlus.setOnClickListener(m_onclick);
        txtMC.setOnClickListener(m_onclick);
    }

    private void addViews() {
        edtFormula=findViewById(R.id.edtFormula);
        btnDel=findViewById(R.id.btnDel);
        btnEqual=findViewById(R.id.btnEqual);

        txtMC=findViewById(R.id.txtMC);
        tXtMR=findViewById(R.id.txtMR);
        txtMPlus=findViewById(R.id.txtMPlus);
        txtMMinus=findViewById(R.id.txtMMinus);
        txtMS=findViewById(R.id.txtMS);
        txtM=findViewById(R.id.txtM);
    }

    public void processInputData(View view) {
        Button btn_clicked= (Button) view;
        //old value
        String old_value=edtFormula.getText().toString();
        //new value
        String input_value=btn_clicked.getText().toString();
        String new_value=old_value+input_value;
        edtFormula.setText(new_value);
    }
}