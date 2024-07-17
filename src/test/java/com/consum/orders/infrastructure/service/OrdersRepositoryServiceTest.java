package com.consum.orders.infrastructure.service;

import com.consum.orders.domain.exception.ProcessingException;
import com.consum.orders.infrastructure.database.entity.Orders;
import com.consum.orders.infrastructure.database.repository.OrdersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OrdersRepositoryServiceTest {

    @Mock
    private OrdersRepository ordersRepository;

    @InjectMocks
    private OrdersRepositoryService ordersRepositoryService;

    private AutoCloseable closeable;

    private List<Orders> ordersList;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        ordersList = List.of(createOrders());
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testSaveAllOrders_Success() {

        when(ordersRepository.saveAll(ordersList)).thenReturn(ordersList);

        ordersRepositoryService.saveAllOrders(ordersList);

        verify(ordersRepository, times(1)).saveAll(ordersList);
    }

    @Test
    void testSaveAllOrders_Exception() {

        doThrow(RuntimeException.class).when(ordersRepository).saveAll(ordersList);

        assertThrows(ProcessingException.class, () -> ordersRepositoryService.saveAllOrders(ordersList));
    }

    @Test
    void testFindAll() {

        when(ordersRepository.findAll()).thenReturn(ordersList);

        List<Orders> actualOrders = ordersRepositoryService.findAll();

        assertEquals(ordersList, actualOrders);
        verify(ordersRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Long orderId = 1L;
        Orders expectedOrder = createOrders();

        when(ordersRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));

        Optional<Orders> actualOrderOptional = ordersRepositoryService.findById(orderId);

        assertEquals(expectedOrder, actualOrderOptional.orElse(null));
        verify(ordersRepository, times(1)).findById(orderId);
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
}