package cz.martindavidik.accountingservice.controllers;

import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.domain.ExpenseItem;
import cz.martindavidik.accountingservice.services.DateService;
import cz.martindavidik.accountingservice.services.ExpenseItemService;
import cz.martindavidik.accountingservice.services.ExpenseService;
import cz.martindavidik.accountingservice.services.OrganizationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ExpenseController {

    public final ExpenseService expenseService;
    public final OrganizationService organizationService;
    public final DateService dateService;
    public final ExpenseItemService expenseItemService;

    public ExpenseController(
            ExpenseService expenseService,
            OrganizationService organizationService,
            DateService dateService,
            ExpenseItemService expenseItemService
    ) {
        this.expenseService = expenseService;
        this.organizationService = organizationService;
        this.dateService = dateService;
        this.expenseItemService = expenseItemService;
    }

    /**
     * Saves Expense with expense items (bound to Expense)
     *
     * @param supplierIdentificationNumber - IČO
     * @param expenseNumber - Expense´s Primary key
     * @param paymentDate - on which date should payment be proceeded
     * @param expenseItems - JSON
     *
     * @return Expense
     */
    @PostMapping("/createExpense")
    public Optional<Expense> createExpense(
            @RequestParam int supplierIdentificationNumber,
            @RequestParam String expenseNumber,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date paymentDate,
            @RequestBody List<ExpenseItem> expenseItems
    ) {
        // check if organization exist
        if (!organizationService.organizationExist(supplierIdentificationNumber)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Organization with provided identification number does not exist"
            );
        }

        // check if date is not weekend or national holiday
        if (!dateService.dateIsABusinessDay(paymentDate)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Payment date must be business day"
            );
        }

        // save Expense
        Expense expense = new Expense(expenseNumber, supplierIdentificationNumber, paymentDate);
        expenseService.save(expense);

        // save Expense items bound to Expense
        expenseItems.forEach(expenseItem -> {
            ExpenseItem expenseItem1 = new ExpenseItem(expenseItem.getCode(), expenseItem.getDescription(),
                    expenseItem.getNumberOfPieces(), expenseItem.getPricePerPiece(), expense);

            expenseItemService.save(expenseItem1);
        });

        return expenseService.findExpenseByExpenseNumber(expenseNumber);
    }

    /**
     * Provides expense items bound to particular Expense
     *
     * @param expenseNumber - Expense´s Primary key
     *
     * @return List<ExpenseItem>
     */
    @GetMapping("/getExpenseItemsByExpenseNumber/{expenseNumber}")
    public List<ExpenseItem> getExpenseItemsByExpenseNumber(@PathVariable String expenseNumber) {
        return expenseItemService.findByExpenseNumber(expenseNumber);
    }
}
