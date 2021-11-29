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
@Table(name = "GOALMODEL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Goalmodel.findAll", query = "SELECT g FROM Goalmodel g")
    , @NamedQuery(name = "Goalmodel.findById", query = "SELECT g FROM Goalmodel g WHERE g.id = :id")
    , @NamedQuery(name = "Goalmodel.findByDate", query = "SELECT g FROM Goalmodel g WHERE g.date = :date")
    , @NamedQuery(name = "Goalmodel.findByContent", query = "SELECT g FROM Goalmodel g WHERE g.content = :content")
    , @NamedQuery(name = "Goalmodel.findByUserid", query = "SELECT g FROM Goalmodel g WHERE g.userID = :userID")
    , @NamedQuery(name = "Goalmodel.countEntries", query = "SELECT COUNT(numGoals) FROM Goalmodel numGoals")})


public class Goalmodel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "DATE")
    private String date;
    @Basic(optional = false)
    @Column(name = "CONTENT")
    private String content;
    @Column(name = "USERID")
    private Integer userID;
    
    public Goalmodel() {
    }

    public Goalmodel(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public void setDate(String date) {
        this.date = date;
    }
    
    public String getDate() {
        return date;
    }


    public Integer getUserId() {
        return userID;
    }
    
    public void setUserId(Integer userID) {
        this.userID = userID;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
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
        if (!(object instanceof Goalmodel)) {
            return false;
        }
        Goalmodel other = (Goalmodel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Goalmodel[ id=" + id + " ]";
    }
    
}
