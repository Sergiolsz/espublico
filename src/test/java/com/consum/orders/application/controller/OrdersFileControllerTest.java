package com.consum.orders.application.controller;

import com.consum.orders.application.model.OrdersFileResponse;
import com.consum.orders.domain.service.file.OrdersFileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrdersFileControllerTest {

    @Mock
    private OrdersFileService ordersFileService;

    @InjectMocks
    private OrdersFileController ordersFileController;

    @Test
    void testGenerateAndDownloadCSV() {

        byte[] fileBytes = "test csv content".getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders_db.csv");

        OrdersFileResponse ordersFileResponse = createOrderFileResponse(fileBytes, headers);

        when(ordersFileService.generateOrderFileCSV()).thenReturn(ordersFileResponse);

        ResponseEntity<byte[]> result = ordersFileController.generateAndDownloadCSV();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, result.getHeaders().getContentType());
        assertEquals(fileBytes.length, Objects.requireNonNull(result.getBody()).length);
        verify(ordersFileService, times(1)).generateOrderFileCSV();
    }

    /**
     * Constructor OrderFileResponse
     *
     * @param bytes datos fichero
     * @param headers cabeceras del fichero
     * @return OrderFileResponse
     */
    private static OrdersFileResponse createOrderFileResponse(byte[] bytes, HttpHeaders headers) {
        return OrdersFileResponse.builder()
                .fileBytes(bytes)
                .headers(headers)
                .build();
    }
}