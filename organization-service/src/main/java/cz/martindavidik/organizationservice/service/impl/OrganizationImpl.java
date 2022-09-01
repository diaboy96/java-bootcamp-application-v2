package cz.martindavidik.organizationservice.service.impl;

import cz.martindavidik.organizationservice.domain.Organization;
import cz.martindavidik.organizationservice.repository.OrganizationRepository;
import cz.martindavidik.organizationservice.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrganizationImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    @Transactional
    public Iterable<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    @Override
    @Transactional
    public List<Organization> findByName(String name) {
        return organizationRepository.findOrganizationsByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional
    public List<Organization> findByIdentificationNumber(int identificationNumber) {
        return organizationRepository.findByIdentificationNumber(identificationNumber);
    }

    @Override
    @Transactional
    public Organization save(Organization organization) {
        return organizationRepository.save(organization);
    }

    @Override
    @Transactional
    public void delete(Organization organization) {
        organizationRepository.delete(organization);
    }
}