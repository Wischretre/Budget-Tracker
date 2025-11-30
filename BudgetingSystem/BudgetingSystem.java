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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.FontMetrics;


public class BudgetingSystem {
    private JFrame frame;
    private String currentUser = "";
    private JPanel mainPanel, adminPanel, studentPanel, userRolePanel, programMenuPanel;
    private CardLayout cardLayout;
    private JTable incomeTable;
    private JTable expenseTable;
    private DefaultTableModel incomeModel;
    private DefaultTableModel expenseModel;



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
    // ROUNDED BUTTONS
    // ---------------------------------------------------------
    RoundedButton studentButton = new RoundedButton("Student");
    RoundedButton adminButton = new RoundedButton("Admin");
    RoundedButton exitButton = new RoundedButton("Exit Program");

    Color buttonColor = Color.decode("#2b643b");

    RoundedButton[] buttons = {studentButton, adminButton, exitButton};
    for (RoundedButton btn : buttons) {
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setCornerRadius(25);   // smooth corners
        btn.setOpaque(false);      // required for rounded look
    }

    studentButton.addActionListener(e -> cardLayout.show(mainPanel, "Student Menu"));
    adminButton.addActionListener(e -> cardLayout.show(mainPanel, "Admin Menu"));
    exitButton.addActionListener(e -> System.exit(0));

    // ---------------------------------------------------------
    // GRIDBAG CONSTRAINTS
    // ---------------------------------------------------------
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.insets = new Insets(20, 0, 20, 0);
    gbc.anchor = GridBagConstraints.CENTER;

    // ---------------------------------------------------------
    // SLIGHTLY MOVE RIGHT OR LEFT & UP/DOWN
    // ---------------------------------------------------------
    int horizontalOffset = 600; // positive → right, negative → left
    int verticalOffset = 90;    // positive → down, negative → up

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
    messageBox.setBackground(new Color(0, 0, 0, 150)); 
    messageBox.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

    JLabel label = new JLabel("<html><center>Welcome to the Budgeting Program</center></html>");
    label.setFont(new Font("Arial", Font.BOLD, 25));
    label.setForeground(Color.WHITE);

    messageBox.add(label);

    // ---------------------------------------------------------
    // ROUNDED BUTTONS
    // ---------------------------------------------------------
    RoundedButton signInButton = new RoundedButton("Sign In");
    RoundedButton signUpButton = new RoundedButton("Sign Up");
    RoundedButton backButton = new RoundedButton("Exit");

    // Button color
    Color buttonColor = Color.decode("#2b643b");
    RoundedButton[] buttons = {signInButton, signUpButton, backButton};
    for (RoundedButton btn : buttons) {
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);

        // -----------------------------
        // ⭐ CHANGE BUTTON SIZE HERE ⭐
        // -----------------------------
        btn.setPreferredSize(new Dimension(350, 60));  
        // just change 350 = width, 60 = height

        // -----------------------------
        // ⭐ CHANGE ROUNDNESS HERE ⭐
        // -----------------------------
        btn.setCornerRadius(25); 
        // higher = more round
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

    // -----------------------------
    // ⭐ POSITION ADJUSTMENT ⭐
    // -----------------------------
    int horizontalOffset = 600; // positive → move RIGHT, negative → move LEFT
    int verticalOffset = 90;    // positive → move DOWN, negative → move UP

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
    messageBox.setBackground(new Color(0, 0, 0, 150));
    messageBox.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

    JLabel titleLabel = new JLabel("<html><center>Welcome to the Admin System</center></html>");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
    titleLabel.setForeground(Color.WHITE);
    messageBox.add(titleLabel);

    // ---------------------------------------------------------
    // USERNAME & PASSWORD BOXES
    // ---------------------------------------------------------
    JPanel userBox = new JPanel(new GridBagLayout());
    userBox.setBackground(new Color(0, 0, 0, 120));
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
    // ROUNDED BUTTONS
    // ---------------------------------------------------------
    RoundedButton loginButton = new RoundedButton("Login");
    RoundedButton backButton = new RoundedButton("Back");

    Color buttonColor = Color.decode("#2b643b");
    RoundedButton[] buttons = {loginButton, backButton};

    for (RoundedButton btn : buttons) {
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);

        // ⭐ EDIT BUTTON SIZE HERE ⭐
        btn.setPreferredSize(new Dimension(250, 55));

