package hu.cubix.balage.logisticsapplication.service;

import hu.cubix.balage.logisticsapplication.model.Address;
import hu.cubix.balage.logisticsapplication.model.Address_;
import org.springframework.data.jpa.domain.Specification;

public class AddressSpecification {

    public static Specification<Address> hasCountry(String country) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Address_.country), country);
    }

    public static Specification<Address> hasCity(String city) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get(Address_.city)), city.toLowerCase() + "%");
    }

    public static Specification<Address> hasStreet(String street) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get(Address_.street)), street.toLowerCase() + "%");
    }

    public static Specification<Address> hasZipCode(Integer zipCode) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Address_.zipCode), zipCode);
    }
}
