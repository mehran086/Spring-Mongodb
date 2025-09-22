package com.MongoSpring.MongoSpring.Controller;


import com.MongoSpring.MongoSpring.Model.StudentDto;
import com.MongoSpring.MongoSpring.Model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private MongoTemplate mongoTemplate;

    // =========================
    // 1️⃣ Get all students (paginated + sorted)
    // =========================
    @GetMapping("/getUsers")
    public List<StudentDto> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "rollno") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Query query = new Query();

        // Pagination
        query.skip(page * size).limit(size);

        // Sorting
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        query.with(Sort.by(direction, sortBy));

        // Projection
        query.fields().include("name").include("address").exclude("_id");

        // Execute query
        List<Student> students = mongoTemplate.find(query, Student.class);

        // Convert to DTO
        return students.stream()
                .map(s -> new StudentDto(s.getName(), s.getAddress()))
                .collect(Collectors.toList());
    }

    // =========================
    // 2️⃣ Search students with filters (AND/OR/regex) + pagination + sorting
    // =========================
    @GetMapping("/search")
    public List<StudentDto> searchStudents(
            @RequestParam(required = false) String namePrefix,
            @RequestParam(required = false) String address1,
            @RequestParam(required = false) String address2,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "rollno") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Criteria criteria = new Criteria();

        // Name regex filter
        if (namePrefix != null && !namePrefix.isEmpty()) {
            criteria = criteria.and("name").regex("^" + namePrefix, "i");
        }

        // OR address filter
        if (address1 != null && address2 != null) {
            criteria = criteria.orOperator(
                    Criteria.where("address").is(address1),
                    Criteria.where("address").is(address2)
            );
        } else if (address1 != null) {
            criteria = criteria.and("address").is(address1);
        }

        Query query = new Query(criteria);

        // Pagination
        query.skip(page * size).limit(size);

        // Sorting
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        query.with(Sort.by(direction, sortBy));

        // Projection
        query.fields().include("name").include("address").exclude("_id");

        List<Student> students = mongoTemplate.find(query, Student.class);

        return students.stream()
                .map(s -> new StudentDto(s.getName(), s.getAddress()))
                .collect(Collectors.toList());
    }
    @GetMapping("/allstudents")
    public List<Student> findAll(){
        return mongoTemplate.findAll(Student.class);
    }

    @GetMapping("/findbyRollno/{id}")
    public Student findByRollno(@PathVariable  Long id){
        Criteria criteria = new Criteria();
        Query query = new Query(criteria.where("rollno").is(id));
     return   mongoTemplate.findOne(query,Student.class);

    }

    @PostMapping("/add")
    public Student addStudent(@RequestBody Student student){
       return mongoTemplate.save(student);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteByRollno(@PathVariable Long id){
        Query query = new Query(Criteria.where("rollno").is(id));
        boolean status= mongoTemplate.remove(query,Student.class).wasAcknowledged();
        if(status){
            return "Deleted successfully";
        }
        else {
            return "Deletion failed";
        }
    }

    @PutMapping("/students/updateAddress/{rollno}")
    public Student updateStudent(@PathVariable Long rollno,@RequestParam String address ){
        Query query = new Query(Criteria.where("rollno").is(rollno));
        Update update = new Update().set("address",address);
        return mongoTemplate.findAndModify(query,update, Student.class);
    }


    @GetMapping("/students/list")
    public List<StudentDto> getStudentList(@RequestParam(defaultValue = "0") Integer page,
                                           @RequestParam(defaultValue = "5") Integer size,
                                           @RequestParam(defaultValue = "rollno") String sortBy,
                                           @RequestParam(defaultValue = "asc") String sortDir
                                           )
    {
        Query query = new Query();
        query.skip(page*size).limit(size);
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        query.with(Sort.by(direction, sortBy));

        query.fields().include("name").include("address").exclude("_id");

      return  mongoTemplate.find(query, Student.class)
              .stream().map(student -> new StudentDto(student.getName(), student.getAddress()))
              .collect(Collectors.toList());
    }

}
