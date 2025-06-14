package com.example.springbootconcepts.repos;
import com.example.springbootconcepts.domains.Department;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, UUID> {

}

