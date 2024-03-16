package ru.tgc.collections;

import java.util.Comparator;

public class MainCollections {

    public static void main(String[] args) {
        MyArrayList<Integer> integers = new MyArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(5);
        integers.add(1);
        integers.add(2);
        integers.add(2);
        integers.add(2);
        integers.add(2);
        integers.add(2);
        integers.add(2);
        integers.add(2);
        integers.add(2);
        integers.add(2);
        integers.sort(Comparator.reverseOrder());
        System.out.println(integers);
        integers.add(1000);
        System.out.println(integers);
    }
}
