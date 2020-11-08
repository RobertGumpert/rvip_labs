package com.example.executor.services;


public class SortingService {
    private int[][] array;


    public String SimpleSort(int[][] matrix) {
        this.array = matrix;
        //
        long start = System.currentTimeMillis();
        for (int index = 0; index < array.length; index++) {
            array[index] = mergeSort(array[index]);
        }
        long finish = System.currentTimeMillis();
        float timeElapsed = (finish - start) / 100F;
        StringBuilder builder = new StringBuilder();
        builder.append(toString());
        builder.append("Result in time elapsed : ");
        builder.append(timeElapsed);
        return builder.toString();
    }

    private int[] mergeSort(int[] input) {
        if (input.length <= 1) {
            return input;
        }
        //
        int middle = input.length / 2;
        int[] left = new int[middle];
        int[] right = new int[input.length - middle];
        //
        for (int i = 0; i < middle; i++) {
            left[i] = input[i];
        }
        for (int i = middle; i < input.length; i++) {
            right[i - middle] = input[i];
        }
        //
        return merge(mergeSort(right), mergeSort(left));
    }

    private int[] merge(int[] right, int[] left) {
        //
        int size = right.length + left.length;
        int i = right.length - 1;
        int j = left.length - 1;
        int[] sorted = new int[size];
        //
        for (int k = size - 1; k >= 0; k--) {
            if (i == -1) {
                sorted[k] = left[j];
                j--;
                continue;
            }
            if (j == -1) {
                sorted[k] = right[i];
                i--;
                continue;
            }
            if (right[i] > left[j]) {
                sorted[k] = left[j];
                j--;
                continue;
            }
            if (right[i] < left[j]) {
                sorted[k] = right[i];
                i--;
                continue;
            }
            if (right[i] == left[j]) {
                sorted[k] = left[j];
                j--;
            }
        }
        return sorted;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int[] row : this.array) {
            for (int element : row) {
                stringBuilder.append(element);
                stringBuilder.append(", ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
