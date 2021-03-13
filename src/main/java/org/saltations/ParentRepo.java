package org.saltations;

import javax.validation.constraints.NotNull;

import java.util.Collection;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.H2)
public interface ParentRepo extends CrudRepository<Parent, Long>
{
    /**
     * Gets the parents matching the list of ids.
     *
     * @param ids the ids we are searching for, non-null
     *
     * @return the iterable of the found parents
     */

    Iterable<Parent> findByIdInList(@NotNull Collection<Long> ids);
}
