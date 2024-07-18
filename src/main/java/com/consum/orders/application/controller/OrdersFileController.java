package com.consum.orders.application.controller;

import com.consum.orders.application.model.OrdersFileResponse;
import com.consum.orders.domain.service.file.OrdersFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Descarga de Ficheros", description = "API para la gestión de ficheros de los pedidos")
@RequestMapping("/file")
public class OrdersFileController {

    private final OrdersFileService ordersFileService;

    public OrdersFileController(OrdersFileService ordersFileService) {
        this.ordersFileService = ordersFileService;
    }

    /**
     * Endpoint para generar y descargar un archivo CSV de pedidos.
     *
     * @return Fichero CSV de los pedidos
     */
    @Operation(summary = "Generar y Descargar CSV de Pedidos",
            description = "Genera un archivo CSV con los pedidos y lo descarga.")
    @GetMapping("/csv")
    public ResponseEntity<byte[]> generateAndDownloadCSV() {

        OrdersFileResponse fileResponse = ordersFileService.generateOrderFileCSV();

        return ResponseEntity.ok()
                .headers(fileResponse.getHeaders())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileResponse.getFileBytes());
    }

}
