package nl.vorstenbosch.required.repository;
import nl.vorstenbosch.required.entity.Requirement;
import nl.vorstenbosch.required.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VersionRepository extends JpaRepository<Version, Long> {

    List<Version> findByRequirement(Requirement requirement);
}
