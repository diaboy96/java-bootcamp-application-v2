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

    /**
     * Return all organizations
     *
     * @return Iterable<Organization>
     */
    @Override
    @Transactional
    public Iterable<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    /**
     * Search Organization in database by Organization name (case-insensitive)
     *
     * @param name - Organization´s name
     *
     * @return List<Organization>
     */
    @Override
    @Transactional
    public List<Organization> findByName(String name) {
        return organizationRepository.findOrganizationsByNameContainingIgnoreCase(name);
    }

    /**
     * Search Organization in database by Organization identification number
     *
     * @param identificationNumber - Organization´s identification number
     *
     * @return List<Organization>
     */
    @Override
    @Transactional
    public List<Organization> findByIdentificationNumber(int identificationNumber) {
        return organizationRepository.findByIdentificationNumber(identificationNumber);
    }

    /**
     * Saves Organization to database
     *
     * @param organization - Organization
     *
     * @return Organization
     */
    @Override
    @Transactional
    public Organization save(Organization organization) {
        return organizationRepository.save(organization);
    }

    /**
     * Deletes Organization from database
     *
     * @param organization - Organization
     */
    @Override
    @Transactional
    public void delete(Organization organization) {
        organizationRepository.delete(organization);
    }
}
