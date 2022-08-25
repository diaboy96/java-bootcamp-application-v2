package cz.martindavidik.accountingservice.services.impl;

import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.domain.ExpenseItem;
import cz.martindavidik.accountingservice.repositories.ExpenseRepository;
import cz.martindavidik.accountingservice.services.ExpenseItemService;
import cz.martindavidik.accountingservice.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Calendar;
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
     * Decodes Base64 encoded PDF file to PDF format and saves it to FileSystem + file path to database
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
            if (expense1.getPDFinvoicePath() != null) {
                // invoice has been already saved
                return null;
            }

            // check if directory exist (otherwise create it)
            File directory = new File("./invoices");
            if (!directory.exists()) {
                directory.mkdir();
            }

            String path = "./invoices/" + Calendar.getInstance().getTimeInMillis() + ".pdf";
            File file = new File(path);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                // decode base64 to PDF and save to file system
                byte[] decoder = Base64.getDecoder().decode(base64encodedPDFInvoice);
                fos.write(decoder);

                // set PDF invoice path to Expense
                expense1.setPDFinvoicePath(path);

                return this.save(expense1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Removes PDF invoice from file system + set path in Expense to null
     *
     * @param expense - Expense
     *
     * @return Expense
     */
    @Override
    @Transactional
    public Expense removeExpenseDocument(Expense expense) {
        String filePath = expense.getPDFinvoicePath();

        if (filePath != null) {
            File file = new File(filePath);

            try {
                if (Files.deleteIfExists(file.toPath())) {
                    expense.setPDFinvoicePath(null);

                    return this.save(expense);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Obtains base64 encoded PDF invoice
     *
     * @param expenseNumber - Expense´s Primary key
     *
     * @return Base64 encoded PDF invoice
     */
    @Override
    @Transactional
    public String getExpenseDocument(String expenseNumber) {
        // obtain file path from database
        Optional<Expense> expense = this.findExpenseByExpenseNumber(expenseNumber);

        if (expense.isPresent()) {
            Expense expense1 = expense.get();
            if (expense1.getPDFinvoicePath() != null) {
                String filePath = expense1.getPDFinvoicePath();

                // obtain PDF from file system
                try {
                    File file = new File(filePath);
                    byte[] bytes = Files.readAllBytes(file.toPath());

                    // return Base64 encoded PDF invoice
                    return Base64.getEncoder().encodeToString(bytes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
     * Removes Expense with all it´s dependencies
     *
     * @param expense - Expense
     */
    @Override
    @Transactional
    public void delete(Expense expense) {
        // remove all expenseItems (bound to Expense)
        expenseItemService.deleteExpenseItemsByExpense(expense);

        // remove PDF invoice (bound to Expense)
        this.removeExpenseDocument(expense);

        // remove Expense
        expenseRepository.delete(expense);
    }
}
