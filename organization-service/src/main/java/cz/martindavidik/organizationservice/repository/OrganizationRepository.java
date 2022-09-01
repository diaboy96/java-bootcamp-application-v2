package cz.martindavidik.organizationservice.repository;

import cz.martindavidik.organizationservice.domain.Organization;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrganizationRepository extends CrudRepository<Organization, Integer> {

    List<Organization> findOrganizationsByNameContainingIgnoreCase(String name);

    List<Organization> findByIdentificationNumber(int identificationNumber);
}
