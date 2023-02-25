package mza.thy.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class UtilsService {
    private final String info;
    private String error;

    public UtilsService() {
        info = "Aby zaimportować starą bazę: zrób eksport w starej aplikacji." +
                " Gotowe zapytania z wygenerowanego pliku wklej w zakladce Sql.\n " +
                "Czyli: \n" +
                "INSERT INTO income VALUES ... \n" +
                "INSERT INTO outcome VALUES ... \n" +
                "Jesli w nazwach/opisach sa ciapki, trzeba je usunac\n" +
                "Kategorie do wydatków modyfikuje się w config.properties";
    }

    String getInfo() {
        return info;
    }

    String getError() {
        return error;
    }

}
