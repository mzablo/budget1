package mza.thy.health;

import lombok.*;
import mza.thy.domain.Cure;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CureDto {
    private Long id;
    private BigDecimal price = BigDecimal.ZERO;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date = LocalDate.now();
    private String medicine;
    private String description;
    private String dose;
    private Long illnessId;

    Cure convert() {
        return Cure.builder()
                .id(id)
                .price(price)
                .date(date)
                .medicine(medicine)
                .description(description)
                .dose(dose)
                .illnessId(illnessId)
                .build();
    }

    static CureDto convert(Cure in) {
        return CureDto.builder()
                .id(in.getId())
                .price(in.getPrice())
                .date(in.getDate())
                .medicine(in.getMedicine())
                .description(in.getDescription())
                .dose(in.getDose())
                .illnessId(in.getIllnessId())
                .build();
    }
}
