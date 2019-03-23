package org.kl.residentevil.services;

import org.kl.residentevil.domain.models.service.CapitalServiceModel;

import java.util.List;

public interface CapitalService {

    List<CapitalServiceModel> findAllCapitals();
}
