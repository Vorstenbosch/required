package nl.vorstenbosch.required.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class Version {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Instant creationTimestamp;
    private String  description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "requirement_id")
    private Requirement requirement;

    public Version() {}

    public Long getId() {
        return id;
    }

    public Instant getCreationTimestamp() {
        return creationTimestamp;
    }

    public String getDescription() {
        return description;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    @Override
    public String toString() {
    return String.format("{id=%s, creationTimestamp=%s, description=%s}", this.id, this.creationTimestamp, this.description);
    }
}
