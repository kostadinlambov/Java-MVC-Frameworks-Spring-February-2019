package org.kl.residentevil.utils.entitiesSeed;


import org.kl.residentevil.domain.entities.UserRole;
import org.kl.residentevil.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class EntitiesSeedExecutor {
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public EntitiesSeedExecutor(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @PostConstruct
    public void insertEntities() {
            if(userRoleRepository.count() == 0L ){
            UserRole role1 = new UserRole();
            UserRole role2 = new UserRole();
            UserRole role3 = new UserRole();
            UserRole role4 = new UserRole();
            role1.setAuthority("ADMIN");
            role2.setAuthority("MODERATOR");
            role3.setAuthority("USER");
            role4.setAuthority("ROOT");
            this.userRoleRepository.save(role1);
            this.userRoleRepository.saveAndFlush(role2);
            this.userRoleRepository.saveAndFlush(role3);
            this.userRoleRepository.saveAndFlush(role4);
        }
    }
}
