package mza.thy.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.domain.filter.FilterParams;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AccountController {
    private final Sort defaultSort = Sort.by(Sort.Direction.DESC, "id");
    private final AccountService accountService;

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
        return "account-list";
    }

    @GetMapping("/account/{id}")
    public String getAccountListWithGivenAccount(Model model, @PathVariable("id") Long id, @RequestParam(required = false) Sort sort) {
        sort = Optional.ofNullable(sort).orElse(Sort.by(Sort.Direction.ASC, "id"));
        model.addAttribute("accountList", accountService.getAccountList(null, sort));
        model.addAttribute("accountDto", accountService.getAccount(id));
        return "account-list";
    }

    @GetMapping("/account/delete/{id}")
    public String getAccountDeleteConfirmationView(Model model, @PathVariable("id") Long id) {
        model.addAttribute("accountList", accountService.getAccountList(null, defaultSort));
        model.addAttribute("accountDto", accountService.getAccount(id));
        return "account-delete";
    }

    @PostMapping("/account/delete/{id}")
    public String deleteAccount(Model model, @PathVariable("id") Long id) {
        accountService.deleteAccount(id);
        model.addAttribute("accountList", accountService.getAccountList(null, null));
        model.addAttribute("accountDto", new AccountDto());
        return "account-list";
    }

    @PostMapping("/account")
    public String saveAccount(Model model, AccountDto accountDto) {
        accountService.saveAccount(accountDto);
        model.addAttribute("accountList", accountService.getAccountList(null, null));
        model.addAttribute("accountDto", new AccountDto());
        return "account-list";
    }


    private void extracted(Model model, Integer pageNumber, Integer pageSize) {
        var page = accountService.getAccountPage(PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "date"));
        model.addAttribute("incomeList", page.stream().toList());
        model.addAttribute("incomeDto", new AccountDto());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("incomePage", page);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("defDate", LocalDate.now());
    }
}
