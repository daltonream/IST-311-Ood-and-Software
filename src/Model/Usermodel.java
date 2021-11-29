/*
 * Weightloss App
 * Group 5 
 * IST 311
 */

package Model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author liamb
 */
@Entity
@Table(name = "USERMODEL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usermodel.findAll", query = "SELECT u FROM Usermodel u")
    , @NamedQuery(name = "Usermodel.findById", query = "SELECT u FROM Usermodel u WHERE u.id = :id")
    , @NamedQuery(name = "Usermodel.findByName", query = "SELECT u FROM Usermodel u WHERE u.name = :name")
    , @NamedQuery(name = "Usermodel.findByHeight", query = "SELECT u FROM Usermodel u WHERE u.height = :height")
    , @NamedQuery(name = "Usermodel.findByWeight", query = "SELECT u FROM Usermodel u WHERE u.weight = :weight")
    , @NamedQuery(name = "Usermodel.findByAge", query = "SELECT u FROM Usermodel u WHERE u.age = :age")
    , @NamedQuery(name = "Usermodel.findByNameAndAge", query = "SELECT u FROM Usermodel u WHERE u.name = :name and u.age = :age")
    , @NamedQuery(name = "Usermodel.readByNameAndId", query = "SELECT u FROM Usermodel u WHERE u.name = :name and u.id = :id")
    , @NamedQuery(name = "Usermodel.readByName", query = "SELECT u FROM Usermodel u WHERE u.name = :name")      
    , @NamedQuery(name = "Usermodel.readByNameAdvanced", query = "SELECT u FROM Usermodel u WHERE  LOWER(u.name) LIKE  CONCAT('%', LOWER(:name), '%')")      
})

public class Usermodel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "HEIGHT")
    private String height;
    @Basic(optional = false)
    @Column(name = "WEIGHT")
    private double weight;
    @Basic(optional = false)
    @Column(name = "AGE")
    private int age;

    public Usermodel() {
    }

    public Usermodel(Integer id) {
        this.id = id;
    }

    public Usermodel(Integer id, String name, String height, double weight, int age) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usermodel)) {
            return false;
        }
        Usermodel other = (Usermodel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  id + " " + name + " " + weight + " " + height + " " + age;
    }

}
