package recipes.business.models.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDomain {
    private long id;
    private String name;
    private String category;
    private LocalDateTime date;
    private String description;
    private String[] ingredients;
    private String[] directions;
}