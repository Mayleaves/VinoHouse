package com.VinoHouse.service;

import com.VinoHouse.entity.AddressBook;

import java.util.List;

public interface AddressBookService {

    List<AddressBook> list(AddressBook addressBook);

    void save(AddressBook addressBook);

    AddressBook getById(Long id);

    void update(AddressBook addressBook);

    void deleteById(Long id);

    void setDefault(AddressBook addressBook);

}
