package cz.martindavidik.accountingservice.services.impl;

import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.domain.ExpenseItem;
import cz.martindavidik.accountingservice.repositories.ExpenseItemRepository;
import cz.martindavidik.accountingservice.repositories.ExpenseRepository;
import cz.martindavidik.accountingservice.services.ExpenseItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseItemImpl implements ExpenseItemService {

    @Autowired
    ExpenseItemRepository expenseItemRepository;

    @Autowired
    ExpenseRepository expenseRepository;

    @Override
    @Transactional
    public List<ExpenseItem> findByExpense(Expense expense) {
        return expenseItemRepository.findByExpense(expense);
    }

    @Override
    @Transactional
    public List<ExpenseItem> findByExpenseNumber(String expenseNumber) {
        Optional<Expense> expense = expenseRepository.findExpenseByExpenseNumber(expenseNumber);

        return expense.map(this::findByExpense).orElse(null);
    }

    @Override
    @Transactional
    public ExpenseItem save(ExpenseItem expenseItem) {
        return expenseItemRepository.save(expenseItem);
    }

    @Override
    @Transactional
    public void delete(ExpenseItem expenseItem) {
        expenseItemRepository.delete(expenseItem);
    }
}
