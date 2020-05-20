/*
 * Copyright (c) 2020 by Marfeel Solutions (http://www.marfeel.com)
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Marfeel Solutions S.L and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Marfeel Solutions S.L and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Marfeel Solutions SL.
 */

public class CountingBits {


    public int[] countBits(int num) {
        if (num == 0)
            return new int[]{0};

        if (num == 1)
            return new int[]{0, 1};

        int[] dp = new int[num + 1];
        dp[0] = 0;
        dp[1] = 1;

        for (int i = 2; i <= num; i++) {
            dp[i] = i % 2 == 0 ? dp[i / 2] : dp[i / 2] + 1;
        }

        return dp;
    }

    int[] base2logs(int num) {
        if (num == 0) {
            return new int[0];
        }
        int log2 = getLog2(num);
        int base2 = 1;
        int[] res = new int[log2 + 1];
        for (int i = 0; i <= log2; i++) {
            res[i] = base2;
            base2 *= 2;
        }
        return res;
    }

    private int getLog2(int num) {
        return (int) Math.floor(Math.log(num) / Math.log(2));
    }
}
