package nl.vorstenbosch.required.repository;
import nl.vorstenbosch.required.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionRepository extends JpaRepository<Version, Long> {

}
