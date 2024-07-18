package com.consum.orders.domain.utils;

import com.consum.orders.application.model.OrdersFileResponse;
import com.consum.orders.domain.dto.OrdersDTO;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Clase utilitaria para métodos relacionados con la generación de archivos de pedidos.
 */
@Slf4j
@Component
public class OrdersFileMethods {

    private static final String[] CSV_HEADER = {"Order ID", "Order Priority", "Order Date", "Region", "Country", "Item Type",
            "Sales Channel", "Ship Date", "Units Sold", "Unit Price", "Unit Cost", "Total Revenue", "Total Cost", "Total Profit"};

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Genera un archivo CSV completo a partir de una lista de OrdersDTO.
     *
     * @param ordersDtoList Lista de OrdersDTO.
     * @param fileName      Nombre del archivo base (sin extensión).
     * @return Fichero CSV.
     */
    public OrdersFileResponse generateOrderFileCSV(List<OrdersDTO> ordersDtoList, String fileName) throws IOException {
        log.info("Iniciando la generación del archivo CSV para {} pedidos.", ordersDtoList.size());

        // Generar el contenido CSV
        String csvContent = generateContentCSVFile(ordersDtoList);
        log.debug("Contenido CSV generado: \n{}", csvContent);

        // Generar los bytes del archivo CSV
        byte[] csvBytes = csvContent.getBytes(StandardCharsets.UTF_8);
        log.debug("Bytes del archivo CSV generados, tamaño: {} bytes.", csvBytes.length);

        // Configurar headers para la respuesta HTTP
        String uniqueFileName = generateUniqueFileName(fileName);
        HttpHeaders headers = getCsvHttpHeaders(csvBytes, uniqueFileName);
        log.info("Encabezados HTTP configurados para el archivo: {}", uniqueFileName);

        return OrdersFileResponse.builder()
                .fileBytes(csvBytes)
                .headers(headers)
                .build();
    }

    /**
     * Genera el contenido de un archivo CSV con los datos de los pedidos usando OpenCSV.
     *
     * @param ordersDTOList Listado de pedidos.
     * @return Contenido en formato String.
     */
    private String generateContentCSVFile(List<OrdersDTO> ordersDTOList) throws IOException {
        log.info("Generando contenido CSV para {} pedidos.", ordersDTOList.size());

        try (StringWriter writer = new StringWriter();
             CSVWriter csvWriter = new CSVWriter(writer)) {

            // Escribir el encabezado
            csvWriter.writeNext(CSV_HEADER);

            // Escribir los datos
            for (OrdersDTO order : ordersDTOList) {
                String[] orderData = {
                        order.getOrderId(),
                        order.getOrderPriority(),
                        DATE_FORMAT.format(order.getOrderDate()),
                        order.getRegion(),
                        order.getCountry(),
                        order.getItemType(),
                        order.getSalesChannel(),
                        DATE_FORMAT.format(order.getShipDate()),
                        String.valueOf(order.getUnitsSold()),
                        String.valueOf(order.getUnitPrice()),
                        String.valueOf(order.getUnitCost()),
                        String.valueOf(order.getTotalRevenue()),
                        String.valueOf(order.getTotalCost()),
                        String.valueOf(order.getTotalProfit())
                };
                csvWriter.writeNext(orderData);
            }
            csvWriter.flush();

            log.info("Contenido CSV generado exitosamente.");
            return writer.toString();
        }
    }

    /**
     * Genera los headers HTTP necesarios para un archivo CSV.
     *
     * @param csvBytes Array de bytes del archivo CSV.
     * @param filename Nombre del archivo CSV.
     * @return HttpHeaders configurados para el archivo CSV.
     */
    private HttpHeaders getCsvHttpHeaders(byte[] csvBytes, String filename) {
        log.info("Configurando encabezados HTTP para el archivo: {}", filename);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentLength(csvBytes.length);

        log.info("Encabezados HTTP configurados exitosamente.");
        return headers;
    }

    /**
     * Genera un nombre de archivo único usando un número aleatorio.
     *
     * @param filename Nombre base del archivo.
     * @return Nombre de archivo único con extensión ".csv".
     */
    private String generateUniqueFileName(String filename) {
        // Generar un número aleatorio de 5 dígitos
        log.info("Generando un nombre de archivo único para el archivo base: {}", filename);
        int randomNumber = ThreadLocalRandom.current().nextInt(10000, 100000);

        String uniqueFileName = filename + "_" + randomNumber + ".csv";
        log.info("Nombre de archivo generado: {}", uniqueFileName);

        return uniqueFileName;
    }
}
