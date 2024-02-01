import java.util.Scanner;

public class sdes {
    static String key1, key2, key, plaintext;
    static int p10[] = { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6 };
    static int p8[] = { 6, 3, 7, 4, 8, 5, 10, 9 };
    static int ip[] = { 2, 6, 3, 1, 4, 8, 5, 7 };
    static int ipinv[] = { 4, 1, 3, 5, 7, 2, 8, 6 };
    static int ep[] = { 4, 1, 2, 3, 2, 3, 4, 1 };
    static int p4[] = { 2, 4, 3, 1 };
    static int s0[][] = {
            { 1, 0, 3, 2 },
            { 3, 2, 1, 0 },
            { 0, 2, 1, 3 },
            { 3, 1, 3, 2 }
    };
    static int s1[][] = {
            { 0, 1, 2, 3 },
            { 2, 0, 1, 3 },
            { 3, 0, 1, 0 },
            { 2, 1, 0, 3 }
    };

    public static String P10(String key) {
        String permuted10 = "";
        for (int i = 0; i < p10.length; i++) {
            permuted10 += key.charAt(p10[i] - 1);
        }
        return permuted10;
    }

    public static String P8(String key) {
        String permuted8 = "";
        for (int i = 0; i < p8.length; i++) {
            permuted8 += key.charAt(p8[i] - 1);
        }
        return permuted8;
    }

    public static String leftShift(String key) {
        String left = key.substring(0, 5);
        String right = key.substring(5, 10);

        String leftShift1 = left.substring(1, 5) + left.charAt(0);
        String rightShift1 = right.substring(1, 5) + right.charAt(0);

        return leftShift1 + rightShift1;
    }

    public static void generateKeys() {
        String permuted10 = P10(key);
        String xbitkey1 = leftShift(permuted10);
        key1 = P8(xbitkey1);
        String xbitkey2 = leftShift(leftShift(xbitkey1));
        key2 = P8(xbitkey2);
    }

    public static String IP(String plaintext) {
        String permutedip = "";
        for (int i = 0; i < ip.length; i++) {
            permutedip += plaintext.charAt(ip[i] - 1);
        }
        return permutedip;
    }

    public static String EP(String text) {
        String expanded = "";
        for (int i = 0; i < ep.length; i++) {
            expanded += text.charAt(ep[i] - 1);
        }
        return expanded;
    }

    public static String P4(String text) {
        String permuted4 = "";
        for (int i = 0; i < p4.length; i++) {
            permuted4 += text.charAt(p4[i] - 1);
        }
        return permuted4;
    }

    public static String leftSBox(String left) {
        String row = "" + left.charAt(0) + left.charAt(3);
        String col = "" + left.charAt(1) + left.charAt(2);
        int rowno = Integer.parseInt(row, 2);
        int colno = Integer.parseInt(col, 2);
        int s0val = s0[rowno][colno];
        String s0bin = Integer.toBinaryString(s0val);
        if (s0bin.length() == 1)
            s0bin = "0" + s0bin;
        return s0bin;
    }

    public static String rightSBox(String right) {
        String row = "" + right.charAt(0) + right.charAt(3);
        String col = "" + right.charAt(1) + right.charAt(2);
        int rowno = Integer.parseInt(row, 2);
        int colno = Integer.parseInt(col, 2);
        int s1val = s1[rowno][colno];
        String s1bin = Integer.toBinaryString(s1val);
        if (s1bin.length() == 1)
            s1bin = "0" + s1bin;
        return s1bin;
    }

    public static String SWAP(String left, String right) {
        return right + left;
    }

    public static String fiestalStructure(String key, String right) {
        String expanded = EP(right);
        String xored = XOR(expanded, key);
        String left = xored.substring(0, 4);
        String right1 = xored.substring(4, 8);
        String leftsbox = leftSBox(left);
        String rightsbox = rightSBox(right1);
        String p4 = P4(leftsbox + rightsbox);
        return p4;
    }

    public static String XOR(String a, String b) {
        String result = "";
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == b.charAt(i))
                result += "0";
            else
                result += "1";
        }
        return result;
    }

    public static String IPINV(String text) {
        String permutedipinv = "";
        for (int i = 0; i < ipinv.length; i++) {
            permutedipinv += text.charAt(ipinv[i] - 1);
        }
        return permutedipinv;
    }

    public static String encrypt() {
        String initialPermutedString = IP(plaintext);
        String left = initialPermutedString.substring(0, 4);
        String right = initialPermutedString.substring(4, 8);
        String fk1 = fiestalStructure(key1, right);
        left = XOR(left, fk1);
        String swapped = SWAP(left, right);
        left = swapped.substring(0, 4);
        right = swapped.substring(4, 8);
        String fk2 = fiestalStructure(key2, right);
        left = XOR(left, fk2);
        String temp = left + right;
        String cipher = IPINV(temp);
        return cipher;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter 10-bit key: ");
        key = sc.nextLine();
        System.out.print("Enter 8-bit plaintext: ");
        plaintext = sc.nextLine();
        sc.close();
        generateKeys();
        System.out.println("Key 1: " + key1);
        System.out.println("Key 2: " + key2);
        String cipher = encrypt();
        System.out.println("Cipher text: " + cipher);
    }
}