package com.windylung.cktp.service.text;

import com.windylung.cktp.dto.text.TextRequest;
import com.windylung.cktp.dto.text.TextResponse;

import java.util.ArrayList;
import java.util.List;

public class TextService {
    public TextResponse translateText(String text){

        TextResponse translator = translator(textSplitter(text));
        System.out.println("text = " + text + "len : " + text.length());
        return new TextResponse(translator.getData(), translator.getSaliva() * 100 / text.length());
    }

    public static List<Character> textSplitter(String text) {
        List<Character> phonemes = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch >= '가' && ch <= '힣') {
                int unicode = ch - '가';
                int jong = unicode % 28;
                int jung = ((unicode - jong) / 28) % 21;
                int cho = ((unicode - jong) / 28) / 21;

                phonemes.add((char) (0x1100 + cho));
                phonemes.add((char) (0x1161 + jung));
                if (jong != 0) {
                    phonemes.add((char) (0x11A7 + jong));
                }
            } else {
                phonemes.add(ch);
            }
        }
        return phonemes;
    }

//            if (i < phonemes.size() - 1) {
//                char nextCh = phonemes.get(i + 1);
//                if ((ch == 'ㄱ' || ch == 'ㄷ' || ch == 'ㅂ') && nextCh == 'ㅎ') {
//                    phonemes.set(i + 1, 'ㅇ');
//                }
//            }

    public static TextResponse translator(List<Character> phonemes) {

        StringBuilder sb = new StringBuilder();
        int saliva = 0;
        for (int i = 0; i < phonemes.size(); i++) {
            char ch = phonemes.get(i);

            if (isJungseong(ch)) {
                System.out.println("Jungseong detected: " + ch);
                sb.append(ch);
            } else if (isChoseong(ch)) {
                System.out.println("Choseong detected: " + ch);
                char newCh = translateChoseong(ch);
                sb.append(newCh);
                if(ch != newCh){
                    saliva++;
                }
            } else if (isJongseong(ch)) {

                System.out.println("Jongseong detected: " + ch);
                char newCh = translateJongseong(ch);
                if(ch != newCh){
                    saliva++;
                }
                sb.append(newCh);
            } else {
                System.out.println("Else case detected: " + ch);
                sb.append(ch);
            }
        }

        if (sb.length() == 0){
            return new TextResponse(sb.toString(),  0);
        }
        return new TextResponse(sb.toString(), saliva  );
    }

    public static boolean isJungseong(char ch) {
        return (ch >= 0x1161 && ch <= 0x1175);
    }

    public static boolean isChoseong(char ch) {
        return (ch >= 0x1100 && ch <= 0x1112);
    }
    public static boolean isJongseong(char ch) {
        return (ch >= 0x11A8 && ch <= 0x11C2);
    }
    public static char translateChoseong(char ch) {

        if (ch == 0x110E || ch == 0x110D) { // 초성 ㅊ 또는 ㅉ
            return 0x110C; // 초성 ㅈ
        }else if (ch == 0x110F || ch == 0x1101) { // 초성 ㅋ, ㄲ
            ch = 0x1100; // 초성 ㄱ
        }else if (ch == 0x1110 || ch == 0x1104) { // 초성 ㅌ, ㄸ
            ch = 0x1103; // 초성 ㄷ
        }else if (ch == 0x1111 || ch == 0x1108) { // 초성 ㅍ, ㅃ
            ch = 0x1107; // 초성 ㅂ
        }
        // 차짜카까타따파빠


        return ch;
    }

    // Translate final consonants if needed
    public static char translateJongseong(char ch) {

        if (ch == 0x11BE || ch == 0xD7F9) { // 종성 ㅊ 또는 ㅉ
            return 0x11BD; // 종성 ㅈ
        } else if (ch == 0x11BF || ch == 0x11A9) { // 종성 ㅋ, ㄲ
            return 0x11A8; // 종성 ㄱ
        } else if (ch == 0x11C0 || ch == 0xD7CD) { // 종성 ㅌ, ㄸ
            return 0x11AE; // 종성 ㄷ
        } else if (ch == 0x11C1 || ch == 0xD7E6) { // 종성 ㅍ, ㅃ
            return 0x11B8; // 종성 ㅂ
        }

        return ch;
    }

}







