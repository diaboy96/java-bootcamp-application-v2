package cz.martindavidik.accountingservice.bootstrap;

import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.domain.ExpenseItem;
import cz.martindavidik.accountingservice.services.ExpenseItemService;
import cz.martindavidik.accountingservice.services.ExpenseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse("02/08/2022");
        Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse("26/08/2022");
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

        expenseService.saveExpenseWithExpenseItems("CZ20220002", 25687221,
                new SimpleDateFormat("dd/MM/yyyy").parse("16/09/2022"), expenseItems
        );

        // attach base64 encoded invoice to Expense1
        expenseService.attachExpenseDocument("CZ20220001",
                "JVBERi0xLjUKJYCBgoMKMSAwIG9iago8PC9GaWx0ZXIvRmxhdGVEZWNvZGUvRmlyc3QgMTQxL04gMjAvTGVuZ3=="
        );
    }
}
