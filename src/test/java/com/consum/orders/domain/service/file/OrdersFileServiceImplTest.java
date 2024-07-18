package com.consum.orders.domain.service.file;

import com.consum.orders.application.model.OrdersFileResponse;
import com.consum.orders.domain.dto.OrdersDTO;
import com.consum.orders.domain.exception.FileException;
import com.consum.orders.domain.exception.ProcessingException;
import com.consum.orders.domain.mapper.OrdersMapper;
import com.consum.orders.domain.utils.OrdersFileMethods;
import com.consum.orders.infrastructure.database.entity.Orders;
import com.consum.orders.infrastructure.service.OrdersRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdersFileServiceImplTest {

    private static final String FILENAME_ORDERS_DB = "orders_file";

    @Mock
    private OrdersRepositoryService ordersRepositoryService;

    @Mock
    private OrdersFileMethods ordersFileMethods;

    @Mock
    private OrdersMapper ordersMapper;

    @InjectMocks
    private OrdersFileServiceImpl orderFileService;

    private Orders orders;
    private OrdersDTO ordersDTO;

    private OrdersFileResponse ordersFileResponse;

    @BeforeEach
    public void setUp() {

        orders = createOrders();
        ordersDTO = createOrderDTO();

        ordersFileResponse = createOrderFileResponse();
    }

    @Test
    void testGenerateOrderFileCSV() throws ProcessingException, IOException {

        when(ordersRepositoryService.findAll()).thenReturn(List.of(orders));
        when(ordersMapper.ordersToOrderDTO(orders)).thenReturn(ordersDTO);
        when(ordersFileMethods.generateOrderFileCSV(List.of(ordersDTO), FILENAME_ORDERS_DB)).thenReturn(ordersFileResponse);

        OrdersFileResponse result = orderFileService.generateOrderFileCSV();

        verify(ordersRepositoryService, times(1)).findAll();
        verify(ordersMapper, times(1)).ordersToOrderDTO(orders);
        verify(ordersFileMethods, times(1)).generateOrderFileCSV(List.of(ordersDTO), FILENAME_ORDERS_DB);
        assertEquals(ordersFileResponse, result);
    }

    @Test
    void testGenerateOrderFileCSV_ExceptionHandling() throws IOException {

        when(ordersRepositoryService.findAll()).thenThrow(new RuntimeException("Database connection error"));

        assertThrows(FileException.class, () -> orderFileService.generateOrderFileCSV());

        verify(ordersRepositoryService, times(1)).findAll();
        verify(ordersMapper, never()).ordersToOrderDTO(orders);
        verify(ordersFileMethods, never()).generateOrderFileCSV(List.of(ordersDTO), FILENAME_ORDERS_DB);
    }


    /**
     * Builder Orders
     *
     * @return Orders
     */
    private Orders createOrders() {
        Orders orders = new Orders();
        orders.setOrderId("123456");
        orders.setUuid("ce288666-5618-4460-9e9a-0e62944850e2");
        orders.setOrderPriority("H");
        orders.setRegion("North America");
        orders.setCountry("United States");
        orders.setItemType("Electronics");
        orders.setSalesChannel("Online");
        orders.setOrderDate(new Date());
        orders.setShipDate(new Date());
        orders.setUnitsSold(50);
        orders.setUnitPrice(BigDecimal.valueOf(75.00));
        orders.setUnitCost(BigDecimal.valueOf(20.00));
        orders.setTotalRevenue(BigDecimal.valueOf(3750.00));
        orders.setTotalCost(BigDecimal.valueOf(1000.00));
        orders.setTotalProfit(BigDecimal.valueOf(2750.00));

        return orders;
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

    /**
     * Builder OrderFileResponse
     *
     * @return OrderFileResponse
     */
    private static OrdersFileResponse createOrderFileResponse() {
        return OrdersFileResponse.builder()
                .fileBytes("test csv content".getBytes())
                .headers(new HttpHeaders())
                .build();
    }
}