package mza.thy.health;

import lombok.*;
import mza.thy.domain.Cure;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CureDto {
    private Long id;
    private BigDecimal price = BigDecimal.ZERO;
    private String medicine;
    private String description;
    private String dose;
    private Long illnessId;

    Cure convert() {
        return Cure.builder()
                .id(id)
                .price(price)
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
                .medicine(in.getMedicine())
                .description(in.getDescription())
                .dose(in.getDose())
                .illnessId(in.getIllnessId())
                .build();
    }
}
