package org.kl.residentevil.domain.entities;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "viruses")
public class Virus extends BaseEntity {
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

    public Virus() {
    }

    @Column(name = "name", nullable = false)
    @NotEmpty
    @Size(min = 3, max = 10)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    @NotEmpty
    @Size(min = 5, max = 100)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "side_effects")
    @Size(max = 50)
    public String getSideEffects() {
        return this.sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "creator")
    public Creator getCreator() {
        return this.creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }
    @Column(name = "is_deadly")
    public boolean isDeadly() {
        return this.isDeadly;
    }

    public void setDeadly(boolean deadly) {
        isDeadly = deadly;
    }

    @Column(name = "is_curable")
    public boolean isCurable() {
        return this.isCurable;
    }

    public void setCurable(boolean curable) {
        isCurable = curable;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "mutation")
    public Mutation getMutation() {
        return this.mutation;
    }

    public void setMutation(Mutation mutation) {
        this.mutation = mutation;
    }

    @Min(0)
    @Max(100)
    @Column(name = "turnover_rate")
    public int getTurnoverRate() {
        return this.turnoverRate;
    }

    public void setTurnoverRate(int turnoverRate) {
        this.turnoverRate = turnoverRate;
    }

    @Min(1)
    @Max(12)
    @Column(name = "hours_until_turn")
    public int getHoursUntilTurn() {
        return this.hoursUntilTurn;
    }

    public void setHoursUntilTurn(int hoursUntilTurn) {
        this.hoursUntilTurn = hoursUntilTurn;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "magnitude")
    public Magnitude getMagnitude() {
        return this.magnitude;
    }

    public void setMagnitude(Magnitude magnitude) {
        this.magnitude = magnitude;
    }

    @Column(name = "released_on")
    public LocalDate getReleasedOn() {
        return this.releasedOn;
    }

    public void setReleasedOn(LocalDate releasedOn) {
        this.releasedOn = releasedOn;
    }

    @ManyToMany()
    @JoinTable(name = "viruses_capitals",
            joinColumns = @JoinColumn(name = "virus_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "capital_id", referencedColumnName = "id"))
    public List<Capital> getCapitals() {
        return this.capitals;
    }

    public void setCapitals(List<Capital> capitals) {
        this.capitals = capitals;
    }
}
