package com.example.master.services;

import com.example.master.models.SortingViewModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class CallbleServiceRequest implements Callable<SortingViewModel> {

    private String url;
    private int indexRow;
    private SortingViewModel resource;

    public CallbleServiceRequest(String url, int indexRow, SortingViewModel resource) {
        this.url = url + "/sorting";
        this.indexRow = indexRow;
        this.resource = resource;
    }


    @Override
    public SortingViewModel call() throws Exception {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        SortingViewModel requestJsonData = new SortingViewModel();
        requestJsonData.setRows(new int[][]{
                this.resource.getRows()[this.indexRow]
        });
        HttpEntity<SortingViewModel> request = new HttpEntity<>(requestJsonData);
        ResponseEntity<SortingViewModel> response = restTemplate.postForEntity(url, request, SortingViewModel.class);
        int[] updateRow = response.getBody().getRows()[0];
        this.resource.updateRow(this.indexRow, updateRow);

        return this.resource;
    }
}
