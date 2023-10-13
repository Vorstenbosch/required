package nl.vorstenbosch.required.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
public class Version {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Instant creationTimestamp;
    @ElementCollection(targetClass=String.class)
    private List<String> description;

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

    public List<String> getDescription() {
        return description;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setDescription(List<String> description) {
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
