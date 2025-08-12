package com.reply.skillshub.controllers.contacts;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.reply.skillshub.data.client.ClientService;
import com.reply.skillshub.data.contact.Contact;
import com.reply.skillshub.data.contact.ContactService;
import com.reply.skillshub.openapi.model.ClientDto;
import com.reply.skillshub.openapi.model.ContactDto;
import com.reply.skillshub.openapi.model.UpdateContactDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactsControllerService {

  private final ContactService contactService;
  private final ClientService clientService;

  public ContactDto getContact(String contactId) {
    return convertToContactDto(contactService.findById(contactId));
  }

  public ContactDto updateContact(String contactId, UpdateContactDto updateContactDto) {
    Contact contact = contactService.findById(contactId);
    contact = updateContactFromDto(contact, updateContactDto);
    contactService.save(contact);
    return convertToContactDto(contact);
  }

  public List<ContactDto> getAllContacts(Optional<String> search, Optional<String> clientId,
      Optional<String> projectId) {
    if (search.isPresent()) {
      throw new UnsupportedOperationException("Unimplemented method 'getAllContacts' with search parameter");
    }
    if (clientId.isPresent()) {
      var client = clientService.findById(clientId.get());
      return convertToContactDtos(client.getContacts());
    }
    if (projectId.isPresent()) {
      throw new UnsupportedOperationException("Unimplemented method 'getAllContacts' with projectId parameter");
    }
    return convertToContactDtos(contactService.findAll());
    
  }

  public ContactDto createContact(UpdateContactDto updateContactDto) {
    Contact contact = createContactFromDto(updateContactDto);
    var savedContact = contactService.save(contact);
    return convertToContactDto(savedContact);
  }

  private Contact updateContactFromDto(Contact contact, UpdateContactDto updateContactDto) {
    contact.setName(updateContactDto.getName());
    contact.setEmail(updateContactDto.getEmail().orElse(null));
    contact.setPhoneNumber(updateContactDto.getPhone().orElse(null));
    return contact;
  }

  private Contact createContactFromDto(UpdateContactDto updateContactDto) {
    var contact = new Contact();
    contact.setName(updateContactDto.getName());
    contact.setEmail(updateContactDto.getEmail().orElse(null));
    contact.setPhoneNumber(updateContactDto.getPhone().orElse(null));
    contact.setCompany(clientService.findById(updateContactDto.getCompany().getId()));
    return contact;
  }

  private List<ContactDto> convertToContactDtos(List<Contact> contacts) {
    return contacts.stream()
        .map(this::convertToContactDto)
        .toList();
  }

  private ContactDto convertToContactDto(Contact contact) {
    var contactDto = new ContactDto();
    contactDto.setId(contact.getId());
    contactDto.setName(contact.getName());
    contactDto.setEmail(Optional.ofNullable(contact.getEmail()));
    contactDto.setPhone(Optional.ofNullable(contact.getPhoneNumber()));
    var client = contact.getCompany();
    if (client != null) {
      var clientDto = new ClientDto();
      clientDto.setId(client.getId());
      clientDto.setName(client.getName());
      contactDto.setCompany(clientDto);
    }
    ;
    return contactDto;
  }

}
