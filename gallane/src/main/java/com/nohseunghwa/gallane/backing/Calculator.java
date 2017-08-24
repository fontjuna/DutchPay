package com.nohseunghwa.gallane.backing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.nohseunghwa.gallane.backing.Constants.HARF_ROUND_UP;
import static com.nohseunghwa.gallane.backing.Constants.OPERATION0;
import static com.nohseunghwa.gallane.backing.Constants.OPERATION1;
import static com.nohseunghwa.gallane.backing.Constants.OPERATION2;
import static com.nohseunghwa.gallane.backing.Constants.WORD_OPERATION1;
import static com.nohseunghwa.gallane.backing.Constants.WORD_OPERATION2;
import static com.nohseunghwa.gallane.backing.Constants.WORD_OPERATION3;

/**
 * Created by fontjuna on 2017-08-24.
 */

public class Calculator {
    //싱글톤
    private static Calculator instance = null;

    /**
     * 생성자
     */
    private Calculator() {
    }

    /**
     * 싱글톤
     */
    private static Calculator getInstance() {
        if (instance == null) {
            instance = new Calculator();
        }
        return instance;
    }

    /**
     * 외부에서 요청 될 계산 함수
     *
     * @param data 계산식
     * @return
     */
    public static BigDecimal Calculate(String data) {
        return Calculator.getInstance().calc(data);
    }

    /**
     * 내부에서 불리는 계산 함수
     *
     * @param data 계산식
     * @return 결과 값
     */
    private BigDecimal calc(String data) {
        //계산 식 안의 빈칸을 없앤다.
        data = data.replace(" ", "");
        //토큰으로 구분,즉 구분되는 수, 구분을 모두 분할
        // 예) (10+2)*(3+4)의 경우는 (, 10, +, 2, ), *, (, 3, +, 4, )로 분할 된다.
        List<Object> tokenStack = makeTokens(data);
        // 후위 표기식으로 변환한다.
        // 예) (, 10, +, 2, ), *, (, 3, +, 4, )의 경우는 10 2 + 3 4 + * 로 변경
        tokenStack = convertPostOrder(tokenStack);
        Stack<Object> calcStack = new Stack<Object>();
        // 후위 표기식 계산
        // List형식으로 tocken이 수가 나오면 스택, 연산자가 나오면 계산을 합니다.
        // 10 2 + 계산 12
        // 12 3 4 + 계산 12 7
        // 12 * 7 계산 84
        //
        for (Object tocken : tokenStack) {
            calcStack.push(tocken);
            calcStack = calcPostOrder(calcStack);
        }
        //스택에 값이 없으면 에러
        if (calcStack.size() != 1) {
            throw new RuntimeException("Calulator Error");
        }
        return (BigDecimal) calcStack.pop();
    }

    /**
     * 후위 표기식 계산
     *
     * @param calcStack 스택에 담겨져 있는 값
     * @return
     */
    private Stack<Object> calcPostOrder(Stack<Object> calcStack) {
        //스택의 가장 위의 값이 수면 계산 안함
        if (calcStack.lastElement().getClass().equals(BigDecimal.class)) {
            return calcStack;
        }
        BigDecimal op1 = null;
        BigDecimal op2 = null;
        String opcode = null;
        //연산자 포함 스택에 최소 2개 이상
        if (calcStack.size() >= 2) {
            //스택의 가장 위는 연산자
            opcode = (String) calcStack.pop();
            //다음 밑은 수
            op1 = (BigDecimal) calcStack.pop();
            //연산자가 수를 1개 필요한지 2개 필요한지 체크
            if (!opCodeCheck(opcode)) {
                op2 = (BigDecimal) calcStack.pop();
            }
            //계산
            BigDecimal result = calculateByOpCode(op1, op2, opcode);
            calcStack.push(result);
        }
        return calcStack;
    }

    /**
     * 연산자가 필요한 수의 개수
     *
     * @param opcode 연산자
     * @return 연산자가 수를 1개 필요하면 true, 연산자가 수를 2개 필요하면 false
     */
    private boolean opCodeCheck(String opcode) {
        return containWord(opcode, WORD_OPERATION2) || containWord(opcode, OPERATION1);
    }

