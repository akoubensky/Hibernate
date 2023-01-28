package org.example;

import org.example.factory.Factory;
import org.example.model.City;
import org.example.model.Country;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    private static Country getCountryById(int countryId) {
        try (Session session = Factory.INSTANCE.getSession()) {
            Transaction tr = session.beginTransaction();
            Country country = session.get(Country.class, countryId);
            tr.commit();
            return country;
        }
    }

    private static City addCity(Country country, String name, double population) {
        try (Session session = Factory.INSTANCE.getSession()) {
            Transaction tr = session.beginTransaction();
            City city = City.builder().name(name).population(population).country(country).build();
            session.persist(city);
            country.getCities().add(city);
            tr.commit();
            return city;
        }
    }

    private static Country addCountry(String name, String capital, double population) {
        try (Session session = Factory.INSTANCE.getSession()) {
            Transaction tr = session.beginTransaction();
            Country country = Country.builder()
                    .name(name).capital(capital).population(population).cities(new ArrayList<>())
                    .build();
            session.persist(country);
            tr.commit();
            return country;
        }
    }

    private static Country getCountryByName(String name) {
        try (Session session = Factory.INSTANCE.getSession()) {
            Transaction tr = session.beginTransaction();
            Country country = session.createQuery("From Country Where name = :name", Country.class)
                    .setParameter("name", name)
                    .getSingleResult();
            tr.commit();
            return country;
        }
    }

    private static List<Country> getBigCountries(double minimum) {
        try (Session session = Factory.INSTANCE.getSession()) {
            Transaction tr = session.beginTransaction();
            List<Country> country = session.createQuery("From Country Where population >= :min", Country.class)
                    .setParameter("min", minimum)
                    .getResultList();
            tr.commit();
            return country;
        }
    }

    private static void removeCityByName(Country country, String cityName) {
        try (Session session = Factory.INSTANCE.getSession()) {
            Transaction tr = session.beginTransaction();
            Optional<City> cityFound = country.getCities().stream()
                    .filter(city -> cityName.equalsIgnoreCase(city.getName()))
                    .findFirst();
            if (cityFound.isEmpty()) return;
            City foundCity = cityFound.get();
            country.getCities().remove(foundCity);
            session.remove(foundCity);
            tr.commit();
        }
    }

    private static void removeCountry(Country country) {
        try (Session session = Factory.INSTANCE.getSession()) {
            Transaction tr = session.beginTransaction();
            session.remove(country);
            tr.commit();
        }
    }

    public static void main(String[] args) {
        Country russia = getCountryById(14);
        System.out.println(russia);

        Country hungary = addCountry("Hungary", "Budapest", 9.7);
        System.out.println(hungary);

        City spb = addCity(russia, "Saint Petersburg", 5.6);
        City nvsb = addCity(russia, "Novosibirsk", 1.5);
        System.out.println(russia);
        russia = getCountryById(6);
        System.out.println(russia);

        City debrecen = addCity(hungary, "Debrecen", 0.2);

        Country china = getCountryByName("China");
        System.out.println(china);

        System.out.println(getBigCountries(1000));

        removeCityByName(russia, "Saint Petersburg");
        System.out.println(russia);

        removeCountry(hungary);
    }
}
