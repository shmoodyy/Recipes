package recipes.business.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.business.models.user.UserDomain;
import recipes.business.models.user.UserEntity;
import recipes.persistence.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ModelMapper serviceModelMapper;

    public void register(UserDomain userDomain) {
        userRepository.save(convertUserDomainToEntity(userDomain));
    }

    public UserEntity convertUserDomainToEntity(UserDomain userDomain) {
        return serviceModelMapper.map(userDomain, UserEntity.class);
    }
}
