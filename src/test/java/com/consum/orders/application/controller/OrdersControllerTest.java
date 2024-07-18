package com.consum.orders.application.controller;

import com.consum.orders.application.model.OrdersListResponse;
import com.consum.orders.application.model.OrdersSingleResponse;
import com.consum.orders.application.model.SummaryResponse;
import com.consum.orders.domain.dto.OrdersDTO;
import com.consum.orders.domain.dto.SummaryDTO;
import com.consum.orders.domain.service.api.OrdersService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrdersControllerTest {

    private static final String PAGE = "1";
    private static final String MAX_PER_PAGE = "100";
    private static final String UUID = "1858f59d-8884-41d7-b4fc-88cfbbf00c53";

    @Mock
    private OrdersService ordersService;

    @InjectMocks
    private OrdersController ordersController;

    private AutoCloseable closeable;

    private OrdersListResponse ordersListResponse;
    private OrdersSingleResponse ordersSingleResponse;
    private SummaryResponse summaryResponse;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        summaryResponse = SummaryResponse.builder().summary(createSummaryDTO()).build();
        ordersSingleResponse = OrdersSingleResponse.builder().order(createOrderDTO()).build();
        ordersListResponse = OrdersListResponse.builder().orders(List.of(createOrderDTO())).build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testImportAndSummarizeOrders() {

        when(ordersService.importAndSummarizeOrders(PAGE, MAX_PER_PAGE)).thenReturn(summaryResponse);

        SummaryResponse result = ordersController.importAndSummarizeOrders(PAGE, MAX_PER_PAGE);

        assertEquals(summaryResponse, result);
        verify(ordersService, times(1)).importAndSummarizeOrders(PAGE, MAX_PER_PAGE);
    }

    @Test
    void testGetOrder() {

        when(ordersService.getOrderByUUID(UUID)).thenReturn(ordersSingleResponse);

        OrdersSingleResponse result = ordersController.getOrderByUUID(UUID);

        assertEquals(ordersSingleResponse, result);
        assertEquals(ordersSingleResponse.getOrder().getOrderId(), result.getOrder().getOrderId());
        verify(ordersService, times(1)).getOrderByUUID(UUID);
    }

    @Test
    void testGetOrders() {

        when(ordersService.getOrders()).thenReturn(ordersListResponse);

        OrdersListResponse result = ordersController.getOrders();

        assertEquals(ordersListResponse, result);
        verify(ordersService, times(1)).getOrders();
    }

    /**
     * Builder SummaryDTO
     *
     * @return SummaryDTO
     */
    private static SummaryDTO createSummaryDTO() {
        Map<String, Long> regionSummary = new HashMap<>();
        regionSummary.put("North America", 10L);

        Map<String, Long> countrySummary = new HashMap<>();
        countrySummary.put("United States", 7L);

        Map<String, Long> itemTypeSummary = new HashMap<>();
        itemTypeSummary.put("Electronics", 8L);

        Map<String, Long> salesChannelSummary = new HashMap<>();
        salesChannelSummary.put("Online", 9L);

        Map<String, Long> orderPrioritySummary = new HashMap<>();
        orderPrioritySummary.put("H", 6L);

        return SummaryDTO.builder()
                .regionSummary(regionSummary)
                .countrySummary(countrySummary)
                .itemTypeSummary(itemTypeSummary)
                .salesChannelSummary(salesChannelSummary)
                .orderPrioritySummary(orderPrioritySummary)
                .build();
    }

    /**
     * Builder OrderDTO
     *
     * @return OrderDTO
     */
    private static OrdersDTO createOrderDTO() {
        return OrdersDTO.builder()
                .orderId("123456")
                .orderPriority("H")
                .orderDate(new Date())
                .region("North America")
                .country("United States")
                .itemType("Electronics")
                .salesChannel("Online")
                .shipDate(new Date())
                .unitsSold(50)
                .unitPrice(BigDecimal.valueOf(75.00))
                .unitCost(BigDecimal.valueOf(20.00))
                .totalRevenue(BigDecimal.valueOf(3750.00))
                .totalCost(BigDecimal.valueOf(1000.00))
                .totalProfit(BigDecimal.valueOf(2750.00))
                .build();
    }

}