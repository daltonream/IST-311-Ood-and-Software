/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
@Table(name = "LOGMODEL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Logmodel.findAll", query = "SELECT l FROM Logmodel l")
    , @NamedQuery(name = "Logmodel.findById", query = "SELECT l FROM Logmodel l WHERE l.id = :id")
    , @NamedQuery(name = "Logmodel.findByDate", query = "SELECT l FROM Logmodel l WHERE l.date = :date")
    , @NamedQuery(name = "Logmodel.findByContent", query = "SELECT l FROM Logmodel l WHERE l.content = :content")
    , @NamedQuery(name = "Logmodel.findByUserid", query = "SELECT l FROM Logmodel l WHERE l.userid = :userid")
    , @NamedQuery(name = "Logmodel.countEntries", query = "SELECT COUNT(numLogs) FROM Logmodel numLogs")})

public class Logmodel implements Serializable {

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
    private Integer userid;

    public Logmodel() {
    }

    public Logmodel(Integer id) {
        this.id = id;
    }

    public Logmodel(Integer id, String date, String content) {
        this.id = id;
        this.date = date;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUserId() {
        return userid;
    }

    public void setUserId(Integer userid) {
        this.userid = userid;
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
        if (!(object instanceof Logmodel)) {
            return false;
        }
        Logmodel other = (Logmodel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model.Logmodel[ id=" + id + " ]";
    }
    
}
