package org.kl.residentevil.services;

import org.kl.residentevil.domain.models.binding.VirusAddBindingModel;
import org.kl.residentevil.domain.models.binding.VirusEditBindingModel;
import org.kl.residentevil.domain.models.service.VirusServiceModel;

import java.util.List;

public interface VirusService {
    boolean createVirus(VirusAddBindingModel virusAddBindingModel);

    List<VirusServiceModel> getAllViruses();

    boolean removeVirus(String id);

    VirusServiceModel findVirusById(String id);

    boolean editVirus(VirusEditBindingModel virusToEdit);
}
