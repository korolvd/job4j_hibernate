package ru.job4j.hibernate.tomany;

import ru.job4j.hibernate.tomany.model.Brand;
import ru.job4j.hibernate.tomany.model.Model;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Model kalina = Model.of("Kalina");
            session.save(kalina);
            Model granta = Model.of("Granta");
            session.save(granta);
            Model priora = Model.of("Priora");
            session.save(priora);
            Model xRay = Model.of("xRay");
            session.save(xRay);
            Model vesta = Model.of("Vesta");
            session.save(vesta);

            Brand lada = Brand.of("Lada");
            lada.addModel(session.load(Model.class, 1));
            session.save(lada);

            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
