package animores.serverapi.diary.entity;

import animores.serverapi.diary.dto.AddDiaryRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "diary")
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Profile 연결 예정

    // List<DiaryImage> 연결 예정

    // List<DiaryComment> 연결 예정

    @Lob
    @Column(nullable = false)
    private String content;


    @Column(updatable = false)
    @CreatedDate
    private LocalDateTime createdDt;

    @LastModifiedDate
    private LocalDateTime updatedDt;

    private LocalDateTime deletedDt;

    public static Diary create(AddDiaryRequest addDiaryRequest) {
        return Diary.builder()
            .content(addDiaryRequest.content())
            .build();
    }

}
