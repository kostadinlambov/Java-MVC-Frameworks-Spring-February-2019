package org.softuni.cardealer.web.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.entities.Part;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.domain.models.service.PartServiceModel;
import org.softuni.cardealer.domain.models.service.SupplierServiceModel;
import org.softuni.cardealer.repository.PartRepository;
import org.softuni.cardealer.repository.SupplierRepository;
import org.softuni.cardealer.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PartControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Before
    public void init() {
        this.partRepository.deleteAll();
        this.supplierRepository.deleteAll();

        Supplier supplier = new Supplier();
        supplier.setName("pesho");
        supplier.setIsImporter(false);

        this.supplierRepository.saveAndFlush(supplier);

        Part part = new Part();
        part.setName("part");
        part.setPrice(new BigDecimal("100.25"));
        part.setSupplier(supplier);

        this.partRepository.saveAndFlush(part);
    }

    @Test
    @WithMockUser
    public void addPart_ReturnsCorrectView() throws Exception {
        Supplier supplier = this.supplierRepository.findAll().get(0);

        this.mvc
                .perform(post("/parts/add")
                        .param("name", "part1")
                        .param("price", "100")
                        .param("supplier", supplier.getName()))
                .andExpect(view().name("redirect:/parts/all"))
                .andExpect(redirectedUrl("/parts/all"));
    }

    @Test
    @WithMockUser
    public void addPart_SavesPartCorrectly() throws Exception {
        Supplier supplier = this.supplierRepository.findAll().get(0);

        this.mvc
                .perform(post("/parts/add")
                        .param("name", "part2")
                        .param("price", "100.38")
                        .param("supplier", supplier.getName()));

        Part actual = this.partRepository.findAll().get(1);

        Assert.assertEquals(2, this.partRepository.count());
        Assert.assertEquals("part2", actual.getName());
        Assert.assertEquals(new BigDecimal("100.38"), actual.getPrice());
        Assert.assertEquals("pesho", actual.getSupplier().getName());
    }

    @Test(expected = Exception.class)
    @WithMockUser
    public void addPart_partNameIsNull_ThrowsException() throws Exception {
        Supplier supplier = this.supplierRepository.findAll().get(0);

        this.mvc
                .perform(post("/parts/add")
                        .param("name", isNull())
                        .param("price", "100")
                        .param("supplier", supplier.getName()));
    }

    @Test
    @WithMockUser()
    public void edit_editWorksCorrectly() throws Exception {
        Part part = this.partRepository.findAll().get(0);

        this.mvc.perform(post("/parts/edit/" + part.getId())
                .param("name", "editedPart")
                .param("price", "50.38"))
                .andExpect(view().name("redirect:/parts/all"))
                .andExpect(redirectedUrl("/parts/all"));


        Part actualPart = this.partRepository.findById(part.getId()).orElse(null);


        Assert.assertEquals("editedPart", actualPart.getName());
        Assert.assertEquals(new BigDecimal("50.38"), actualPart.getPrice());

    }

    @Test
    @WithMockUser()
    public void delete_deleteWorksCorrectly() throws Exception {
        Part part = this.partRepository.findAll().get(0);

        this.mvc.perform(post("/parts/delete/" + part.getId()));

        Assert.assertEquals(0, this.partRepository.count());
    }

    @Test(expected = Exception.class)
    @WithMockUser()
    public void delete_deleteThrowExceptionWhenInvalidIdGiven() throws Exception {

        this.mvc.perform(post("/parts/delete/pesho"));
    }

    @Test
    @WithMockUser()
    public void all_ReturnCorrectView() throws Exception {
        this.mvc
                .perform(get("/parts/all"))
                .andExpect(view().name("all-parts"));
    }

    @Test
    @WithMockUser("spring")
    public void all_ReturnCorrectAttribute() throws Exception {
        this.mvc
                .perform(get("/parts/all"))
                .andExpect(model().attributeExists("parts"));
    }

    @Test
    public void fetch_ReturnsCorrectView() {

    }

    @Test
    @WithMockUser("spring")
    public void fetch_worksCorrectlyJsonPath() throws Exception {
        Part part = this.partRepository.findAll().get(0);

        PartServiceModel partServiceModel = this.modelMapper.map(part, PartServiceModel.class);

        this.mvc
                .perform(get("/parts/fetch"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(partServiceModel.getId())))
                .andExpect(jsonPath("$[0].name", is("part")))
                .andExpect(jsonPath("$[0].price", is(closeTo(100.25, 0.0005))));

    }
}
