package com.consum.orders.domain.utils;

import com.consum.orders.domain.exception.InvalidException.InvalidDateFormatException;
import com.consum.orders.domain.exception.InvalidException.InvalidNumberValueException;
import com.consum.orders.domain.exception.InvalidException.InvalidStringValueException;
import com.consum.orders.infrastructure.client.dto.ContentClientDTO;
import com.consum.orders.infrastructure.client.dto.ContentLinksDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class OrdersValidationsTest {

    @InjectMocks
    private OrdersValidations ordersValidations;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void validateParseDate_validDate_shouldNotThrowException() throws ParseException {
        String dateString = "15/07/2024";
        String fieldName = "date";
        String id = "123";

        ordersValidations.validateParseDate(dateString, fieldName, id);
    }

    @Test
    void validateParseDate_invalidDateFormat_shouldThrowInvalidDateFormatException() {
        String dateString = "2024-07-15";
        String fieldName = "date";
        String id = "123";

        assertThrows(InvalidDateFormatException.class, () -> ordersValidations.validateParseDate(dateString, fieldName, id));
    }

    @Test
    void validateStringNotEmpty_validString_shouldNotThrowException() throws ParseException {
        String fieldValue = "Test";
        String fieldName = "field";
        String id = "123";

        ordersValidations.validateStringNotEmpty(fieldValue, fieldName, id);
    }

    @Test
    void validateStringNotEmpty_emptyString_shouldThrowInvalidStringValueException() {
        String fieldValue = "";
        String fieldName = "field";
        String id = "123";

        assertThrows(InvalidStringValueException.class, () -> ordersValidations.validateStringNotEmpty(fieldValue, fieldName, id));
    }

    @Test
    void validatePositiveNumber_positiveNumber_shouldNotThrowException() throws ParseException {
        Number fieldValue = 10;
        String fieldName = "field";
        String id = "123";

        ordersValidations.validatePositiveNumber(fieldValue, fieldName, id);
    }

    @Test
    void validatePositiveNumber_nonPositiveNumber_shouldThrowInvalidNumberValueException() {
        Number fieldValue = 0;
        String fieldName = "field";
        String id = "123";

        assertThrows(InvalidNumberValueException.class, () -> ordersValidations.validatePositiveNumber(fieldValue, fieldName, id));
    }

    @Test
    void validateContentClientDTO_validContentClientDTO_shouldNotThrowException() throws ParseException {
        ContentClientDTO contentClientDTO = createValidContentClientDTO();

        ordersValidations.validateContentClientDTO(contentClientDTO);
    }

    @Test
    void validateContentClientDTO_invalidContentClientDTO_shouldThrowException() {
        ContentClientDTO contentClientDTO = createInvalidContentClientDTO();

        assertThrows(Exception.class, () -> ordersValidations.validateContentClientDTO(contentClientDTO));
    }

    /**
     * Crea un ContentClientDTO válido para pruebas.
     */
    private ContentClientDTO createValidContentClientDTO() {

        ContentLinksDTO contentLinksDTO = new ContentLinksDTO("https://kata-espublicotech.g3stiona.com:443/v1/orders/1858f59d-8884-41d7-b4fc-88cfbbf00c53");

        return new ContentClientDTO(
                "uuid123",
                "order123",
                "Region",
                "Country",
                "ItemType",
                "SalesChannel",
                "H",
                "15/07/2024", // Fecha válida en formato dd/MM/yyyy
                "20/07/2024", // Fecha válida en formato dd/MM/yyyy
                10,
                100.0,
                50.0,
                1000.0,
                500.0,
                500.0,
                contentLinksDTO
        );
    }

    /**
     * Crea un ContentClientDTO inválido para pruebas.
     */
    private ContentClientDTO createInvalidContentClientDTO() {

        ContentLinksDTO contentLinksDTO = new ContentLinksDTO("https://kata-espublicotech.g3stiona.com:443/v1/orders/1858f59d-8884-41d7-b4fc-88cfbbf00c53");

        return new ContentClientDTO(
                "", // uuid vacío
                "order123",
                "Region",
                "Country",
                "ItemType",
                "SalesChannel",
                "H",
                "2024-07-15", // Fecha en formato incorrecto
                "20/07/2024", // Fecha válida en formato dd/MM/yyyy
                0, // unidades vendidas no positivas
                100.0,
                50.0,
                1000.0,
                500.0,
                500.0,
                contentLinksDTO
        );
    }
}