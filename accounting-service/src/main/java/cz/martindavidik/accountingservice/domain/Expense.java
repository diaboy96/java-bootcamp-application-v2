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

    public Expense() {
    }

    public Expense(String expenseNumber, int supplier, Date date) {
        this.expenseNumber = expenseNumber;
        this.supplier = supplier;
        this.date = date;
    }

    public String getExpenseNumber() {
        return expenseNumber;
    }

    public void setExpenseNumber(String expenseNumber) {
        this.expenseNumber = expenseNumber;
    }

    public int getSupplier() {
        return supplier;
    }

    public void setSupplier(int supplier) {
        this.supplier = supplier;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPDFinvoiceBase64Encoded() {
        return PDFinvoiceBase64Encoded;
    }

    public void setPDFinvoiceBase64Encoded(String PDFinvoiceBase64Encoded) {
        this.PDFinvoiceBase64Encoded = PDFinvoiceBase64Encoded;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
