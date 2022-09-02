package cz.martindavidik.accountingservice.service.impl;

import cz.martindavidik.accountingservice.service.DateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public boolean dateIsABusinessDay(LocalDate date) {
        // check whether date is weekend day
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return false;
        }

        // date to be checked in particular format
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String searchedDate = dateFormat.format(date);

        // get public holiday days from API
        Flux<cz.martindavidik.accountingservice.dto.Date> dateFlux = client.get()
                .uri("/PublicHolidays/" + date.getYear() + "/" + COUNTRY_CODE)
                .retrieve().bodyToFlux(cz.martindavidik.accountingservice.dto.Date.class);

        // check whether date to be checked was found in API response --> date is public holiday
        boolean dateIsNationalHoliday = dateFlux.toStream().filter(date1 -> Objects.equals(date1.getDate(), searchedDate)).count() > 0;

        return !dateIsNationalHoliday;
    }
}
