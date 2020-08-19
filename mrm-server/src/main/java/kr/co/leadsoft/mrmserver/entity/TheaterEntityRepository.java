package kr.co.leadsoft.mrmserver.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TheaterEntityRepository extends JpaRepository<TheaterEntity, Integer> {

    TheaterEntity findByTheaterNumber(int theaterNumber);

    List<TheaterEntity> findAll();
}
