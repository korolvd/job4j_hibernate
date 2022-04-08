package ru.job4j.hibernate.hql;

import javax.persistence.*;

@Entity
@Table(name = "candidate")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private double experience;
    private int salary;

    @OneToOne(fetch = FetchType.LAZY)
    private VacancyStore store;

    public Candidate() {
    }

    public static Candidate of(String name, double experience, int salary, VacancyStore store) {
        Candidate candidate = new Candidate();
        candidate.name = name;
        candidate.experience = experience;
        candidate.salary = salary;
        candidate.store = store;
        return candidate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public VacancyStore getStore() {
        return store;
    }

    public void setStore(VacancyStore store) {
        this.store = store;
    }
}
