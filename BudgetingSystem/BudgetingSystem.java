import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class BudgetingSystem {
    private JFrame frame;
    private String currentUser = "";
    private JPanel mainPanel, adminPanel, studentPanel, userRolePanel, programMenuPanel;
    private CardLayout cardLayout;

    int PANEL_SPENDING_X = 100;
    int PANEL_SPENDING_Y = 100;

    int PANEL_SAVING_X = 550;
    int PANEL_SAVING_Y = 100;

    int PANEL_HEALTH_X = 100;
    int PANEL_HEALTH_Y = 380;

    int PANEL_FEEDBACK_X = 550;
    int PANEL_FEEDBACK_Y = 380;

    public BudgetingSystem() {

        // ⭐ GLOBAL FONT OVERRIDE FOR ALL COMPONENTS
        FontUIResource generalFont = new FontUIResource("Arial", Font.PLAIN, 20);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, generalFont);
            }
        }

        // ⭐ GLOBAL BUTTON STYLE
        UIManager.put("Button.font", new FontUIResource("Arial", Font.BOLD, 20));
        UIManager.put("Button.margin", new Insets(10, 20, 10, 20));

        // ⭐ GLOBAL TEXTFIELD, LABEL, COMBOBOX STYLE
        UIManager.put("TextField.font", new FontUIResource("Arial", Font.PLAIN, 20));
        UIManager.put("Label.font", new FontUIResource("Arial", Font.PLAIN, 20));
        UIManager.put("ComboBox.font", new FontUIResource("Arial", Font.PLAIN, 20));

        // ⭐ GLOBAL TABLE STYLE
        Font tableFont = new Font("Arial", Font.PLAIN, 18);
        Font tableHeaderFont = new Font("Arial", Font.BOLD, 20);
        int tableRowHeight = 32;

        UIManager.put("Table.font", tableFont);
        UIManager.put("Table.rowHeight", tableRowHeight);
        UIManager.put("Table.foreground", Color.BLACK);
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.gridColor", Color.GRAY);
        UIManager.put("TableHeader.font", tableHeaderFont);
        UIManager.put("TableHeader.foreground", Color.BLACK);
        UIManager.put("TableHeader.background", new Color(200, 200, 200));

        // ⭐ CREATE FRAME
        frame = new JFrame("Budgeting Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ⭐ FORCE MAXIMIZE WINDOW ON LAUNCH
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false); // lock size to full screen

        // ⭐ CARD LAYOUT SETUP
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // ⭐ INITIALIZE ALL PANELS
        initializeUserRolePanel();
        initializeStudentPanel();
        initializeAdminPanel();
        initializeProgramMenuPanel();

        mainPanel.add(userRolePanel, "User Role");
        mainPanel.add(studentPanel, "Student Menu");
        mainPanel.add(adminPanel, "Admin Menu");
        mainPanel.add(programMenuPanel, "Program Menu");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

private void initializeUserRolePanel() {
    userRolePanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon bg = new ImageIcon(getClass().getResource("role_bg.jpeg"));
            g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    };
    userRolePanel.setLayout(new GridBagLayout());

    // ---------------------------------------------------------
    // MESSAGE BOX PANEL (for label)
    // ---------------------------------------------------------
    JPanel messageBox = new JPanel(new GridBagLayout());
    messageBox.setBackground(new Color(0, 0, 0, 150));
    messageBox.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

    JLabel label = new JLabel("<html><center>Welcome!<br>Select User Role</center></html>");
    label.setFont(new Font("Arial", Font.BOLD, 25));
    label.setForeground(Color.WHITE);
    messageBox.add(label);

    // ---------------------------------------------------------
    // BUTTONS
    // ---------------------------------------------------------
    JButton studentButton = createStyledButton("Student");
    JButton adminButton = createStyledButton("Admin");
    JButton exitButton = createStyledButton("Exit Program");

    Color buttonColor = Color.decode("#2b643b");
    JButton[] buttons = {studentButton, adminButton, exitButton};
    for (JButton btn : buttons) {
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
    }

    studentButton.addActionListener(e -> cardLayout.show(mainPanel, "Student Menu"));
    adminButton.addActionListener(e -> cardLayout.show(mainPanel, "Admin Menu"));
    exitButton.addActionListener(e -> System.exit(0));

    // ---------------------------------------------------------
    // GRIDBAG CONSTRAINTS
    // ---------------------------------------------------------
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.insets = new Insets(20, 0, 20, 0); // vertical spacing
    gbc.anchor = GridBagConstraints.CENTER; // default: center

// ---------------------------------------------------------
// SLIGHTLY MOVE RIGHT OR LEFT & UP/DOWN
// ---------------------------------------------------------
int horizontalOffset = 600; // positive → move right, negative → move left
int verticalOffset = 90;    // positive → move down, negative → move up

// Message box
gbc.gridy = 0;
gbc.insets = new Insets(20 + verticalOffset, horizontalOffset, 20, 0);
userRolePanel.add(messageBox, gbc);

// Student button
gbc.gridy = 1;
gbc.insets = new Insets(20, horizontalOffset, 20, 0);
userRolePanel.add(studentButton, gbc);

// Admin button
gbc.gridy = 2;
gbc.insets = new Insets(20, horizontalOffset, 20, 0);
userRolePanel.add(adminButton, gbc);

// Exit button
gbc.gridy = 3;
gbc.insets = new Insets(20, horizontalOffset, 20, 0);
userRolePanel.add(exitButton, gbc);

}

private void initializeStudentPanel() {
    studentPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon bg = new ImageIcon(getClass().getResource("role_bg.jpeg"));
            g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    };
    studentPanel.setLayout(new GridBagLayout());

    // ---------------------------------------------------------
    // MESSAGE BOX PANEL (for label)
    // ---------------------------------------------------------
    JPanel messageBox = new JPanel(new GridBagLayout());
    messageBox.setBackground(new Color(0, 0, 0, 150)); // semi-transparent black
    messageBox.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

    JLabel label = new JLabel("<html><center>Welcome to the Budgeting Program</center></html>");
    label.setFont(new Font("Arial", Font.BOLD, 25));
    label.setForeground(Color.WHITE);

    messageBox.add(label);

    // ---------------------------------------------------------
    // BUTTONS
    // ---------------------------------------------------------
    JButton signInButton = createStyledButton("Sign In");
    JButton signUpButton = createStyledButton("Sign Up");
    JButton backButton = createStyledButton("Exit");

    Color buttonColor = Color.decode("#2b643b"); // same green color as user role panel
    JButton[] buttons = {signInButton, signUpButton, backButton};
    for (JButton btn : buttons) {
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
    }

    signInButton.addActionListener(e -> signIn());
    signUpButton.addActionListener(e -> signUp());
    backButton.addActionListener(e -> cardLayout.show(mainPanel, "User Role"));

    // ---------------------------------------------------------
    // GRIDBAG CONSTRAINTS
    // ---------------------------------------------------------
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.CENTER;

    // Horizontal and vertical positioning
    int horizontalOffset = 600; // positive → move right, negative → move left
    int verticalOffset = 90;    // positive → move down, negative → move up

    // Message box
    gbc.gridy = 0;
    gbc.insets = new Insets(25 + verticalOffset, horizontalOffset, 25, 0);
    studentPanel.add(messageBox, gbc);

    // Buttons
    gbc.gridy = 1;
    gbc.insets = new Insets(25, horizontalOffset, 25, 0);
    studentPanel.add(signInButton, gbc);

    gbc.gridy = 2;
    studentPanel.add(signUpButton, gbc);

    gbc.gridy = 3;
    studentPanel.add(backButton, gbc);
}

