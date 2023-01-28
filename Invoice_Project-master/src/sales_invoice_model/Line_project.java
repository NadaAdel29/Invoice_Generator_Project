
package sales_invoice_model ;

public class Line_project {
    private String Item;
    private double Price;
    private int Count;
    private Invoice_project Invoice;

    public Line_project() {
    }

    public Line_project(String Item, double Price, int Count, Invoice_project Invoice) {
        this.Item = Item;
        this.Price = Price;
        this.Count = Count;
        this.Invoice = Invoice;
    }

    public double getLineTotal() {
        return Price * Count;
    }
    
    public int getCount() {
        return Count;
    }

    public void setCount(int Count) {
        this.Count = Count;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        this.Item = item;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        this.Price = price;
    }

    @Override
    public String toString() {
        return "Line{" + "num=" + Invoice.getNum() + ", item=" + Item + ", price=" + Price + ", count=" + Count + '}';
    }

    public Invoice_project getInvoice() {
        return Invoice;
    }
    
    public String getAsCSV() {
        return Invoice.getNum() + "," + Item + "," + Price + "," + Count;
    }
    
}
