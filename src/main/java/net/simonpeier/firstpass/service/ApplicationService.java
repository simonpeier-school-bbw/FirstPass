package net.simonpeier.firstpass.service;

import net.simonpeier.firstpass.model.Application;
import net.simonpeier.firstpass.model.User;
import net.simonpeier.firstpass.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    public List<Application> findAllByUser(User user) {
        List<Application> applications = new ArrayList<>();
        for (Application application : findAll()) {
            if (application.getUser() == user) {
                applications.add(application);
            }
        }
        return applications;
    }

    public Optional<Application> findApplicationById(long id) {
        return applicationRepository.findById(id);
    }

    public Application createApplication(Application application) {
        return applicationRepository.saveAndFlush(application);
    }

    public void deleteApplicationById(long id) {
        if (applicationRepository.existsById(id)) {
            applicationRepository.deleteById(id);
        }
    }

    public Application updateApplication(Application application, long id) {
        Application updatedApplication;
        Optional<Application> optionalUpdatedEntry = findApplicationById(id);

        if (optionalUpdatedEntry.isPresent()) {
            updatedApplication = optionalUpdatedEntry.get();
        } else {
            updatedApplication = application;
            updatedApplication.setId(id);
        }
        return applicationRepository.saveAndFlush(updatedApplication);
    }
}
