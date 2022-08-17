package cz.martindavidik.organizationservice.services.impl;

import cz.martindavidik.organizationservice.repositories.OrganizationRepository;
import cz.martindavidik.organizationservice.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;
}
