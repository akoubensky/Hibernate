package org.example.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "COUNTRIES")
public class Country {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "NAME")
    @NotEmpty(message = "Country shouldn't be empty")
    private String name;
    @Column(name = "CAPITAL")
    private String capital;
    @Column(name = "POPULATION")
    private Double population;
    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    List<City> cities = new ArrayList<>();
}
