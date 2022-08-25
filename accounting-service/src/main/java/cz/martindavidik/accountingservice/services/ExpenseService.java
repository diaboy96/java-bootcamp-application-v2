package cz.martindavidik.accountingservice.services;

import cz.martindavidik.accountingservice.domain.Expense;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ExpenseService {

    Optional<Expense> findExpenseByExpenseNumber(String expenseNumber);

    List<Expense> findByDateBetween(Date from, Date to);

    Expense attachExpenseDocument(String expenseNumber, String base64encodedPDFInvoice);

    double getTotalExpenseAmountByExpenseNumber(String expenseNumber);

    Expense save(Expense expense);

    void delete(Expense expense);
}
