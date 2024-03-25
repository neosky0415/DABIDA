package com.example.demo11.FullCalendar;

import com.example.demo11.entity.User;
import com.example.demo11.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class CalendarDataController {

    private EventRepository eventRepository;
    private UserRepository userRepository;

    @Autowired
    public CalendarDataController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

//    @Autowired
//    public CalendarDataController(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }



    @GetMapping("/calendar")
    public String home() {

        return "fullCalendar/calendar";
    }

    @RequestMapping("/event_get_all")
    public ResponseEntity<List<Event>> event_get_all () {
        List<Event> eventList = eventRepository.findAll();
        return new ResponseEntity<List<Event>>(eventList, HttpStatus.OK);
    }

    @PostMapping("/event_add")
    @ResponseBody
    public ResponseEntity<JsonResponse> event_add (
            @RequestBody EventForm eventForm
            ) {
        System.out.println(eventForm);
        Event event = Event.builder()
                .title(eventForm.getTitle())
                .start(eventForm.getStart())
                .end(eventForm.getEnd())
                .build();

        JsonResponse response = new JsonResponse();
        Event eventResult = eventRepository.save(event);
        response.setResult(true);
        return ResponseEntity.ok(response);

    }

    @PostMapping("/event_remove")
    @ResponseBody
    public ResponseEntity<JsonResponse> event_remove (
            @RequestBody EventForm eventForm
            ) {
        int id = eventForm.getId();
        System.out.println(id);

        eventRepository.deleteById(id);

        JsonResponse response = new JsonResponse();
        response.setResult(true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/event_drag_and_drop")
    @ResponseBody
    public ResponseEntity<JsonResponse> event_drag_and_drop (
            @RequestBody EventForm eventForm
            , Principal principal
            ) {
          String addUser=principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(addUser);
        User user = userOptional.get();
        int id = eventForm.getId();
        eventRepository.deleteById(id);

        Event event = Event.builder()
                .title(eventForm.getTitle())
                .start(eventForm.getStart())
                .end(eventForm.getEnd())
                .user(user)
                .build();

        JsonResponse response = new JsonResponse();
        eventRepository.save(event);

        response.setResult(true);
        return ResponseEntity.ok(response);
    }
}
