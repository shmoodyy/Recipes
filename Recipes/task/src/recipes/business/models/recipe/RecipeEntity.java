package recipes.business.models.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RECIPES")
@Embeddable
public class RecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column
    private String name;

    @Column
    private String category;

    @Column
    private LocalDateTime date;

    @Column
    private String description;

    @Column
    private String[] ingredients;

    @Column
    private String[] directions;
}