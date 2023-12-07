package hu.cubix.balage.logisticsapplication.web;

import hu.cubix.balage.logisticsapplication.config.LogisticsConfigurationProperties;
import hu.cubix.balage.logisticsapplication.dto.DelayDto;
import hu.cubix.balage.logisticsapplication.dto.LoginDto;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class TransportPlansControllerIT {

    private static final String TEST_USER = "testUser";
    private static final String TEST_PASS = "testPass";

    private static final String URI = "/api/transportPlans/%d/delay";

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private TransportPlanRepository transportPlanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LogisticsConfigurationProperties properties;

    private String token;

    private Long transportId;
    private LocalDateTime expectedIncome;

    private Long mileStone1Id;
    private LocalDateTime mileStone1PlannedTime;

    private Long mileStone2Id;
    private LocalDateTime mileStone2PlannedTime;

    private Long mileStone3Id;
    private LocalDateTime mileStone3PlannedTime;

    private Long mileStone4Id;
    private LocalDateTime mileStone4PlannedTime;

    private Long mileStone5Id;

    @BeforeEach
    public void init() {
        deleteAllInBatch();
        createTransportPlan();
        createUser();

        this.token = webTestClient.post()
                .uri("/api/login")
                .bodyValue(new LoginDto(TEST_USER, TEST_PASS))
                .exchange()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    public void test_transportPlan_notFound() {
        webTestClient
                .post()
                .uri(String.format(URI, transportId + 1))
                .headers(http -> http.setBearerAuth(token))
                .bodyValue(new DelayDto(1, 1))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void test_MileStone_notFound() {
        webTestClient
                .post()
                .uri(String.format(URI, transportId))
                .headers(http -> http.setBearerAuth(token))
                .bodyValue(new DelayDto(mileStone5Id + 1, 1))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void test_MileStone_notPartOfSection() {
        webTestClient
                .post()
                .uri(String.format(URI, transportId))
                .headers(http -> http.setBearerAuth(token))
                .bodyValue(new DelayDto(mileStone5Id, 1))
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void test_transportPlan_startMilestoneDelay() {
        int delay = 15;
        webTestClient
                .post()
                .uri(String.format(URI, transportId))
                .headers(http -> http.setBearerAuth(token))
                .bodyValue(new DelayDto(mileStone1Id, delay))
                .exchange()
                .expectStatus().isOk();

        Milestone milestone1 = milestoneRepository.findById(mileStone1Id).get();
        assertThat(milestone1.getPlannedTime()).isCloseTo(mileStone1PlannedTime.plusMinutes(delay), within(1, ChronoUnit.MICROS));

        Milestone milestone2 = milestoneRepository.findById(mileStone2Id).get();
        assertThat(milestone2.getPlannedTime()).isCloseTo(mileStone2PlannedTime.plusMinutes(delay), within(1, ChronoUnit.MICROS));

        Milestone milestone3 = milestoneRepository.findById(mileStone3Id).get();
        assertThat(milestone3.getPlannedTime()).isCloseTo(mileStone3PlannedTime, within(1, ChronoUnit.MICROS));

        Milestone milestone4 = milestoneRepository.findById(mileStone4Id).get();
        assertThat(milestone4.getPlannedTime()).isCloseTo(mileStone4PlannedTime, within(1, ChronoUnit.MICROS));
    }

    @Test
    public void test_transportPlan_endMilestoneDelay() {
        int delay = 15;
        webTestClient
                .post()
                .uri(String.format(URI, transportId))
                .headers(http -> http.setBearerAuth(token))
                .bodyValue(new DelayDto(mileStone2Id, delay))
                .exchange()
                .expectStatus().isOk();

        Milestone milestone1 = milestoneRepository.findById(mileStone1Id).get();
        assertThat(milestone1.getPlannedTime()).isCloseTo(mileStone1PlannedTime, within(1, ChronoUnit.MICROS));

        Milestone milestone2 = milestoneRepository.findById(mileStone2Id).get();
        assertThat(milestone2.getPlannedTime()).isCloseTo(mileStone2PlannedTime.plusMinutes(delay), within(1, ChronoUnit.MICROS));

        Milestone milestone3 = milestoneRepository.findById(mileStone3Id).get();
        assertThat(milestone3.getPlannedTime()).isCloseTo(mileStone3PlannedTime.plusMinutes(delay), within(1, ChronoUnit.MICROS));

        Milestone milestone4 = milestoneRepository.findById(mileStone4Id).get();
        assertThat(milestone4.getPlannedTime()).isCloseTo(mileStone4PlannedTime, within(1, ChronoUnit.MICROS));
    }

    @Test
    public void test_transportPlan_endMilestoneDelay_noMoreSection() {
        int delay = 15;
        webTestClient
                .post()
                .uri(String.format(URI, transportId))
                .headers(http -> http.setBearerAuth(token))
                .bodyValue(new DelayDto(mileStone4Id, delay))
                .exchange()
                .expectStatus().isOk();

        Milestone milestone4 = milestoneRepository.findById(mileStone4Id).get();
        assertThat(milestone4.getPlannedTime()).isCloseTo((mileStone4PlannedTime.plusMinutes(delay)), within(1, ChronoUnit.MICROS));
    }

    @Test
    public void test_transportPlan_ExpectedIncome_30minutes() {
        int delay = 30;
        webTestClient
                .post()
                .uri(String.format(URI, transportId))
                .headers(http -> http.setBearerAuth(token))
                .bodyValue(new DelayDto(mileStone1Id, delay))
                .exchange()
                .expectStatus().isOk();

        TransportPlan transportPlan = transportPlanRepository.findById(transportId).get();

        assertThat(transportPlan.getExpectedIncome()).isCloseTo(getCalculatedExpectedIncome(properties.getPercent().getPercent1()), within(1, ChronoUnit.MICROS));
    }

    @Test
    public void test_transportPlan_ExpectedIncome_60minutes() {
        int delay = 60;
        webTestClient
                .post()
                .uri(String.format(URI, transportId))
                .headers(http -> http.setBearerAuth(token))
                .bodyValue(new DelayDto(mileStone1Id, delay))
                .exchange()
                .expectStatus().isOk();

        TransportPlan transportPlan = transportPlanRepository.findById(transportId).get();

        assertThat(transportPlan.getExpectedIncome()).isCloseTo(getCalculatedExpectedIncome(properties.getPercent().getPercent2()), within(1, ChronoUnit.MICROS));
    }

    @Test
    public void test_transportPlan_ExpectedIncome_120minutes() {
        int delay = 120;
        webTestClient
                .post()
                .uri(String.format(URI, transportId))
                .headers(http -> http.setBearerAuth(token))
                .bodyValue(new DelayDto(mileStone1Id, delay))
                .exchange()
                .expectStatus().isOk();

        TransportPlan transportPlan = transportPlanRepository.findById(transportId).get();

        assertThat(transportPlan.getExpectedIncome()).isCloseTo(getCalculatedExpectedIncome(properties.getPercent().getPercent3()), within(1, ChronoUnit.MICROS));
    }

    @Test
    public void test_transportPlan_ExpectedIncome_noDecrease() {
        int delay = 29;
        webTestClient
                .post()
                .uri(String.format(URI, transportId))
                .headers(http -> http.setBearerAuth(token))
                .bodyValue(new DelayDto(mileStone1Id, delay))
                .exchange()
                .expectStatus().isOk();

        TransportPlan transportPlan = transportPlanRepository.findById(transportId).get();

        assertThat(transportPlan.getExpectedIncome()).isCloseTo(expectedIncome, within(1, ChronoUnit.MICROS));
    }

    private LocalDateTime getCalculatedExpectedIncome(Double percentage) {
        long secondsToSubtract = (long) (percentage * ChronoUnit.SECONDS.between(expectedIncome, LocalDateTime.now()));
        return expectedIncome.minusSeconds(secondsToSubtract);
    }

    private void deleteAllInBatch() {
        sectionRepository.deleteAllInBatch();
        transportPlanRepository.deleteAllInBatch();
        milestoneRepository.deleteAllInBatch();
        addressRepository.deleteAllInBatch();
    }

    public void createTransportPlan() {
        Address address1 = new Address("HU", "Gotham", "Denevér utca", 9999, "22-32");
        address1 = addressRepository.save(address1);
        Address address2 = new Address("HU", "Metropolis", "Acél utca", 8888, "1/A");
        address2 = addressRepository.save(address2);

        LocalDateTime now = LocalDateTime.now();
        TransportPlan transportPlan = new TransportPlan();
        expectedIncome = now.plusMinutes(50);
        transportPlan.setExpectedIncome(expectedIncome);
        transportPlan = transportPlanRepository.save(transportPlan);
        transportId = transportPlan.getId();

        mileStone1PlannedTime = now.plusMinutes(5);
        Milestone milestone1 = new Milestone(address1, mileStone1PlannedTime);
        milestone1 = milestoneRepository.save(milestone1);
        mileStone1Id = milestone1.getId();

        mileStone2PlannedTime = now.plusMinutes(10);
        Milestone milestone2 = new Milestone(address2, mileStone2PlannedTime);
        milestone2 = milestoneRepository.save(milestone2);
        mileStone2Id = milestone2.getId();

        Section section1 = new Section(0, milestone1, milestone2);
        section1.setTransportPlan(transportPlan);
        sectionRepository.save(section1);

        mileStone3PlannedTime = now.plusMinutes(15);
        Milestone milestone3 = new Milestone(address1, mileStone3PlannedTime);
        milestone3 = milestoneRepository.save(milestone3);
        mileStone3Id = milestone3.getId();

        mileStone4PlannedTime = now.plusMinutes(20);
        Milestone milestone4 = new Milestone(address2, mileStone4PlannedTime);
        milestone4 = milestoneRepository.save(milestone4);
        mileStone4Id = milestone4.getId();

        Section section2 = new Section(1, milestone3, milestone4);
        section2.setTransportPlan(transportPlan);
        sectionRepository.save(section2);

        Milestone milestone5 = new Milestone(address2, now.plusMinutes(100));
        milestone5 = milestoneRepository.save(milestone5);
        mileStone5Id = milestone5.getId();
    }

    private void createUser() {
        if (userRepository.findByUsername(TEST_USER).isEmpty()) {
            userRepository.save(new Users(TEST_USER, passwordEncoder.encode(TEST_PASS), Set.of("TransportManager")));
        }
    }
}