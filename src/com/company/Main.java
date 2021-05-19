package com.company;

public class Main {

    public static void main(String[] args) throws Exception {

        // Saga activities
        ReserveCar reserveCar = new ReserveCar();
        BookHotel bookHotel = new BookHotel();
        BookFlight bookFlight = new BookFlight();


        // Use saga builder to build saga activities
        SagaBuilder sagaBuilder = new SagaBuilder();
        sagaBuilder.activity(reserveCar);
        sagaBuilder.activity(bookHotel);
        sagaBuilder.activity(bookFlight);

        Saga saga = sagaBuilder.build(); // build the saga to run

        System.out.println("Did saga complete: " + saga.run()); // run the saga
    }
}
