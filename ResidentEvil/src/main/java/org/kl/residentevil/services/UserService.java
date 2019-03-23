package org.kl.residentevil.services;

import org.kl.residentevil.domain.models.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService  extends UserDetailsService {
    boolean register(UserServiceModel userServiceModel);

    List<UserServiceModel> getAll();

    boolean promote(String id);

    boolean demote(String id);
}
