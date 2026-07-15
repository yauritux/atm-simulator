package com.dkatalis;

import java.util.Scanner;

/**
 * @author yauritux@gmail.com
 * @version 1.0
 *
 */
public class AppRunner {
    public static void main( String[] args ) {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("$ ");
            String line = input.nextLine();

            if (line.equalsIgnoreCase("exit")) {
                break;
            }
        }
    }
}
