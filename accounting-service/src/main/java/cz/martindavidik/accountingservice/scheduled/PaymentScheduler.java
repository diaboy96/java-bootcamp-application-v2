package cz.martindavidik.accountingservice.scheduled;

import cz.martindavidik.accountingservice.domain.Expense;
import cz.martindavidik.accountingservice.dto.Organization;
import cz.martindavidik.accountingservice.services.ExpenseService;
import cz.martindavidik.accountingservice.services.OrganizationService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public void payExpense() throws ParseException {
        String dateWithoutTime = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        Date todayDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateWithoutTime);
        List<Expense> expenses = expenseService.findExpensesByDateAndPaid(todayDate, false);

        if (!expenses.isEmpty()) {
            expenses.forEach(expense -> {
                String when = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
                String who = expense.getExpenseNumber();
                double amount = expenseService.getTotalExpenseAmountByExpenseNumber(who);
                int whom = expense.getSupplier();
                Flux<Organization> organizationFlux = organizationService.getOrganizationByIdentificationNumber(whom);
                String organizationName = Objects.requireNonNull(organizationFlux.blockFirst()).getName();

                System.out.println("Transaction: " + when + " " + who + " paid " + amount + " Kč to " + organizationName + " (ičo: "+ whom + ")");

                // mark in database as paid
                expense.setPaid(true);
            });
        }
    }
}
