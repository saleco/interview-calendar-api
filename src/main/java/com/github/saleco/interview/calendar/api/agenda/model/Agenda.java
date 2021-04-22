package com.github.saleco.interview.calendar.api.agenda.model;

import com.github.saleco.interview.calendar.api.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    private Timestamp start;
    private Timestamp end;
}
