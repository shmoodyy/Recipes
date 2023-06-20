package recipes.business.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import recipes.business.models.recipe.RecipeEntity;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private long id;

    @Column
    private String email;

    @Column
    private String password;

    @ElementCollection
    private Set<RecipeEntity> recipeEntities;
}