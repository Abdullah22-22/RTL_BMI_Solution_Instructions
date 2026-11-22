package org.example.demo1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class BMICalculatorTest {

    private final BMICalculator calculator = new BMICalculator();


    @Test
    void calculate_normalWeightAndHeight_returnsCorrectBmi() {
        double bmi = calculator.calculate(70, 175);
        assertEquals(22.857, bmi, 0.001);
    }

    @Test
    void calculate_zeroWeight_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(0, 175));
    }

    @Test
    void calculate_negativeWeight_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(-10, 175));
    }

    @Test
    void calculate_zeroHeight_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(70, 0));
    }

    @Test
    void calculate_negativeHeight_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(70, -170));
    }


    @Test
    void format_usesDotAsDecimalSeparator() {
        String result = calculator.format(22.8571);
        assertEquals("22.86", result);
    }

    @Test
    void format_roundsToTwoDecimals() {
        assertEquals("25.00", calculator.format(25.0));
    }


    @ParameterizedTest
    @CsvSource({
            "17.0, underweight",
            "18.49, underweight",
            "18.5, normal",
            "24.99, normal",
            "25.0, overweight",
            "29.99, overweight",
            "30.0, obese",
            "40.0, obese"
    })
    void category_returnsCorrectWhoCategory(double bmi, String expected) {
        assertEquals(expected, calculator.category(bmi));
    }
}