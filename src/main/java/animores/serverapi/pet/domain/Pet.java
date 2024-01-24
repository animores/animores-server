package animores.serverapi.pet.domain;

import animores.serverapi.account.domain.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Builder
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    private String name;

    private LocalDate birthday;

    private int gender;

    // 커스텀 요소 보류

    public Pet(Long id, Account account, String name, LocalDate birthday, int gender) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
    }

}
