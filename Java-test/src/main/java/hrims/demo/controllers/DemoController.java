package hrims.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import hrims.demo.services.DemoService;

@RestController
@RequestMapping("/Demo")
public class DemoController {
    
    @Autowired
    private DemoService _service;

    @GetMapping("{id}")
	public String getParameter(@PathVariable String id) {
		return _service.GetResponse(id);
	}

    @PostMapping("")
	public String getJson(@RequestBody Object someObject) throws Exception {
        return _service.GetJson(someObject);
	}

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        // Log the exception and return a meaningful response
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
