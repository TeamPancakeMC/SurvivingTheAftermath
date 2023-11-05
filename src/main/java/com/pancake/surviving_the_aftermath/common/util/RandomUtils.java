package com.pancake.surviving_the_aftermath.common.util;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomUtils<T> {
    private static final Random RANDOM = new Random();
    public static <T> T getRandomElement(Collection<T> collection) {
        return (T) getRandomElement(collection.toArray());
    }

    public static <T> T getRandomElement(T[] array) {
        int randomIndex = RANDOM.nextInt(array.length);
        return array[randomIndex];
    }

    public static boolean randomChanceOf(double percent) {
        return RANDOM.nextDouble() <= percent;
    }


}
