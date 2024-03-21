package ru.tgc.collections;

import java.util.*;

public class MyArrayList<E> implements List<E> {

    private static final int INITIAL_CAPACITY = 10;
    private int size;

    @SuppressWarnings("unchecked")
    private final E[] EMPTY_ARRAY = (E[]) new Object[INITIAL_CAPACITY];

    private E[] array;

    public MyArrayList() {
        this(INITIAL_CAPACITY);
    }

    public MyArrayList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity should not be negative!");
        }

        array = EMPTY_ARRAY;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public Iterator<E> iterator() {
        return new MyItr();
    }

    private class MyItr implements Iterator<E> {
        private int cursor;

        MyItr() {
        }

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There is no more elements");
            }
            E item = array[cursor];
            cursor++;
            return item;
        }

        @Override
        public void remove() {
            MyArrayList.this.remove(cursor);
            cursor--;
        }
    }

    @Override
    public Object[] toArray() {
        trimSize();
        return Arrays.copyOf(array, size);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) new Object[size];
    }

    @Override
    public boolean add(E e) {
        add(size, e);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(o, array[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        addAll(size, c);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        int numNew = c.size();
        ensureCapacity(size + numNew);

        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(array, index, array, index + numNew, numMoved);
        }

        int i = index;
        for (E e : c) {
            array[i++] = e;
        }
        size += numNew;
        return numNew != 0;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        array = EMPTY_ARRAY;
        size = 0;
    }

    @Override
    public E get(int index) {
        checkIndex(index);
        return array[index];
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        array[index] = element;
        return element;
    }

    @Override
    public void add(int index, E element) {
        checkIndex(index);
        ensureCapacity(size + 1);
        System.arraycopy(array, index, array, index + 1, size - index);
        array[index] = element;
        size++;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        E oldValue = array[index];

        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(array, index + 1, array, index, numMoved);
        }
        array[--size] = null;

        return oldValue;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(array[i], o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i > 0; i--) {
            if (Objects.equals(array[i], o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return Collections.emptyList();
    }

    @Override
    public void sort(Comparator<? super E> comparator) {
        this.trimSize();
        if (array.length < 2) {
            return;
        }
        quicksort(array, 0, array.length - 1, comparator);
    }

    @SuppressWarnings("unchecked")
    public void sort() {
        this.trimSize();
        if (array.length < 2) {
            return;
        }
        if (!isComparable()) {
            throw new IllegalArgumentException();
        }
        quicksort(
                array, 0, array.length - 1, (Comparator<E>) Comparator.naturalOrder()
        );
    }

    private void trimSize() {
        if (size < array.length) {
            array = Arrays.copyOf(array, size);
        }
    }

    private void quicksort(E[] arr, int low, int high, Comparator<? super E> comparator) {
        if (low < high) {
            int pivot = partition(arr, low, high, comparator);

            quicksort(arr, low, pivot - 1, comparator);
            quicksort(arr, pivot + 1, high, comparator);
        }
    }

    private int partition(E[] arr, int low, int high, Comparator<? super E> comparator) {
        E pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (comparator.compare(arr[j], pivot) < 0) {
                i++;

                E temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        E temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity - array.length > 0) {
            growArray(minCapacity);
        }
    }

    private void growArray(int minCapacity) {
        int oldCapacity = array.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        if (newCapacity - Integer.MAX_VALUE > 0) {
            if (minCapacity < 0) {
                throw new OutOfMemoryError();
            }
            newCapacity = Integer.MAX_VALUE;
        }
        array = Arrays.copyOf(array, newCapacity);
    }


    private boolean isComparable() {
        return array[0] instanceof Comparable<?>;
    }

    private void checkIndex(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            if (array[i] == null) {
                sb.append("null, ");
            } else {
                sb.append(array[i].toString());
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
