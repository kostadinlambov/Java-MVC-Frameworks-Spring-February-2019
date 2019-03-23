package org.kl.residentevil.services;

import org.kl.residentevil.domain.entities.User;
import org.kl.residentevil.domain.entities.UserRole;
import org.kl.residentevil.domain.models.service.UserServiceModel;
import org.kl.residentevil.repositories.UserRepository;
import org.kl.residentevil.repositories.UserRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public boolean register(UserServiceModel userServiceModel) {
        User user = this.modelMapper.map(userServiceModel, User.class);

        user.setPassword(this.bCryptPasswordEncoder
                .encode(user.getPassword()));

        UserRole userRole = this.userRoleRepository.findByAuthority("USER");
        UserRole root = this.userRoleRepository.findByAuthority("ROOT");

        Set<UserRole> roles = new HashSet<>();
        if (this.userRepository.findAll().isEmpty()) {
            roles.add(root);
        } else {
            roles.add(userRole);
        }

        user.setAuthorities(roles);
        User savedUser = this.userRepository.save(user);

        if (savedUser != null) {
            return true;
        }
        return false;
    }

    @Override
    public List<UserServiceModel> getAll() {
        List<User> allUsers = this.userRepository.findAll();
        return allUsers.stream()
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository
                .findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));
    }

    @Override
    public boolean promote(String id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user != null) {
            String role = this.extractAuthority(user);

            switch (role) {
                case "USER":
                    user.setAuthorities(this.updateAuthority("MODERATOR"));
                    break;
                case "MODERATOR":
                    user.setAuthorities(this.updateAuthority("ADMIN"));
                    break;
                default:
                    throw new IllegalArgumentException("There is no role, higher than Admin!");
            }

            return this.userRepository.saveAndFlush(user) != null;
        }

        throw new IllegalArgumentException("Something went wrong!");
    }

    @Override
    public boolean demote(String id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user != null) {
            String role = this.extractAuthority(user);

            switch (role) {
                case "ADMIN":
                    user.setAuthorities(this.updateAuthority("MODERATOR"));
                    break;
                case "MODERATOR":
                    user.setAuthorities(this.updateAuthority("USER"));
                    break;
                default:
                    throw new IllegalArgumentException("There is no role, lower than Admin!");
            }

            return this.userRepository.saveAndFlush(user) != null;
        }

        throw new IllegalArgumentException("Something went wrong!");
    }


    private String extractAuthority(User user) {
        return user.getAuthorities().stream()
                .findFirst()
                .orElse(null)
                .getAuthority();
    }

    private Set<UserRole> updateAuthority(String authorityName) {
        Set<UserRole> userRoles = new HashSet<>();
        UserRole authority = this.userRoleRepository.findByAuthority(authorityName);
        userRoles.add(authority);

        return userRoles;
    }


}
