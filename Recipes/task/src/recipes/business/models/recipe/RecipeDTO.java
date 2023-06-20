package recipes.business.models.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String category;

    private LocalDateTime date = LocalDateTime.now();

    @NotBlank
    private String description;

    @NotEmpty
    private String[] ingredients;

    @NotEmpty
    private String[] directions;
}