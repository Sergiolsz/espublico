package com.consum.orders.application.controller;

import com.consum.orders.application.model.OrdersListResponse;
import com.consum.orders.application.model.OrdersSingleResponse;
import com.consum.orders.application.model.SummaryResponse;
import com.consum.orders.domain.service.api.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "API gestión datos de Pedidos", description = "API para la gestión de los pedidos")
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    /**
     * Endpoint para importar y resumir pedidos desde la API de Katas.
     *
     * @param page       número de páginas a recuperar
     * @param maxPerPage máximo número de pedidos por página
     * @return resumen de los pedidos importados y resumidos
     */
    @Operation(summary = "Importar y Resumir los pedidos",
            description = "Importa pedidos desde la API de Katas, las guarda en la base de datos y resume los datos.")
    @GetMapping("/import-summary")
    public SummaryResponse importAndSummarizeOrders(
            @Parameter(description = "Números de páginas para la petición", example = "1")
            @RequestParam(defaultValue = "1") String page,
            @Parameter(description = "Máximo de pedidos por página", example = "100")
            @RequestParam(defaultValue = "100") String maxPerPage) {

        return ordersService.importAndSummarizeOrders(page, maxPerPage);
    }

    /**
     * Endpoint para obtener un pedido desde la API de Katas por su UUID
     *
     * @param uuid Identificador del pedido
     * @return Pedido
     */
    @Operation(summary = "Obtener un pedido por su UUID",
            description = "Llamada al endpoint de la API de Katas para obtener un pedido por su identificador UUID.")
    @GetMapping("/{uuid}")
    public OrdersSingleResponse getOrderByUUID(
            @Parameter(description = "Identificador del pedido", example = "1858f59d-8884-41d7-b4fc-88cfbbf00c53")
            @PathVariable String uuid) {

        return ordersService.getOrderByUUID(uuid);
    }

    /**
     * Endpoint para obtener todos los pedidos de la base datos.
     *
     * @return listado de pedidos
     */
    @Operation(summary = "Obtener pedidos", description = "Recupera la lista de pedidos.")
    @GetMapping()
    public OrdersListResponse getOrders() {
        return ordersService.getOrders();
    }

}