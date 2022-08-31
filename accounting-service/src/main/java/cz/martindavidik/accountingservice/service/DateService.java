package cz.martindavidik.accountingservice.service;

import java.util.Date;

public interface DateService {

    boolean dateIsABusinessDay(Date date);
}
