package com.consum.orders.application.model;

import com.consum.orders.domain.dto.OrdersDTO;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrdersSingleResponse {

    OrdersDTO order;
}
