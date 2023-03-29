package com.gly.zhongnan.basic;

import java.math.BigDecimal;

/**
 * BigDecimal测试
 *
 * @author EugeneGe
 * @date 2023-03-24 9:22
 */
public class BigDecimalTest {
    public static void main(String[] args) {
//        BigDecimal time = BigDecimal.ONE;
//        BigDecimal num = BigDecimal.ZERO;
//        System.out.println(time.divide(num));
        equalsTest();
    }

    private static void equalsTest() {
        //数值的大小是一样的,只有精度不一样
        BigDecimal a = new BigDecimal("0.0");
        BigDecimal b = new BigDecimal("0.000000000");
        BigDecimal c = BigDecimal.ZERO;
        BigDecimal d = new BigDecimal("1.100");
        BigDecimal e = new BigDecimal("1.10000000000");
        System.out.println("b.equals(a): " + b.equals(a));
        System.out.println("b.equals(c): " + b.equals(c));
        System.out.println("d.equals(e): " + d.equals(e));
        System.out.println("b.compareTo(a) == 0: " + (b.compareTo(a) == 0));
        System.out.println("b.compareTo(c) == 0: " + (b.compareTo(c) == 0));
        System.out.println("d.compareTo(e) == 0: " + (d.compareTo(e) == 0));
    }
}
