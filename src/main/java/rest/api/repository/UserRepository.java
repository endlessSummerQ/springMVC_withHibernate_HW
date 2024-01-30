package rest.api.repository;
import rest.api.entity.User;
import java.util.List;

public interface UserRepository {
    public List<User> getUserList(); //список всех юзеров

    public User getUser(int id); //один юзер

    public void addUser(User user); //добавить юзера(регистрация)

    public User loginUser(User user); //вход по логину и паролю

    public void changePassword(User user); //изменение пароля
}
