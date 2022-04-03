package ru.job4j.hibernate.manytomany.unidirectional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.hibernate.manytomany.unidirectional.model.Author;
import ru.job4j.hibernate.manytomany.unidirectional.model.Book;

public class HbmRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Book dayWatch = Book.of("Дневной дозор");
            Book swords = Book.of("Клинки");
            Book nightWatch = Book.of("Ночной дозор");
            Book lastWatch = Book.of("Последний дозор");
            Book ghostV = Book.of("Ghost V");
            Book milkRun = Book.of("Milk Run");

            Author vasiliev = Author.of("Васильев");
            vasiliev.getBooks().add(dayWatch);
            vasiliev.getBooks().add(swords);

            Author lukianenko = Author.of("Луккьяненко");
            lukianenko.getBooks().add(dayWatch);
            lukianenko.getBooks().add(nightWatch);
            lukianenko.getBooks().add(lastWatch);

            Author shekli = Author.of("Шекли");
            shekli.getBooks().add(ghostV);
            shekli.getBooks().add(milkRun);

            session.persist(vasiliev);
            session.persist(lukianenko);
            session.persist(shekli);

            Author author = session.get(Author.class, 1);
            session.remove(author);
            session.getTransaction().commit();

            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
