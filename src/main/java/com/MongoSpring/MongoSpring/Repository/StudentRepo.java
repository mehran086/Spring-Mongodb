package com.MongoSpring.MongoSpring.Repository;

import com.MongoSpring.MongoSpring.Model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudentRepo extends MongoRepository<Student,Long> {
    // Find students by exact name
    List<Student> findByName(String name);

    // Find students with name starting with
    List<Student> findByNameStartingWith(String prefix);

    // Find students with name containing substring
    List<Student> findByNameContaining(String keyword);

    // Find students by address
    List<Student> findByAddress(String address);

    // Find students by name and address
    List<Student> findByNameAndAddress(String name, String address);

    // Find students with rollno greater than a value
    List<Student> findByRollnoGreaterThan(Long rollno);

    // Find students with rollno between two values
    List<Student> findByRollnoBetween(Long start, Long end);

    // Sort students by name ascending
    List<Student> findByOrderByNameAsc();

    // Sort students by rollno descending
    List<Student> findByOrderByRollnoDesc();


    // Find top 5 students by rollno ascending
    List<Student> findTop5ByOrderByRollnoAsc();

    // Find top 3 students by age descending (if you had an age field)
    // List<Student> findTop3ByOrderByAgeDesc();

    // Find first student by name (just one result)
    Student findFirstByName(String name);

    // Find students by address ordered by name ascending
    List<Student> findByAddressOrderByNameAsc(String address);

    // Find students by name containing keyword, ordered by rollno descending
    List<Student> findByNameContainingOrderByRollnoDesc(String keyword);
}
