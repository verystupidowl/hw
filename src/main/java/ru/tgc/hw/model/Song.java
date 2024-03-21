package ru.tgc.hw.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Song {

    private long id;
    private String name;
    private long price;
    private String describe;
    private Artist owner;
}
