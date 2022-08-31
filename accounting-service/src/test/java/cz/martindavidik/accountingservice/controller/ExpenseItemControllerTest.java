package cz.martindavidik.accountingservice.controller;

import cz.martindavidik.accountingservice.domain.ExpenseItem;
import cz.martindavidik.accountingservice.service.ExpenseItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebMvcTest(ExpenseItemController.class)
public class ExpenseItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private static ExpenseItemService expenseItemService;

    private final int codeOfExistingItem = 2;
    private final int codeOfNonExistingItem = 1;
    private final int numberOfPiecesValue = 10;
    private final double pricePerPieceValue = 10;
    private final String descriptionValue = "lorem ipsum dolor sit amet";

    @BeforeEach
    public void mockData() {
        ExpenseItem savedItem = new ExpenseItem(codeOfNonExistingItem, descriptionValue, numberOfPiecesValue, pricePerPieceValue);

        // data mock - fetch existing item
        Mockito.when(expenseItemService.findById(codeOfExistingItem))
                .thenReturn(
                        Optional.of(new ExpenseItem(codeOfExistingItem, descriptionValue, numberOfPiecesValue, pricePerPieceValue))
                );
        // data mock - fetch non-existing item
        Mockito.when(expenseItemService.findById(codeOfNonExistingItem))
                .thenReturn(
                        Optional.empty()
                );
        // data mock - save new item
        Mockito.when(expenseItemService.save(savedItem))
                .thenReturn(savedItem);
    }

    @Test
    public void testCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    public void testCreateExpenseItem() throws Exception {
        // perform incorrect request - trying to save already saved entity
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/createExpenseItem")
                        .param("code", "2")
                        .param("description", "lorem ipsum")
                        .param("numberOfPieces", "5")
                        .param("pricePerPiece", "10")
                )
                .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());

        // perform correct request
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/createExpenseItem")
                        .param("code", String.valueOf(codeOfNonExistingItem))
                        .param("description", descriptionValue)
                        .param("numberOfPieces", String.valueOf(numberOfPiecesValue))
                        .param("pricePerPiece", String.valueOf(pricePerPieceValue))
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateExpenseItem() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/updateExpenseItem/" + codeOfExistingItem)
                        .param("description", descriptionValue)
                        .param("numberOfPieces", String.valueOf(numberOfPiecesValue))
                        .param("pricePerPiece", String.valueOf(pricePerPieceValue))
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
