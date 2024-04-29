package lecture.domain;
    public class Student extends User {
        public Student(int id, String name, String role, String password) {
            super(id, name, role, password);
        }

    @Override
    public String getRole() {
        return "student";
    }
}
