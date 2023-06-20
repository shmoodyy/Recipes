package recipes.presentation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;
import recipes.business.models.recipe.RecipeDTO;
import recipes.business.models.recipe.RecipeDomain;
import recipes.business.models.recipe.RecipeIDDTO;
import recipes.business.models.recipe.RecipeIDDomain;
import recipes.business.services.RecipeService;

import javax.validation.Valid;
import java.util.*;

@RequestMapping("/api/recipe")
@RestController
public class RecipesController {

    @Autowired
    RecipeService recipeService;

    @Autowired
    ModelMapper controllerModelMapper;

    @PostMapping("/new")
    public ResponseEntity<RecipeIDDTO> postRecipeForId(@Valid @RequestBody RecipeDTO recipeDTO
            , @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        var domain = convertDTOToDomain(recipeDTO);
        var idDto = convertDomainToIdDTO(recipeService.save(email, domain));
        return ResponseEntity.ok(idDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> getRecipeById(@PathVariable Long id) {
        var potentialRecipeDTO = recipeService.findRecipeById(id).flatMap(this::convertDomainToRecipeDTO);
        return ResponseEntity.of(potentialRecipeDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipeById(@PathVariable Long id
            , @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        if(recipeService.recipeDoesntExist(id)) return ResponseEntity.notFound().build();
        if (recipeService.deleteById(username, id).isPresent()) {
            return ResponseEntity.noContent().build();
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRecipeById(@PathVariable Long id, @Valid @RequestBody RecipeDTO recipeDTO
            , @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        if (recipeService.recipeDoesntExist(id)) return ResponseEntity.notFound().build();
        if (recipeService.updateRecipeById(username, id, convertDTOToDomain(recipeDTO)).isPresent()) {
            return ResponseEntity.noContent().build();
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RecipeDTO>> searchRecipesByNameOrCategory(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name) {

        if ((name == null && category == null) || (name != null && category != null)) {
            return ResponseEntity.badRequest().build();
        }

        List<RecipeDTO> recipes;

        if (name != null) {
            recipes = recipeService.findRecipeByName(name).stream()
                    .map(this::convertDomainToRecipeDTO)
                    .flatMap(Optional::stream)
                    .toList();
        } else {
            recipes = recipeService.findRecipeByCategory(category).stream()
                    .map(this::convertDomainToRecipeDTO)
                    .flatMap(Optional::stream)
                    .toList();
        }

        return ResponseEntity.ok(recipes);
    }

    public Optional<RecipeDTO> convertDomainToRecipeDTO(RecipeDomain recipeDomain) {
        return Optional.ofNullable(controllerModelMapper.map(recipeDomain, RecipeDTO.class));
    }

    public RecipeIDDTO convertDomainToIdDTO(RecipeIDDomain idDomain) {
        return controllerModelMapper.map(idDomain, RecipeIDDTO.class);
    }

    public RecipeDomain convertDTOToDomain(RecipeDTO recipeDTO) {
        return controllerModelMapper.map(recipeDTO, RecipeDomain.class);
    }
}