package ru.ekaterina.kitchendelivery.config;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamundaAdminConfig {

    @Bean
    public CommandLineRunner initCamundaAdmin(IdentityService identityService) {
        return args -> {
            User adminUser = identityService.createUserQuery().userId("admin").singleResult();

            if (adminUser == null) {
                User newUser = identityService.newUser("admin");
                newUser.setFirstName("Admin");
                newUser.setLastName("Admin");
                newUser.setEmail("admin@localhost");
                newUser.setPassword("admin");
                identityService.saveUser(newUser);

                Group adminGroup = identityService.createGroupQuery().groupId("camunda-admin").singleResult();
                if (adminGroup == null) {
                    adminGroup = identityService.newGroup("camunda-admin");
                    adminGroup.setName("Camunda Administrators");
                    adminGroup.setType("SYSTEM");
                    identityService.saveGroup(adminGroup);
                }

                identityService.createMembership("admin", "camunda-admin");

            } else {
                adminUser.setPassword("admin");
                identityService.saveUser(adminUser);
            }
        };
    }
}