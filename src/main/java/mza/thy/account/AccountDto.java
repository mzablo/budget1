package mza.thy.account;

import lombok.*;
import mza.thy.domain.Account;

import java.math.BigDecimal;
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
    private String balance;

    Account convert() {
        return Account.builder()
                .id(id)
                .name(name)
                .bank(bank)
                .build();
    }

    public static AccountDto convertToDto(Account in, String balance) {
        return Optional.ofNullable(in).map(a ->
                        AccountDto.builder()
                                .id(a.getId())
                                .name(a.getName())
                                .bank(a.getBank())
                                .balance(balance)
                                .build())
                .orElse(null);
    }
}
