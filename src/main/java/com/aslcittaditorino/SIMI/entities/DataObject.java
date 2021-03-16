package com.aslcittaditorino.SIMI.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Data
public abstract class DataObject implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "idgen")
    @Column(name="id")
    private Long id;
}
