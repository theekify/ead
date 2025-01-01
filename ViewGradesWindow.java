import javax.swing.*;
import java.awt.*;

public class ViewGradesWindow extends JFrame {
    public ViewGradesWindow() {
        setTitle("View Grades");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        // Add components for viewing grades
        panel.add(new JLabel("Your Grades:"));
        panel.add(new JLabel("Course 1: A"));
        panel.add(new JLabel("Course 2: B"));
        panel.add(new JLabel("Course 3: C"));
        panel.add(new JLabel("Course 4: A"));
        panel.add(new JLabel("Course 5: B"));

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ViewGradesWindow().setVisible(true);
            }
        });
    }
}