package ru.job4j.hibernate.lazyinit;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.hibernate.lazyinit.model.Brand;
import ru.job4j.hibernate.lazyinit.model.Model;

import java.util.ArrayList;
import java.util.List;

public class HbmRun {
    public static void main(String[] args) {
        List<Brand> list = new ArrayList<>();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Brand lada = Brand.of("Lada");
            lada.setId(1);
            session.save(lada);

            Model kalina = Model.of("Kalina", lada);
            session.save(kalina);
            Model granta = Model.of("Granta", lada);
            session.save(granta);
            Model priora = Model.of("Priora", lada);
            session.save(priora);
            Model xRay = Model.of("xRay", lada);
            session.save(xRay);
            Model vesta = Model.of("Vesta", lada);
            session.save(vesta);

            list = session.createQuery(
                    "select distinct b from Brand b join fetch b.models"
            ).list();
            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        for (Model model : list.get(0).getModels()) {
            System.out.println(model);
        }
    }
}
