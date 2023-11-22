package ru.practicum.ewm.mainsvc.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.mainsvc.error.EntryNotFoundException;
import ru.practicum.ewm.mainsvc.user.dto.NewUserDto;
import ru.practicum.ewm.mainsvc.user.dto.UserDto;
import ru.practicum.ewm.mainsvc.user.dto.UserMapper;
import ru.practicum.ewm.mainsvc.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    @Transactional
    public UserDto addUser(NewUserDto userInDto) {
        User user = new User();
        user.setName(userInDto.getName());
        user.setEmail(userInDto.getEmail());
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getUsers(List<Long> userIds, Integer from, Integer size) {
        List<User> users;
        if (userIds != null && !userIds.isEmpty()) {
            users = userRepository.findAllById(userIds);
        } else {
            users = userRepository.findAll(
                    PageRequest.of(from/size, size, Sort.by(Sort.Direction.ASC, "id"))
            ).toList();
        }
        return UserMapper.toDto(users);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntryNotFoundException(
                                String.format("User with id=%d was not found", userId)
                        )
                );
        userRepository.deleteById(userId);
    }
}
