package com.company;

import java.util.*;

// Represents an individual step in a saga which can be rolled back.
interface SagaActivity
{
    // Attempts to perform the activity.  Throws an Exception if the attempt
    // fails for whatever reason (e.g. a network failure)
    void runAction() throws Exception;

    // Undoes a previously-successful activity.  For example, if a saga
    // runAction() created a file, runCompensatingAction() would delete
    // the created file.  For the purposes of this assignment, this
    // compensating action is assumed to never fail.
    void runCompensatingAction();
}

// Reserve car saga activity
class ReserveCar implements SagaActivity {

    public boolean success = false;
    int val = 0;
    public int maxRand = 100;

    public void runAction() {
        Random rand = new Random();
        val = rand.nextInt(100); // Create random value between 0 - 100 and if num < 50 successful brand
        if (val <= maxRand) { // activity is successful if less than maxRand
            success = true;
            System.out.println("Reserved a car.");
        }
    }


    public void runCompensatingAction() {
        success = false;
        if (val > maxRand) { // if val > maxRand - this service failed
            System.out.println("Failed to reserve car.");
        }
        else { // rollback
            System.out.println("Canceled reserved car.");
        }

    }
}

// Book hotel activity
class BookHotel implements SagaActivity {

    public boolean success = false;
    int val = 0;
    public int maxRand = 100;

    @Override
    public void runAction() throws Exception {
        Random rand = new Random();
        val = rand.nextInt(100); // Create random value between 0 - 100 and if num < 50 successful brand
        if (val <= maxRand) { // activity is successful if less than maxRand
            success = true;
            System.out.println("Booked a hotel.");
        }

    }

    @Override
    public void runCompensatingAction() {
        if (val > maxRand) { // if val > maxRand - this service failed
            System.out.println("Failed to retrieve hotel reservations.");
        }
        else { // rollback
            System.out.println("Canceled hotel reservations.");
        }

    }
}

// Book flight activity
class BookFlight implements SagaActivity {

    public boolean success = false;
    int val = 0;
    public int maxRand = 100;

    @Override
    public void runAction() throws Exception {
        Random rand = new Random();
        val = rand.nextInt(100); // Create random value between 0 - 100 and if num < 50 successful brand

        if (val <= maxRand) { // activity is successful if less than maxRand
            success = true;
            System.out.println("Booked a flight.");
        }
    }

    @Override
    public void runCompensatingAction() {
        success = false;
        if (val > maxRand) { // if val > maxRand - this service failed
            System.out.println("Failed to retrieve flight reservations.");
        }
        else { // rollback but since it's the last service it would fail never rollback
            System.out.println("Canceled flight reservations.");
        }
    }
}

// Represents an entire distributed transaction.  Consists of one or more
// SagaActivitys.
class Saga {
    // Any constructors you deem appropriate
    // Executes the entire Saga by running each activity one-by-one.  If
    // any SagaActivity in the Saga fails, undoes all previously executed
    // activties by running their compensating actions.  Compensating actions
    // must run in reverse order of their original application.  Returns
    // true if the entire Saga completed successfully or false otherwise.

    // Create the saga activities to cast the saga activities in the queue
    ReserveCar reserveCar = new ReserveCar();
    BookHotel bookHotel = new BookHotel();
    BookFlight bookFlight = new BookFlight();

    public Queue<SagaActivity> activities = new LinkedList<>();

    public boolean run() throws Exception {

        int count = 0; // count to see which is the current activity for proper casting
        boolean result = false;

        // Loop through each activity and run it actions
        while (!activities.isEmpty()) {
            if (count == 0) {
                reserveCar = (ReserveCar) activities.poll(); // if count = 0, the sagaActivity will be reserve car
                reserveCar.runAction();

                if (reserveCar.success == false) {
                    // Since we failed to reserve car and car is first activity - we only need to roll back the reserve car
                    reserveCar.runCompensatingAction(); // rollback
                    break; // break loop - no need to continue
                }
            }
            else if (count == 1) {
                bookHotel = (BookHotel) activities.poll(); // if count = 1, the sagaActivity will be book hotel
                bookHotel.runAction();

                // since we failed at booking hotel - we need to rollback reserve car
                if (bookHotel.success == false) {
                    bookHotel.runCompensatingAction();
                    reserveCar.runCompensatingAction();
                    break;
                }
            }
            else if (count == 2) {
                bookFlight = (BookFlight) activities.poll(); // if count = 1, the sagaActivity will be book flight
                bookFlight.runAction();
                if (bookFlight.success == true) {
                    result = true; // if last activity passes, than saga passed
                }
                else { // saga failed at last stage so roll back
                    bookFlight.runCompensatingAction();
                    bookHotel.runCompensatingAction();
                    reserveCar.runCompensatingAction();
                    break;
                }

            }

            ++count;
        }
        return result;
    }
}


// A utility class to make constructing sagas easier.
class SagaBuilder {

    Queue<SagaActivity> activities = new LinkedList<>(); // Create saga class

    // Add the specified activity to the to-be-built saga.
    public SagaBuilder activity(SagaActivity activity) {
        this.activities.add(activity);  // add activity to queue
        return this;
    }

    // Builds the saga by making the saga activities = to total activities
    public Saga build() {
        Saga saga = new Saga();
        saga.activities = activities;
        return saga;
    }

}
