import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BudgetingSystem {
    private JFrame frame;
    private String currentUser = "";
    private JPanel mainPanel, adminPanel, studentPanel, userRolePanel, programMenuPanel;
    private CardLayout cardLayout;

    public BudgetingSystem() {
        frame = new JFrame("Budgeting Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

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
        frame.setResizable(false);
    }

    private void initializeUserRolePanel() {
        userRolePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bg = new ImageIcon(getClass().getResource("acc_bg.jpeg"));
                g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        userRolePanel.setLayout(new GridBagLayout());

        JLabel label = new JLabel("Select User Role");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        JButton studentButton = createStyledButton("Student");
        JButton adminButton = createStyledButton("Admin");
        JButton exitButton = createStyledButton("Exit Program");

        studentButton.addActionListener(e -> cardLayout.show(mainPanel, "Student Menu"));
        adminButton.addActionListener(e -> cardLayout.show(mainPanel, "Admin Menu"));
        exitButton.addActionListener(e -> System.exit(0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        userRolePanel.add(label, gbc);
        gbc.gridy = 1;
        userRolePanel.add(studentButton, gbc);
        gbc.gridy = 2;
        userRolePanel.add(adminButton, gbc);
        gbc.gridy = 3;
        userRolePanel.add(exitButton, gbc);
    }

    private void initializeStudentPanel() {
        studentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bg = new ImageIcon(getClass().getResource("acc_bg.jpeg"));
                g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        studentPanel.setLayout(new GridBagLayout());

        JLabel label = new JLabel("Welcome to the Budgeting Program");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        JButton signInButton = createStyledButton("Sign In");
        JButton signUpButton = createStyledButton("Sign Up");
        JButton backButton = createStyledButton("Exit");

        signInButton.addActionListener(e -> signIn());
        signUpButton.addActionListener(e -> signUp());
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "User Role"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        studentPanel.add(label, gbc);
        gbc.gridy = 1;
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

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = createStyledButton("Login");
        JButton backButton = createStyledButton("Back");

        adminPanel.add(new JLabel("Welcome to the Admin System"), createGbc(0, 0));
        adminPanel.add(new JLabel("Username: "), createGbc(0, 1));
        adminPanel.add(usernameField, createGbc(1, 1));
        adminPanel.add(new JLabel("Password: "), createGbc(0, 2));
        adminPanel.add(passwordField, createGbc(1, 2));
        adminPanel.add(loginButton, createGbc(0, 3));
        adminPanel.add(backButton, createGbc(1, 3));

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
                ImageIcon bg = new ImageIcon(getClass().getResource("menu_bg.jpeg"));
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
        gbc.insets = new Insets(10, 10, 10, 10);
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

    private void adminMenu() {
        JPanel adminMenu = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bg = new ImageIcon(getClass().getResource("admin_bg2.jpeg"));
                g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        adminMenu.setLayout(new GridBagLayout());

        JButton viewUserDataButton = createStyledButton("View User Data");
        JButton editUserDataButton = createStyledButton("Edit User Data");
        JButton removeUserDataButton = createStyledButton("Remove User Data");
        JButton adminExitButton = createStyledButton("Exit");

        viewUserDataButton.addActionListener(e -> displayFile("users.txt"));
        editUserDataButton.addActionListener(e -> editUserFile());
        removeUserDataButton.addActionListener(e -> removeUserFile());
        adminExitButton.addActionListener(e -> cardLayout.show(mainPanel, "User Role"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        adminMenu.add(viewUserDataButton, gbc);
        gbc.gridy = 1;
        adminMenu.add(editUserDataButton, gbc);
        gbc.gridy = 2;
        adminMenu.add(removeUserDataButton, gbc);
        gbc.gridy = 3;
        adminMenu.add(adminExitButton, gbc);

        mainPanel.add(adminMenu, "Admin Menu Actions");
        cardLayout.show(mainPanel, "Admin Menu Actions");
    }

    private void signIn() {
        String username = JOptionPane.showInputDialog(frame, "Enter username [FirstName_LastName]");
        String password = JOptionPane.showInputDialog(frame, "Enter password");

        if (authenticateUser(username, password)) {
            currentUser = username;
            cardLayout.show(mainPanel, "Program Menu");
        } else {
            JOptionPane.showMessageDialog(frame, "Incorrect username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void signUp() {
        String username = JOptionPane.showInputDialog(frame, "Enter a new username [FirstName_LastName]");
        String password = JOptionPane.showInputDialog(frame, "Enter a new password");

        if (registerUser(username, password)) {
            JOptionPane.showMessageDialog(frame, "Sign-up successful! You can now sign in.");
        } else {
            JOptionPane.showMessageDialog(frame, "Username already exists. Please try again.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addIncome() {
        String source = JOptionPane.showInputDialog(frame, "Enter Income Source");
        String amountStr = JOptionPane.showInputDialog(frame, "Enter Income Amount");

        try {
            float amount = Float.parseFloat(amountStr);
            writeUserData(currentUser, amount, source, "Income");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addExpense() {
        String source = JOptionPane.showInputDialog(frame, "Enter Expense Source");
        String amountStr = JOptionPane.showInputDialog(frame, "Enter Expense Amount");

        try {
            float amount = Float.parseFloat(amountStr);
            writeUserData(currentUser, amount, source, "Expense");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void displayFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            StringBuilder userData = new StringBuilder("User Data:\n");
            while ((line = br.readLine()) != null) {
                userData.append(line).append("\n");
            }
            JOptionPane.showMessageDialog(frame, userData.toString(), "User Data", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "User data not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editUserFile() {
        String username = JOptionPane.showInputDialog(frame, "Enter the username to edit data");
        File originalFile = new File(username + ".txt");
        File tempFile = new File("temp.txt");

        if (!originalFile.exists()) {
            JOptionPane.showMessageDialog(frame, "User data not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(originalFile));
                BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (originalFile.delete() && tempFile.renameTo(originalFile)) {
            JOptionPane.showMessageDialog(frame, "User data updated successfully.");
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to update user data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeUserFile() {
        String username = JOptionPane.showInputDialog(frame, "Enter the username to remove data");
        File userFile = new File(username + ".txt");

        if (userFile.delete()) {
            JOptionPane.showMessageDialog(frame, "User data for " + username + " removed successfully.");
        } else {
            JOptionPane.showMessageDialog(frame, "Failed to remove user data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            StringBuilder historyData = new StringBuilder("Transaction History:\n");
            while ((line = br.readLine()) != null) {
                historyData.append(line).append("\n");
            }
            JOptionPane.showMessageDialog(frame, historyData.toString(), "Transaction History",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "No transaction history found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewSummary() {
        String filename = currentUser + ".txt";
        float totalIncome = 0, totalExpenses = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 2) {
                    float amount = Float.parseFloat(parts[0]);
                    String type = parts[2];
                    if (type.equals("Income"))
                        totalIncome += amount;
                    else if (type.equals("Expense"))
                        totalExpenses += amount;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading data for summary.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String summary = "Total Income: " + totalIncome + "\nTotal Expenses: " + totalExpenses + "\nSavings: "
                + (totalIncome - totalExpenses);
        JOptionPane.showMessageDialog(frame, summary, "Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    private void getSuggestions() {
        String message = "1. Save 10% of your income for emergency.\n" +
                "2. Review your subscriptions and cancel the unused ones.\n" +
                "3. Consider using public transport to save on commuting.\n" +
                "4. Start a small coin savings challenge for fun.";
        JOptionPane.showMessageDialog(frame, message, "Suggestions", JOptionPane.INFORMATION_MESSAGE);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isArmed())
                    g.setColor(getBackground().darker());
                else
                    g.setColor(getBackground());
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };

        button.setPreferredSize(new Dimension(200, 40));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(34, 139, 34));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private GridBagConstraints createGbc(int gridx, int gridy) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        return gbc;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BudgetingSystem::new);
    }
}
