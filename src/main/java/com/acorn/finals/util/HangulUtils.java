package com.acorn.finals.util;

public class HangulUtils {
    private static final char[] leadingMap = {'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};
    private static final char[] medialMap = {'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ', 'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'};
    private static final char[] trailingMap = {0, 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};

    /**
     * 한글 자소 분리를 합니다.
     *
     * @param hangul 자소 분리가 되지 않은 한글 문자열 "한글"
     * @return 자소 분리가 된 한글 문자열 "ㅎㅏㄴㄱㅡㄹ"
     */
    public static String dissectHangul(String hangul) {
        StringBuilder sb = new StringBuilder();
        hangul.chars().forEach((c) -> {
            if (0xAC00 > c || c > 0xD7AF) {
                sb.append((char) c);
                return;
            }
            var leading = (c - 44032) / 588;
            var medial = ((c - 44032) % 588) / 28;
            var trailing = ((c - 44032) % 588) % 28;
            var validLeading = leading <= 18;
            var validMedial = validLeading && medial <= 20;
            var validTrailing = validMedial && 0 < trailing && trailing <= 27;

            if (validLeading) {
                sb.append(leadingMap[leading]);
            }
            if (validMedial) {
                var tmp = medialMap[medial];
                switch (tmp) {
                    case 'ㅘ':
                        sb.append('ㅗ');
                        sb.append('ㅏ');
                        break;
                    case 'ㅙ':
                        sb.append('ㅗ');
                        sb.append('ㅐ');
                        break;
                    case 'ㅚ':
                        sb.append('ㅗ');
                        sb.append('ㅣ');
                        break;
                    case 'ㅝ':
                        sb.append('ㅜ');
                        sb.append('ㅓ');
                        break;
                    case 'ㅞ':
                        sb.append('ㅜ');
                        sb.append('ㅔ');
                        break;
                    case 'ㅟ':
                        sb.append('ㅜ');
                        sb.append('ㅣ');
                        break;
                    case 'ㅢ':
                        sb.append('ㅡ');
                        sb.append('ㅣ');
                        break;
                    default:
                        sb.append(tmp);
                }
            }
            if (validTrailing) {
                var tmp = trailingMap[trailing];
                switch (tmp) {
                    case 'ㄳ':
                        sb.append('ㄱ');
                        sb.append('ㅅ');
                        break;
                    case 'ㄵ':
                        sb.append('ㄴ');
                        sb.append('ㅈ');
                        break;
                    case 'ㄶ':
                        sb.append('ㄴ');
                        sb.append('ㅎ');
                        break;
                    case 'ㄺ':
                        sb.append('ㄹ');
                        sb.append('ㄱ');
                        break;
                    case 'ㄻ':
                        sb.append('ㄹ');
                        sb.append('ㅁ');
                        break;
                    case 'ㄼ':
                        sb.append('ㄹ');
                        sb.append('ㅂ');
                        break;
                    case 'ㄽ':
                        sb.append('ㄹ');
                        sb.append('ㅅ');
                        break;
                    case 'ㄾ':
                        sb.append('ㄹ');
                        sb.append('ㅌ');
                        break;
                    case 'ㄿ':
                        sb.append('ㄹ');
                        sb.append('ㅍ');
                        break;
                    case 'ㅀ':
                        sb.append('ㄹ');
                        sb.append('ㅎ');
                        break;
                    case 'ㅄ':
                        sb.append('ㅂ');
                        sb.append('ㅅ');
                        break;
                    default:
                        sb.append(tmp);
                }
            }
        });
        return sb.toString();
    }
}
