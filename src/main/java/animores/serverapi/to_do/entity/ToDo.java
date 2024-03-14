package animores.serverapi.to_do.entity;

import animores.serverapi.account.domain.Account;
import animores.serverapi.common.BaseEntity;
import animores.serverapi.pet.type.Tag;
import animores.serverapi.profile.domain.Profile;
import animores.serverapi.to_do.dto.RepeatUnit;
import animores.serverapi.to_do.dto.WeekDay;
import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ToDo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "toDo", cascade = CascadeType.ALL)
    private List<PetToDoRelationship> petToDoRelationships;
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
    @Enumerated(EnumType.STRING)
    RepeatUnit unit;
    Integer interval;
    @ElementCollection(targetClass = WeekDay.class)
    @CollectionTable(name = "to_do_week_day", joinColumns = @JoinColumn(name = "to_do_id"))
    @Column(name = "week_day")
    @Enumerated(EnumType.STRING)
    List<WeekDay> weekDays;

    public static ToDo fromRequest(ToDoCreateRequest request, Account account, Profile createProfile) {
        ToDo toDo = new ToDo();
        toDo.account = account;
        toDo.createProfile = createProfile;
        toDo.date = request.date();
        toDo.time = request.time();
        toDo.isAllDay = request.isAllDay();
        toDo.resolveTag(toDo, request);
        toDo.color = request.color();
        toDo.isUsingAlarm = request.isUsingAlarm();
        toDo.unit = request.repeat().unit();
        toDo.interval = request.repeat().interval();
        toDo.weekDays = request.repeat().weekDays();
        return toDo;
    }

    protected void setPetToDoRelationships(List<PetToDoRelationship> petToDoRelationships) {
        this.petToDoRelationships = petToDoRelationships;
    }

    private void resolveTag(ToDo toDo, ToDoCreateRequest request) {
        if(request.tag() != null) {
            toDo.tag = request.tag();
        } else {
            toDo.content = request.content();
        }
    }
}