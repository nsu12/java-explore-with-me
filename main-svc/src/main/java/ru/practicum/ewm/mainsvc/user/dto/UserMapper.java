package ru.practicum.ewm.mainsvc.user.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.mainsvc.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {
    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(), user.getName(), user.getEmail()
        );
    }

    public static List<UserDto> toDto(List<User> users) {
        if (users == null || users.isEmpty()) return Collections.emptyList();
        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public static UserShortDto toShortDto(User user) {
        return new UserShortDto(
                user.getId(), user.getName()
        );
    }
}
