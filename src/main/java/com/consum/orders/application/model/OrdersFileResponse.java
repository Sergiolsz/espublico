package com.consum.orders.application.model;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpHeaders;

@Value
@Builder
public class OrdersFileResponse {

    byte[] fileBytes;
    HttpHeaders headers;
}
