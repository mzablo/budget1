package mza.thy.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Clock;

@Configuration
public class Config {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    DecimalFormat getDecimalFormat() {
        DecimalFormat df = new DecimalFormat("###,##0.00");

        DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
        customSymbol.setGroupingSeparator(' ');
        df.setDecimalFormatSymbols(customSymbol);
        return df;
    }
}
