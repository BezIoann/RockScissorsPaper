package com.rsp;
import org.apache.commons.codec.digest.HmacUtils;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import static org.apache.commons.codec.digest.HmacAlgorithms.HMAC_SHA_256;

public class RSP {

    private String sharedSecret;

    public RSP(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    public String calculateHmac(String data) {
        return new HmacUtils(HMAC_SHA_256, sharedSecret).hmacHex(data);
    }

    public static void menu(String[] moves) {
        System.out.println("Available moves:");
        for (int j = 0; j <= moves.length; j++) {
            System.out.println((j == moves.length) ? "0 - exit" : j+1 + " - " + moves[j]);
        }
    }


    public static String whoWin(String[] args, int movePlayer, int computerChooseInt) {
        String result = "Draw!";
        int len = (args.length/2);
        if (movePlayer >= len ) {
            if (movePlayer == computerChooseInt) {
                result = "Draw!";
            }
            else if (movePlayer-len <= computerChooseInt && computerChooseInt < movePlayer ) {
                result = "You win!";
            }
            else {
                result = "You lose!";
            }
        }
        else {
            if (movePlayer == computerChooseInt) {
                result = "Draw!";
            }
            else if (movePlayer>=computerChooseInt-len && movePlayer<computerChooseInt) {
                result = "You lose!";			}
            else {
                result = "You win!";
            }
        }
        return result;
    }

    public static String hex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte));
        }
        return result.toString();
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SecureRandom random = new SecureRandom();
        String secretCode = hex(random.generateSeed(16)).toUpperCase();
        RSP hmac = new RSP(secretCode);
        String[] moves = args;
        Set<String> setMoves = new HashSet<>(Arrays.asList(moves));
        if (moves.length >= 3 && moves.length % 2 != 0 && setMoves.size() == moves.length)  {
            int i = (int) (Math.random() * moves.length);
            System.out.println("HMAC: " + hmac.calculateHmac(moves[i]));
            menu(moves);
            System.out.print("Eneter your move: ");
            int answer = scanner.nextInt();

            while (answer < 0 || answer > moves.length) {
                menu(moves);
                System.out.print("Eneter your move: ");
                answer = scanner.nextInt();
            }
            if (answer != 0) {
                System.out.println("Your move: " + moves[answer-1]);
                System.out.println("Computer move: " + moves[i]);
                if(moves[i] == moves[answer-1]) {
                    System.out.println("Draw!");
                } else {
                    System.out.println(whoWin(moves, answer-1, i));
                }
                System.out.println("HMAC key: " + secretCode);
            }
        } else {
            System.out.println("Incorrect input! Should be odd number >= 3 of non-repeating lines, like: rock paper scissors");
        }
    }
}
