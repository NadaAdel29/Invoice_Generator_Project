package sales_invoice_controller;

import sales_invoice_model.Invoice_project;
import sales_invoice_model.Invoice_Table_Model;
import sales_invoice_model.Line_project;
import sales_invoice_model.Lines_Table_Model;

import sales_invoice_view.Invoice_Frame_project;
import sales_invoice_view.Line_Dialog_project;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import sales_invoice_view.Invoice_Dialog;

public class Controller_project implements ActionListener, ListSelectionListener {

    private Invoice_Frame_project Frame;
    private Invoice_Dialog invoice_Dialog;
    private Line_Dialog_project line_Dialog;

    public Controller_project(Invoice_Frame_project frame) {
        this.Frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println("Action: " + actionCommand);
        switch (actionCommand) {
            case "Load File":
                loadFile();
                break;
            case "Save File":
                saveFile();
                break;
            case " New Invoice":
                NewInvoice();
                break;
            case "Delete Invoice":
                deleteInvoice();
                break;
            case " New Item":
                NewItem();
                break;
            case "Delete Item":
                deleteItem();
                break;
            case "NewInvoiceCancel":
                NewInvoiceCancel();
                break;
            case "NewInvoiceOK":
                NewInvoiceOK();
                break;
            case "NewItemOK":
                NewItemOK();
                break;
            case "NewItemCancel":
                NewItemCancel();
                break;
        } 
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedIndex = Frame.getInvoiceTable().getSelectedRow();
        if (selectedIndex != -1) {
            System.out.println("You have selected row: " + selectedIndex);
            Invoice_project currentInvoice = Frame.getInvoices().get(selectedIndex);
            Frame.getInvoiceNumLabel().setText("" + currentInvoice.getNum());
            Frame.getInvoiceDateLabel().setText(currentInvoice.getDate());
            Frame.getCustomerNameLabel().setText(currentInvoice.getCustomer());
            Frame.getInvoiceTotalLabel().setText("" + currentInvoice.getInvoiceTotal());
            Lines_Table_Model linesTableModel = new Lines_Table_Model(currentInvoice.getLines());
            Frame.getLineTable().setModel(linesTableModel);
            linesTableModel.fireTableDataChanged();
        }
    }

