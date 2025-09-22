package com.MongoSpring.MongoSpring.Controller;


import com.MongoSpring.MongoSpring.Model.Student;
import com.MongoSpring.MongoSpring.Repository.StudentRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class MainController {
    @Autowired
    private StudentRepo repo;

    @PostMapping("/addStudent")
    public void addStudent(@RequestBody Student student)
    {
        repo.save(student);
    }
    @PostMapping("/addStudentList")
    public void addStudentList(@RequestBody List<Student> student)
    {
//        repo.save(student);
        repo.saveAll(student);
    }

    @GetMapping("/getStudent/{id}")
    public Student getStudent(@PathVariable Integer id)
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
    public String deleteStudentById(@PathVariable Integer id)
    {   if (repo.existsById(id)) {
        repo.deleteById(id);
        return "Deleted successfully!";
    } else {
        return "Student not found!";
    }


    }

}
