package recipes.presentation;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import recipes.business.models.user.UserDTO;
import recipes.business.models.user.UserDomain;
import recipes.business.services.UserService;
import recipes.persistence.UserRepository;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final PasswordEncoder encoder;

    @Autowired
    private final ModelMapper controllerModelMapper;

    @PostMapping("/api/register")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserDTO userDTO) {
        System.out.println("user = " + userDTO);
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest().build();
        } else {
            userDTO.setPassword(encoder.encode(userDTO.getPassword()));
            userService.register(convertUserDTOToUserDomain(userDTO));
            return ResponseEntity.ok().build();
        }
    }

    public UserDomain convertUserDTOToUserDomain(UserDTO userDTO) {
        return controllerModelMapper.map(userDTO, UserDomain.class);
    }
}
