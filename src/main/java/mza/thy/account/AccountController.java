package mza.thy.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.domain.filter.FilterParams;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Clock;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AccountController {
    private final Sort defaultSort = Sort.by(Sort.Direction.DESC, "id");
    private final AccountService accountService;
    private final Clock clock;

    @GetMapping("account")
    public String getAccountList(Model model,
                                 FilterParams filterParams,
                                 @RequestParam(defaultValue = "id") String sortField,
                                 @RequestParam(required = false) Sort.Direction sortDirection) {
        sortDirection = Optional.ofNullable(sortDirection)
                .orElse(Sort.Direction.ASC);
        model.addAttribute("accountList", accountService.getAccountList(filterParams, Sort.by(sortDirection, sortField)));
        model.addAttribute("accountDto", new AccountDto());
        model.addAttribute("isAscending", sortDirection.isAscending());
        model.addAttribute("filterParams", new FilterParams());

        model.addAttribute("headers", List.of("BANK", "BALANCE"));
        model.addAttribute("rows", accountService.getDepositsRows());
        return "account-list";
    }

    @GetMapping("/account/{id}")
    public String getAccountListWithGivenAccount(Model model, @PathVariable("id") Long id, @RequestParam(required = false) Sort sort) {
        sort = Optional.ofNullable(sort).orElse(Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("accountList", accountService.getAccountList(null, sort));
        model.addAttribute("accountDto", accountService.getAccount(id));

        model.addAttribute("headers", List.of("BANK", "BALANCE"));
        model.addAttribute("rows", accountService.getDepositsRows());
        return "account-list";
    }

    @GetMapping("/account/delete/{id}")
    public String getAccountDeleteConfirmationView(Model model, @PathVariable("id") Long id) {
        model.addAttribute("accountList", accountService.getAccountList(null, defaultSort));
        model.addAttribute("accountDto", accountService.getAccount(id));
        model.addAttribute("error", "");

        model.addAttribute("headers", List.of("BANK", "BALANCE"));
        model.addAttribute("rows", accountService.getDepositsRows());
        return "account-delete";
    }

    @PostMapping("/account/delete/{id}")
    public String deleteAccount(Model model, @PathVariable("id") Long id) {
        var result = accountService.deleteAccount(id);
        if (Objects.isNull(result)) {
            model.addAttribute("accountList", accountService.getAccountList(null, defaultSort));
            model.addAttribute("accountDto", new AccountDto());
            return "account-list";
        }
        model.addAttribute("accountDto", accountService.getAccount(id));
        model.addAttribute("error", result);
        return "account-delete";

    }

    @PostMapping("/account")
    public String saveAccount(Model model, AccountDto accountDto) {
        accountService.saveAccount(accountDto);
        model.addAttribute("accountList", accountService.getAccountList(null, defaultSort));
        model.addAttribute("accountDto", new AccountDto());
        return "account-list";
    }
}
