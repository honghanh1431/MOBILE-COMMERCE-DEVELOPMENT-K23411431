package com.example.k234112eapp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class EmployeeManagementActivity extends AppCompatActivity {

    Button btnExit;

    ListView lvEmployee;
    ArrayList<String>listEmployee;
    ArrayAdapter<String>adapterEmployee;

    EditText edtId, edtName, edtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_management);
        addViews();
        addEvents();

        loadData();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadData() {
        listEmployee.add("e1-Tèo-0965669729");
        listEmployee.add("e2-Tý-0965669730");
        listEmployee.add("e3-Bin-0965669731");
        listEmployee.add("e4-Bo-0965669732");
        listEmployee.add("e5-Kun-0965669733");
        //nói adapter cập nhật giao diện:
        adapterEmployee.notifyDataSetChanged();
    }

    private void addEvents() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processExit();
            }
        });

        lvEmployee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayEmployeeInfor(position);
            }
        });
    }

    private void displayEmployeeInfor(int position) {
        String data=listEmployee.get(position);
        String[]items=data.split("-");
        //hiển thị items [0] ---> id, items [1] --> name, items [2] -> phone
        edtId.setText(items[0]);
        edtName.setText(items[1]);
        edtPhone.setText(items[2]);
    }

    private void processExit() {
        Dialog custom=new Dialog(this);
        custom.setContentView(R.layout.custom_dialog);
        ImageView imgSave=custom.findViewById(R.id.imgYes);
        ImageView imgCancel=custom.findViewById(R.id.imgCancel);
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom.dismiss();
            }
        });
        custom.show();
    }

    private void addViews() {
        btnExit=findViewById(R.id.btnExit);
        lvEmployee=findViewById(R.id.lvEmployee);
        listEmployee=new ArrayList<>();
        adapterEmployee=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listEmployee);
        lvEmployee.setAdapter(adapterEmployee);

        edtId=findViewById(R.id.edtId);
        edtName=findViewById(R.id.edtName);
        edtPhone=findViewById(R.id.edtPhone);
    }
}