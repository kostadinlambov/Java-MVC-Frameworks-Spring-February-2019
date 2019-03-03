package kl.services;

import kl.domain.entities.User;
import kl.domain.models.service.UserServiceModel;
import kl.repositories.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserServiceModel userRegister(UserServiceModel userServiceModel) {
        User user = this.modelMapper.map(userServiceModel, User.class);
        user.setPassword(DigestUtils.sha256Hex(user.getPassword()));
        User savedUser = this.userRepository.saveAndFlush(user);
        if(savedUser != null){
            return this.modelMapper.map(user, UserServiceModel.class);
        }
        return null;
    }

    @Override
    public UserServiceModel userLogin(UserServiceModel userServiceModel) {
        UserServiceModel userByUsername = this.findUserByUsername(userServiceModel.getUsername());

        if(userByUsername == null || !DigestUtils.sha256Hex(userServiceModel.getPassword()).equals(userByUsername.getPassword())) {
               throw new IllegalArgumentException("Incorrect credentials!");
        }

        return this.modelMapper.map(userByUsername, UserServiceModel.class);
    }

    @Override
    public UserServiceModel findUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username);
        if(user == null){
            return null;
        }

        return this.modelMapper.map(user, UserServiceModel.class);
    }

}
