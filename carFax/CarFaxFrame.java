package kptProject3;

import java.util.*;
import javax.swing.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.*;

public class CarFaxFrame extends JFrame implements ActionListener {
    // GUI and logic class
    
	JPanel panel;
    JLabel lblTitle, lblVin, lblMake, lblModel, lblYear;
    JButton btnFind, btnAdd, btnDelete, btnClear, btnUpdate;
    JTextField txtVin, txtMake, txtModel, txtYear, txtFind;
    
    String vin = "";
    String make = "";
    String model = "";
    int year = 0;
    
    public CarFaxFrame()
    {
    	setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		GridLayout thisLayout = new GridLayout();
		
		getContentPane().setLayout(thisLayout);
		
		panel = new JPanel();
		getContentPane().add(panel);
		GridBagLayout panelLayout = new GridBagLayout();
		
		panel.setLayout(panelLayout);
		
		lblTitle = new JLabel();
		panel.add(lblTitle, gc(1, 0, 4, 1));
		lblTitle.setText("Welcome to Java DB CarFax");
		
		lblVin = new JLabel();
		panel.add(lblVin, gc(0, 1, 1, 1));
		lblVin.setText("VIN: ");

		lblMake = new JLabel();
		panel.add(lblMake, gc(0, 2, 1, 1));
		lblMake.setText("Car Make");

		lblModel = new JLabel();
		panel.add(lblModel, gc(0, 3, 1, 1));
		lblModel.setText("Car Model");

		lblYear = new JLabel();
		panel.add(lblYear, gc(0, 4, 1, 1));
		lblYear.setText("Car Year");
		
		txtVin = new JTextField();
		panel.add(txtVin, gc(1, 1, 4, 1));
		
		txtMake = new JTextField();
		panel.add(txtMake, gc(1, 2, 4, 1));
		
		txtModel = new JTextField();
		panel.add(txtModel, gc(1, 3, 4, 1));
		
		txtYear = new JTextField();
		panel.add(txtYear, gc(1, 4, 4, 1));
		
		btnAdd = new JButton();
		panel.add(btnAdd, gc(1, 5, 1, 1));
		btnAdd.setText("Add");
		
		btnUpdate = new JButton();
		panel.add(btnUpdate, gc(2, 5, 1, 1));
		btnUpdate.setText("Update");
		
		btnDelete = new JButton();
		panel.add(btnDelete, gc(3, 5, 1, 1));
		btnDelete.setText("Delete");
		
		btnClear = new JButton();
		panel.add(btnClear, gc(4, 5, 1, 1));
		btnClear.setText("Clear");
		
		btnFind = new JButton();
		panel.add(btnFind, gc(1, 6, 1, 1));
		btnFind.setText("Find");
		
		txtFind = new JTextField();
		panel.add(txtFind, gc(2, 6, 3, 1));
		
		btnAdd.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnDelete.addActionListener(this);
		btnClear.addActionListener(this);
		btnFind.addActionListener(this);
		
		btnClear.setToolTipText("Clears all info in the form");
		btnFind.setToolTipText("Checks databse for a car with the given vin");
		
		resetForm();
		pack();
		this.setSize(420, 450);
		this.setLocation(300, 100);
		this.setVisible(true);
		this.setTitle("Car Fax App");
    }	
    
    private void resetForm()
	{
		// clears the form

    	txtVin.setText("");
    	txtMake.setText("");
    	txtModel.setText("");
    	txtYear.setText("");
    	txtFind.setText("");
		
	}

	private GridBagConstraints gc(int x, int y, int w, int h)
	{
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = w;
		c.gridheight = h;
		
		c.anchor = GridBagConstraints.WEST;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.insets = new Insets(15, 5, 15, 5);
		
		return c;
	}
    
