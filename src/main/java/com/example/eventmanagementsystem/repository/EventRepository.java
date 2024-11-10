package com.example.eventmanagementsystem.repository;

import java.util.ArrayList;
import java.util.List;

import com.example.eventmanagementsystem.model.*;

public interface EventRepository {

    ArrayList<Event> getEvents();

    Event getEvent(int eventId);

    Event addEvent(Event event);

    Event updateEvent(int eventId, Event event);

    void deleteEvent(int eventId);

    ArrayList<Sponsor> getSponsor(int eventId);

}
