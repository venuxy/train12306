package com.venux.train.common.util;

import java.util.HashMap;
import java.util.Map;
import java.lang.System;

public class LongestSubsequence {

    public static int gao(int [] nums, int k) {
        Map<Integer, Integer> rx = new HashMap();

        rx.put(0, -1);

        int sum = 0;

        int ans = 0;

        for (int i = 0; i < nums.length; i++) {

            sum += nums[i];
            int x = sum % k;

            if (rx.containsKey(x)) {
                ans = Math.max(ans, i - rx.get(x));
            }

            if (!rx.containsKey(x)) {
                rx.put(x, i);
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        
        int k = 9;

        int [] nums = {1, 2, 3, 4, 5};

        int ans = gao(nums, k);

        System.out.println(ans);

    }
}
