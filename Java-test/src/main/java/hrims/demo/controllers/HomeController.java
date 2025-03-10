package hrims.demo.controllers;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;

import hrims.demo.support.Settings;

@RestController
@RequestMapping("/")
public class HomeController {

    @Autowired
    private Settings _settings;

    private final String ApplicationName = "Demo";

    @GetMapping("")
	public ModelAndView info() {
        ModelAndView mav = new ModelAndView("info");
        mav.addObject("applicationName", ApplicationName);
		return mav;
	}

    @GetMapping("confirmsetup")
	public ModelAndView confirmSetup() throws Exception {
        ModelAndView mav = new ModelAndView("confirmsetup");
        mav.addObject("applicationName", ApplicationName);
        mav.addObject("version", "1.0");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        var date = LocalDateTime.now();
        mav.addObject("commitDate", date.format(formatter));

        //Run checks
        var checks = new ArrayList<String>();
        checks.add("Failed setting 1");
        checks.add("Failed setting 2");

        mav.addObject("checks", checks);

		ObjectMapper mapper = new ObjectMapper();
		var settingsJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(_settings);
        mav.addObject("jsonString", settingsJson);
        return mav;
	}

	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        // Log the exception and return a meaningful response
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
