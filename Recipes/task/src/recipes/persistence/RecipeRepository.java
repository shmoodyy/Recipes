package recipes.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.business.models.recipe.RecipeEntity;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<RecipeEntity, Long> {
    RecipeEntity findRecipeById(Long id);
    List<RecipeEntity> findByNameIgnoreCaseContainsOrderByDateDesc(String name);
    List<RecipeEntity> findByCategoryIgnoreCaseOrderByDateDesc(String category);
}