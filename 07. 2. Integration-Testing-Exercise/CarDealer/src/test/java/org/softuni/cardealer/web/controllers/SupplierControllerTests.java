package org.softuni.cardealer.web.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SupplierControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SupplierRepository supplierRepository;


    @Before
    public void init() {
        this.supplierRepository.deleteAll();
    }

    @Test
    @WithMockUser("spring")
    public void add_savesEntityProperly() throws Exception {
        this.mvc
                .perform(post("/suppliers/add")
                        .param("name", "pesho")
                        .param("isImporter", "off")
                );

        this.mvc
                .perform(post("/suppliers/add")
                        .param("name", "gosho")
                        .param("isImporter", "true")
                );

        Supplier actual = this.supplierRepository.findAll().get(1);

        Assert.assertEquals(2, this.supplierRepository.count());
        Assert.assertEquals("gosho", actual.getName());
        Assert.assertTrue(actual.getIsImporter());
    }

    @Test
    @WithMockUser("spring")
    public void add_addRedirectCorrectly() throws Exception {
        this.mvc
                .perform(post("/suppliers/add")
                        .param("name", "pesho")
                        .param("isImporter", "off")
                ).andExpect(view().name("redirect:all"));
    }

    @Test
    @WithMockUser("spring")
    public void edit_editWorksCorrectly() throws Exception {
        Supplier first = new Supplier();
        first.setName("pesho");
        first.setIsImporter(false);

        Supplier second = new Supplier();
        second.setName("gosho");
        second.setIsImporter(true);


        Supplier firstSuppl = this.supplierRepository.saveAndFlush(first);
        Supplier secondSuppl = this.supplierRepository.saveAndFlush(second);

        this.mvc.perform(post("/suppliers/edit/" + firstSuppl.getId())
                .param("name", "PESHO")
                .param("isImporter", "true"));

        this.mvc.perform(post("/suppliers/edit/" + secondSuppl.getId())
                .param("name", "Stamat")
                .param("isImporter", "false"));

        Supplier actualFirst = this.supplierRepository.findById(firstSuppl.getId()).orElse(null);
        Supplier actualSecond = this.supplierRepository.findById(secondSuppl.getId()).orElse(null);

        Assert.assertEquals("PESHO", actualFirst.getName());
        Assert.assertTrue(actualFirst.getIsImporter());
        Assert.assertEquals("Stamat", actualSecond.getName());
        Assert.assertFalse(actualSecond.getIsImporter());
    }

    @Test
    @WithMockUser("spring")
    public void delete_deleteWorksCorrectly() throws Exception {
        Supplier first = new Supplier();
        first.setName("pesho");
        first.setIsImporter(false);

        Supplier second = new Supplier();
        second.setName("gosho");
        second.setIsImporter(true);

        Supplier firstSuppl = this.supplierRepository.saveAndFlush(first);
        Supplier secondSuppl = this.supplierRepository.saveAndFlush(second);

        this.mvc.perform(post("/suppliers/delete/" + firstSuppl.getId()));

        Assert.assertEquals(1, this.supplierRepository.count());

        this.mvc.perform(post("/suppliers/delete/" + secondSuppl.getId()));

        Assert.assertEquals(0, this.supplierRepository.count());
    }

    @Test(expected = Exception.class)
    @WithMockUser("spring")
    public void delete_deleteThrowExceptionWhenInvalidIdGiven() throws Exception {

        this.mvc.perform(post("/suppliers/delete/pesho"));
    }

    @Test()
    public void all_withGuestRedirectToLogin() throws Exception {
        this.mvc
                .perform(get("/suppliers/all"))
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser("spring")
    public void all_ReturnCorrectView() throws Exception {
        this.mvc
                .perform(get("/suppliers/all"))
                .andExpect(view().name("all-suppliers"));
    }

    @Test
    @WithMockUser("spring")
    public void all_ReturnCorrectAttribute() throws Exception {
        this.mvc
                .perform(get("/suppliers/all"))
                .andExpect(model().attributeExists("suppliers"));
    }

    @Test
    @WithMockUser("spring")
    public void fetch_worksCorrectly() throws Exception {
        Supplier first = new Supplier();
        first.setName("pesho");
        first.setIsImporter(false);

        Supplier firstSuppl = this.supplierRepository.saveAndFlush(first);

        String contentAsString = this.mvc
                .perform(get("/suppliers/fetch"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String expected = "[{\"id\":\"" + firstSuppl.getId() + "\",\"name\":\"pesho\",\"isImporter\":false}]";

        Assert.assertEquals(expected, contentAsString);
    }

    @Test
    @WithMockUser("spring")
    public void fetch_worksCorrectlyJsonPath() throws Exception {
        Supplier first = new Supplier();
        first.setId("1");
        first.setName("pesho");
        first.setIsImporter(false);

        Supplier second = new Supplier();
        second.setId("2");
        second.setName("gosho");
        second.setIsImporter(true);

        Supplier firstSupplier = this.supplierRepository.saveAndFlush(first);
        Supplier secondSupplier = this.supplierRepository.saveAndFlush(second);

        this.mvc
                .perform(get("/suppliers/fetch"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(firstSupplier.getId())))
                .andExpect(jsonPath("$[0].name", is("pesho")))
                .andExpect(jsonPath("$[0].isImporter", is(false)))
                .andExpect(jsonPath("$[1].id", is(secondSupplier.getId())))
                .andExpect(jsonPath("$[1].name", is("gosho")))
                .andExpect(jsonPath("$[1].isImporter", is(true)));
    }
}
