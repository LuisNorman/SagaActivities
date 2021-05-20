package com.company;

public class Main {

    public static void main(String[] args) throws Exception {

        // Saga activities - this saga passes all activities
        ReserveCar reserveCar = new ReserveCar();
        reserveCar.maxRand = 100; // since saga activity val num is 100 this will always pass
        BookHotel bookHotel = new BookHotel();
        bookHotel.maxRand = 100; // since saga activity val is 100 this will always pass
        BookFlight bookFlight = new BookFlight();
        bookFlight.maxRand = 100; // since saga activity val is 100 this will always pass

        // Note: if max rand is set at 50 that means there is a 50% chance of it passing
        // Use saga builder to build saga activities
        SagaBuilder sagaBuilder = new SagaBuilder();
        sagaBuilder.activity(reserveCar);
        sagaBuilder.activity(bookHotel);
        sagaBuilder.activity(bookFlight);

        Saga saga = sagaBuilder.build(); // build the saga to run

        boolean success = saga.run(); // run the saga and get results

        System.out.println("Did saga1 complete: " + success + "\n\n"); // run the saga



        // This saga fails on the last activity (Book flight)
        // Saga activities - use diff objects
        ReserveCar reserveCar2 = new ReserveCar();
        reserveCar2.maxRand = 100; // since saga activity val num is 100 this will always pass
        BookHotel bookHotel2 = new BookHotel();
        bookHotel2.maxRand = 100; // since saga activity val is 100 this will always pass
        BookFlight bookFlight2 = new BookFlight();
        bookFlight2.maxRand = -1; // since min ran value is 0 this will always fail

        // Note: if max rand is set at 0 < it will always fail
        // Use saga builder to build saga activities
        SagaBuilder sagaBuilder2 = new SagaBuilder();
        sagaBuilder2.activity(reserveCar2);
        sagaBuilder2.activity(bookHotel2);
        sagaBuilder2.activity(bookFlight2);

        Saga saga2 = sagaBuilder2.build(); // build the saga to run

        success = saga2.run(); // Run the saga and get the results
        // Print saga results
        System.out.println("Did saga2 complete: " + success+ "\n\n");

    }
}
