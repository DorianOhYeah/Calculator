package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_result;
    private final static String TAG = "MainActivity";
    private String operator="";
    private String firstNum="";
    private String nextNum="";
    private String result="";
    private String showText="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set result momvement method
        tv_result = findViewById(R.id.tv_result);
        tv_result.setMovementMethod(new ScrollingMovementMethod());

        //add listener to each button
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_divide).setOnClickListener(this);
        findViewById(R.id.btn_mutiply).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_seven).setOnClickListener(this);
        findViewById(R.id.btn_eight).setOnClickListener(this);
        findViewById(R.id.btn_nine).setOnClickListener(this);
        findViewById(R.id.btn_plus).setOnClickListener(this);
        findViewById(R.id.btn_four).setOnClickListener(this);
        findViewById(R.id.btn_five).setOnClickListener(this);
        findViewById(R.id.btn_six).setOnClickListener(this);
        findViewById(R.id.btn_minus).setOnClickListener(this);
        findViewById(R.id.btn_one).setOnClickListener(this);
        findViewById(R.id.btn_two).setOnClickListener(this);
        findViewById(R.id.btn_three).setOnClickListener(this);
        findViewById(R.id.btn_zero).setOnClickListener(this);
        findViewById(R.id.btn_dot).setOnClickListener(this);
        findViewById(R.id.btn_equal).setOnClickListener(this);
        findViewById(R.id.btn_sqrt).setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        int resId = v.getId();
        String inputText;
        if (resId == R.id.btn_sqrt) {
            inputText = "√";
        }else{
            inputText = ((TextView) v).getText().toString();

        }
        Log.d(TAG, "resid=" + resId + ",inputText=" + inputText);
        if(resId == R.id.btn_clear){// clear function
            clear("");
        }else if (resId == R.id.btn_cancel) { // cancel function
            if (operator.equals("")) { // back the operation of the fist number if exsiting no operator
                if (firstNum.length() == 1) {
                    firstNum = "0";
                } else if (firstNum.length() > 0) {
                    firstNum = firstNum.substring(0, firstNum.length() - 2);
                } else {
                    Toast.makeText(this, "No Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                showText = firstNum;
                tv_result.setText(showText);
            } else { // cancel the operation one by one of the second number if exsiting operator
                if (nextNum.length() == 1) {
                    nextNum = "";
                } else if (nextNum.length() > 1) {
                    nextNum = nextNum.substring(0, nextNum.length() - 2);
                } else {
                    Toast.makeText(this, "No Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                showText = showText.substring(0, showText.length() - 2);
                tv_result.setText(showText);
            }
        } else if (resId == R.id.btn_equal) { // click the eaqual button
            if (operator.length() == 0 ) {
                Toast.makeText(this, "please enter the operator", Toast.LENGTH_SHORT).show();
                return;
            } else if (nextNum.length() <= 0) {
                Toast.makeText(this, "please enter the number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (caculate()) { // show the result if successfully calculating
                operator = inputText;
                showText = showText + "=" + result;
                tv_result.setText(showText);
            } else { // return if failed
                return;
            }
        } else if (resId == R.id.btn_plus || resId == R.id.btn_minus // click plus,minus,mutiply or divide
                || resId == R.id.btn_mutiply || resId == R.id.btn_divide) {
            if (firstNum.length() <= 0) {
                Toast.makeText(this, "please enter the number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (operator.length() == 0 || operator.equals("＝") || operator.equals("√")) {
                operator = inputText; // operator
                showText = showText + operator;
                tv_result.setText(showText);
            } else {
                Toast.makeText(this, "please enter the number", Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (resId == R.id.btn_sqrt) { // click the sqrt button
            if (firstNum.length() <= 0) {
                Toast.makeText(this, "Please enter the number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Double.parseDouble(firstNum) < 0) {
                Toast.makeText(this, "The square root cannot be less than 0", Toast.LENGTH_SHORT).show();
                return;
            }
            // Calculating for sqrt
            result = String.valueOf(Math.sqrt(Double.parseDouble(firstNum)));
            firstNum = result;
            nextNum = "";
            operator = inputText;
            showText = showText + "√=" + result;
            tv_result.setText(showText);
            Log.d(TAG, "result=" + result + ",firstNum=" + firstNum + ",operator=" + operator);
        } else { // click other button
            if (operator.equals("＝")) { // clean the text if clicking =
                operator = "";
                firstNum = "";
                showText = "";
            }
            if (resId == R.id.btn_dot) { // clicking dot button
                inputText = ".";
            }
            if (operator.equals("")) { // add the firstNum if no operator
                firstNum = firstNum + inputText;
            } else { // add the lastNum if there is a operator
                nextNum = nextNum + inputText;
            }
            showText = showText + inputText;
            tv_result.setText(showText);
        }
        return;
    }

    // calculate and return true if being successful or false if being failed
    private boolean caculate() {
        Log.d(TAG, "operator=" + operator);
        if (operator.equals("+")) { // add
            result = CalculatingProcess.add(firstNum, nextNum);
        } else if (operator.equals("-")) { // minus
            result = String.valueOf(CalculatingProcess.sub(firstNum, nextNum));
        } else if (operator.equals("*")) { // multiply
            result = String.valueOf(CalculatingProcess.mul(firstNum, nextNum));
        } else if (operator.equals("/")) { // divide
            if ("0".equals(nextNum)) { // fist number is 0 when dividing
                // remind the user if the first number is 0 within the operation of dividing
                Toast.makeText(this, "dividend cannot be 0", Toast.LENGTH_SHORT).show();
                // false if failed calculating
                return false;
            } else { // dividend is not 0
                result = String.valueOf(CalculatingProcess.div(firstNum, nextNum));
            }
        } else {
            Log.d(TAG, "wrong=" + operator);
        }
        // print the result in the Log
       Log.d(TAG, "result=" + result);
        firstNum = result;
        nextNum = "";
        // true if successfully calculating
        return true;
    }

    private void clear(String text) {
        showText = text;
        tv_result.setText(showText);
        operator = "";
        firstNum = "";
        nextNum = "";
        result = "";
    }
}
