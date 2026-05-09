package com.example.k234112eapp;

import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.List;

public class CalculatorActivity extends AppCompatActivity {
    EditText edtFormula;
    Button btnDel, btnEqual;
    TextView txtMC, tXtMR, txtMPlus, txtMMinus, txtMS, txtM;

    View.OnClickListener m_onclick;
    String name_share_pref = "CalculatorInfo";

    //memory
    double memoryValue=0;

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

    @Override
    protected void onPause() {
        super.onPause();
        String current_formula = edtFormula.getText().toString();
        SharedPreferences preferences = getSharedPreferences(name_share_pref, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Formula", current_formula);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(name_share_pref, MODE_PRIVATE);
        String saved_formula = preferences.getString("Formula", ""); // mặc định rỗng
        memoryValue = preferences.getFloat("Memory", 0f);
        edtFormula.setText(saved_formula);
    }

    private List<String> tokenize(String expr) throws Exception {
        List<String> tokens = new ArrayList<>();
        int i = 0;
        expr = expr.trim();

        while (i < expr.length()) {
            char c = expr.charAt(i);

            // Bỏ qua khoảng trắng
            if (c == ' ') { i++; continue; }
            if (Character.isDigit(c) || c == '.' ||
                    (c == '-' && (tokens.isEmpty() ||
                            isOperator(tokens.get(tokens.size() - 1))))) {

                StringBuilder sb = new StringBuilder();
                if (c == '-') { sb.append('-'); i++; }

                boolean hasDot = false;
                while (i < expr.length() &&
                        (Character.isDigit(expr.charAt(i)) ||
                                (expr.charAt(i) == '.' && !hasDot))) {
                    if (expr.charAt(i) == '.') hasDot = true;
                    sb.append(expr.charAt(i));
                    i++;
                }
                if (sb.toString().equals("-"))
                    throw new Exception("Biểu thức không hợp lệ");
                tokens.add(sb.toString());

            } else if (c == '+' || c == '-' || c == 'x' || c == ':') {
                if (c == 'x') tokens.add("*");
                else if (c == ':') tokens.add("/");
                else tokens.add(String.valueOf(c));
                i++;
            } else {
                throw new Exception("Ký tự không hợp lệ: " + c);
            }
        }
        return tokens;
    }

    private boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/");
    }

    private double evaluate(List<String> tokens) throws Exception {
        if (tokens.isEmpty()) throw new Exception("Biểu thức rỗng");

        //nhân và chia
        List<String> pass1 = new ArrayList<>();
        int i = 0;
        while (i < tokens.size()) {
            String token = tokens.get(i);
            if ((token.equals("*") || token.equals("/")) && !pass1.isEmpty()) {
                double left  = Double.parseDouble(pass1.remove(pass1.size() - 1));
                double right = Double.parseDouble(tokens.get(i + 1));
                if (token.equals("/")) {
                    if (right == 0) throw new Exception("Chia cho 0");
                    pass1.add(String.valueOf(left / right));
                } else {
                    pass1.add(String.valueOf(left * right));
                }
                i += 2;
            } else {
                pass1.add(token);
                i++;
            }
        }

        //cộng và trừ
        double result = Double.parseDouble(pass1.get(0));
        i = 1;
        while (i < pass1.size()) {
            String op    = pass1.get(i);
            double right = Double.parseDouble(pass1.get(i + 1));
            if (op.equals("+"))      result += right;
            else if (op.equals("-")) result -= right;
            else throw new Exception("Toán tử không xác định: " + op);
            i += 2;
        }
        return result;
    }

    private String calculate(String formula) {
        try {
            formula = formula.trim();
            if (formula.isEmpty()) return "";

            List<String> tokens = tokenize(formula);
            if (tokens.size() == 1) {
                return formatResult(Double.parseDouble(tokens.get(0)));
            }
            double result = evaluate(tokens);
            return formatResult(result);

        } catch (Exception e) {
            return "Lỗi: " + e.getMessage();
        }
    }

    private String formatResult(double value) {
        if (value == Math.floor(value) && !Double.isInfinite(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(Math.round(value * 1e10) / 1e10);
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
                String result=calculate(formular);
                //result=library_nào_đó(formular)
                //step 3:
                edtFormula.setText(result);
            }
        });

        m_onclick=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String current = edtFormula.getText().toString();
                if (view.equals(txtMS)) {
                    // MS: lưu giá trị hiện tại vào bộ nhớ
                    try {
                        String formula = current.replace("x", "*").replace(":", "/");
                        String result  = calculate(formula);
                        if (!result.startsWith("Lỗi")) {
                            memoryValue = Double.parseDouble(result);
                        }
                    } catch (Exception ignored) {}

                } else if (view.equals(tXtMR)) {
                    // MR: lấy giá trị từ bộ nhớ ra màn hình
                    edtFormula.setText(formatResult(memoryValue));

                } else if (view.equals(txtMPlus)) {
                    // M+: cộng giá trị hiện tại vào bộ nhớ
                    try {
                        String formula = current.replace("x", "*").replace(":", "/");
                        String result  = calculate(formula);
                        if (!result.startsWith("Lỗi")) {
                            memoryValue += Double.parseDouble(result);
                        }
                    } catch (Exception ignored) {}

                } else if (view.equals(txtMMinus)) {
                    // M-: trừ giá trị hiện tại khỏi bộ nhớ
                    try {
                        String formula = current.replace("x", "*").replace(":", "/");
                        String result  = calculate(formula);
                        if (!result.startsWith("Lỗi")) {
                            memoryValue -= Double.parseDouble(result);
                        }
                    } catch (Exception ignored) {}

                } else if (view.equals(txtMC)) {
                    // MC: xóa bộ nhớ
                    memoryValue = 0;

                } else if (view.equals(txtM)) {
                    // M: hiển thị giá trị bộ nhớ hiện tại (không thay màn hình)
                    edtFormula.setText(formatResult(memoryValue));
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