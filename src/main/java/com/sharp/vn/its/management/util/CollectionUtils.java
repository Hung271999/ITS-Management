package com.sharp.vn.its.management.util;

import java.util.Collection;

public final class CollectionUtils {

    private CollectionUtils() {
        // private constructor to prevent instantiation
    }

    /**
     * Adds an item to the collection if it is not null or empty.
     *
     * @param collection the collection to add the item to
     * @param item the item to add to the collection
     * @param <T> the type of item
     */
    public static <T> void addIfNotEmptyOrNull(Collection<T> collection, T item) {
        if (item != null) {
            if (item instanceof String && !((String) item).isEmpty()) {
                collection.add(item);
            } else if (!(item instanceof String) && !isEmpty(item)) {
                collection.add(item);
            }
        }
    }

    /**
     * Checks if an object is empty.
     *
     * @param obj the object to check
     * @return true if the object is empty, false otherwise
     */
    private static boolean isEmpty(Object obj) {
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        } else if (obj instanceof Object[]) {
            return ((Object[]) obj).length == 0;
        } else {
            // Add more cases as needed for different types of objects
            return false; // default case
        }
    }
}
