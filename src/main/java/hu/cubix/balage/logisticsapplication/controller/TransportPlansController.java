package hu.cubix.balage.logisticsapplication.controller;

import hu.cubix.balage.logisticsapplication.dto.DelayDto;
import hu.cubix.balage.logisticsapplication.exception.MilestoneNotFoundException;
import hu.cubix.balage.logisticsapplication.exception.SectionNotFoundException;
import hu.cubix.balage.logisticsapplication.exception.TransportPlanNotFoundException;
import hu.cubix.balage.logisticsapplication.service.TransportPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transportPlans")
public class TransportPlansController {

    private final TransportPlanService transportPlanService;

    public TransportPlansController(TransportPlanService transportPlanService) {
        this.transportPlanService = transportPlanService;
    }

    @PostMapping("/{id}/delay")
    public ResponseEntity delay(@PathVariable long id, @RequestBody DelayDto delayDto) {
        try {
            transportPlanService.addDelay(id, delayDto.getMileStoneId(), delayDto.getDelay());
        } catch (TransportPlanNotFoundException | MilestoneNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (SectionNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}