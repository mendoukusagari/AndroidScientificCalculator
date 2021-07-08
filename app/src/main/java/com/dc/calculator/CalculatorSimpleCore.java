package com.dc.calculator;

import android.view.View;
import android.widget.Button;

public class CalculatorSimpleCore {
    protected StringBuffer buffer;
    protected char lastChar;
    protected String result;
    protected boolean isDotExist;
    public CalculatorSimpleCore(){
        buffer = new StringBuffer();
        buffer.append("0");
        lastChar = ' ';
        result = "";
        isDotExist = false;
    }
    void assignReset(){
        lastChar=' ';
        buffer.replace(0,buffer.length(),"0");
        result = buffer.toString();
        isDotExist=false;
    }
    void assignDelete(){
        if(buffer.length()>=2){
            char temp = buffer.charAt(buffer.length()-1);
            buffer.delete(buffer.length()-1,buffer.length());
            if(temp == '.')
                isDotExist=false;
            process();
        }else
            assignReset();
    }
    void assignDot(){
        if(!isDotExist)
            buffer.append(".");
        isDotExist=true;
    }
    void assignPercentage(){
        Double total = Double.parseDouble(result);
        assignReset();
        total /= 100;
        result = roundDecimal(total);
        buffer.delete(0,buffer.length());
        buffer.append(result);
    }
    void assignChar(View v){
        String newChar = ((Button)v).getText().toString();
        buffer.append(newChar);
        lastChar=newChar.charAt(0);
    }
    void assignNumber(View v){
        if(buffer.charAt(0)=='0' && buffer.length()==1)
            buffer.delete(buffer.length()-1,buffer.length());
        assignChar(v);
    }
    void assignOperator(View v){
        char charTemp = buffer.charAt(buffer.length()-1);
        if(charTemp== '+' || charTemp=='-' || charTemp=='x' || charTemp=='/')
            buffer.delete(buffer.length()-1,buffer.length());
        assignChar(v);
        isDotExist=false;
    }
    void assignEqual(){
        buffer.replace(0,buffer.length(),result);
    }
    void process(){
        Double total = 0.0, totalTemp = 0.0;
        String temp = "0";
        char op = '+';
        for(int i=0;i<buffer.length();i++){
            char charTemp = buffer.charAt(i);
            if(charTemp== '+' || charTemp=='-' || charTemp=='x' || charTemp=='/'){
                op = charTemp;
                temp="0";
                totalTemp = total;
            }else{
                temp += String.format("%s",charTemp);
                total = operate(op,totalTemp,Double.parseDouble(temp));
            }
        }
        result = roundDecimal(total);
    }
    String roundDecimal(double d){
        if(d == (long)d)
            return String.format("%d",(long)d);
        else{
            String s = String.format("%.9f",d);
            while (s.endsWith("0")){
                s = (s.substring(0, s.length() - 1));
            }
            return s;
        }
    }
    Double operate(char op, Double total, Double temp){
        switch(op){
            case '+':
                return total + temp;
            case 'x':
                return total * temp;
            case '/':
                return total / temp;
            case '-':
                return total - temp;
            default:
                return 0.0;
        }
    }
    String getBuffer(){
        return buffer.toString();
    }
    String getResult(){
        return result;
    }
    void assignFunc(View v){}
    void assignFunc2(View v){}
    void assignBracket(View v){}
}
