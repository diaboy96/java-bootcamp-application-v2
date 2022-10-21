package cz.martindavidik.organizationservice.controller;

import cz.martindavidik.organizationservice.config.ContainersEnvironment;
import cz.martindavidik.organizationservice.domain.Organization;
import cz.martindavidik.organizationservice.service.OrganizationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@WebMvcTest(OrganizationController.class)
public class OrganizationControllerTest extends ContainersEnvironment {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private static OrganizationService organizationService;

    @Test
    public void testCreateMockMvc() {
        assertNotNull(mockMvc);
    }

    @Test
    public void testGetAllOrganizations() throws Exception {
        // data mock
        when(organizationService.getAllOrganizations())
                .thenReturn(
                        List.of(
                                new Organization("CORTEX, a.s.", 47125616),
                                new Organization("TOXIN s.r.o.", 2225166),
                                new Organization("Lundegaard a.s.", 25687221),
                                new Organization("Lundegaarden a.s.", 6636292),
                                new Organization("Johanssen s.r.o.", 25666380),
                                new Organization("Inspirio s.r.o.", 28399439)
                        )
                );

        // controller test
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/getAllOrganizations"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(6))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("CORTEX, a.s."))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].identificationNumber").value(47125616))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("TOXIN s.r.o."))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].identificationNumber").value(2225166))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("Lundegaard a.s."))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].identificationNumber").value(25687221))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].name").value("Lundegaarden a.s."))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].identificationNumber").value(6636292))
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].name").value("Johanssen s.r.o."))
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].identificationNumber").value(25666380))
                .andExpect(MockMvcResultMatchers.jsonPath("$[5].name").value("Inspirio s.r.o."))
                .andExpect(MockMvcResultMatchers.jsonPath("$[5].identificationNumber").value(28399439));
    }

    @Test
    public void testListOrganizations() throws Exception {
        // data mock
        this.findByIdentificationNumberDataMock();
        when(organizationService.findByName("lunde"))
                .thenReturn(
                        List.of(
                                new Organization("Lundegaard a.s.", 25687221),
                                new Organization("Lundegaarden a.s.", 6636292)
                        )
                );

        // search by organization name
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/listOrganizations/lunde"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Lundegaard a.s."))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].identificationNumber").value(25687221))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Lundegaarden a.s."))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].identificationNumber").value(6636292));

        // search by organization identification number
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/listOrganizations/47125616"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("CORTEX, a.s."))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].identificationNumber").value(47125616));
    }

    @Test
    public void testGetOrganization() throws Exception {
        this.findByIdentificationNumberDataMock();

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/api/getOrganization/47125616"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("CORTEX, a.s."))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].identificationNumber").value(47125616));
    }

    @Test
    public void testCreateOrganization() throws Exception {
        // test for inserting correct values
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/createOrganization")
                        .param("name", "Inspirio s.r.o.")
                        .param("identificationNumber", "28399439")
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
        // test for inserting incorrect values
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/api/createOrganization")
                        .param("name", "Test")
                        .param("identificationNumber", "string")
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    /**
     * Method for mocking data
     */
    private void findByIdentificationNumberDataMock() {
        when(organizationService.findByIdentificationNumber(47125616))
                .thenReturn(
                        List.of(new Organization("CORTEX, a.s.", 47125616))
                );
    }
}
