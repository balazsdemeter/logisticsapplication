package hu.cubix.balage.logisticsapplication.service;

import hu.cubix.balage.logisticsapplication.model.Address;
import hu.cubix.balage.logisticsapplication.model.Milestone;
import hu.cubix.balage.logisticsapplication.model.Section;
import hu.cubix.balage.logisticsapplication.model.TransportPlan;
import hu.cubix.balage.logisticsapplication.model.Users;
import hu.cubix.balage.logisticsapplication.repository.AddressRepository;
import hu.cubix.balage.logisticsapplication.repository.MilestoneRepository;
import hu.cubix.balage.logisticsapplication.repository.SectionRepository;
import hu.cubix.balage.logisticsapplication.repository.TransportPlanRepository;
import hu.cubix.balage.logisticsapplication.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class InitDbService {

    private final AddressRepository addressRepository;
    private final MilestoneRepository milestoneRepository;
    private final SectionRepository sectionRepository;
    private final TransportPlanRepository transportPlanRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public InitDbService(AddressRepository addressRepository,
                         MilestoneRepository milestoneRepository,
                         SectionRepository sectionRepository,
                         TransportPlanRepository transportPlanRepository,
                         UserRepository userRepository,
                         PasswordEncoder passwordEncoder) {
        this.addressRepository = addressRepository;
        this.milestoneRepository = milestoneRepository;
        this.sectionRepository = sectionRepository;
        this.transportPlanRepository = transportPlanRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void initDb() {
        Address address1 = new Address("HU", "Budapest", "Teve utca", 9999, "22-32");
        address1 = addressRepository.save(address1);
        Address address2 = new Address("HU", "Miskolc", "Teszt utca", 8888, "1/A");
        address2 = addressRepository.save(address2);

        LocalDateTime now = LocalDateTime.now();
        Milestone milestone1 = new Milestone(address1, now.plusMinutes(5));
        milestone1 = milestoneRepository.save(milestone1);
        Milestone milestone2 = new Milestone(address2, now.plusMinutes(10));
        milestone2 = milestoneRepository.save(milestone2);

        Section section1 = new Section(0, milestone1, milestone2);
        section1 = sectionRepository.save(section1);

        Milestone milestone3 = new Milestone(address1, now.plusMinutes(15));
        milestone3 = milestoneRepository.save(milestone3);
        Milestone milestone4 = new Milestone(address2, now.plusMinutes(20));
        milestone4 = milestoneRepository.save(milestone4);

        Section section2 = new Section(1, milestone3, milestone4);
        section2 = sectionRepository.save(section2);

        TransportPlan transportPlan = new TransportPlan();
        section1.setTransportPlan(transportPlan);
        transportPlan.getSections().add(section1);
        section2.setTransportPlan(transportPlan);
        transportPlan.getSections().add(section2);
        transportPlan.setExpectedIncome(now.plusMinutes(50));

        transportPlanRepository.save(transportPlan);

        userRepository.save(new Users("address", passwordEncoder.encode("pass"), Set.of("AddressManager")));
        userRepository.save(new Users("transportPlan", passwordEncoder.encode("pass"), Set.of("TransportManager")));
    }
}