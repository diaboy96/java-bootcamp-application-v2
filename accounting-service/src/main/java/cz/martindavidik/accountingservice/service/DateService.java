package cz.martindavidik.accountingservice.service;

import java.time.LocalDate;

public interface DateService {

    boolean dateIsABusinessDay(LocalDate date);
}
