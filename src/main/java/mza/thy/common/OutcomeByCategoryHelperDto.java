package mza.thy.common;

import java.math.BigDecimal;

public record OutcomeByCategoryHelperDto(String category, BigDecimal amount) {

    public String getCategory() {
        return category;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
