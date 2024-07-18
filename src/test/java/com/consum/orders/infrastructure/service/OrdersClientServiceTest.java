package com.consum.orders.infrastructure.service;

import com.consum.orders.domain.exception.ProcessingException;
import com.consum.orders.infrastructure.client.KatasClientFeign;
import com.consum.orders.infrastructure.client.dto.ContentClientDTO;
import com.consum.orders.infrastructure.client.dto.ContentLinksDTO;
import com.consum.orders.infrastructure.client.dto.LinksClientDTO;
import com.consum.orders.infrastructure.client.dto.PaginatedOrderClientDTO;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdersClientServiceTest {

    private static final String PAGE = "1";
    private static final String MAX_PER_PAGE = "100";
    private static final String UUID = "1858f59d-8884-41d7-b4fc-88cfbbf00c53";

    @Mock
    private KatasClientFeign katasClientFeign;

    @InjectMocks
    private OrdersClientService ordersClientService;

    private ContentClientDTO contentClientDTO;
    private PaginatedOrderClientDTO paginatedOrderClientDTO;

    @BeforeEach
    void setUp() {

        contentClientDTO = createContentClientDTO();
        paginatedOrderClientDTO = createPaginatedOrderClientDTO();
    }

    @Test
    void testGetPagedOrders_Client_Success() {

        when(katasClientFeign.getClientOrders(PAGE, MAX_PER_PAGE)).thenReturn(paginatedOrderClientDTO);

        PaginatedOrderClientDTO result = ordersClientService.getPagedOrdersClient(PAGE, MAX_PER_PAGE);

        assertNotNull(result);
        assertEquals(paginatedOrderClientDTO.getContent().size(), result.getContent().size());
        assertEquals(paginatedOrderClientDTO.getContent().get(0).getId(), result.getContent().get(0).getId());
        assertEquals(paginatedOrderClientDTO.getContent().get(1).getId(), result.getContent().get(1).getId());

        verify(katasClientFeign, times(1)).getClientOrders(PAGE, MAX_PER_PAGE);
    }

    @Test
    void testGetPagedOrders_Client_EmptyResult() {

        when(katasClientFeign.getClientOrders(PAGE, MAX_PER_PAGE)).thenReturn(new PaginatedOrderClientDTO());

        ProcessingException exception = assertThrows(ProcessingException.class,
                () -> ordersClientService.getPagedOrdersClient(PAGE, MAX_PER_PAGE));

        assertEquals("Fallo al obtener pedidos del servicio externo.", exception.getMessage());

        verify(katasClientFeign, times(1)).getClientOrders(PAGE, MAX_PER_PAGE);
    }

    @Test
    void testGetPagedOrders_Client_FeignException() {

        when(katasClientFeign.getClientOrders(PAGE, MAX_PER_PAGE)).thenThrow(FeignException.class);

        ProcessingException exception = assertThrows(ProcessingException.class,
                () -> ordersClientService.getPagedOrdersClient(PAGE, MAX_PER_PAGE));

        assertEquals("Error de comunicación con el servicio externo.", exception.getMessage());

        verify(katasClientFeign, times(1)).getClientOrders(PAGE, MAX_PER_PAGE);
    }

    @Test
    void testGetOrdersByUUID_Client_Success() {

        when(katasClientFeign.getClientOrderByUUID(UUID)).thenReturn(contentClientDTO);

        ContentClientDTO result = ordersClientService.getOrderByUUIDClient(UUID);

        assertNotNull(result);
        assertEquals(result.getId(), contentClientDTO.getId());

        verify(katasClientFeign, times(1)).getClientOrderByUUID(UUID);
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
     * Builder PaginatedOrderClientDTO
     *
     * @return PaginatedOrderClientDTO
     */
    private static PaginatedOrderClientDTO createPaginatedOrderClientDTO() {
        String next = "https://kata-espublicotech.g3stiona.com:443/v1/orders?page=2&max-per-page=100";
        String self = "https://kata-espublicotech.g3stiona.com:443/v1/orders?page=1&max-per-page=100";

        ContentLinksDTO contentLinksDTO1 = new ContentLinksDTO("https://kata-espublicotech.g3stiona.com:443/v1/orders/1858f59d-8884-41d7-b4fc-88cfbbf00c53");
        ContentLinksDTO contentLinksDTO2 = new ContentLinksDTO("https://kata-espublicotech.g3stiona.com:443/v1/orders/6b94d991-33c1-4899-aff4-70f544cdfced");

        ContentClientDTO contentClientDTO1 =
                new ContentClientDTO("1", "uuid1", "region1", "country1", "itemType1",
                        "salesChannel1", "priority1", "01/01/2024", "02/01/2024", 10,
                        100.0, 50.0, 1000.0, 500.0, 500.0,
                        contentLinksDTO1);
        ContentClientDTO contentClientDTO2 =
                new ContentClientDTO("2", "uuid2", "region2", "country2", "itemType2",
                        "salesChannel2", "priority2", "03/01/2024", "04/01/2024", 20,
                        200.0, 100.0, 2000.0, 1000.0, 1000.0,
                        contentLinksDTO2);

        LinksClientDTO linksClientDTO = new LinksClientDTO(next, self);

        return new PaginatedOrderClientDTO(1,
                List.of(contentClientDTO1, contentClientDTO2),
                linksClientDTO);
    }

}