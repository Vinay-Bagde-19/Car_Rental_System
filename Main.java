import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Car{
    private String carID;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable;

    public Car(String carID, String brand, String model, double basePricePerDay) {
        this.carID = carID;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getCarID() {
        return carID;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double getBasePricePerDay() {
        return basePricePerDay;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent(){
        isAvailable = false;
    }

    public void returnCar(){
        isAvailable = true;
    }

    public double calculatePrice(int rentalDays){
        return basePricePerDay * rentalDays;
    }
}

class Customer{
    private String customerID;
    private String name;
    private String phoneNo;

    public Customer(String customerID, String name, String phoneNo) {
        this.customerID = customerID;
        this.name = name;
        this.phoneNo = phoneNo;
    }

    public Customer(String customerID, String name) {
        this.customerID = customerID;
        this.name = name;
        this.phoneNo = "NA";
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }
}

class Rental{
    private Car car;     // car variable of type Car
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days){
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

class CarRentalSystem{
    private List<Car> cars;        //Car type arrayList "car" to  store Car class objects
    private List<Customer> customers;      //Customer type arrayList "customer" to  store Customer class objects
    private List<Rental> rentals;      //Rental type arrayList "rentals" to  store Rental class objects

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car){
        cars.add(car);
    }

    public void addCustomer(Customer customer){
        customers.add(customer);
    }

    public void addRental(Car car, Customer customer, int days){
        if (car.isAvailable()){
            car.rent();
            rentals.add(new Rental(car, customer, days));

        }else {
            System.out.println("Car is not available for rent.");
        }
    }

    public void returnCar(Car car){
        Rental rentalToRemove = null;
        for (Rental rental: rentals){
            if (rental.getCar() == car){
                rentalToRemove = rental;
                break;
            }
        }
        if (rentalToRemove != null){
            rentals.remove(rentalToRemove);
        }else {
            System.out.println("Car was not rented.");
        }
    }

    public void menu(){
        Scanner input = new Scanner(System.in);

        while (true){
            System.out.println("********** Car Rental System **********");
            System.out.println("1. Rent a Car");
            System.out.println("2. Return a Car");
            System.out.println("3. Exit");
            System.out.println("Enter Your Choice: ");

            int choice = input.nextInt();
            input.nextLine(); // Consume a new Line

            if (choice == 1){
                System.out.println("\n**** Rent a car ****\n");
                System.out.println("Enter your name: ");
                String customerName = input.nextLine();

                System.out.println("\nAvailable Cars: ");
                for (Car car: cars){
                    if (car.isAvailable()){
                        System.out.println(car.getCarID() + " - " + car.getBrand() + "  " + car.getModel());
                    }
                }

                System.out.println("\nEnter the car ID you want to rent: ");
                String carID = input.nextLine().toUpperCase();

                System.out.println("Enter the number of days for rental: ");
                int days = input.nextInt();
                input.nextLine();  // Consume newLine

                Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
                addCustomer(newCustomer);

                Car selectedCar = null;
                for (Car car : cars){
                    if(car.getCarID().equals(carID) && car.isAvailable()){
                        selectedCar = car;
                        break;
                    }
                }

                if (selectedCar != null){
                    double totalPrice = selectedCar.calculatePrice(days);
                    System.out.println("\n## Rental Information ##\n");
                    System.out.println("Customer ID: " + newCustomer.getCustomerID());
                    System.out.println("Customer Name: " + newCustomer.getName());
                    System.out.println("Customer PhoneNo.: " + newCustomer.getPhoneNo());
                    System.out.println("Car: " + selectedCar.getCarID() + " " + selectedCar.getModel() + " " + selectedCar.getBrand());
                    System.out.println("Rental Days: " + days);
                    System.out.printf("1 Day price to rent %s is ₹%.2f %n",selectedCar.getModel(), selectedCar.getBasePricePerDay());
                    System.out.printf("Total Price: ₹%.2f\n",totalPrice);

                    System.out.println("\nConfirm rental (Y/N): ");
                    String confirm = input.nextLine();

                    if (confirm.equalsIgnoreCase("Y")){
                        addRental(selectedCar, newCustomer, days);
                        System.out.println("\nCar rented Successfully.");
                    }else {
                        System.out.println("\nRental canceled");
                    }

                }else {
                    System.out.println("\nInvalid car selected or car not available for rent.");
                }
            } else if (choice == 2) {
                System.out.println("\n**** Return a Car ****\n");
                System.out.println("Enter the car ID you want to return: ");
                String carID = input.nextLine().toUpperCase();

                Car carToReturn = null;
                for (Car car : cars){
                    if (car.getCarID().equals(carID) && !car.isAvailable()){
                        carToReturn = car;
                        break;
                    }
                }

                if (carToReturn != null){
                    Customer customer = null;
                    for (Rental rental : rentals){
                        if(rental.getCar() == carToReturn){
                            customer = rental.getCustomer();
                            break;
                        }
                    }

                    if (customer != null){
                        returnCar(carToReturn);
                        System.out.println("\nCar returned Successfully by " + customer.getName());
                    }else{
                        System.out.println("\nCar was not rented or rental information missing.");
                    }
                }else {
                    System.out.println("Invalid car ID or car is not rented.");
                }
            } else if (choice == 3) {
                break;
            }else {
                System.out.println("Invalid choice!! Please enter a valid option.");
            }
        }

        System.out.println("\nThank you for using the Car Rental System!");
    }
}

public class Main {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        Car car1 = new Car("C001","Mahindra","Thar",1500);
        Car car2 = new Car("C002","Toyota","Corolla",1000);
        Car car3 = new Car("C003","Ford","Mustang",2500);
        Car car4 = new Car("C004","Rolls-Royce","Phantom",3000);

        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);
        rentalSystem.addCar(car4);

        rentalSystem.menu();
    }
}

