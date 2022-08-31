package cz.martindavidik.organizationservice.controller;

import cz.martindavidik.organizationservice.domain.Organization;
import cz.martindavidik.organizationservice.service.OrganizationService;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    /**
     * Return all organizations
     *
     * @return List<Organization>
     */
    @GetMapping("/getAllOrganizations")
    public List<Organization> getAllOrganizations() {
        return organizationService.getAllOrganizations();
    }

    /**
     * Return list of organizations by its name or identification number
     *
     * @param organization - organization name or identification number
     * @return List<Organization>
     */
    @GetMapping("/listOrganizations/{organization}")
    public List<Organization> listOrganizations(@PathVariable String organization) {
        try {
            // user is searching by identification number
            int identificationNumber = Integer.parseInt(organization);

            return organizationService.findByIdentificationNumber(identificationNumber);
        } catch (NumberFormatException e) {
            // user is searching by organization name
            return organizationService.findByName(organization);
        }
    }

    /**
     * Return organization by its identification number
     *
     * @param identificationNumber - organization identification number
     * @return List<Organization>
     */
    @GetMapping("/getOrganization/{identificationNumber}")
    public List<Organization> getOrganization(@PathVariable int identificationNumber) {
        return organizationService.findByIdentificationNumber(identificationNumber);
    }

    /**
     * Create new organization
     *
     * @param name - organization name
     * @param identificationNumber - organization identification number
     * @return Organization
     */
    @PostMapping("/createOrganization")
    public Organization createOrganization(@RequestParam String name, @RequestParam int identificationNumber) {
        try {
            Organization organization = new Organization(name, identificationNumber);

            return organizationService.save(organization);
        } catch (ConstraintViolationException | javax.validation.ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
