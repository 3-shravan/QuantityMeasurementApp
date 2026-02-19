package com.shravan;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        QuantityMeasurementApp quantityMeasurementApp = new QuantityMeasurementApp();

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter first value in feet: ");
            if (!scanner.hasNextDouble()) {
                System.out.println("Invalid input. Please enter a numeric value.");
                return;
            }
            double firstInput = scanner.nextDouble();

            System.out.print("Enter second value in feet: ");
            if (!scanner.hasNextDouble()) {
                System.out.println("Invalid input. Please enter a numeric value.");
                return;
            }
            double secondInput = scanner.nextDouble();

            boolean result = quantityMeasurementApp.areEqual(firstInput, secondInput);
            System.out.println("Input: " + firstInput + " ft and " + secondInput + " ft");
            System.out.println("Output: Equal (" + result + ")");
        }
    }
}
