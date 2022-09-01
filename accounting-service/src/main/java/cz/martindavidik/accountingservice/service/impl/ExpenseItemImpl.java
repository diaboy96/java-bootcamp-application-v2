package cz.martindavidik.accountingservice.service.impl;

import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.domain.ExpenseItem;
import cz.martindavidik.accountingservice.repository.ExpenseItemRepository;
import cz.martindavidik.accountingservice.repository.ExpenseRepository;
import cz.martindavidik.accountingservice.service.ExpenseItemService;
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

    /**
     * Return ExpenseItem by provided code
     *
     * @param code - ExpenseItem´s primary key
     *
     * @return Optional<ExpenseItem>
     */
    @Override
    @Transactional
    public Optional<ExpenseItem> findById(int code) {
        return expenseItemRepository.findById(code);
    }

    /**
     * Returns all ExpenseItem
     *
     * @return Iterable<ExpenseItem>
     */
    @Override
    @Transactional
    public Iterable<ExpenseItem> findAll() {
        return expenseItemRepository.findAll();
    }

    /**
     * Returns set of ExpenseItem bound to particular Expense
     *
     * @param expenseNumber - Expense´s primary key
     *
     * @return Set<ExpenseItem>
     */
    @Override
    @Transactional
    public Set<ExpenseItem> findByExpenseNumber(String expenseNumber) {
        Optional<Expense> expense = expenseRepository.findExpenseByExpenseNumber(expenseNumber);

        return expense.map(Expense::getExpenseItems).orElse(null);
    }

    /**
     * Saves ExpenseItem to database
     *
     * @param expenseItem - ExpenseItem
     *
     * @return ExpenseItem
     */
    @Override
    @Transactional
    public ExpenseItem save(ExpenseItem expenseItem) {
        return expenseItemRepository.save(expenseItem);
    }

    /**
     * Removes ExpenseItem from database
     *
     * @param expenseItem - ExpenseItem
     */
    @Override
    @Transactional
    public void delete(ExpenseItem expenseItem) {
        expenseItemRepository.delete(expenseItem);
    }
}
