package animores.serverapi.pet.entity;

import animores.serverapi.pet.type.Tag;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class BreedImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Breed breed;

    private String image_url;

    @Enumerated(value = EnumType.STRING)
    private Tag tag;

    protected BreedImage(Long id, Breed breed, String image_url, Tag tag) {
        this.id = id;
        this.breed = breed;
        this.image_url = image_url;
        this.tag = tag;
    }

}
