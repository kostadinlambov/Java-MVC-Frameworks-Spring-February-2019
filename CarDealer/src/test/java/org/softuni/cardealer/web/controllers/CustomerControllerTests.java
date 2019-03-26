package org.softuni.cardealer.web.controllers;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.domain.entities.Customer;
import org.softuni.cardealer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomerControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CustomerRepository customerRepository;


    @Before
    public void init() {
        this.customerRepository.deleteAll();
    }

    @Test
    @WithMockUser()
    public void add_savesEntityProperly() throws Exception {
        this.mvc
                .perform(post("/customers/add")
                        .param("name", "pesho")
                        .param("birthDate", String.valueOf(LocalDate.now()))
                );

        this.mvc
                .perform(post("/customers/add")
                        .param("name", "gosho")
                        .param("birthDate", String.valueOf(LocalDate.now()))
                );

        Customer actual = this.customerRepository.findAll().get(1);

        Assert.assertEquals(2, this.customerRepository.count());
        Assert.assertEquals("gosho", actual.getName());
        Assert.assertEquals(LocalDate.now(), actual.getBirthDate());
    }

    @Test
    @WithMockUser()
    public void add_addRedirectCorrectly() throws Exception {
        this.mvc
                .perform(post("/customers/add")
                        .param("name", "gosho")
                        .param("birthDate", String.valueOf(LocalDate.now()))
                ).andExpect(view().name("redirect:all"));
    }

    @Test
    @WithMockUser()
    public void all_ReturnCorrectView() throws Exception {
        this.mvc
                .perform(get("/customers/all"))
                .andExpect(view().name("all-customers"));
    }

    @Test
    @WithMockUser()
    public void all_ReturnCorrectAttribute() throws Exception {
        this.mvc
                .perform(get("/customers/all"))
                .andExpect(model().attributeExists("customers"));
    }

}
