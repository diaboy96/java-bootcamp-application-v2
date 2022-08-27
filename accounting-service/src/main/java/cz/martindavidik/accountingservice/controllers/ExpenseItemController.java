package cz.martindavidik.accountingservice.controllers;

import cz.martindavidik.accountingservice.domain.ExpenseItem;
import cz.martindavidik.accountingservice.services.ExpenseItemService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ExpenseItemController {

    public final ExpenseItemService expenseItemService;

    public ExpenseItemController(ExpenseItemService expenseItemService) {
        this.expenseItemService = expenseItemService;
    }

    /**
     * Create new ExpenseItem (NOT UPDATE)
     *
     * @param code - ExpenseItem´s primary key
     * @param description - description of expense item
     * @param numberOfPieces - number of items
     * @param pricePerPiece - price per 1 piece
     *
     * @return ExpenseItem
     */
    @PostMapping("/createExpenseItem")
    public ExpenseItem createExpenseItem(
            @RequestParam int code,
            @RequestParam String description,
            @RequestParam int numberOfPieces,
            @RequestParam double pricePerPiece
    ) {
        Optional<ExpenseItem> expenseItem = expenseItemService.findById(code);

        if (expenseItem.isPresent()) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
                    "ExpenseItem with provided code has been already created - use updateExpenseItem method instead"
            );
        }

        ExpenseItem expenseItem1 = new ExpenseItem(code, description, numberOfPieces, pricePerPiece);
        return expenseItemService.save(expenseItem1);
    }

    /**
     * Return all expense items
     *
     * @return Iterable<ExpenseItem>
     */
    @GetMapping("/listExpenseItems")
    public Iterable<ExpenseItem> listExpenseItems() {
        return expenseItemService.findAll();
    }

    /**
     * Update existing expense item
     *
     * @param code - ExpenseItem´s primary key
     * @param description - description of expense item
     * @param numberOfPieces - number of items
     * @param pricePerPiece - price per 1 piece
     *
     * @return ExpenseItem
     */
    @PutMapping("/updateExpenseItem/{code}")
    public ExpenseItem updateExpenseItem(
            @PathVariable int code,
            @RequestParam String description,
            @RequestParam int numberOfPieces,
            @RequestParam double pricePerPiece
    ) {
        Optional<ExpenseItem> expenseItem = expenseItemService.findById(code);

        if (expenseItem.isPresent()) {
            ExpenseItem expenseItem1 = expenseItem.get();
            expenseItem1.setDescription(description);
            expenseItem1.setNumberOfPieces(numberOfPieces);
            expenseItem1.setPricePerPiece(pricePerPiece);

            return expenseItemService.save(expenseItem1);
        }

        return null;
    }

    /**
     * Delete ExpenseItem by its code
     *
     * @param code - ExpenseItem´s primary key
     */
    @DeleteMapping("/deleteExpenseItem/{code}")
    public void deleteExpenseItem(@PathVariable int code) {
        Optional<ExpenseItem> expenseItem = expenseItemService.findById(code);

        expenseItem.ifPresent(expenseItemService::delete);
    }
}
