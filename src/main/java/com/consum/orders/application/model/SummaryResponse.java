package com.consum.orders.application.model;

import com.consum.orders.domain.dto.SummaryDTO;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SummaryResponse {

    SummaryDTO summary;
}