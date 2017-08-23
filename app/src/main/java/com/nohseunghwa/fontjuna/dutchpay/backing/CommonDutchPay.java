package com.nohseunghwa.fontjuna.dutchpay.backing;

/**
 * Created by fontjuna on 2017-08-20.
 */

public class CommonDutchPay {

    /**
     * Created by fontjuna on 2017-08-15.
     *
     *  아이템즈 ( 아이템 / 아이템 )
     *    아이템 ( 레프트아이템 @ 라이트아이템 )
     *      레프트아이템 ( 타이틀 : 금액들 )
     *        금액들 ( 금액 + [-]금액 )
     *      라이트아이템 ( 멤버들 , 멤버 )
     *        멤버들 ( 멤버 , 멤버 )
     *        멤버 ( 멤버 ! 배율 )
     *
     *
     *  아이템(= 레프트( = 타이틀 : 금액들( = 금액 + 금액 ) @ 라이트 ) / 아이템
     *
     * '/' 그룹과 그룹 구분
     * '=' 그룹에서 그룹명칭과 내용 구분
     * ':' 내용에서 금액과 구성멤버 구분
     * ',' 구성멤버에서 멤버와 멤버 구분
     * '!' 멤버에서 멤버와 멤버의 비중 구분
     * '~' 숫자와 숫자사이에 써서 이름대신 순번으로 대치 1~10 (1부터 10까지 10명)
     */

    // 계산용
    public static final String MINUS = "-";
    public static final String PLUS = "+";
    public static final String DOT = ".";
    public static final String COMMA = ",";
    public static final String CALC = PLUS + MINUS + DOT + COMMA;

    // 구분자 용
    public static final String UNIT = "#UNIT#";
    public static final String ITEMnITEM = "/";
    public static final String TITLEnMONEY = ":";
    public static final String MEMBER2MEMBER = "~";
    public static final String LEFTnRIGHT = "@";
    public static final String MEMBERnMEMBER = ",";
    public static final String MEMBERnRATIO = "!";
    public static final String DELIMITER = ITEMnITEM + TITLEnMONEY + MEMBER2MEMBER + LEFTnRIGHT + MEMBERnMEMBER + MEMBERnRATIO;

    // 입력 텍스트
    public static final String DIGIT = "0-9";
    public static final String ENGLISH = "A-Za-z";
    public static final String KOREAN = "ㄱ-힣";
    public static final String TEXT = DIGIT + ENGLISH + KOREAN; // "0-9A-Za-zㄱ-힣"

    // 텍스트 조합
    public static final String CHARS = CALC + DELIMITER;
    public static final String TEXT_CHARS = TEXT + CHARS;       // "0-9A-Za-zㄱ-힣-+./:,!"

    // 입력 텍스트 검사
    public static final String VALID_CHARACTERS_ALL = String.format("^[%s]*$", TEXT_CHARS);
    public static final String VALID_CHARACTERS_TEXT = String.format("^[%s]*$", TEXT);
    public static final String VALID_CHARACTERS_RATIO = String.format("^[%s]*$", DIGIT + DOT);
    public static final String VALID_CHARACTERS_MEMBER = String.format("^[%s]*$", TEXT + MEMBERnMEMBER + MEMBERnRATIO + DOT + MEMBER2MEMBER);
    public static final String VALID_CHARACTERS_AMOUNT = String.format("^[%s]*$", DIGIT + DOT + COMMA + PLUS + MINUS); // MINUS가 젤 뒤로 와야 작동함

    // 메세지
    public static final String ERROR_WRONG_EXPRESSION = "수식에 불필요한 문자가 들어 있습니다.";
    public static final String ERROR_EMPTY_INPUT = "입력된 내용이 없습니다.";
    public static final String ERROR_INVALID = "입력된 내용이 규칙에 맞지 않습니다.";
    public static final String ERROR_DELIMITER = "구분자 사용이 잘 못 되었습니다.";
    public static final String ERROR_IN_MEMBER = "나눌 인원에 불필요한 문자가 있습니다.";
    public static final String ERROR_IN_RATIO = "나눌 배율에 불필요한 문자가 있습니다.";
    public static final String ERROR_IN_AMOUNT = "금액에 불필요한 문자가 있습니다.";
    public static final String ERROR_IN_DONT_DIVIDE = "금액과 나눌 사람들이 명확하지 않습니다.";

