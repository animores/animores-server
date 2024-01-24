package animores.serverapi.pet.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Builder
public class Breed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Species species;

    private String name;

    private String defaultImageUrl;

    public Breed(Long id, Species species, String name, String defaultImageUrl) {
        this.id = id;
        this.species = species;
        this.name = name;
        this.defaultImageUrl = defaultImageUrl;
    }

}
