package animores.serverapi.to_do.entity;

import animores.serverapi.common.BaseEntity;
import animores.serverapi.user.entity.Profile;
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
    @Column(name = "complete_profile_id")
    private Profile completeProfile;
    private LocalDateTime completeTime;

}