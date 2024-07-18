package com.consum.orders.domain.dto;

import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpHeaders;

@Value
@Builder
public class OrdersFileDTO {

    byte[] fileBytes;
    HttpHeaders headers;
}
