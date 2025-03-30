package animores.serverapi.to_do.entity;

import animores.serverapi.account.entity.Account;
import animores.serverapi.common.BaseEntity;
import animores.serverapi.pet.type.Tag;
import animores.serverapi.profile.entity.Profile;
import animores.serverapi.to_do.dto.RepeatUnit;
import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import animores.serverapi.to_do.dto.request.ToDoPatchRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
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

    @OneToMany(mappedBy = "toDo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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
    Integer intervalNum;
    @ElementCollection(targetClass = DayOfWeek.class)
    @CollectionTable(name = "to_do_week_day", joinColumns = @JoinColumn(name = "to_do_id"))
    @Column(name = "week_day")
    @Enumerated(EnumType.STRING)
    @Schema(description = "반복 요일 목록, unit이 WEEK일 때만 사용")
    List<DayOfWeek> weekDays;
    private String description;

    public static ToDo fromRequest(ToDoCreateRequest request, Account account,
        Profile createProfile) {
        ToDo toDo = new ToDo();

        toDo.account = account;
        toDo.createProfile = createProfile;
        toDo.date = request.date();
        toDo.isUsingAlarm = false;
        toDo.isAllDay = request.isAllDay();
        if (toDo.isAllDay()) {
            toDo.time = request.time();
            toDo.isUsingAlarm = request.isUsingAlarm();
        }
        if (request.tag() != null) {
            toDo.tag = request.tag();
        } else {
            toDo.content = request.content();
        }
        toDo.color = request.color();
        if (request.repeat() != null) {
            toDo.unit = request.repeat().unit();
            toDo.intervalNum = request.repeat().interval();
            toDo.weekDays = request.repeat().weekDays()
                    .stream()
                    .map(DayOfWeek::valueOf).toList();
        }
        return toDo;
    }

    public void setPetToDoRelationships(List<PetToDoRelationship> petToDoRelationships) {
        this.petToDoRelationships = petToDoRelationships;
    }

    public void update(ToDoPatchRequest request) {
        if (request.tag() != null) {
            this.tag = request.tag();
        } else {
            this.content = request.content();
        }
        this.date = request.date() == null ? this.date : request.date();
        this.time = request.time() == null ? this.time : request.time();
        this.isAllDay = request.isAllDay() == null ? this.isAllDay : request.isAllDay();
        this.color = request.color() == null ? this.color : request.color();
        this.isUsingAlarm =
            request.isUsingAlarm() == null ? this.isUsingAlarm : request.isUsingAlarm();
        this.description
                = request.description() == null ? this.description : request.description();
    }
}