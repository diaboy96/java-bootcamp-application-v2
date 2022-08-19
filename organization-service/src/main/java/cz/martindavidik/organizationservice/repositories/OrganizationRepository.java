package cz.martindavidik.organizationservice.repositories;

import cz.martindavidik.organizationservice.domain.Organization;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrganizationRepository extends CrudRepository<Organization, Integer> {

    @Query(value = "SELECT * from organization", nativeQuery = true)
    List<Organization> getAllOrganizations();

    @Query(value = "SELECT * from organization WHERE organization.name ILIKE %:name%", nativeQuery = true)
    List<Organization> findByName(String name);

    List<Organization> findByIdentificationNumber(int identificationNumber);
}
