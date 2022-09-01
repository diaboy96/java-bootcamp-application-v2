package cz.martindavidik.accountingservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties
@Data
public class Date {
    private String date;
    private String localName;
    private String name;
    private String countryCode;
    private Boolean fixed;
    private Boolean global;
    private Object counties;
    private int launchYear;
    private List<String> types;
}
