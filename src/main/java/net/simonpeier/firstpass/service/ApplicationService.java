package net.simonpeier.firstpass.service;

import net.simonpeier.firstpass.model.Application;
import net.simonpeier.firstpass.model.User;
import net.simonpeier.firstpass.repository.ApplicationRepository;
import net.simonpeier.firstpass.security.Cypher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UserService userService;
    private final Cypher cypher;

    public ApplicationService(ApplicationRepository applicationRepository, UserService userService) {
        this.applicationRepository = applicationRepository;
        this.userService = userService;
        cypher = new Cypher();
    }

    public List<Application> findAll() {
        return cypher.secureData(applicationRepository.findAll(), userService.getSecretKey(), false);
    }

    public List<Application> findAllByUser(User user) {
        List<Application> applications = new ArrayList<>();
        for (Application application : findAll()) {
            if (application.getUser() == user) {
                applications.add(application);
            }
        }
        return cypher.secureData(applications, userService.getSecretKey(), false);
    }

    private Application getReferenceToApplicationById(long id) {
        return cypher.secureData(applicationRepository.getOne(id), userService.getSecretKey(), false);
    }

    public Application findApplicationById(long id) {
        Optional<Application> optionalApplication = applicationRepository.findById(id);
        return cypher.secureData(optionalApplication.orElse(null), userService.getSecretKey(), false);
    }

    public void createApplication(Application application) {
        applicationRepository.saveAndFlush(cypher.secureData(application, userService.getSecretKey(), true));
    }

    public void deleteApplicationById(long id) {
        if (applicationRepository.existsById(id)) {
            applicationRepository.deleteById(id);
        }
    }

    public void updateApplication(long id, Application application, User user) {
        Application applicationToUpdate = getReferenceToApplicationById(id);

        applicationToUpdate.setUser(application.getUser());
        applicationToUpdate.setName(application.getName());
        applicationToUpdate.setUsername(application.getUsername());
        applicationToUpdate.setDescription(application.getDescription());
        applicationToUpdate.setUser(user);

        applicationRepository.save(cypher.secureData(applicationToUpdate, userService.getSecretKey(), true));
    }
}
