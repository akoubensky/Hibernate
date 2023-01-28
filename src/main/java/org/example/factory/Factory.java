package org.example.factory;

import org.example.model.City;
import org.example.model.Country;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Factory {
    public static final Factory INSTANCE = new Factory();

    private SessionFactory sessionFactory = null;

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration config = new Configuration();
            config.addAnnotatedClass(Country.class);
            config.addAnnotatedClass(City.class);
            sessionFactory = config.buildSessionFactory();
        }
        return sessionFactory;
    }

    public Session getSession() {
        return getSessionFactory().getCurrentSession();
    }

    private Factory() {}
}
