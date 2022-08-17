package cz.martindavidik.organizationservice.services;

import cz.martindavidik.organizationservice.domain.Organization;

public interface OrganizationService {

    Organization findByName(String name);

    Organization findByIdentificationNumber(int identificationNumber);

    Organization save(Organization organization);

    void delete(Organization organization);
}
