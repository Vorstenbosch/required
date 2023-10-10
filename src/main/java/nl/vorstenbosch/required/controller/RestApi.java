package nl.vorstenbosch.required.controller;

import nl.vorstenbosch.required.entity.Requirement;
import nl.vorstenbosch.required.entity.Version;
import nl.vorstenbosch.required.repository.RequirementRepository;
import nl.vorstenbosch.required.repository.VersionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="api")
public class RestApi {
    @Autowired
    private RequirementRepository requirementRepository;
    @Autowired
    private VersionRepository versionRepository;

    private static final Logger log = LoggerFactory.getLogger(RestApi.class);

    @PostMapping(path = "requirement")
    public Requirement createNewRequirement(@RequestParam String name, @RequestParam String description) {
        Requirement requirement = new Requirement(name);
        requirementRepository.save(requirement);
        return requirement;
    }

    @GetMapping(path = "requirement")
    public List<Requirement> getRequirements(@RequestParam Optional<Long> id) {
        if (id.isPresent()) {
            List requirementList = new ArrayList();
            Optional<Requirement> result = requirementRepository.findById(id.get());
            if (result.isPresent()) {
                requirementList.add(result.get());
            }
            return requirementList;

        } else {
            return requirementRepository.findAll();
        }
    }

    @DeleteMapping(path = "requirement")
    public void deleteRequirement(@RequestParam Long id) {
        requirementRepository.deleteAllById(Arrays.asList(id));
    }

    // TODO: add path param here?
    @GetMapping(path = "version")
    public List<Version> getVersions(@RequestParam Optional<Long> id) {
        if (id.isPresent()) {
            return versionRepository.findAllById(Arrays.asList(id.get()));
        } else {
            return versionRepository.findAll();
        }
    }

    @DeleteMapping(path = "version")
    public void deleteVersion(@RequestParam Long id) {
        versionRepository.deleteAllById(Arrays.asList(id));
    }
}
