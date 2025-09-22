package com.MongoSpring.MongoSpring.Controller;


import com.MongoSpring.MongoSpring.Model.DatabaseSequence;
import com.MongoSpring.MongoSpring.Model.Student;
import com.MongoSpring.MongoSpring.Model.StudentDto;
import com.MongoSpring.MongoSpring.Repository.StudentRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;


@RestController
public class MainController {
    @Autowired
    private StudentRepo repo;

    @Autowired
    private MongoOperations mongoOperations;

    public long generateSequence(String seqName) {
        DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }


    @PostMapping("/addStudent")
    public void addStudent(@RequestBody Student student)
    {

        Student studentTemp = new Student();
        studentTemp.setName(student.getName());
        studentTemp.setAddress(student.getAddress());
        studentTemp.setRollno(generateSequence(Student.SEQUENCE_NAME));
        repo.save(studentTemp);
    }
    @PostMapping("/addStudentList")
    public void addStudentList(@RequestBody List<Student> student)
    {
//        repo.save(student);
        repo.saveAll(student);
    }

    @GetMapping("/getStudent/{id}")
    public Student getStudent(@PathVariable Long id)
    {
        return repo.findById(id).orElse(null);
    }


    @GetMapping("/getUsers")
    public List<Student> returnAllStudents(){
        return repo.findAll();
    }
    @PutMapping("/updateStudent")
    public void updateStudent(@RequestBody Student student)
    {

        // first fetch the student using id
        Student data =repo.findById(student.getRollno()).orElse(null);
        // then check if it exists , only update if it exists only.
        if(data!=null){
            data.setName(student.getName());
            data.setAddress(student.getAddress());
            repo.save(data);
        }
       else{
            System.out.println("No student with id "+student.getRollno()+" found");
        }
    }

    @DeleteMapping("/deleteStudent")
    public void deleteStudent(@RequestBody Student student)
    {   repo.delete(student);
//        repo.save(student);
    }
    @DeleteMapping("/deleteStudent/{id}")
    public String deleteStudentById(@PathVariable Long id)
    {   if (repo.existsById(id)) {
        repo.deleteById(id);
        return "Deleted successfully!";
    } else {
        return "Student not found!";
    }


    }

    @GetMapping("/students/byName/{name}")
    public List<Student> getStudentsByName(@PathVariable String name) {
        return repo.findByName(name);
    }

    @GetMapping("/students/byNameStartingWith/{prefix}")
    public List<Student> getStudentsByNamePrefix(@PathVariable String prefix) {
        return repo.findByNameStartingWith(prefix);
    }

    @GetMapping("/students/byRollnoGreaterThan/{rollno}")
    public List<Student> getStudentsWithRollnoGreaterThan(@PathVariable Long rollno) {
        return repo.findByRollnoGreaterThan(rollno);
    }

    @GetMapping("/students/byNameAndAddress")
    public List<Student> getStudentsByNameAndAddress(@RequestParam String name, @RequestParam String address) {
        return repo.findByNameAndAddress(name, address);
    }

    @GetMapping("/students/top5ByRollno")
    public List<Student> getTop5ByRollno() {
        return repo.findTop5ByOrderByRollnoAsc();
    }

    @GetMapping("/students/firstByName/{name}")
    public Student getFirstByName(@PathVariable String name) {
        return repo.findFirstByName(name);
    }

    @GetMapping("/students/byAddressSorted/{address}")
    public List<Student> getByAddressSorted(@PathVariable String address) {
        return repo.findByAddressOrderByNameAsc(address);
    }

    @GetMapping("/students/byNameKeywordSorted/{keyword}")
    public List<Student> getByNameKeywordSorted(@PathVariable String keyword) {
        return repo.findByNameContainingOrderByRollnoDesc(keyword);
    }


    @GetMapping("/getallStudents")
    public List<StudentDto> getAllStudents() {
        return repo.findAllStudentNameAddress();
    }
}
