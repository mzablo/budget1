package mza.thy.deposit;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Slf4j
public class Process {
    private List<String> closeInfo;
    private List<String> prolongedInfo;
}
