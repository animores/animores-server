package animores.serverapi.to_do.entity;

import animores.serverapi.pet.entity.Pet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Getter
public class PetToDoRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Pet pet;

    @ManyToOne
    private ToDo toDo;

    public PetToDoRelationship(Pet pet, ToDo toDo) {
        this.pet = pet;
        this.toDo = toDo;
    }
}
