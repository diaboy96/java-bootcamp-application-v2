package cz.martindavidik.accountingservice.service.impl;

import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.domain.ExpenseItem;
import cz.martindavidik.accountingservice.repository.ExpenseRepository;
import cz.martindavidik.accountingservice.service.ExpenseItemService;
import cz.martindavidik.accountingservice.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Calendar;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
     * Provides list of Expenses by given date
     *
     * @param date - date by which is searched
     * @param paid - whether the expense was paid
     *
     * @return List<Expense>
     */
    @Override
    @Transactional
    public List<Expense> findExpensesByDateAndPaid(LocalDate date, boolean paid) {
        return expenseRepository.findExpenseByDateAndPaid(date, paid);
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
    public List<Expense> findByDateBetween(LocalDate from, LocalDate to) {
        return expenseRepository.findByDateBetween(from, to);
    }

    /**
     * Attach PDF invoice to Expense
     * Decodes Base64 encoded PDF file to PDF format and saves it to FileSystem + file path to database
     *
     * @param expenseNumber - Expense´s Primary key
     * @param base64encodedPDFInvoice - PDF Invoice (base64 encoded)
     *
     * @return Optional<Expense>
     */
    @Override
    @Transactional
    public Optional<Expense> attachExpenseDocument(String expenseNumber, String base64encodedPDFInvoice) {
        Optional<Expense> expense = this.findExpenseByExpenseNumber(expenseNumber);

        if (expense.isPresent()) {
            Expense expense1 = expense.get();
            if (expense1.getPDFinvoicePath() != null) {
                // invoice had been already saved
                return Optional.empty();
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

                return Optional.ofNullable(this.save(expense1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return Optional.empty();
    }

    /**
     * Removes PDF invoice from file system + set path in Expense to null
     *
     * @param expense - Expense
     *
     * @return Optional<Expense>
     */
    @Override
    @Transactional
    public Optional<Expense> removeExpenseDocument(Expense expense) {
        String filePath = expense.getPDFinvoicePath();

        if (filePath != null) {
            File file = new File(filePath);

            try {
                if (Files.deleteIfExists(file.toPath())) {
                    expense.setPDFinvoicePath(null);

                    return Optional.ofNullable(this.save(expense));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Optional.empty();
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
        Set<ExpenseItem> expenseItems = expenseItemService.findByExpenseNumber(expenseNumber);

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
     * Saves Expense with ExpenseItems bound to Expense
     *
     * @param expenseNumber - Expense´s Primary key
     * @param supplierIdentificationNumber - IČO
     * @param paymentDate - on which date should payment be proceeded
     * @param expenseItems - List of expenseItems
     *
     * @return Optional<Expense>
     */
    @Override
    @Transactional
    public Optional<Expense> saveExpenseWithExpenseItems(
            String expenseNumber,
            int supplierIdentificationNumber,
            LocalDate paymentDate,
            List<ExpenseItem> expenseItems
    ) {
        // save expense only with expense items
        if (!expenseItems.isEmpty()) {
            Expense expense = new Expense(expenseNumber, supplierIdentificationNumber, paymentDate);

            // save Expense
            expenseRepository.save(expense);

            // save Expense items bound to Expense
            expenseItems.forEach(expenseItem -> {
                // fetch expenseItem entity from database (previously saved using ExpenseItemController)
                Optional<ExpenseItem> expenseItem1 = expenseItemService.findById(expenseItem.getCode());

                if (expenseItem1.isPresent()) {
                    ExpenseItem expenseItem2 = expenseItem1.get();

                    // bound Expense with ExpenseItem
                    expenseItem2.addExpense(expense);
                    expenseItemService.save(expenseItem2);
                }
            });

            // return saved Expense
            return Optional.of(expense);
        }

        return Optional.empty();
    }

    /**
     * Removes Expense with PDF invoice bound to it
     *
     * @param expense - Expense
     */
    @Override
    @Transactional
    public void delete(Expense expense) {
        // remove PDF invoice (bound to Expense)
        this.removeExpenseDocument(expense);

        // remove link to expense items
        expense.removeAllExpenseItems();

        // remove Expense
        expenseRepository.delete(expense);
    }
}
