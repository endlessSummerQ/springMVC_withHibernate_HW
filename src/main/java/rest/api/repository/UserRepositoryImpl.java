package rest.api.repository;

import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import rest.api.entity.User;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getUserList() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from User", User.class).getResultList();
    }

    @Override
    public User getUser(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from User where id=:id", User.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public void addUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(user);
    }

    @Override
    public User loginUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from User where login=:login and password=:password", User.class)
                .setParameter("login", user.getLogin())
                .setParameter("password", user.getPassword())
                .getSingleResult();
    }

    @Override
    public void changePassword(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.get(User.class, user.getId()).setPassword(user.getNewPassword());
    }
}
