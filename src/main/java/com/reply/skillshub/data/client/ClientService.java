package com.reply.skillshub.data.client;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientService {

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ClientService.class);

  private final ClientRepository clientRepository;


  public Client save(Client client) {
    logger.info("Saving client: {}", client.getName());
    client.setName(client.getName().trim());
    var potentialClient = clientRepository.findByNameIgnoreCase(client.getName());
    if (potentialClient.isEmpty()) {
      logger.info("Client does not exist, saving new client: {}", client.getName());
    } else {
      logger.info("Client already exists, returning existing client: {}", client.getName());
      return potentialClient.get(0);
    }
    var savedClient = clientRepository.save(client);
    logger.info("Client saved successfully: {}", savedClient.getName());
    return savedClient;
  }

  public Client findById(String id) {
    return clientRepository.findById(id).orElseThrow();
  }

  public List<Client> findAllClients() {
    return clientRepository.findAll();
  }


  
}
