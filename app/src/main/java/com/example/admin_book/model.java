package com.example.admin_book;

public class model {
    String bname,aname,pic,did;
    model(String bname,String aname,String pic,String did)
    {
        this.bname=bname;
        this.aname=aname;
        this.pic=pic;
        this.did=did;
    }
    model()
    {

    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }
}
