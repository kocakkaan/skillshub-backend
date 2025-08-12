package com.reply.skillshub.data.contact;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContactService {

  private final ContactRepository contactRepository;
  private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ContactService.class);

  public Contact save(Contact contact) {
    logger.info("Saving contact: {}", contact.getName());
    var savedContact = contactRepository.save(contact);
    logger.info("Contact saved successfully: {}", savedContact.getName());
    return savedContact;
  }

  public Contact findById(String id) {
    return contactRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
  }

  public List<Contact> findAll() {
    return contactRepository.findAll();
  }

}
