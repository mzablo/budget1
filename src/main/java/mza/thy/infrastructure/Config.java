package mza.thy.infrastructure;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Clock;

@Configuration
public class Config {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean
    public Session getSession() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        var session = sessionFactory.openSession();
        return session;
    }

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
