package com.chrislm.client.module_client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    public void addClient(Client client) {
        boolean exists = clientRepository.existsByEmail(client.getEmail());
        if(exists){
            throw new IllegalStateException("email taken");
        }
        clientRepository.save(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClient(Long id) throws InstanceNotFoundException {
        Client client = clientRepository.findById(id)
                .orElseThrow(InstanceNotFoundException::new);

        return client;
    }

    public Client getClientByEmail(String email) throws InstanceNotFoundException {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow( InstanceNotFoundException::new);
        return client;
    }

    public void updateClientDetails(Long id, String name, String email) throws InstanceNotFoundException {
        Client client = clientRepository.findById(id)
                .orElseThrow(InstanceNotFoundException::new);

        if(name != null && name.length() > 0){
            client.setName(name);
        }

        if(email != null && email.length() > 0 && !Objects.equals(email, client.getEmail())){
            Optional<Client> clientOptional = clientRepository.findByEmail(email);

            if(clientOptional.isPresent()){
                throw new IllegalStateException("email taken");
            }

            client.setEmail(email);
        }
    }


    public void deleteClient(Long id) throws InstanceNotFoundException {
        boolean exists = clientRepository.existsById(id);

        if(!exists){
            throw new InstanceNotFoundException("client with id: " + id + " cannot be found");
        }

        clientRepository.deleteById(id);
    }
}
