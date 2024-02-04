package animores.serverapi.to_do.entity;

import animores.serverapi.account.domain.Account;
import animores.serverapi.common.BaseEntity;
import animores.serverapi.pet.type.Tag;
import animores.serverapi.profile.domain.Profile;
import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class ToDo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;
    @OneToMany
    private List<PetToDoRelationship> pets;
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "create_profile_id")
    private Profile createProfile;
    @Enumerated(value = EnumType.STRING)
    private Tag tag;
    private String title;
    private LocalDate date;
    private LocalTime time;

    //반복에 대한 내용 추가

    public static ToDo fromRequest(ToDoCreateRequest request, Account account) {
        ToDo toDo = new ToDo();
        toDo.title = request.title();
        toDo.account = account;
        toDo.date = request.date();
        toDo.time = request.time();
        return toDo;
    }
}