package concurrency;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

public class MergeSort {

    public String SortedWithExecutorService(int[][] matrix) {
        //
        // Создаём разделяемый ресурс.
        //
        CommonResource commonResource = new CommonResource(matrix);
        int length = matrix.length;
        //
        // Создаём лист объектов Future от которых будем получать
        // данные из запущенных потоков.
        //
        List<Future<String>> futures = new ArrayList<>();
        //
        // Создаём объект ExecutorService, которые будет управлять
        // пуллом запущенных потоков.
        //
        ExecutorService executor = Executors.newFixedThreadPool(length);
        ReentrantLock locker = new ReentrantLock();
        //
        for (int i = 0; i < length; i++) {
            //
            // Интерфейс Callable в отличие от интрефейса Runnable умеет возвращать
            // значение из потока.
            //
            Callable<String> callable = new CallableSorting(commonResource, locker, i);
            //
            // Передадим методу submit интерфейс Callable,
            // после итерация продолжится.
            //
            Future<String> future = executor.submit(callable);
            futures.add(future);
        }
        long start = System.currentTimeMillis();
        for (Future<String> future : futures) {
            try {
                //
                // Метод get блокирует текущий поток
                // до тех пор пока не получит результат
                // от выбранного потока.
                //
                String result = future.get();
                System.out.println(result);
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("task interrupted");
            }
        }
        long finish = System.currentTimeMillis();
        float timeElapsed = (finish - start) / 100F;
        //
        // Метод shutdown() завершит все потоки.
        //
        executor.shutdown();
        //
        //
        //
        StringBuilder builder = new StringBuilder();
        builder.append(commonResource);
        builder.append("Result in time elapsed : ");
        builder.append(timeElapsed);
        return builder.toString();
    }

    public void SortedWithThreadAndRunnable(int[][] matrix) {
        //
        // Создаём разделяемый ресурс.
        //
        CommonResource commonResource = new CommonResource(matrix);
        ReentrantLock locker = new ReentrantLock();
        int length = matrix.length;
        //
        for (int i = 0; i < length; i++) {
            //
            // Создаём новый поток Thread, которому передаём интрефейс
            // Runnable содержащий разделяемый ресурс.
            // Потоки не синхронизованы, поэтому выполняться они будут
            // не по порядку как с помощью ExecutorService, а до тех пор
            // пока они запущены и не будут остановлены планировщиком задач.
            //
            Thread thread = new Thread(new RunnableSorting(commonResource, locker, i));
            thread.setName("Thread " + i);
            thread.start();
        }
    }
}

