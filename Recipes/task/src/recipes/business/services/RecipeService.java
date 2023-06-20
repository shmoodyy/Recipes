package recipes.business.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import recipes.business.models.recipe.RecipeDomain;
import recipes.business.models.recipe.RecipeEntity;
import recipes.business.models.recipe.RecipeIDDomain;
import recipes.persistence.RecipeRepository;
import recipes.persistence.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecipeService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RecipeRepository recipeRepository;

    @Autowired
    private final ModelMapper serviceModelMapper;

    public RecipeIDDomain save(String email, RecipeDomain recipeDomain) {
        var entity = convertRecipeDomainToEntity(recipeDomain);
        userRepository.findUserByEmailIgnoreCase(email).getRecipeEntities().add(entity);
        return convertEntityToIdDomain(recipeRepository.save(entity));
    }

    public Optional<RecipeDomain> findRecipeById(Long id) {
        return convertEntityToRecipeDomain(recipeRepository.findRecipeById(id));
    }

    public Optional<RecipeDomain> deleteById(String email, Long id) {
        Set<RecipeEntity> userRecipeSet = userRepository.findUserByEmailIgnoreCase(email).getRecipeEntities();
        var potentialAuthoredRecipe = potentialAuthoredRecipe(userRecipeSet, id);
        if (potentialAuthoredRecipe.isPresent()) {
            var recipeEntity = potentialAuthoredRecipe.get();
            userRecipeSet.remove(recipeEntity);
            recipeRepository.deleteById(id);
            return convertEntityToRecipeDomain(recipeEntity);
        }
        return Optional.empty();
    }

    public Optional<RecipeDomain> updateRecipeById(String email, Long id, RecipeDomain recipeDomain) {
        Set<RecipeEntity> userRecipeSet = userRepository.findUserByEmailIgnoreCase(email).getRecipeEntities();
        var potentialAuthoredRecipe = potentialAuthoredRecipe(userRecipeSet, id);
        if (potentialAuthoredRecipe.isPresent()) {
            Optional<RecipeEntity> potentialRecipeEntity = recipeRepository.findById(id);
            if (potentialRecipeEntity.isPresent()) {
                Optional<RecipeDomain> potentialRecipeDomain = convertEntityToRecipeDomain(potentialRecipeEntity.get());
                if (potentialRecipeDomain.isPresent()) {
                    RecipeDomain foundRecipeDomain = potentialRecipeDomain.get();
                    foundRecipeDomain.setName(recipeDomain.getName());
                    foundRecipeDomain.setCategory(recipeDomain.getCategory());
                    foundRecipeDomain.setDate(recipeDomain.getDate());
                    foundRecipeDomain.setDescription(recipeDomain.getDescription());
                    foundRecipeDomain.setIngredients(recipeDomain.getIngredients());
                    foundRecipeDomain.setDirections(recipeDomain.getDirections());
                    recipeRepository.save(convertRecipeDomainToEntity(foundRecipeDomain));
                }
            }
            return convertEntityToRecipeDomain(potentialAuthoredRecipe.get());
        } else {
            return Optional.empty();
        }
    }

    public List<RecipeDomain> findRecipeByName(String name) {
        return recipeRepository.findByNameIgnoreCaseContainsOrderByDateDesc(name).stream()
                .map(this::convertEntityToRecipeDomain)
                .flatMap(Optional::stream)
                .toList();
    }

    public List<RecipeDomain> findRecipeByCategory(String category) {
        return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category).stream()
                .map(this::convertEntityToRecipeDomain)
                .flatMap(Optional::stream)
                .toList();
    }

    public Optional<RecipeEntity> potentialAuthoredRecipe(Set<RecipeEntity> recipeEntitySet, Long id) {
        return recipeEntitySet.stream()
                .filter(recipe -> recipe.getId() == id)
                .findFirst();
    }

    public boolean recipeDoesntExist(Long id) {
        return !recipeRepository.existsById(id);
    }

    public Optional<RecipeDomain> convertEntityToRecipeDomain(RecipeEntity recipeEntity) {
        return Optional.ofNullable(recipeEntity).map(entity -> serviceModelMapper.map(entity, RecipeDomain.class));
    }

    public RecipeIDDomain convertEntityToIdDomain(RecipeEntity recipeEntity) {
        return serviceModelMapper.map(recipeEntity, RecipeIDDomain.class);
    }

    public RecipeEntity convertRecipeDomainToEntity(RecipeDomain recipeDomain) {
        return serviceModelMapper.map(recipeDomain, RecipeEntity.class);
    }
}