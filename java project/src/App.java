import javax.swing.*;

public class App {
    public static void main(String[] args) {
        int boardWidth = 760; // Updated to match the Aero class
        int boardHeight = 640;

        JFrame frame = new JFrame("Aeroplane obstacles");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Aero aero = new Aero(); // Use the correct class name here
        frame.add(aero);
        frame.pack();
        aero.requestFocus();
        frame.setVisible(true);
    }
}

