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
        List<Future<SortingViewModel>> futures = new ArrayList<>();
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
                System.out.println(result);
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("task interrupted");
            }
        }
        executor.shutdown();
        return viewModel;
    }
}
