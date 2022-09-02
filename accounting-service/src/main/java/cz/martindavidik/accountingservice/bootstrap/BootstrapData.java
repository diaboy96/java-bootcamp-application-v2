package cz.martindavidik.accountingservice.bootstrap;

import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.domain.ExpenseItem;
import cz.martindavidik.accountingservice.service.ExpenseItemService;
import cz.martindavidik.accountingservice.service.ExpenseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Month;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {

    public final ExpenseService expenseService;
    public final ExpenseItemService expenseItemService;

    public BootstrapData(ExpenseService expenseService, ExpenseItemService expenseItemService) {
        this.expenseService = expenseService;
        this.expenseItemService = expenseItemService;
    }

    @Override
    public void run(String... args) throws Exception {
        // create ExpenseItems
        ExpenseItem expenseItem1 = new ExpenseItem(1, "lorem Ipsum dolor sit amet", 15, 147.90);
        ExpenseItem expenseItem2 = new ExpenseItem(2, "lorem Ipsum dolor sit amet", 10, 97.90);
        ExpenseItem expenseItem3 = new ExpenseItem(3, "lorem Ipsum dolor sit amet", 15, 77.49);
        ExpenseItem expenseItem4 = new ExpenseItem(4, "lorem Ipsum dolor sit amet", 15, 100);

        // save ExpenseItems
        expenseItemService.save(expenseItem1);
        expenseItemService.save(expenseItem2);
        expenseItemService.save(expenseItem3);
        expenseItemService.save(expenseItem4);

        // create Expenses
        LocalDate date1 = LocalDate.of(2022, Month.AUGUST, 2);
        LocalDate date2 = LocalDate.of(2022, Month.SEPTEMBER, 16);
        Expense expense1 = new Expense("CZ20220001", 25687221, date1);
        Expense expense2 = new Expense("CZ20220002", 25687221, date2);

        // save Expenses
        expenseService.save(expense1);
        expenseService.save(expense2);

        // save Expense3 with assigned ExpenseItems
        List<ExpenseItem> expenseItems = new ArrayList<>();
        expenseItems.add(expenseItem1);
        expenseItems.add(expenseItem2);
        expenseItems.add(expenseItem3);

        expenseService.saveExpenseWithExpenseItems("CZ20220003", 25687221,
                LocalDate.of(2022, Month.SEPTEMBER, 2), expenseItems
        );

        // attach base64 encoded invoice to Expense1
        expenseService.attachExpenseDocument("CZ20220001",
                "JVBERi0xLjUKJYCBgoMKMSAwIG9iago8PC9GaWx0ZXIvRmxhdGVEZWNvZGUvRmlyc3QgMTQxL04gMjAvTGVuZ3=="
        );
    }
}
