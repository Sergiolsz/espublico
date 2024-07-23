package com.consum.orders.application.model;

import com.consum.orders.domain.dto.SummaryDTO;
import lombok.Builder;
import lombok.Value;

import java.io.Serial;
import java.io.Serializable;

@Value
@Builder
public class SummaryResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    SummaryDTO summary;
}