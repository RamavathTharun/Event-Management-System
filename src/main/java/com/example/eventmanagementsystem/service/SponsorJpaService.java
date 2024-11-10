package com.example.eventmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.eventmanagementsystem.repository.*;
import com.example.eventmanagementsystem.model.*;

import java.util.*;

@Service
public class SponsorJpaService implements SponsorRepository {

    @Autowired
    private SponsorJpaRepository sponsorJpaRepository;

    @Autowired
    private EventJpaRepository eventJpaRepository;

    @Override
    public ArrayList<Sponsor> getSponsors() {
        List<Sponsor> sponsorList = sponsorJpaRepository.findAll();
        ArrayList<Sponsor> sponsors = new ArrayList<>(sponsorList);
        return sponsors;
    }

    @Override
    public Sponsor getSponsorById(int sponsorId) {
        try {
            Sponsor sponsor = sponsorJpaRepository.findById(sponsorId).get();
            return sponsor;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public Sponsor addSponsor(Sponsor sponsor) {
        List<Integer> eventIds = new ArrayList<>();

        for (Event event : sponsor.getEvents()) {
            eventIds.add(event.getEventId());
        }

        List<Event> eventList = eventJpaRepository.findAllById(eventIds);

        if (eventList.size() != eventIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some of authors are found");
        }

        sponsor.setEvents(eventList);

        for (Event event : eventList) {
            event.getSponsors().add(sponsor);

        }

        Sponsor savedSponsor = sponsorJpaRepository.save(sponsor);
        eventJpaRepository.saveAll(eventList);

        return savedSponsor;
    }

    @Override
    public Sponsor updateSponsor(int sponsorId, Sponsor sponsor) {
        try {
            Sponsor newSponsor = sponsorJpaRepository.findById(sponsorId).get();

            if (sponsor.getSponsorName() != null) {
                newSponsor.setSponsorName(sponsor.getSponsorName());
            }

            if (sponsor.getIndustry() != null) {
                newSponsor.setIndustry(sponsor.getIndustry());
            }

            if (sponsor.getEvents() != null) {
                List<Event> events = newSponsor.getEvents();

                for (Event event : events) {
                    event.getSponsors().remove(newSponsor);
                }

                eventJpaRepository.saveAll(events);

                List<Integer> eventIds = new ArrayList<>();
                for (Event event : sponsor.getEvents()) {
                    eventIds.add(event.getEventId());
                }

                List<Event> eventList = eventJpaRepository.findAllById(eventIds);

                if (eventList.size() != eventIds.size()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                }

                for (Event event : eventList) {
                    event.getSponsors().add(newSponsor);

                }
                eventJpaRepository.saveAll(eventList);

                newSponsor.setEvents(eventList);

            }

            return sponsorJpaRepository.save(newSponsor);

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public void deleteSponsor(int sponsorId) {

        try {
            // Fetch the author entity
            Sponsor sponsor = sponsorJpaRepository.findById(sponsorId).get();

            // Remove the associations
            List<Event> events = sponsor.getEvents();
            for (Event event : events) {
                event.getSponsors().remove(sponsor);
            }

            // Update the book entity after removing the association
            eventJpaRepository.saveAll(events);

            // Delete the author
            sponsorJpaRepository.deleteById(sponsorId);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);

    }

    @Override
    public ArrayList<Event> getEventsBySponsorId(int sponsorId) {

        try {
            Sponsor sponsor = sponsorJpaRepository.findById(sponsorId).get();
            List<Event> eventList = sponsor.getEvents();
            ArrayList<Event> events = new ArrayList<>(eventList);

            return events;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }
}
