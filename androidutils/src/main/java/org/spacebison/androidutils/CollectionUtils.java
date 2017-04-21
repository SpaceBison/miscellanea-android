package org.spacebison.androidutils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;

public class CollectionUtils {
    @Nullable
    public static <E> E getObjectAt(@NonNull Iterable<E> iterable, int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Negative index: " + index);
        }

        try {
            return getNthElement(iterable.iterator(), index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public static <E> E getObjectAt(@NonNull Collection<E> collection, int index) {
        if (index < 0 || index >= collection.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + "; size: " + collection.size());
        }

        return getNthElement(collection.iterator(), index);
    }

    @Nullable
    private static <E> E getNthElement(Iterator<E> iterator, int index) {
        E element = null;
        for (int i = 0; i <= index; i++) {
            if (!iterator.hasNext()) {
                throw new IndexOutOfBoundsException("Index " + index + " of " + i);
            } else {
                element = iterator.next();
            }
        }
        return element;
    }
}
