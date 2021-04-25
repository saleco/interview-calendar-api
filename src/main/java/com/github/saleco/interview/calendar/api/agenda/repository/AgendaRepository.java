package com.github.saleco.interview.calendar.api.agenda.repository;

import com.github.saleco.interview.calendar.api.agenda.model.Agenda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {

    String SEARCH_AVAILABILITY = "select interviewer_agenda " +
      "from Agenda candidate_agenda \n" +
      "inner join Agenda interviewer_agenda \n" +
      " on PARSEDATETIME(candidate_agenda.start, 'yyyy-MM-dd HH:mm:ss') = PARSEDATETIME(interviewer_agenda.start, 'yyyy-MM-dd HH:mm:ss') \n" +
      "and PARSEDATETIME(candidate_agenda.end, 'yyyy-MM-dd HH:mm:ss') = PARSEDATETIME(interviewer_agenda.end, 'yyyy-MM-dd HH:mm:ss') \n" +
      "where candidate_agenda.user.id = ?1 \n" +
      "and interviewer_agenda.user.id in (?2) \n" +
      "and ((candidate_agenda.start between ?3 and ?4) or (candidate_agenda.end between ?3 and ?4)) \n" +
      "and ((interviewer_agenda.start between ?3 and ?4) or (interviewer_agenda.end between ?3 and ?4)) \n";

    @Query(value = SEARCH_AVAILABILITY)
    Page<Agenda> searchAvailabilityBy(Pageable pageable, Long candidateId, List<Long> interviewerIds, Timestamp start, Timestamp end);
}
