package ru.job4j.hibernate.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Candidate one = Candidate.of("Ivanov", 1.5, 120000);
            Candidate two = Candidate.of("Petrov", 0.5, 50000);
            Candidate three = Candidate.of("Sidorov", 1.0, 90000);

            session.save(one);
            session.save(two);
            session.save(three);

            List listAllCandidates = session.createQuery("from Candidate").list();
            Candidate candidate = (Candidate) session.createQuery("from Candidate where id = 1").uniqueResult();
            List listCandidatesByName = session.createQuery("from Candidate where name = 'Ivanov'").list();
            session.createQuery("update Candidate c " +
                            "set c.name = :newName, experience = :newExp, c.salary = :newSalary where c.id = :id")
                    .setParameter("newName", "Ivanenko")
                    .setParameter("newExp", 2.0)
                    .setParameter("newSalary", 200000)
                    .setParameter("id", 1)
                    .executeUpdate();
            session.createQuery("delete from Candidate where id = :id")
                    .setParameter("id", 2)
                    .executeUpdate();

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
