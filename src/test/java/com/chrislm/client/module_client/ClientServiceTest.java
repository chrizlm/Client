package com.chrislm.client.module_client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock private ClientRepository clientRepository;
    private ClientService clientServiceUnderTest;

    @BeforeEach
    void setUp() {
        clientServiceUnderTest = new ClientService(clientRepository);
    }



    @Test
    void getAllClients() {
        //given
        //when
        clientServiceUnderTest.getAllClients();
        //then
        verify(clientRepository).findAll();
    }

    @Test
    void canAddClient() {
        //given
        String email = "shermim@gmail.com";
        Client client = new Client(
                1L,
                "shermim",
                email
        );
        //when
        clientServiceUnderTest.addClient(client);

        //then
        ArgumentCaptor<Client> clientArgumentCaptor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(clientArgumentCaptor.capture());

        Client capturedClient = clientArgumentCaptor.getValue();

        assertThat(capturedClient).isEqualTo(client);
    }


    @Test
    void willThrowWhenEmailIsTaken(){
        //given
        String email = "shermim@gmail.com";
        Client client = new Client(
                1L,
                "shermim",
                email
        );

        //given from mokito
        given(clientRepository.existsByEmail(anyString()))
                .willReturn(true);
        //when
        //then

        assertThatThrownBy(() ->clientServiceUnderTest.addClient(client))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("email taken");

        verify(clientRepository, never()).save(any());

    }

    @Test
    void canGetClient() throws InstanceNotFoundException {
        //given
        String email = "shermim@gmail.com";
        Long id = 1L;
        Client client = new Client(
                id,
                "shermim",
                email
        );

        //clientServiceUnderTest.addClient(client);
        given(clientRepository.findById(id)).willReturn(Optional.of(client));
        //when
        Client expected = clientServiceUnderTest.getClient(id);
        //then
        assertThat(expected).isEqualTo(client);

       /* ArgumentCaptor<Long> clientArgumentCapture = ArgumentCaptor.forClass(Long.class);
        verify(clientRepository).findById(clientArgumentCapture.capture());
        Long capturedId = clientArgumentCapture.getValue();

        assertThat(capturedId).isEqualTo(1L);


        assertThatThrownBy(() -> clientServiceUnderTest.getClient(2L))
                .isInstanceOf(InstanceNotFoundException.class);*/
    }

    @Test
    void canGetClientByEmail() throws InstanceNotFoundException {
        //given
        String email = "shermim@gmail.com";
        Long id = 1L;
        Client client = new Client(
                id,
                "shermim",
                email
        );
        given(clientRepository.findByEmail(email)).willReturn(Optional.of(client));
        //when
        Client expectedClientByEmail = clientServiceUnderTest.getClientByEmail(email);
        //then
        assertThat(expectedClientByEmail).isEqualTo(client);
    }

    @Test
    void willThrowExceptionUpdateClientEmailTaken() {
        //given
        String email = "shermim@gmail.com";
        String name = "shermim";
        String newName = "matunda";
        Long id = 1L;
        Long id2 = 2L;
        String name2 = "who";
        String email2 = "who@gmail.com";
        Client client = new Client(
                id,
                name,
                email
        );
        Client client2 = new Client(
                id2,
                name2,
                email2
        );

        given(clientRepository.findById(id)).willReturn(Optional.of(client));
        given(clientRepository.findByEmail(email2)).willReturn(Optional.of(client2));

        //when
        //then
        assertThatThrownBy(() -> clientServiceUnderTest.updateClientDetails(id, newName, email2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("email taken");

    }

    @Test
    void canUpdateClientDetails() throws InstanceNotFoundException {
        //given
        String email = "shermim@gmail.com";
        String newEmail = "matunda@gmail.com";
        String name = "shermim";
        String newName = "matunda";
        Long id = 1L;
        Client client = new Client(
                id,
                name,
                email
        );

        given(clientRepository.findById(id)).willReturn(Optional.of(client));

        //when
        clientServiceUnderTest.updateClientDetails(id, newName, newEmail);
        //then
        Optional<Client> expectedClientById = clientRepository.findById(id);
        String expectedName = expectedClientById.get().getName();
        String expectedEmail = expectedClientById.get().getEmail();
        assertThat(expectedName).isEqualTo(newName);
        assertThat(expectedEmail).isEqualTo(newEmail);
        assertThat(expectedClientById).isNotEqualTo(client);
    }


    @Test
    void willThrownEmailNotFoundExceptionForDeleteClientOperation() {
        //given
        String email = "shermim@gmail.com";
        String name = "shermim";
        Long id = 1L;
        Client client = new Client(
                id,
                name,
                email
        );

        given(clientRepository.existsById(id)).willReturn(false);

        //when
        //then

        assertThatThrownBy(()-> clientServiceUnderTest.deleteClient(id))
                .isInstanceOf(InstanceNotFoundException.class)
                .hasMessageContaining("client with id: " + id + " cannot be found");

        verify(clientRepository, never()).deleteById(id);

    }

}