private void initializeAdminPanel() {
    adminPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon bg = new ImageIcon(getClass().getResource("admin_bg.jpeg"));
            g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    };
    adminPanel.setLayout(new GridBagLayout());

    // ---------------------------------------------------------
    // WELCOME MESSAGE BOX
    // ---------------------------------------------------------
    JPanel messageBox = new JPanel(new GridBagLayout());
    messageBox.setBackground(new Color(0, 0, 0, 150)); // semi-transparent black
    messageBox.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

    JLabel titleLabel = new JLabel("<html><center>Welcome to the Admin System</center></html>");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
    titleLabel.setForeground(Color.WHITE);
    messageBox.add(titleLabel);

    // ---------------------------------------------------------
    // USERNAME & PASSWORD BOXES
    // ---------------------------------------------------------
    JPanel userBox = new JPanel(new GridBagLayout());
    userBox.setBackground(new Color(0, 0, 0, 120)); // slightly transparent
    userBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JLabel userLabel = new JLabel("Username: ");
    userLabel.setFont(new Font("Arial", Font.PLAIN, 24));
    userLabel.setForeground(Color.WHITE);
    JTextField usernameField = new JTextField(20);

    GridBagConstraints gbcUser = new GridBagConstraints();
    gbcUser.gridx = 0;
    gbcUser.gridy = 0;
    gbcUser.anchor = GridBagConstraints.WEST;
    gbcUser.insets = new Insets(5, 5, 5, 5);
    userBox.add(userLabel, gbcUser);

    gbcUser.gridx = 1;
    userBox.add(usernameField, gbcUser);

    JPanel passBox = new JPanel(new GridBagLayout());
    passBox.setBackground(new Color(0, 0, 0, 120));
    passBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JLabel passLabel = new JLabel("Password: ");
    passLabel.setFont(new Font("Arial", Font.PLAIN, 24));
    passLabel.setForeground(Color.WHITE);
    JPasswordField passwordField = new JPasswordField(20);

    GridBagConstraints gbcPass = new GridBagConstraints();
    gbcPass.gridx = 0;
    gbcPass.gridy = 0;
    gbcPass.anchor = GridBagConstraints.WEST;
    gbcPass.insets = new Insets(5, 5, 5, 5);
    passBox.add(passLabel, gbcPass);

    gbcPass.gridx = 1;
    passBox.add(passwordField, gbcPass);

    // ---------------------------------------------------------
    // BUTTONS
    // ---------------------------------------------------------
    JButton loginButton = createStyledButton("Login");
    JButton backButton = createStyledButton("Back");

    Color buttonColor = Color.decode("#2b643b");
    JButton[] buttons = {loginButton, backButton};
    for (JButton btn : buttons) {
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
    }

    // ---------------------------------------------------------
    // GRIDBAG CONSTRAINTS FOR MAIN PANEL
    // ---------------------------------------------------------
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.CENTER;

    int horizontalOffset = 100; // move right
    int verticalOffset = 20;    // move down

    // Welcome message
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(20 + verticalOffset, horizontalOffset, 20, 100);
    adminPanel.add(messageBox, gbc);

    gbc.gridwidth = 2;

    // Username box
    gbc.gridy = 1;
    gbc.insets = new Insets(15, horizontalOffset, 15, 100);
    adminPanel.add(userBox, gbc);

    // Password box
    gbc.gridy = 2;
    gbc.insets = new Insets(15, horizontalOffset, 15, 100);
    adminPanel.add(passBox, gbc);

    // Buttons
    gbc.gridwidth = 1;

    gbc.gridy = 3;
    gbc.gridx = 0;
    gbc.insets = new Insets(20, horizontalOffset, 20, 10);
    adminPanel.add(loginButton, gbc);

    gbc.gridx = 1;
    gbc.insets = new Insets(20, 10, 20, horizontalOffset);
    adminPanel.add(backButton, gbc);

    // ---------------------------------------------------------
    // ACTIONS
    // ---------------------------------------------------------
    loginButton.addActionListener(e -> {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.equals("admin") && password.equals("runme")) {
            adminMenu();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid Admin Credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    backButton.addActionListener(e -> cardLayout.show(mainPanel, "User Role"));
}

    private void initializeProgramMenuPanel() {
        programMenuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bg = new ImageIcon(getClass().getResource("acc_bg.jpeg"));
                g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        programMenuPanel.setLayout(new GridBagLayout());

        JButton addIncomeButton = createStyledButton("Add Income");
        JButton addExpenseButton = createStyledButton("Add Expense");
        JButton viewHistoryButton = createStyledButton("View History");
        JButton viewSummaryButton = createStyledButton("View Summary");
        JButton getSuggestionsButton = createStyledButton("Get Suggestions");
        JButton exitButton = createStyledButton("Exit");

        addIncomeButton.addActionListener(e -> addIncome());
        addExpenseButton.addActionListener(e -> addExpense());
        viewHistoryButton.addActionListener(e -> viewHistory());
        viewSummaryButton.addActionListener(e -> viewSummary());
        getSuggestionsButton.addActionListener(e -> getSuggestions());
        exitButton.addActionListener(e -> cardLayout.show(mainPanel, "User Role"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        programMenuPanel.add(addIncomeButton, gbc);
        gbc.gridy = 1;
        programMenuPanel.add(addExpenseButton, gbc);
        gbc.gridy = 2;
        programMenuPanel.add(viewHistoryButton, gbc);
        gbc.gridy = 3;
        programMenuPanel.add(viewSummaryButton, gbc);
        gbc.gridy = 4;
        programMenuPanel.add(getSuggestionsButton, gbc);
        gbc.gridy = 5;
        programMenuPanel.add(exitButton, gbc);
    }

private void signIn() {
    JPanel signInPanel = new JPanel(new GridBagLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setOpaque(false); // allow parent background to show
        }
    };

    // Welcome message
    JLabel welcomeLabel = new JLabel("✨ Welcome Back! Sign in to continue ✨");
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
    welcomeLabel.setForeground(Color.CYAN);

    // Username
    JLabel userLabel = new JLabel("👤 Username:");
    userLabel.setFont(new Font("Arial", Font.BOLD, 24));
    userLabel.setForeground(new Color(168, 255, 96));
    JTextField usernameField = new JTextField(20);
    usernameField.setBackground(new Color(255, 255, 255, 220));
    usernameField.setForeground(Color.BLACK);
    usernameField.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));

    // Password
    JLabel passLabel = new JLabel("🔒 Password:");
    passLabel.setFont(new Font("Arial", Font.BOLD, 24));
    passLabel.setForeground(new Color(168, 255, 96));
    JPasswordField passwordField = new JPasswordField(20);
    passwordField.setBackground(new Color(255, 255, 255, 220));
    passwordField.setForeground(Color.BLACK);
    passwordField.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));

    // Buttons
    JButton submitButton = createStyledButton("Sign In ✅");
    JButton cancelButton = createStyledButton("Cancel ❌");
    Color buttonColor = Color.decode("#2b643b");
    for (JButton btn : new JButton[]{submitButton, cancelButton}) {
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
    }

    // Layout
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.CENTER;
    int hOffset = 50, vOffset = 20;

    gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
    gbc.insets = new Insets(10 + vOffset, hOffset, 20, 10);
    signInPanel.add(welcomeLabel, gbc);

    gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
    gbc.insets = new Insets(5, hOffset, 5, 5);
    signInPanel.add(userLabel, gbc);
    gbc.gridx = 1;
    gbc.insets = new Insets(5, 5, 5, hOffset);
    signInPanel.add(usernameField, gbc);

    gbc.gridy = 2; gbc.gridx = 0;
    gbc.insets = new Insets(5, hOffset, 5, 5);
    signInPanel.add(passLabel, gbc);
    gbc.gridx = 1;
    gbc.insets = new Insets(5, 5, 5, hOffset);
    signInPanel.add(passwordField, gbc);

    gbc.gridy = 3; gbc.gridx = 0;
    gbc.insets = new Insets(15, hOffset, 15, 5);
    signInPanel.add(submitButton, gbc);
    gbc.gridx = 1;
    gbc.insets = new Insets(15, 5, 15, hOffset);
    signInPanel.add(cancelButton, gbc);

    // Dialog
    JDialog dialog = new JDialog(frame, "Sign In", true);
    dialog.getContentPane().add(signInPanel);
    dialog.pack();
    dialog.setLocationRelativeTo(frame);

    submitButton.addActionListener(e -> {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (authenticateUser(username, password)) {
            currentUser = username;
            dialog.dispose();
            cardLayout.show(mainPanel, "Program Menu");
        } else {
            JOptionPane.showMessageDialog(dialog, "Incorrect username or password ❌", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    });

    cancelButton.addActionListener(e -> dialog.dispose());
    dialog.setVisible(true);
}

private void signUp() {
    JPanel signUpPanel = new JPanel(new GridBagLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setOpaque(false);
        }
    };

    // Welcome message
    JLabel welcomeLabel = new JLabel("✨ Join Us! Create your account below ✨");
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
    welcomeLabel.setForeground(Color.CYAN);

    // Username
    JLabel userLabel = new JLabel("👤 New Username:");
    userLabel.setFont(new Font("Arial", Font.BOLD, 24));
    userLabel.setForeground(new Color(168, 255, 96));
    JTextField usernameField = new JTextField(20);
    usernameField.setBackground(new Color(255, 255, 255, 220));
    usernameField.setForeground(Color.BLACK);
    usernameField.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));

    // Password
    JLabel passLabel = new JLabel("🔒 New Password:");
    passLabel.setFont(new Font("Arial", Font.BOLD, 24));
    passLabel.setForeground(new Color(168, 255, 96));
    JPasswordField passwordField = new JPasswordField(20);
    passwordField.setBackground(new Color(255, 255, 255, 220));
    passwordField.setForeground(Color.BLACK);
    passwordField.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));

    // Buttons
    JButton submitButton = createStyledButton("Sign Up ✅");
    JButton cancelButton = createStyledButton("Cancel ❌");
    Color buttonColor = Color.decode("#2b643b");
    for (JButton btn : new JButton[]{submitButton, cancelButton}) {
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
    }

    // Layout
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.CENTER;
    int hOffset = 50, vOffset = 20;

    gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
    gbc.insets = new Insets(10 + vOffset, hOffset, 20, 10);
    signUpPanel.add(welcomeLabel, gbc);

    gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
    gbc.insets = new Insets(5, hOffset, 5, 5);
    signUpPanel.add(userLabel, gbc);
    gbc.gridx = 1;
    gbc.insets = new Insets(5, 5, 5, hOffset);
    signUpPanel.add(usernameField, gbc);

    gbc.gridy = 2; gbc.gridx = 0;
    gbc.insets = new Insets(5, hOffset, 5, 5);
    signUpPanel.add(passLabel, gbc);
    gbc.gridx = 1;
    gbc.insets = new Insets(5, 5, 5, hOffset);
    signUpPanel.add(passwordField, gbc);

    gbc.gridy = 3; gbc.gridx = 0;
    gbc.insets = new Insets(15, hOffset, 15, 5);
    signUpPanel.add(submitButton, gbc);
    gbc.gridx = 1;
    gbc.insets = new Insets(15, 5, 15, hOffset);
    signUpPanel.add(cancelButton, gbc);

    // Dialog
    JDialog dialog = new JDialog(frame, "Sign Up", true);
    dialog.getContentPane().add(signUpPanel);
    dialog.pack();
    dialog.setLocationRelativeTo(frame);

    submitButton.addActionListener(e -> {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (registerUser(username, password)) {
            JOptionPane.showMessageDialog(dialog, "Sign-up successful! 🎉 You can now sign in.");
            dialog.dispose();
        } else {
            JOptionPane.showMessageDialog(dialog, "Username already exists ❌. Please try again.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    });

    cancelButton.addActionListener(e -> dialog.dispose());
    dialog.setVisible(true);
}


    private void addIncome() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Income Source
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Income Source:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> sourceCombo = new JComboBox<>(new String[] { "Salary", "Bonus", "Freelance", "Other" });
        sourceCombo.setEditable(true); // user can type or select
        panel.add(sourceCombo, gbc);

        // Income Amount
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Income Amount:"), gbc);

        gbc.gridx = 1;
        JPanel amountPanel = new JPanel(new BorderLayout());
        JLabel pesoLabel = new JLabel("₱ ");
        JTextField amountField = new JTextField();
        amountPanel.add(pesoLabel, BorderLayout.WEST);
        amountPanel.add(amountField, BorderLayout.CENTER);
        panel.add(amountPanel, gbc);

        // Show dialog
        int result = JOptionPane.showConfirmDialog(frame, panel, "Add Income",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String source = sourceCombo.getEditor().getItem().toString().trim();
            String amountStr = amountField.getText().trim();

            try {
                float amount = Float.parseFloat(amountStr);
                // Format to 2 decimal places
                amount = Float.parseFloat(String.format("%.2f", amount));

                writeUserData(currentUser, amount, source, "Income");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addExpense() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Expense Source
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Expense Source:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> sourceCombo = new JComboBox<>(new String[] { "Food", "Transportation", "Bills", "Other" });
        sourceCombo.setEditable(true); // user can type or select
        panel.add(sourceCombo, gbc);

        // Expense Amount
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Expense Amount:"), gbc);

        gbc.gridx = 1;
        JPanel amountPanel = new JPanel(new BorderLayout());
        JLabel pesoLabel = new JLabel("₱ ");
        JTextField amountField = new JTextField();
        amountPanel.add(pesoLabel, BorderLayout.WEST);
        amountPanel.add(amountField, BorderLayout.CENTER);
        panel.add(amountPanel, gbc);

        // Show dialog
        int result = JOptionPane.showConfirmDialog(frame, panel, "Add Expense",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String source = sourceCombo.getEditor().getItem().toString().trim();
            String amountStr = amountField.getText().trim();

            try {
                float amount = Float.parseFloat(amountStr);
                // Format to 2 decimal places
                amount = Float.parseFloat(String.format("%.2f", amount));

                writeUserData(currentUser, amount, source, "Expense");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void writeUserData(String username, float amount, String source, String type) {
        String filename = username + ".txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            String dateTime = LocalDateTime.now().format(dtf);
            bw.write(amount + "," + source + "," + type + "," + dateTime);
            bw.newLine();
            JOptionPane.showMessageDialog(frame, "Data recorded successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error writing user data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

private void adminMenu() {
    JPanel adminMenu = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon bg = new ImageIcon(getClass().getResource("admin_bg.jpeg"));
            g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    };
    adminMenu.setLayout(new GridBagLayout());

    // ---------------------------------------------------------
    // BUTTONS
    // ---------------------------------------------------------
    JButton viewUserDataButton = createStyledButton("View User Data");
    JButton editUserDataButton = createStyledButton("Edit User Data");
    JButton removeUserDataButton = createStyledButton("Remove User Data");
    JButton adminExitButton = createStyledButton("Exit");

    // Set button colors
    Color buttonColor = Color.decode("#2b643b");
    JButton[] buttons = {viewUserDataButton, editUserDataButton, removeUserDataButton, adminExitButton};
    for (JButton btn : buttons) {
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
    }

    // Button actions
    viewUserDataButton.addActionListener(e -> viewAllUsers());
    editUserDataButton.addActionListener(e -> editUserFile());
    removeUserDataButton.addActionListener(e -> removeUserFile());
    adminExitButton.addActionListener(e -> cardLayout.show(mainPanel, "User Role"));

    // ---------------------------------------------------------
    // GRIDBAG CONSTRAINTS
    // ---------------------------------------------------------
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.CENTER;

    int horizontalOffset = 100; // move right
    int verticalOffset = 20;    // move down
    gbc.insets = new Insets(15 + verticalOffset, horizontalOffset, 15, 0);

    // Add buttons to panel
    gbc.gridy = 0;
    adminMenu.add(viewUserDataButton, gbc);
    gbc.gridy = 1;
    adminMenu.add(editUserDataButton, gbc);
    gbc.gridy = 2;
    adminMenu.add(removeUserDataButton, gbc);
    gbc.gridy = 3;
    adminMenu.add(adminExitButton, gbc);

    // ---------------------------------------------------------
    // Add to main panel and show
    // ---------------------------------------------------------
    mainPanel.add(adminMenu, "Admin Menu Actions");
    cardLayout.show(mainPanel, "Admin Menu Actions");
}

    private void viewAllUsers() {
        File usersFile = new File("users.txt");

        if (!usersFile.exists()) {
            JOptionPane.showMessageDialog(frame, "No users found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Step 1: Read users.txt and prepare data for JTable
        String[] columnNames = { "Username", "Password" };
        Object[][] data = null;

        try (BufferedReader br = new BufferedReader(new FileReader(usersFile))) {
            java.util.List<Object[]> rows = new java.util.ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    rows.add(new Object[] { parts[0], parts[1] });
                }
            }
            data = rows.toArray(new Object[0][]);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading users file.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        // Step 2: Create panel with JTable and username input
        JTable table = new JTable(data, columnNames);
        table.setRowHeight(32); // bigger rows
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 22)); // big header
        table.setFont(new Font("Arial", Font.PLAIN, 20)); // big table text
        table.setEnabled(false); // read-only
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 500));

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 10));
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Enter a username to view data: "));
        JTextField usernameField = new JTextField(15);
        inputPanel.add(usernameField);

        panel.add(inputPanel, BorderLayout.SOUTH);

        // Step 3: Show dialog with OK/Cancel buttons
        int result = JOptionPane.showConfirmDialog(frame, panel, "All Registered Users",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String usernameToView = usernameField.getText().trim();
            if (usernameToView.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No username entered.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Call the helper method to show individual user transactions
            viewIndividualUserData(usernameToView);
        }
    }

    private void viewIndividualUserData(String username) {
        File userFile = new File(username + ".txt");
        if (!userFile.exists()) {
            JOptionPane.showMessageDialog(frame, "User data not found for: " + username,
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lists for income and expense
        List<Object[]> incomeRows = new ArrayList<>();
        List<Object[]> expenseRows = new ArrayList<>();

        // Formatters
        DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a"); // 12-hour with AM/PM

        // Read user transaction file
        List<Object[]> allRows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // amount,source,type,date
                if (parts.length == 4) {
                    Object[] row = new Object[] { parts[0], parts[1], parts[2], parts[3] };
                    allRows.add(row);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading user data.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Sort allRows by datetime (latest first)
        Collections.sort(allRows, new Comparator<Object[]>() {
            @Override
            public int compare(Object[] row1, Object[] row2) {
                try {
                    LocalDateTime dt1 = LocalDateTime.parse(row1[3].toString(), fileFormatter);
                    LocalDateTime dt2 = LocalDateTime.parse(row2[3].toString(), fileFormatter);
                    return dt2.compareTo(dt1); // latest first
                } catch (DateTimeParseException e) {
                    return 0;
                }
            }
        });

        // Convert datetime to 12-hour display format
        for (Object[] row : allRows) {
            try {
                LocalDateTime dt = LocalDateTime.parse(row[3].toString(), fileFormatter);
                row[3] = displayFormatter.format(dt);
            } catch (DateTimeParseException e) {
                // leave as is if parsing fails
            }
            // Separate into income and expense lists
            if (row[2].toString().equalsIgnoreCase("Income")) {
                incomeRows.add(new Object[] { row[0], row[1], row[3] });
            } else if (row[2].toString().equalsIgnoreCase("Expense")) {
                expenseRows.add(new Object[] { row[0], row[1], row[3] });
            }
        }

        // Add placeholder if empty
        if (incomeRows.isEmpty())
            incomeRows.add(new Object[] { "-", "No Income", "-" });
        if (expenseRows.isEmpty())
            expenseRows.add(new Object[] { "-", "No Expense", "-" });

        // Columns
        String[] columns = { "Amount", "Source", "Date" };

        // Tables
        JTable incomeTable = new JTable(incomeRows.toArray(new Object[0][]), columns);
        JTable expenseTable = new JTable(expenseRows.toArray(new Object[0][]), columns);

        // Center text
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < incomeTable.getColumnCount(); i++) {
            incomeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            if (i == 0)
                incomeTable.getColumnModel().getColumn(i).setPreferredWidth(100);
            else if (i == 1)
                incomeTable.getColumnModel().getColumn(i).setPreferredWidth(150);
            else
                incomeTable.getColumnModel().getColumn(i).setPreferredWidth(200);
        }
        for (int i = 0; i < expenseTable.getColumnCount(); i++) {
            expenseTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            if (i == 0)
                expenseTable.getColumnModel().getColumn(i).setPreferredWidth(100);
            else if (i == 1)
                expenseTable.getColumnModel().getColumn(i).setPreferredWidth(150);
            else
                expenseTable.getColumnModel().getColumn(i).setPreferredWidth(200);
        }

        // Row height
        incomeTable.setRowHeight(30);
        expenseTable.setRowHeight(30);

        // Scroll panes with bold borders
        JScrollPane incomeScroll = new JScrollPane(incomeTable);
        incomeScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Income",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 25)));
        incomeScroll.setPreferredSize(new Dimension(600, 500));

        JScrollPane expenseScroll = new JScrollPane(expenseTable);
        expenseScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Expense",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 25)));
        expenseScroll.setPreferredSize(new Dimension(600, 500));

        // Panel to hold tables side by side
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.add(incomeScroll);
        panel.add(expenseScroll);

        // Show dialog
        JOptionPane.showMessageDialog(frame, panel,
                "Transaction Data of " + username, JOptionPane.PLAIN_MESSAGE);
    }

    private void editUserFile() {
        File usersFile = new File("users.txt");

        if (!usersFile.exists()) {
            JOptionPane.showMessageDialog(frame, "No users found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- Step 1: Show table of all users with input ---
        String[] userColumns = { "Username", "Password" };
        Object[][] userData = null;

        try (BufferedReader br = new BufferedReader(new FileReader(usersFile))) {
            java.util.List<Object[]> rows = new java.util.ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2)
                    rows.add(new Object[] { parts[0], parts[1] });
            }
            userData = rows.toArray(new Object[0][]);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading users file.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        JTable usersTable = new JTable(userData, userColumns);
        usersTable.setRowHeight(32);
        usersTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 22));
        usersTable.setFont(new Font("Arial", Font.PLAIN, 20));
        usersTable.setEnabled(false);
        JScrollPane usersScroll = new JScrollPane(usersTable);
        usersScroll.setPreferredSize(new Dimension(700, 500));

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.add(usersScroll, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Enter a username to edit data: "));
        JTextField usernameField = new JTextField(15);
        inputPanel.add(usernameField);

        panel.add(inputPanel, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Edit User Data",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION)
            return;

        String usernameToEdit = usernameField.getText().trim();
        if (usernameToEdit.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No username entered.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        File userFile = new File(usernameToEdit + ".txt");
        if (!userFile.exists()) {
            JOptionPane.showMessageDialog(frame, "User data not found for: " + usernameToEdit,
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- Step 2: Load user's transaction data ---
        java.util.List<Object[]> incomeRows = new java.util.ArrayList<>();
        java.util.List<Object[]> expenseRows = new java.util.ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // amount,source,type,date
                if (parts.length == 4) {
                    Object[] row = new Object[] { parts[0], parts[1], parts[3] };
                    if (parts[2].equalsIgnoreCase("Income"))
                        incomeRows.add(row);
                    else if (parts[2].equalsIgnoreCase("Expense"))
                        expenseRows.add(row);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading user transactions.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

        String[] columns = { "Amount", "Source", "Date" };

        // --- Step 3: Use DefaultTableModel for editable & deletable tables ---
        DefaultTableModel incomeModel = new DefaultTableModel(incomeRows.toArray(new Object[0][]), columns);
        DefaultTableModel expenseModel = new DefaultTableModel(expenseRows.toArray(new Object[0][]), columns);

        JTable incomeTable = new JTable(incomeModel);
        JTable expenseTable = new JTable(expenseModel);

        incomeTable.setRowHeight(30);
        expenseTable.setRowHeight(30);

        // Column widths & center alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < columns.length; i++) {
            incomeTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            expenseTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

            int width = (i == 0) ? 100 : (i == 1) ? 150 : 200;
            incomeTable.getColumnModel().getColumn(i).setPreferredWidth(width);
            expenseTable.getColumnModel().getColumn(i).setPreferredWidth(width);
        }

        JScrollPane incomeScroll = new JScrollPane(incomeTable);
        incomeScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Income",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 25)));
        incomeScroll.setPreferredSize(new Dimension(600, 500));

        JScrollPane expenseScroll = new JScrollPane(expenseTable);
        expenseScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Expense",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 25)));
        expenseScroll.setPreferredSize(new Dimension(600, 500));

        // --- Step 4: Tables panel ---
        JPanel tablesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        tablesPanel.add(incomeScroll);
        tablesPanel.add(expenseScroll);

        // --- Step 5: Edit panel ---
        JPanel editPanel = new JPanel(new FlowLayout());
        editPanel.add(new JLabel("Amount:"));
        JTextField amountField = new JTextField(6);
        editPanel.add(amountField);

        editPanel.add(new JLabel("Source:"));
        JTextField sourceField = new JTextField(10);
        editPanel.add(sourceField);

        JButton updateButton = new JButton("Update Selected Row");
        JButton deleteButton = new JButton("Delete Selected Row");
        editPanel.add(updateButton);
        editPanel.add(deleteButton);

        JPanel transPanel = new JPanel(new BorderLayout(0, 10));
        transPanel.add(tablesPanel, BorderLayout.CENTER);
        transPanel.add(editPanel, BorderLayout.SOUTH);

        // --- Step 6: Row selection listeners ---
        incomeTable.getSelectionModel().addListSelectionListener(e -> {
            int row = incomeTable.getSelectedRow();
            if (row >= 0) {
                amountField.setText(incomeTable.getValueAt(row, 0).toString());
                sourceField.setText(incomeTable.getValueAt(row, 1).toString());
                expenseTable.clearSelection();
            }
        });

        expenseTable.getSelectionModel().addListSelectionListener(e -> {
            int row = expenseTable.getSelectedRow();
            if (row >= 0) {
                amountField.setText(expenseTable.getValueAt(row, 0).toString());
                sourceField.setText(expenseTable.getValueAt(row, 1).toString());
                incomeTable.clearSelection();
            }
        });

        // --- Step 7: Update button ---
        updateButton.addActionListener(e -> {
            int row = -1;
            JTable tableToUpdate = null;

            if (incomeTable.getSelectedRow() >= 0) {
                row = incomeTable.getSelectedRow();
                tableToUpdate = incomeTable;
            } else if (expenseTable.getSelectedRow() >= 0) {
                row = expenseTable.getSelectedRow();
                tableToUpdate = expenseTable;
            }

            if (row < 0) {
                JOptionPane.showMessageDialog(frame, "Select a row to update.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            tableToUpdate.setValueAt(amountField.getText().trim(), row, 0);
            tableToUpdate.setValueAt(sourceField.getText().trim(), row, 1);

            saveTransactionTables(userFile, incomeTable, expenseTable);
        });

        // --- Step 8: Delete button ---
        deleteButton.addActionListener(e -> {
            int row = -1;
            JTable tableToDelete = null;

            if (incomeTable.getSelectedRow() >= 0) {
                row = incomeTable.getSelectedRow();
                tableToDelete = incomeTable;
            } else if (expenseTable.getSelectedRow() >= 0) {
                row = expenseTable.getSelectedRow();
                tableToDelete = expenseTable;
            }

            if (row < 0) {
                JOptionPane.showMessageDialog(frame, "Select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to delete the selected row?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                ((DefaultTableModel) tableToDelete.getModel()).removeRow(row);
                saveTransactionTables(userFile, incomeTable, expenseTable);
            }
        });

        // --- Step 9: Show dialog ---
        JOptionPane.showMessageDialog(frame, transPanel,
                "Editing Transactions of " + usernameToEdit, JOptionPane.PLAIN_MESSAGE);
    }

    private void saveTransactionTables(File userFile, JTable incomeTable, JTable expenseTable) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFile))) {
            for (int i = 0; i < incomeTable.getRowCount(); i++) {
                bw.write(incomeTable.getValueAt(i, 0) + "," +
                        incomeTable.getValueAt(i, 1) + ",Income," +
                        incomeTable.getValueAt(i, 2));
                bw.newLine();
            }
            for (int i = 0; i < expenseTable.getRowCount(); i++) {
                bw.write(expenseTable.getValueAt(i, 0) + "," +
                        expenseTable.getValueAt(i, 1) + ",Expense," +
                        expenseTable.getValueAt(i, 2));
                bw.newLine();
            }
            JOptionPane.showMessageDialog(frame, "Transactions saved successfully!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error saving transactions.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void removeUserFile() {
        File usersFile = new File("users.txt");

        if (!usersFile.exists()) {
            JOptionPane.showMessageDialog(frame, "No users found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- Step 1: Read users into table ---
        String[] columns = { "Username", "Password" };
        Object[][] userData = null;

        try (BufferedReader br = new BufferedReader(new FileReader(usersFile))) {
            java.util.List<Object[]> rows = new java.util.ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2)
                    rows.add(new Object[] { parts[0], parts[1] });
            }
            userData = rows.toArray(new Object[0][]);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading users file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel model = new DefaultTableModel(userData, columns);
        JTable table = new JTable(model);
        table.setRowHeight(32); // bigger rows
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 22)); // big header
        table.setFont(new Font("Arial", Font.PLAIN, 20)); // big table text
        table.setEnabled(false); // read-only
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(700, 500));

        // --- Step 2: Prompt field under the table ---
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scroll, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Enter username to delete:"));
        JTextField usernameField = new JTextField(15);
        inputPanel.add(usernameField);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(
                frame,
                mainPanel,
                "Remove User",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION)
            return;

        String userToRemove = usernameField.getText().trim();

        if (userToRemove.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No username entered.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- Step 3: Check if user exists ---
        boolean exists = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equals(userToRemove)) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            JOptionPane.showMessageDialog(frame, "User not found: " + userToRemove,
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- Step 4: Ask for delete confirmation ---
        int confirm = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to delete user \"" + userToRemove + "\"?\nThis will delete their data file too.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION)
            return;

        // --- Step 5: Remove from users.txt ---
        try (BufferedReader br = new BufferedReader(new FileReader(usersFile))) {
            java.util.List<String> lines = new java.util.ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(userToRemove + " ")) {
                    lines.add(line);
                }
            }

            try (PrintWriter pw = new PrintWriter(usersFile)) {
                for (String l : lines)
                    pw.println(l);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error updating users file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- Step 6: Delete user’s own data file ---
        File userFile = new File(userToRemove + ".txt");
        if (userFile.exists()) {
            userFile.delete();
        }

        JOptionPane.showMessageDialog(frame, "User \"" + userToRemove + "\" successfully deleted.");
    }

    private boolean authenticateUser(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(" ");
                if (credentials[0].equals(username) && credentials[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean registerUser(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.split(" ")[0].equals(username)) {
                    return false;
                }
            }
        } catch (IOException e) {
            // File not found; will create it
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt", true))) {
            bw.write(username + " " + password);
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void viewHistory() {
        String filename = currentUser + ".txt";
        java.util.List<Object[]> allRows = new java.util.ArrayList<>();

        // Read user transaction file
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // format: amount,source,type,date
                if (parts.length == 4) {
                    allRows.add(new Object[] { parts[0], parts[1], parts[2], parts[3] }); // Amount, Source, Type, Date
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "No transaction history found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (allRows.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No transaction history found.", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Sort by date and time (latest first)
        DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"); // your file format
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a"); // 12-hour for display

        Collections.sort(allRows, new Comparator<Object[]>() {
            @Override
            public int compare(Object[] row1, Object[] row2) {
                try {
                    LocalDateTime dt1 = LocalDateTime.parse(row1[3].toString(), fileFormatter);
                    LocalDateTime dt2 = LocalDateTime.parse(row2[3].toString(), fileFormatter);
                    return dt2.compareTo(dt1); // latest first
                } catch (DateTimeParseException e) {
                    return 0; // keep original order if parse fails
                }
            }
        });

        // Optional: convert the date column to 12-hour format for display
        for (Object[] row : allRows) {
            try {
                LocalDateTime dt = LocalDateTime.parse(row[3].toString(), fileFormatter);
                row[3] = displayFormatter.format(dt);
            } catch (DateTimeParseException e) {
                // keep original if parse fails
            }
        }

        // Columns
        String[] columns = { "Amount", "Source", "Type", "Date" };
        Object[][] data = allRows.toArray(new Object[0][]);

        // Table
        JTable table = new JTable(data, columns);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 25));
        table.setFont(new Font("Arial", Font.PLAIN, 20));

        // Center text
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            if (i == 0)
                table.getColumnModel().getColumn(i).setPreferredWidth(100);
            else if (i == 1)
                table.getColumnModel().getColumn(i).setPreferredWidth(150);
            else if (i == 2)
                table.getColumnModel().getColumn(i).setPreferredWidth(100);
            else
                table.getColumnModel().getColumn(i).setPreferredWidth(200);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 500));
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Transaction History",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 25)));

        // Show dialog
        JOptionPane.showMessageDialog(frame, scrollPane, "Transaction History of " + currentUser,
                JOptionPane.PLAIN_MESSAGE);
    }

    private void viewSummary() {
        String filename = currentUser + ".txt";
        File file = new File(filename);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(frame, "No data found for user.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // FRAME
        JFrame summaryFrame = new JFrame("Summary - " + currentUser);
        summaryFrame.setSize(1200, 700);
        summaryFrame.setLayout(new BorderLayout());

        // TABLE MODELS
        DefaultTableModel incomeModel = new DefaultTableModel(new String[] { "Amount", "Category", "Date" }, 0);
        DefaultTableModel expenseModel = new DefaultTableModel(new String[] { "Amount", "Category", "Date" }, 0);

        float totalIncome = 0, totalExpense = 0;
        TreeMap<String, Float> incomeMonthly = new TreeMap<>();
        TreeMap<String, Float> expenseMonthly = new TreeMap<>();
        HashMap<String, Float> incomeMap = new HashMap<>();
        HashMap<String, Float> expenseMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length < 4)
                    continue;

                try {
                    float amount = Float.parseFloat(p[0]);
                    String category = p[1];
                    String type = p[2];
                    String date = p[3];

                    String monthKey = date.substring(3); // MM-YYYY

                    if (type.equalsIgnoreCase("Income")) {
                        totalIncome += amount;
                        incomeModel.addRow(new Object[] { amount, category, date });
                        incomeMap.put(category, incomeMap.getOrDefault(category, 0f) + amount);
                        incomeMonthly.put(monthKey, incomeMonthly.getOrDefault(monthKey, 0f) + amount);
                    } else if (type.equalsIgnoreCase("Expense")) {
                        totalExpense += amount;
                        expenseModel.addRow(new Object[] { amount, category, date });
                        expenseMap.put(category, expenseMap.getOrDefault(category, 0f) + amount);
                        expenseMonthly.put(monthKey, expenseMonthly.getOrDefault(monthKey, 0f) + amount);
                    }
                } catch (NumberFormatException ex) {
                    System.err.println("Skipping invalid line: " + line);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error loading summary.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ADD TOTAL ROWS
        incomeModel.addRow(new Object[] { "", "TOTAL:", totalIncome });
        expenseModel.addRow(new Object[] { "", "TOTAL:", totalExpense });

        JTable incomeTable = new JTable(incomeModel);
        JTable expenseTable = new JTable(expenseModel);

        // Format tables: right-align amounts and bold totals
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        incomeTable.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
        expenseTable.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);

        // Highlight TOTAL rows
        incomeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row == table.getRowCount() - 1)
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                return c;
            }
        });

        expenseTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row == table.getRowCount() - 1)
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                return c;
            }
        });

        // PANELS
        JPanel topPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        topPanel.add(new JScrollPane(incomeTable));
        topPanel.add(new JScrollPane(expenseTable));

        // Suggestions panel (resizable)
        JInternalFrame suggestionFrame = new JInternalFrame("Suggestions", true, true, true, true);
        suggestionFrame.setSize(300, 300);
        suggestionFrame.setVisible(true);
        JTextArea suggestionArea = new JTextArea();
        suggestionArea.setEditable(false);
        suggestionFrame.add(new JScrollPane(suggestionArea));
        topPanel.add(suggestionFrame);

        // Bottom panel for charts
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        bottomPanel.add(ChartHelper.createPieChartPanel(incomeMap, expenseMap));
        bottomPanel.add(ChartHelper.createLineChartPanel(incomeMonthly, expenseMonthly));

        // SPLIT PANE for adjustable layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        splitPane.setDividerLocation(350);
        summaryFrame.add(splitPane, BorderLayout.CENTER);

        summaryFrame.setVisible(true);
    }

    public class ChartHelper {

        @SuppressWarnings("unchecked")
        public static JPanel createPieChartPanel(HashMap<String, Float> incomeMap, HashMap<String, Float> expenseMap) {
            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

            for (Map.Entry<String, Float> entry : incomeMap.entrySet()) {
                dataset.setValue("Income - " + entry.getKey(), entry.getValue());
            }
            for (Map.Entry<String, Float> entry : expenseMap.entrySet()) {
                dataset.setValue("Expense - " + entry.getKey(), entry.getValue());
            }

            JFreeChart chart = org.jfree.chart.ChartFactory.createPieChart(
                    "Income vs Expenses",
                    dataset,
                    true,
                    true,
                    false);

            PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
            plot.setSectionPaint("Income", new Color(79, 129, 189));
            plot.setSectionPaint("Expense", new Color(192, 80, 77));

            return new ChartPanel(chart);
        }

        public static JPanel createLineChartPanel(TreeMap<String, Float> incomeMonthly,
                TreeMap<String, Float> expenseMonthly) {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for (Map.Entry<String, Float> entry : incomeMonthly.entrySet()) {
                dataset.addValue(entry.getValue(), "Income", entry.getKey());
            }
            for (Map.Entry<String, Float> entry : expenseMonthly.entrySet()) {
                dataset.addValue(entry.getValue(), "Expense", entry.getKey());
            }

            JFreeChart chart = org.jfree.chart.ChartFactory.createLineChart(
                    "Monthly Income vs Expenses",
                    "Month",
                    "Amount",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false);

            return new ChartPanel(chart);
        }
    }

    private void getSuggestions() {

        List<Object[]> incomeRows = getAllIncomeRows();
        List<Object[]> expenseRows = getAllExpenseRows();
        List<Object[]> savingsRows = getAllSavingsRows();

        double totalIncome = 0, totalExpense = 0, totalSavings = 0;

        for (Object[] r : incomeRows)
            totalIncome += Double.parseDouble(r[0].toString());
        for (Object[] r : expenseRows)
            totalExpense += Double.parseDouble(r[0].toString());
        for (Object[] r : savingsRows)
            totalSavings += Double.parseDouble(r[0].toString());

        // --- TOP EXPENSES ---
        expenseRows.sort((a, b) -> Double.compare(
                Double.parseDouble(b[0].toString()),
                Double.parseDouble(a[0].toString())));

        StringBuilder top3 = new StringBuilder();
        for (int i = 0; i < Math.min(3, expenseRows.size()); i++) {
            Object[] e = expenseRows.get(i);
            top3.append("• ₱").append(e[0]).append(" - ").append(e[1]).append("\n");
        }

        String[] catchPhrases = {
                "🔥 Careful! Your spending is rising fast!",
                "😭 Your wallet is begging for mercy!",
                "⚠️ Heavy expenses detected!",
                "💸 Your finances are overheating!",
                "🐗 Your spendings are going wild!"
        };
        String spendingPhrase = catchPhrases[(int) (Math.random() * catchPhrases.length)];

        // --- SAVING NUDGE ---
        String savingNudge;
        if (totalSavings < totalIncome * 0.10)
            savingNudge = "💰 You saved less than 10%. Try saving more!";
        else if (totalSavings < totalIncome * 0.30)
            savingNudge = "👍 Good start! Try increasing your savings.";
        else if (totalSavings < totalIncome * 0.50)
            savingNudge = "💪 Great job! You're saving well!";
        else
            savingNudge = "🧠 Amazing! You're a savings master!";

        // --- FINANCIAL HEALTH ---
        double score = (totalIncome + totalSavings) - totalExpense;
        String status;

        if (score < -5000)
            status = "EXTREMELY BAD";
        else if (score < -2000)
            status = "VERY BAD";
        else if (score < 0)
            status = "BAD";
        else if (score < 2000)
            status = "BARELY SURVIVING";
        else if (score < 6000)
            status = "KINDA THRIVING";
        else if (score < 12000)
            status = "GOOD";
        else if (score < 20000)
            status = "VERY GOOD";
        else
            status = "GODLY FINANCIAL MANAGING";

        // --- FEEDBACK ---
        Map<String, String[]> fb = new HashMap<>();

        fb.put("EXTREMELY BAD", new String[] {
                "⚠️ Your finances are collapsing!",
                "💀 You're in deep danger. Fix ASAP.",
                "🔥 Emergency! Cut spending NOW."
        });

        fb.put("VERY BAD", new String[] {
                "🚨 Alarming spending level.",
                "💸 Expenses too high!",
                "⚡ Tighten your wallet immediately."
        });

        fb.put("BAD", new String[] {
                "😬 You're close to negative.",
                "🪫 Weak balance. Be careful.",
                "📉 Reduce weekly spending."
        });

        fb.put("BARELY SURVIVING", new String[] {
                "🙂 You're surviving — but barely.",
                "🟡 Try to build more savings.",
                "⏳ Your balance is fragile."
        });

        fb.put("KINDA THRIVING", new String[] {
                "🟢 Nice! You're doing alright.",
                "✨ Keep this pace up!",
                "💼 You're improving well."
        });

        fb.put("GOOD", new String[] {
                "💚 Stable finances!",
                "📊 Great balance!",
                "😎 Strong money management."
        });

        fb.put("VERY GOOD", new String[] {
                "💎 Amazing discipline!",
                "🔥 Managing money like a pro!",
                "🌟 Excellent financial habits!"
        });

        fb.put("GODLY FINANCIAL MANAGING", new String[] {
                "👑 GOD-TIER CONTROL!",
                "🌌 Legendary budgeting.",
                "💰 Unstoppable mastery!"
        });

        String feedback = fb.get(status)[(int) (Math.random() * fb.get(status).length)];

        // ----------------------------------------------------
        // SHOW ALL 4 PANELS (custom positions)
        // ----------------------------------------------------
        showStyledPanel("🔥 Spending Alert", spendingPhrase + "\n\n" + top3, PANEL_SPENDING_X, PANEL_SPENDING_Y);
        showStyledPanel("💰 Saving Nudge", savingNudge, PANEL_SAVING_X, PANEL_SAVING_Y);
        showStyledPanel("📊 Financial Health Status", "Your status: " + status, PANEL_HEALTH_X, PANEL_HEALTH_Y);
        showStyledPanel("💬 Feedback", feedback, PANEL_FEEDBACK_X, PANEL_FEEDBACK_Y);

    }

    private void showStyledPanel(String title, String message, int x, int y) {

        JDialog dialog = new JDialog(frame, title, false);
        dialog.setSize(400, 260);
        dialog.setLocation(x, y);
        dialog.setUndecorated(false);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JTextArea area = new JTextArea(message);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(new Color(245, 245, 245));

        panel.setLayout(new BorderLayout());
        panel.add(area, BorderLayout.CENTER);

        dialog.add(panel);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    private List<Object[]> getAllIncomeRows() {
        List<Object[]> incomeList = new ArrayList<>();
        String filename = currentUser + ".txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");

                if (p.length == 4 && p[2].equalsIgnoreCase("Income")) {
                    incomeList.add(new Object[] { p[0], p[1], p[2], p[3] });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return incomeList;
    }

    private List<Object[]> getAllExpenseRows() {
        List<Object[]> expenseList = new ArrayList<>();
        String filename = currentUser + ".txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");

                if (p.length == 4 && p[2].equalsIgnoreCase("Expense")) {
                    expenseList.add(new Object[] { p[0], p[1], p[2], p[3] });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return expenseList;
    }

    private List<Object[]> getAllSavingsRows() {
        // You said you do NOT store savings in file → return empty list
        return new ArrayList<>();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isArmed())
                    g2.setColor(getBackground().darker());
                else
                    g2.setColor(getBackground());

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(300, 60); // fixed size for all buttons
            }
        };

        button.setFont(new Font("Arial", Font.BOLD, 25));
        button.setBackground(new Color(34, 139, 34));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BudgetingSystem::new);
    }
}