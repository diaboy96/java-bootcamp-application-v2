package cz.martindavidik.organizationservice.repositories;

import cz.martindavidik.organizationservice.domain.Organization;
import org.springframework.data.repository.CrudRepository;

public interface OrganizationRepository extends CrudRepository<Organization, Integer> {

}