    /**
     * 각 연산자의 계산 함수
     *
     * @param op1    수1
     * @param op2    수2
     * @param opcode 연산자
     * @return
     */
    private BigDecimal calculateByOpCode(BigDecimal op1, BigDecimal op2, String opcode) {
        if (OPERATION2[0].equals(opcode)) {
            //더하기
            return op1.add(op2);
        } else if (OPERATION2[1].equals(opcode)) {
            //빼기;
            return op1.subtract(op2);
        } else if (OPERATION2[2].equals(opcode)) {
            //곱하기
            return op1.multiply(op2);
        } else if (OPERATION2[3].equals(opcode)) {
            //나누기, 반올림은 지정된 수
            return op2.divide(op1, HARF_ROUND_UP, BigDecimal.ROUND_HALF_UP);
        } else if (OPERATION2[4].equals(opcode)) {
            //제곱
            return op2.pow(op1.intValue());
        } else if (OPERATION2[5].equals(opcode)) {
            //나머지
            return op2.remainder(op1);
        } else if (OPERATION1[0].equals(opcode)) {
            //팩토리얼
            return Factorial(op1);
        } else if (WORD_OPERATION2[0].equals(opcode)) {
            return BigDecimal.valueOf(Math.sin(op1.doubleValue()));
        } else if (WORD_OPERATION2[1].equals(opcode)) {
            return BigDecimal.valueOf(Math.sinh(op1.doubleValue()));
        } else if (WORD_OPERATION2[2].equals(opcode)) {
            return BigDecimal.valueOf(Math.asin(op1.doubleValue()));
        } else if (WORD_OPERATION2[3].equals(opcode)) {
            return BigDecimal.valueOf(Math.cos(op1.doubleValue()));
        } else if (WORD_OPERATION2[4].equals(opcode)) {
            return BigDecimal.valueOf(Math.cosh(op1.doubleValue()));
        } else if (WORD_OPERATION2[5].equals(opcode)) {
            return BigDecimal.valueOf(Math.acos(op1.doubleValue()));
        } else if (WORD_OPERATION2[6].equals(opcode)) {
            return BigDecimal.valueOf(Math.tan(op1.doubleValue()));
        } else if (WORD_OPERATION2[7].equals(opcode)) {
            return BigDecimal.valueOf(Math.tanh(op1.doubleValue()));
        } else if (WORD_OPERATION2[8].equals(opcode)) {
            return BigDecimal.valueOf(Math.atan(op1.doubleValue()));
        } else if (WORD_OPERATION2[9].equals(opcode)) {
            return BigDecimal.valueOf(Math.sqrt(op1.doubleValue()));
        } else if (WORD_OPERATION2[10].equals(opcode)) {
            return BigDecimal.valueOf(Math.exp(op1.doubleValue()));
        } else if (WORD_OPERATION2[11].equals(opcode)) {
            return BigDecimal.valueOf(Math.abs(op1.doubleValue()));
        } else if (WORD_OPERATION2[12].equals(opcode)) {
            return BigDecimal.valueOf(Math.log(op1.doubleValue()));
        } else if (WORD_OPERATION2[13].equals(opcode)) {
            return BigDecimal.valueOf(Math.ceil(op1.doubleValue()));
        } else if (WORD_OPERATION2[14].equals(opcode)) {
            return BigDecimal.valueOf(Math.floor(op1.doubleValue()));
        } else if (WORD_OPERATION2[15].equals(opcode)) {
            return BigDecimal.valueOf(Math.round(op1.doubleValue()));
        } else if (WORD_OPERATION3[0].equals(opcode)) {
            return op2.pow(op1.intValue());
        }
        throw new RuntimeException("Operation Error");
    }

    /**
     * 팩토리얼 알고리즘(재귀로 구현)
     *
     * @param input
     * @return
     */
    private BigDecimal Factorial(BigDecimal input) {
        if (BigDecimal.ONE.equals(input)) {
            return BigDecimal.ONE;
        }
        return Factorial(input.subtract(BigDecimal.ONE)).multiply(input);
    }

