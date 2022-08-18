package cz.martindavidik.organizationservice.services;

import cz.martindavidik.organizationservice.domain.Organization;

import java.util.List;

public interface OrganizationService {

    List<Organization> getAllOrganizations();

    List<Organization> findByName(String name);

    List<Organization> findByIdentificationNumber(int identificationNumber);

    Organization save(Organization organization);

    void delete(Organization organization);
}
