package com.dc.calculator;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class CalculatorScientificSimpleCore extends CalculatorSimpleCore {
    private Stack numStack, opStack;
    private int topNumStack, topOpStack;
    private StringBuffer backEndBuffer;
    public CalculatorScientificSimpleCore(){
        super();
        numStack = new Stack();
        opStack = new Stack();
        topNumStack = -1;
        topOpStack = -1;
        backEndBuffer = new StringBuffer();
    }
    Map setOperatorList(){
        Map<String,Integer> operatorList = new HashMap<>();
        operatorList.put("S",2);
        operatorList.put("C",2);
        operatorList.put("T",2);
        operatorList.put("L",2);
        operatorList.put("N",2);
        operatorList.put("(",1);
        operatorList.put("^",2);
        operatorList.put("/",3);
        operatorList.put("x",3);
        operatorList.put("+",4);
        operatorList.put("-",4);
        operatorList.put("",5);
        return operatorList;
    }

    @Override
    void process() {
        String firstPriorOperator="()";
        String secondPriorOperator="^";
        String thirdPriorOperator="/x";
        String fourthPriorOperator="+-";
        String numTemp = null;
        Map<String,Integer> opMap = setOperatorList();
        try{
            for(int i=0;i<buffer.length();i++){
                String newChar = String.format("%s",buffer.charAt(i));
                if((firstPriorOperator.contains(newChar) || secondPriorOperator.contains(newChar) || thirdPriorOperator.contains(newChar) || fourthPriorOperator.contains(newChar))
                        && !(newChar.equals("(") && i+1 < buffer.length() && buffer.charAt(i+1) == '-')){
                    if(numTemp!=null) {
                        pushNumStack(numTemp);
                        numTemp = null;
                    }
                    String prevOp = opStack.empty() ? "" : String.format("%s",opStack.get(topOpStack));
                    if(newChar.equals(")")){
                        String tempChar = prevOp;
                        while(tempChar.charAt(0)!='('){
                            processOperate();
                            tempChar = opStack.get(topOpStack).toString();
                        }
                        popOpStack();
                    }else if(opMap.get(newChar) <= opMap.get(prevOp) || prevOp.equals("(")){
                        pushOpStack(newChar);
                    }else if(opMap.get(newChar) > opMap.get(prevOp)){
                        processOperate();
                        pushOpStack(newChar);
                    }
                }else if(newChar.equals("S") || newChar.equals("C") || newChar.equals("T") || newChar.equals("L")){
                    switch (newChar.charAt(0)){
                        case 'S':
                            pushOpStack("S");
                            i+=2;
                            break;
                        case 'C':
                            pushOpStack("C");
                            i+=2;
                            break;
                        case 'T':
                            pushOpStack("T");
                            i+=2;
                            break;
                        case 'L':
                            switch (buffer.charAt(i+1)){
                                case 'O':
                                    pushOpStack("L");
                                    i+=2;
                                    break;
                                case 'N':
                                    pushOpStack("N");
                                    i+=1;
                                    break;
                            }
                            break;
                    }
                }else{
                    Log.d("msg",newChar);
                    if(newChar.equals("("))
                        pushOpStack(newChar);
                    if(numTemp==null)
                        numTemp="";
                    numTemp+=newChar;
                }
            }

            if(buffer.charAt(buffer.length()-1)!=')')
                pushNumStack(numTemp);
            while(!opStack.isEmpty()){
                if(!(topOpStack == 0 && opStack.get(topOpStack).toString().equals("(")))
                    processOperate();
                else
                    popOpStack();
            }
            try {
                result = roundDecimal(Double.parseDouble(numStack.pop().toString()));
            }catch (Exception e){}
        }catch (Exception e){
            result="Error";
        }


    }
    @Override
    void assignOperator(View v){
        char charTemp = buffer.charAt(buffer.length()-1);
        String newChar = ((Button)v).getText().toString();
        if((charTemp== '+' || charTemp=='-' || charTemp=='x' || charTemp=='/' || charTemp=='^') && !(newChar.equals("-")))
            buffer.delete(buffer.length()-1,buffer.length());
        else if((charTemp== '+' || charTemp=='-' || charTemp=='x' || charTemp=='/' || charTemp=='^') && newChar.equals("-"))
            newChar="(-";

        buffer.append(newChar);
        lastChar=newChar.charAt(newChar.length()-1);
        isDotExist=false;
    }
    void assignDelete(){
        if(buffer.length()>=4 &&
                (buffer.charAt(buffer.length()-4) == 'S' || buffer.charAt(buffer.length()-4) == 'C' || buffer.charAt(buffer.length()-4) == 'T' || buffer.charAt(buffer.length()-4) == 'L')) {
            buffer.delete(buffer.length()-4,buffer.length());
            if(buffer.length()==0)
                assignReset();
            process();
        }else if(buffer.length()>=3 && buffer.charAt(buffer.length()-3) == 'L'){
            buffer.delete(buffer.length()-3,buffer.length());
            if(buffer.length()==0)
                assignReset();
            process();
        }else if(buffer.length()>=2){
            char temp = buffer.charAt(buffer.length()-1);
            buffer.delete(buffer.length()-1,buffer.length());
            if(temp == '.')
                isDotExist=false;
            process();
        }else
            assignReset();
    }
    void pushNumStack(String num){
        numStack.push(num);
        topNumStack++;
    }
    void pushOpStack(String op){
        opStack.push(op);
        topOpStack++;
    }

    @Override
    void assignEqual() {
        super.assignEqual();
        backEndBuffer.replace(0,backEndBuffer.length(),result);
    }

    Double popNumStack(){
        try{
            topNumStack--;
            return Double.parseDouble(numStack.pop().toString());
        }catch (Exception e){
            return 0.0;
        }

    }
    String popOpStack(){
        topOpStack--;
        return opStack.pop().toString();
    }
    void processOperate(){
        String listAdditional = "SCTLN";
        String prevOp = popOpStack();
        if(prevOp.equals("(") && !opStack.isEmpty())
           prevOp = popOpStack();
        double num1 = popNumStack();
        Double result = 0.0;
        if(listAdditional.contains(prevOp))
            result = operateAdditional(num1,prevOp.charAt(0));
        else{
            double num2 = popNumStack();
            result = operate(num1,num2,prevOp.charAt(0));
        }
        pushNumStack(result.toString());
    }
    Double operate(double num1, double num2, char op){
        switch(op){
            case '+':
                return num1 + num2;
            case '-':
                return num2 - num1;
            case 'x':
                return num1 * num2;
            case '/':
                return num2 / num1;
            case '^':
                return Math.pow(num2,num1);
            default:
                return 0.0;
        }
    }
    Double operateAdditional(double num, char op){
        switch (op){
            case 'S':
                return Math.sin(num);
            case 'C':
                return Math.cos(num);
            case 'T':
                return Math.tan(num);
            case 'L':
                return Math.log10(num);
            case 'N':
                return Math.log(num);
            default:
                return 0.0;
        }
    }
    void assignFunc(View v){
        String operatorList = "+-/x^";

        if(buffer.charAt(0)=='0' && buffer.length()==1){
            buffer.delete(buffer.length()-1,buffer.length());
        }
        if(buffer.length()==0 || (buffer.length()>0 && operatorList.contains(String.format("%s",buffer.charAt(buffer.length()-1))))){
            assignChar(v);
            buffer.append("(");
            lastChar='(';
        }
    }
    void assignFunc2(View v){
        char charTemp = buffer.charAt(buffer.length()-1);
        if((charTemp== '+' || charTemp=='-' || charTemp=='x' || charTemp=='/' || charTemp=='^'))
            buffer.delete(buffer.length()-1,buffer.length());
        String s = ((Button)v).getText().toString();
        String temp="";
        if(s.equals("^X"))
            temp="^";
        else if(s.equals("√X"))
            temp="^0.5";
        else if(s.equals("1/X"))
            temp="^(-1)";
        buffer.append(temp);
        lastChar=temp.charAt(temp.length()-1);
    }
    void assignBracket(View v){
        String operatorList = "+-/x^";

        if(buffer.charAt(0)=='0' && buffer.length()==1){
            buffer.delete(buffer.length()-1,buffer.length());
        }
        if(buffer.length()==0 || (buffer.length()>0 && operatorList.contains(String.format("%s",buffer.charAt(buffer.length()-1)))))
            assignChar(v);
    }
}
