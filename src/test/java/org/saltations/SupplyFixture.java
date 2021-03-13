package org.saltations;

import javax.validation.constraints.NotNull;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A Supply of single or multiple valid entities of type T
 *
 * @param <T>  the type of class being supplied
 */

public abstract class  SupplyFixture<T>
{
    private final List<T> all;

    private int next = 0;

    /**
     * Constructor
     *
     * @param ref  the jackson type reference telling us the generic list class we are supplying from, non null
     * @param raw  the string of json we populate our supply from, non null
     */

    protected SupplyFixture(@NotNull TypeReference<List<T>> ref, @NotNull String raw)
    {
        try
        {
            ObjectMapper jsonMapper=new ObjectMapper();

            all=jsonMapper.readValue(raw, ref);
        }
        catch (Exception e)
        {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Get next available member of the supply
     *
     * @return the next available T or throw exception if no more are available
     */

    public T nextOne()
    {
        return all.get(next++);
    }

    /**
     * Get the next n available memebrs of the supply
     *
     * @param n the number of requested members of the supply
     */

    public List<T> next(int n)
    {
        int start = next;
        int end = next + n;

        next+=n;

        return all.subList(start,end);
    }

}
