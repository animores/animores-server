package animores.serverapi.to_do.dao;

import animores.serverapi.pet.type.Tag;
import animores.serverapi.to_do.dto.RepeatUnit;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;

@Getter
public class GetTodosDao {

    private Long id;
    private LocalDate date;
    private LocalTime time;
    private Boolean isAllDay;
    private String content;
    private Tag tag;
    private String color;
    private Boolean isUsingAlarm;
    private RepeatUnit unit;
    private Integer intervalNum;
    private List<GetTodosPetDao> pets;
//    private List<WeekDay> weekDays;


    @QueryProjection
    public GetTodosDao(
        Long id,
        LocalDate date,
        LocalTime time,
        Boolean isAllDay,
        String content,
        Tag tag,
        String color,
        Boolean isUsingAlarm,
        RepeatUnit unit,
        Integer intervalNum,
        List<GetTodosPetDao> pets
    ) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.isAllDay = isAllDay;
        this.content = content;
        this.tag = tag;
        this.color = color;
        this.isUsingAlarm = isUsingAlarm;
        this.unit = unit;
        this.intervalNum = intervalNum;
        this.pets = pets;
    }
}
