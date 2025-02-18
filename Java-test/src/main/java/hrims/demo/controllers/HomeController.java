package hrims.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;

import hrims.demo.support.Settings;

@RestController
@RequestMapping("/")
public class HomeController {

    @Autowired
    private Settings _settings;

    @GetMapping()
	public String info() {
        var content = "<html><body><h1>" + _settings.ApplicationName + "</h1><p>" + _settings.ApplicationName + " is Running. Use '/confirmsetup' address to test setup</p></body></html>";

		return content;
	}

    @GetMapping("confirmsetup")
	public String confirmSetup() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty print
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
        
		var settingsJson = mapper.writeValueAsString(_settings);

        return settingsJson;
	}

	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        // Log the exception and return a meaningful response
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
