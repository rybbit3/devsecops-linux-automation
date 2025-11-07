package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {

    Calculator calc = new Calculator();

    @Test
    void testAdd() {
        assertEquals(5, calc.add(2, 3));
    }

    @Test
    void testSubtract() {
        assertEquals(1, calc.subtract(3, 2));
    }

    @Test
    void testCheckPositive_WhenPositive() {
        // "Positive" 케이스 (if)만 테스트하고,
        // "Not Positive" 케이스 (else)는 일부러 테스트하지 않음
        assertEquals("Positive", calc.checkPositive(10));
    }
}