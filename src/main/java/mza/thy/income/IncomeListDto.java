package mza.thy.income;

import lombok.Getter;
import lombok.Value;

import java.util.List;

@Value
@Getter
public class IncomeListDto {
    List<IncomeDto> incomeList;
}
