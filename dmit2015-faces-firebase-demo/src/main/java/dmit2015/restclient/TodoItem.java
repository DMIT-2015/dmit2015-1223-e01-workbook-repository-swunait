package dmit2015.restclient;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TodoItem {

    private String description;
    private boolean done;
    private LocalDateTime createTime;

}
