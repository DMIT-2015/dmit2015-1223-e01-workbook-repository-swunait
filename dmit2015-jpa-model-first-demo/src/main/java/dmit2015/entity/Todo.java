package dmit2015.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Entity
//@Table(schema = "CustomSchemaName", name="CustomTableName")
@Getter
@Setter
public class Todo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(length = 128)
    private String description;

    private boolean completed;

    public Todo() {

    }

    @Version
    private Integer version;

    @Column(nullable = false)
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @PrePersist
    private void beforePersist() {
        createTime = LocalDateTime.now();
    }

    @PreUpdate
    private void beforeUpdate() {
        updateTime = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object obj) {
        return (
                (obj instanceof Todo other) &&
                        Objects.equals(id, other.id)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public static Optional<Todo> parseCsv(String line) {
        Optional<Todo> optionalTodo = Optional.empty();
        final var DELIMITER = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
        String[] tokens = line.split(DELIMITER, -1);  // The -1 limit allows for any number of fields and not discard trailing empty fields
        /**
         * The order of the columns are:
         * 0 - column1
         * 1 - column2
         * 2 - column3
         * 3 - column4
         */
        if (tokens.length == 4) {
            Todo parsedTodo = new Todo();

            try {
                // String stringColumnValue = tokens[0].replaceAll("\"","");
                // LocalDate dateColumnValue = tokens[0].isBlank() ? null : LocalDate.parse(tokens[0]);
                // BigDecimal decimalColumnValue = tokens[0].isBlank() ? null : BigDecimal.valueOf(Double.parseDouble(tokens[0]));
                // Integer IntegerColumnValue = tokens[0].isBlank() ? null : Integer.valueOf(tokens[0]);
                // Double DoubleColumnValue = tokens[0].isBlank() ? null : Double.valueOf(tokens[0]);
                // int intColumnValue = tokens[0].isBlank() ? 0 : Integer.parseInt(tokens[0]);
                // double doubleColumnValue = tokens[0].isBlank() ? 0 : Double.parseDouble(tokens[0]);

                // parsedTodo.setProperty1(column1Value);

                optionalTodo = Optional.of(parsedTodo);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return optionalTodo;
    }

}