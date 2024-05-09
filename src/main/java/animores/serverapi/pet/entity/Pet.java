package animores.serverapi.pet.entity;

import animores.serverapi.account.domain.Account;
import animores.serverapi.common.BaseEntity;
import animores.serverapi.pet.dto.request.PetCreateRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;

import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Pet extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    private Breed breed;

    private String name;

    private LocalDate birthday;

    private int gender;

    public static Pet createFromRequest(Account account, PetCreateRequest request, Breed breed) {
        return Pet.builder()
                .account(account)
                .breed(breed)
                .name(request.name())
                .birthday(request.birthday())
                .build();
    }

    public void update(PetCreateRequest request, Breed breed) {
        this.breed = breed;
        this.name = request.name();
        this.birthday = request.birthday();
        this.gender = request.gender();
    }
}
