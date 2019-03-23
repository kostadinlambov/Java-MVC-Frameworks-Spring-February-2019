package org.kl.residentevil.domain.models.binding;

import org.kl.residentevil.domain.entities.Capital;
import org.kl.residentevil.domain.entities.Creator;
import org.kl.residentevil.domain.entities.Magnitude;
import org.kl.residentevil.domain.entities.Mutation;
import org.kl.residentevil.validations.ReleaseDateBefore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public class VirusEditBindingModel {
    private String id;
    private String name;
    private String description;
    private String sideEffects;
    private Creator creator;
    private boolean isDeadly;
    private boolean isCurable;
    private Mutation mutation;
    private int turnoverRate;
    private int hoursUntilTurn;
    private Magnitude magnitude;
    private LocalDate releasedOn;
    private List<Capital> capitals;

    public VirusEditBindingModel() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotEmpty(message = "Cannot be empty.")
    @Size(min = 3, max = 10,  message = "Should be between 3 and 10 symbols.")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty(message = "Cannot be empty.")
    @Size(min = 5, max = 100, message = "Should be between 5 and 100 symbols.")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Size(max = 50, message = "Should have a maximum of 50 symbols.")
    public String getSideEffects() {
        return this.sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }


    public Creator getCreator() {
        return this.creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }


    public boolean isDeadly() {
        return this.isDeadly;
    }

    public void setDeadly(boolean deadly) {
        isDeadly = deadly;
    }

    public boolean isCurable() {
        return this.isCurable;
    }

    public void setCurable(boolean curable) {
        isCurable = curable;
    }


    @NotNull(message = "Mutation cannot be null.")
    public Mutation getMutation() {
        return this.mutation;
    }

    public void setMutation(Mutation mutation) {
        this.mutation = mutation;
    }

    @Min(value = 0, message = "Should be a number between 0 and 100")
    @Max(value = 100, message = "Should be a number between 0 and 100")
    public int getTurnoverRate() {
        return this.turnoverRate;
    }

    public void setTurnoverRate(int turnoverRate) {
        this.turnoverRate = turnoverRate;
    }

    @Min(value = 1, message = "Should be a number between 1 and 12")
    @Max(value = 12,  message = "Should be a number between 1 and 12")
    public int getHoursUntilTurn() {
        return this.hoursUntilTurn;
    }

    public void setHoursUntilTurn(int hoursUntilTurn) {
        this.hoursUntilTurn = hoursUntilTurn;
    }

    public Magnitude getMagnitude() {
        return this.magnitude;
    }

    public void setMagnitude(Magnitude magnitude) {
        this.magnitude = magnitude;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ReleaseDateBefore()
    public LocalDate getReleasedOn() {
        return this.releasedOn;
    }

    public void setReleasedOn(LocalDate releasedOn) {
        this.releasedOn = releasedOn;
    }

    public List<Capital> getCapitals() {
        return this.capitals;
    }

    public void setCapitals(List<Capital> capitals) {
        this.capitals = capitals;
    }
}
