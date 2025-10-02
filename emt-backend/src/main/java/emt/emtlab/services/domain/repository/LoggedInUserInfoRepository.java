package emt.emtlab.services.domain.repository;

import emt.emtlab.services.domain.model.LoggedInUserLogs;
import emt.emtlab.services.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoggedInUserInfoRepository extends JpaRepository<LoggedInUserLogs, Long> {
    Optional<LoggedInUserLogs> findByUser(User user);

    List<LoggedInUserLogs> findAll();
}