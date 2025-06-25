package com.example.scheduler.domain.repository;

import com.example.scheduler.domain.model.Credential;
import com.example.scheduler.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    /**
     * Метод для поиска нового пользователя
     *
     * @param username имя пользователя, по которому осуществляется поиск
     * @return Optional с объектом {@link User}, если пользователь найден; иначе — пустой Optional
     */
    Optional<User> findByUsername(String username);

    /**
     * Метод для сохранения нового пользователя
     *
     * @param username имя пользователя
     * @param password хэшированный пароль пользователя
     * @param email    адрес электронной почты пользователя
     * @return созданный и сохранённый объект {@link User}
     */
    User save(String username, String password, String email);

    /**
     * Метод для получения учетных данных пользователя
     *
     * @param username имя пользователя, для которого нужно получить учетные данные
     * @return Optional с объектом {@link Credential}, если учётные данные найдены; иначе — пустой Optional
     */
    Optional<Credential> getCredential(String username);
}