        // ⭐ EDIT ROUNDNESS HERE ⭐
        btn.setCornerRadius(25);
    }

    // ---------------------------------------------------------
    // GRIDBAG FOR MAIN PANEL
    // ---------------------------------------------------------
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.CENTER;

    int horizontalOffset = 100; 
    int verticalOffset = 20;

    // Welcome message
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.insets = new Insets(20 + verticalOffset, horizontalOffset, 20, 100);
    adminPanel.add(messageBox, gbc);

    // Username box
    gbc.gridy = 1;
    gbc.insets = new Insets(15, horizontalOffset, 15, 100);
    adminPanel.add(userBox, gbc);

    // Password box
    gbc.gridy = 2;
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

    // ---------------------------------------------------------
    // MESSAGE BOX PANEL
    // ---------------------------------------------------------
    JPanel messageBox = new JPanel(new GridBagLayout());
    messageBox.setBackground(new Color(0, 0, 0, 150)); // semi-transparent
    messageBox.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

    JLabel label = new JLabel("<html><center>Welcome!<br>Select an option</center></html>");
    label.setFont(new Font("Arial", Font.BOLD, 25));
    label.setForeground(Color.WHITE);
    messageBox.add(label);

    // ---------------------------------------------------------
    // ROUNDED BUTTONS MATCHING USERROLE PANEL
    // ---------------------------------------------------------
    Color buttonColor = Color.decode("#2b643b"); // same as UserRole buttons
    RoundedButton addIncomeButton = new RoundedButton("Add Income");
    RoundedButton addExpenseButton = new RoundedButton("Add Expense");
    RoundedButton viewHistoryButton = new RoundedButton("View History");
    RoundedButton viewSummaryButton = new RoundedButton("View Summary");
    RoundedButton getSuggestionsButton = new RoundedButton("Get Suggestions");
    RoundedButton exitButton = new RoundedButton("Exit");

    RoundedButton[] buttons = {addIncomeButton, addExpenseButton, viewHistoryButton,
                               viewSummaryButton, getSuggestionsButton, exitButton};

    for (RoundedButton btn : buttons) {
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setCornerRadius(25);
        btn.setOpaque(false);
    }

    // ---------------------------------------------------------
    // Actions
    // ---------------------------------------------------------
    addIncomeButton.addActionListener(e -> addIncome());
    addExpenseButton.addActionListener(e -> addExpense());
    viewHistoryButton.addActionListener(e -> viewHistory());
    viewSummaryButton.addActionListener(e -> viewSummary());
    getSuggestionsButton.addActionListener(e -> getSuggestions());
    exitButton.addActionListener(e -> cardLayout.show(mainPanel, "User Role"));

    // ---------------------------------------------------------
    // GRIDBAG CONSTRAINTS & ADD TO PANEL
    // ---------------------------------------------------------
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.CENTER;

    // Message box first
    gbc.gridy = 0;
    gbc.insets = new Insets(20, 0, 20, 0);
    programMenuPanel.add(messageBox, gbc);

    // Buttons
    for (int i = 0; i < buttons.length; i++) {
        gbc.gridy = i + 1; // start after message box
        gbc.insets = new Insets(20, 0, 20, 0);
        programMenuPanel.add(buttons[i], gbc);
    }
}

