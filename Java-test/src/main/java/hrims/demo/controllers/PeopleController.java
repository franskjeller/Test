package hrims.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import hrims.demo.data.Person;
import hrims.demo.services.PeopleService;

@RestController
@RequestMapping("/People")
public class PeopleController {
    
    @Autowired
    private PeopleService _service;

    @GetMapping("{nid}")
	public ResponseEntity<Person> getPerson(@PathVariable String nid) throws Exception {
		var match = _service.getPersonByNid(nid);

        if (match == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(match, HttpStatus.OK);
	}

    @PostMapping("")
	public String addOrUpdatePerson(@RequestBody Person person) throws Exception {
        return _service.addOrUpdatePerson(person);
	}

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        // Log the exception and return a meaningful response
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
