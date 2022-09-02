package cz.martindavidik.accountingservice.service;

import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.domain.ExpenseItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseService {

    Optional<Expense> findExpenseByExpenseNumber(String expenseNumber);

    List<Expense> findExpensesByDateAndPaid(LocalDate date, boolean paid);

    List<Expense> findByDateBetween(LocalDate from, LocalDate to);

    Optional<Expense> attachExpenseDocument(String expenseNumber, String base64encodedPDFInvoice);

    Optional<Expense> removeExpenseDocument(Expense expense);

    String getExpenseDocument(String expenseNumber);

    double getTotalExpenseAmountByExpenseNumber(String expenseNumber);

    Expense save(Expense expense);

    Optional<Expense> saveExpenseWithExpenseItems(String expenseNumber, int supplierIdentificationNumber, LocalDate paymentDate, List<ExpenseItem> expenseItems);

    void delete(Expense expense);
}