    private void loadFile() {
        JFileChooser fc = new JFileChooser();
        try {
            int result = fc.showOpenDialog(Frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                Path headerPath = Paths.get(headerFile.getAbsolutePath());
                List<String> headerLines = Files.readAllLines(headerPath);
                System.out.println("Invoices have been read");
                // 1,22-11-2020,Ali
                // 2,13-10-2021,Saleh
                // 3,09-01-2019,Ibrahim
                ArrayList<Invoice_project> invoicesArray = new ArrayList<>();
                for (String headerLine : headerLines) {
                    try {
                        String[] headerParts = headerLine.split(",");
                        int invoiceNum = Integer.parseInt(headerParts[0]);
                        String invoiceDate = headerParts[1];
                        String customerName = headerParts[2];

                        Invoice_project invoice = new Invoice_project(invoiceNum, invoiceDate, customerName);
                        invoicesArray.add(invoice);
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(Frame, "Error in line format", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                System.out.println("Check point");
                result = fc.showOpenDialog(Frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fc.getSelectedFile();
                    Path linePath = Paths.get(lineFile.getAbsolutePath());
                    List<String> lineLines = Files.readAllLines(linePath);
                    System.out.println("Lines have been read");
                    for (String lineLine : lineLines) {
                        try {
                            String lineParts[] = lineLine.split(",");
                            int invoiceNum = Integer.parseInt(lineParts[0]);
                            String itemName = lineParts[1];
                            double itemPrice = Double.parseDouble(lineParts[2]);
                            int count = Integer.parseInt(lineParts[3]);
                            Invoice_project inv = null;
                            for (Invoice_project invoice : invoicesArray) {
                                if (invoice.getNum() == invoiceNum) {
                                    inv = invoice;
                                    break;
                                }
                            }

                            Line_project line = new Line_project(itemName, itemPrice, count, inv);
                            inv.getLines().add(line);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(Frame, "Error in line format", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    System.out.println("Check point");
                }
                Frame.setInvoices(invoicesArray);
                Invoice_Table_Model invoicesTableModel = new Invoice_Table_Model(invoicesArray);
                Frame.setInvoicesTableModel(invoicesTableModel);
                Frame.getInvoiceTable().setModel(invoicesTableModel);
                Frame.getInvoicesTableModel().fireTableDataChanged();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(Frame, "Cannot read file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveFile() {
        ArrayList<Invoice_project> invoices = Frame.getInvoices();
        String headers = "";
        String lines = "";
        for (Invoice_project invoice : invoices) {
            String invCSV = invoice.getAsCSV();
            headers += invCSV;
            headers += "\n";

            for (Line_project line : invoice.getLines()) {
                String lineCSV = line.getAsCSV();
                lines += lineCSV;
                lines += "\n";
            }
        }
        System.out.println("Check point");
        try {
            JFileChooser fc = new JFileChooser();
            int result = fc.showSaveDialog(Frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                FileWriter hfw = new FileWriter(headerFile);
                hfw.write(headers);
                hfw.flush();
                hfw.close();
                result = fc.showSaveDialog(Frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fc.getSelectedFile();
                    FileWriter lfw = new FileWriter(lineFile);
                    lfw.write(lines);
                    lfw.flush();
                    lfw.close();
                }
            }
        } catch (Exception ex) {

        }
    }

    private void createNewInvoice() {
        invoice_Dialog = new Invoice_Dialog(Frame);
        invoice_Dialog.setVisible(true);
    }

    private void deleteInvoice() {
        int selectedRow = Frame.getInvoiceTable().getSelectedRow();
        if (selectedRow != -1) {
            Frame.getInvoices().remove(selectedRow);
            Frame.getInvoicesTableModel().fireTableDataChanged();
        }
    }

    private void NewItem() {
        line_Dialog = new Line_Dialog_project(Frame);
        line_Dialog.setVisible(true);
    }

    private void deleteItem() {
        int selectedRow = Frame.getLineTable().getSelectedRow();

        if (selectedRow != -1) {
            Lines_Table_Model linesTableModel = (Lines_Table_Model) Frame.getLineTable().getModel();
            linesTableModel.getLines().remove(selectedRow);
            linesTableModel.fireTableDataChanged();
            Frame.getInvoicesTableModel().fireTableDataChanged();
        }
    }

    private void NewInvoiceCancel() {
        invoice_Dialog.setVisible(false);
        invoice_Dialog.dispose();
        invoice_Dialog = null;
    }

    private void NewInvoiceOK() {
        String date = invoice_Dialog.getInvDateField().getText();
        String customer = invoice_Dialog.getCustNameField().getText();
        int num = Frame.getNextInvoiceNum();
        try {
            String[] dateParts = date.split("-");  // "22-05-2013" -> {"22", "05", "2013"}  xy-qw-20ij
            if (dateParts.length < 3) {
                JOptionPane.showMessageDialog(Frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                if (day > 31 || month > 12) {
                    JOptionPane.showMessageDialog(Frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    Invoice_project invoice = new Invoice_project(num, date, customer);
                    Frame.getInvoices().add(invoice);
                    Frame.getInvoicesTableModel().fireTableDataChanged();
                    invoice_Dialog.setVisible(false);
                    invoice_Dialog.dispose();
                    invoice_Dialog = null;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(Frame, "Wrong date format", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void NewItemOK() {
        String item = line_Dialog.getItemNameField().getText();
        String countStr = line_Dialog.getItemCountField().getText();
        String priceStr = line_Dialog.getItemPriceField().getText();
        int count = Integer.parseInt(countStr);
        double price = Double.parseDouble(priceStr);
        int selectedInvoice = Frame.getInvoiceTable().getSelectedRow();
        if (selectedInvoice != -1) {
            Invoice_project invoice = Frame.getInvoices().get(selectedInvoice);
            Line_project line = new Line_project(item, price, count, invoice);
            invoice.getLines().add(line);
            Lines_Table_Model linesTableModel = (Lines_Table_Model) Frame.getLineTable().getModel();
            linesTableModel.fireTableDataChanged();
            Frame.getInvoicesTableModel().fireTableDataChanged();
        }
        line_Dialog.setVisible(false);
        line_Dialog.dispose();
        line_Dialog = null;
    }

    private void NewItemCancel() {
        line_Dialog.setVisible(false);
        line_Dialog.dispose();
        line_Dialog = null;
    }

    private void NewInvoice() {
        invoice_Dialog = new Invoice_Dialog(Frame);
        invoice_Dialog.setVisible(true);
    }

  
    }


