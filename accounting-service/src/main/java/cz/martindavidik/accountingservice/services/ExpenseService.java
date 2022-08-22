package cz.martindavidik.accountingservice.services;

import cz.martindavidik.accountingservice.domain.Expense;

public interface ExpenseService {

    Expense save(Expense expense);

    void delete(Expense expense);
}
