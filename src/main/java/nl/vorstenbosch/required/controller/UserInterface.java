package nl.vorstenbosch.required.controller;

import nl.vorstenbosch.required.entity.Requirement;
import nl.vorstenbosch.required.entity.Version;
import nl.vorstenbosch.required.repository.RequirementRepository;
import nl.vorstenbosch.required.repository.VersionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping
public class UserInterface {

    @Autowired
    private RequirementRepository requirementRepository;
    @Autowired
    private VersionRepository versionRepository;

    private static final Logger log = LoggerFactory.getLogger(UserInterface.class);

    @GetMapping
    public RedirectView root() {
        return new RedirectView("ui");
    }

    @GetMapping(path = "ui")
    public ModelAndView listRequirements(Map<String, Object> model) {
        model.put("requirements", requirementRepository.findAll());
        model.put("showAllRequirements", true);
        return new ModelAndView("index", model);
    }

    @GetMapping(path = "ui/requirement/{id}")
    public ModelAndView requirementOverview(@PathVariable String id, Map<String, Object> model) {
        Optional<Requirement> requirementOptional = requirementRepository.findById(Long.valueOf(id));
        if (requirementOptional.isPresent()) {
            log.info("requirement found: {}", requirementOptional.get());
            model = prepareRequirementOverviewModel(model, requirementOptional.get());
        } else {
            model.put("error", String.format("requirement with id '%s' does not exist", id));
        }

        log.info("requirement overview model: {}", model);
        return new ModelAndView("index", model);
    }

    @GetMapping(path = "/ui/requirement/{id}/version")
    public ModelAndView showNewVersionForm(@PathVariable String id, Map<String, Object> model) {
        Optional<Requirement> requirementOptional = requirementRepository.findById(Long.valueOf(id));
        if (requirementOptional.isPresent()) {
            Requirement requirement = requirementOptional.get();
            model.put("showNewVersionForm", true);
            model.put("requirement", requirement);
            if (requirement.getLatestVersion() != null) {
                model.put("previousVersion", requirement.getLatestVersion().getDescription());
            }
        } else {
            model.put("error", String.format("requirement with id '%s' does not exist", id));
        }

        return new ModelAndView("index", model);
    }

    @PostMapping(path = "/ui/requirement/{id}/version")
    public ModelAndView createNewVersion(@PathVariable String id, Map<String, Object> model, @ModelAttribute Version version) {
        log.info(String.format("model received: %s", model));
        Optional<Requirement> requirementOptional = requirementRepository.findById(Long.valueOf(id));
        if (requirementOptional.isPresent()) {
            Requirement requirement = requirementOptional.get();
            version.setRequirement(requirement);
            version = versionRepository.save(version);
            model.put("info", String.format("New version created on '%s' for requirement '%s' created", version.getCreationTimestamp(), requirement.getName()));
            requirement.setLatestVersion(version);
            model = prepareRequirementOverviewModel(model, requirement);
        } else {
            model.put("error", String.format("requirement with id '%s' does not exist", id));
            model.put("requirements", requirementRepository.findAll());
            model.put("showAllRequirements", true);
        }

        return new ModelAndView("index", model);
    }

    private Map<String, Object> prepareRequirementOverviewModel(Map<String, Object> model, Requirement requirement) {
        log.info("preparing requirement overview for {}", requirement);
        model.put("requirement", requirement);
        model.put("showRequirementOverview", true);

        if (requirement.getLatestVersion() != null) {
            model.put("latestVersion", requirement.getLatestVersion());
        }

        // FIXME: only find versions of this requirement
        model.put("versions", versionRepository.findAll());

        return model;
    }
}

