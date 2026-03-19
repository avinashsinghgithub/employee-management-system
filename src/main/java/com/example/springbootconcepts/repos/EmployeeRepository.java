package com.example.springbootconcepts.repos;

import com.example.springbootconcepts.domains.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    //finding by first name or last name
    @Query("SELECT e FROM Employee e WHERE e.firstName = :name OR e.lastName =:name")
    Optional<Employee> findByFirstNameOrLastName(@Param("name")String name);

   Optional<Employee> findByLastName(String lastName);

    @Transactional
    @Modifying
    //Delete by year
    @Query(value = "delete from Employee emp WHERE SubString(cast(emp.joiningDate as text),1,4) =:joiningYear")
    void deleteByJoiningYear(@Param("joiningYear") String joiningYear);

    // Fetch employees with images to avoid LazyInitializationException during DTO mapping
    @EntityGraph(attributePaths = {"images"})
    @Query("SELECT e FROM Employee e")
    List<Employee> findAllWithImages();

    // For paging, override findAll(Pageable) to include images via EntityGraph
    @Override
    @EntityGraph(attributePaths = {"images"})
    Page<Employee> findAll(Pageable pageable);
}
