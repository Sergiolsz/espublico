package com.consum.orders.domain.utils;

import com.consum.orders.domain.exception.InvalidException.InvalidDateFormatException;
import com.consum.orders.domain.exception.InvalidException.InvalidNumberValueException;
import com.consum.orders.domain.exception.InvalidException.InvalidStringValueException;
import com.consum.orders.infrastructure.client.dto.ContentClientDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Clase de utilidades para validaciones comunes en el servicio de órdenes.
 */
@Slf4j
@Component
public class OrdersValidations {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Valida el parseo de una cadena de fecha en formato "dd/MM/yyyy" a un objeto Date.
     *
     * @param dateString Fecha en formato "dd/MM/yyyy".
     * @param fieldName  Nombre del campo de fecha.
     * @param id         Identificador asociado del objeto JSON.
     * @throws ParseException Si la cadena de fecha es nula, vacía o no tiene el formato esperado.
     */
    public void validateParseDate(String dateString, String fieldName, String id) throws ParseException {
        try {
            dateFormat.parse(dateString);
        } catch (ParseException e) {
            String errorMessage = String.format("Error en el parse del campo %s : %s, id: %s", fieldName, dateString, id);
            log.error(errorMessage, e);
            throw new InvalidDateFormatException(errorMessage, e);
        }
    }

    /**
     * Valida que una cadena de texto no esté vacía o sea nula.
     *
     * @param fieldValue Valor de la cadena a validar.
     * @param fieldName  Nombre del campo para el mensaje de la excepción.
     * @param id         Identificador asociado del objeto JSON.
     * @throws ParseException Si la cadena es nula o está vacía.
     */
    public void validateStringNotEmpty(String fieldValue, String fieldName, String id) throws ParseException {
        if (!StringUtils.hasText(fieldValue)) {
            String errorMessage = String.format("Campo: %s vacío o nulo, id: %s", fieldName, id);
            log.error(errorMessage);
            throw new InvalidStringValueException(errorMessage);
        }
    }

    /**
     * Valida que un número sea positivo.
     *
     * @param fieldValue Número a validar.
     * @param fieldName  Nombre del campo para el mensaje de excepción.
     * @param id         Identificador asociado para el mensaje de excepción.
     * @throws ParseException Si el número es nulo o no es positivo.
     */
    public void validatePositiveNumber(Number fieldValue, String fieldName, String id) throws ParseException {
        if (fieldValue == null || fieldValue.doubleValue() <= 0) {
            String errorMessage = String.format("Invalid positive number for field: %s, id: %s", fieldName, id);
            log.error(errorMessage);
            throw new InvalidNumberValueException(errorMessage);
        }
    }

    /**
     * Valida los campos del ContentClientDTO antes de realizar el mapeo a la entidad Order.
     *
     * @param contentClientDTO DTO que contiene la información de la orden.
     * @throws ParseException Si algún dato no pasa las validaciones.
     */
    public void validateContentClientDTO(ContentClientDTO contentClientDTO) throws ParseException {
        String id = contentClientDTO.getId() != null ? contentClientDTO.getId() : "unknown";
        validateStringNotEmpty(contentClientDTO.getUuid(), "uuid", id);
        validateStringNotEmpty(contentClientDTO.getId(), "id", id);
        validateStringNotEmpty(contentClientDTO.getRegion(), "region", id);
        validateStringNotEmpty(contentClientDTO.getCountry(), "country", id);
        validateStringNotEmpty(contentClientDTO.getItemType(), "itemType", id);
        validateStringNotEmpty(contentClientDTO.getSalesChannel(), "salesChannel", id);
        validateStringNotEmpty(contentClientDTO.getPriority(), "priority", id);
        validateParseDate(contentClientDTO.getDate(), "date", id);
        validateParseDate(contentClientDTO.getShipDate(), "ship_date", id);
        validatePositiveNumber(contentClientDTO.getUnitsSold(), "unitsSold", id);
        validatePositiveNumber(contentClientDTO.getUnitPrice(), "unitPrice", id);
        validatePositiveNumber(contentClientDTO.getUnitCost(), "unitCost", id);
        validatePositiveNumber(contentClientDTO.getTotalRevenue(), "totalRevenue", id);
        validatePositiveNumber(contentClientDTO.getTotalCost(), "totalCost", id);
        validatePositiveNumber(contentClientDTO.getTotalProfit(), "totalProfit", id);
    }
}
