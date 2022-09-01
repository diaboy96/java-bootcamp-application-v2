package cz.martindavidik.accountingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties
@Data
public class Organization {
    private int id;
    private String name;
    private int identificationNumber; // IČO
}
