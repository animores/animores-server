package animores.serverapi.to_do.entity;

import animores.serverapi.account.entity.Account;
import animores.serverapi.common.BaseEntity;
import animores.serverapi.pet.type.Tag;
import animores.serverapi.profile.entity.Profile;
import animores.serverapi.to_do.dto.RepeatUnit;
import animores.serverapi.to_do.dto.WeekDay;
import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import animores.serverapi.to_do.dto.request.ToDoPatchRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        toDo.resolveTag(request);
        toDo.color = request.color();
        toDo.isUsingAlarm = request.isUsingAlarm();
        if(request.repeat() != null) {
            toDo.unit = request.repeat().unit();
            toDo.intervalNum = request.repeat().interval();
            toDo.weekDays = request.repeat().weekDays();
        }
        return toDo;
    }

    public void setPetToDoRelationships(List<PetToDoRelationship> petToDoRelationships) {
        this.petToDoRelationships = petToDoRelationships;
    }

    public void update(ToDoPatchRequest request) {
        resolveTag(this, request);
        this.date = request.date() == null ? this.date : request.date();
        this.time = request.time() == null ? this.time : request.time();
        this.isAllDay = request.isAllDay() == null ? this.isAllDay : request.isAllDay();
        this.color = request.color() == null ? this.color : request.color();
        this.isUsingAlarm = request.isUsingAlarm() == null ? this.isUsingAlarm : request.isUsingAlarm();
    }

    public ToDoInstance getNextToDoInstance() {
        if (getNextTime() == null) {
            return null;
        }

        return ToDoInstance.builder()
                .toDo(this)
                .date(getNextTime().toLocalDate())
                .time(getNextTime().toLocalTime())
                .build();
    }

    private void resolveTag(ToDoCreateRequest request) {
        if(request.tag() != null) {
            this.tag = request.tag();
        } else {
            this.content = request.content();
        }
    }

    private void resolveTag(ToDo toDo, ToDoPatchRequest request) {
        if(request.tag() != null) {
            toDo.tag = request.tag();
        } else {
            toDo.content = request.content();
        }
    }

    public LocalDateTime getNextTime() {
        LocalDateTime targetDateTime = LocalDateTime.of(date, time);
        while (targetDateTime.isBefore(LocalDateTime.now())) {
            switch (unit) {
                case DAY:
                    targetDateTime = targetDateTime.plusDays(intervalNum);
                    break;
                case WEEK:
                    LocalDateTime lastDayOfLastWeek = targetDateTime.minusDays(targetDateTime.getDayOfWeek().getValue());
                    for (WeekDay weekDay : WeekDay.values()) {
                        if(this.weekDays.contains(weekDay) && lastDayOfLastWeek.plusDays(weekDay.getValue()).isAfter(LocalDateTime.now())) {
                            return lastDayOfLastWeek.plusDays(weekDay.getValue());
                        }
                    }

                    targetDateTime = targetDateTime.plusWeeks(intervalNum);
                    break;
                case MONTH:
                    targetDateTime = targetDateTime.plusMonths(intervalNum);
                    break;
                case YEAR:
                    targetDateTime = targetDateTime.plusYears(intervalNum);
                    break;
                default:
                    return null;
            }
        }

        return targetDateTime;
    }
}