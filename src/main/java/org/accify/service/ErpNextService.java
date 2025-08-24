package org.accify.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.accify.dto.Consignment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ErpNextService {

    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${erpnext.base-url}")
    private String baseUrl;

    @Value("${erpnext.api-key}")
    private String apiKey;

    @Value("${erpnext.api-secret}")
    private String apiSecret;

    private WebClient client() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "token " + apiKey + ":" + apiSecret)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public Mono<String> createJournalEntry(Consignment dto) {
        String payload = buildJournalEntryJson(dto);

        return client()
                .post()
                .uri("/api/resource/Journal Entry")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class);
    }

    private String buildJournalEntryJson(Consignment d) {
        String company = "SHIVAM LOGISTICS";
        String debitAccount = "Debtors - SL";
        String creditAccount = "Service - SL";
        String costCenter = "Main - SL";

        return """
        {
          "posting_date": "%s",
          "company": "%s",
          "accounts": [
            {
              "account": "%s",
              "debit_in_account_currency": %.2f,
              "party_type": "Customer",
              "party": "%s",
              "cost_center": "%s"
            },
            {
              "account": "%s",
              "credit_in_account_currency": %.2f,
              "cost_center": "%s"
            }
          ],
          "user_remark": "Consignment %s %s ➜ %s",
          "docstatus": 1
        }
        """.formatted(
                d.getDate(),
                company,
                debitAccount,
                d.getGoodsValue(),
                d.getConsignor().getName(),
                costCenter,
                creditAccount,
                d.getGoodsValue(),
                costCenter,
                d.getConsignmentNo(),
                d.getFromLocation(),
                d.getToLocation()
        );
    }
}
