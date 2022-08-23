package cz.martindavidik.accountingservice.controllers;

import cz.martindavidik.accountingservice.dto.Organization;
import cz.martindavidik.accountingservice.services.DateService;
import cz.martindavidik.accountingservice.services.ExpenseService;
import cz.martindavidik.accountingservice.services.OrganizationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

import java.util.Date;

@RestController
@RequestMapping("/api")
public class ExpenseController {

    public final ExpenseService expenseService;
    public final OrganizationService organizationService;
    public final DateService dateService;

    public ExpenseController(ExpenseService expenseService, OrganizationService organizationService, DateService dateService) {
        this.expenseService = expenseService;
        this.organizationService = organizationService;
        this.dateService = dateService;
    }

    @PostMapping("/createExpense")
    public /*Expense*/Flux<Organization> createExpense(
            @RequestParam int supplierIdentificationNumber,
            @RequestParam String expenseNumber,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date paymentDate,
            @RequestParam String expenseItems
    ) {
        // check if organization exist
        if (!organizationService.organizationExist(supplierIdentificationNumber)) {
            System.out.println("organizace neexistuje");
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Organization with provided identification number does not exist"
            );
        }

        // check if date is not weekend or national holiday
        if (!dateService.dateIsABusinessDay(paymentDate)) {
            System.out.println("zvolený datum není pracovní den");
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "Payment date must be business day"
            );
        }

        // todo: pořešit parsování expense items + vkládání do databáze

        return organizationService.getOrganizationByIdentificationNumber(supplierIdentificationNumber); // todo returnovat Expense
    }
}
