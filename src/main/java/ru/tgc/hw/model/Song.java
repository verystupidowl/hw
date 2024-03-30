package ru.tgc.hw.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Song {

    private long id;
    private String name;
    private String albumName;
    private Date date;
    private Artist owner;
}
