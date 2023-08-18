package com.example.oneToOneTest.services;

import com.example.oneToOneTest.dto.ClientDTO;
import com.example.oneToOneTest.entities.Address;
import com.example.oneToOneTest.entities.Client;
import com.example.oneToOneTest.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    public ClientDTO insert(ClientDTO dto){
        Client client = new Client();
        copyDtoToEntity(dto, client);
        client = clientRepository.save(client);

        return new ClientDTO(client);
    }

    private void copyDtoToEntity(ClientDTO dto, Client entity){
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());

        Address address = new Address();
        address.setStreet(dto.getAddress().getStreet());
        address.setNeighborhood(dto.getAddress().getNeighborhood());
        address.setCity(dto.getAddress().getCity());
        address.setNumber(dto.getAddress().getNumber());
        address.setState(dto.getAddress().getState());
        address.setZipCode(dto.getAddress().getZipCode());

        entity.setAddress(address);
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO dto){
        Client entity = clientRepository.getReferenceById(id);

        Address address = new Address();
        address.setId(entity.getAddress().getId());

        copyDtoToReferencedEntities(dto, entity, address);

        entity = clientRepository.save(entity);
        return new ClientDTO(entity);
    }

    private void copyDtoToReferencedEntities(ClientDTO dto, Client client, Address address){
        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());

        address.setStreet(dto.getAddress().getStreet());
        address.setNeighborhood(dto.getAddress().getNeighborhood());
        address.setCity(dto.getAddress().getCity());
        address.setNumber(dto.getAddress().getNumber());
        address.setState(dto.getAddress().getState());
        address.setZipCode(dto.getAddress().getZipCode());

        client.setAddress(address);
    }

    @Transactional(readOnly = true)
    public List<ClientDTO> findAll(){
        List<Client> clients = clientRepository.findAll();
        List<ClientDTO> dtos = clients.stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
        return dtos;
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id){
        Client client = clientRepository.findById(id).get();
        return new ClientDTO(client);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        clientRepository.deleteById(id);
    }
}
