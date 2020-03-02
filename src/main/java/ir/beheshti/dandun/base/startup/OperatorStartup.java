package ir.beheshti.dandun.base.startup;

import ir.beheshti.dandun.base.user.entity.UserEntity;
import ir.beheshti.dandun.base.user.repository.UserRepository;
import ir.beheshti.dandun.base.user.util.UserType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OperatorStartup implements Insert {

    private final UserRepository userRepository;

    public OperatorStartup(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private void insertOperator() {
        List<UserEntity> userEntityList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserEntity operator = new UserEntity();
            operator.setUserType(UserType.Operator);
            operator.setPhoneNumber("operator" + i);
            operator.setPassword("operator" + i);
            operator.setVerified(true);
            operator.setSignedUp(true);
            userEntityList.add(operator);
        }
        userRepository.saveAll(userEntityList);
    }

    @Override
    public void insert() {
        insertOperator();
    }
}
