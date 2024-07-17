package com.consum.orders.domain.utils;

import com.consum.orders.application.model.OrdersFileResponse;
import com.consum.orders.domain.dto.OrdersDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class OrdersFileMethodsTest {

    @InjectMocks
    private OrdersFileMethods ordersFileMethods;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testGenerateOrderFileCSV() throws IOException {

        List<OrdersDTO> ordersDtoList = Collections.singletonList(createOrderDTO());
        String fileName = "orders_test";

        OrdersFileResponse response = ordersFileMethods.generateOrderFileCSV(ordersDtoList, fileName);

        assertNotNull(response);
        assertNotNull(response.getFileBytes());
        assertTrue(response.getFileBytes().length > 0);
        assertNotNull(response.getHeaders());

        String actualHeaderValue = Objects.requireNonNull(response.getHeaders()
                .getFirst(HttpHeaders.CONTENT_DISPOSITION)).replace("\"", "");
        String expectedHeaderValue = "form-data; name=attachment; filename=" + Objects.requireNonNull(response.getHeaders()
                .getContentDisposition().getFilename()).replace("\"", "");

        assertEquals(expectedHeaderValue, actualHeaderValue);
    }

    /**
     * Builder OrderDTO
     *
     * @return OrderDTO
     */
    private static OrdersDTO createOrderDTO() {
        return OrdersDTO.builder()
                .orderId("1001")
                .orderPriority("H")
                .orderDate(new Date())
                .region("North America")
                .country("United States")
                .itemType("Electronics")
                .salesChannel("Online")
                .shipDate(new Date())
                .unitsSold(100)
                .unitPrice(BigDecimal.valueOf(50.00))
                .unitCost(BigDecimal.valueOf(30.00))
                .totalRevenue(BigDecimal.valueOf(5000.00))
                .totalCost(BigDecimal.valueOf(3000.00))
                .totalProfit(BigDecimal.valueOf(2000.00))
                .build();
    }

}