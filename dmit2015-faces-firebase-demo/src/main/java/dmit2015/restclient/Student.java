package dmit2015.restclient;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class Student {

//    @Getter @Setter
    @NotBlank(message = "First Name value is required.")
    private String firstName;

//    @Getter @Setter
    @NotBlank(message = "Last Name value is required.")
    private String lastName;

//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public Student() {
//    }
}
