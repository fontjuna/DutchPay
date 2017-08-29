package com.nohseunghwa.fontjuna.dutchpay.temporary;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.BRACKET_END;
import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.BRACKET_LEFT;
import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.BRACKET_RIGHT;
import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.BRACKET_START;
import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.CONVERT_FROM;
import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.CONVERT_TO;
import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.MINUS;
import static com.nohseunghwa.fontjuna.dutchpay.temporary.Constants.PLUS;


/**
 * Created by fontjuna on 2017-08-27.
 */

public class Calculation {
    private static Calculation instance = null;

    private Calculation() {
    }

    // 싱글톤
    private static Calculation getInstance() {
        if (instance == null) {
            instance = new Calculation();
        }
        return instance;
    }

    // 외부에서 요청 될 계산 함수
    public static String Calculate(String data) {
        return Calculation.getInstance().excute(data);
    }

    // 내부에서 불리는 계산 함수
    private String excute(String data) {
        //공백제거 필요한 치환
        data = prepareData(data);
        //괄호 쌍과 데이타 끝을 검사 (데이타 끝은 숫자이거나 오른쪽 괄호이어야 함)
        if (!checkBracketPair(data)) {
            throw new RuntimeException("Calculation Error");
        }
        ArrayList<String> tokenList = new ArrayList<>();
        tokenList = splitByOperator(data);
        //계산호출
        tokenList = stackCalc(tokenList);
        if (tokenList.size() < 1) {
            throw new RuntimeException("Calulation Error");
        }
        return tokenList.get(0);
    }

    //계산 시작
    private ArrayList<String> stackCalc(ArrayList<String> tokenList) {
        String[] singleOp = {"√", "!"};
        ArrayList<String> resultList = new ArrayList<>();
        if (hasSingleOperator(tokenList)) {
            for (String op : singleOp) {
                tokenList = calcSingleOperator(tokenList, op);
            }
        }
        if (tokenList.contains("(")) {
            int[] bracketPosition = findBracketPosition(tokenList, 0);
            // 괄호안에 데이타가 있다면
            if (bracketPosition[BRACKET_END] < bracketPosition[BRACKET_START] - 2) {
                //없으면
                throw new RuntimeException("Calculation Error");
            } else {
                //있으면
                ArrayList<String> centerList = new ArrayList<>();
                for (int i = bracketPosition[BRACKET_START] + 1; i < bracketPosition[BRACKET_END]; i++) {
                    centerList.add(tokenList.get(i));
                }
                centerList = stackCalc(centerList);
                resultList = replaceCenterList(tokenList, bracketPosition[BRACKET_START], bracketPosition[BRACKET_END], centerList);
            }
        } else {
            int calcPosition = findCalcPosition(tokenList);
            if (calcPosition > 0) {
                double value = calculateByOpCode(
                        Double.parseDouble(tokenList.get(calcPosition - 1)),
                        Double.parseDouble(tokenList.get(calcPosition + 1)),
                        tokenList.get(calcPosition));
                tokenList.set(calcPosition - 1, String.valueOf(value));
                tokenList.set(calcPosition, "");
                tokenList.set(calcPosition + 1, "");
            }
            for (String s : tokenList) {
                if (!s.isEmpty()) {
                    resultList.add(s);
                }
            }
        }

        if (resultList.size() <= 1) {
            return resultList;
        } else {
            return stackCalc(resultList);
        }
    }

    private ArrayList<String> calcSingleOperator(ArrayList<String> tokenList, String op) {
        ArrayList<String> resultList = new ArrayList<>();
        int location = -1;
        int[] braket = {-1, -1};

        // 연산자 위치 찾아온다
        location = tokenList.indexOf(op);
        if (location > -1) {
            //괄호위치 찾아본다
            braket = findBracketPosition(tokenList, location);
            ArrayList<String> newTokenList = new ArrayList<>();
            if (braket[BRACKET_END] < 2) {
                braket[BRACKET_END] = location + 1;
                newTokenList.add(tokenList.get(location + 1));
            } else {
                for (int i = braket[BRACKET_START] + 1; i < braket[BRACKET_END]; i++) {
                    newTokenList.add(tokenList.get(i));
                }
                newTokenList = stackCalc(newTokenList);
            }
            double value = calculateByOpCode(Double.parseDouble(newTokenList.get(0)), 0.0, op);
            resultList.add(String.valueOf(value));
        }
        return replaceCenterList(tokenList, location, braket[BRACKET_END], resultList);
    }

    private boolean hasSingleOperator(ArrayList<String> tokenList) {
        return (tokenList.contains("√") || tokenList.contains("!"));
    }

    private ArrayList<String> replaceCenterList(ArrayList<String> tokenList,
                                                int start,
                                                int end,
                                                ArrayList<String> centerList) {
        ArrayList<String> resultList = new ArrayList<>();
        for (int i = 0; i < start; i++) {
            resultList.add(tokenList.get(i));
        }
        for (String s : centerList) {
            resultList.add(s);
        }
        for (int i = end + 1; i < tokenList.size(); i++) {
            resultList.add(tokenList.get(i));
        }
        return resultList;
    }

