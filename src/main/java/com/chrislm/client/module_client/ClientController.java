package com.chrislm.client.module_client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import java.util.List;

@RestController
@RequestMapping("api/v1/client")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity addClient(@RequestBody Client client){
        clientService.addClient(client);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Client>> getAllClients(){
        return new ResponseEntity<>(clientService.getAllClients(), HttpStatus.OK);
    }

    @GetMapping(path = "{clientId}")
    public ResponseEntity<Client> getClient(@PathVariable("clientId") Long id) throws InstanceNotFoundException {
        return new ResponseEntity<>(clientService.getClient(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Client> getClientByEmail(@RequestParam String email) throws InstanceNotFoundException {
        return new ResponseEntity<>(clientService.getClientByEmail(email), HttpStatus.FOUND);
    }

    @PutMapping(path = "{clientId}")
    public ResponseEntity updateClientDetails(@PathVariable("clientId") Long id,
                                              @RequestParam String name,
                                              @RequestParam String email)
            throws InstanceNotFoundException {
        clientService.updateClientDetails(id, name, email);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(path = "{clientId}")
    public ResponseEntity deleteClient(@PathVariable("clientId") Long id) throws InstanceNotFoundException {
        clientService.deleteClient(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
