package cz.martindavidik.accountingservice.services.impl;

import cz.martindavidik.accountingservice.dto.Organization;
import cz.martindavidik.accountingservice.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class OrganizationImpl implements OrganizationService {

    private final WebClient client;

    @Autowired
    public OrganizationImpl(Environment environment) throws Exception {
        String endpoint = environment.getProperty("api.organizationService.endpoint");
        if (endpoint == null) {
            throw new Exception("Endpoint is not defined");
        }

        this.client = WebClient.create(endpoint);
    }

    @Override
    public Flux<Organization> getOrganizationByIdentificationNumber(int identificationNumber) {
        return client.get().uri("/getOrganization/" + identificationNumber).retrieve().bodyToFlux(Organization.class);
    }

    @Override
    public boolean organizationExist(int identificationNumber) {
        Flux<Organization> organizationFlux = this.getOrganizationByIdentificationNumber(identificationNumber);

        return organizationFlux.blockFirst() != null;
    }
}
