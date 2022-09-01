package cz.martindavidik.accountingservice.service;

import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.domain.ExpenseItem;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ExpenseService {

    Optional<Expense> findExpenseByExpenseNumber(String expenseNumber);

    List<Expense> findExpensesByDateAndPaid(Date date, boolean paid);

    List<Expense> findByDateBetween(Date from, Date to);

    Expense attachExpenseDocument(String expenseNumber, String base64encodedPDFInvoice);

    Expense removeExpenseDocument(Expense expense);

    String getExpenseDocument(String expenseNumber);

    double getTotalExpenseAmountByExpenseNumber(String expenseNumber);

    Expense save(Expense expense);

    Expense saveExpenseWithExpenseItems(String expenseNumber, int supplierIdentificationNumber, Date paymentDate, List<ExpenseItem> expenseItems);

    void delete(Expense expense);
}
