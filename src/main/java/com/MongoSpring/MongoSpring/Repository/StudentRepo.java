package com.MongoSpring.MongoSpring.Repository;

import com.MongoSpring.MongoSpring.Model.Student;
import com.MongoSpring.MongoSpring.Model.StudentDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

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






    // 1️ Custom query to return only name and address by rollno
    @Query(value = "{ 'rollno' : ?0 }", fields = "{ 'name' : 1, 'address' : 1, '_id' : 0 }")
    StudentDto findNameAndAddressByRollno(Long rollno);

    // 2️ Custom query to return all students in a specific city/address
    @Query(value = "{ 'address' : ?0 }", fields = "{ 'name' : 1, 'address' : 1, '_id' : 0 }")
    List<StudentDto> findAllByAddress(String address);

    // 3️ Custom query using regex to find students by name containing keyword
    @Query(value = "{ 'name' : { $regex: ?0, $options: 'i' } }", fields = "{ 'name' : 1, 'address' : 1, '_id' : 0 }")
    List<StudentDto> findByNameContainingIgnoreCase(String keyword);

    // Return all students with only name and address
    @Query(value = "{}", fields = "{ 'name' : 1, 'address' : 1, '_id' : 0 }")
    List<StudentDto> findAllStudentNameAddress();
}
