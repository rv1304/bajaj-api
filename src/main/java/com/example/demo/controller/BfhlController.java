package com.example.demo.controller;

import com.example.demo.dto.BfhlRequest;
import com.example.demo.dto.BfhlResponse;
import com.example.demo.service.BfhlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class BfhlController {
    @Autowired
    BfhlService service;

    @Value("${chitkara.email}")
    private String email;

    @GetMapping("/health")
    public ResponseEntity<BfhlResponse<Void>> health() {
        return ResponseEntity.ok(BfhlResponse.<Void>builder()
                .isSuccess(true)
                .officialEmail(email)
                .build());
    }

    @PostMapping("/bfhl")
    public ResponseEntity<BfhlResponse<Object>> bfhl(@RequestBody BfhlRequest request) {
        int count = 0;
        if (request.getFibonacci() != null) count++;
        if (request.getPrime() != null) count++;
        if (request.getLcm() != null) count++;
        if (request.getHcf() != null) count++;
        if (request.getAI() != null) count++;

        if (count != 1) {
            throw new IllegalArgumentException("Exactly one key needed");
        }

        Object data = service.processRequest(request);

        return ResponseEntity.ok(BfhlResponse.builder()
                .isSuccess(true)
                .officialEmail(email)
                .data(data)
                .build());
    }
}
