package com.consum.orders.domain.service.api;

import com.consum.orders.application.model.OrdersListResponse;
import com.consum.orders.application.model.OrdersSingleResponse;
import com.consum.orders.application.model.SummaryResponse;
import com.consum.orders.domain.dto.OrdersDTO;
import com.consum.orders.domain.utils.OrdersMethods;
import com.consum.orders.infrastructure.client.dto.ContentClientDTO;
import com.consum.orders.infrastructure.client.dto.PaginatedOrderClientDTO;
import com.consum.orders.infrastructure.database.entity.Orders;
import com.consum.orders.infrastructure.service.OrdersClientService;
import com.consum.orders.infrastructure.service.OrdersRepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class OrdersServiceImpl implements OrdersService {

    private final OrdersClientService ordersClientService;
    private final OrdersRepositoryService ordersRepositoryService;
    private final OrdersMethods ordersMethods;

    public OrdersServiceImpl(OrdersClientService ordersClientService,
                             OrdersRepositoryService ordersRepositoryService, OrdersMethods ordersMethods) {
        this.ordersClientService = ordersClientService;
        this.ordersRepositoryService = ordersRepositoryService;
        this.ordersMethods = ordersMethods;
    }

    @Override
    @CacheEvict(value = "orders", allEntries = true)
    @Transactional
    public SummaryResponse importAndSummarizeOrders(String page, String maxPerPage) {
        log.info("Iniciando proceso de la petición de pedidos, página {}, máximo por página {}", page, maxPerPage);

        // Paso 1: Obtener pedidos desde la API externa
        log.debug("Obteniendo pedidos de la API de Katas...");
        PaginatedOrderClientDTO paginatedOrderClientDTO = ordersClientService.getPagedOrdersClient(page, maxPerPage);

        // Paso 2: Convertir a entidades de órdenes y guardar en la base de datos
        log.debug("Transformando DTOs de pedidos a entidad de la tabla Orders...");
        List<Orders> ordersClient = ordersMethods.convertToOrders(paginatedOrderClientDTO);

        // Paso 3: Guardar los pedidos en la tabla Orders
        log.debug("Guardando pedidos en la base de datos...");
        ordersRepositoryService.saveAllOrders(ordersClient);

        // Paso 4: Mapeo de datos Orders a OrderResponse
        log.debug("Guardando pedidos en la base de datos...");
        List<OrdersDTO> ordersDTO = ordersMethods.convertToOrdersDTO(ordersClient);

        // Paso 5: Generar el resumen del número de pedidos de cada tipo
        var summaryResponse = ordersMethods.generateOrderSummary(ordersDTO);

        // Retornar el resumen y el contenido del archivo CSV
        return SummaryResponse.builder().summary(summaryResponse).build();
    }

    @Override
    @Cacheable(value = "orders", key = "#uuid")
    public OrdersSingleResponse getOrderByUUID(String uuid) {
        log.info("Iniciando proceso de obtener un pedido por su UUID. Servicio: getOrdersByUUID");

        // Paso 1: Obtener pedidos desde la API externa
        log.debug("Obteniendo el pedido por su UUID {} de la API de Katas...", uuid);
        ContentClientDTO contentClientDTO = ordersClientService.getOrderByUUIDClient(uuid);

        log.debug("Transformando el pedido a la respuesta DTO...");
        var ordersDTO = ordersMethods.convertContentClientDTOToOrderDTO(contentClientDTO);

        return OrdersSingleResponse.builder().order(ordersDTO).build();
    }

    @Override
    @Cacheable("orders")
    public OrdersListResponse getOrders() {
        log.info("Iniciando proceso de obtener todos los pedidos de la base de datos. Servicio: getAllOrders");

        log.debug("Transformando los pedidos a la respuesta DTO...");
        var ordersDTO = ordersMethods.convertToOrdersDTO(ordersRepositoryService.findAll());

        return OrdersListResponse.builder().orders(ordersDTO).build();
    }

}
