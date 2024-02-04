package animores.serverapi.to_do.entity;

import animores.serverapi.common.BaseEntity;
import animores.serverapi.profile.domain.Profile;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class ToDoInstance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private ToDo todo;
    private LocalDateTime time;
    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "complete_profile_id")
    private Profile completeProfile;
    private LocalDateTime completeTime;

}
