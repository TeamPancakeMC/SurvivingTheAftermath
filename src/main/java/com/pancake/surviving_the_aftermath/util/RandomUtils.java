package com.pancake.surviving_the_aftermath.util;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RandomUtils<T> {
    private static final Random RANDOM = new Random();
    public static <T> T getRandomElement(Collection<T> collection) {
        int size = collection.size();
        if (size == 0) {
            throw new IllegalArgumentException("Collection cannot be empty.");
        }
        int randomIndex = RANDOM.nextInt(size);
        if (collection instanceof List) {
            return ((List<T>) collection).get(randomIndex);
        } else {
            int i = 0;
            for (T item : collection) {
                if (i == randomIndex) {
                    return item;
                }
                i++;
            }
            throw new IllegalStateException("Unexpected condition: randomIndex matched no element.");
        }
    }

    public static boolean randomChanceOf(double percent) {
        return RANDOM.nextDouble() <= percent;
    }


}