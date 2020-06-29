package net.simonpeier.firstpass.service;

import net.simonpeier.firstpass.model.Application;
import net.simonpeier.firstpass.model.User;
import net.simonpeier.firstpass.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    private Application getReferenceToApplicationById(long id) {
        return applicationRepository.getOne(id);
    }

    public Application findApplicationById(long id) {
        return applicationRepository.findById(id).get();
    }

    public Application createApplication(Application application) {
        return applicationRepository.saveAndFlush(application);
    }

    public void deleteApplicationById(long id) {
        if (applicationRepository.existsById(id)) {
            applicationRepository.deleteById(id);
        }
    }

    public Application updateApplication(long id, Application application, User user) {
        Application applicationToUpdate = getReferenceToApplicationById(id);

        applicationToUpdate.setUser(application.getUser());
        applicationToUpdate.setName(application.getName());
        applicationToUpdate.setUsername(application.getUsername());
        applicationToUpdate.setDescription(application.getDescription());
        applicationToUpdate.setUser(user);

        return applicationRepository.save(applicationToUpdate);
    }
}
