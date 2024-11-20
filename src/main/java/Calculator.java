import java.util.Scanner;
import java.util.function.DoubleBinaryOperator;
import java.util.Map;

public class Calculator {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<Character, DoubleBinaryOperator> OPERATIONS = Map.of(
            '+', (a, b) -> a + b,
            '-', (a, b) -> a - b,
            '*', (a, b) -> a * b,
            '/', (a, b) -> {
                if (b == 0) throw new ArithmeticException("Деление на ноль невозможно");
                return a / b;
            }
    );

    public static void main(String[] args) {
        while (true) {
            displayMenu();
            calculate();
        }
    }

    private static void displayMenu() {
        System.out.println("\nКалькулятор");
        System.out.println("Для выхода введите 'q'");
    }

    private static void calculate() {
        double num1 = getNumber("первое");
        char operation = getOperation();
        double num2 = getNumber("второе");
        printResult(num1, num2, operation);
    }

    private static double getNumber(String ordinal) {
        while (true) {
            System.out.printf("Введите %s число (можно отрицательное, для выхода 'q'):%n", ordinal);
            String input = scanner.next();

            if (isQuit(input)) System.exit(0);

            try {
                return parseNumber(input);
            } catch (NumberFormatException e) {
                if (!handleError("Введено некорректное число")) {
                    return Double.MIN_VALUE;
                }
            }
        }
    }

    private static double parseNumber(String input) {
        if (input.startsWith("-")) {
            String numberPart = input.substring(1);
            return isNumeric(numberPart) ? -Double.parseDouble(numberPart) :
                    Double.parseDouble(input);
        }
        return Double.parseDouble(input);
    }

    private static char getOperation() {
        while (true) {
            System.out.println("Введите операцию (+, -, *, /) или 'q' для выхода:");
            String input = scanner.next();

            if (isQuit(input)) System.exit(0);

            if (isValidOperation(input)) {
                return input.charAt(0);
            }

            if (!handleError("Неверная операция")) {
                return 'q';
            }
        }
    }

    private static void printResult(double num1, double num2, char operation) {
        try {
            double result = OPERATIONS.get(operation).applyAsDouble(num1, num2);
            String formattedOutput = formatCalculation(num1, num2, operation, result);
            System.out.println(formattedOutput);
        } catch (ArithmeticException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static String formatCalculation(double num1, double num2, char operation, double result) {
        String num1Str = formatNumber(num1);
        String num2Str = formatNumber(num2);
        return String.format("Результат: %s %c %s = %.2f", num1Str, operation, num2Str, result);
    }

    private static String formatNumber(double number) {
        return number < 0 ? "(" + number + ")" : String.valueOf(number);
    }

    private static boolean handleError(String errorMessage) {
        System.out.printf("Ошибка: %s. Хотите повторить? (y/n)%n", errorMessage);
        return retryPrompt();
    }

    private static boolean retryPrompt() {
        while (true) {
            String response = scanner.next().toLowerCase();
            if (response.equals("y")) return true;
            if (response.equals("n")) return false;
            System.out.println("Пожалуйста, введите 'y' для повтора или 'n' для возврата в начало:");
        }
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValidOperation(String input) {
        return input.length() == 1 && OPERATIONS.containsKey(input.charAt(0));
    }

    private static boolean isQuit(String input) {
        return input.equalsIgnoreCase("q");
    }
}