    private int findCalcPosition(ArrayList<String> tokenList) {
        int firstRun = -1;
        int level = -1;
        int num = 0;
        for (int i = num; i < tokenList.size(); i++) {
            if ("^".equals(tokenList.get(i))) {
                if (level < 2) {
                    level = 2;
                    firstRun = i;
                }
            } else if ("*".equals(tokenList.get(i))) {
                if (level < 1) {
                    level = 1;
                    firstRun = i;
                }
            } else if ("/".equals(tokenList.get(i))) {
                if (level < 1) {
                    level = 1;
                    firstRun = i;
                }
            } else if ("%".equals(tokenList.get(i))) {
                if (level < 1) {
                    level = 1;
                    firstRun = i;
                }
            } else if ("+".equals(tokenList.get(i))) {
                if (level < 0) {
                    level = 0;
                    firstRun = i;
                }
            } else if ("-".equals(tokenList.get(i))) {
                if (level < 0) {
                    level = 0;
                    firstRun = i;
                }
            }
        }
        return firstRun;
    }

    // 처음 나오는 괄호의 시작 위치 끝위치 찾기
    private int[] findBracketPosition(ArrayList<String> tokenList, int start) {
        int[] bracketPosition = {-1, -1};
        int bracketCount = 0;
        for (int i = start; i < tokenList.size(); i++) {
            if (BRACKET_LEFT.equals(tokenList.get(i))) {
                bracketCount++;
                if (bracketPosition[BRACKET_START] < 0) {
                    bracketPosition[BRACKET_START] = i;
                }
            } else if (BRACKET_RIGHT.equals(tokenList.get(i))) {
                bracketCount--;
                if (bracketCount == 0) {
                    bracketPosition[BRACKET_END] = i;
                    break;
                }
            }
        }
        return bracketPosition;
    }

    // 입력된 문자열을 숫자 및 연산자 등으로 각각 분리해서 리스트로 만든다
    private ArrayList<String> splitByOperator(String data) {
        ArrayList tokenList = new ArrayList();
        String digits = "";
        for (int i = 0; i < data.length(); i++) {
            if (isDigitOrDot(data.charAt(i))) {
                digits += data.substring(i, i + 1);
            } else {
                // 연산자가 연속되어 나오는 경우 부호로 처리 (숫자와 함께 넣는다)
                if (digits.isEmpty() &&
                        // 그러나 오른쪽 괄호")" 뒤에 나온다면 연산자이고 아니면 부호
                        (tokenList.size() > 1 &&
                                !BRACKET_RIGHT.equals(tokenList.get(tokenList.size() - 1)) &&
                        (MINUS.equals(data.substring(i, i + 1)) || PLUS.equals(data.substring(i, i + 1))))) {
                    digits += data.substring(i, i + 1);
                } else {
                    if (!digits.isEmpty()) {
                        tokenList.add(digits);
                    }
                    tokenList.add(data.substring(i, i + 1));
                    digits = "";
                }
            }
        }
        if (!digits.isEmpty()) {
            tokenList.add(digits);
        }
        return tokenList;
    }

    // 숫자 계산
    private double calculateByOpCode(double op1, double op2, String opcode) {
        if ("+".equals(opcode)) {
            //더하기
            return op1 + op2;
        } else if ("-".equals(opcode)) {
            //빼기;
            return op1 - op2;
        } else if ("*".equals(opcode)) {
            //곱하기
            return op1 * op2;
        } else if ("/".equals(opcode)) {
            //나누기, 반올림은 지정된 수
            return op1 / op2;
        } else if ("^".equals(opcode)) {
            //제곱
            return Math.pow(op1, op2);
        } else if ("%".equals(opcode)) {
            //나머지
            return op1 % op2;
        } else if ("√".equals(opcode)) {
            //루트
            return Math.sqrt(op1);
        } else if ("!".equals(opcode)) {
            //팩토리얼
            return factorial(op1);
        }
        throw new RuntimeException("Operation Error");
    }

    private double factorial(double input) {
        if (input == 1.0) {
            return 1.0;
        }
        return factorial(input - 1) * input;
    }

    /****************************************************/
    private String recursiveCalc(String data) {
        // 계산 끝이면 종료
//        if (Pattern.matches("^[0-9.]*$", data)) {
//        if (calculateDone(data)) {
//            return data;
//        }

        // 남았으면 계산

        //괄호가 있으면 제일 바깥쪽 괄호 부터 추출해서 다시 호출
        if (data.contains("(")) {
            // 제일 처음 계산할 괄호 추출
//            data = calcUseBracket(data);
            int bracketStart = -1;
            int bracketEnd = -1;
            int bracketCount = 0;
            for (int i = 0; i < data.length(); i++) {
                if ("(".equals(data.substring(i, i + 1))) {
                    bracketCount++;
                    if (bracketStart < 0) {
                        bracketStart = i;
                    }
                } else if (")".equals(data.substring(i, i + 1))) {
                    bracketCount--;
                    if (bracketCount == 0) {
                        bracketEnd = i;
                        break;
                    }
                }
            }

            // 괄호안에 데이타가 있다면
            if (bracketEnd < bracketStart - 2) {
                //없으면
                throw new RuntimeException("Calculation Error");
            } else {
                //있으면
                String leftData = data.substring(0, bracketStart); //왼쪽괄호제거
                String rightData = data.substring(bracketEnd + 1); //오른쪽괄호제거
                String centerData = recursiveCalc(data.substring(bracketStart + 1, bracketEnd));

                data = leftData + centerData + rightData;
            }
        }
        //괄호가 없으면 계산
        data = calcNonBracket(data);

        return data;
    }

