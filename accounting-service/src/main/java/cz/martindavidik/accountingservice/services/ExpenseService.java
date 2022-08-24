package cz.martindavidik.accountingservice.services;

import cz.martindavidik.accountingservice.domain.Expense;

import java.util.Optional;

public interface ExpenseService {

    Optional<Expense> findExpenseByExpenseNumber(String expenseNumber);

    Expense save(Expense expense);

    void delete(Expense expense);
}
