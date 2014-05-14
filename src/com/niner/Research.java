package com.niner;

import java.io.*;
import java.util.*;

public class Research {
    private static void myAssert(String msg, boolean exp) {
        if (!exp) {
            throw new AssertionError(msg);
        }
    }

    private static void solveImpl(int[] items, int sum, boolean[] answer) {
        int n = items.length;
        int r = 0;
        for (int i : items) {
            r = Math.max(r, i);
        }
        int minWeight = Math.max(0, sum - r + 1);
        int maxWeight = sum + r;
        int breakItem = 0;
        int breakWeight = 0;
        for (int i = 0; i < items.length; ++i) {
            if (breakWeight + items[i] > sum) {
                breakItem = i;
                break;
            } else {
                breakWeight += items[i];
            }
        }

        int fw = n - breakItem + 1;
        int sw = maxWeight - minWeight + 1;
        int[][] s = new int[fw][sw];
        byte[][] b = new byte[fw][sw];

        final byte UNKNOWN = (byte)-1;
        final byte DECREASE_T = 1;
        final byte TAKE_T = 2;
        final byte TAKE_S = 3;

        Arrays.fill(s[0], 0, sum - minWeight + 1, -1);
        Arrays.fill(s[0], sum - minWeight + 1, sw, 0);
        s[0][breakWeight - minWeight] = breakItem;

        Arrays.fill(b[0], UNKNOWN);
        for (int t = 1; t < fw; ++t) {
            int[] sPrev = s[t - 1];
            int[] sCurr = s[t];
            byte[] bCurr = b[t];

            //Inherit the previous layer.
            System.arraycopy(sPrev, 0, sCurr, 0, sw);
            Arrays.fill(bCurr, DECREASE_T);

            //Try adding items
            for (int w = sum - minWeight; w >= 0; --w) {
                int nw = w + items[t + breakItem - 1];
                if (sCurr[nw] < sCurr[w]) {
                    sCurr[nw] = sCurr[w];
                    bCurr[nw] = TAKE_T;
                }
            }

            //Try removing items
            for (int w = sum + items[t + breakItem - 1] - minWeight; w > sum - minWeight; --w) {
                for (int j = sCurr[w] - 1; j >= sPrev[w]; --j) {
                    int nw = w - items[j];
                    if (sCurr[nw] < j) {
                        sCurr[nw] = j;
                        bCurr[nw] = TAKE_S;
                    }
                }
            }
        }

        for (int w = sum; w >= 0; --w) {
            if (s[fw - 1][w - minWeight] != -1) {
                int curw = w - minWeight;
                int tt = fw - 1;
                Arrays.fill(answer, 0, breakItem, true);
                while (tt > 0) {
                    int ss;
                    switch (b[tt][curw]) {
                        case DECREASE_T:
                            --tt;
                            break;
                        case TAKE_T:
                            answer[tt + breakItem - 1] = true;
                            curw -= items[tt + breakItem - 1];
                            --tt;
                            break;
                        case TAKE_S:
                            ss = s[tt][curw];
                            curw += items[ss];
                            answer[ss] = false;
                            break;
                    }
                }
                return;
            }
        }
    }

    private static void solve(int[] items, int sum, boolean[] answer) {
        int n = items.length;
        int itemSum = 0;
        int itemMax = 0;
        for (int i : items) {
            itemSum += i;
            itemMax = Math.max(itemMax, i);
        }
        if (itemSum <= sum) {
            Arrays.fill(answer, true);
        } else if (itemMax > sum) {
            int count = 0;
            for (int i : items) {
                count += i <= sum ? 1 : 0;
            }
            int[] ni = new int[count];
            boolean[] na = new boolean[count];
            for (int i = 0, j = 0; i < n; ++i) {
                if (items[i] <= sum) {
                    ni[j++] = items[i];
                }
            }
            if (count > 0) {
                solve(ni, sum, na);
            }
            for (int i = 0, j = 0; i < n; ++i) {
                if (items[i] <= sum) {
                    answer[i] = na[j++];
                }
            }
        } else {
            solveImpl(items, sum, answer);
        }
    }

    public static int solve(int[] items, int sum) {
        boolean[] ans = new boolean[items.length];
        solve(items, sum, ans);
        int rv = 0;
        for (int i = 0; i < ans.length; ++i) {
            if (ans[i]) {
                rv += items[i];
            }
        }
        myAssert("algorithm failed", rv <= sum);
        return rv;
    }

    private static int solveThirdParty(String[] exec, int[] items, int sum, int mod) {
        try {
            PrintWriter test = new PrintWriter("bibliophile.in");
            test.println(items.length + " " + sum);
            for (int i = 0; i < items.length; ++i) {
                test.print(items[i] + " ");
            }
            test.println();
            test.close();

//            try {
//                int exitCode = Runtime.getRuntime().exec(exec).waitFor();
//                if (exitCode != 0) {
//                    return -1;
//                }
                try {
                    new bibliophile_6622_wa(mod).run();
                } catch (Throwable th) {
                    return -1;
                }

                Scanner in = new Scanner(new File("bibliophile.out"));
                int ic = in.nextInt();
                int rv = 0;
                boolean[] used = new boolean[items.length];
                for (int i = 0; i < ic; ++i) {
                    int item = in.nextInt() - 1;
                    if (item < 0 || item >= items.length || used[item]) {
                        return -2;
                    }
                    rv += items[item];
                }
                return rv > sum ? -3 : rv;
//            } catch (InterruptedException ex) {
//                myAssert("interrupted", false);
//                return -100;
//            }
        } catch (IOException ex) {
            myAssert("IO error", false);
            return -200;
        }
    }

    static boolean flag = true;

    static class Drainer implements Runnable {
        public void run() {
            try {
                System.in.read();
            } catch (IOException ignored) {
            }finally {
                flag = false;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Random r = new Random();

        Thread drainer = new Thread(new Drainer());
        drainer.setDaemon(true);
        drainer.start();

        int mod = 1990;

        while (flag) {
            int n = r.nextInt(100) + 1;
            int[] items = new int[n];
            int totalSum = 0;
            for (int i = 0; i < n; ++i) {
                items[i] = 1 + r.nextInt(2009);
                totalSum += items[i];
            }
            int sum = r.nextInt(2 * totalSum + 1);

            int correct = -239;
            try {
                correct = solve(items.clone(), sum);
            } catch (Throwable th) {
                th.printStackTrace();
            }
            long t0 = System.currentTimeMillis();
            int testee = solveThirdParty(args, items.clone(), sum, mod);
            t0 = System.currentTimeMillis() - t0;
            if (correct != testee || t0 > 4500) {
                System.out.println();
                System.out.println("Mod: " + mod);
                System.out.println("Time: " + t0);
                System.out.println("Expected " + correct + " found " + testee);

                new File("bibliophile.in").renameTo(new File("bibliophile.in." + mod));
//                System.out.println("Test:");
//                System.out.println(items.length + " " + sum);
//                for (int i : items) {
//                    System.out.print(i + " ");
//                }
//                System.out.println();
                ++mod;
            }
            System.out.print(".");
        }
    }
}
