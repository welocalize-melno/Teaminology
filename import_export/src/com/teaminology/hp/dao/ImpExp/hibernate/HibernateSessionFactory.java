package com.teaminology.hp.dao.ImpExp.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory
{

    private static SessionFactory sessionFactory;

    private HibernateSessionFactory() {

    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        return sessionFactory;
    }
}
