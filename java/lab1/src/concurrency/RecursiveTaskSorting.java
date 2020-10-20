package concurrency;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.locks.ReentrantLock;

public class RecursiveTaskSorting extends RecursiveAction {

    private CommonResource resource;

    private ReentrantLock locker;

    private int indexOfRow;


    public RecursiveTaskSorting(CommonResource resource, ReentrantLock locker, int indexOfRow) {
        this.resource = resource;
        this.locker = locker;
        this.indexOfRow = indexOfRow;
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
    protected void compute() {
        locker.lock();
        try {
            //
            // Читаем из разделяемого ресурса.
            // Строка матрицы.
            //
            int[] matrixRowToSort = resource.matrix[indexOfRow];
            int[] sorted = mergeSort(matrixRowToSort);
            //
            // Изменяем разделяемый ресурс.
            //
            resource.matrix[indexOfRow] = sorted;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            locker.unlock();
        }
    }
}
