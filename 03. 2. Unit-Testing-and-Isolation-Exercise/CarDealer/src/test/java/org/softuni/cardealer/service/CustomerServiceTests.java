package org.softuni.cardealer.service;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.models.service.CarServiceModel;
import org.softuni.cardealer.domain.models.service.CustomerServiceModel;
import org.softuni.cardealer.repository.CarRepository;
import org.softuni.cardealer.repository.CustomerRepository;
import org.softuni.cardealer.repository.PartRepository;
import org.softuni.cardealer.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@DataJpaTest()
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomerServiceTests {

    @Autowired
    private CustomerRepository customerRepository;

    private ModelMapper modelMapper;
    private CustomerService customerService;
    private CustomerServiceModel customerServiceModel;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        customerService = new CustomerServiceImpl(this.customerRepository, this.modelMapper);
        this.createCustomerServiceModel();
    }

    private void createCustomerServiceModel(){
        customerServiceModel = new CustomerServiceModel();
        customerServiceModel.setBirthDate(LocalDate.now());
        customerServiceModel.setName("pesho");
        customerServiceModel.setYoungDriver(true);
    }

    @Test
    public void customerService_saveCustomerWithCorrectValues_ReturnsCorrect() {
        CustomerServiceModel actual = customerService.saveCustomer(customerServiceModel);
        CustomerServiceModel expected = this.modelMapper.map(this.customerRepository.findAll().get(0), CustomerServiceModel.class);

        Assert.assertEquals(expected.getBirthDate(), actual.getBirthDate());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.isYoungDriver(), actual.isYoungDriver());
    }

    @Test(expected = Exception.class)
    public void customerService_saveCustomerWithNullValue_ThrowsException() {
        customerServiceModel.setName(null);
        customerService.saveCustomer(customerServiceModel);
    }

    @Test
    public void customerService_editCustomerWithCorrectValues_ReturnsCorrect() {
        CustomerServiceModel customerServiceModel = customerService.saveCustomer(this.customerServiceModel);

        CustomerServiceModel customerServiceModelEdit = new CustomerServiceModel();
        customerServiceModelEdit.setId(customerServiceModel.getId());
        customerServiceModelEdit.setName("gosho");
        customerServiceModelEdit.setYoungDriver(true);
        customerServiceModelEdit.setBirthDate(LocalDate.now());

        CustomerServiceModel actual = customerService.editCustomer(customerServiceModelEdit);
        CustomerServiceModel expected = this.modelMapper.map(this.customerRepository.findAll().get(0), CustomerServiceModel.class);

        Assert.assertEquals(expected.getBirthDate(), actual.getBirthDate());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.isYoungDriver(), actual.isYoungDriver());
    }

    @Test(expected = Exception.class)
    public void customerService_editCustomerWithNullValues_ThrowsException() {
        CustomerServiceModel customerServiceModel = customerService.saveCustomer(this.customerServiceModel);

        CustomerServiceModel customerServiceModelEdit = new CustomerServiceModel();
        customerServiceModelEdit.setId(customerServiceModel.getId());
        customerServiceModelEdit.setName(null);
        customerServiceModelEdit.setYoungDriver(true);
        customerServiceModelEdit.setBirthDate(LocalDate.now());

        customerService.editCustomer(customerServiceModelEdit);
    }

    @Test
    public void customerService_deleteCustomerWithCorrectValues_ReturnsCorrect() {
        CustomerServiceModel customerServiceModel = customerService.saveCustomer(this.customerServiceModel);

        CustomerServiceModel actual = customerService.deleteCustomer(customerServiceModel.getId());
        CustomerServiceModel expected = this.modelMapper.map(customerServiceModel, CustomerServiceModel.class);

        Assert.assertEquals(expected.getBirthDate(), actual.getBirthDate());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.isYoungDriver(), actual.isYoungDriver());
    }

    @Test(expected = Exception.class)
    public void customerService_deleteCustomerWithInvalidId_ThrowsException() {
        customerService.deleteCustomer("invalid_id");
    }

    @Test
    public void customerService_findByIdCustomerWithCorrectValues_ReturnsCorrect() {
        CustomerServiceModel customerServiceModel = customerService.saveCustomer(this.customerServiceModel);

        CustomerServiceModel actual = customerService.findCustomerById(customerServiceModel.getId());
        CustomerServiceModel expected = this.modelMapper.map(customerServiceModel, CustomerServiceModel.class);

        Assert.assertEquals(expected.getBirthDate(), actual.getBirthDate());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.isYoungDriver(), actual.isYoungDriver());
    }

    @Test(expected = Exception.class)
    public void customerService_findByIdCustomerWithInvalidId_ThrowsException() {
        customerService.findCustomerById("invalid_id");

    }
}
