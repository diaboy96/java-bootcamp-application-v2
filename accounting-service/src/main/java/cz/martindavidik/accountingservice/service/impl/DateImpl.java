package cz.martindavidik.accountingservice.service.impl;

import cz.martindavidik.accountingservice.service.DateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Service
public class DateImpl implements DateService {

    private final WebClient client;
    private static String COUNTRY_CODE = "cz";

    /**
     * Constructor
     * creates instance of WebClient and setup country code (for searching national holiday in API)
     *
     * @param environment - Environment
     *
     * @throws Exception - when endpoint value is not defined in application.properties
     */
    @Autowired
    public DateImpl(Environment environment) throws Exception {
        String endpoint = environment.getProperty("api.dateNager.endpoint");
        if (endpoint == null) {
            throw new Exception("Endpoint is not defined");
        }

        String countryCode = environment.getProperty("api.dateNager.countryCode");
        if (countryCode != null) {
            COUNTRY_CODE = countryCode;
        }

        this.client = WebClient.create(endpoint);
    }

    /**
     * Return true when day is business day (it is not weekend or national holiday)
     *
     * @param date - date to be checked
     *
     * @return boolean
     */
    @Override
    public boolean dateIsABusinessDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String searchedDate = dateFormat.format(date);

        Flux<cz.martindavidik.accountingservice.dto.Date> dateFlux = client.get()
                .uri("/PublicHolidays/" + calendar.get(Calendar.YEAR) + "/" + COUNTRY_CODE)
                .retrieve().bodyToFlux(cz.martindavidik.accountingservice.dto.Date.class);

        boolean dateIsNationalHoliday = dateFlux.toStream().filter(date1 -> Objects.equals(date1.getDate(), searchedDate)).count() > 0;

        return !dateIsNationalHoliday;
    }
}