    /***
     * 똑 같이 나눌경우 (12,000원을 A,B,C 세사람이 분배)
     * 12000:A,B,C
     *
     * 꼴찌한 횟수만큼 내기 (총액 6,000원 게임당 1,000원, A=1회, B=2회, C=3회)
     * 6000:A,B!2,C!3         (6,000원중 A=1,000원, B=2,000원, C=3,000원)
     *
     * 낼 금액이 두가지 이상인 경우("/"로 구분)
     * 6000:A,B,C/3000:A,D    (A=3,500원, B,C=2,000원, D=1,500원)
     *
     * 찬조금액을 빼고 계산할 경우 (총액이 10,000원 이고 찬조금이 1,000원 가정)
     * 10000-1000:A,B,C       (9,000원중 A,B,C=3,000원)
     * 9000:A,B,C             (9,000원중 A,B,C=3,000원)
     *
     * 특정인이 정해진 금액을 더 낼 경우
     * 5000-1000:A,B,C/1000:C (5,000원중 C가 1,000원을 더 낸다.| A,B=1,333원,C=2,333원)
     * 4000:A,B,C/1000:C      (5,000원중 C가 1,000원을 더 낸다.| A,B=1,333원,C=2,333원)
     * 5000:A,B,C/1000:C      (6,000원중 C가 1,000원을 더 낸다.| A,B=1,667원,C=2,667원)
     *
     * 특정인이 정해진 금액만 낼 경우
     * 5000-1000:A,B/1000:C (5,000원중 C는 1,000원만 낸다.| A,B=2,000원,C=1,000원)
     * 4000:A,B/1000:C      (5,000원중 C는 1,000원만 낸다.| A,B=2,000원,C=1,000원)
     *
     * 특정인이 다른 사람 몫까지 낼 경우 (C의 몫을 다른 사람이 낸다는 가정)
     * 10000:A,B,C!0,D!2      (10,000원 중 A,B=2,500원, D=5,000원) => 10000:A,B,D!2
     * 10000:A,B!1.5,C!0,D!1.5(10,000원 중 A=2,500원, B,D=2,750원) => 10000:A,B!1.5,D!1.5
     */

    public static final String HINT_INFORMATION
        = "구분자로   " + LEFTnRIGHT + "   " + MEMBERnMEMBER + "   " + MEMBERnRATIO + "   " + ITEMnITEM + "  4개 문자를 사용합니다"

        + "\n\n▣ 똑 같이 나눌 때( '" + LEFTnRIGHT + "' 로 금액과 사람 구분)"
        + "\n  입력 12000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnMEMBER + "C (결과 A,B,C=4,000원)"

        + "\n\n▣ 꼴찌한 횟수 만큼 낼때( '" + MEMBERnRATIO + "' 뒤에 횟수(배율))"
        + "\n  입력 12000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnRATIO + "2" + MEMBERnMEMBER + "C" + MEMBERnRATIO + "3 (" + MEMBERnRATIO + "1 은 없어도 같음)"
        + "\n   (결과 A=2,000/B=4,000/C=6,000원)"

        + "\n\n▣ 나눌 금액이 두가지 이상일 때( '" + ITEMnITEM + "' 로 구분)"
        + "\n  입력 9000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnMEMBER + "C" + ITEMnITEM + "3000" + LEFTnRIGHT + "C" + MEMBERnMEMBER + "D"
        + "\n    (결과 A,B=3,000/C=4,500/D=1,500원)"

        + "\n\n▣ C가 3,000원을 더내야 할 때"
        + "\n  입력 12000-3000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnMEMBER + "C" + ITEMnITEM + "3000" + LEFTnRIGHT + "C"
        + "\n  또는 9000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnMEMBER + "C" + ITEMnITEM + "3000" + LEFTnRIGHT + "C"
        + "\n    (결과 A,B=3,000/C=6,000원)"

        + "\n\n▣ C는 3,000원만 낼 때"
        + "\n  입력 12000-3000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + ITEMnITEM + "3000" + LEFTnRIGHT + "C"
        + "\n  또는 9000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + ITEMnITEM + "3000" + LEFTnRIGHT + "C"
        + "\n    (결과 A,B=4,500/C=3,000원)"

        + "\n\n▣ B의 몫을 C가 낼 때"
        + "\n  입력 12000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnRATIO + "0" + MEMBERnMEMBER + "C" + MEMBERnRATIO + "2"
        + "\n  또는 12000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "C" + MEMBERnRATIO + "2 (B" + MEMBERnRATIO + "0 배율이 0은 생략)"
        + "\n    (결과 A=4,000/C=8,000원)"

        + "\n\n▣ B의 몫을 C,D가 낼 때"
        + "\n  입력 12000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "C" + MEMBERnRATIO + "1.5" + MEMBERnMEMBER + "D" + MEMBERnRATIO + "1.5"
        + "\n    (결과 A=3,000/B=0/C,D=4,500원)"

        + "\n\n▣ 찬조금액(=3,000원) 만큼 감해 줄 때"
        + "\n  입력 12000-3000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnMEMBER + "C"
        + "\n  또는 12000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnMEMBER + "C" + ITEMnITEM + "-3000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnMEMBER + "C"
        + "\n  또는 9000" + LEFTnRIGHT + "A" + MEMBERnMEMBER + "B" + MEMBERnMEMBER + "C"
        + "\n  (결과 각 3,000원)"

        + "\n\n▣ 더 복잡한 경우도 위의 예를 응용해 보세요";

    public static final String HINT_EXPRESSION = "금액" + LEFTnRIGHT + "이름" + MEMBERnRATIO + "배율" + MEMBERnMEMBER + "...";
    public static final String INPUT_EXPRESSION = "input";
    public static final String NO_BANKING = "\n\n(이 내용은 메세지에서 제외 됩니다."
            + "\n계좌 정보를 같이 보내시려면"
            + "\n예금주, 은행명, 계좌번호가 있어야 합니다.)";

    public static final String TAB_TITLE_1 = "가를것들";
    public static final String TAB_TITLE_2 = "쉬운가름";
    public static final String TAB_TITLE_3 = "가름전달";

    public static final int TAB_SELECT_KIDS = 0;
    public static final int TAB_SELECT_PAPA = 1;
    public static final int TAB_SELECT_SEND = 2;

//    public boolean isError();
//    public String  getError();


}
