package com.example.master.controllers;

import com.example.master.models.SortingViewModel;
import com.example.master.services.MainService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
public class MainController {

    private MainService mainService = new MainService();

    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    public String pingPong() {
        return "pong";
    }

    @PostMapping(value = "/sorting", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SortingViewModel sorting(@RequestBody SortingViewModel viewModel) {
         SortingViewModel response = mainService.Sorting(viewModel);
        return response;
    }
}
