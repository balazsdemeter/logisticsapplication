package hu.cubix.balage.logisticsapplication.controller;

import hu.cubix.balage.logisticsapplication.dto.AddressDto;
import hu.cubix.balage.logisticsapplication.dto.AddressInnerDto;
import hu.cubix.balage.logisticsapplication.exception.AddressNotFoundException;
import hu.cubix.balage.logisticsapplication.mapper.AddressMapper;
import hu.cubix.balage.logisticsapplication.model.Address;
import hu.cubix.balage.logisticsapplication.service.AddressService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    public AddressController(AddressService addressService, AddressMapper addressMapper) {
        this.addressService = addressService;
        this.addressMapper = addressMapper;
    }

    @GetMapping
    public ResponseEntity<List<AddressDto>> findAll() {
        List<AddressInnerDto> addresses = addressService.findAll();
        return ResponseEntity.ok(addressMapper.innerDtosToDtos(addresses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> findById(@PathVariable long id) {
        try {
            AddressInnerDto address = addressService.findById(id);
            return ResponseEntity.ok(addressMapper.innerDtoToDto(address));
        } catch (AddressNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<AddressDto> create(@Validated @RequestBody AddressDto addressDto) {
        AddressInnerDto address = addressService.create(addressMapper.dtoToInnerDto(addressDto));
        return ResponseEntity.ok(addressMapper.innerDtoToDto(address));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable long id) {
        addressService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> modify(@PathVariable long id, @Validated @RequestBody AddressDto addressDto) {
        try {
            AddressInnerDto address = addressService.modify(id, addressMapper.dtoToInnerDto(addressDto));
            return ResponseEntity.ok(addressMapper.innerDtoToDto(address));
        } catch (AddressNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/search")
    public ResponseEntity<List<AddressDto>> search(@Validated @RequestBody AddressDto addressDto, @SortDefault("id") Pageable pageable) {
        Page<Address> page = addressService.search(addressMapper.dtoToInnerDto(addressDto), pageable);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Total-Count", String.valueOf(page.getTotalElements()));

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(addressMapper.addressesToDtos(page.getContent()));
    }
}