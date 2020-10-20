import concurrency.RunnableSorting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class main {

    private static int[][] array = new int[][]{
            {28, 4, 98, 3, 95},
            {85, 10, 85, 11, 44},
            {100, 35, 51, 26, 30},
            {29, 50, 87, 55, 92},
            {42, 100, 78, 67, 9},
    };

    private static String toStringArray(int[][] array) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int[] row : array) {
            for (int element : row) {
                stringBuilder.append(element);
                stringBuilder.append(", ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private static int[][] getCopy() {
        final int[][] copiedArray = new int[array.length][];
        for (int i = 0; i < array.length; i++) {
            final int[] row = array[i];
            copiedArray[i] = new int[row.length];
            System.arraycopy(row, 0, copiedArray[i], 0, row.length);
        }
        return copiedArray;
    }

    public static void main(String args[]) {
        //
        System.out.println("Сортируем в одном потоке...");
        int[][] copy = getCopy();
        singlethread.MergeSort singlethreadSorted = new singlethread.MergeSort();
        String result = singlethreadSorted.SimpleSort(copy);
        System.out.println(result + "\n");
        //
        //
        //
        concurrency.MergeSort concurrency = new concurrency.MergeSort();
        //
        //
        //
        System.out.println("Сортируем во множестве потоков с помощью ExecutorService...");
        copy = getCopy();
        result = concurrency.SortedWithExecutorService(copy);
        System.out.println(result + "\n");
        //
        //
        //
        System.out.println("Сортируем во множестве потоков с помощью ForkJoinPool...");
        copy = getCopy();
        result = concurrency.SortedForkJoinPool(copy);
        System.out.println(result + "\n");
        //
        //
        //
        System.out.println("Сортируем во множестве потоков с помощью Thread...");
        copy = getCopy();
        concurrency.SortedWithThreadAndRunnable(copy);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Thread has been interrupted");
        }
        System.out.println(toStringArray(copy));
        //
        //
        //
        System.out.println("Сортируем во множестве потоков с помощью Java Concurrent Collections....");
        ExampleForSyncCollections();
    }

    private static void ExampleForSyncCollections() {
        List<Integer> list = Collections.synchronizedList(Arrays.asList(28, 4, 98, 3, 95));
        for (int step = 0; step < 10; step++) {
            Runnable bubbleSorting = () -> {
                synchronized (list) {
                    for (int x = 0; x < list.size(); x++) {
                        for (int i = 0; i < list.size() - x - 1; i++) {
                            if (list.get(i).compareTo(list.get(i + 1)) > 0) {
                                Integer temp = list.get(i);
                                list.set(i, list.get(i + 1));
                                list.set(i + 1, temp);
                            }
                        }
                    }
                }
            };

            Thread thread = new Thread(bubbleSorting);
            thread.setName("Thread " + step);
            thread.start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Thread has been interrupted");
        }
        System.out.println(toStringArray(new int[][]{
                list.stream().mapToInt(i -> i).toArray()
        }));
    }
}
