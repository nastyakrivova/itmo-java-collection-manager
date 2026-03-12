package com.myorg.lab5.model;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

public class MusicBand implements Comparable<MusicBand>{
    private static final AtomicInteger generator = new AtomicInteger(1);

    private Integer id;
    private String name;
    private Coordinates coordinates;
    private LocalDate creationDate;
    private int numberOfParticipants;
    private Integer singlesCount;
    private int albumsCount;
    private MusicGenre genre;
    private Studio studio;

    public MusicBand(String name,
                    Coordinates coordinates,
                    int numberOfParticipants,
                    int albumsCount,
                    MusicGenre genre) {
        setId();
        setCreationDate();
        setName(name);
        setCoordinates(coordinates);
        setNumberOfParticipants(numberOfParticipants);
        setAlbumsCount(albumsCount);
        setGenre(genre);
    }

    public MusicBand(String name,
                    Coordinates coordinates,
                    int numberOfParticipants,
                    int albumsCount,
                    MusicGenre genre,
                    Studio studio,
                    Integer singlesCount) {
        this(name, coordinates, numberOfParticipants, albumsCount, genre);
        setStudio(studio);
        setSinglesCount(singlesCount);
    }


    private void setId() {
        this.id = generator.getAndIncrement();
    }

    private void setCreationDate() {
        this.creationDate = LocalDate.now();
    }


    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Coordinates cannot be null");
        }
        this.coordinates = coordinates;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        if (numberOfParticipants <= 0) {
            throw new IllegalArgumentException("Number of participants must be > 0");
        }
        this.numberOfParticipants = numberOfParticipants;
    }

    public void setAlbumsCount(int albumsCount) {
        if (albumsCount <= 0) {
            throw new IllegalArgumentException("Albums count must be > 0");
        }
        this.albumsCount = albumsCount;
    }

    public void setGenre(MusicGenre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }
        this.genre = genre;
    }

    public void setSinglesCount(Integer singlesCount) {
        if (singlesCount != null && singlesCount <= 0) {
            throw new IllegalArgumentException("Singles count must be > 0 if provided");
        }
        this.singlesCount = singlesCount;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }

    public Integer getId() { return id; }
    public String getName() { return name; }
    public Coordinates getCoordinates() { return coordinates; }
    public LocalDate getCreationDate() { return creationDate; }
    public int getNumberOfParticipants() { return numberOfParticipants; }
    public int getAlbumsCount() { return albumsCount; }
    public Integer getSinglesCount() { return singlesCount; }
    public MusicGenre getGenre() { return genre; }
    public Studio getStudio() { return studio; }


    @Override
    public String toString() {
        return String.format("[%d] %s | Участников: %d | Альбомов: %d | Жанр: %s | Студия: %s",
            id, name, numberOfParticipants, albumsCount, genre,
            studio != null ? studio.getName() : "не указана");

    }

    @Override
    public int compareTo(MusicBand other) {
        return Integer.compare(this.numberOfParticipants, other.numberOfParticipants);
    }
}