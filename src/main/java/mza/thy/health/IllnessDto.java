package mza.thy.health;

import lombok.*;
import mza.thy.domain.Illness;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class IllnessDto {
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date = LocalDate.now();
    private String name;
    private String description;
    private String who;
    private Integer free = 0;

    Illness convert() {
        var illness = new Illness();
        illness.setDate(date);
        illness.setName(name);
        illness.setDescription(description);
        illness.setWho(who);
        illness.setFree(free);
        return illness;
    }

    static IllnessDto convert(Illness in) {
        return IllnessDto.builder()
                .id(in.getId())
                .date(in.getDate())
                .name(in.getName())
                .description(in.getDescription())
                .who(in.getWho())
                .free(in.getFree())
                .build();
    }
}
