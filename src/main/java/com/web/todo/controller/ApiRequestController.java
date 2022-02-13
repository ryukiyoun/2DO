package com.web.todo.controller;

import com.web.todo.service.ApiRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ApiRequestController {
    private final ApiRequestService apiRequestService;

    @GetMapping("/api/national/day")
    public ResponseEntity<Object> getNationalDay(@RequestParam("apiURL") String apiURL, @RequestParam Map<String, String> param){
        return ResponseEntity.ok().body(apiRequestService.nationalDayRequest(apiURL, param));
    }
}
