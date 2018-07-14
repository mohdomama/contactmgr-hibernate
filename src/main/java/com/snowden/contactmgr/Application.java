package com.snowden.contactmgr;

import com.snowden.contactmgr.model.Contact;
import com.snowden.contactmgr.model.Contact.ContactBuilder;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class Application {
    // Hold a reusable reference to a session factory
   private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        // Create a StandardServiceRegistory
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();


    }

    public static void main(String[] args) {
        Contact contact = new ContactBuilder("Mohd", "Omama")
                .withEmail("ammo@maildrop.cc")
                .withPhone(9012367951L)
                .build();

        save(contact);

        //Display the contacts with stream. New feature in java
        fetchAllContacts().stream().forEach(System.out::println);


    }

    @SuppressWarnings("unchecked")
    private static List<Contact> fetchAllContacts() {
        Session session = sessionFactory.openSession();

        // Create criteria object
        Criteria criteria = session.createCriteria(Contact.class);

        // get a list of contact object according to criteria object
        List<Contact> contacts = criteria.list();

        session.close();

        return contacts;
    }

    private static void save (Contact contact) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to save the contact
        session.save(contact);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();

    }
}
