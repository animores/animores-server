package animores.serverapi.to_do.entity;

import animores.serverapi.account.domain.Account;
import animores.serverapi.common.BaseEntity;
import animores.serverapi.pet.type.Tag;
import animores.serverapi.profile.domain.Profile;
import animores.serverapi.to_do.dto.Repeat;
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
    private Long id;
    @OneToMany(mappedBy = "toDo", cascade = CascadeType.ALL)
    private List<PetToDoRelationship> pets;
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "create_profile_id")
    private Profile createProfile;
    private LocalDate date;
    private LocalTime time;
    private boolean isAllDay;
    private String content;
    @Enumerated(value = EnumType.STRING)
    private Tag tag;
    private String color;
    private boolean isUsingAlarm;
    //반복에 대한 내용 추가
    private Repeat repeat;

    public static ToDo fromRequest(ToDoCreateRequest request, Account account, Profile createProfile) {
        ToDo toDo = new ToDo();
        toDo.account = account;
        toDo.createProfile = createProfile;
        toDo.date = request.date();
        toDo.time = request.time();
        toDo.isAllDay = request.isAllDay();
        if(request.tag() != null) {
            toDo.tag = request.tag();
        } else {
            toDo.content = request.content();
        }
        toDo.color = request.color();
        toDo.isUsingAlarm = request.isUsingAlarm();
        toDo.repeat = request.repeat();
        return toDo;
    }
}