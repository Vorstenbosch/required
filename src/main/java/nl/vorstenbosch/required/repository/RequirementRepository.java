package nl.vorstenbosch.required.repository;
import nl.vorstenbosch.required.entity.Requirement;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RequirementRepository extends JpaRepository<Requirement, Long> {

}