    /**
     * 후위표기식으로 변환 함수
     *
     * @param tokenList 토큰 리스트
     * @return
     */
    private List<Object> convertPostOrder(List<Object> tokenList) {
        List<Object> postOrderList = new ArrayList<>();
        Stack<String> exprStack = new Stack<>();
        Stack<String> wordStack = new Stack<>();
        for (Object token : tokenList) {
            if (BigDecimal.class.equals(token.getClass())) {
                //수면 그대로 입력
                postOrderList.add(token);
            } else {
                //연산자 처리
                exprAppend((String) token, exprStack, wordStack, postOrderList);
            }
        }
        String item = null;
        //남은 연산자 넣기
        while (!exprStack.isEmpty()) {
            item = exprStack.pop();
            postOrderList.add(item);
        }
        return postOrderList;
    }

    /**
     * 후위 계산법의 연산자 순서처리
     *
     * @param token         토큰
     * @param exprStack     연산자 스택(기호형)
     * @param wordStack     연산자 스택(문자형)
     * @param postOrderList 후위 계산 리스트(참초형)
     * @return
     */
    private void exprAppend(String token, Stack<String> exprStack, Stack<String> wordStack,
                            List<Object> postOrderList) {
        //토큰이 문자일 경우처리
        if (isWordOperation(token)) {
            //PI, E의 값
            BigDecimal wordValue = ConverterWordResult(token);
            if (wordValue != null) {
                postOrderList.add(wordValue);
            } else {
                wordStack.push(token);
            }
        } else if (OPERATION0[0].equals(token)) {
            //왼쪽 괄호(
            exprStack.push(token);
        } else if (OPERATION0[1].equals(token)) {
            //오른쪽 괄호)
            String opcode = null;
            while (true) {
                //문자 스택이 없을 때 까지
                if (wordStack.size() > 0) {
                    //기호를 스택에서 가져온다.
                    opcode = exprStack.pop();
                    //왼쪽 괄호(를 만나면 작성 끝
                    if (OPERATION0[0].equals(opcode)) {
                        opcode = wordStack.pop();
                        postOrderList.add(opcode);
                        break;
                    }
                    //스택 순서로 후위 계산 리스트에 값을 넣는다.
                    postOrderList.add(opcode);
                } else {
                    //연산 스택이 없으면 종료
                    if (exprStack.size() < 1) {
                        break;
                    }
                    opcode = exprStack.pop();
                    //왼쪽 괄호(를 만나면 작성 끝
                    if (OPERATION0[0].equals(opcode)) {
                        break;
                    }
                    postOrderList.add(opcode);
                }
            }
        } else if (OPERATION0[2].equals(token)) {
            //콤마 처리
            //콤마는 문자 연산자와 같이 사용하므로 콤마 연산자가 나왔는데 문자 연산자가 없으면 에러
            if (wordStack.size() < 1) {
                throw new RuntimeException("data error");
            }
            String opcode = null;
            while (true) {
                //연산 스택이 없으면 종료
                if (exprStack.size() < 1) {
                    break;
                }
                //왼쪽 괄호면 종료
                if (OPERATION0[0].equals(exprStack.lastElement())) {
                    break;
                }
                opcode = exprStack.pop();
                postOrderList.add(opcode);
            }
        } else if (isOperation(token)) {
            //연산자 처리
            String opcode = null;
            while (true) {
                //연산자가 없으면 입력
                if (exprStack.isEmpty()) {
                    exprStack.push(token);
                    break;
                }
                //연산자가 있으면
                opcode = exprStack.pop();
                //연산자 우선순위 체크 + * 가 만나면 *계산 먼저(스택에 늦게 들어가는 게 FIFO법칙으로 먼저 계산됨)
                if (exprOrder(opcode) <= exprOrder(token)) {
                    exprStack.push(opcode);
                    exprStack.push(token);
                    break;
                }
                postOrderList.add(opcode);
            }
        }
    }

