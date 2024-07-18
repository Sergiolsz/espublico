package com.consum.orders.domain.service.api;

import com.consum.orders.application.model.OrdersListResponse;
import com.consum.orders.application.model.OrdersSingleResponse;
import com.consum.orders.application.model.SummaryResponse;

public interface OrdersService {

    SummaryResponse importAndSummarizeOrders(String page, String maxPerPage);

    OrdersSingleResponse getOrderByUUID(String uuid);

    OrdersListResponse getOrders();
}
