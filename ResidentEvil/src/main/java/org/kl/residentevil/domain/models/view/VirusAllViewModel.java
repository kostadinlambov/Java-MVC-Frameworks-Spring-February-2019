package org.kl.residentevil.domain.models.view;

import org.kl.residentevil.domain.entities.Magnitude;

import java.time.LocalDate;

public class VirusAllViewModel {
    private String id;
    private String name;
    private Magnitude magnitude;
    private LocalDate releasedOn;

    public VirusAllViewModel() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Magnitude getMagnitude() {
        return this.magnitude;
    }

    public void setMagnitude(Magnitude magnitude) {
        this.magnitude = magnitude;
    }

    public LocalDate getReleasedOn() {
        return this.releasedOn;
    }

    public void setReleasedOn(LocalDate releasedOn) {
        this.releasedOn = releasedOn;
    }
}
