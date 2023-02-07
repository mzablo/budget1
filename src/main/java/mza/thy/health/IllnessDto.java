package mza.thy.health;

import lombok.*;
import mza.thy.domain.Account;
import mza.thy.domain.Illness;
import mza.thy.domain.Operation;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Optional;

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
        return Illness.builder()
                .id(id)
                .date(date)
                .name(name)
                .description(description)
                .who(who)
                .free(free)
                .build();
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
