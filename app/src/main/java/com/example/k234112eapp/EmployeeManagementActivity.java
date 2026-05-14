package com.example.k234112eapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class EmployeeManagementActivity extends AppCompatActivity {

    private static final String SEPARATOR = "-";
    private static final String EMPTY_STRING = "";

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
        String[] samples = getResources().getStringArray(R.array.employee_samples);
        for (String sample : samples) {
            listEmployee.add(sample);
        }
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
        String[]items=data.split(SEPARATOR);
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

    public void saveEmployee(View view) {
        String id = edtId.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();

        if (id.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Mã nhân viên", Toast.LENGTH_SHORT).show();
            edtId.requestFocus();
            return;
        }

        String newData = id + SEPARATOR + name + SEPARATOR + phone;

        int index = -1;
        for (int i = 0; i < listEmployee.size(); i++) {
            String item = listEmployee.get(i);
            String[] parts = item.split(SEPARATOR);
            if (parts[0].equalsIgnoreCase(id)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            // Nếu id chưa tồn tại thì thêm mới
            listEmployee.add(newData);
            Toast.makeText(this, "Đã thêm nhân viên mới", Toast.LENGTH_SHORT).show();
            // Cuộn xuống cuối danh sách để thấy mục mới thêm
            lvEmployee.setSelection(listEmployee.size() - 1);
        } else {
            // Nếu id đã có thì cập nhật
            listEmployee.set(index, newData);
            Toast.makeText(this, "Đã cập nhật thông tin nhân viên", Toast.LENGTH_SHORT).show();
        }
        adapterEmployee.notifyDataSetChanged();

        // Xóa trắng các ô nhập liệu và focus về ô ID
        edtId.setText(EMPTY_STRING);
        edtName.setText(EMPTY_STRING);
        edtPhone.setText(EMPTY_STRING);
        edtId.requestFocus();
    }

    public void removeEmployee(View view) {
        String id = edtId.getText().toString().trim();
        if (id.isEmpty()) {
            Toast.makeText(this, getString(R.string.msg_select_employee_to_delete), Toast.LENGTH_SHORT).show();
            return;
        }

        int index = -1;
        for (int i = 0; i < listEmployee.size(); i++) {
            String item = listEmployee.get(i);
            String[] parts = item.split(SEPARATOR);
            if (parts[0].equalsIgnoreCase(id)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            Toast.makeText(this, getString(R.string.msg_employee_not_found), Toast.LENGTH_SHORT).show();
            return;
        }

        final int deleteIndex = index;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_delete_confirm_title));
        builder.setMessage(getString(R.string.dialog_delete_confirm_msg, id));
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setPositiveButton(getString(R.string.str_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listEmployee.remove(deleteIndex);
                adapterEmployee.notifyDataSetChanged();
                
                // Xóa trắng sau khi xóa
                edtId.setText(EMPTY_STRING);
                edtName.setText(EMPTY_STRING);
                edtPhone.setText(EMPTY_STRING);
                edtId.requestFocus();
                
                Toast.makeText(EmployeeManagementActivity.this, getString(R.string.msg_delete_success), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(getString(R.string.str_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
}