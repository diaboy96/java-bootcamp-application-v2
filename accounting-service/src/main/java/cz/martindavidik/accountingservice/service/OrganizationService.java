package cz.martindavidik.accountingservice.service;

import cz.martindavidik.accountingservice.dto.Organization;
import reactor.core.publisher.Flux;

public interface OrganizationService {

    Flux<Organization> getOrganizationByIdentificationNumber(int identificationNumber);

    boolean organizationExist(int identificationNumber);
}
