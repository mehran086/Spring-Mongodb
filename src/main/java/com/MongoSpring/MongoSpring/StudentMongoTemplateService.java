package com.MongoSpring.MongoSpring;

import com.MongoSpring.MongoSpring.Model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

//Criteria → build conditions
//       ↓
//Query → wrap criteria + add sort, limit, projection
//       ↓
//MongoTemplate → execute query and return results



@Service
public class StudentMongoTemplateService {

    @Autowired
    private MongoTemplate mongoTemplate;

    // ===========================
    // 1️⃣ Find all students
    // ===========================
    public List<Student> findAllStudents() {

        return mongoTemplate.findAll(Student.class);
    }

    // ===========================
    // 2️⃣ Find by rollno
    // ===========================
    public Student findByRollno(Long rollno) {
        return mongoTemplate.findById(rollno, Student.class);
    }

    // ===========================
    // 3️⃣ Find with filter (Criteria)
    // ===========================
    public List<Student> findStudentsInDelhiWithRollnoGreaterThan5() {
        Query query = new Query();
        query.addCriteria(Criteria.where("address").is("Delhi").and("rollno").gt(5));
        return mongoTemplate.find(query, Student.class);
    }

    // ===========================
    // 4️⃣ Regex search (name starts with "A")
    // ===========================
    public List<Student> findStudentsByNamePrefix(String prefix) {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").regex("^" + prefix, "i")); // case-insensitive
        return mongoTemplate.find(query, Student.class);
    }

    // ===========================
    // 5️⃣ Sorting
    // ===========================
    public List<Student> findStudentsSortedByRollnoDesc() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC, "rollno","address"));
        return mongoTemplate.find(query, Student.class);
    }

    // ===========================
    // 6️⃣ Limit results
    // ===========================
    public List<Student> findTop5Students() {
        Query query = new Query();
        query.limit(5);
        return mongoTemplate.find(query, Student.class);
    }

    // ===========================
    // 7️⃣ Update a student
    // ===========================
    public void updateStudentAddress(Long rollno, String newAddress) {
        Query query = new Query(Criteria.where("rollno").is(rollno));
        Update update = new Update().set("address", newAddress);
        mongoTemplate.updateFirst(query, update, Student.class); // updateFirst
    }

    // ===========================
    // 8️⃣ Update multiple students
    // ===========================
    public void updateAllDelhiStudentsAddress(String newAddress) {
        Query query = new Query(Criteria.where("address").is("Delhi"));
        Update update = new Update().set("address", newAddress);
        mongoTemplate.updateMulti(query, update, Student.class);
    }

    // ===========================
    // 9️⃣ Delete a student
    // ===========================
    public void deleteStudentByRollno(Long rollno) {
        Query query = new Query(Criteria.where("rollno").is(rollno));
        mongoTemplate.remove(query, Student.class);
    }

    // ===========================
    // 🔟 Check existence
    // ===========================
    public boolean studentExists(Long rollno) {
        Query query = new Query(Criteria.where("rollno").is(rollno));
        return mongoTemplate.exists(query, Student.class);
    }

    // ===========================
    // 1️⃣1️⃣ Count students
    // ===========================
    public long countStudentsInDelhi() {
        Query query = new Query(Criteria.where("address").is("Delhi"));
        return mongoTemplate.count(query, Student.class);
    }

    // ===========================
    // 1️⃣2️⃣ Projection: return only name & address
    // ===========================
    public List<Student> findStudentsNameAndAddress() {
        Query query = new Query();
        query.fields().include("name").include("address").exclude("_id");
        return mongoTemplate.find(query, Student.class);
    }

    // ===========================
    // 1️⃣3️⃣ OR condition
    // ===========================
    public List<Student> findStudentsInDelhiOrMumbai() {
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("address").is("Delhi"),
                Criteria.where("address").is("Mumbai")
        );
        Query query = new Query(criteria);
        return mongoTemplate.find(query, Student.class);
    }
}
