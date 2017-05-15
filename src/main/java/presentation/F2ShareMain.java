/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import presentation.swing.F2ShareMenu;
import presentation.swing.LanguageChooserDialog;

import javax.swing.*;
import java.util.Locale;

/**
 * Represents the application startup.
 *
 * @author Ivo Ferro
 * @author Pedro Fernandes
 * @author Renato Olveira
 */
public class F2ShareMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Locale locale = LanguageChooserDialog.chooseLanguage();
        Locale.setDefault(locale);
        JOptionPane.setDefaultLocale(locale);

        final F2ShareMenu F = new F2ShareMenu();

    }

}
