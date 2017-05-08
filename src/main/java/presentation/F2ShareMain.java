/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import java.io.IOException;
import java.util.Timer;

import connection.AnnounceTimerTask;
import domain.Configuration;
import presentation.swing.F2ShareMenu;

/**
 *
 * @author Pedro Fernandes
 */
public class F2ShareMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        // FIXME either make configuration a singleton or instantiate it here

        // TODO not sure yet where this is going to be
        int secondsToAnnounce = 30;
        Timer timer = new Timer();
        timer.schedule(new AnnounceTimerTask(), 0, secondsToAnnounce * 1000);

        final F2ShareMenu F = new F2ShareMenu();

    }
    
}
