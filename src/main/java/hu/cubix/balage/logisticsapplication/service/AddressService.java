package hu.cubix.balage.logisticsapplication.service;

import hu.cubix.balage.logisticsapplication.dto.AddressInnerDto;
import hu.cubix.balage.logisticsapplication.exception.AddressNotFoundException;
import hu.cubix.balage.logisticsapplication.mapper.AddressMapper;
import hu.cubix.balage.logisticsapplication.model.Address;
import hu.cubix.balage.logisticsapplication.repository.AddressRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AddressService(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    @Transactional
    public AddressInnerDto create(AddressInnerDto addressInnerDto) {
        Address address = addressMapper.innerDtoToAddress(addressInnerDto);
        return addressMapper.addressToInnerDto(addressRepository.save(address));
    }

    @Transactional
    public AddressInnerDto modify(long id, AddressInnerDto addressInnerDto) {
        Address address = addressRepository.findById(id).orElseThrow(AddressNotFoundException::new);
        address.setCountry(addressInnerDto.getCountry());
        address.setCity(addressInnerDto.getCity());
        address.setStreet(addressInnerDto.getStreet());
        address.setZipCode(addressInnerDto.getZipCode());
        address.setHouseNumber(addressInnerDto.getHouseNumber());
        address.setLatitude(addressInnerDto.getLatitude());
        address.setLongitude(addressInnerDto.getLongitude());
        return addressMapper.addressToInnerDto(addressRepository.save(address));
    }

    @Transactional
    public void delete(long id) {
        addressRepository.deleteById(id);
    }

    public List<AddressInnerDto> findAll() {
        return addressMapper.addressesToInnerDtos(addressRepository.findAll());
    }

    public AddressInnerDto findById(long id) {
        Address address = addressRepository.findById(id).orElseThrow(AddressNotFoundException::new);
        return addressMapper.addressToInnerDto(address);
    }

    public Page<Address> search(AddressInnerDto addressInnerDto, Pageable pageable) {

        Specification<Address> specification = Specification.where(null);

        String country = addressInnerDto.getCountry();
        if (StringUtils.hasLength(country)) {
            specification = specification.and(AddressSpecification.hasCountry(country));
        }

        String city = addressInnerDto.getCity();
        if (StringUtils.hasLength(city)) {
            specification = specification.and(AddressSpecification.hasCity(city));
        }

        Integer zipCode = addressInnerDto.getZipCode();
        if (zipCode != null) {
            specification = specification.and(AddressSpecification.hasZipCode(zipCode));
        }

        String street = addressInnerDto.getStreet();
        if (StringUtils.hasLength(street)) {
            specification = specification.and(AddressSpecification.hasStreet(street));
        }

        return addressRepository.findAll(specification, pageable);
    }
}