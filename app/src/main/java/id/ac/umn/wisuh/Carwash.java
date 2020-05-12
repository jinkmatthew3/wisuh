package id.ac.umn.wisuh;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

public class Carwash implements Serializable {
    public String alamat;
    public String desc;
    public int hargaMobil;
    public int hargaMotor;
    public String jamBuka;
    public String jamTutup;
    public GeoPoint latLong;
    public String nama;
    public float rating ;

    public Carwash(){}

    public Carwash(String alamat, String desc, int hargaMobil, int hargaMotor, String jamBuka, String jamTutup,GeoPoint latLong, String nama, float rating) {
        this.alamat = alamat;
        this.desc = desc;
        this.hargaMobil = hargaMobil;
        this.hargaMotor = hargaMotor;
        this.jamBuka = jamBuka;
        this.jamTutup = jamTutup;
        this.latLong = latLong;
        this.nama = nama;
        this.rating = rating;
    }
    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getHargaMobil() {
        return hargaMobil;
    }

    public void setHargaMobil(int hargaMobil) {
        this.hargaMobil = hargaMobil;
    }

    public int getHargaMotor() {
        return hargaMotor;
    }

    public void setHargaMotor(int hargaMotor) {
        this.hargaMotor = hargaMotor;
    }

    public String getJamBuka() {
        return jamBuka;
    }

    public void setJamBuka(String jamBuka) {
        this.jamBuka = jamBuka;
    }

    public String getJamTutup() {
        return jamTutup;
    }

    public void setJamTutup(String jamTutup) {
        this.jamTutup = jamTutup;
    }


    public GeoPoint getLatLong() {
        return latLong;
    }

    public void setLatLong(GeoPoint latLong) {
        this.latLong = latLong;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

}


