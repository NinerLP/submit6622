package com.niner;

import java.util.ArrayList;
import java.util.Random;
import java.io.*;

public class Test {

    public Test() {
        weights = new int[1];
        packSize = 0;
        wrongAnswers = 0;
    }

    public Test(Test anc, double cQ, double psQ) {
        weights = new int[anc.weights.length];
        generator = new Random();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = (int) ((generator.nextInt(2009) - anc.weights[i]) * cQ) + anc.weights[i]+1;
            packSize += weights[i];
        }
        packSize = (int) Math.round(packSize * psQ);
    }

    public Test(int taskSize, int maxWeight, double k) {
        weights = new int[taskSize];
        generator = new Random();
        for (int i = 0; i < taskSize; i++) {
            weights[i] =(int) Math.abs(generator.nextLong() % maxWeight) + 1;
            packSize += weights[i];
        }
        packSize = (int) Math.round(packSize * k);
        wrongAnswers = 0;
    }



    public static Test crossover(Test a, Test b, int cut, double k) {
        Test result = new Test();
        if (cut > a.weights.length) {
            cut = a.weights.length;
        }
        int bsize = b.weights.length - cut;
        if (bsize <= 0) {
            result.weights = new int[cut];
        } else {
            result.weights = new int[cut+bsize];
        }
        for (int i = 0; i < cut; i++) {
            result.weights[i] = a.weights[i];
            result.packSize += a.weights[i];
        }
        for (int i = cut; i < b.weights.length; i++) {
            result.weights[i] = b.weights[i];
            result.packSize += b.weights[i];
        }
        result.packSize = (int) Math.round(result.packSize * k);
        return result;
    }

    public void print() {
        for (int i = 0; i < weights.length; i++) {
            System.out.print(Integer.toString(weights[i])+" ");
        }
        System.out.println("\n"+Integer.toString(packSize));
        System.out.println(Integer.toString(wrongAnswers));
    }

    public void printFile(String mods){
        if (mods != "") {
            mods += ".";
            mods +=  Long.toString(Math.abs(generator.nextLong()));
        }
        try {
            PrintWriter writer = new PrintWriter("bibliophile.in"+mods,"UTF-8");
            writer.println(Integer.toString(weights.length)+" "+Integer.toString(packSize));
            for (int i = 0; i < weights.length; i++) {
                writer.print(Integer.toString(weights[i])+" ");
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    public void printFile() {
        printFile("");
    }

    public int[] weights;
    public int packSize;
    public int wrongAnswers;
    Random generator;
}
