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

        int id = save(contact);

        //Display the contacts with stream. New feature in java. (Before update)
        System.out.println("\n\nBefore Update: \n");
        fetchAllContacts().stream().forEach(System.out::println);

        // Get the persisted contact
        Contact c = getContactById(id);

        // Update contact
        c.setFirstName("Awesome");

        // Persist changes
        System.out.println("\nUpdating ... ");
        updateContact(c);

        // Display list after updates
        System.out.println("\n\nAfter Update: \n");
        fetchAllContacts().stream().forEach(System.out::println);

        System.out.println("\nDeleting ... ");
        deleteContactById(id);


        System.out.println("\n\nAfter Delete: \n");
        fetchAllContacts().stream().forEach(System.out::println);




    }

    private static Contact getContactById(int id) {
        Session session = sessionFactory.openSession();

        Contact contact = session.get(Contact.class, id);

        session.close();

        return contact;
    }

    private static void deleteContactById(int id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.delete(session.get(Contact.class, id));

        session.getTransaction().commit();


        session.close();
    }
    private static void updateContact(Contact contact) {
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        session.update(contact);

        session.getTransaction().commit();

        session.close();
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

    private static int save (Contact contact) {
        // Open a session
        Session session = sessionFactory.openSession();

        // Begin a transaction
        session.beginTransaction();

        // Use the session to save the contact
        int id = (int) session.save(contact);

        // Commit the transaction
        session.getTransaction().commit();

        // Close the session
        session.close();

        return id;
    }
}
