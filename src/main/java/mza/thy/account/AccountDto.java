package mza.thy.account;

import lombok.*;
import mza.thy.domain.Account;

import java.util.Optional;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class AccountDto {
    private Long id;
    private String name;
    private String bank;

    Account convert() {
        return Account.builder()
                .id(id)
                .name(name)
                .bank(bank)
                .build();
    }

    public static AccountDto convertToDto(Account in) {
        return Optional.ofNullable(in).map(a ->
                        AccountDto.builder()
                                .id(a.getId())
                                .name(a.getName())
                                .bank(a.getBank())
                                .build())
                .orElse(null);
    }
}
