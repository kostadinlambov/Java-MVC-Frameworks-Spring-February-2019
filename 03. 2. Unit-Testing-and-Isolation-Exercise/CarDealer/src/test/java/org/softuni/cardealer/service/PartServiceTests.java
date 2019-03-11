package org.softuni.cardealer.service;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.models.service.CustomerServiceModel;
import org.softuni.cardealer.domain.models.service.PartServiceModel;
import org.softuni.cardealer.domain.models.service.SupplierServiceModel;
import org.softuni.cardealer.repository.CustomerRepository;
import org.softuni.cardealer.repository.PartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@DataJpaTest()
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PartServiceTests {

    @Autowired
    private PartRepository partRepository;

    private ModelMapper modelMapper;
    private PartService partService;
    private PartServiceModel partServiceModel;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        partService = new PartServiceImpl(this.partRepository, this.modelMapper);
        this.createPartServiceModel();
    }

    private void createPartServiceModel(){
        partServiceModel = new PartServiceModel();
        partServiceModel.setName("pesho");
        partServiceModel.setPrice(BigDecimal.TEN);
        partServiceModel.setSupplier(null);
    }

    @Test
    public void partService_savePartWithCorrectValues_ReturnsCorrect() {
        PartServiceModel actual = partService.savePart(partServiceModel);
        PartServiceModel expected = this.modelMapper.map(this.partRepository.findAll().get(0), PartServiceModel.class);

        Assert.assertEquals(expected.getPrice(), actual.getPrice());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getSupplier(), actual.getSupplier());
    }

    @Test(expected = Exception.class)
    public void partService_savePartWithNullValue_ThrowsException() {
        partServiceModel.setName(null);
        partService.savePart(partServiceModel);
    }

    @Test
    public void partService_editPartWithCorrectValues_ReturnsCorrect() {
        PartServiceModel partServiceModel = partService.savePart(this.partServiceModel);

        PartServiceModel partServiceModelEdit = new PartServiceModel();
        partServiceModelEdit.setId(partServiceModel.getId());
        partServiceModelEdit.setName("gosho");
        partServiceModelEdit.setPrice(BigDecimal.ONE);

        PartServiceModel actual = partService.editPart(partServiceModelEdit);
        PartServiceModel expected = this.modelMapper.map(this.partRepository.findAll().get(0), PartServiceModel.class);

        Assert.assertEquals(expected.getPrice(), actual.getPrice());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getSupplier(), actual.getSupplier());
    }

    @Test(expected = Exception.class)
    public void partService_editPartWithNullValues_ThrowsException() {
        PartServiceModel partServiceModel = partService.savePart(this.partServiceModel);

        PartServiceModel partServiceModelEdit = new PartServiceModel();
        partServiceModelEdit.setId(partServiceModel.getId());
        partServiceModelEdit.setName(null);
        partServiceModelEdit.setPrice(BigDecimal.ONE);

        partService.editPart(partServiceModelEdit);
    }

    @Test
    public void partService_deletePartWithCorrectValues_ReturnsCorrect() {
        PartServiceModel partServiceModel = partService.savePart(this.partServiceModel);

        PartServiceModel actual = partService.deletePart(partServiceModel.getId());

        Assert.assertEquals(partServiceModel.getPrice(), actual.getPrice());
        Assert.assertEquals(partServiceModel.getName(), actual.getName());
        Assert.assertEquals(partServiceModel.getSupplier(), actual.getSupplier());
    }

    @Test(expected = Exception.class)
    public void partService_deletePartWithInvalidId_ThrowsException() {
        partService.deletePart("invalid_id");
    }

    @Test
    public void partService_findByIdPartWithCorrectValues_ReturnsCorrect() {
        PartServiceModel partServiceModel = partService.savePart(this.partServiceModel);

        PartServiceModel actual = partService.findPartById(partServiceModel.getId());
        PartServiceModel expected = this.modelMapper.map(this.partRepository.findAll().get(0), PartServiceModel.class);

        Assert.assertEquals(expected.getPrice(), actual.getPrice());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getSupplier(), actual.getSupplier());
    }

    @Test(expected = Exception.class)
    public void partService_findByIdPartWithInvalidId_ThrowsException() {
        partService.findPartById("invalid_id");

    }
}
