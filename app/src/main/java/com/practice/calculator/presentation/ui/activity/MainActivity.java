package com.practice.calculator.presentation.ui.activity;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.practice.calculator.R;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Stack<String> historyOfInputsAndResult;
    TextView textView;
    boolean lastNumeric = false;
    boolean lastDot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.edt1);
        textView = findViewById(R.id.equation);
        editText.setInputType(InputType.TYPE_NULL);
        historyOfInputsAndResult = new Stack<>();
    }

    public void digitClick(View view) {
        String text = ((Button) view).getText().toString();
        if (lastNumeric) {
            editText.append(text);
            lastNumeric = true;
        } else {
            editText.setText(text);
        }
        lastNumeric = true;
        historyOfInputsAndResult.push(editText.getText().toString());

    }

    public void symbolClick(View view) {
        lastDot = false;
        String text = ((Button) view).getText().toString();
        editText.append(text);
        historyOfInputsAndResult.push(editText.getText().toString());

    }

    public void clearClick(View view) {
        editText.setText("");
    }

    public void onDecimalPointClick(View view) {
        if (lastNumeric && !lastDot) {
            editText.append(".");
            lastNumeric = false;
            lastDot = true;
            historyOfInputsAndResult.push(editText.getText().toString());
        }
    }

    private String simplifyInput(String currentInput) {
        char[] arr = currentInput.toCharArray();
        int index = arr.length - 1;
        for (; index >= 0; index--) {
            if (arr[index] == '+'
                    || arr[index] == '-'
                    || arr[index] == '/'
                    || arr[index] == '*'
                    || arr[index] == '.'
            ) {
            } else break;
        }
        return currentInput.substring(0, index + 1);
    }

    public void equalClick(View view) {
        lastNumeric = false;
        String currentInput = simplifyInput(editText.getText().toString());
        try {
            Double result = evaluateExpressionResult(currentInput);
            String resultToShow = String.valueOf(result);
            if (!isActualResultInDouble(resultToShow)) {
                resultToShow = String.valueOf((int) Math.round(result));
            }
            editText.setText(resultToShow);
            textView.append(currentInput + "=" + resultToShow + "\n");
        } catch (Exception e) {
            editText.setText("Invalid operation!");
        }
    }

    private boolean isActualResultInDouble(String result) {
        char[] ch = result.toCharArray();
        for (int index = result.length() - 1; index >= 0; index--) {
            if (ch[index] == '.') return false;
            else if (ch[index] != '0') return true;
        }
        return false;
    }


    private double evaluateExpressionResult(String expression) {
        char[] tokens = expression.toCharArray();

        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ')
                continue;

            if (tokens[i] >= '0' && tokens[i] <= '9') {
                StringBuilder sbuf = new StringBuilder();

                while (i < tokens.length &&
                        tokens[i] >= '0' &&
                        tokens[i] <= '9')
                    sbuf.append(tokens[i++]);
                values.push(Double.parseDouble(sbuf.toString()));
                i--;
            } else if (tokens[i] == '(')
                ops.push(tokens[i]);
            else if (tokens[i] == ')') {
                while (ops.peek() != '(')
                    values.push(applyOp(ops.pop(),
                            values.pop(),
                            values.pop()));
                ops.pop();
            } else if (tokens[i] == '+' ||
                    tokens[i] == '-' ||
                    tokens[i] == '*' ||
                    tokens[i] == '/' ||
                    tokens[i] == '^') {
                while (!ops.empty() &&
                        hasPrecedence(tokens[i], ops.peek()))
                    values.push(applyOp(ops.pop(),
                            values.pop(),
                            values.pop()));

                ops.push(tokens[i]);
            }
        }

        while (!ops.empty())
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));

        return values.pop();
    }

    public static boolean hasPrecedence(
            char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '*' || op1 == '/') &&
                (op2 == '+' || op2 == '-'))
            return false;
        if ((op1 == '^') && (op2 == '*' || op2 == '/' || op2 == '+' || op2 == '-'))
            return false;
        else
            return true;
    }

    public static double applyOp(char op, double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '^':
                return Math.pow(a, b);
            case '/':
                if (b == 0)
                    throw new
                            UnsupportedOperationException(
                            "Cannot divide by zero");
                return a / b;
        }
        return 0;
    }

    public void undoButtonClick(View view) {

        if (!historyOfInputsAndResult.isEmpty()) {
            Log.e("sandeep", "stack popped for : " + historyOfInputsAndResult.pop());
            if (historyOfInputsAndResult.isEmpty())
                editText.setText("");
            else
                editText.setText(historyOfInputsAndResult.peek());
        }
    }
}