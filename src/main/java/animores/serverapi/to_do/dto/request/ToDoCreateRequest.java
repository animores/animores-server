package animores.serverapi.to_do.dto.request;

import animores.serverapi.pet.type.Tag;
import animores.serverapi.to_do.dto.RepeatUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Schema(description = "할 일 생성 요청")
public record ToDoCreateRequest(
    @Schema(description = "프로필 아이디", example = "1")
    @NotNull(message = "프로필 아이디는 필수입니다.")
    Long profileId,
    @Schema(description = "펫 아이디 목록", example = "[1, 2, 3]")
    @NotNull(message = "펫 아이디는 필수입니다.")
    @NotEmpty(message = "펫 아이디는 비어있을 수 없습니다.")
    List<Long> petIds,
    @Schema(description = "내용", example = "할 일 내용")
    String content,
    @Schema(description = "태그")
    Tag tag,
    @Schema(description = "날짜", example = "2021-07-01")
    @NotNull(message = "날짜는 필수입니다.")
    LocalDate date,
    @Schema(description = "시간", example = "10:00")
    LocalTime time,
    @Schema(description = "하루 종일 일정 여부", example = "false")
    boolean isAllDay,
    @Schema(description = "캘린더 색상", example = "#FFFFFF")
    String color,
    @Schema(description = "알람 사용 여부", example = "true")
    boolean isUsingAlarm,
    @Schema(description = "반복 설정")
    Repeat repeat,
    @Schema(description = " 설명")
    String description
) {

    public void validate() {
        if (tag == null && (content == null || content.isBlank())) {
            throw new IllegalArgumentException("태그와 내용 중 하나는 필수입니다.");
        }

        if (!isAllDay && time == null) {
            throw new IllegalArgumentException("시간은 필수입니다.");
        }

        if (isAllDay && repeat != null && repeat.unit() == RepeatUnit.HOUR) {
            throw new IllegalArgumentException("하루 종일 일정일 때 시간 반복 설정은 할 수 없습니다.");
        }
    }


    public record Repeat(
        @Schema(description = "반복 단위")
        RepeatUnit unit,
        @Schema(description = "반복 간격", example = "1")
        Integer interval,
        @Schema(description = "반복 요일 목록, unit이 WEEK일 때만 사용")
        List<String> weekDays,
        @Schema(description = "반복 횟수")
        int repeatNum) {

    }
}
