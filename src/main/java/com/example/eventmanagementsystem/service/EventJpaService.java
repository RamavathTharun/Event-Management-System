package com.example.eventmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.eventmanagementsystem.repository.EventJpaRepository;
import com.example.eventmanagementsystem.repository.EventRepository;
import com.example.eventmanagementsystem.repository.SponsorJpaRepository;
import com.example.eventmanagementsystem.model.*;

import java.util.*;

@Service
public class EventJpaService implements EventRepository {

    @Autowired
    EventJpaRepository eventJpaRepository;

    @Autowired
    SponsorJpaRepository sponsorJpaRepository;

    @Override
    public ArrayList<Event> getEvents() {
        List<Event> eventList = eventJpaRepository.findAll();
        ArrayList<Event> events = new ArrayList<>(eventList);
        return events;
    }

    @Override
    public Event getEvent(int eventId) {
        try {
            Event event = eventJpaRepository.findById(eventId).get();

            return event;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Event addEvent(Event event) {
        try {
            List<Integer> sponsorIds = new ArrayList<>();

            for (Sponsor sponsor : event.getSponsors()) {
                sponsorIds.add(sponsor.getSponsorId());
            }

            List<Sponsor> sponsorList = sponsorJpaRepository.findAllById(sponsorIds);

            if (sponsorList.size() != sponsorIds.size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some of sponsors are found");
            }

            event.setSponsors(sponsorList);

            return eventJpaRepository.save(event);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Event updateEvent(int eventId, Event event) {
        try {
            Event newEvent = eventJpaRepository.findById(eventId).get();

            if (event.getEventName() != null) {
                newEvent.setEventName(event.getEventName());
            }

            if (event.getDate() != null) {
                newEvent.setDate(event.getDate());
            }

            if (event.getSponsors() != null) {

                List<Integer> sponsorIds = new ArrayList<>();

                for (Sponsor sponsor : event.getSponsors()) {
                    sponsorIds.add(sponsor.getSponsorId());
                }

                List<Sponsor> sponsorList = sponsorJpaRepository.findAllById(sponsorIds);

                if (sponsorList.size() != sponsorIds.size()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some of sponsors are found");
                }

                newEvent.setSponsors(sponsorList);

            }

            return eventJpaRepository.save(newEvent);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteEvent(int eventId) {
        try {
            Event event = eventJpaRepository.findById(eventId).get();

            List<Sponsor> sponsors = event.getSponsors();
            for (Sponsor sponsor : sponsors) {
                sponsor.getEvents().remove(event);

            }

            sponsorJpaRepository.saveAll(sponsors);
            eventJpaRepository.deleteById(eventId);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    public ArrayList<Sponsor> getSponsor(int eventId) {
        try {
            Event event = eventJpaRepository.findById(eventId).get();

            List<Sponsor> sponsorsList = event.getSponsors();
            ArrayList<Sponsor> sponsors = new ArrayList<>(sponsorsList);

            return sponsors;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}