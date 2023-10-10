package nl.vorstenbosch.required.entity;

import org.hibernate.annotations.JoinFormula;

import javax.persistence.*;

@Entity
public class Requirement {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula("(" +
            "SELECT v.id " +
            "FROM version v " +
            "WHERE v.requirement_id = id " +
            "ORDER BY v.creation_timestamp DESC " +
            "LIMIT 1" +
            ")")
    private Version latestVersion;

    protected Requirement() {}

    public Requirement(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Version getLatestVersion() {
        return latestVersion;
    }

    @Override
    public String toString() {
        return String.format("{id=%s, name=%s, latestVersion=%s}", this.id, this.name, this.latestVersion);
    }

    public void setLatestVersion(Version version) {
        this.latestVersion = version;
    }
}
