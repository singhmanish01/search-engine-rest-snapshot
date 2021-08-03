package com.mss;

import java.util.*;
import java.io.*;

public class MaxPairwiseProduct {
    public static void main(String[] args) {
        FastScanner scanner = new FastScanner(System.in);
        int n = scanner.nextInt();
        int[] numbers = new int[n];
        for (int i = 0; i < n; i++) {
            numbers[i] = scanner.nextInt();
        }
        System.out.println(getMaxPairwiseProduct(numbers));
    }
    static long getMaxPairwiseProduct(int[] numbers) {
        long max_product = 0L;
        int n = numbers.length;
        System.out.println(Arrays.toString(numbers));
        int index1 = 0;
        for (int first = 1; first < n; ++first) {
            if(numbers[index1]<numbers[first])
                index1 = first;
        }
        int index2 = 0;
        for (int first = 1; first < n; ++first) {
            if((numbers[index2]<numbers[first])&&(index1!=first))
                index2 = first;
        }
        max_product = numbers[index1]*numbers[index2];
        return max_product;
    }



    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner(InputStream stream) {
            try {
                br = new BufferedReader(new
                        InputStreamReader(stream));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }

}
