package hu.cubix.balage.logisticsapplication.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Section {
    @Id
    @GeneratedValue
    private long id;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    private int orderNumber;

    @ManyToOne
    private Milestone startMileStone;

    @ManyToOne
    private Milestone endMileStone;

    @ManyToOne
    private TransportPlan transportPlan;

    public Section() {
    }

    public Section(int orderNumber, Milestone startMileStone, Milestone endMileStone) {
        this.orderNumber = orderNumber;
        this.startMileStone = startMileStone;
        this.endMileStone = endMileStone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Milestone getStartMileStone() {
        return startMileStone;
    }

    public void setStartMileStone(Milestone startMileStone) {
        this.startMileStone = startMileStone;
    }

    public Milestone getEndMileStone() {
        return endMileStone;
    }

    public void setEndMileStone(Milestone endMileStone) {
        this.endMileStone = endMileStone;
    }

    public TransportPlan getTransportPlan() {
        return transportPlan;
    }

    public void setTransportPlan(TransportPlan transportPlan) {
        this.transportPlan = transportPlan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return id == section.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}