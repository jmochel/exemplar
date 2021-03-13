package org.saltations;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import javax.inject.Inject;

@MicronautTest
class ExemplarTest
{
    @Inject
    EmbeddedApplication<?> application;

    @Inject
    private ParentRepo parentRepo;

    @Test
    void testItWorks() {
        Assertions.assertTrue(application.isRunning());
    }

}
