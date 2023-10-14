package nl.vorstenbosch.required.controller;

import nl.vorstenbosch.required.entity.Requirement;
import nl.vorstenbosch.required.entity.Version;
import nl.vorstenbosch.required.model.CreateVersionData;
import nl.vorstenbosch.required.repository.RequirementRepository;
import nl.vorstenbosch.required.repository.VersionRepository;
import nl.vorstenbosch.required.util.DiffUtils;
import nl.vorstenbosch.required.util.UserInterfaceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static nl.vorstenbosch.required.util.UserInterfaceUtils.ListToStringWithNewLines;

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
        prepareAllRequirementsList(model);
        return new ModelAndView("index", model);
    }

    @GetMapping(path = "ui/requirement/{id}")
    public ModelAndView requirementOverview(@PathVariable String id, Map<String, Object> model) {
        Optional<Requirement> requirementOptional = requirementRepository.findById(Long.valueOf(id));
        if (requirementOptional.isPresent()) {
            log.info("requirement found: {}", requirementOptional.get());
            prepareRequirementOverviewModel(model, requirementOptional.get());
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
                model.put("previousVersion", ListToStringWithNewLines(requirement.getLatestVersion().getDescription()));
            }
        } else {
            model.put("error", String.format("requirement with id '%s' does not exist", id));
        }

        return new ModelAndView("index", model);
    }

    @GetMapping(path = "/ui/requirement")
    public ModelAndView showNewRequirementForm(Map<String, Object> model) {
        model.put("showNewRequirementForm", true);
        return new ModelAndView("index", model);
    }

    @PostMapping(path = "/ui/requirement")
    public ModelAndView showNewRequirementForm(@ModelAttribute Requirement requirement, Map<String, Object> model) {
        model.put("info", String.format("Requirement '%s' created", requirement.getName()));
        requirementRepository.save(requirement);
        prepareAllRequirementsList(model);
        return new ModelAndView("index", model);
    }

    @PostMapping(path = "/ui/requirement/{id}/version")
    public ModelAndView createNewVersion(@PathVariable String id, Map<String, Object> model, @ModelAttribute CreateVersionData data) {
        log.info(String.format("model received: %s", model));
        Optional<Requirement> requirementOptional = requirementRepository.findById(Long.valueOf(id));
        if (requirementOptional.isPresent()) {
            Requirement requirement = requirementOptional.get();
            Version version = new Version();
            version.setDescription(UserInterfaceUtils.parseDescription(data.getDescription()));
            version.setRequirement(requirement);
            version = versionRepository.save(version);
            model.put("info", String.format("New version created on '%s' for requirement '%s' created", version.getCreationTimestamp(), requirement.getName()));
            requirement.setLatestVersion(version);
            prepareRequirementOverviewModel(model, requirement);
        } else {
            model.put("error", String.format("requirement with id '%s' does not exist", id));
            model.put("requirements", requirementRepository.findAll());
            model.put("showAllRequirements", true);
        }

        return new ModelAndView("index", model);
    }

    @GetMapping(path = "/ui/requirement/{requirementId}/version/{versionId}")
    public ModelAndView showVersionOverview(@PathVariable String requirementId, @PathVariable String versionId, Map<String, Object> model) {
        Optional<Requirement> requirementOptional = requirementRepository.findById(Long.valueOf(requirementId));
        Optional<Version> versionOptional = versionRepository.findById(Long.valueOf(versionId));
        if (requirementOptional.isPresent() && versionOptional.isPresent()) {
            Requirement requirement = requirementOptional.get();
            Version version = versionOptional.get();
            model.put("requirement", requirement);
            model.put("version", version);
            model.put("descriptionString", UserInterfaceUtils.ListToStringWithNewLines(version.getDescription()));
            model.put("versions", versionRepository.findByRequirement(requirement));
            model.put("showVersionOverview", true);
        } else {
            model.put("error", String.format("version with id '%s' for requirement with id '%s' does not exist", versionId, requirementId));
            model.put("requirements", requirementRepository.findAll());
            model.put("showAllRequirements", true);
        }

        return new ModelAndView("index", model);
    }

    @PostMapping(path = "/ui/requirement/{requirementId}/version/{versionId}/diff")
    public ModelAndView compareVersions(@PathVariable String requirementId, @PathVariable String versionId, Map<String, Object> model, @ModelAttribute Version version) {
        log.info("model received: {}", version);
        Optional<Version> versionLeftOptional = versionRepository.findById(Long.valueOf(versionId));
        //FIXME: nullpointer on empty model
        Optional<Version> versionRightOptional = versionRepository.findById(Long.valueOf(version.getId()));
        Optional<Requirement> requirementOptional = requirementRepository.findById(Long.valueOf(requirementId));
        if (versionRightOptional.isPresent() && versionLeftOptional.isPresent() && requirementOptional.isPresent()) {
            model.put("showVersionCompare", true);
            model.put("versionLeft", versionLeftOptional.get());
            model.put("versionRight", versionRightOptional.get());
            model.put("requirement", requirementOptional.get());
            model.put("diffRows", DiffUtils.generateDiff(versionLeftOptional.get().getDescription(), versionRightOptional.get().getDescription()));
        } else {
            model.put("error", String.format("invalid compare"));
            model.put("requirements", requirementRepository.findAll());
            model.put("showAllRequirements", true);
        }
        return new ModelAndView("index", model);
    }

    private void prepareRequirementOverviewModel(Map<String, Object> model, Requirement requirement) {
        log.info("preparing requirement overview for {}", requirement);
        model.put("requirement", requirement);
        model.put("showRequirementOverview", true);

        if (requirement.getLatestVersion() != null) {
            model.put("latestVersion", requirement.getLatestVersion());
            model.put("latestVersionDescription", requirement.getLatestVersion().getDescription());
        }

        List<Version> versions = versionRepository.findByRequirement(requirement);
        model.put("noVersions", !versions.isEmpty());
        model.put("versions", versions);
    }

    private void prepareAllRequirementsList(Map<String, Object> model) {
        model.put("requirements", requirementRepository.findAll());
        model.put("showAllRequirements", true);
    }
}

