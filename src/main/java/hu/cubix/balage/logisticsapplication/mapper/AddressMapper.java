package hu.cubix.balage.logisticsapplication.mapper;

import hu.cubix.balage.logisticsapplication.dto.AddressDto;
import hu.cubix.balage.logisticsapplication.dto.AddressInnerDto;
import hu.cubix.balage.logisticsapplication.model.Address;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDto addressToDto(Address address);

    Address dtoToAddress(AddressDto addressDto);

    List<AddressDto> addressesToDtos(List<Address> addresses);

    List<Address> dtosToAddresses(List<AddressDto> addressDtos);

    AddressInnerDto addressToInnerDto(Address address);

    Address innerDtoToAddress(AddressInnerDto addressInnerDto);

    List<AddressInnerDto> addressesToInnerDtos(List<Address> addresses);

    List<Address> innerDtosToAddresses(List<AddressInnerDto> addressInnerDtos);

    AddressInnerDto dtoToInnerDto(AddressDto addressDto);

    AddressDto innerDtoToDto(AddressInnerDto addressInnerDto);

    List<AddressInnerDto> dtosToInnerDtos(List<AddressDto> addressDtos);

    List<AddressDto> innerDtosToDtos(List<AddressInnerDto> addressInnerDtos);
}