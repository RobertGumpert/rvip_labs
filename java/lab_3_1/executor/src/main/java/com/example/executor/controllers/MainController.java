package com.example.executor.controllers;

import com.example.executor.models.SortingViewModel;
import com.example.executor.services.SortingService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.Arrays;

@RestController
public class MainController {

    private SortingService sortingService = new SortingService();

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public String ping() {
        return "pong";
    }


    @PostMapping(value = "/sorting", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SortingViewModel sorting(@RequestBody SortingViewModel viewModel) {
        System.out.println(Arrays.deepToString(viewModel.getRows()));
        sortingService.SimpleSort(viewModel.getRows());
        return viewModel;
    }
}
