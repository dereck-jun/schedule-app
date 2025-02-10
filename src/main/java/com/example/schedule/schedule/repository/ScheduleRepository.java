package com.example.schedule.schedule.repository;

import com.example.schedule.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value = "SELECT s FROM Schedule s",
        countQuery = "SELECT count(s) FROM Schedule s")
    Page<Schedule> findAllByUpdatedDateDesc(Pageable pageable);
}
