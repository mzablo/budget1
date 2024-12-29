package mza.thy.domain.filter;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class FilterString {
    private String value;

    public String getValue() {
        return ("%" + value.toLowerCase() + "%");
    }
}
