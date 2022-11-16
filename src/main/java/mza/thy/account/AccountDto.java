package mza.thy.account;

import lombok.*;
import mza.thy.domain.Account;
import mza.thy.domain.Income;

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

    static AccountDto convert(Account in) {
        return AccountDto.builder()
                .id(in.getId())
                .name(in.getName())
                .bank(in.getBank())
                .build();
    }
}
