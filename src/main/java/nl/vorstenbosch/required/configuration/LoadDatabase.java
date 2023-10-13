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

import java.util.Arrays;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(RequirementRepository requirementRepository, VersionRepository versionRepository) {
        return args -> {
            log.info("Preloading " + requirementRepository.save(new Requirement("UI color scheme")));
            log.info("Preloading " + requirementRepository.save(new Requirement("Login")));
        };
    }
}
