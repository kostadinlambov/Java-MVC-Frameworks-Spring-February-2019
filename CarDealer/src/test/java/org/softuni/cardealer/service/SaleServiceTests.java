package org.softuni.cardealer.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.models.service.*;
import org.softuni.cardealer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

@RunWith(SpringRunner.class)
@DataJpaTest()
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SaleServiceTests {
    @Autowired
    private CarSaleRepository carSaleRepository;

    @Autowired
    private PartSaleRepository partSaleRepository;

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    private ModelMapper modelMapper;
    private SaleService saleService;
    private PartService partService;
    private CustomerService customerService;
    private CarService carService;
    private PartSaleServiceModel partSaleServiceModel;
    private CarSaleServiceModel carSaleServiceModel;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        saleService = new SaleServiceImpl(this.carSaleRepository, this.partSaleRepository, this.modelMapper);
        partService = new PartServiceImpl(this.partRepository, this.modelMapper, this.supplierRepository, this.carService);
        customerService = new CustomerServiceImpl(this.customerRepository, this.modelMapper);
        carService = new CarServiceImpl(this.carRepository,this.partRepository, this.modelMapper);

        this.createSaleServiceModels();
    }

    private void createSaleServiceModels(){
        PartServiceModel partServiceModel = new PartServiceModel();
        partServiceModel.setName("pesho");
        partServiceModel.setPrice(BigDecimal.TEN);
        partServiceModel.setSupplier(null);

        PartServiceModel partServiceModel1 = this.partService.savePart(partServiceModel);

        CarServiceModel carServiceModel = new CarServiceModel();
        carServiceModel.setMake("BMW");
        carServiceModel.setModel("M3");
        carServiceModel.setTravelledDistance(25L);
        carServiceModel.setParts(new ArrayList<>());

        CarServiceModel carServiceModel1 = this.carService.saveCar(carServiceModel);

        CustomerServiceModel customerServiceModel  = new CustomerServiceModel();
        customerServiceModel.setBirthDate(LocalDate.now());
        customerServiceModel.setName("pesho");
        customerServiceModel.setIsYoungDriver(true);

        CustomerServiceModel customerServiceModel1 = this.customerService.saveCustomer(customerServiceModel);

        partSaleServiceModel = new PartSaleServiceModel();
        partSaleServiceModel.setQuantity(5);
        partSaleServiceModel.setDiscount(25.52);
        partSaleServiceModel.setCustomer(customerServiceModel1);
        partSaleServiceModel.setPart(partServiceModel1);

        carSaleServiceModel = new CarSaleServiceModel();
        carSaleServiceModel.setCar(carServiceModel1);
        carSaleServiceModel.setDiscount(25.52);
        carSaleServiceModel.setCustomer(customerServiceModel1);
    }

    @Test
    public void saleService_saleCarWithCorrectValues_ReturnsCorrect() {
        CarSaleServiceModel actual = saleService.saleCar(carSaleServiceModel);
        CarSaleServiceModel expected = this.modelMapper.map(this.carSaleRepository.findAll().get(0), CarSaleServiceModel.class);

        Assert.assertEquals(expected.getCar().getModel(), actual.getCar().getModel());
        Assert.assertEquals(expected.getDiscount(), actual.getDiscount());
        Assert.assertEquals(expected.getCustomer().getName(), actual.getCustomer().getName());

    }

    @Test(expected = Exception.class)
    public void saleService_saleCarWithNullValue_ThrowsException() {
        carSaleServiceModel.setDiscount(null);
        saleService.saleCar(carSaleServiceModel);
    }

    @Test
    public void saleService_salePartWithCorrectValues_ReturnsCorrect() {
        PartSaleServiceModel actual = saleService.salePart(partSaleServiceModel);
        PartSaleServiceModel expected = this.modelMapper.map(this.partSaleRepository.findAll().get(0), PartSaleServiceModel.class);

        Assert.assertEquals(expected.getPart().getPrice(), actual.getPart().getPrice());
        Assert.assertEquals(expected.getDiscount(), actual.getDiscount());
        Assert.assertEquals(expected.getCustomer().getName(), actual.getCustomer().getName());
        Assert.assertEquals(expected.getQuantity(), actual.getQuantity());

    }

    @Test(expected = Exception.class)
    public void saleService_salePartWithNullValue_ThrowsException() {
        partSaleServiceModel.setDiscount(null);
        saleService.salePart(partSaleServiceModel);
    }


}
