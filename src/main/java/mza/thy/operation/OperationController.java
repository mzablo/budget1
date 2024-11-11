package mza.thy.operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.account.AccountFacade;
import mza.thy.domain.filter.FilterParams;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OperationController {
    private static final String DEFAULT_NAME = "platnosc karta";
    private static final String VALIDATE = "validate";
    private final int pageNumberDefault = 0;
    private final int pageSizeDefault = 500;
    private final Sort defaultSort = Sort.by(Sort.Direction.DESC, "id");
    private final PageRequest pageableDefault = PageRequest.of(pageNumberDefault, pageSizeDefault, defaultSort);
    private final OperationService operationService;
    private final AccountFacade accountService;
    private final Clock clock;

    @GetMapping("operation")
    public String getOperationList(Model model,
                                   FilterParams filterParams,
                                   @RequestParam(defaultValue = "id") String sortField,
                                   @RequestParam(required = false) Sort.Direction sortDirection) {
        sortDirection = Optional.ofNullable(sortDirection)
                .orElse(Sort.Direction.DESC);
        model.addAttribute("operationList", operationService.getOperationList(filterParams, getPageable(sortField, sortDirection)));
        model.addAttribute("operationDto", getNewDefaultOperation());
        model.addAttribute("isAscending", sortDirection.isAscending());
        model.addAttribute("filterParams", new FilterParams());
        model.addAttribute("accountList", accountService.getAccountList());
        return "operation-list";
    }

    Pageable getPageable(String sortField, Sort.Direction sortDirection) {
        return Optional.ofNullable(sortField)
                .map(s -> PageRequest.of(pageNumberDefault, pageSizeDefault, Sort.by(sortDirection, s)))
                .orElse(pageableDefault);
    }

    @GetMapping("/operation/{id}")
    public String getOperationListWithGivenOperation(Model model, @PathVariable("id") Long id, @RequestParam(required = false) Sort sort) {
        model.addAttribute("operationList", operationService.getOperationList(null, getPageable(null, null)));
        model.addAttribute("operationDto", operationService.getOperation(id));
        model.addAttribute("accountList", accountService.getAccountList());
        return "operation-list";
    }

    @GetMapping("/operation/delete/{id}")
    public String getOperationDeleteConfirmationView(Model model, @PathVariable("id") Long id) {
        model.addAttribute("operationList", operationService.getOperationList(null, getPageable(null, null)));
        model.addAttribute("operationDto", operationService.getOperation(id));
        model.addAttribute("accountList", accountService.getAccountList());
        return "operation-delete";
    }

    @PostMapping("/operation/delete/{id}")
    public String deleteOperation(Model model, @PathVariable("id") Long id) {
        operationService.deleteOperation(id);
        model.addAttribute("operationList", operationService.getOperationList(null, getPageable(null, null)));
        model.addAttribute("operationDto", getNewDefaultOperation());
        model.addAttribute("accountList", accountService.getAccountList());
        return "operation-list";
    }

    @PostMapping("/operation")
    public String saveOperation(Model model, OperationDto operationDto, BindingResult bindingResult,
                                @RequestParam(value = "action", required = false) String action) {
        if (validate(action) && operationService.isAmountAlreadyExist(operationDto)) {
            ObjectError error = new ObjectError("globalError", " Amount already exists: " + operationDto.getAmount());
            bindingResult.addError(error);
            model.addAttribute("accountList", accountService.getAccountList());
            return "operation-list";
        }
        operationService.saveOperation(operationDto);
        model.addAttribute("operationList", operationService.getOperationList(null, getPageable(null, null)));
        model.addAttribute("operationDto", getNewDefaultOperation());
        model.addAttribute("accountList", accountService.getAccountList());
        return "operation-list";
    }

    private boolean validate(String action) {
        return Optional.ofNullable(action)
                .map(VALIDATE::equals)
                .orElse(false);
    }

    private OperationDto getNewDefaultOperation() {
        return OperationDto.builder()
                .name(DEFAULT_NAME)
                .date(LocalDate.now(clock))
                .build();
    }
}
