package com.practice.calculator.presentation.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.practice.calculator.R;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Double number1 = 0.0;
    TextView textView;
    int lastOperation;
    boolean isEqualClicked = false;
    boolean lastNumeric = false;
    boolean lastDot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.edt1);
        textView = findViewById(R.id.equation);
    }

    public void digitClick(View view) {
        editText.append(((Button) view).getText());
        if(isEqualClicked) {
            isEqualClicked = false;
            textView.setText("");
        }
        lastNumeric = true;
    }

    public void symbolClick(View view) {

        if (view.getId() == R.id.buttonadd) {
            number1 = Double.parseDouble(editText.getText().toString());
            textView.setText(number1 + " + ");
            editText.setText("");
            lastOperation = R.id.buttonadd;
        } else if (view.getId() == R.id.buttonsub) {
            number1 = Double.parseDouble(editText.getText().toString());
            textView.setText(number1 + " - ");
            editText.setText("");
            lastOperation = R.id.buttonsub;
        } else if (view.getId() == R.id.buttonmul) {
            number1 = Double.parseDouble(editText.getText().toString());
            textView.setText(number1 + " * ");
            editText.setText("");
            lastOperation = R.id.buttonmul;
        } else if (view.getId() == R.id.buttondiv) {
            number1 = Double.parseDouble(editText.getText().toString());
            textView.setText(number1 + " / ");
            editText.setText("");
            lastOperation = R.id.buttondiv;
        }
    }

    public void clearClick(View view) {
        editText.setText("");
        textView.setText("");
    }

    public void equalClick(View view) {
        isEqualClicked = true;
        long number2 = Long.parseLong(editText.getText().toString());
        double result = 0;
        switch (lastOperation) {

            case R.id.buttonadd:
                result = (double)number1 + number2;
                textView.setText(textView.getText().toString() + number2 + " = " + result);
                break;

            case R.id.buttonsub:
                result = (double)number1 - number2;
                textView.setText(textView.getText().toString() + number2 + " = " + result);
                break;
            case R.id.buttonmul:
                result = (double)number1 * number2;
                textView.setText(textView.getText().toString() + number2 + " = " + result);
                break;
            case R.id.buttondiv:
                try {
                    result = (double)number1 / number2;
                    textView.setText(textView.getText().toString() + number2 + " = " + result);
                } catch (ArithmeticException exception) {
                    textView.setText("Invalid expression!");
                }
                break;
        }
        editText.setText("");
    }

    public void onDecimalPointClick(View view) {
        if(lastNumeric && !lastDot) {
            editText.append(".");
            lastNumeric = false;
            lastDot = true;
        }
    }
}