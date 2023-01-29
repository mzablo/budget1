package mza.thy.domain;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class DepositPeriod {
    public static final String ONE_DAY = "1D";
    public static final String ONE_WEEK = "7D";
    public static final String ONE_MONTH = "1M";
    public static final String TWO_MONTHS = "2M";
    public static final String THREE_MONTHS = "3M";
    public static final String FOUR_MONTHS = "4M";
    public static final String SIX_MONTHS = "6M";
    public static final String YEAR = "12M";
    public static final String YEAR_AND_HALF = "18M";
    public static final String TWO_HEARS = "24M";

    public List<String> getAllPriods() {
        return List.of(ONE_DAY, ONE_WEEK, ONE_MONTH, TWO_MONTHS, THREE_MONTHS, FOUR_MONTHS, SIX_MONTHS, YEAR, YEAR_AND_HALF, TWO_HEARS);
    }

    public int getMonths(String value) {
        if (daysPeriod(value)) {
            return 0;
        }
        return Integer.parseInt(value.substring(0, value.length() - 1));
    }

    boolean daysPeriod(String value) {
        return value.contains("D");
    }
}
