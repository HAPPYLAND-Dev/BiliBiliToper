package me.xiaozhangup.bilibilitoper.utils.tools;

public class INumber {

    public static Integer getRandom(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    public static double getRandom(final double start, final double end) {
        return (Math.random() * (end - start + 1) + start);
    }

}