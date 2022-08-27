package cz.martindavidik.accountingservice.services.impl;

import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.domain.ExpenseItem;
import cz.martindavidik.accountingservice.repositories.ExpenseItemRepository;
import cz.martindavidik.accountingservice.repositories.ExpenseRepository;
import cz.martindavidik.accountingservice.services.ExpenseItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
public class ExpenseItemImpl implements ExpenseItemService {

    @Autowired
    ExpenseItemRepository expenseItemRepository;

    @Autowired
    ExpenseRepository expenseRepository;

    @Override
    @Transactional
    public Optional<ExpenseItem> findById(int code) {
        return expenseItemRepository.findById(code);
    }

    @Override
    @Transactional
    public Iterable<ExpenseItem> findAll() {
        return expenseItemRepository.findAll();
    }

    @Override
    @Transactional
    public Set<ExpenseItem> findByExpenseNumber(String expenseNumber) {
        Optional<Expense> expense = expenseRepository.findExpenseByExpenseNumber(expenseNumber);

        return expense.map(Expense::getExpenseItems).orElse(null);
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
