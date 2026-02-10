package com.example.demo.service;

import com.example.demo.dto.BfhlRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BfhlService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${chitkara.email}")
    private String email;

    private final RestClient restClient;

    public BfhlService() {
        this.restClient = RestClient.create();
    }

    public Object processRequest(BfhlRequest request) {
        if (request.getFibonacci() != null) {
            return calculateFibonacci(request.getFibonacci());
        }
        if (request.getPrime() != null) {
            return filterPrimes(request.getPrime());
        }
        if (request.getLcm() != null) {
            return calculateLCM(request.getLcm());
        }
        if (request.getHcf() != null) {
            return calculateHCF(request.getHcf());
        }
        if (request.getAI() != null) {
            return getAIResponse(request.getAI());
        }
        throw new IllegalArgumentException("No valid key in request!");
    }

    private List<Integer> calculateFibonacci(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n can't be negative");
        }
        List<Integer> series = new ArrayList<>();
        int a = 0, b = 1;
        for (int i = 0; i < n; i++) {
            series.add(a);
            int temp = a + b;
            a = b;
            b = temp;
        }
        return series;
    }

    private List<Integer> filterPrimes(List<Integer> numbers) {
        List<Integer> primes = new ArrayList<>();
        for (int num : numbers) {
            if (isPrime(num)) {
                primes.add(num);
            }
        }
        return primes;
    }

    private boolean isPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    private int calculateLCM(List<Integer> numbers) {
        if (numbers.isEmpty()) return 0;
        int result = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            result = lcm(result, numbers.get(i));
        }
        return result;
    }

    private int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }

    private int calculateHCF(List<Integer> numbers) {
        if (numbers.isEmpty()) return 0;
        int result = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            result = gcd(result, numbers.get(i));
        }
        return result;
    }

    private int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    private String getAIResponse(String question) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + geminiApiKey;

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", "Answer with EXACTLY ONE word: " + question)
                        ))
                )
        );

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restClient.post()
                    .uri(url)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            List<?> candidates = (List<?>) response.get("candidates");
            Map<?, ?> candidate = (Map<?, ?>) candidates.get(0);
            Map<?, ?> content = (Map<?, ?>) candidate.get("content");
            List<?> parts = (List<?>) content.get("parts");
            Map<?, ?> part = (Map<?, ?>) parts.get(0);
            return ((String) part.get("text")).trim();
        } catch (Exception e) {
            return "AI error";
        }
    }
}
