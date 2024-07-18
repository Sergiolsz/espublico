package com.consum.orders.domain.service.api;

import com.consum.orders.application.model.OrdersListResponse;
import com.consum.orders.application.model.OrdersSingleResponse;
import com.consum.orders.application.model.SummaryResponse;
import com.consum.orders.domain.dto.OrdersDTO;
import com.consum.orders.domain.dto.SummaryDTO;
import com.consum.orders.domain.utils.OrdersMethods;
import com.consum.orders.infrastructure.client.dto.ContentClientDTO;
import com.consum.orders.infrastructure.client.dto.ContentLinksDTO;
import com.consum.orders.infrastructure.client.dto.LinksClientDTO;
import com.consum.orders.infrastructure.client.dto.PaginatedOrderClientDTO;
import com.consum.orders.infrastructure.database.entity.Orders;
import com.consum.orders.infrastructure.service.OrdersClientService;
import com.consum.orders.infrastructure.service.OrdersRepositoryService;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class OrdersServiceImplTest {

    private static final String PAGE = "1";
    private static final String MAX_PER_PAGE = "100";
    private static final String UUID = "1858f59d-8884-41d7-b4fc-88cfbbf00c53";

    @Mock
    private OrdersClientService ordersClientService;

    @Mock
    private OrdersRepositoryService ordersRepositoryService;

    @Mock
    private OrdersMethods ordersMethods;

    @InjectMocks
    private OrdersServiceImpl ordersService;

    private AutoCloseable closeable;

    private OrdersDTO ordersDTO;
    private SummaryDTO summaryDTO;
    private PaginatedOrderClientDTO paginatedOrderClientDTO;
    private ContentClientDTO contentClientDTO;

    private List<Orders> ordersList;
    private List<OrdersDTO> ordersDTOList;

    private SummaryResponse summaryResponse;
    private OrdersSingleResponse ordersSingleResponse;
    private OrdersListResponse ordersListResponse;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        Orders orders = createOrders();
        ordersDTO = createOrderDTO();
        summaryDTO = createSummaryDTO();
        paginatedOrderClientDTO = createPaginatedOrderClientDTO();
        contentClientDTO = createContentClientDTO();

        ordersList = List.of(orders);
        ordersDTOList = List.of(ordersDTO);

        summaryResponse = SummaryResponse.builder().summary(summaryDTO).build();
        ordersSingleResponse = OrdersSingleResponse.builder().order(ordersDTO).build();
        ordersListResponse = OrdersListResponse.builder().orders(ordersDTOList).build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testImportSummarizeOrders() {

        when(ordersClientService.getPagedOrdersClient(PAGE, MAX_PER_PAGE)).thenReturn(paginatedOrderClientDTO);
        when(ordersMethods.convertToOrders(paginatedOrderClientDTO)).thenReturn(ordersList);
        doNothing().when(ordersRepositoryService).saveAllOrders(ordersList);
        when(ordersMethods.convertToOrdersDTO(ordersList)).thenReturn(ordersDTOList);
        when(ordersMethods.generateOrderSummary(ordersDTOList)).thenReturn(summaryDTO);

        SummaryResponse actualResponse = ordersService.importAndSummarizeOrders(PAGE, MAX_PER_PAGE);

        assertNotNull(actualResponse);
        assertEquals(summaryResponse, actualResponse);
        assertEquals(summaryResponse.getSummary().getCountrySummary(), actualResponse.getSummary().getCountrySummary());
        verify(ordersClientService, times(1)).getPagedOrdersClient(PAGE, MAX_PER_PAGE);
        verify(ordersMethods, times(1)).convertToOrders(paginatedOrderClientDTO);
        verify(ordersRepositoryService, times(1)).saveAllOrders(ordersList);
        verify(ordersMethods, times(1)).convertToOrdersDTO(ordersList);
        verify(ordersMethods, times(1)).generateOrderSummary(ordersDTOList);
    }

    @Test
    public void testGetOrder() {

        when(ordersClientService.getOrderByUUIDClient(UUID)).thenReturn(contentClientDTO);
        when(ordersMethods.convertContentClientDTOToOrderDTO(contentClientDTO)).thenReturn(ordersDTO);

        OrdersSingleResponse actualResponse = ordersService.getOrderByUUID(UUID);

        assertNotNull(actualResponse);
        assertEquals(ordersSingleResponse, actualResponse);
        assertEquals(ordersSingleResponse.getOrder().getOrderId(), actualResponse.getOrder().getOrderId());
        verify(ordersClientService, times(1)).getOrderByUUIDClient(UUID);
        verify(ordersMethods, times(1)).convertContentClientDTOToOrderDTO(contentClientDTO);
    }

    @Test
    public void testGetOrders() {

        when(ordersRepositoryService.findAll()).thenReturn(ordersList);
        when(ordersMethods.convertToOrdersDTO(ordersList)).thenReturn(ordersDTOList);

        OrdersListResponse actualResponse = ordersService.getOrders();

        assertNotNull(actualResponse);
        assertEquals(ordersListResponse, actualResponse);
        verify(ordersRepositoryService, times(1)).findAll();
        verify(ordersMethods, times(1)).convertToOrdersDTO(ordersList);
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
                "",
                "",
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