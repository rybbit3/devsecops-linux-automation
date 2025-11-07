package com.example;

public class Calculator {

    // 덧셈
    public int add(int a, int b) {
        return a + b;
    }

    // 뺄셈
    public int subtract(int a, int b) {
        return a - b;
    }

    // 테스트 커버리지를 확인하기 위한 분기문
    public String checkPositive(int number) {
        if (number > 0) {
            return "Positive";
        } else {
            return "Not Positive";
        }
    }
}