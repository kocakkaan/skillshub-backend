package com.reply.skillshub.controllers.clients;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.openapi.api.ClientApi;
import com.reply.skillshub.openapi.model.ClientDto;
import com.reply.skillshub.openapi.model.CreateClientDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ClientController implements ClientApi {

  private final ClientControllerService clientControllerService;

  @Override
  public ResponseEntity<List<ClientDto>> clientsGet(Optional<String> search) {
    return ResponseEntity.ok(clientControllerService.getClients(search));
  }

  @Override
  public ResponseEntity<ClientDto> clientsPost(CreateClientDto createClientDto) {
    return ResponseEntity.ok(clientControllerService.createClient(createClientDto));
  }

}
