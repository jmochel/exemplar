package org.saltations;

import javax.inject.Inject;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.StreamSupport.stream;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Confirms mapping of parents to and from the database
 */

@MicronautTest
@DisplayName("Parent Repo")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ParentRepoTest
{
    @Inject
    EmbeddedApplication<?> application;

    @Inject
    private ParentRepo parentRepo;

    private ParentSupply parentSupply = new ParentSupply();

    @Test
    @Order(2)
    @DisplayName("can insert and retrieve a parent")
    void canInsertAndRetrieveParent()
    {
        // Create example parent

        Parent original =  parentSupply.nextOne();

        // Insert and verify

        Parent inserted = parentRepo.save(original);
        assertNotNull(inserted,"Insertion");
        assertTrue(inserted.getId() != null, "Has ID");

        // Verify contents

        assertAll(
                () -> assertEquals(original.getAge(),inserted.getAge(),"Age"),
                () -> assertEquals(original.getFirstName(),inserted.getFirstName(),"FirstName"),
                () -> assertEquals(original.getLastName(),inserted.getLastName(),"LastName")
        );

        // Retrieve and verify

        Optional<Parent> option = parentRepo.findById(inserted.getId());
        assertTrue(option.isPresent(), "Retrieved");

        Parent retrieved = option.get();

        // Verify contents

        assertAll(
                () -> assertEquals(inserted.getAge(),retrieved.getAge(),"Age"),
                () -> assertEquals(inserted.getFirstName(),retrieved.getFirstName(),"FirstName"),
                () -> assertEquals(inserted.getLastName(),retrieved.getLastName(),"LastName")
        );
    }

    @Test
    @Order(4)
    @DisplayName("can update a parent")
    void canUpdateParent()
    {
        // Create example parent

        Parent original = parentSupply.nextOne();

        Parent inserted = parentRepo.save(original);

        // Update

        Parent update = inserted.withLastName("Cavares");

        Parent updated = parentRepo.update(update);
        assertNotNull(updated,"Update");

        // Verify contents

        assertAll(
                () -> assertEquals(update.getAge(),updated.getAge(),"Age"),
                () -> assertEquals(update.getFirstName(),updated.getFirstName(),"FirstName"),
                () -> assertEquals(update.getLastName(),updated.getLastName(),"LastName")
        );
    }

    @Test
    @Order(6)
    @DisplayName("can delete a parent")
    void canDeleteParent()
    {
        // Create example parent

        Parent original = parentSupply.nextOne();

        Long id = parentRepo.save(original).getId();

        // Delete

        parentRepo.deleteById(id);

        // Verify

        Optional<Parent> option = parentRepo.findById(id);
        assertTrue(option.isEmpty(), "Deleted");
    }

    @Test
    @Order(10)
    @DisplayName("can insert and retrieve multiple parents")
    void canInsertParents()
    {
        // Create example parent

        List<Parent> originals = parentSupply.next(5);

        // Insert and verify

        List<Parent> inserteds = stream(parentRepo.saveAll(originals).spliterator(), false)
                .collect(toList());
        assertFalse(inserteds.isEmpty(), "Insertion");
        assertEquals(originals.size(), inserteds.size(), "Num Inserted");

        // Verify inserted assuming they go in the order they were handed off to be saved...

        for (int i = 0; i < originals.size(); i++)
        {
            Parent original = originals.get(i);
            Parent inserted = inserteds.get(i);

            assertTrue(inserted.getId() != null, "Has ID");

            // Verify contents

            assertAll(
                    () -> assertEquals(original.getAge(),inserted.getAge(),"Age"),
                    () -> assertEquals(original.getFirstName(),inserted.getFirstName(),"FirstName"),
                    () -> assertEquals(original.getLastName(),inserted.getLastName(),"LastName")
            );

        }

        // Retrieve

        Set<Long> insertedsIds = inserteds.stream()
                .map(i -> i.getId())
                .collect(Collectors.toSet());

        List<Parent> retrieveds = stream(parentRepo.findByIdInList(insertedsIds).spliterator(), false)
                .collect(toList());

        // And verify

        assertEquals(insertedsIds.size(), retrieveds.size(), "Number of inserted and retrieved");

        Map<Long,Parent> insertedById = inserteds.stream().collect(toMap(i -> i.getId(), i -> i));

        Map<Long,Parent> retrievedById = retrieveds.stream().collect(toMap(i -> i.getId(), i -> i));

        for (Long id : insertedsIds )
        {
            Parent inserted = insertedById.get(id);
            Parent retrieved = retrievedById.get(id);

            // Verify contents

            assertAll(
                    () -> assertEquals(inserted.getAge(),retrieved.getAge(),"Age"),
                    () -> assertEquals(inserted.getFirstName(),retrieved.getFirstName(),"FirstName"),
                    () -> assertEquals(inserted.getLastName(),retrieved.getLastName(),"LastName")
            );
        }
    }

    /**
     * Supply single or multiple parents
     */

    static class ParentSupply extends SupplyFixture<Parent>
    {
        ParentSupply()
        {
            super(new TypeReference<List<Parent>>(){}, "[\n" +
                          "  {\"age\": 27, \"first_name\": \"Trula\", \"last_name\": \"Bischof\"}, \n" +
                          "  {\"age\": 38, \"first_name\": \"Scarface\", \"last_name\": \"Fairtlough\"}, \n" +
                          "  {\"age\": 97, \"first_name\": \"Marilee\", \"last_name\": \"Iscowitz\"}, \n" +
                          "  {\"age\": 48, \"first_name\": \"Marcelo\",  \"last_name\": \"De Launde\"}, \n" +
                          "  {\"age\": 43, \"first_name\": \"Barnett\", \"last_name\": \"MacFarlane\"}, \n" +
                          "  {\"age\": 30, \"first_name\": \"Dudley\", \"last_name\": \"Gricks\"}, \n" +
                          "  {\"age\": 34, \"first_name\": \"Radcliffe\", \"last_name\": \"Shooter\"}, \n" +
                          "  {\"age\": 45, \"first_name\": \"Minette\", \"last_name\": \"Gaylord\"}, \n" +
                          "  {\"age\": 138, \"first_name\": \"Joyan\", \"last_name\": \"Jedrasik\"}, \n" +
                          "  {\"age\": 63, \"first_name\": \"Norby\", \"last_name\": \"Mungham\"}\n" +
                          "]");
        }
    };
}
