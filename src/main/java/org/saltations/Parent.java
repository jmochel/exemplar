package org.saltations;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.extern.jackson.Jacksonized;


/**
 *  A Parent TODO describe scope and representation
 */

@Data
@With
@Jacksonized
@Builder(builderMethodName = "of", builderClassName = "Builder", buildMethodName = "done")
@MappedEntity("parent")
public class Parent
{
    @Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    @JsonProperty("id")
    @MappedProperty("id")
    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 40)
    @JsonProperty("first_name")
    @MappedProperty("first_name")
    private String firstName;

    @NotNull
    @NotBlank
    @Size(max = 60)
    @JsonProperty("last_name")
    @MappedProperty("last_name")
    private String lastName;

    @NotNull
    @Size(min = 0, max = 60)
    @lombok.Builder.Default
    @JsonProperty("age")
    @MappedProperty("age")
    private Integer age = 0;

}
