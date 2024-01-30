package rest.api.service;


import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rest.api.entity.User;
import rest.api.exceptions.ProjectException;
import rest.api.repository.UserRepositoryImpl;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepositoryImpl userRepository;

    public UserService(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUserList() {
        return userRepository.getUserList();
    }

    public User getUser(int id) {
        try {
            return userRepository.getUser(id);
        } catch (PersistenceException e) {
            throw new ProjectException("user with id " + id + " not found in database");
        }
    }

    public void addUser(User user) {
        try {
            userRepository.addUser(user);
        } catch (PersistenceException e) {
            throw new ProjectException("login taken");
        }
    }

    public void loginUser(User user) {
        try {
            userRepository.loginUser(user);
        } catch (PersistenceException e) {
            throw new ProjectException("login or password incorrect");
        }
    }

    public void changePassword(User user) {
        User checkUser;
        try {
            checkUser = userRepository.loginUser(user);
            checkUser.setNewPassword(user.getNewPassword());
        } catch (PersistenceException e) {
            throw new ProjectException("old password incorrectly");
        }
        userRepository.changePassword(checkUser);
    }
}
