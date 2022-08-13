package com.chrislm.client.module_client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepositoryUnderTest;

    @AfterEach
    void tearDown() {
        clientRepositoryUnderTest.deleteAll();
    }

    @Test
    void itShouldCheckIfClientEmailExists() {
        //given
        String email = "shermim@gmail.com";
        Client client = new Client(
                1L,
                "shermim",
                email
        );

        clientRepositoryUnderTest.save(client);

        //when
        boolean expected = clientRepositoryUnderTest.existsByEmail(email);

        //then
        assertThat(expected).isTrue();
    }


    @Test
    void itShouldCheckIfClientEmailDoesNotExists() {
        //given
        String email = "shermim@gmail.com";

        //when
        boolean expected = clientRepositoryUnderTest.existsByEmail(email);

        //then
        assertThat(expected).isFalse();
    }


    @Test
    void itShouldFindClientByEmail() {
        //given
        String email = "shermim@gmail.com";

        Client client = new Client(
                1L,
                "shermim",
                email
        );


        clientRepositoryUnderTest.save(client);

        //when
        Client expectedClient = clientRepositoryUnderTest.findByEmail(email).orElseThrow();

        //then
        assertThat(expectedClient).isEqualTo(client);
    }


    @Test
    void itShouldNotFindClientByEmail() {
        //given
        String email = "shermim@gmail.com";
        String email1 = "who@gmail.com";

        Client client = new Client(
                1L,
                "shermim",
                email
        );

        Client client1 = new Client(
                2L,
                "who",
                email1
        );

        clientRepositoryUnderTest.save(client);

        //when
        Optional<Client> expectedClient = clientRepositoryUnderTest.findByEmail(email);

        //then
        assertThat(expectedClient).isNotEqualTo(client1);
    }
}