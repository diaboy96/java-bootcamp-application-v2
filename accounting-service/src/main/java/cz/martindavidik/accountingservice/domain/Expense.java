package cz.martindavidik.accountingservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Expense {

    @Id
    @Column(unique = true)
    private String expenseNumber;

    @Column(length = 8)
    private int supplier;

    private Date date;

    @OneToMany(mappedBy = "expense")
    private Set<ExpenseItem> expenseItems = new HashSet<>();

    private String PDFinvoiceBase64Encoded;

    private boolean paid = false;
}
