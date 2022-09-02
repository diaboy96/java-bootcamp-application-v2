package cz.martindavidik.accountingservice.controller;

import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.domain.ExpenseItem;
import cz.martindavidik.accountingservice.service.DateService;
import cz.martindavidik.accountingservice.service.ExpenseItemService;
import cz.martindavidik.accountingservice.service.ExpenseService;
import cz.martindavidik.accountingservice.service.OrganizationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
     * expenseItems must be created before calling this method (create expenseItem can be done by calling ExpenseItemController.createExpenseItem)
     *
     * @param supplierIdentificationNumber - IČO
     * @param expenseNumber - Expense´s Primary key
     * @param paymentDate - on which date should payment be proceeded
     * @param expenseItems - JSON
     *
     * @return Optional<Expense>
     */
    @PutMapping("/createExpense")
    public Optional<Expense> createExpense(
            @RequestParam int supplierIdentificationNumber,
            @RequestParam String expenseNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paymentDate,
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
        return expenseService.saveExpenseWithExpenseItems(expenseNumber, supplierIdentificationNumber, paymentDate, expenseItems);
    }

    /**
     * Provides expense items bound to particular Expense
     *
     * @param expenseNumber - Expense´s Primary key
     *
     * @return List<ExpenseItem>
     */
    @GetMapping("/getExpenseItemsByExpenseNumber/{expenseNumber}")
    public Set<ExpenseItem> getExpenseItemsByExpenseNumber(@PathVariable String expenseNumber) {
        return expenseItemService.findByExpenseNumber(expenseNumber);
    }

    /**
     * Provides Expenses between specified dates
     *
     * @param from - from which date should be searched
     * @param to - to which date should be searched
     *
     * @return List<Expense>
     */
    @GetMapping("/listExpenses")
    public List<Expense> listExpenses(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return expenseService.findByDateBetween(from, to);
    }

    /**
     * Attach PDF invoice to Expense
     * invoice will be saved only if the Expense has no saved invoice (otherwise method return null - the previously saved invoice will NOT be overwritten)
     *
     * @param expenseNumber - Expense´s Primary key
     * @param expenseDocument - base 64 encoded PDF invoice
     *
     * @return Optional<Expense>
     */
    @PutMapping("/uploadExpenseDocument")
    public Optional<Expense> uploadExpenseDocument(@RequestParam String expenseNumber, @RequestBody String expenseDocument) {
        return expenseService.attachExpenseDocument(expenseNumber, expenseDocument);
    }

    /**
     * Obtains PDF invoice (base 64 encoded) attached to Expense
     *
     * @param expenseNumber - Expense´s Primary key
     *
     * @return Base64 encoded PDF invoice
     */
    @GetMapping("/downloadExpenseDocument/{expenseNumber}")
    public String downloadExpenseDocument(@PathVariable String expenseNumber) {
        return expenseService.getExpenseDocument(expenseNumber);
    }

    /**
     * Remove PDF invoice from file system + path to file from database
     *
     * @param expenseNumber - Expense´s Primary key
     *
     * @return Expense
     */
    @DeleteMapping("/removeExpenseDocument/{expenseNumber}")
    public Optional<Expense> removeExpenseDocument(@PathVariable String expenseNumber) {
        Optional<Expense> expense = expenseService.findExpenseByExpenseNumber(expenseNumber);

        return expense.map(expenseService::removeExpenseDocument).orElse(null);
    }

    /**
     * Return the total expenses for all expense items
     *
     * @param expenseNumber - Expense´s Primary key
     *
     * @return double
     */
    @GetMapping("/getTotalExpenseAmountByExpenseNumber/{expenseNumber}")
    public double getTotalExpenseAmountByExpenseNumber(@PathVariable String expenseNumber) {
        return expenseService.getTotalExpenseAmountByExpenseNumber(expenseNumber);
    }

    /**
     * Removes Expense with PDF invoice bound to it
     *
     * @param expenseNumber - Expense´s Primary key
     *
     * @return Expense
     */
    @DeleteMapping("/deleteExpense/{expenseNumber}")
    public Expense deleteExpense(@PathVariable String expenseNumber) {
        Optional<Expense> expense = expenseService.findExpenseByExpenseNumber(expenseNumber);

        if (expense.isPresent()) {
            Expense expense1 = expense.get();
            expenseService.delete(expense1);

            return expense1;
        }

        return null;
    }
}
