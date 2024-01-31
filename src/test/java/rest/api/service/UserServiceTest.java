package rest.api.service;

import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import rest.api.entity.User;
import rest.api.exceptions.ProjectException;
import rest.api.repository.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepositoryImpl userRepository;
    @InjectMocks
    private UserService userService;

    //list with Users
    @Test
    void getUserList_withUserAndWithoutUser_getUsersOrEmptyList() {
        List<User> list = List.of(new User("test", "test"), new User("test", "test"));
        Mockito.doReturn(list).when(userRepository).getUserList();
        assertEquals(list, userService.getUserList());
    }

    //list without Users
    @Test
    void getUserList_withoutUsers_getEmptyList() {
        List<User> emptyList = new ArrayList<>();
        Mockito.doReturn(emptyList).when(userRepository).getUserList();
        assertEquals(emptyList, userService.getUserList());
    }

    //есть юзер
    @Test
    void getUser_isInDataBase_getUser() {
        User user = new User();
        user.setId(1);
        user.setLogin("test");
        user.setPassword("test");
        Mockito.doReturn(user).when(userRepository).getUser(anyInt());
        assertEquals(user, userService.getUser(1));
    }

    //нет юзера
    @Test
    void getUser_notIsInDataBase_getException() {
        Mockito.doThrow(PersistenceException.class).when(userRepository).getUser(0);
        assertThrows(ProjectException.class, () -> userService.getUser(0));
    }

    //если пытаемся добавить уже существующий логин -> Exception
    @Test
    void addUser_whenLoginIsTaken_getException() {
        User user = new User("test", "test123");
        User user1 = new User("test", "test");
        Mockito.doAnswer(invocation -> {
            if (user.getLogin().equals(user1.getLogin())) {
                throw new PersistenceException();
            }
            return null;
        }).when(userRepository).addUser(user);
        assertThrows(ProjectException.class, () -> userService.addUser(user));
    }

    // добавляем юзера в базу
    @Test
    void addUser_addUniqueUser_addUser() {
        User user = new User();
        userRepository.addUser(user);
        Mockito.verify(userRepository).addUser(user);
    }

    //логин и пароль совпадают (логин)
    @Test
    void loginUser_loginAndPasswordAreTheSame_login() {
        User checkUser = new User("login", "login");
        User dataBase = new User("login", "login");
        Mockito.doAnswer(invocationOnMock -> {
            if (checkUser.getLogin().equals(dataBase.getLogin()) &&
                    checkUser.getPassword().equals(dataBase.getPassword())) {
                return checkUser;
            }
            throw new PersistenceException();
        }).when(userRepository).loginUser(checkUser);
        assertDoesNotThrow(() -> userService.loginUser(checkUser));
    }

    //логин и пароль не совпадают
    @Test
    void loginUser_loginAndPasswordDontMatch_getException() {
        User checkUser = new User("login", "login");
        User dataBase = new User("login1", "login");
        Mockito.doAnswer(invocationOnMock -> {
            if (checkUser.getLogin().equals(dataBase.getLogin()) ||
                    checkUser.getPassword().equals(dataBase.getPassword())) {
                throw new PersistenceException();
            }
            return null;
        }).when(userRepository).loginUser(checkUser);
        assertThrows(ProjectException.class, () -> userService.loginUser(checkUser));
    }

    //успешная смена пароля
    @Test
    void changePassword_oldPasswordMatchWithNewPassword_change() {
        User checkUser = new User("login", "login");
        User dataBase = new User("login", "login");
        Mockito.doAnswer(invocationOnMock -> {
            if (checkUser.getLogin().equals(dataBase.getLogin()) &&
                    checkUser.getPassword().equals(dataBase.getPassword())) {
                return checkUser;
            }
            throw new PersistenceException();
        }).when(userRepository).loginUser(checkUser);
        User result = userRepository.loginUser(checkUser);
        userRepository.changePassword(result);

        Mockito.verify(userRepository).changePassword(result);
    }
    // не удалось сменить пароль
    @Test
    void changePassword_oldPasswordNotMatchWithNewPassword_exception() {
        User checkUser = new User("login", "login");
        User dataBase = new User("login1", "login");
        Mockito.doAnswer(invocationOnMock -> {
            if (checkUser.getLogin().equals(dataBase.getLogin()) &&
                    checkUser.getPassword().equals(dataBase.getPassword())) {
                return checkUser;
            }
            throw new PersistenceException();
        }).when(userRepository).loginUser(checkUser);

        assertThrows(ProjectException.class, () -> userService.changePassword(checkUser));
    }
}