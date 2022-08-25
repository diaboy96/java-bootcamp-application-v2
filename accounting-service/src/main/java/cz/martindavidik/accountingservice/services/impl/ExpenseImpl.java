package cz.martindavidik.accountingservice.services.impl;

import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.domain.ExpenseItem;
import cz.martindavidik.accountingservice.repositories.ExpenseRepository;
import cz.martindavidik.accountingservice.services.ExpenseItemService;
import cz.martindavidik.accountingservice.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseItemService expenseItemService;

    /**
     * Provides Expense according to expenseNumber
     *
     * @param expenseNumber - Expense´s Primary key
     *
     * @return Optional<Expense>
     */
    @Override
    @Transactional
    public Optional<Expense> findExpenseByExpenseNumber(String expenseNumber) {
        return expenseRepository.findExpenseByExpenseNumber(expenseNumber);
    }

    /**
     * Provides Expenses between specified dates
     *
     * @param from - from which date should be searched
     * @param to - to which date should be searched
     *
     * @return List<Expense>
     */
    @Override
    @Transactional
    public List<Expense> findByDateBetween(Date from, Date to) {
        return expenseRepository.findByDateBetween(from, to);
    }

    /**
     * Attach PDF invoice to Expense
     *
     * @param expenseNumber - Expense´s Primary key
     * @param base64encodedPDFInvoice - PDF Invoice (base64 encoded)
     *
     * @return Expense
     */
    @Override
    @Transactional
    public Expense attachExpenseDocument(String expenseNumber, String base64encodedPDFInvoice) {
        Optional<Expense> expense = this.findExpenseByExpenseNumber(expenseNumber);

        if (expense.isPresent()) {
            Expense expense1 = expense.get();
            expense1.setPDFinvoiceBase64Encoded(base64encodedPDFInvoice);

            return this.save(expense1);
        }

        return null;
    }

    /**
     * Calculates the total value of expenses according to expense items
     *
     * @param expenseNumber - Expense´s Primary key
     *
     * @return double
     */
    @Override
    @Transactional
    public double getTotalExpenseAmountByExpenseNumber(String expenseNumber) {
        List<ExpenseItem> expenseItems = expenseItemService.findByExpenseNumber(expenseNumber);

        if (expenseItems != null) {
            var wrapper = new Object() {
                double totalExpenses = 0;
            };

            expenseItems.forEach(expenseItem -> {
                wrapper.totalExpenses = wrapper.totalExpenses + (expenseItem.getNumberOfPieces() * expenseItem.getPricePerPiece());
            });

            return wrapper.totalExpenses;
        }

        return 0;
    }

    /**
     * Saves Expense
     *
     * @param expense - Expense
     *
     * @return Expense
     */
    @Override
    @Transactional
    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

    /**
     * Removes Expense
     *
     * @param expense - Expense
     */
    @Override
    @Transactional
    public void delete(Expense expense) {
        expenseRepository.delete(expense);
    }
}
