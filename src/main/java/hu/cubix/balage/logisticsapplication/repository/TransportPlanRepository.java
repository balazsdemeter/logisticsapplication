package hu.cubix.balage.logisticsapplication.repository;

import hu.cubix.balage.logisticsapplication.model.TransportPlan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TransportPlanRepository extends JpaRepository<TransportPlan, Long>  {


    @EntityGraph(attributePaths = {"sections", "sections.startMileStone", "sections.endMileStone"})
    @Query("SELECT t FROM TransportPlan t WHERE t.id=:id")
    Optional<TransportPlan> findByIdWithSections(long id);
}