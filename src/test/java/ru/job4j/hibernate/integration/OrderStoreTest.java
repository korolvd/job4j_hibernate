package ru.job4j.hibernate.integration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.core.Is.is;

public class OrderStoreTest {
    private BasicDataSource pool = new BasicDataSource();

    @Before
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("./db/update_001.sql"))
        )) {
            br.lines().forEach(l -> builder.append(l).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @After
    public void dropTable() throws SQLException{
        pool.getConnection().prepareStatement("DROP TABLE orders").executeUpdate();
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        OrderStore store = new OrderStore(pool);
        store.save(Order.of("name1", "description1"));
        List<Order> all = (List<Order>) store.findAll();
        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenSaveOrderAndUpdate() {
        OrderStore store = new OrderStore(pool);
        store.save(Order.of("name1", "description1"));
        Order updateOrder = Order.of("updateName", "updateDesc");
        assertTrue(store.update(1, updateOrder));
        Order rsl = store.findById(1);
        assertThat(rsl.getName(), is(updateOrder.getName()));
        assertThat(rsl.getDescription(), is(updateOrder.getDescription()));
    }

    @Test
    public void whenSaveAndFindById() {
        OrderStore store = new OrderStore(pool);
        Order order = Order.of("name1", "description1");
        store.save(order);
        Order rsl = store.findById(order.getId());
        assertThat(rsl.getName(), is(order.getName()));
        assertThat(rsl.getDescription(), is(order.getDescription()));
    }

    @Test
    public void whenSaveTwoOrdersWithEqualsNameAndFindTwoRowsByName(){
        OrderStore store = new OrderStore(pool);
        store.save(Order.of("sameName", "description1"));
        store.save(Order.of("sameName", "description2"));
        List<Order> rsl = (List<Order>) store.findByName("sameName");
        assertThat(rsl.size(), is(2));
        assertThat(rsl.get(0).getName(), is(rsl.get(1).getName()));
        assertThat(rsl.get(0).getDescription(), is("description1"));
        assertThat(rsl.get(1).getDescription(), is("description2"));
    }

}