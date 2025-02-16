package animores.serverapi.diary.dto;

import lombok.Getter;

@Getter
public class GetCalendarResponse {

    private int day;

    private boolean todoYn;
    private boolean diaryYn;
}