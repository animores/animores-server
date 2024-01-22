package animores.serverapi.to_do.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record ToDoDetailResponse(
        Long id,
        String title,
        List<PetResponse> pets,
        Boolean isAllDay,
        LocalDate date,
        LocalTime time,
        Boolean hasAlarm,
        String repeatString,
        String color,
        String completeProfileImage,
        LocalDateTime completeDateTime
) {
}
