
package sales_invoice_model;

import java.util.ArrayList;

public class Invoice_project {
    private int number;
    private String Date;
    private String Customer;
    private ArrayList<Line_project> lines;
    
    public Invoice_project() {
    }

    public Invoice_project(int num, String date, String customer) {
        this.number = num;
        this.Date = date;
        this.Customer = customer;
    }

    public double getInvoiceTotal() {
        double total = 0.0;
        for (Line_project line : getLines()) {
            total += line.getLineTotal();
        }
        return total;
    }
    
    public ArrayList<Line_project> getLines() {
        if (lines == null) {
            lines = new ArrayList<>();
        }
        return lines;
    }

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String customer) {
        this.Customer = customer;
    }

    public int getNum() {
        return number;
    }

    public void setNum(int num) {
        this.number = num;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    @Override
    public String toString() {
        return "Invoice{" + "number=" + number + ", Date=" + Date + ", Customer=" + Customer + '}';
    }
    
    public String getAsCSV() {
        return number + "," + Date + "," + Customer;
    }
    
}
