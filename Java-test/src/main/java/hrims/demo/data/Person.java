package hrims.demo.data;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Component
@Data
public class Person {
    @JsonProperty("nationalId")
    public String NationalId;

    @JsonProperty("name")
    public String Name;

    @JsonProperty("email")
    public String Email;
}
