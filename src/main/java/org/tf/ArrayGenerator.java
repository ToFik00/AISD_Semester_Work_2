package org.tf;

import java.util.Random;

public abstract class ArrayGenerator {

    public static int[] generate(int count, int min, int max) {
        int[] array = new int[count];
        Random random = new Random();
        for (int i = 0; i < array.length; i++)
            array[i] = random.nextInt(min, max+1);
        return array;
    }
}
