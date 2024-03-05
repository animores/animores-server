package animores.serverapi.to_do.entity;

import animores.serverapi.common.BaseEntity;
import animores.serverapi.profile.domain.Profile;
import animores.serverapi.to_do.dto.Repeat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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


    public static List<ToDoInstance> fromToDo(ToDo toDo, LocalDate date, LocalTime time, Repeat repeat) {
        //TODO: Implement this method
        return null;
    }
}
