package dmit2015.restclient;

import lombok.Data;

@Data
public class TodoItem {

    private Long id;

    private String name;

    private boolean complete;

    private int version;

}
