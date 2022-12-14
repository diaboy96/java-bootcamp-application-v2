package cz.martindavidik.organizationservice.bootstrap;

import cz.martindavidik.organizationservice.domain.Organization;
import cz.martindavidik.organizationservice.repository.OrganizationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {

    private final OrganizationRepository organizationRepository;

    public BootstrapData(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Organization cortex = new Organization(1, "CORTEX, a.s.", 47125616);
        Organization toxin = new Organization(2, "TOXIN s.r.o.", 2225166);
        Organization lundegaard = new Organization(3, "Lundegaard a.s.", 25687221);
        Organization lundegaarden = new Organization(4, "Lundegaarden a.s.", 6636292);
        Organization johanssen = new Organization(5, "Johanssen s.r.o.", 25666380);
        Organization inspirio = new Organization(6, "Inspirio s.r.o.", 28399439);

        organizationRepository.save(cortex);
        organizationRepository.save(toxin);
        organizationRepository.save(lundegaard);
        organizationRepository.save(lundegaarden);
        organizationRepository.save(johanssen);
        organizationRepository.save(inspirio);

        System.out.println("Started in Bootstrap");
        System.out.println("Number of organizations: " + organizationRepository.count());
    }
}
