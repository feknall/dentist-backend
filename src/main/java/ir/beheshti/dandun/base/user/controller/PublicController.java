package ir.beheshti.dandun.base.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import ir.beheshti.dandun.base.user.dto.user.UserInfoOutputDto;
import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Public", description = "Public information")
@RestController
@RequestMapping(path = "/api/v1/public")
public class PublicController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(path = "/user")
    public List<UserInfoOutputDto> getUserEntities() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities
                .stream()
                .map(user -> {
                    UserInfoOutputDto dto = new UserInfoOutputDto();
                    dto.setPhoneNumber(user.getPhoneNumber());
                    dto.setFirstName(user.getFirstName());
                    dto.setId(user.getId());
                    dto.setLastName(user.getLastName());
                    dto.setUserType(user.getUserType());
                    return dto;
                }).collect(Collectors.toList());
    }
}
