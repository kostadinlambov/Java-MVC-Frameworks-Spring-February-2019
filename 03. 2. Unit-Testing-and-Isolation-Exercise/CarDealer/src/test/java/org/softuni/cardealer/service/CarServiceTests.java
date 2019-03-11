package org.softuni.cardealer.service;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.entities.Car;
import org.softuni.cardealer.domain.entities.Part;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.domain.models.service.CarServiceModel;
import org.softuni.cardealer.domain.models.service.PartServiceModel;
import org.softuni.cardealer.domain.models.service.SupplierServiceModel;
import org.softuni.cardealer.repository.CarRepository;
import org.softuni.cardealer.repository.PartRepository;
import org.softuni.cardealer.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@DataJpaTest()
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CarServiceTests {

    @Autowired
    private CarRepository carRepository;

    private ModelMapper modelMapper;
    private CarService carService;
    private CarServiceModel carServiceModel;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        carService = new CarServiceImpl(this.carRepository, this.modelMapper);
        this.createCarServiceModel();
    }

    private void createCarServiceModel() {
        carServiceModel = new CarServiceModel();
        carServiceModel.setMake("BMW");
        carServiceModel.setModel("M3");
        carServiceModel.setTravelledDistance(25L);
        carServiceModel.setParts(new ArrayList<>());
    }

    @Test
    public void carService_saveCarWithCorrectValues_ReturnsCorrect() {
        CarServiceModel actual = carService.saveCar(carServiceModel);
        CarServiceModel expected = this.modelMapper.map(this.carRepository.findAll().get(0), CarServiceModel.class);

        Assert.assertEquals(expected.getMake(), actual.getMake());
        Assert.assertEquals(expected.getModel(), actual.getModel());
        Assert.assertEquals(expected.getTravelledDistance(), actual.getTravelledDistance());
        Assert.assertEquals(expected.getParts().size(), actual.getParts().size());
    }

    @Test(expected = Exception.class)
    public void carService_saveCarWithNullValue_ThrowsException() {
        carServiceModel.setMake(null);
        carService.saveCar(carServiceModel);
    }

    @Test
    public void carService_editCarWithCorrectValues_ReturnsCorrect() {
        CarServiceModel carServiceModel = carService.saveCar(this.carServiceModel);

        CarServiceModel carServiceModelEdit = new CarServiceModel();
        carServiceModelEdit.setId(carServiceModel.getId());
        carServiceModelEdit.setMake("FORD");
        carServiceModelEdit.setModel("MUSTANG");
        carServiceModelEdit.setTravelledDistance(25L);
        carServiceModelEdit.setParts(new ArrayList<>());

        CarServiceModel actual = carService.editCar(carServiceModelEdit);
        CarServiceModel expected = this.modelMapper.map(this.carRepository.findAll().get(0), CarServiceModel.class);

        Assert.assertEquals(expected.getMake(), actual.getMake());
        Assert.assertEquals(expected.getModel(), actual.getModel());
        Assert.assertEquals(expected.getTravelledDistance(), actual.getTravelledDistance());
        Assert.assertEquals(expected.getParts().size(), actual.getParts().size());
    }

    @Test(expected = Exception.class)
    public void carService_editCarWithNullValues_ThrowsException() {
        CarServiceModel carServiceModel = carService.saveCar(this.carServiceModel);

        CarServiceModel carServiceModelEdit = new CarServiceModel();
        carServiceModelEdit.setId(carServiceModel.getId());
        carServiceModelEdit.setMake(null);
        carServiceModelEdit.setModel("MUSTANG");
        carServiceModelEdit.setTravelledDistance(25L);
        carServiceModelEdit.setParts(new ArrayList<>());

        carService.editCar(carServiceModelEdit);
    }

    @Test
    public void carService_deleteCarWithCorrectValues_ReturnsCorrect() {
        CarServiceModel carServiceModel = carService.saveCar(this.carServiceModel);

        CarServiceModel actual = carService.deleteCar(carServiceModel.getId());
        CarServiceModel expected = this.modelMapper.map(carServiceModel, CarServiceModel.class);

        Assert.assertEquals(expected.getMake(), actual.getMake());
        Assert.assertEquals(expected.getModel(), actual.getModel());
        Assert.assertEquals(expected.getTravelledDistance(), actual.getTravelledDistance());
        Assert.assertEquals(expected.getParts().size(), actual.getParts().size());
    }

    @Test(expected = Exception.class)
    public void carService_deleteCarWithInvalidId_ThrowsException() {
        carService.deleteCar("invalid_id");
    }

    @Test
    public void carService_findByIdCarWithCorrectValues_ReturnsCorrect() {
        CarServiceModel carServiceModel = carService.saveCar(this.carServiceModel);

        CarServiceModel actual = carService.findCarById(carServiceModel.getId());
        CarServiceModel expected = this.modelMapper.map(carServiceModel, CarServiceModel.class);

        Assert.assertEquals(expected.getMake(), actual.getMake());
        Assert.assertEquals(expected.getModel(), actual.getModel());
        Assert.assertEquals(expected.getTravelledDistance(), actual.getTravelledDistance());
        Assert.assertEquals(expected.getParts().size(), actual.getParts().size());
    }

    @Test(expected = Exception.class)
    public void carService_findByIdCarWithInvalidId_ThrowsException() {
        carService.findCarById("invalid_id");
    }
}
