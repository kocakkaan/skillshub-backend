package com.reply.skillshub.controllers.contacts;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.reply.skillshub.openapi.api.ContactsApi;
import com.reply.skillshub.openapi.model.ContactDto;
import com.reply.skillshub.openapi.model.UpdateContactDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ContactsController implements ContactsApi {

  private final ContactsControllerService contactsControllerService;

  @Override
  public ResponseEntity<ContactDto> contactsContactIdGet(String contactId) {
    return ResponseEntity.ok(contactsControllerService.getContact(contactId));
  }

  @Override
  public ResponseEntity<ContactDto> contactsContactIdPut(String contactId,UpdateContactDto updateContactDto) {
    return ResponseEntity.ok(contactsControllerService.updateContact(contactId, updateContactDto));
  }

  @Override
  public ResponseEntity<List<ContactDto>> contactsGet(Optional<String> search, Optional<String> clientId,
      Optional<String> projectId) {
    return ResponseEntity.ok(contactsControllerService.getAllContacts(search, clientId, projectId));
  }

  @Override
  public ResponseEntity<ContactDto> contactsPost(UpdateContactDto updateContactDto) {
    return ResponseEntity.ok(contactsControllerService.createContact(updateContactDto));
  }

}