private void signIn() {
    // -------------------------------
    // Sign-in panel
    // -------------------------------
    JPanel signInPanel = new JPanel(new GridBagLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setOpaque(false); // allow parent background to show
        }
    };

    // -------------------------------
    // Welcome message
    // -------------------------------
    JLabel welcomeLabel = new JLabel("Welcome Back! Sign in to continue");
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
    welcomeLabel.setForeground(Color.BLACK);

    // -------------------------------
    // Rounded Labels
    // -------------------------------
    RoundedLabel userLabel = new RoundedLabel("Username:");
    userLabel.setBackground(Color.decode("#2b643b"));

    RoundedLabel passLabel = new RoundedLabel("Password:");
    passLabel.setBackground(Color.decode("#2b643b"));

    // -------------------------------
    // Rounded Text Fields
    // -------------------------------
    RoundedTextField usernameField = new RoundedTextField(20);
    usernameField.setBackground(new Color(255, 255, 255, 220));
    usernameField.setForeground(Color.BLACK);

    RoundedPasswordField passwordField = new RoundedPasswordField(20);
    passwordField.setBackground(new Color(255, 255, 255, 220));
    passwordField.setForeground(Color.BLACK);

    // -------------------------------
    // Rounded Buttons
    // -------------------------------
    RoundedButton submitButton = new RoundedButton("Sign In");
    RoundedButton cancelButton = new RoundedButton("Cancel");
    Color buttonColor = Color.decode("#2b643b");

    for (RoundedButton btn : new RoundedButton[]{submitButton, cancelButton}) {
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setCornerRadius(25);
        btn.setOpaque(false);
    }

    // -------------------------------
    // Layout
    // -------------------------------
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.CENTER;
    int hOffset = 50, vOffset = 20;

    // Welcome label
    gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
    gbc.insets = new Insets(10 + vOffset, hOffset, 20, 10);
    signInPanel.add(welcomeLabel, gbc);

    // Username
    gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
    gbc.insets = new Insets(5, hOffset, 5, 5);
    signInPanel.add(userLabel, gbc);
    gbc.gridx = 1;
    gbc.insets = new Insets(5, 5, 5, hOffset);
    signInPanel.add(usernameField, gbc);

    // Password
    gbc.gridy = 2; gbc.gridx = 0;
    gbc.insets = new Insets(5, hOffset, 5, 5);
    signInPanel.add(passLabel, gbc);
    gbc.gridx = 1;
    gbc.insets = new Insets(5, 5, 5, hOffset);
    signInPanel.add(passwordField, gbc);

    // Buttons
    gbc.gridy = 3; gbc.gridx = 0;
    gbc.insets = new Insets(15, hOffset, 15, 5);
    signInPanel.add(submitButton, gbc);
    gbc.gridx = 1;
    gbc.insets = new Insets(15, 5, 15, hOffset);
    signInPanel.add(cancelButton, gbc);

    // -------------------------------
    // Dialog
    // -------------------------------
    JDialog dialog = new JDialog(frame, "Sign In", true);
    dialog.getContentPane().add(signInPanel);
    dialog.pack();
    dialog.setLocationRelativeTo(frame);

    // -------------------------------
    // Actions
    // -------------------------------
    submitButton.addActionListener(e -> {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (authenticateUser(username, password)) {
            currentUser = username;
            dialog.dispose();
            cardLayout.show(mainPanel, "Program Menu");
        } else {
            JOptionPane.showMessageDialog(dialog, "Incorrect username or password", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    });

    cancelButton.addActionListener(e -> dialog.dispose());

    dialog.setVisible(true);
}

private void signUp() {
    // -------------------------------
    // Sign-up panel
    // -------------------------------
    JPanel signUpPanel = new JPanel(new GridBagLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setOpaque(false); // allow parent background to show
        }
    };

    // -------------------------------
    // Welcome message
    // -------------------------------
    JLabel welcomeLabel = new JLabel("Join Us! Create your account below");
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
    welcomeLabel.setForeground(Color.BLACK);

    // -------------------------------
    // Rounded Labels
    // -------------------------------
    RoundedLabel userLabel = new RoundedLabel("New Username:");
    userLabel.setBackground(Color.decode("#2b643b"));

    RoundedLabel passLabel = new RoundedLabel("New Password:");
    passLabel.setBackground(Color.decode("#2b643b"));

    // -------------------------------
    // Rounded Text Fields
    // -------------------------------
    RoundedTextField usernameField = new RoundedTextField(20);
    usernameField.setBackground(new Color(255, 255, 255, 220));
    usernameField.setForeground(Color.BLACK);

    RoundedPasswordField passwordField = new RoundedPasswordField(20);
    passwordField.setBackground(new Color(255, 255, 255, 220));
    passwordField.setForeground(Color.BLACK);

    // -------------------------------
    // Rounded Buttons
    // -------------------------------
    RoundedButton submitButton = new RoundedButton("Sign Up");
    RoundedButton cancelButton = new RoundedButton("Cancel");
    Color buttonColor = Color.decode("#2b643b");

    for (RoundedButton btn : new RoundedButton[]{submitButton, cancelButton}) {
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setCornerRadius(25);
        btn.setOpaque(false);
    }

    // -------------------------------
    // Layout
    // -------------------------------
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.CENTER;
    int hOffset = 50, vOffset = 20;

    // Welcome label
    gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
    gbc.insets = new Insets(10 + vOffset, hOffset, 20, 10);
    signUpPanel.add(welcomeLabel, gbc);

    // Username
    gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
    gbc.insets = new Insets(5, hOffset, 5, 5);
    signUpPanel.add(userLabel, gbc);
    gbc.gridx = 1;
    gbc.insets = new Insets(5, 5, 5, hOffset);
    signUpPanel.add(usernameField, gbc);

    // Password
    gbc.gridy = 2; gbc.gridx = 0;
    gbc.insets = new Insets(5, hOffset, 5, 5);
    signUpPanel.add(passLabel, gbc);
    gbc.gridx = 1;
    gbc.insets = new Insets(5, 5, 5, hOffset);
    signUpPanel.add(passwordField, gbc);

    // Buttons
    gbc.gridy = 3; gbc.gridx = 0;
    gbc.insets = new Insets(15, hOffset, 15, 5);
    signUpPanel.add(submitButton, gbc);
    gbc.gridx = 1;
    gbc.insets = new Insets(15, 5, 15, hOffset);
    signUpPanel.add(cancelButton, gbc);

    // -------------------------------
    // Dialog
    // -------------------------------
    JDialog dialog = new JDialog(frame, "Sign Up", true);
    dialog.getContentPane().add(signUpPanel);
    dialog.pack();
    dialog.setLocationRelativeTo(frame);

    // -------------------------------
    // Actions
    // -------------------------------
    submitButton.addActionListener(e -> {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (registerUser(username, password)) {
            JOptionPane.showMessageDialog(dialog, "Sign-up successful! You can now sign in.");
            dialog.dispose();
        } else {
            JOptionPane.showMessageDialog(dialog, "Username already exists. Please try again.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    });

    cancelButton.addActionListener(e -> dialog.dispose());

    dialog.setVisible(true);
}

private void addIncome() {
    // -------------------------------
    // Add Income panel
    // -------------------------------
    JPanel incomePanel = new JPanel(new GridBagLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setOpaque(false);
        }
    };

    // -------------------------------
    // Welcome message
    // -------------------------------
    JLabel welcomeLabel = new JLabel("Add Your Income");
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
    welcomeLabel.setForeground(Color.BLACK);

    // -------------------------------
    // Rounded Labels
    // -------------------------------
    RoundedLabel sourceLabel = new RoundedLabel("Income Source:");
    sourceLabel.setBackground(Color.decode("#2b643b"));

    RoundedLabel amountLabel = new RoundedLabel("Income Amount:");
    amountLabel.setBackground(Color.decode("#2b643b"));

    // -------------------------------
    // Rounded Inputs
    // -------------------------------
JComboBox<String> sourceCombo = new JComboBox<>(new String[]{"Allowance","Part-Time Job","Freelance","Scholarship/Grant","Internship Stipend",
    "Business/Side Hustle","Commission","Tutoring","Project-Based Income","Gift/Donation"});    
    sourceCombo.setEditable(true);
    sourceCombo.setBackground(new Color(255, 255, 255, 220));
    sourceCombo.setForeground(Color.BLACK);
    sourceCombo.setFont(new Font("Arial", Font.PLAIN, 18));

    // Amount field with peso symbol
    JPanel amountFieldPanel = new JPanel(new BorderLayout());
    amountFieldPanel.setOpaque(false);
    JLabel pesoLabel = new JLabel("₱ ");
    pesoLabel.setFont(new Font("Arial", Font.BOLD, 18));
    pesoLabel.setForeground(Color.BLACK);

    RoundedTextField amountField = new RoundedTextField(20); // width adjustable here
    amountField.setBackground(new Color(255, 255, 255, 220));
    amountField.setForeground(Color.BLACK);

    amountFieldPanel.add(pesoLabel, BorderLayout.WEST);
    amountFieldPanel.add(amountField, BorderLayout.CENTER);

    // -------------------------------
    // Rounded Buttons
    // -------------------------------
    RoundedButton okButton = new RoundedButton("OK");
    RoundedButton cancelButton = new RoundedButton("Cancel");
    Color buttonColor = Color.decode("#2b643b");

    for (RoundedButton btn : new RoundedButton[]{okButton, cancelButton}) {
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setCornerRadius(25);
        btn.setOpaque(false);
    }

    // -------------------------------
    // Layout
    // -------------------------------
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.CENTER;
    int hOffset = 50, vOffset = 20;

    // Welcome label
    gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
    gbc.insets = new Insets(10 + vOffset, hOffset, 20, 10);
    incomePanel.add(welcomeLabel, gbc);

    // Source label & combo
    gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
    gbc.insets = new Insets(5, hOffset, 5, 5);
    incomePanel.add(sourceLabel, gbc);
    gbc.gridx = 1;
    gbc.insets = new Insets(5, 5, 5, hOffset);
    incomePanel.add(sourceCombo, gbc);

    // Amount label & field panel
    gbc.gridy = 2; gbc.gridx = 0;
    gbc.insets = new Insets(5, hOffset, 5, 5);
    incomePanel.add(amountLabel, gbc);
    gbc.gridx = 1;
    gbc.insets = new Insets(5, 5, 5, hOffset);
    incomePanel.add(amountFieldPanel, gbc);

    // Buttons
    gbc.gridy = 3; gbc.gridx = 0;
    gbc.insets = new Insets(15, hOffset, 15, 5);
    incomePanel.add(okButton, gbc);
    gbc.gridx = 1;
    gbc.insets = new Insets(15, 5, 15, hOffset);
    incomePanel.add(cancelButton, gbc);

    // -------------------------------
    // Dialog
    // -------------------------------
    JDialog dialog = new JDialog(frame, "Add Income", true);
    dialog.getContentPane().add(incomePanel);
    dialog.pack();
    dialog.setLocationRelativeTo(frame);

    // -------------------------------
    // Actions
    // -------------------------------
    okButton.addActionListener(e -> {
        String source = sourceCombo.getEditor().getItem().toString().trim();
        String amountStr = amountField.getText().trim();

        try {
            float amount = Float.parseFloat(amountStr);
            amount = Float.parseFloat(String.format("%.2f", amount));
            writeUserData(currentUser, amount, source, "Income");
            dialog.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "Invalid amount entered.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    });

    cancelButton.addActionListener(e -> dialog.dispose());

    dialog.setVisible(true);
}

private void addExpense() {
    // -------------------------------
    // Add Expense panel
    // -------------------------------
    JPanel expensePanel = new JPanel(new GridBagLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setOpaque(false);
        }
    };

    // -------------------------------
    // Welcome message
    // -------------------------------
    JLabel welcomeLabel = new JLabel("Add Your Expense");
    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
    welcomeLabel.setForeground(Color.BLACK);

    // -------------------------------
    // Rounded Labels
    // -------------------------------
    RoundedLabel sourceLabel = new RoundedLabel("Expense Source:");
    sourceLabel.setBackground(Color.decode("#2b643b"));

    RoundedLabel amountLabel = new RoundedLabel("Expense Amount:");
    amountLabel.setBackground(Color.decode("#2b643b"));

    // -------------------------------
    // Rounded Inputs
    // -------------------------------
    JComboBox<String> sourceCombo = new JComboBox<>(new String[]{"Food","Transportation","School Supplies","Tuition/Fees","Bills/Utilities",
    "Rent/Boarding","Shopping","Entertainment","Healthcare","Personal Care","Subscriptions","Travel/Trips"});
    sourceCombo.setEditable(true);
    sourceCombo.setBackground(new Color(255, 255, 255, 220));
    sourceCombo.setForeground(Color.BLACK);
    sourceCombo.setFont(new Font("Arial", Font.PLAIN, 18));

    // Amount field with peso symbol
    JPanel amountFieldPanel = new JPanel(new BorderLayout());
    amountFieldPanel.setOpaque(false);
    JLabel pesoLabel = new JLabel("₱ ");
    pesoLabel.setFont(new Font("Arial", Font.BOLD, 18));
    pesoLabel.setForeground(Color.BLACK);

    RoundedTextField amountField = new RoundedTextField(20); // width adjustable
    amountField.setBackground(new Color(255, 255, 255, 220));
    amountField.setForeground(Color.BLACK);

    amountFieldPanel.add(pesoLabel, BorderLayout.WEST);
    amountFieldPanel.add(amountField, BorderLayout.CENTER);

    // -------------------------------
    // Rounded Buttons
    // -------------------------------
    RoundedButton okButton = new RoundedButton("OK");
    RoundedButton cancelButton = new RoundedButton("Cancel");
    Color buttonColor = Color.decode("#2b643b");

    for (RoundedButton btn : new RoundedButton[]{okButton, cancelButton}) {
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setCornerRadius(25);
        btn.setOpaque(false);
    }

    // -------------------------------
    // Layout
    // -------------------------------
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.CENTER;
    int hOffset = 50, vOffset = 20;

    // Welcome label
    gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
    gbc.insets = new Insets(10 + vOffset, hOffset, 20, 10);
    expensePanel.add(welcomeLabel, gbc);

    // Source label & combo
    gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
    gbc.insets = new Insets(5, hOffset, 5, 5);
    expensePanel.add(sourceLabel, gbc);
    gbc.gridx = 1;
    gbc.insets = new Insets(5, 5, 5, hOffset);
    expensePanel.add(sourceCombo, gbc);

    // Amount label & field panel
    gbc.gridy = 2; gbc.gridx = 0;
    gbc.insets = new Insets(5, hOffset, 5, 5);
    expensePanel.add(amountLabel, gbc);
    gbc.gridx = 1;
    gbc.insets = new Insets(5, 5, 5, hOffset);
    expensePanel.add(amountFieldPanel, gbc);

    // Buttons
    gbc.gridy = 3; gbc.gridx = 0;
    gbc.insets = new Insets(15, hOffset, 15, 5);
    expensePanel.add(okButton, gbc);
    gbc.gridx = 1;
    gbc.insets = new Insets(15, 5, 15, hOffset);
    expensePanel.add(cancelButton, gbc);

    // -------------------------------
    // Dialog
    // -------------------------------
    JDialog dialog = new JDialog(frame, "Add Expense", true);
    dialog.getContentPane().add(expensePanel);
    dialog.pack();
    dialog.setLocationRelativeTo(frame);

    // -------------------------------
    // Actions
    // -------------------------------
    okButton.addActionListener(e -> {
        String source = sourceCombo.getEditor().getItem().toString().trim();
        String amountStr = amountField.getText().trim();

        try {
            float amount = Float.parseFloat(amountStr);
            amount = Float.parseFloat(String.format("%.2f", amount));
            writeUserData(currentUser, amount, source, "Expense");
            dialog.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, "Invalid amount entered.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    });

    cancelButton.addActionListener(e -> dialog.dispose());

    dialog.setVisible(true);
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
    // -------------------------------
    // Admin Menu Panel with Background
    // -------------------------------
    JPanel adminMenuPanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon bg = new ImageIcon(getClass().getResource("admin_bg.jpeg"));
            g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    };
    adminMenuPanel.setLayout(new GridBagLayout());

    // -------------------------------
    // MESSAGE BOX PANEL
    // -------------------------------
    JPanel messageBox = new JPanel(new GridBagLayout());
    messageBox.setBackground(new Color(0, 0, 0, 150)); // semi-transparent
    messageBox.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

    JLabel label = new JLabel("<html><center>Welcome, Admin!<br>Select an action</center></html>");
    label.setFont(new Font("Arial", Font.BOLD, 25));
    label.setForeground(Color.WHITE);
    messageBox.add(label);

    // -------------------------------
    // ROUNDED BUTTONS
    // -------------------------------
    Color buttonColor = Color.decode("#2b643b");

    RoundedButton viewUserDataButton = new RoundedButton("View User Data");
    RoundedButton editUserDataButton = new RoundedButton("Edit User Data");
    RoundedButton removeUserDataButton = new RoundedButton("Remove User");
    RoundedButton adminExitButton = new RoundedButton("Exit");

    RoundedButton[] buttons = {viewUserDataButton, editUserDataButton, removeUserDataButton, adminExitButton};

    for (RoundedButton btn : buttons) {
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setCornerRadius(25);
        btn.setOpaque(false);
    }

    // -------------------------------
    // BUTTON ACTIONS
    // -------------------------------
    viewUserDataButton.addActionListener(e -> viewAllUsers());
    editUserDataButton.addActionListener(e -> editUserFile());
    removeUserDataButton.addActionListener(e -> removeUserFile());
    adminExitButton.addActionListener(e -> cardLayout.show(mainPanel, "User Role"));

    // -------------------------------
    // GRIDBAG CONSTRAINTS & ADD TO PANEL
    // -------------------------------
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.anchor = GridBagConstraints.CENTER;

    // Message box first
    gbc.gridy = 0;
    gbc.insets = new Insets(20, 0, 20, 0);
    adminMenuPanel.add(messageBox, gbc);

    // Buttons
    for (int i = 0; i < buttons.length; i++) {
        gbc.gridy = i + 1; // start after message box
        gbc.insets = new Insets(20, 0, 20, 0);
        adminMenuPanel.add(buttons[i], gbc);
    }

    // -------------------------------
    // Add to main panel and show
    // -------------------------------
    mainPanel.add(adminMenuPanel, "Admin Menu Actions");
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

    // -------------------------------
    // Summary Frame
    // -------------------------------
    JFrame summaryFrame = new JFrame("Summary - " + currentUser);
    summaryFrame.setSize(1200, 700);
    summaryFrame.setLayout(new BorderLayout());

    // -------------------------------
    // Initialize models
    // -------------------------------
    incomeModel = new DefaultTableModel(new String[]{"Amount", "Category", "Date"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 0 || column == 1;
        }
    };

    expenseModel = new DefaultTableModel(new String[]{"Amount", "Category", "Date"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 0 || column == 1;
        }
    };

    float totalIncome = 0, totalExpense = 0;
    TreeMap<String, Float> incomeMonthly = new TreeMap<>();
    TreeMap<String, Float> expenseMonthly = new TreeMap<>();

    // -------------------------------
    // Read user data
    // -------------------------------
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split(",");
            if (p.length < 4) continue;

            try {
                float amount = Float.parseFloat(p[0]);
                String category = p[1];
                String type = p[2];
                String date = p[3];
                String monthKey = date.substring(3); // MM-YYYY

                if (type.equalsIgnoreCase("Income")) {
                    totalIncome += amount;
                    incomeModel.addRow(new Object[]{amount, category, date});
                    incomeMonthly.put(monthKey, incomeMonthly.getOrDefault(monthKey, 0f) + amount);
                } else if (type.equalsIgnoreCase("Expense")) {
                    totalExpense += amount;
                    expenseModel.addRow(new Object[]{amount, category, date});
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

    // -------------------------------
    // Add total rows
    // -------------------------------
    incomeModel.addRow(new Object[]{"", "TOTAL:", totalIncome});
    expenseModel.addRow(new Object[]{"", "TOTAL:", totalExpense});

    // -------------------------------
    // Initialize tables
    // -------------------------------
    incomeTable = new JTable(incomeModel);
    expenseTable = new JTable(expenseModel);

    Font tableFont = new Font("Arial", Font.PLAIN, 16);
    incomeTable.setFont(tableFont);
    expenseTable.setFont(tableFont);
    incomeTable.setRowHeight(25);
    expenseTable.setRowHeight(25);

    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
    rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
    incomeTable.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);
    expenseTable.getColumnModel().getColumn(0).setCellRenderer(rightRenderer);

    // Bold total row
    DefaultTableCellRenderer boldRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (row == table.getRowCount() - 1) c.setFont(c.getFont().deriveFont(Font.BOLD));
            return c;
        }
    };
    for (int i = 0; i < incomeTable.getColumnCount(); i++)
        incomeTable.getColumnModel().getColumn(i).setCellRenderer(boldRenderer);
    for (int i = 0; i < expenseTable.getColumnCount(); i++)
        expenseTable.getColumnModel().getColumn(i).setCellRenderer(boldRenderer);

    // -------------------------------
    // Buttons
    // -------------------------------
    Color editButtonColor = Color.decode("#2b643b");
    Color deleteButtonColor = new Color(192, 80, 77);

    RoundedButton editIncomeButton = new RoundedButton("Update Income");
    editIncomeButton.setBackground(editButtonColor);
    editIncomeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
    editIncomeButton.setCornerRadius(15);
    editIncomeButton.setPreferredSize(new Dimension(150, 35));

    RoundedButton editExpenseButton = new RoundedButton("Update Expense");
    editExpenseButton.setBackground(editButtonColor);
    editExpenseButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
    editExpenseButton.setCornerRadius(15);
    editExpenseButton.setPreferredSize(new Dimension(160, 35));

    RoundedButton deleteIncomeButton = new RoundedButton("Delete Income");
    deleteIncomeButton.setBackground(deleteButtonColor);
    deleteIncomeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
    deleteIncomeButton.setCornerRadius(15);
    deleteIncomeButton.setPreferredSize(new Dimension(150, 35));

    RoundedButton deleteExpenseButton = new RoundedButton("Delete Expense");
    deleteExpenseButton.setBackground(deleteButtonColor);
    deleteExpenseButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
    deleteExpenseButton.setCornerRadius(15);
    deleteExpenseButton.setPreferredSize(new Dimension(160, 35));

    // -------------------------------
    // Button actions
    // -------------------------------
    deleteIncomeButton.addActionListener(e -> {
        int row = incomeTable.getSelectedRow();
        if (row >= 0 && row != incomeTable.getRowCount() - 1) {
            incomeModel.removeRow(row);
            saveAllToFile();
        }
    });

    deleteExpenseButton.addActionListener(e -> {
        int row = expenseTable.getSelectedRow();
        if (row >= 0 && row != expenseTable.getRowCount() - 1) {
            expenseModel.removeRow(row);
            saveAllToFile();
        }
    });

    editIncomeButton.addActionListener(e -> editTableRow(incomeTable, incomeModel, "Income"));
    editExpenseButton.addActionListener(e -> editTableRow(expenseTable, expenseModel, "Expense"));

    // -------------------------------
    // Layout tables & buttons
    // -------------------------------
    JPanel incomeButtonPanel = new JPanel();
    incomeButtonPanel.add(editIncomeButton);
    incomeButtonPanel.add(deleteIncomeButton);

    JPanel expenseButtonPanel = new JPanel();
    expenseButtonPanel.add(editExpenseButton);
    expenseButtonPanel.add(deleteExpenseButton);

    JScrollPane incomeScroll = new JScrollPane(incomeTable);
    incomeScroll.setPreferredSize(new Dimension(700, 500));
    incomeScroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Income Transactions",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 25)
    ));

    JScrollPane expenseScroll = new JScrollPane(expenseTable);
    expenseScroll.setPreferredSize(new Dimension(700, 500));
    expenseScroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Expense Transactions",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 25)
    ));

    JPanel incomePanel = new JPanel(new BorderLayout());
    incomePanel.add(incomeScroll, BorderLayout.CENTER);
    incomePanel.add(incomeButtonPanel, BorderLayout.SOUTH);

    JPanel expensePanel = new JPanel(new BorderLayout());
    expensePanel.add(expenseScroll, BorderLayout.CENTER);
    expensePanel.add(expenseButtonPanel, BorderLayout.SOUTH);

    JPanel tablePanel = new JPanel(new GridLayout(1, 2, 10, 10));
    tablePanel.add(incomePanel);
    tablePanel.add(expensePanel);

    // -------------------------------
    // Charts
    // -------------------------------
    JPanel savingsPanel = ChartHelper.createSavingsPieChartPanel(totalIncome, totalExpense);
    JPanel monthlyBarPanel = ChartHelper.createMonthlyIncomeExpenseBarChart(incomeMonthly, expenseMonthly);

    JSplitPane chartSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, monthlyBarPanel, savingsPanel);
    chartSplit.setDividerLocation(600);

    JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tablePanel, chartSplit);
    mainSplit.setDividerLocation(300);

    summaryFrame.add(mainSplit, BorderLayout.CENTER);
    summaryFrame.setLocationRelativeTo(frame);
    summaryFrame.setVisible(true);
}

