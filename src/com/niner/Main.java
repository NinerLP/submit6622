package com.niner;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class Main {

    private static PrintWriter prog;
    private static boolean w2239 = false;
    private static Map<Integer,Boolean> brokens = new HashMap<Integer,Boolean>();

    private static int check(Test test) {
        int errors = 0;
        int correct = Research.solve(test.weights.clone(),test.packSize);
        test.printFile();
        String mods = ".";
        //boolean w2239 = false;
        for (int mod = 2200; mod < 2240; mod++) {
            try {
                new bibliophile_6622_wa(mod).run();
                Scanner in = new Scanner(new File("bibliophile.out"));
                int ic = in.nextInt();
                int rv = 0;
                boolean[] used = new boolean[test.weights.length];
                for (int i = 0; i < ic; ++i) {
                    int item = in.nextInt() - 1;
                    if (item < 0 || item >= test.weights.length || used[item]) {
                        errors++;
                        System.out.println(Integer.toString(mod) + " -> index|used error");
                        mods+=(Integer.toString(mod)+".");
                        break;
                    }
                    rv += test.weights[item];
                }
                if (rv != correct) {
                    if (mod == 2239) {
                        w2239 = true;
                    }
                    brokens.put(mod,true);
                    errors++;
                    mods+=(Integer.toString(mod)+".");
                    System.out.println(Integer.toString(mod) + " -> wa error: expected " + Integer.toString(correct) + " got " +Integer.toString(rv));
                }
                in.close();
            } catch (Throwable e) {
                e.printStackTrace();
                errors++;
                mods+=("."+Integer.toString(mod));
                System.out.println(Integer.toString(mod) + " -> error");
            }
        }
        if (errors > 0) {
            //test.printFile("."+Integer.toString(errors)+mods);
        }

        return errors;
    }

    public static void main(String[] args) {
        try {
            for (int i = 2200; i < 2240; ++i) {
                brokens.put(i,false);
            }
            prog = new PrintWriter("progress.csv","UTF-8");
            prog.println("pass;gen;newbrk;bestbrk;allbrk");
            Random r = new Random();
            Test wrk = new Test(r.nextInt(50) + 1, 2009, 0.35);
            Test otst = wrk;
            int ff = check(wrk);
            int off = -1;
            int cnt = 0;
            int all;
            int gen = 0;
            int pass = 0;
            while (true) {
                gen++;
                pass++;
                if (ff > off) {
                    otst = wrk;
                    off = ff;
                    cnt = 0;
                } else if (ff == 0) {
                    cnt++;
                    if (cnt == 7 * off + 1) {
                        System.out.println("Restarted");
                        cnt = 0;
                        pass = 0;
                        otst = new Test(r.nextInt(50) + 1, 2009, 0.35);
                        off = check(otst);
                    }
                }
                wrk = new Test(otst, 0.5, 0.35);
                ff = check(wrk);
                all = 0;
                for (Map.Entry<Integer, Boolean> i : brokens.entrySet()) {
                    all += (i.getValue()) ? 1 : 0;
                }
                prog.println(Integer.toString(pass)+";"+Integer.toString(gen)+";"+Integer.toString(ff) + ";" + Integer.toString(off) + ";" + Integer.toString(all));
                System.out.println(Integer.toString(ff) + " mistakes opposed to " + Integer.toString(off));
                if (w2239) {
                    break;
                }
            }
            prog.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
