package animores.serverapi.to_do.entity;

import animores.serverapi.common.BaseEntity;
import animores.serverapi.profile.entity.Profile;
import animores.serverapi.to_do.dto.RepeatUnit;
import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ToDoInstance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private ToDo toDo;
    private LocalDate date;
    private LocalTime time;
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "complete_profile_id")
    private Profile completeProfile;
    private LocalDateTime completeTime;


    public static List<ToDoInstance> fromToDoAndRepeat(ToDo toDo, ToDoCreateRequest.Repeat repeat) {
        if (repeat.unit().equals(RepeatUnit.WEEK)) {
            LocalDateTime startTime = LocalDateTime.of(toDo.getDate(), toDo.getTime());

            if (repeat.weekDays() == null || repeat.weekDays().isEmpty()) {
                return IntStream.range(0,repeat.repeatNum())
                        .mapToObj(num ->
                                ToDoInstance.builder()
                                        .date(startTime.toLocalDate().plusWeeks((long) toDo.intervalNum * num))
                                        .time(startTime.toLocalTime())
                                        .build()
                        ).toList();
            } else if (!toDo.weekDays.contains(startTime.getDayOfWeek())) {
                throw new IllegalArgumentException("해당 날짜의 요일을 요청에 포함해야합니다.");
            }
            List<DayOfWeek> dayOfWeeks = toDo.weekDays.stream().sorted(
                    Comparator.naturalOrder()
            ).toList();
            int dayOfWeekIndex = dayOfWeeks.indexOf(startTime.getDayOfWeek());

            List<ToDoInstance> instances = new LinkedList<>();
            LocalDate endOfLastWeek = startTime.toLocalDate().minusDays(startTime.getDayOfWeek().getValue());
            while (instances.size() < repeat.repeatNum()) {
                if (dayOfWeekIndex >= dayOfWeeks.size()) {
                    dayOfWeekIndex = 0;
                    endOfLastWeek = endOfLastWeek.plusDays(7L * toDo.intervalNum);
                }

                ToDoInstance toDoInstance =
                        ToDoInstance.builder()
                                .date(endOfLastWeek.plusDays(dayOfWeeks.get(dayOfWeekIndex).getValue()))
                                .time(startTime.toLocalTime())
                                .build();
                instances.add(toDoInstance);
                dayOfWeekIndex++;
            }

            return instances;
        } else {
            LocalDateTime startTime = LocalDateTime.of(toDo.getDate(), toDo.getTime());
            return IntStream.range(0,repeat.repeatNum())
                    .mapToObj(
                            num -> {
                                LocalDateTime thistime;
                                switch (repeat.unit()) {
                                    case HOUR:
                                        thistime = startTime.plusHours((long) num * toDo.getIntervalNum());
                                        break;
                                    case DAY:
                                        thistime = startTime.plusDays((long) num * toDo.getIntervalNum());
                                        break;
                                    case MONTH:
                                        thistime = startTime.plusMonths((long) num * toDo.getIntervalNum());
                                        break;
                                    default:
                                        throw new IllegalArgumentException("잘못된 입력입니다.");
                                }

                                return ToDoInstance.builder()
                                    .toDo(toDo)
                                    .date(thistime.toLocalDate())
                                    .time(thistime.toLocalTime())
                                    .build();
                            }
                    ).toList();
        }
    }

    public void setComplete(Profile profile) {
        this.completeProfile = profile;
        this.completeTime = LocalDateTime.now();
    }
}
