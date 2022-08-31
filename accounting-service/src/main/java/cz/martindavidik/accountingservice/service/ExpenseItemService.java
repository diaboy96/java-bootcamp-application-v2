package cz.martindavidik.accountingservice.service;

import cz.martindavidik.accountingservice.domain.ExpenseItem;

import java.util.Optional;
import java.util.Set;

public interface ExpenseItemService {

    Optional<ExpenseItem> findById(int code);

    Iterable<ExpenseItem> findAll();

    Set<ExpenseItem> findByExpenseNumber(String expenseNumber);

    ExpenseItem save(ExpenseItem expenseItem);

    void delete(ExpenseItem expenseItem);
}
