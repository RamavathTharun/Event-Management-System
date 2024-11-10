package com.example.eventmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.eventmanagementsystem.service.EventJpaService;
import com.example.eventmanagementsystem.model.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EventController {

    @Autowired
    EventJpaService eventService;

    @GetMapping("/events")
    public ArrayList<Event> getEvents() {
        return eventService.getEvents();
    }

    @GetMapping("/events/{eventId}")
    public Event getEvent(@PathVariable("eventId") int eventId) {
        return eventService.getEvent(eventId);
    }

    @PostMapping("/events")
    public Event addEvent(@RequestBody Event event) {
        return eventService.addEvent(event);
    }

    @PutMapping("/events/{eventId}")
    public Event updateEvent(@PathVariable("eventId") int eventId, @RequestBody Event event) {
        return eventService.updateEvent(eventId, event);
    }

    @DeleteMapping("/events/{eventId}")
    public void deleteEvent(@PathVariable("eventId") int eventId) {
        eventService.deleteEvent(eventId);
    }

    @GetMapping("events/{eventId}/sponsors")
    public ArrayList<Sponsor> getSponsor(@PathVariable("eventId") int eventId) {
        return eventService.getSponsor(eventId);
    }
}