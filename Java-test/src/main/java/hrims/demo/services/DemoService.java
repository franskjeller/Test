package hrims.demo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.stereotype.Service;

@Service
public class DemoService {
    
    public String GetResponse(String id) {
        return "Provided parameter " + id;
    }

    public String GetJson(Object item) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty print
        

        return objectMapper.writeValueAsString(item);
    }
}
