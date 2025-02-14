package hrims.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import hrims.demo.support.Settings;

@RestController
@RequestMapping("/")
public class HomeController {

    @Autowired
    private Settings _settings;

    @GetMapping()
	public String Info() {
        var content = "<html><body><h1>" + _settings.ApplicationName + "</h1><p>" + _settings.ApplicationName + " is Running. Use '/confirmsetup' address to test setup</p></body></html>";

		return content;
	}

    @GetMapping("confirmsetup")
	public String ConfirmSetup() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty print
        
		var settingsJson = objectMapper.writeValueAsString(_settings);

        return settingsJson;
	}

	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        // Log the exception and return a meaningful response
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
