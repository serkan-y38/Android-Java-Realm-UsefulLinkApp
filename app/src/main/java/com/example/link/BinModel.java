package com.example.link;
import io.realm.RealmObject;

public class BinModel extends RealmObject {

    private String linkname;
    private String linktopic;
    private String link;
    private String linkdefinition;
    private String linkdate;
    private String favstatus;

    public String getLinkname() {
        return linkname;
    }

    public void setLinkname(String linkname) {
        this.linkname = linkname;
    }

    public String getLinktopic() {
        return linktopic;
    }

    public void setLinktopic(String linktopic) {
        this.linktopic = linktopic;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkdefinition() {
        return linkdefinition;
    }

    public void setLinkdefinition(String linkdefinition) {
        this.linkdefinition = linkdefinition;
    }

    public String getLinkdate() {
        return linkdate;
    }

    public void setLinkdate(String linkdate) {
        this.linkdate = linkdate;
    }

    public String getFavstatus() {
        return favstatus;
    }

    public void setFavstatus(String favstatus) {
        this.favstatus = favstatus;
    }

    @Override
    public String toString() {
        return "BinModel{" +
                "linkname='" + linkname + '\'' +
                ", linktopic='" + linktopic + '\'' +
                ", link='" + link + '\'' +
                ", linkdefinition='" + linkdefinition + '\'' +
                ", linkdate='" + linkdate + '\'' +
                ", favstatus='" + favstatus + '\'' +
                '}';
    }
}
