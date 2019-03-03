package kl.services;

import kl.domain.models.service.UserServiceModel;

public interface UserService {

    UserServiceModel userRegister(UserServiceModel userServiceModel);

    UserServiceModel userLogin(UserServiceModel userServiceModel);

    UserServiceModel findUserByUsername(String username);

}
