package ru.job4j.hibernate.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class HbmRunFetch {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Vacancy vacancy1 = Vacancy.of("Junior");
            Vacancy vacancy2 = Vacancy.of("Middle");
            Vacancy vacancy3 = Vacancy.of("Senior");

            VacancyStore store = new VacancyStore();
            store.addVacancy(vacancy1);
            store.addVacancy(vacancy2);
            store.addVacancy(vacancy3);
            session.save(store);

            Candidate candidate = Candidate.of("Ivanov", 1.0, 35000, store);
            session.save(candidate);
            Candidate candidateDB = session.createQuery(
                    "select distinct cd from Candidate cd "
                            + "join fetch cd.store s "
                            + "join fetch s.vacancies v "
                            + "where cd.id = :cId", Candidate.class
            ).setParameter("cId", 1).uniqueResult();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
