package com.example.calculatrice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner mySpinner;
    EditText ecran;
    LinearLayout linearLayout;
    TextView result ;
    final List<String> options = new ArrayList<>();
    double a, b, resultat;
    String Op = "+";
    boolean isStandardMode = true;
    boolean isShiftMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mySpinner = findViewById(R.id.sp);
        ecran =  findViewById(R.id.ecran);
        linearLayout = findViewById(R.id.linearLayout);
        result = findViewById(R.id.result);
        options.add("Standard");
        options.add("Scientifique");
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, options ) ;
        mySpinner.setAdapter(adapter);
        mySpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, options.get(position), Toast.LENGTH_SHORT).show();
        if(options.get(position) == "Standard"){
            isStandardMode = true;
            ecran.setText("");
            result.setText("");
            linearLayout.setVisibility(View.GONE);
        }else{
            isStandardMode = false;
            ecran.setText("");
            result.setText("");
            linearLayout.setVisibility(View.VISIBLE);
        }
        result.setText("");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void chiffre(View view) {
        String chf = view.getTag().toString();
        String ecranTxt = ecran.getText().toString();
        if(isStandardMode) {
            if (ecranTxt.equals("NaN") || ecranTxt.equals("0") || ecranTxt.contains("."))
                ecranTxt = "";
            ecranTxt += chf;
        }else {
            ecranTxt += chf;
        }
         ecran.setText(ecranTxt);
    }

    public void dot(View view){
        String dot = view.getTag().toString();
        String ecranTxt = ecran.getText().toString();
        char last = ' ';
        if (ecranTxt.length() > 0){
            last = ecranTxt.charAt(ecranTxt.length()-1);
        }
        if(last >= '0' && last <= '9') {
            ecranTxt += dot;
        }else{
            ecranTxt += "0" + dot;
        }
        ecran.setText(ecranTxt);
    }

    public void effacer(View view) {
        ecran.setText("");
        if(!isStandardMode)
            result.setText("");
    }

    public void delete(View view) {
        String str = ecran.getText().toString();
        if (str.length() > 0){
            /*if(str.getCharAt(str.lenght() -1) == ' '){
                str = str.substring(0, str.length() -1);
            }*/
            str = str.substring(0, str.length() -1);
            /*if(str.getCharAt(str.lenght() -1) == ' '){
                str = str.substring(0, str.length() -1);
            }*/
            ecran.setText(str);
        }
    }

    public void shift(View view){
        Button cos = findViewById(R.id.cos);
        Button sin = findViewById(R.id.sin);
        Button tan = findViewById(R.id.tan);

        if(isShiftMode){
            view.setBackgroundResource(android.R.drawable.btn_default);
            cos.setText("cos");
            sin.setText("sin");
            tan.setText("tan");
            cos.setTag("cos(");
            sin.setTag("sin(");
            tan.setTag("tan(");
            //cos.setTextColor();
            isShiftMode = false;
        }else{
            view.setBackgroundColor(0xFF980000);
            cos.setText("cos−1");
            sin.setText("sin−1");
            tan.setText("tan−1");
            cos.setTag("acos(");
            sin.setTag("asin(");
            tan.setTag("atan(");
            isShiftMode = true;
        }
    }

    public void operation(View view) {
        Button btn = (Button) view;
        String ecranTxt = ecran.getText().toString();
        if(isStandardMode) {
            a = Double.valueOf(ecranTxt);
            ecran.setText("");
            Op = btn.getText().toString();
        }else {
            String espace;

            if (ecranTxt.length() > 0){
                espace = ecranTxt.substring(ecranTxt.length() - 1);
                if(espace == " "){
                    ecranTxt += btn.getTag().toString() + " ";
                }else {
                    ecranTxt += " " + btn.getTag().toString() + " ";
                }
            }else {
                ecranTxt += btn.getTag().toString();
            }
            ecran.setText(ecranTxt);
        }
    }

    public void function(View view){
        String str = view.getTag().toString();
        String ecranTxt = ecran.getText().toString();
        String espace;

        if (ecranTxt.length() > 0) {
            espace = ecranTxt.substring(ecranTxt.length() - 1);
            if (espace == " ") {
                ecranTxt += str + " ";
            } else {
                ecranTxt += " " + str + " ";
            }
        } else {
            ecranTxt += str;
        }
        ecran.setText(ecranTxt);
    }

    public void egal(View view) {
        String ecranTxt = ecran.getText().toString();

        if(isStandardMode) {
            b = Double.valueOf(ecranTxt);

            Log.i("test", "OP = " + Op);
            switch (Op) {
                case "+":
                    resultat = a + b;
                    break;
                case "-":
                    resultat = a - b;
                    break;
                case "/":
                    resultat = b == 0 ? Double.NaN : a / b;
                    break;
                case "X":
                    resultat = a * b;
                    break;
            }
            ecran.setText(Double.toString(resultat));
        }else{
            try {
                if(ecranTxt != "") {
                    double res = eval(ecranTxt);
                    result.setText("  = " + res);
                }else{
                    result.setText("");
                }
            } catch (Exception e) {
                result.setText(" Syntax Error");
            }
        }
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch,next;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
                next = (pos+1 < str.length() ? str.charAt(pos+1) : ' ');
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus
                if (eat('π')) return Math.PI;
                if (ch == 'e' && next != 'x' && eat('e')) return Math.E;
                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z' || ch == '√') { // functions
                    while (ch >= 'a' && ch <= 'z'|| ch == '√') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else if (func.equals("asin")) x = Math.toDegrees(Math.asin(x));
                    else if (func.equals("acos")) x = Math.toDegrees(Math.acos(x));
                    else if (func.equals("atan")) x = Math.toDegrees(Math.atan(x));
                    else if (func.equals("log")) x = Math.tan(Math.log(x));
                    else if (func.equals("ln")) x = (1/Math.log(2.718281828459045))* Math.log(x);
                    else if (func.equals("exp")) x = Math.exp(x);
                    else if (func.equals("√")) x = Math.sqrt(x);
                    else throw new RuntimeException("Unknown function: " + func);
                }else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation
                if (eat('²')) x = Math.pow(x, 2);

                return x;
            }
        }.parse();
    }
}
