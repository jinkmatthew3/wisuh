package id.ac.umn.wisuh;

import java.io.Serializable;

class Customer implements Serializable {
    public String id;
    public String fName;
    public String lName;
    public String pNumber;
    public String email;
    public int saldo;
    public String password;

    public Customer(){}

    public Customer(String id, String fName, String lName, String pNumber, String email, String password) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.pNumber = pNumber;
        this.email = email;
        this.password = password;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getpNumber() {
        return pNumber;
    }

    public void setpNumber(String pNumber) {
        this.pNumber = pNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
