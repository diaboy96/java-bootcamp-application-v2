package cz.martindavidik.accountingservice.domain;

import org.hibernate.validator.constraints.Range;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ExpenseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int code;

    private String description;

    @Range(min = 1, max = 9999)
    private int numberOfPieces;

    private double pricePerPiece;

    public ExpenseItem() {
    }

    public ExpenseItem(int code, String description, int numberOfPieces, double pricePerPiece) {
        this.code = code;
        this.description = description;
        this.numberOfPieces = numberOfPieces;
        this.pricePerPiece = pricePerPiece;
    }

    public ExpenseItem(int id, int code, String description, int numberOfPieces, double pricePerPiece) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.numberOfPieces = numberOfPieces;
        this.pricePerPiece = pricePerPiece;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberOfPieces() {
        return numberOfPieces;
    }

    public void setNumberOfPieces(int numberOfPieces) {
        this.numberOfPieces = numberOfPieces;
    }

    public double getPricePerPiece() {
        return pricePerPiece;
    }

    public void setPricePerPiece(double pricePerPiece) {
        this.pricePerPiece = pricePerPiece;
    }
}