package id.ac.umn.wisuh;

class Customer {
    public String id;
    public String fName;
    public String lName;
    public String pNumber;
    public String email;
    public String password;

    public Customer(String id, String fName, String lName, String pNumber, String email, String password) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.pNumber = pNumber;
        this.email = email;
        this.password = password;
    }
}
