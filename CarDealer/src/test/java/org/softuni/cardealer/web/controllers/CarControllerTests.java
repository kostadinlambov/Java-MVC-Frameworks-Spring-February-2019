package org.softuni.cardealer.web.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.softuni.cardealer.domain.entities.Car;
import org.softuni.cardealer.domain.entities.Part;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.repository.CarRepository;
import org.softuni.cardealer.repository.PartRepository;
import org.softuni.cardealer.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CarControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PartRepository partRepository;


    @Before
    public void init() {
        this.carRepository.deleteAll();
    }

    @Test
    @WithMockUser()
    public void add_savesEntityProperly() throws Exception {
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

        Part savedPart = this.partRepository.saveAndFlush(part);

        List<String> partList = Arrays.asList(savedPart.getId());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.addAll("parts", partList);

        this.mvc
                .perform(post("/cars/add")
                        .param("make", "VW")
                        .param("model", "Golf")
                        .param("travelledDistance", "250")
                        .params(params)
                );


        Car actual = this.carRepository.findAll().get(1);

        Assert.assertEquals(2, this.carRepository.count());
        Assert.assertEquals("BMW", actual.getMake());
        Assert.assertEquals("3", actual.getModel());
        Assert.assertEquals(Long.valueOf(750), actual.getTravelledDistance());
    }

    @Test
    @WithMockUser()
    public void add_addRedirectCorrectly() throws Exception {
        this.mvc
                .perform(post("/cars/add")
                        .param("make", "VW")
                        .param("model", "Golf")
                        .param("travelledDistance", "250")
                )
                .andExpect(view().name("redirect:all"));
    }

    @Test
    @WithMockUser()
    public void edit_editWorksCorrectly() throws Exception {
        Car car = new Car();
        car.setMake("VW");
        car.setModel("Golf");
        car.setTravelledDistance(750L);
        car.setParts(new ArrayList<>());

        Car savedCar = this.carRepository.saveAndFlush(car);

        this.mvc.perform(post("/cars/edit/" + savedCar.getId())
                .param("make", "BMW")
                .param("model", "5")
                .param("travelledDistance", "3200"));

        Car actualCar = this.carRepository.findById(savedCar.getId()).orElse(null);

        Assert.assertEquals(1, this.carRepository.count());
        Assert.assertEquals("BMW", actualCar.getMake());
        Assert.assertEquals("5", actualCar.getModel());
        Assert.assertEquals(Long.valueOf(3200), actualCar.getTravelledDistance());
    }

    @Test
    @WithMockUser()
    public void delete_deleteWorksCorrectly() throws Exception {
        Car car = new Car();
        car.setMake("VW");
        car.setModel("Golf");
        car.setTravelledDistance(750L);
        car.setParts(new ArrayList<>());

        Car savedCar = this.carRepository.saveAndFlush(car);

        this.mvc.perform(post("/cars/delete/" + savedCar.getId()));

        Assert.assertEquals(0, this.carRepository.count());
    }

    @Test(expected = Exception.class)
    @WithMockUser()
    public void delete_deleteThrowExceptionWhenInvalidIdGiven() throws Exception {
        this.mvc.perform(post("/cars/delete/Mercedes"));
    }

    @Test()
    public void all_withGuestRedirectToLogin() throws Exception {
        this.mvc
                .perform(get("/cars/all"))
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    @WithMockUser()
    public void all_ReturnCorrectView() throws Exception {
        this.mvc
                .perform(get("/cars/all"))
                .andExpect(view().name("all-cars"));
    }

    @Test
    @WithMockUser()
    public void all_ReturnCorrectAttribute() throws Exception {
        this.mvc
                .perform(get("/cars/all"))
                .andExpect(model().attributeExists("cars"));
    }
}