    /**
     * 토큰 만드는 함수
     *
     * @param inputData
     * @return
     */
    private List<Object> makeTokens(String inputData) {
        List<Object> tokenStack = new ArrayList<>();
        StringBuffer numberTokenBuffer = new StringBuffer();
        StringBuffer wordTokenBuffer = new StringBuffer();
        int argSize = inputData.length();
        char token;
        for (int i = 0; i < argSize; i++) {
            //char형식으로 분할
            token = inputData.charAt(i);
            //수 토큰
            if (!isOperation(token)) {
                //문자열이 있으면 넣는다.
                setWordOperation(tokenStack, wordTokenBuffer);
                numberTokenBuffer.append(token);
                if (i == argSize - 1) {
                    setNumber(tokenStack, numberTokenBuffer);
                }
            } else {
                //연산자면 기존의 수를 입력
                setNumber(tokenStack, numberTokenBuffer);
                if (setOperation(tokenStack, token)) {
                    continue;
                }
                //기호 연산자가 아니면 문자 연산자
                wordTokenBuffer.append(token);
                setWordOperation(tokenStack, wordTokenBuffer);
            }
        }
        return tokenStack;
    }

    /**
     * 기호 연산자 입력
     *
     * @param tokenStack
     * @param token
     * @return
     */
    private boolean setOperation(List<Object> tokenStack, char token) {
        String tokenBuffer = Character.toString(token);
        if (containWord(tokenBuffer, OPERATION2) || containWord(tokenBuffer, OPERATION1)
                || containWord(tokenBuffer, OPERATION0)) {
            tokenStack.add(tokenBuffer);
            return true;
        }
        return false;
    }

    /**
     * 문자 연산자 입력
     *
     * @param tokenStack
     * @param tokenBuffer
     */
    private void setWordOperation(List<Object> tokenStack, StringBuffer tokenBuffer) {
        if (isWordOperation(tokenBuffer)) {
            tokenStack.add(tokenBuffer.toString());
            tokenBuffer.setLength(0);
        }
    }

    /**
     * 숫자 입력
     *
     * @param tokenStack
     * @param tokenBuffer
     */
    private void setNumber(List<Object> tokenStack, StringBuffer tokenBuffer) {
        if (tokenBuffer.length() > 0) {
            BigDecimal number = new BigDecimal(tokenBuffer.toString());
            tokenStack.add(number);
            tokenBuffer.setLength(0);
        }
    }

    /**
     * 연산자 체크 함수
     *
     * @param token
     * @param check
     * @return
     */
    private boolean containWord(String token, String[] check) {
        if (token == null) {
            return false;
        }
        for (String word : check) {
            if (word.equals(token)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 글자 연산자 여부 체크
     *
     * @param wordTokenBuffer
     * @return
     */
    private boolean isWordOperation(StringBuffer wordTokenBuffer) {
        String wordToken = wordTokenBuffer.toString();
        return isWordOperation(wordToken);
    }

    /**
     * 글자 연산자 여부 체크
     *
     * @param wordTokenBuffer
     * @return
     */
    private boolean isWordOperation(String wordToken) {
        return containWord(wordToken, WORD_OPERATION3) || containWord(wordToken, WORD_OPERATION2)
                || containWord(wordToken, WORD_OPERATION1);
    }

    /**
     * 수가 필요없는 연산자일 경우는 값을 내놓는다.(PI, E)
     *
     * @param wordToken
     * @return
     */
    private BigDecimal ConverterWordResult(String wordToken) {
        if (containWord(wordToken, WORD_OPERATION1)) {
            if (WORD_OPERATION1[0].equals(wordToken.toLowerCase())) {
                return BigDecimal.valueOf(Math.PI);
            } else if (WORD_OPERATION1[1].equals(wordToken.toLowerCase())) {
                return BigDecimal.valueOf(Math.E);
            }
        }
        return null;
    }

    /**
     * 기호 연산자인지 체크
     *
     * @param token
     * @return
     */
    private boolean isOperation(String token) {
        return containWord(token, OPERATION2) || containWord(token, OPERATION1);
    }

    /**
     * 기호 연산자인지 체크
     *
     * @param token
     * @return
     */
    private boolean isOperation(char token) {
        if ((token >= 48 && token <= 57) || token == 46) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 기호 우선순위 비교
     *
     * @param s
     * @return
     */
    private int exprOrder(String s) {
        if (s == null)
            throw new NullPointerException();
        int order = -1;
        if ("-".equals(s) || "+".equals(s)) {
            order = 0;
        } else if ("*".equals(s) || "/".equals(s) || "%".equals(s)) {
            order = 1;
        } else if ("^".equals(s) || "!".equals(s)) {
            order = 2;
        }
        return order;
    }
}
