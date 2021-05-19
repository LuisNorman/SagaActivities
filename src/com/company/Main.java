package com.company;

public class Main {

    public static void main(String[] args) throws Exception {

        // Saga activities - this saga passes all activities
        ReserveCar reserveCar = new ReserveCar();
        reserveCar.maxRand = 100;
        BookHotel bookHotel = new BookHotel();
        bookHotel.maxRand = 100;
        BookFlight bookFlight = new BookFlight();
        bookFlight.maxRand = 100;

        // Use saga builder to build saga activities
        SagaBuilder sagaBuilder = new SagaBuilder();
        sagaBuilder.activity(reserveCar);
        sagaBuilder.activity(bookHotel);
        sagaBuilder.activity(bookFlight);

        Saga saga = sagaBuilder.build(); // build the saga to run

        System.out.println("Did saga1 complete: " + saga.run() + "\n\n"); // run the saga



        // This saga fails on the last activity (Book flight)
        // Saga activities - use diff objects
        ReserveCar reserveCar2 = new ReserveCar();
        reserveCar2.maxRand = 100;
        BookHotel bookHotel2 = new BookHotel();
        bookHotel2.maxRand = 100;
        BookFlight bookFlight2 = new BookFlight();
        bookFlight2.maxRand = 0;

        SagaBuilder sagaBuilder2 = new SagaBuilder();
        sagaBuilder2.activity(reserveCar2);
        sagaBuilder2.activity(bookHotel2);
        sagaBuilder2.activity(bookFlight2);

        Saga saga2 = sagaBuilder2.build(); // build the saga to run

        System.out.println("Did saga2 complete: " + saga2.run() + "\n\n");

    }
}
