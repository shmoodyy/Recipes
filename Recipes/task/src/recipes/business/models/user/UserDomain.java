package recipes.business.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import recipes.business.models.recipe.RecipeEntity;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDomain {
    private long id;
    private String email;
    private String password;
    private Set<RecipeEntity> recipeEntities;
}