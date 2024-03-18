package ru.tgc.collections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainCollections {

    public static void main(String[] args) {
        MyArrayList<Integer> integers = new MyArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(5);
        integers.add(1);
        integers.add(2);
        integers.add(null);
        integers.sort(Comparator.reverseOrder());
        System.out.println(integers);
        integers.add(1000);
        System.out.println(integers);
        List<Integer> integers1 = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            integers1.add(i);
        }
        integers.addAll(integers1);
        System.out.println(integers1);
        System.out.println(integers);
    }
}
