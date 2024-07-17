package com.consum.orders.domain.utils;

import com.consum.orders.domain.exception.ProcessingException;
import com.consum.orders.domain.dto.OrdersDTO;
import com.consum.orders.domain.dto.SummaryDTO;
import com.consum.orders.domain.mapper.OrdersMapper;
import com.consum.orders.infrastructure.client.dto.ContentClientDTO;
import com.consum.orders.infrastructure.client.dto.ContentLinksDTO;
import com.consum.orders.infrastructure.client.dto.LinksClientDTO;
import com.consum.orders.infrastructure.client.dto.PaginatedOrderClientDTO;
import com.consum.orders.infrastructure.database.entity.Orders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class OrdersMethodsTest {

    @InjectMocks
    private OrdersMethods ordersMethods;

    @Mock
    private OrdersMapper ordersMapper;

    @Mock
    private OrdersValidations ordersValidations;

    private AutoCloseable closeable;

    private Orders orders;
    private OrdersDTO ordersDTO;
    private PaginatedOrderClientDTO paginatedOrderClientDTO;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        orders = createOrders();
        ordersDTO = createOrderDTO();
        paginatedOrderClientDTO = createPaginatedOrderClientDTO();
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void convertToOrders_whenPaginatedOrderClientDTOIsValid_shouldReturnListOfOrders() throws ParseException {
        when(ordersMapper.contentClientDTOToOrders(any(ContentClientDTO.class))).thenReturn(orders);
        doNothing().when(ordersValidations).validateContentClientDTO(any(ContentClientDTO.class));

        List<Orders> result = ordersMethods.convertToOrders(paginatedOrderClientDTO);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(paginatedOrderClientDTO.getContent().get(0).getId(), result.get(0).getOrderId());
    }

    @Test
    void convertToOrdersDTO_whenOrdersListIsValid_shouldReturnListOfOrderDTO() {
        when(ordersMapper.ordersToOrderDTO(orders)).thenReturn(ordersDTO);

        List<OrdersDTO> result = ordersMethods.convertToOrdersDTO(List.of(orders));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ordersDTO.getOrderId(), result.get(0).getOrderId());
    }

    @Test
    void generateOrderSummary_whenOrderDTOListIsValid_shouldReturnSummaryDTO() {

        SummaryDTO result = ordersMethods.generateOrderSummary(List.of(ordersDTO));

        assertNotNull(result);
        assertEquals(1, result.getRegionSummary().get("North America"));
        assertEquals(1, result.getCountrySummary().get("United States"));
        assertEquals(1, result.getItemTypeSummary().get("Electronics"));
        assertEquals(1, result.getSalesChannelSummary().get("Online"));
        assertEquals(1, result.getOrderPrioritySummary().get("H"));
    }

    @Test
    void generateOrderSummary_whenOrderDTOListIsNull_shouldThrowProcessingException() {
        ProcessingException exception = assertThrows(ProcessingException.class,
                () -> ordersMethods.generateOrderSummary(null));

        assertEquals("La lista de pedidos no puede ser vacía.", exception.getMessage());
    }

    @Test
    void generateOrderSummary_whenOrderDTOListIsEmpty_shouldThrowProcessingException() {
        ProcessingException exception = assertThrows(ProcessingException.class,
                () -> ordersMethods.generateOrderSummary(new ArrayList<>()));

        assertEquals("La lista de pedidos no puede ser vacía.", exception.getMessage());
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

    /**
     * Constructor ContentClientDTO
     *
     * @return ContentClientDTO
     */
    private static ContentClientDTO createContentClientDTO() {

        ContentLinksDTO contentLinksDTO = new ContentLinksDTO("https://kata-espublicotech.g3stiona.com:443/v1/orders/1858f59d-8884-41d7-b4fc-88cfbbf00c53");

        return new ContentClientDTO("ce288666-5618-4460-9e9a-0e62944850e2",
                "123456",
                "North America",
                "United States",
                "Electronics",
                "Online",
                "H",
                "07/07/2024",
                "07/07/2024",
                50,
                75.00,
                20.00,
                3750.00,
                1000.00,
                2750.00,
                contentLinksDTO
        );
    }

    /**
     * Builder PaginatedOrderClientDTO
     *
     * @return PaginatedOrderClientDTO
     */
    private static PaginatedOrderClientDTO createPaginatedOrderClientDTO() {
        String next = "https://kata-espublicotech.g3stiona.com:443/v1/orders?page=2&max-per-page=100";
        String self = "https://kata-espublicotech.g3stiona.com:443/v1/orders?page=1&max-per-page=100";

        LinksClientDTO linksClientDTO = new LinksClientDTO(next, self);

        return new PaginatedOrderClientDTO(1,
                List.of(createContentClientDTO()),
                linksClientDTO);
    }
}