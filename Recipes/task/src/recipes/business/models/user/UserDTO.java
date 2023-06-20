package recipes.business.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @Pattern(regexp = ".+@.+\\..+")
    @NotBlank
    private String email;

    @Size(min = 8)
    @NotBlank
    private String password;
}