package cz.martindavidik.organizationservice.controllers;

import cz.martindavidik.organizationservice.domain.Organization;
import cz.martindavidik.organizationservice.services.OrganizationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

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

    @GetMapping("/getOrganization/{identificationNumber}")
    public List<Organization> getOrganization(@PathVariable int identificationNumber) {
        return organizationService.findByIdentificationNumber(identificationNumber);
    }
}
