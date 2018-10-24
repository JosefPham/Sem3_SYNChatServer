package Persistence;

import Acquaintance.IPersistence;


public class TestMain {
    
    public static void main(String[] args) {
        IPersistence facade = PersistenceFacade.getInstance();
    }

}