    private String calcNonBracket(String data) {
        String leftData = "";
        String rightData = "";
        String op1 = "";
        String op2 = "";
        int firstRun = getFirstRun(data);
        if (firstRun > 0) { // 연산자가 남아 있다면
            leftData = splitData(data, firstRun, -1, false);
            rightData = splitData(data, firstRun, 1, false);
            op1 = splitData(data, firstRun, -1, true);
            op2 = splitData(data, firstRun, 1, true);
            double value = calculateByOpCode(
                    Double.parseDouble(op1),
                    Double.parseDouble(op2),
                    data.substring(firstRun, firstRun + 1));
            data = leftData +
                    String.valueOf(value) +
                    rightData;
        }
        if (calculateDone(data)) {
            return data;
        } else {
            return calcNonBracket(data);
        }
    }


    // 계산 우선 순위를 반영하여 첫번째 연산할 위치 검색
    private int getFirstRun(String data) {
        int firstRun = -1;
        int level = -1;
        String oneChar;
        for (int i = 0; i < data.length(); i++) {
            oneChar = data.substring(i, i + 1);
            if ("^".equals(oneChar)) {
                if (level < 2) {
                    level = 2;
                    firstRun = i;
                }
            } else if ("*".equals(oneChar)) {
                if (level < 1) {
                    level = 1;
                    firstRun = i;
                }
            } else if ("/".equals(oneChar)) {
                if (level < 1) {
                    level = 1;
                    firstRun = i;
                }
            } else if ("%".equals(oneChar)) {
                if (level < 1) {
                    level = 1;
                    firstRun = i;
                }
            } else if ("+".equals(oneChar)) {
                if (level < 0) {
                    level = 0;
                    firstRun = i;
                }
            } else if ("-".equals(oneChar)) {
                if (level < 0) {
                    level = 0;
                    firstRun = i;
                }
            }
        }
        return firstRun;
    }

    // position을 기준으로 문자열을 돌려준다.
    private String splitData(String data, int position, int inc, boolean isTarget) {
        int current = position;
        String newData = "";
        while (true) {
            current += inc;
            if (current < 0 || current > data.length() - 1) {
                break;
            }
            if (isDigitOrDot(data.charAt(current))) {
                if (inc > 0) {
                    newData += data.substring(current, current + 1);
                } else {
                    newData = data.substring(current, current + 1) + newData;
                }
            } else {
                break;
            }
        }
        if (!isTarget) { // false 이면 추출하고 남은 문자열을 돌려 준다
            if (inc > 0) {
                newData = data.substring(current);
            } else {
                newData = data.substring(0, current + 1);
            }
        }
        return newData;
    }

    // 계산이 끝났는가?
    private boolean calculateDone(String data) {
        boolean result;
        if (Pattern.matches("^[0-9.]*$", data)) {
            result = true;
        } else if ("-".equals(data.substring(0, 1)) &&
                Pattern.matches("^[0-9.]*$", data.substring(1))) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    // 숫자 및 점 이외는 false
    private boolean isDigitOrDot(char token) {
        return (Character.isDigit(token) || token == 46);  // 46 = dot
    }

    // 받은 문자열에서 공백은 없애고, 필요한 변환 작업 실행
    private String prepareData(String data) {
        //계산 식 안의 빈칸을 없앤다.
        data = data.replace(" ", "");
        //괄호 앞 "*" 생략 처리, 단항 부호 "-","+" 처리(안됨)
        for (int i = 0; i < CONVERT_FROM.length; i++) {
            if (data.contains(CONVERT_FROM[i])) {
                data = data.replace(CONVERT_FROM[i], CONVERT_TO[i]);
            }
        }
        //앞에 부호있으면 변화
        if (data.charAt(0) == 43 || data.charAt(0) == 45) {
            data = "0" + data;
        }
        return data;
    }

    // 괄호 쌍이 맞는지 검사와 데이타 맨 오른쪽이 숫자이거나 괄호이어야 함
    private boolean checkBracketPair(String data) {
        char ch = data.charAt(data.length() - 1);   // 41="("
        return (isDigitOrDot(ch) || ch == 41)  // 데이타 끝이 숫자, 점, 오른쪽 괄호 이면서 쌍이 맞아야 됨
                && (data.replace("(", "").length() == data.replace(")", "").length());
    }

}
