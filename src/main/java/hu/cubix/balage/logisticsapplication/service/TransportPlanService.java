package hu.cubix.balage.logisticsapplication.service;

import hu.cubix.balage.logisticsapplication.config.LogisticsConfigurationProperties;
import hu.cubix.balage.logisticsapplication.config.LogisticsConfigurationProperties.Delay;
import hu.cubix.balage.logisticsapplication.config.LogisticsConfigurationProperties.Percent;
import hu.cubix.balage.logisticsapplication.exception.MilestoneNotFoundException;
import hu.cubix.balage.logisticsapplication.exception.SectionNotFoundException;
import hu.cubix.balage.logisticsapplication.exception.TransportPlanNotFoundException;
import hu.cubix.balage.logisticsapplication.model.Milestone;
import hu.cubix.balage.logisticsapplication.model.Section;
import hu.cubix.balage.logisticsapplication.model.TransportPlan;
import hu.cubix.balage.logisticsapplication.repository.MilestoneRepository;
import hu.cubix.balage.logisticsapplication.repository.TransportPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

@Service
public class TransportPlanService {

    private final MilestoneRepository milestoneRepository;
    private final TransportPlanRepository transportPlanRepository;
    private final LogisticsConfigurationProperties logisticsConfigurationProperties;

    public TransportPlanService(MilestoneRepository milestoneRepository,
                                TransportPlanRepository transportPlanRepository,
                                LogisticsConfigurationProperties logisticsConfigurationProperties) {
        this.milestoneRepository = milestoneRepository;
        this.transportPlanRepository = transportPlanRepository;
        this.logisticsConfigurationProperties = logisticsConfigurationProperties;
    }

    @Transactional
    public void addDelay(long transportPlanId, long milesStoneId, int delay) {
        TransportPlan transportPlan = transportPlanRepository.findByIdWithSections(transportPlanId).orElseThrow(TransportPlanNotFoundException::new);
        milestoneRepository.findById(milesStoneId).orElseThrow(MilestoneNotFoundException::new);

        List<Section> sections = transportPlan.getSections();
        sections = sections.stream().sorted(Comparator.comparing(Section::getOrderNumber)).toList();

        var lastIndex = sections.size() - 1;
        Section section = sections
                .stream()
                .filter(s -> s.getStartMileStone().getId() == milesStoneId || s.getEndMileStone().getId() == milesStoneId)
                .findFirst()
                .orElseThrow(SectionNotFoundException::new);

        Milestone startMileStone = section.getStartMileStone();
        Milestone endMileStone = section.getEndMileStone();

        if (startMileStone.getId() == milesStoneId) {
            startMileStone.setPlannedTime(startMileStone.getPlannedTime().plusMinutes(delay));
            milestoneRepository.save(startMileStone);
            endMileStone.setPlannedTime(endMileStone.getPlannedTime().plusMinutes(delay));
            milestoneRepository.save(endMileStone);
        }

        if (endMileStone.getId() == milesStoneId) {
            endMileStone.setPlannedTime(endMileStone.getPlannedTime().plusMinutes(delay));
            milestoneRepository.save(endMileStone);

            var index = sections.indexOf(section);

            if (index + 1 <= lastIndex) {
                Section newSection = sections.get(index + 1);
                Milestone newStartMileStone = newSection.getStartMileStone();
                newStartMileStone.setPlannedTime(newStartMileStone.getPlannedTime().plusMinutes(delay));
                milestoneRepository.save(newStartMileStone);
            }
        }

        transportPlan.setExpectedIncome(decreaseExpectedIncome(transportPlan.getExpectedIncome(), delay));
        transportPlanRepository.save(transportPlan);
    }

    private LocalDateTime decreaseExpectedIncome(LocalDateTime expectedIncome, int delay) {
        double percentage = getPercent(delay);
        if (percentage == 0) {
            return expectedIncome;
        }

        long secondsToSubtract = (long) (percentage * ChronoUnit.SECONDS.between(expectedIncome, LocalDateTime.now()));
        return expectedIncome.minusSeconds(secondsToSubtract);
    }

    private Double getPercent(int delay) {
        Delay delayProperty = logisticsConfigurationProperties.getDelay();
        Percent percent = logisticsConfigurationProperties.getPercent();

        if (delay >= delayProperty.getDelay3()) {
            return percent.getPercent3();
        }

        if (delay >= delayProperty.getDelay2()) {
            return percent.getPercent2();
        }

        if (delay >= delayProperty.getDelay1()) {
            return percent.getPercent1();
        }

        return (double) 0;
    }
}