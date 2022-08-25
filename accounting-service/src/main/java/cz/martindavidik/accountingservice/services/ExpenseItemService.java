package cz.martindavidik.accountingservice.services;

import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.domain.ExpenseItem;

import java.util.List;

public interface ExpenseItemService {

    List<ExpenseItem> findByExpense(Expense expense);

    List<ExpenseItem> findByExpenseNumber(String expenseNumber);

    ExpenseItem save(ExpenseItem expenseItem);

    void deleteExpenseItemsByExpense(Expense expense);

    void delete(ExpenseItem expenseItem);
}
