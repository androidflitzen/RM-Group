package com.flitzen.rmapp.AdaptersAndItems;

public class OrderListItems {
    public int oId;
    public String oNumber;
    public String oDate;
    public String customerName;
    public String salesPerson;
    public String pdfUrl;
    public String webUrl;

    public void setoId(int oId) {
        this.oId = oId;
    }

    public void setoNumber(String oNumber) {
        this.oNumber = oNumber;
    }

    public void setoDate(String oDate) {
        this.oDate = oDate;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setSalesPerson(String salesPerson) {
        this.salesPerson = salesPerson;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
