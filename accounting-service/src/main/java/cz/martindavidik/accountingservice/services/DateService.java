package cz.martindavidik.accountingservice.services;

import java.util.Date;

public interface DateService {

    boolean dateIsABusinessDay(Date date);
}
