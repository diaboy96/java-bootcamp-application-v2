package cz.martindavidik.organizationservice.services.impl;

import cz.martindavidik.organizationservice.domain.Organization;
import cz.martindavidik.organizationservice.repositories.OrganizationRepository;
import cz.martindavidik.organizationservice.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public Organization findByName(String name) {
        return organizationRepository.findByName(name);
    }

    @Override
    public Organization findByIdentificationNumber(int identificationNumber) {
        return organizationRepository.findByIdentificationNumber(identificationNumber);
    }

    @Override
    public Organization save(Organization organization) {
        return organizationRepository.save(organization);
    }

    @Override
    public void delete(Organization organization) {
        organizationRepository.delete(organization);
    }
}
