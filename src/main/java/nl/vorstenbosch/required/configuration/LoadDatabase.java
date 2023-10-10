package nl.vorstenbosch.required.configuration;

import nl.vorstenbosch.required.entity.Requirement;
import nl.vorstenbosch.required.entity.Version;
import nl.vorstenbosch.required.repository.RequirementRepository;
import nl.vorstenbosch.required.repository.VersionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(RequirementRepository requirementRepository, VersionRepository versionRepository) {
        return args -> {
            Requirement requirement = new Requirement("UI color scheme");
            Version version = new Version();
            version.setDescription("Colors scheme consist only of white, blue and red");
            version.setRequirement(requirement);

            log.info("Preloading " + requirementRepository.save(requirement));
            log.info("Preloading " + versionRepository.save(version));
            log.info("Preloading " + requirementRepository.save(new Requirement("Login")));
        };
    }
}
