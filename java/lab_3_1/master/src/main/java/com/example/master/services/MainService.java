package com.example.master.services;

import com.example.master.models.SortingViewModel;
import java.util.*;
import java.util.concurrent.*;


public class MainService {

    private String[] services = new String[]{
            "http://localhost:6491",
            "http://localhost:6492",
            "http://localhost:6493"
    };

    public SortingViewModel Sorting(SortingViewModel viewModel) {
        System.out.println("Исходная матрица: ");
        System.out.println(Arrays.deepToString(viewModel.getRows()));
        List<Future<SortingViewModel>> futures = new ArrayList<>();
        long start = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(viewModel.getRows().length);
        for (int i = 0; i < viewModel.getRows().length; i++) {
            int service = i % services.length;
            System.out.println("Send to service[" + service + "], url: " + services[service]);
            Callable<SortingViewModel> callable = new CallbleServiceRequest(services[service], i, viewModel);
            Future<SortingViewModel> future = executor.submit(callable);
            futures.add(future);
        }
        for (Future<SortingViewModel> future : futures) {
            try {
                SortingViewModel result = future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("task interrupted");
            }
        }
        long finish = System.currentTimeMillis();
        float timeElapsed = (finish - start) / 100F;
        executor.shutdown();
        StringBuilder builder = new StringBuilder();
        builder.append("Result in time elapsed : ");
        builder.append(timeElapsed);
        System.out.println(Arrays.deepToString(viewModel.getRows()));
        System.out.println(builder.toString());
        return viewModel;
    }
}