    public void actionPerformed(ActionEvent e)
    {
    	JButton source = (JButton) e.getSource();
		
		if (source == btnAdd)
		{
			add();
		}
		else if (source == btnUpdate)
		{
			update();
		}
		else if (source == btnDelete)
		{
			delete();
		}
		else if (source == btnClear)
		{
			resetForm();
		}
		else if (source == btnFind)
		{
			find();
		}
		
		
		
	CarFaxDB.clearErrors();
	CarFaxValidator.clearErrors();
    }

	private void find()
	{
		// Checks the database for a car with the given vin
		// If found displays info in the forms
		
		String vin = CarFaxValidator.getValidVin(txtFind.getText());
		if ( ! vin.equals(""))
		{
			Car car = CarFaxDB.select(vin);
			if (car != null)
			{
				txtVin.setText(car.getVin());
				txtMake.setText(car.getMake());
				txtModel.setText(car.getModel());
				txtYear.setText(String.valueOf(car.getYear()));
				
				String message = "Car Found\n\n";
				message += "Vin:      " + car.getVin() + "\n";
				message += "Make:  " + car.getMake() + "\n";
				message += "Model: " + car.getModel() + "\n";
				message += "Year:    " + car.getYear() + "\n";
				JOptionPane.showMessageDialog(null, message);
			}
			else 
			{
				JOptionPane.showMessageDialog(null, CarFaxDB.getErrors());
			}
		}
		else 
		{
			JOptionPane.showMessageDialog(null, CarFaxValidator.getErrors());
		}
		
	}

	private void delete()
	{
		// deletes a car from the database with the given vin
		
		String vin = CarFaxValidator.getValidVin(txtVin.getText());
		if ( ! vin.equals(""))
		{
			if (CarFaxDB.delete(vin))
			{
				JOptionPane.showMessageDialog(null, "Car " + vin + " successfully deleted");
			}
			else
			{
				JOptionPane.showMessageDialog(null, CarFaxDB.getErrors());
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, CarFaxValidator.getErrors());
		}
	}

	private void update()
	{
		// looks for a car with the given vin in the DB
		// if found updates it to match the info currently in the form

		if (validateFields())
		{
			Car car = new Car(vin, make, model, year);
			CarFaxDB.update(car);
			
			if (CarFaxDB.getErrors().equals(""))
			{
				JOptionPane.showMessageDialog(null, "Car " + vin + " updated");
			}
			else 
			{
				JOptionPane.showMessageDialog(null, CarFaxDB.getErrors());
			}
		}
		else
		{
			String message = "Input Error\n" + CarFaxValidator.getErrors();
			JOptionPane.showMessageDialog(null, message);
		}
		
		
	}

	private void add()
	{
		// adds a car to the DB based on current info
		
		if (validateFields())
		{
			Car car = new Car(vin, make, model, year);
			CarFaxDB.add(car);
			
			if (CarFaxDB.getErrors().equals(""))
			{
				JOptionPane.showMessageDialog(null, "Car added to database");
			}
			else 
			{
				JOptionPane.showMessageDialog(null, CarFaxDB.getErrors());
			}
		}
		else
		{
			String message = "Input Error\n" + CarFaxValidator.getErrors();
			JOptionPane.showMessageDialog(null, message);
		}
		
		
	}
	
	private Boolean validateFields()
	{
		// takes the text from the vin, make, model, and year fields and checks for validation
		// returns true if they are valid and assigns them to global variables
		// otherwise returns false and sets globals to empty strings
		
		Boolean output = false;
		
		vin = CarFaxValidator.getValidVin(txtVin.getText());
		make = CarFaxValidator.getValidMake(txtMake.getText());
		model = CarFaxValidator.getValidModel(txtModel.getText());
		year = CarFaxValidator.getValidYear(txtYear.getText());
		
		if (! vin.equals("") && ! make.equals("") && ! model.equals("") && year != 0)
		{
			output = true;
		}
		else
		{
			vin = "";
			make = "";
			model = "";
			year = 0;
		}
		
		return output;
	}
}
