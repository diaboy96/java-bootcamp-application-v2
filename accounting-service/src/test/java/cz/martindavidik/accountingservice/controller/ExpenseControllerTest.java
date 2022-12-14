package cz.martindavidik.accountingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.domain.ExpenseItem;
import cz.martindavidik.accountingservice.service.DateService;
import cz.martindavidik.accountingservice.service.ExpenseItemService;
import cz.martindavidik.accountingservice.service.ExpenseService;
import cz.martindavidik.accountingservice.service.OrganizationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.time.Month;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebMvcTest(ExpenseController.class)
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private static ExpenseService expenseService;

    @MockBean
    private static OrganizationService organizationService;

    @MockBean
    private static DateService dateService;

    @MockBean
    private static ExpenseItemService expenseItemService;

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    private final int existingOrganization = 6636292; // organization??s identification number
    private final LocalDate businessDay = LocalDate.of(2022, Month.JULY, 12);
    private final String expenseNumber = "CZ20220001";
    private final String responseDate = "2022-07-12";
    private final ExpenseItem expenseItem1 = new ExpenseItem(224, "Popis polo??ky ??.1", 7, 14.90);
    private final ExpenseItem expenseItem2 = new ExpenseItem(1027, "Popis polo??ky ??.2", 42, 769);

    @Test
    public void testCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    public void testCreateExpense() throws Exception {
        String businessDayString = "2022-07-12";
        String weekendDayString = "2022-08-13";
        String publicHolidayString = "2022-07-06";
        // create expenseItems list
        List<ExpenseItem> expenseItems = new ArrayList<>();
        expenseItems.add(expenseItem1);
        expenseItems.add(expenseItem2);

        // convert List<ExpenseItem> to JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(expenseItems);

        // data mock
        int nonexistentOrganization = 12345678; // organization identification number
        LocalDate weekendDay = LocalDate.of(2022, Month.AUGUST, 13);
        LocalDate publicHoliday = LocalDate.of(2022, Month.JULY, 6);

        Mockito.when(organizationService.organizationExist(existingOrganization)).thenReturn(true);
        Mockito.when(organizationService.organizationExist(nonexistentOrganization)).thenReturn(false);
        Mockito.when(dateService.dateIsABusinessDay(businessDay)).thenReturn(true);
        Mockito.when(dateService.dateIsABusinessDay(weekendDay)).thenReturn(false);
        Mockito.when(dateService.dateIsABusinessDay(publicHoliday)).thenReturn(false);

        // correct request (existing organization + business day)
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/createExpense")
                        .param("supplierIdentificationNumber", String.valueOf(existingOrganization))
                        .param("expenseNumber", expenseNumber)
                        .param("paymentDate", businessDayString)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());

        // incorrect request (existing organization + weekend day)
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/createExpense")
                        .param("supplierIdentificationNumber", String.valueOf(existingOrganization))
                        .param("expenseNumber", expenseNumber)
                        .param("paymentDate", weekendDayString)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        // incorrect request (existing organization + public holiday day)
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/createExpense")
                        .param("supplierIdentificationNumber", String.valueOf(existingOrganization))
                        .param("expenseNumber", expenseNumber)
                        .param("paymentDate", publicHolidayString)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        // incorrect request (non-existent organization + business day)
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/createExpense")
                        .param("supplierIdentificationNumber", String.valueOf(nonexistentOrganization))
                        .param("expenseNumber", expenseNumber)
                        .param("paymentDate", businessDayString)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(requestJson)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void testGetExpenseItemsByExpenseNumber() throws Exception {
        // data mock
        Mockito.when(expenseItemService.findByExpenseNumber(expenseNumber))
                .thenReturn(
                        Set.of(expenseItem1)
                );

        // perform request
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/getExpenseItemsByExpenseNumber/" + expenseNumber))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].code").value(224))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("Popis polo??ky ??.1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].numberOfPieces").value(7))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].pricePerPiece").value(14.90));
    }

    @Test
    public void testListExpenses() throws Exception {
        String fromDateString = "2022-07-10";
        LocalDate fromDate = LocalDate.of(2022, Month.JULY, 10);
        String toDateString = "2022-07-15";
        LocalDate toDate = LocalDate.of(2022, Month.JULY, 15);

        // data mock
        Mockito.when(expenseService.findByDateBetween(fromDate, toDate))
                .thenReturn(
                        List.of(new Expense(expenseNumber, existingOrganization, businessDay))
                );

        // perform correct request
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/listExpenses/")
                        .param("from", fromDateString)
                        .param("to", toDateString)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].expenseNumber").value(expenseNumber))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].supplier").value(existingOrganization))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date").value(responseDate));

        // perform incorrect request (without parameters)
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/listExpenses"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testUploadExpenseDocument() throws Exception {
        String base64encodedFile = "JVBERi0xLjUKJYCBgoMKMSAwIG9iago8PC9GaWx0ZXIvRmxhdGVEZWNvZGUvRmlyc3QgMTQxL04gMjAvTGVuZ3==";
        String pdfInvoicePath = "./invoices/1661436227729.pdf";

        // data mock
        Mockito.when(expenseService.attachExpenseDocument(expenseNumber, base64encodedFile))
                .thenReturn(
                        Optional.of(new Expense(expenseNumber, existingOrganization, businessDay, pdfInvoicePath, false))
                );

        // perform correct request
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/uploadExpenseDocument")
                        .param("expenseNumber", expenseNumber)
                        .content(base64encodedFile)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("expenseNumber").value(expenseNumber))
                .andExpect(MockMvcResultMatchers.jsonPath("supplier").value(existingOrganization))
                .andExpect(MockMvcResultMatchers.jsonPath("date").value(responseDate))
                .andExpect(MockMvcResultMatchers.jsonPath("paid").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("pdfinvoicePath").value(pdfInvoicePath));
    }

    @Test
    public void testDownloadExpenseDocument() throws Exception {
        String base64encodedFile = "JVBERi0xLjUKJYCBgoMKMSAwIG9iago8PC9GaWx0ZXIvRmxhdGVEZWNvZGUvRmlyc3QgMTQxL04gMjAvTGVuZ3==";

        // data mock
        Mockito.when(expenseService.getExpenseDocument(expenseNumber))
                .thenReturn(base64encodedFile);

        // perform correct request
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/downloadExpenseDocument/" + expenseNumber)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(base64encodedFile));
    }

    @Test
    public void testGetTotalExpenseAmountByExpenseNumber() throws Exception {
        // data mock
        Mockito.when(expenseService.getTotalExpenseAmountByExpenseNumber(expenseNumber))
                .thenReturn(20745.9);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/getTotalExpenseAmountByExpenseNumber/" + expenseNumber)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(20745.9));
    }
}
