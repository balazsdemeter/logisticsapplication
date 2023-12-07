package hu.cubix.balage.logisticsapplication.repository;

import hu.cubix.balage.logisticsapplication.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {
}