package org.kl.residentevil.services;

import org.kl.residentevil.domain.entities.Capital;
import org.kl.residentevil.domain.entities.Virus;
import org.kl.residentevil.domain.models.binding.VirusAddBindingModel;
import org.kl.residentevil.domain.models.binding.VirusEditBindingModel;
import org.kl.residentevil.domain.models.service.VirusServiceModel;
import org.kl.residentevil.repositories.CapitalRepository;
import org.kl.residentevil.repositories.VirusRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VirusServiceImpl implements VirusService {

    private final VirusRepository virusRepository;
    private final CapitalRepository capitalRepository;
    private final ModelMapper modelMapper;

    public VirusServiceImpl(VirusRepository virusRepository, CapitalRepository capitalRepository, ModelMapper modelMapper) {
        this.virusRepository = virusRepository;
        this.capitalRepository = capitalRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean createVirus(VirusAddBindingModel virusAddBindingModel) {
        List<Capital> capitalList = virusAddBindingModel.getCapitals().stream().map(currentCapital -> {
            Capital capital = this.capitalRepository.findById(currentCapital.getId()).orElse(null);
            if (capital != null) {
                return capital;
            } else {
                throw new IllegalArgumentException("Capital does not exist!");
            }
        }).collect(Collectors.toList());


        VirusServiceModel virusServiceModel = this.modelMapper.map(virusAddBindingModel, VirusServiceModel.class);
        virusServiceModel.setCapitals(capitalList);

        Virus virus = this.modelMapper.map(virusServiceModel, Virus.class);

        return this.virusRepository.saveAndFlush(virus) != null;
    }

    @Override
    public List<VirusServiceModel> getAllViruses() {
        List<Virus> virusList = this.virusRepository.findAll();

        return virusList.stream()
                .map(virus -> this.modelMapper.map(virus, VirusServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public VirusServiceModel findVirusById(String id) {
        Virus virus = this.virusRepository.findById(id).orElse(null);
        if (virus != null) {
            return this.modelMapper.map(virus, VirusServiceModel.class);
        }

        throw new IllegalArgumentException("Virus does not exist!");
    }

    @Override
    public boolean editVirus(VirusEditBindingModel virusToEdit) {

        Virus virusById = this.virusRepository.findById(virusToEdit.getId()).orElse(null);

        if (virusById != null) {
            try {
                Virus virus = this.modelMapper.map(virusToEdit, Virus.class);
                return this.virusRepository.saveAndFlush(virus) != null;
            } catch (Exception ex) {
                throw new IllegalArgumentException("Something went wrong by editing of the virus");
            }
        }
        return false;
    }

    @Override
    public boolean removeVirus(String id) {
        Virus virus = this.virusRepository.findById(id).orElse(null);

        if (virus != null) {
            try {
                this.virusRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                throw new IllegalArgumentException("Something went wrong by deleting of the virus");
            }
        }
        return false;
    }
}
