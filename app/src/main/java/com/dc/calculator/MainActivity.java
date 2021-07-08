package com.dc.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn0;
    Button btnAdd,btnSub,btnMul,btnDiv;
    Button btnEqual, btnPer, btnDot, btnC;
    Button btnSin, btnCos, btnTan, btnLog, btnPow, btnSqrt, btnA, btnLn;
    Button btnBr1,btnBr2;
    ImageButton btnSwap,btnDel;
    LinearLayout linearLayout1;
    TextView txtRes,txtBox;
    CalculatorSimpleCore calculatorSimpleCore = new CalculatorSimpleCore();
    int swt = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
        setComponentEvent();
        setAdditionalButtonEvent();
    }
    void initComponent(){
        linearLayout1 = findViewById(R.id.linearlayout1);
        initBasicCalcComponent();
    }
    void initBasicCalcComponent(){
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);
        btn0 = findViewById(R.id.btn0);

        btnAdd = findViewById(R.id.btnAdd);
        btnSub = findViewById(R.id.btnSub);
        btnMul = findViewById(R.id.btnMul);
        btnDiv = findViewById(R.id.btnDiv);

        btnEqual = findViewById(R.id.btnEqual);
        btnDel = findViewById(R.id.btnDel);
        btnPer = findViewById(R.id.btnPer);
        btnDot = findViewById(R.id.btnDot);
        btnC = findViewById(R.id.btnC);

        btnSwap = findViewById(R.id.btnSwap);

        txtBox = findViewById(R.id.txtBox);
        txtRes = findViewById(R.id.txtRes);

        btnSin = findViewById(R.id.btnSin);
        btnCos = findViewById(R.id.btnCos);
        btnTan = findViewById(R.id.btnTan);

        btnLog = findViewById(R.id.btnLog);
        btnA = findViewById(R.id.btnA);
        btnSqrt = findViewById(R.id.btnSqrt);
        btnPow = findViewById(R.id.btnPow);
        btnLn = findViewById(R.id.btnLn);

        btnBr1 = findViewById(R.id.btnBr1);
        btnBr2 = findViewById(R.id.btnBr2);
    }
    void changeLayout(){
        swt*=-1;
        if(swt==-1){
            calculatorSimpleCore = new CalculatorScientificSimpleCore();
            btnDel.setImageResource(R.drawable.ic_backspace_black_24dp);
            btnDel.setPadding(60,60,60,60);
        }
        else {
            calculatorSimpleCore = new CalculatorSimpleCore();
            btnDel.setImageResource(R.drawable.ic_backspace_black_16dp);
            btnDel.setPadding(80,80,80,80);
        }
        toggleAdditionalComponent();
        changeResultOutput();
    }
    void toggleAdditionalComponent(){
        int visibility = swt == 1 ? View.GONE : View.VISIBLE;
        linearLayout1.setVisibility(visibility);

        btnSin.setVisibility(visibility);
        btnCos.setVisibility(visibility);
        btnTan.setVisibility(visibility);

        btnLog.setVisibility(visibility);
        btnA.setVisibility(visibility);
        btnSqrt.setVisibility(visibility);
        btnPow.setVisibility(visibility);
        btnLn.setVisibility(visibility);

        btnBr1.setVisibility(visibility);
        btnBr2.setVisibility(visibility);
    }
    void setComponentEvent(){
        setNumberButtonEvent();
        setOperatorButtonEvent();
        btnDot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorSimpleCore.assignDot();
                changeResultOutput();
            }
        });
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorSimpleCore.assignReset();
                changeResultOutput();
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorSimpleCore.assignDelete();
                changeResultOutput();
            }
        });
        btnPer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorSimpleCore.assignPercentage();
                changeResultOutput();
            }
        });
        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatorSimpleCore.assignEqual();
                changeResultOutput();
            }
        });
        btnSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLayout();
            }
        });
    }
    void setAdditionalButtonEvent(){
        Function1OnClick function1OnClick = new Function1OnClick();
        btnSin.setOnClickListener(function1OnClick);
        btnCos.setOnClickListener(function1OnClick);
        btnTan.setOnClickListener(function1OnClick);
        btnLn.setOnClickListener(function1OnClick);
        btnLog.setOnClickListener(function1OnClick);

        Function2OnClick function2OnClick = new Function2OnClick();
        btnPow.setOnClickListener(function2OnClick);
        btnA.setOnClickListener(function2OnClick);
        btnSqrt.setOnClickListener(function2OnClick);

        BracketOnClick bracketOnClick = new BracketOnClick();
        btnBr1.setOnClickListener(bracketOnClick);
        btnBr2.setOnClickListener(new NumberOnClick());
    }
    void setNumberButtonEvent(){
        NumberOnClick numberOnClick = new NumberOnClick();
        btn1.setOnClickListener(numberOnClick);
        btn2.setOnClickListener(numberOnClick);
        btn3.setOnClickListener(numberOnClick);
        btn4.setOnClickListener(numberOnClick);
        btn5.setOnClickListener(numberOnClick);
        btn6.setOnClickListener(numberOnClick);
        btn7.setOnClickListener(numberOnClick);
        btn8.setOnClickListener(numberOnClick);
        btn9.setOnClickListener(numberOnClick);
        btn0.setOnClickListener(numberOnClick);
    }
    void setOperatorButtonEvent(){
        OperatorOnClick operatorOnClick = new OperatorOnClick();
        btnAdd.setOnClickListener(operatorOnClick);
        btnMul.setOnClickListener(operatorOnClick);
        btnSub.setOnClickListener(operatorOnClick);
        btnDiv.setOnClickListener(operatorOnClick);
    }
    void changeResultOutput(){
        String result = calculatorSimpleCore.getResult();
        txtBox.setText(calculatorSimpleCore.getBuffer());
        if(result.equals(""))
            txtRes.setText("=0");
        else
            txtRes.setText("="+result);
    }
    class NumberOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
           calculatorSimpleCore.assignNumber(v);
           calculatorSimpleCore.process();
           changeResultOutput();
        }
    }
    class OperatorOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            calculatorSimpleCore.assignOperator(v);
            calculatorSimpleCore.process();
            changeResultOutput();
        }
    }
    class Function1OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            calculatorSimpleCore.assignFunc(v);
            calculatorSimpleCore.process();
            changeResultOutput();
        }
    }
    class Function2OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            calculatorSimpleCore.assignFunc2(v);
            calculatorSimpleCore.process();
            changeResultOutput();
        }
    }
    class BracketOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            calculatorSimpleCore.assignBracket(v);
            calculatorSimpleCore.process();
            changeResultOutput();
        }
    }
}
