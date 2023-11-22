package ru.practicum.ewm.mainsvc.user;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainsvc.user.dto.NewUserDto;
import ru.practicum.ewm.mainsvc.user.dto.UserDto;

import java.util.List;

@Transactional(readOnly = true)
public interface UserService {

    @Transactional
    UserDto addUser(NewUserDto userInDto);

    List<UserDto> getUsers(List<Long> userIds, Integer from, Integer size);

    @Transactional
    void deleteUser(Long userId);
}
