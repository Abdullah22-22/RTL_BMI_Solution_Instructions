package org.example.demo1;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class BMICalculator {

    public static final double UNDERWEIGHT_LIMIT = 18.5;
    public static final double NORMAL_LIMIT = 25.0;
    public static final double OVERWEIGHT_LIMIT = 30.0;

    public double calculate(double weightKg, double heightCm) {
        if (weightKg <= 0) {
            throw new IllegalArgumentException("Weight must be greater than zero");
        }
        if (heightCm <= 0) {
            throw new IllegalArgumentException("Height must be greater than zero");
        }
        double heightM = heightCm / 100.0;
        return weightKg / (heightM * heightM);
    }

    public String format(double bmi) {
        DecimalFormat df = new DecimalFormat("#0.00",
                DecimalFormatSymbols.getInstance(Locale.US));
        return df.format(bmi);
    }

    public String category(double bmi) {
        if (bmi < UNDERWEIGHT_LIMIT) {
            return "underweight";
        }
        if (bmi < NORMAL_LIMIT) {
            return "normal";
        }
        if (bmi < OVERWEIGHT_LIMIT) {
            return "overweight";
        }
        return "obese";
    }
}