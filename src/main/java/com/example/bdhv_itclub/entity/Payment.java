package com.example.bdhv_itclub.entity;


import com.example.bdhv_itclub.dto.reponse.BankResponse;
import com.example.bdhv_itclub.dto.reponse.BankTransactionInfo;
import com.example.bdhv_itclub.dto.request.BankRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

@Component
@Getter
public class Payment {
    private String ACCOUNT_NAME = "ĐỖ KIM LÂM"; // Không để được vào env do khi lấy ra nó không hỗ trợ utf-8
    @Value("${payment.bank_branch}")
    private String BANK_BRANCH;
    @Value("${payment.bank_number}")
    private String BANK_NUMBER;
    @Value("${payment.bank_username}")
    private String USERNAME;
    @Value("${payment.bank_password}")
    private String PASSWORD;
    private String tokenBank = null;
    private LocalDateTime lastLoginTime = null;

    // Lấy token của tpbank
    private String parseAccessToken(String jsonResponse) {
        int startIndex = jsonResponse.indexOf("\"access_token\":") + 16;
        int endIndex = jsonResponse.indexOf("\"", startIndex);

        if (endIndex != -1) {
            return jsonResponse.substring(startIndex, endIndex);
        }

        return null;
    }

    private HttpHeaders initHttpRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json, text/plain, */*");
        headers.set("Accept-Encoding", "gzip, deflate, br, zstd");
        headers.set("Accept-Language", "en-US,en;q=0.9,vi;q=0.8");
        headers.set("Device_id", "PDwAAkeiJplpQ2g2BEAvo8pQSdLAsry1xD9wS5w1YMGiS");
        headers.set("Referer", "https://ebank.tpb.vn");
        headers.set("Sec-Ch-Ua", "\"Microsoft Edge\";v=\"123\", \"Not:A-Brand\";v=\"8\", \"Chromium\";v=\"123\"");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36 Edg/123.0.0.0");
        return headers;
    }

    public String login() {
        String url = "https://ebank.tpb.vn/gateway/api/auth/login";

        String un = new String(Base64.getDecoder().decode(USERNAME));
        String pw = new String(Base64.getDecoder().decode(PASSWORD));

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("deviceId", "PDwAAkeiJplpQ2g2BEAvo8pQSdLAsry1xD9wS5w1YMGiS");
        requestBody.add("password", pw);
        requestBody.add("step_2FA", "VERIFY");
        requestBody.add("username", un);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, initHttpRequest());

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String jsonResponse = responseEntity.getBody();

            assert jsonResponse != null;
            return parseAccessToken(jsonResponse);
        } else {
            System.err.println("Failed to login. Status code: " + responseEntity.getStatusCode());
            return null;
        }
    }

    public BankTransactionInfo[] getTransactions() {

//        // Vì sao phải check? vì khi login nhiều quá sẽ bị block ip nên phải kiểm tra khi nào token hết hạn thì gọi token mới
        if (tokenBank == null || lastLoginTime == null || lastLoginTime.plusMinutes(15).isBefore(LocalDateTime.now())) {
            tokenBank = login(); // Gọi hàm login() để lấy token mới
            lastLoginTime = LocalDateTime.now();
        }
        String token = tokenBank;
        String url = "https://ebank.tpb.vn/gateway/api/smart-search-presentation-service/v2/account-transactions/find";

        LocalDate date = LocalDate.now();
        LocalDate twoDaysBefore = date.minusDays(2);
        // Lấy ra ngày giờ hiện tại
        String toDateNow = String.format("%d%02d%02d", date.getYear(), date.getMonthValue(), date.getDayOfMonth());
        String to2DaysBefore = String.format("%d%02d%02d", twoDaysBefore.getYear(), twoDaysBefore.getMonthValue(), twoDaysBefore.getDayOfMonth());

        HttpHeaders headers = initHttpRequest();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        BankRequest transactionRequest = new BankRequest();
        transactionRequest.setAccountNo(BANK_NUMBER);
        transactionRequest.setCurrency("VND");
        transactionRequest.setFromDate(to2DaysBefore);
        transactionRequest.setKeyword("");
        transactionRequest.setMaxAcentrysrno("");
        transactionRequest.setPageNumber(1);
        transactionRequest.setPageSize(5);
        transactionRequest.setToDate(toDateNow);

        // Chuyển đổi đối tượng Java thành JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(transactionRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<?> responseEntity = restTemplate.postForEntity(url, requestEntity, BankResponse.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            BankResponse transactionResponse = (BankResponse) responseEntity.getBody();
            assert transactionResponse != null;
            return transactionResponse.getTransactionInfos();
        } else {
            System.err.println("Failed to login. Status code: " + responseEntity.getStatusCode());
        }
        return null;
    }
}