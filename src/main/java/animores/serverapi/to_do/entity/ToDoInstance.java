package animores.serverapi.to_do.entity;

import animores.serverapi.common.BaseEntity;
import animores.serverapi.profile.domain.Profile;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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


    public static ToDoInstance fromToDo(ToDo toDo) {
        return ToDoInstance.builder()
            .toDo(toDo)
            .date(toDo.getDate())
            .time(toDo.getTime())
            .build();
    }

    public void setComplete(Profile profile) {
        this.completeProfile = profile;
        this.completeTime = LocalDateTime.now();
    }
}
