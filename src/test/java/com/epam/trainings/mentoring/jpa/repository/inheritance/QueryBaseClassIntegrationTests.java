package com.epam.trainings.mentoring.jpa.repository.inheritance;

import com.epam.trainings.mentoring.jpa.domain.inheritance.mappedsuperclass.Burger;
import com.epam.trainings.mentoring.jpa.domain.inheritance.tableperclass.Sandwich;
import com.epam.trainings.mentoring.jpa.repository.AbstractIntegrationTests;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.generator.ValueGenerators;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class QueryBaseClassIntegrationTests extends AbstractIntegrationTests {
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void checkingQueringBurgersFromMappedSuperClass() {
        //GIVEN
        Operation operation = sequenceOf(
                insertInto("CHEESE_BURGER")
                        .withGeneratedValue("ID", ValueGenerators.sequence())
                        .columns("BUN", "BEEF", "CHEESE")
                        .values(0.5, 0.8, 0.6)
                        .values(0.4, 0.3, 0.7)
                        .build(),
                insertInto("CHICKEN_BURGER")
                        .withGeneratedValue("ID", ValueGenerators.sequence())
                        .columns("FRENCH_BUN", "CHICKEN")
                        .values(0.7, 0.3)
                        .values(0.4, 0.2)
                        .build()
        );
        new DbSetup(destination, operation).launch();
        //WHEN
        List<Burger> burgers = entityManager.createQuery("SELECT b from Burger b", Burger.class).getResultList();
        //THEN
        assertThat(burgers).hasSize(4);
    }

    @Test
    public void checkingQueringSandwichesFromTablePerClassInheritance() {
        //GIVEN
        Operation operation = sequenceOf(
                insertInto("CLASSIC_SANDWICH")
                        .withGeneratedValue("ID", ValueGenerators.sequence())
                        .columns("BREAD", "CHEESE", "SAUSAGE")
                        .values(0.2, 0.5, 0.4)
                        .values(0.2, 0.6, 0.4)
                        .build(),
                insertInto("HAWAII_SANDWICH")
                        .withGeneratedValue("ID", ValueGenerators.sequence())
                        .columns("BREAD", "PINE_APPLE")
                        .values(0.2, 0.3)
                        .build()
        );
        new DbSetup(destination, operation).launch();
        //WHEN
        List<Sandwich> sandwiches = entityManager.createQuery("SELECT s from Sandwich s", Sandwich.class).getResultList();
        //THEN
        assertThat(sandwiches).hasSize(3);
    }
}
