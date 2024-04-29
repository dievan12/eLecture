package lecture.domain;

public class Lecturer extends User {

    public Lecturer(int id, String name, String role, String password) {
        super(id, name, role, password);
    }

    @Override
    public String getRole() {
        return "Lecturer";
    }
}
