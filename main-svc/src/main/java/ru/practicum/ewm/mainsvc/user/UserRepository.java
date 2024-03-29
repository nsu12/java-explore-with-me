package ru.practicum.ewm.mainsvc.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.mainsvc.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
