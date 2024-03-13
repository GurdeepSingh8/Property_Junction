package property.junction.co.methods;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;

public class NumberToWords {

    private static final String[] tensNamesEnglishAmerican = {
            "",
            " ten",
            " twenty",
            " thirty",
            " forty",
            " fifty",
            " sixty",
            " seventy",
            " eighty",
            " ninety"
    };

    private static final String[] numNamesEnglishAmerican = {
            "",
            " one",
            " two",
            " three",
            " four",
            " five",
            " six",
            " seven",
            " eight",
            " nine",
            " ten",
            " eleven",
            " twelve",
            " thirteen",
            " fourteen",
            " fifteen",
            " sixteen",
            " seventeen",
            " eighteen",
            " nineteen"
    };

    private NumberToWords() {
    }

    private static String convertLessThanOneThousand(int number) {
        String soFar;

        if (number % 100 < 20) {
            soFar = numNamesEnglishAmerican[number % 100];
            number /= 100;
        } else {
            soFar = numNamesEnglishAmerican[number % 10];
            number /= 10;

            soFar = tensNamesEnglishAmerican[number % 10] + soFar;
            number /= 10;
        }
        if (number == 0) return soFar;
        return numNamesEnglishAmerican[number] + " hundred" + soFar;
    }

    public static String convertToEnglishAmerican(long number) {
        // 0 to 999 999 999 999
        if (number == 0) {
            return "zero";
        }

        String snumber = Long.toString(number);

        // pad with "0"
        String mask = "000000000000";
        DecimalFormat df = new DecimalFormat(mask);
        snumber = df.format(number);

        // XXXnnnnnnnnn
        int billions = Integer.parseInt(snumber.substring(0, 3));
        // nnnXXXnnnnnn
        int millions = Integer.parseInt(snumber.substring(3, 6));
        // nnnnnnXXXnnn
        int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
        // nnnnnnnnnXXX
        int thousands = Integer.parseInt(snumber.substring(9, 12));

        String tradBillions;
        switch (billions) {
            case 0:
                tradBillions = "";
                break;
            case 1:
                tradBillions = convertLessThanOneThousand(billions)
                        + " billion ";
                break;
            default:
                tradBillions = convertLessThanOneThousand(billions)
                        + " billion ";
        }
        String result = tradBillions;

        String tradMillions;
        switch (millions) {
            case 0:
                tradMillions = "";
                break;
            case 1:
                tradMillions = convertLessThanOneThousand(millions)
                        + " million ";
                break;
            default:
                tradMillions = convertLessThanOneThousand(millions)
                        + " million ";
        }
        result = result + tradMillions;

        String tradHundredThousands;
        switch (hundredThousands) {
            case 0:
                tradHundredThousands = "";
                break;
            case 1:
                tradHundredThousands = "one thousand ";
                break;
            default:
                tradHundredThousands = convertLessThanOneThousand(hundredThousands)
                        + " thousand ";
        }
        result = result + tradHundredThousands;

        String tradThousand;
        tradThousand = convertLessThanOneThousand(thousands);
        result = result + tradThousand;

        // remove extra spaces!
        return result.replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
    }

    public static final String[] unitsIndianEnglish = {"", "One", "Two", "Three", "Four",
            "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve",
            "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
            "Eighteen", "Nineteen"};

    public static final String[] tensIndianEnglish = {
            "",         // 0
            "",     // 1
            "Twenty",   // 2
            "Thirty",   // 3
            "Forty",    // 4
            "Fifty",    // 5
            "Sixty",    // 6
            "Seventy",  // 7
            "Eighty",   // 8
            "Ninety"    // 9
    };

    public static String convertToIndianEnglish(final int n) {
        if (n < 0) {
            return "Minus " + convertToIndianEnglish(-n);
        }

        if (n < 20) {
            return unitsIndianEnglish[n];
        }

        if (n < 100) {
            return tensIndianEnglish[n / 10] + ((n % 10 != 0) ? " " : "") + unitsIndianEnglish[n % 10];
        }

        if (n < 1000) {
            return unitsIndianEnglish[n / 100] + " Hundred" + ((n % 100 != 0) ? " " : "") + convertToIndianEnglish(n % 100);
        }

        if (n < 100000) {
            return convertToIndianEnglish(n / 1000) + " Thousand" + ((n % 10000 != 0) ? " " : "") + convertToIndianEnglish(n % 1000);
        }

        if (n < 10000000) {
            return convertToIndianEnglish(n / 100000) + " Lakh" + ((n % 100000 != 0) ? " " : "") + convertToIndianEnglish(n % 100000);
        }

        return convertToIndianEnglish(n / 10000000) + " Crore" + ((n % 10000000 != 0) ? " " : "") + convertToIndianEnglish(n % 10000000);
    }
}
