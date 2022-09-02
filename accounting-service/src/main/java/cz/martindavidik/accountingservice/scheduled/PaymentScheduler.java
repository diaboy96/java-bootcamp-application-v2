package cz.martindavidik.accountingservice.scheduled;

import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.dto.Organization;
import cz.martindavidik.accountingservice.service.ExpenseService;
import cz.martindavidik.accountingservice.service.OrganizationService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableScheduling
public class PaymentScheduler {

    public final ExpenseService expenseService;
    public final OrganizationService organizationService;

    public PaymentScheduler(ExpenseService expenseService, OrganizationService organizationService) {
        this.expenseService = expenseService;
        this.organizationService = organizationService;
    }

    @Scheduled(cron = "${cron.expression.payexpense}")
    @Transactional
    public void payExpense() {
        LocalDate todayDate = LocalDate.now();
        List<Expense> expenses = expenseService.findExpensesByDateAndPaid(todayDate, false);

        if (!expenses.isEmpty()) {
            expenses.forEach(expense -> {
                // get actual dateTime
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
                String when = LocalDateTime.now().format(formatter);
                // get expenseNumber
                String who = expense.getExpenseNumber();
                // get total amount to be paid
                double amount = expenseService.getTotalExpenseAmountByExpenseNumber(who);
                // get supplier identification number
                int whom = expense.getSupplier();
                // get supplier name (by identification number)
                Flux<Organization> organizationFlux = organizationService.getOrganizationByIdentificationNumber(whom);
                String organizationName = Objects.requireNonNull(organizationFlux.blockFirst()).getName();

                System.out.println("Transaction: " + when + " " + who + " paid " + amount + " Kč to " + organizationName + " (ičo: "+ whom + ")");

                // mark in database as paid
                expense.setPaid(true);
            });
        }
    }
}
