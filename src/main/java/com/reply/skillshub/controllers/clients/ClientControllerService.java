package com.reply.skillshub.controllers.clients;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.reply.skillshub.data.client.Client;
import com.reply.skillshub.data.client.ClientService;
import com.reply.skillshub.openapi.model.ClientDto;
import com.reply.skillshub.openapi.model.CreateClientDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientControllerService {

  private final ClientService clientService;

  public List<ClientDto> getClients(Optional<String> search) {
    var clients = clientService.findAllClients();
    if (clients.isEmpty()) {
      return List.of();
    }

    var clientDtos = clients.stream()
        .map(ClientControllerService::fromClient)
        .collect(Collectors.toList());

    return clientDtos;
  }

  public ClientDto createClient(CreateClientDto clientDto) {
    var client = new Client();
    client.setName(clientDto.getName());
    var savedClient = clientService.save(client);
    return fromClient(savedClient);
  }

  private static ClientDto fromClient(Client client) {
    return new ClientDto(client.getId(), client.getName());
  }

}
