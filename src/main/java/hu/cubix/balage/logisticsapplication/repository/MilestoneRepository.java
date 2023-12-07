package hu.cubix.balage.logisticsapplication.repository;

import hu.cubix.balage.logisticsapplication.model.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
}