private void editTableRow(JTable table, DefaultTableModel model, String type) {
    int row = table.getSelectedRow();
    if (row >= 0 && row != model.getRowCount() - 1) {
        Object currentAmount = table.getValueAt(row, 0);
        Object currentCategory = table.getValueAt(row, 1);
        String newAmount = JOptionPane.showInputDialog(frame, "Edit Amount:", currentAmount);
        String newCategory = JOptionPane.showInputDialog(frame, "Edit Category:", currentCategory);
        try {
            float amount = Float.parseFloat(newAmount);
            table.setValueAt(amount, row, 0);
            table.setValueAt(newCategory, row, 1);
            saveAllToFile();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

private void saveAllToFile() {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(currentUser + ".txt"))) {
        for (int i = 0; i < incomeModel.getRowCount() - 1; i++) {
            Object amount = incomeModel.getValueAt(i, 0);
            Object category = incomeModel.getValueAt(i, 1);
            Object date = incomeModel.getValueAt(i, 2);
            bw.write(amount + "," + category + ",Income," + date);
            bw.newLine();
        }
        for (int i = 0; i < expenseModel.getRowCount() - 1; i++) {
            Object amount = expenseModel.getValueAt(i, 0);
            Object category = expenseModel.getValueAt(i, 1);
            Object date = expenseModel.getValueAt(i, 2);
            bw.write(amount + "," + category + ",Expense," + date);
            bw.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

class ChartHelper {

// ------------------------------------------------------------
// 1. Income vs Expense Pie Chart (Resizable Hollow Donut, Text Below Savings)
// ------------------------------------------------------------
static JPanel createSavingsPieChartPanel(float totalIncome, float totalExpense) {

    float income = Math.max(totalIncome, 0);
    float expense = Math.max(totalExpense, 0);
    float savings = Math.max(totalIncome - totalExpense, 0);

    // Dataset
    DefaultPieDataset dataset = new DefaultPieDataset();
    dataset.setValue("Income", income);
    dataset.setValue("Expense", expense);

    // Create chart
    JFreeChart chart = ChartFactory.createPieChart(
            null,  // no title
            dataset,
            false,
            false,
            false
    );

    PiePlot plot = (PiePlot) chart.getPlot();
    plot.setSectionPaint("Income", Color.decode("#2b643b")); // green
    plot.setSectionPaint("Expense", Color.decode("#C0504D")); // red
    plot.setCircular(true);
    plot.setInteriorGap(0.15); // hollow donut
    plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} - ₱{1} ({2})"));
    plot.setLabelFont(new Font("Arial", Font.BOLD, 14));
    plot.setLabelPaint(Color.BLACK);
    plot.setSimpleLabels(true);

    chart.setBackgroundPaint(Color.WHITE);
    plot.setBackgroundPaint(Color.WHITE);

    // Custom ChartPanel that scales chart and draws savings text below
    ChartPanel panel = new ChartPanel(chart) {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Draw savings text below the pie chart
            String text = String.format("Savings - ₱%.2f", savings);
            int fontSize = Math.max(14, height / 20); // scales with panel height
            g2.setFont(new Font("Arial", Font.BOLD, fontSize));
            g2.setColor(Color.decode("#2b643b"));

            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();

            int x = (width - textWidth) / 2;
            int y = height - textHeight / 2;

            g2.drawString(text, x, y);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(400, 400); // default size
        }
    };

    // Allow resizing
    panel.setMinimumSize(new Dimension(200, 200));
    panel.setPreferredSize(new Dimension(400, 400));
    panel.setMaximumSize(new Dimension(800, 800));

    return panel;
}

    // ------------------------------------------------------------
    // 2. Monthly Income vs Expense Bar Chart
    // ------------------------------------------------------------
    static JPanel createMonthlyIncomeExpenseBarChart(
            TreeMap<String, Float> incomeMonthly,
            TreeMap<String, Float> expenseMonthly
    ) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Load income data
        incomeMonthly.forEach((month, value) ->
                dataset.addValue(value, "Income", month)
        );

        // Load expense data
        expenseMonthly.forEach((month, value) ->
                dataset.addValue(value, "Expense", month)
        );

        // Create bar chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Monthly Income vs Expense",
                "Month",
                "Amount (₱)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        return new ChartPanel(chart);
    }
}

private void getSuggestions() {

    List<Object[]> incomeRows = getAllIncomeRows();
    List<Object[]> expenseRows = getAllExpenseRows();

    double totalIncome = 0, totalExpense = 0;

    for (Object[] r : incomeRows)
        totalIncome += Double.parseDouble(r[0].toString());
    for (Object[] r : expenseRows)
        totalExpense += Double.parseDouble(r[0].toString());

    double totalSavings = totalIncome - totalExpense;

    // Sort expenses for top 3
    expenseRows.sort((a, b) -> Double.compare(
            Double.parseDouble(b[0].toString()),
            Double.parseDouble(a[0].toString())
    ));

    StringBuilder top3 = new StringBuilder();
    for (int i = 0; i < Math.min(3, expenseRows.size()); i++) {
        Object[] e = expenseRows.get(i);
        top3.append("• ₱").append(e[0]).append(" - ").append(e[1]).append("\n");
    }

    // Catchphrases
    String[] catchPhrases = {
            "Careful. Your spending is rising fast.",
            "Your wallet is reaching its limit.",
            "High expenses detected.",
            "Your finances are under pressure.",
            "Your spendings are unusually high."
    };
    String spendingPhrase = catchPhrases[(int) (Math.random() * catchPhrases.length)];

    // Savings evaluation
    String savingNudge;
    if (totalIncome == 0) {
        savingNudge = "No income recorded. Please add income data to get accurate savings insights.";
    } else if (totalSavings < totalIncome * 0.10) {
        savingNudge = "You saved less than 10% of your income. Consider reviewing your spending habits and try to set aside more regularly.";
    } else if (totalSavings < totalIncome * 0.30) {
        savingNudge = "Good start! You've saved between 10% and 30% of your income. Keep building your momentum.";
    } else if (totalSavings < totalIncome * 0.50) {
        savingNudge = "Great job! Your savings show strong financial habits. Consider investment opportunities.";
    } else {
        savingNudge = "Outstanding savings management! Over 50% savings indicates elite discipline.";
    }

    // Financial Health Score
    double score = (totalIncome + totalSavings) - totalExpense;
    String status;

    if (score < -5000) status = "FINANCIALLY COOKED";
    else if (score < -2000) status = "YOU'RE COOKED";
    else if (score < 0) status = "NOT LOOKING GOOD";
    else if (score < 2000) status = "SURVIVING OUT OF PURE LUCK";
    else if (score < 6000) status = "KINDA WINNING";
    else if (score < 12000) status = "WINNING";
    else if (score < 20000) status = "PEAK MONEY MINDSET";
    else status = "ASCENDED FINANCIAL DEITY";

// Feedback map (unchanged)
Map<String, String[]> fb = new HashMap<>();

fb.put("FINANCIALLY COOKED", new String[]{
        "Your wallet is in the deepest pit imaginable. This is beyond rock bottom—you have discovered the secret basement under rock bottom.",
        "You are financially speedrunning disaster. Immediate cooldown required. Cut everything that isn't oxygen.",
        "Your budget is on life support and the doctor is shaking his head. Time to emergency-stop all spending."
});

fb.put("YOU'RE COOKED", new String[]{
        "The situation is bad. Like, 'someone check on you' bad. Your expenses are dropping elbows on your savings.",
        "Money is leaving faster than your will to budget. Please stop the financial bleeding immediately.",
        "You're in the danger zone. One more impulse purchase and your bank account might file a restraining order."
});

fb.put("NOT LOOKING GOOD", new String[]{
        "You're walking on financial thin ice, and it is cracking loudly. One wrong step and you’re underwater.",
        "You're not negative yet, but your money is sweating. A small adjustment will save you from disaster.",
        "You're close to the red zone. Tighten up before things get unfunny real quick."
});

fb.put("SURVIVING OUT OF PURE LUCK", new String[]{
        "You’re staying afloat purely because the universe is giving you a pity buff. Do not rely on this buff.",
        "Your finances are held together by hopes, dreams, and maybe tape. Build some actual stability now.",
        "You’re not struggling, but you’re definitely not safe either. A little discipline goes a long way."
});

fb.put("KINDA WINNING", new String[]{
        "You're starting to win at this money thing. Still not main character level, but definitely supporting character with good arc.",
        "Your financial habits are improving. Keep the momentum so you can fully escape NPC status.",
        "You're doing pretty well. Just don’t get too confident—expenses are always lurking."
});

fb.put("WINNING", new String[]{
        "Strong financial discipline detected. You’re entering protagonist territory.",
        "Your budgeting is clean. Your spending is controlled. Your wallet? Happy.",
        "You’re on track to becoming That Person who actually has their finances together."
});

fb.put("PEAK MONEY MINDSET", new String[]{
        "You’re in your peak era. Your finances are hitting gym PRs daily.",
        "Your wallet is thriving. Your bank app loads without fear. Peak behavior.",
        "You understand money. You command money. Money follows you like a loyal sidekick."
});

fb.put("ASCENDED FINANCIAL DEITY", new String[]{
        "You have transcended mortal budgeting. You are the final boss of financial literacy.",
        "Your money management is so good it should be illegal. You might teach a masterclass accidentally.",
        "You are untouchable. Your wallet is glowing. Your savings fear nothing. Truly divine financial energy."
});


    String feedback = fb.get(status)[(int)(Math.random() * fb.get(status).length)];

    // ----------------------------------------------------
    // GUI Panel Setup
    // ----------------------------------------------------
    JFrame sugFrame = new JFrame("Financial Suggestions");
    sugFrame.setSize(1000, 700);
    sugFrame.setLayout(new BorderLayout());
    sugFrame.setLocationRelativeTo(frame);

    // Fonts & Colors
    Font titleFont = new Font("Segoe UI", Font.BOLD, 25);   // CHANGE SIZE HERE
    Font textFont  = new Font("Segoe UI", Font.PLAIN, 20);  // CHANGE SIZE HERE
    Color textColor = Color.WHITE;

    JPanel container = new JPanel(new GridLayout(2, 2, 15, 15));
    container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    container.setBackground(Color.LIGHT_GRAY);

    // ADD PANELS WITH CUSTOM BACKGROUNDS
    container.add(createSuggestionPanel("Spending Alert",
            spendingPhrase + "\n\nTop Expenses:\n" + top3,
            Color.decode("#058c32ff"), // ← CUSTOM COLOR
            titleFont, textFont, textColor));

    container.add(createSuggestionPanel("Saving Insight",
            savingNudge,
            Color.decode("#2b643b"), // ← CUSTOM COLOR
            titleFont, textFont, textColor));

    container.add(createSuggestionPanel("Financial Health Status",
            "Your status: " + status,
            Color.decode("#2b643b"), // ← CUSTOM COLOR
            titleFont, textFont, textColor));

    container.add(createSuggestionPanel("Feedback",
            feedback,
            Color.decode("#058c32ff"), // ← CUSTOM COLOR
            titleFont, textFont, textColor));

    sugFrame.add(container, BorderLayout.CENTER);
    sugFrame.setVisible(true);
}


private JPanel createSuggestionPanel(String title, String message, Color bgColor, Font titleFont, Font textFont, Color textColor) {
    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
        }
    };

    panel.setOpaque(false);
    panel.setLayout(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
    titleLabel.setFont(titleFont);
    titleLabel.setForeground(textColor);

    JTextArea messageArea = new JTextArea(message);
    messageArea.setFont(textFont);
    messageArea.setForeground(textColor);
    messageArea.setOpaque(false);
    messageArea.setEditable(false);
    messageArea.setHighlighter(null);
    messageArea.setLineWrap(true);
    messageArea.setWrapStyleWord(true);
    messageArea.setAlignmentX(Component.CENTER_ALIGNMENT);
    messageArea.setAlignmentY(Component.CENTER_ALIGNMENT);

    panel.add(titleLabel, BorderLayout.NORTH);
    panel.add(messageArea, BorderLayout.CENTER);

    return panel;
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

public static void main(String[] args) {
        SwingUtilities.invokeLater(BudgetingSystem::new);
    }
}

class RoundedButton extends JButton {

    private int cornerRadius = 20; // Adjust roundness here

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 25));
        setPreferredSize(new Dimension(350, 50));
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    public void paintBorder(Graphics g) {
        // No border
    }
}

class RoundedTextField extends JTextField {
    private int cornerRadius = 25;

    public RoundedTextField(int columns) {
        super(columns);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        super.paintComponent(g);
        g2.dispose();
    }
}

class RoundedPasswordField extends JPasswordField {
    private int cornerRadius = 25;

    public RoundedPasswordField(int columns) {
        super(columns);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        super.paintComponent(g);
        g2.dispose();
    }
}

class RoundedLabel extends JLabel {
    private int cornerRadius = 25;

    public RoundedLabel(String text) {
        super(text);
        setOpaque(false); // allow custom painting
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 24));
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        super.paintComponent(g);
        g2.dispose();
    }
}

