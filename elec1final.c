#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <windows.h>
#include <time.h>

 // Function prototypes
 void MaximizeOutputWindow();
 void userRole();
 void studentMenu();
 void adminMenu();
 void displayFile(const char *user);
 void signMenu();
 void signUp();
 int signIn();
 void programMenu(float incomes[], char incomeSources[][50], char incomeFrequency[][10], char incomeDateTime[][20], int *incomeCount, float expenseAmounts[], char expenseSources[][50], char expenseFrequency[][10], char expenseType[][10], char expenseDateTime[][20], int *expenseCount, const char *username, char userType[]);
 void addIncome(float incomes[], char incomeSources[][50], char incomeFrequency[][10], char incomeDateTime[][20], int *incomeCount, const char *username, char userType[]);
 void addExpense(float expenseAmounts[], char expenseSources[][50], char expenseFrequency[][10], char expenseType[][10], char expenseDateTime[][20], int *expenseCount, const char *username, char userType[], char incomeFrequency[][10], int incomeCount);
 void viewHistory(float incomes[], char incomeFrequency[][10], char incomeDateTime[][20], int incomeCount, char incomeSources[][50], char expenseNames[][50], float expenseAmounts[], char expenseFrequency[][10], char expenseType[][10], char expenseDateTime[][20], int expenseCount, const char *username);
 void viewSummary(float incomes[], int incomeCount, char expenseType[][10], float expenseAmounts[], int expenseCount, const char *username);
 void displayMenu(char userType[]);
 void getSuggestions(char userType[]);
 void funChallenges();
 void coinSavingsChallenge();
 void bringYourOwnWaterBottleChallenge();
 void writeUserData(const char *username, float amount, const char *source, const char *frequency, const char *type, const char *dateTime);
 void viewUserFile();
 void editUserFile();
 void removeUserFile();

char currentUser[50] = {0};

int main() {
    MaximizeOutputWindow();
    userRole();
    return 0;
}

// Function to Maximize Output Window
void MaximizeOutputWindow() {
    HWND consoleWindow = GetConsoleWindow();
    ShowWindow(consoleWindow, SW_MAXIMIZE);
}

// Function for Selecting User Role
void userRole() {
    int choice;
    do {
        printf("\n\t\t\t\t\t\t\t\t\t\t\t=== Select user role ===\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[1] Student\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[2] Admin\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[3] Exit Program\n");
        printf("\t\t\t\t\t\t\t\t\t\t\tEnter your choice: ");
        scanf("%d", &choice);

        system("cls");

        switch (choice) {
            case 1:
                studentMenu();
                break;
            case 2:
                adminMenu();
                break;
            case 3:
                printf("\t\t\t\t\t\t\t\t\t\t\tExiting Program.\n");
                break;
            default:
                printf("\t\t\t\t\t\t\t\t\t\t\tInvalid choice. Please enter a valid option.\n");
        }
    } while (choice != 4);
}

//Function for Student Menu
void studentMenu() {
    int choice;
    int Authenticated = 0;
    char userType[10] = "student";
    float incomes[100] = {0};
    char incomeSources[100][50] = {0};
    char incomeFrequency[100][10] = {0};
    char incomeDateTime[100][20] = {0};
    int incomeCount = 0;

    float expenseAmounts[100] = {0};
    char expenseSources[100][50] = {0};
    char expenseFrequency[100][10] = {0};
    char expenseType[100][10] = {0};
    char expenseDateTime[100][20] = {0};
    int expenseCount = 0;


    do {

        printf("\t\t\t\t\t\t\t\t\t\t\t--- Welcome to the Budgeting Program ---\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[1] Sign In\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[2] Sign Up\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[3] Exit\n");
        printf("\t\t\t\t\t\t\t\t\t\t\tEnter your choice: ");


        if (scanf("%d", &choice) != 1) {
            printf("\t\t\t\t\t\t\t\t\t\t\tInvalid input. Please enter a number.\n");
            while (getchar() != '\n');
            continue;
        }


        getchar();
        system("cls");


        switch (choice) {
            case 1:
                Authenticated = signIn();
                break;
            case 2:
                signUp();
                break;
            case 3:
                printf("\t\t\t\t\t\t\t\t\t\t\tGoing back to User Role Menu.\n");
                return;
            default:
                printf("\t\t\t\t\t\t\t\t\t\t\tInvalid choice. Please enter a valid option.\n");
        }
    } while (!Authenticated);


    programMenu(incomes, incomeSources, incomeFrequency, incomeDateTime, &incomeCount, expenseAmounts, expenseSources, expenseFrequency, expenseType, expenseDateTime, &expenseCount, currentUser, userType);
}


//Function for Admin Menu
void adminMenu() {
    int choice;
    char adminUsername[50], adminPassword[50];
    float incomes[100] = {0};
    int incomeCount = 0;
    char expenseType[100][10] = {0};
    float expenseAmounts[100] = {0};
    int expenseCount = 0;


    const char *username = "admin";
    const char *password = "runme";

    printf("\t\t\t\t\t\t\t\t\t\t\t=== Welcome to the Admin System ===\n");
    printf("\t\t\t\t\t\t\t\t\t\t\tEnter username: ");
    scanf("%s", adminUsername);
    printf("\t\t\t\t\t\t\t\t\t\t\tEnter password: ");
    scanf("%s", adminPassword);
    system("cls");

    if (strcmp(adminUsername, username) != 0 || strcmp(adminPassword, password) != 0) {
        printf("\t\t\t\t\t\t\t\t\t\t\tIncorrect username or password. Please try again.\n");
        return;
    }

    do {
        printf("\n\t\t\t\t\t\t\t\t\t\t\t=== Admin Menu ===\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[1] View User Data\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[2] Edit User Data\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[3] Remove User Data\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[4] Exit\n");
        printf("\t\t\t\t\t\t\t\t\t\t\tEnter your choice: ");
        scanf("%d", &choice);
        system("cls");

        switch (choice) {
            case 1:
                displayFile(username);
                viewUserFile();
                break;
            case 2:
                displayFile(username);
                editUserFile();
                break;
            case 3:
                displayFile(username);
                removeUserFile();
                break;
            case 4:
                printf("\t\t\t\t\t\t\t\t\t\t\tExiting Admin Menu. Goodbye!\n");
                break;
            default:
                printf("\t\t\t\t\t\t\t\t\t\t\tInvalid choice. Please enter a valid option.\n");
        }
    } while (choice != 4);
}

//Function for Displaying User
void displayFile(const char *user) {
    FILE *file = fopen("users.txt", "r");

    if (file == NULL) {
    printf("\t\t\t\t\t\t\t\t\t\t\tUser data not found.\n");
    system("cls");
    return;
}

    system("cls");
    char data[100];
    printf("\t\t\t\t\t\t\t\t\t\t\t=== User ===\n");
    while (fgets(data, 100, file) != NULL) {
    printf("\t\t\t\t\t\t\t\t\t\t\t%s\n", data);
}

fclose(file);
}

//Function for User Viewing for Admin
void viewUserFile() {
    char username[50];
    printf("\t\t\t\t\t\t\t\t\t\t\tEnter the username to view data: ");
    scanf("%s", username);

    char filename[50];
    sprintf(filename, "%s.txt", username);
    FILE *userFile = fopen(filename, "r");

    if (userFile == NULL) {
        system("cls");
        printf("\t\t\t\t\t\t\t\t\t\t\tUser data not found.\n");

    }
    else {
        system("cls");
        char data[100];
        printf("\t\t\t\t\t\t\t\t\t\t\tCurrent data of %s:\n\n", username);
        while (fgets(data, 100, userFile) != NULL) {
            printf("\t\t\t\t\t\t\t\t\t\t\t%s\n", data);
        }
        fclose(userFile);
    }
}

//Function for User Editing for Admin
void editUserFile() {
    char username[50];
    printf("\t\t\t\t\t\t\t\t\t\t\tEnter the username to edit data: ");
    scanf("%s", username);

    char filename[50];
    sprintf(filename, "%s.txt", username);
    FILE *userFile = fopen(filename, "r+");

    if (userFile == NULL) {
        system("cls");
        printf("\t\t\t\t\t\t\t\t\t\t\tUser data not found.\n");
        return;
    }

    system("cls");
    char data[100];
    printf("\t\t\t\t\t\t\t\t\t\t\tCurrent data:\n");
    while (fgets(data, 100, userFile) != NULL) {
        printf("\t\t\t\t\t\t\t\t\t\t\t%s\n", data);
    }

    char editType[10];
    printf("\t\t\t\t\t\t\t\t\t\t\tEnter 'Income' or 'Expense' to edit: ");
    scanf("%s", editType);

    char sourceToEdit[50];
    printf("\t\t\t\t\t\t\t\t\t\t\tEnter Source Name to edit: ");
    scanf("%s", sourceToEdit);

    fseek(userFile, 0, SEEK_SET);
    sprintf(filename, "%s.txt", username);
    FILE *tempFile = fopen("temp.txt", "w");

    char line[100];
    while (fgets(line, 100, userFile) != NULL) {
        if (strstr(line, sourceToEdit) != NULL) {
            float amount;
            char source[50], frequency[10], datetime[20];
            sscanf(line, "%f,%[^,],%[^,],%s", &amount, source, frequency, datetime);

            float newAmount;
            char newSource[50], newFrequency[10], newDatetime[20];
            printf("\t\t\t\t\t\t\t\t\t\t\tEnter the New Amount: ");
            scanf("%f", &newAmount);
            printf("\t\t\t\t\t\t\t\t\t\t\tEnter the New Source Name: ");
            scanf("%s", newSource);
            printf("\t\t\t\t\t\t\t\t\t\t\tEnter the New Frequency (Daily, Weekly, Monthly): ");
            scanf("%s", newFrequency);
            printf("\t\t\t\t\t\t\t\t\t\t\tEnter the New Date (xx-xx-xxxx): ");
            scanf("%s", newDatetime);

            fprintf(tempFile, "%.2f,%s,%s,%s\n", newAmount, newSource, newFrequency, newDatetime);
            } else {
                fprintf(tempFile, "%s", line);
            }
        }

        fclose(userFile);
        fclose(tempFile);

        remove(filename);
        rename("temp.txt", filename);

        printf("\t\t\t\t\t\t\t\t\t\t\tUser data updated successfully.\n");
    }

//Function for User Deleting for Admin
void removeUserFile() {
    char username[50];
    printf("\t\t\t\t\t\t\t\t\t\t\tEnter the username to remove data: ");
    scanf("%s", username);

    char filename[50];
    sprintf(filename, "%s.txt", username);

    if (remove(filename) == 0) {

        printf("\t\t\t\t\t\t\t\t\t\t\tUser data for %s removed successfully.\n", username);

    }
    else {
        system("cls");
        printf("\t\t\t\t\t\t\t\t\t\t\tFailed to remove user data.\n");

    }
}

//Function for Main Program Menu
void programMenu(float incomes[], char incomeSources[][50], char incomeFrequency[][10], char incomeDateTime[][20], int *incomeCount, float expenseAmounts[], char expenseSources[][50], char expenseFrequency[][10], char expenseType[][10], char expenseDateTime[][20], int *expenseCount, const char *username, char userType[]) {
    int choice;
    char expenseNames[100][50] = {0}; // Declare expenseNames array

    do {
        printf("\n\t\t\t\t\t\t\t\t\t\t\t--- Budgeting Program ---\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[1] Add Income\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[2] Add Expense\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[3] View History\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[4] View Summary\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[5] Get Suggestions\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[6] Exit\n");
        printf("\t\t\t\t\t\t\t\t\t\t\tEnter your choice: ");
        scanf("%d", &choice);
        system("cls");

        switch (choice) {
            case 1:
                addIncome(incomes, incomeSources, incomeFrequency, incomeDateTime, incomeCount, username, userType);
                break;
            case 2:
                addExpense(expenseAmounts, expenseSources, expenseFrequency, expenseType, expenseDateTime, expenseCount, username, userType, incomeFrequency, *incomeCount);
                break;
            case 3:
               viewHistory(incomes, incomeFrequency, incomeDateTime, *incomeCount, incomeSources, expenseNames, expenseAmounts, expenseFrequency, expenseType, expenseDateTime, *expenseCount, username);
                break;
            case 4:
                viewSummary(incomes, *incomeCount, expenseType, expenseAmounts, *expenseCount, username);
                break;
            case 5:
                getSuggestions(userType);
                break;
            case 6:
                printf("\t\t\t\t\t\t\t\t\t\t\tExiting program. Goodbye!\n");
                return;
            default:
                printf("\t\t\t\t\t\t\t\t\t\t\tInvalid choice. Please enter a valid option.\n");
        }
    } while (1);
}

// Function to Sign In
int signIn() {
    char username[50], password[50], fileUsername[50], filePassword[50];
    FILE *file = fopen("users.txt", "r");

    if (file == NULL) {
        system("cls");
        printf("\t\t\t\t\t\t\t\t\t\t\tNo users found. Please sign up first.\n");
        return 0;
    }

    printf("\t\t\t\t\t\t\t\t\t\t\tEnter username [FirstName_LastName]: ");
    scanf("%s", username);

    printf("\t\t\t\t\t\t\t\t\t\t\tEnter password: ");
    scanf("%s", password);

    while (fscanf(file, "%s %s", fileUsername, filePassword) != EOF) {
        if (strcmp(username, fileUsername) == 0 && strcmp(password, filePassword) == 0) {
            fclose(file);
            system("cls");
            printf("\t\t\t\t\t\t\t\t\t\t\tSign-in successful! Welcome, %s.\n", username);
            strcpy(currentUser, username);
            return 1;
        }
    }

    fclose(file);
    system("cls");
    printf("\t\t\t\t\t\t\t\t\t\t\tIncorrect username or password. Please try again.\n");
    return 0;
}

// Function to Sign Up
void signUp() {
    char username[50], password[50], fileUsername[50], filePassword[50];
    FILE *file = fopen("users.txt", "a+");

    if (file == NULL) {
        system("cls");
        printf("\t\t\t\t\t\t\t\t\t\t\tError opening file.\n");
        return;
    }

    printf("\t\t\t\t\t\t\t\t\t\t\tEnter a new username [FirstName_LastName]: ");
    scanf("%49s", username);
    printf("\t\t\t\t\t\t\t\t\t\t\tEnter a new password: ");
    scanf("%49s", password);

    rewind(file);

    while (fscanf(file, "%49s %49s", fileUsername, filePassword) == 2) {
        if (strcmp(username, fileUsername) == 0 && strcmp(password, filePassword) == 0) {
            system("cls");
            printf("\t\t\t\t\t\t\t\t\t\t\tUsername and password already exist. Please sign in to your account.\n");
            fclose(file);
            return;
        }
        if (strcmp(username, fileUsername) == 0) {
            system("cls");
            printf("\t\t\t\t\t\t\t\t\t\t\tUsername already exists. Please try another username.\n");
            fclose(file);
            return;
        }
    }

    fprintf(file, "%s %s\n", username, password);
    fclose(file);
    system("cls");
    printf("\t\t\t\t\t\t\t\t\t\t\tSign-up successful! You can now sign in.\n");
}

// Add Income Function
void addIncome(float incomes[], char incomeSources[][50], char incomeFrequency[][10], char incomeDateTime[][20], int *incomeCount, const char *username, char userType[]) {
    int continueOption = 1;
    char frequencyChoice[10];
    int frequencyOption;

    // Get income frequency FIRST
    do {
        printf("\n\t\t\t\t\t\t\t\t\t\t\tSelect the frequency of the income:\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[1] Daily\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[2] Weekly\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[3] Monthly\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[4] Back\n"); // Added Back option
        printf("\t\t\t\t\t\t\t\t\t\t\tEnter your choice: ");
        while (scanf("%d", &frequencyOption) != 1 || frequencyOption < 1 || frequencyOption > 4) {
            printf("\t\t\t\t\t\t\t\t\t\t\tInvalid choice. Please select 1, 2, 3 or 4: ");
            while (getchar() != '\n');

        }
        if (frequencyOption == 4) {
            system("cls");
            return;
        }
        strcpy(frequencyChoice, (frequencyOption == 1) ? "Daily" : (frequencyOption == 2) ? "Weekly" : "Monthly");

    } while (frequencyOption == 4);


    while (continueOption == 1) {
        float income;
        int sourceOption;
        char otherSource[50];

        // Subsequent income entries - only ask for source
        do {
            printf("\n\t\t\t\t\t\t\t\t\t\t\tSelect the source of income:\n");
            if (strcmp(userType, "student") == 0) {
                printf("\t\t\t\t\t\t\t\t\t\t\t[1] Part-time's salary\n");
                printf("\t\t\t\t\t\t\t\t\t\t\t[2] Allowance from parents\n");
                printf("\t\t\t\t\t\t\t\t\t\t\t[3] Allowance from scholarship\n");
                printf("\t\t\t\t\t\t\t\t\t\t\t[4] Others\n");
                printf("\t\t\t\t\t\t\t\t\t\t\t[5] Back\n"); // Added Back option
            }
            printf("\t\t\t\t\t\t\t\t\t\t\tEnter your choice: ");

            int choice_limit = (strcmp(userType, "student") == 0) ? 5 : 5;
            while (scanf("%d", &sourceOption) != 1 || sourceOption < 1 || sourceOption > choice_limit) {
                printf("\t\t\t\t\t\t\t\t\t\t\tInvalid choice. Please select a number between 1 and %d: ", choice_limit);
                while (getchar() != '\n');
            }
            if (sourceOption == 5){
                system("cls");
                return; // Go back to the previous menu
            }
        } while (sourceOption == 5);

        if (sourceOption == 4) {
            printf("\t\t\t\t\t\t\t\t\t\t\tPlease specify the source: ");
            scanf(" %[^\n]", otherSource);
            strcpy(incomeSources[*incomeCount], otherSource);
        } else {
            if (strcmp(userType, "student") == 0) {
                const char *sources[] = {"Part-time's salary", "Allowance from parents", "Allowance from scholarship"};
                strcpy(incomeSources[*incomeCount], sources[sourceOption - 1]);
            } else {
                const char *sources[] = {"Salary", "Business", "Side Hustle"};
                strcpy(incomeSources[*incomeCount], sources[sourceOption - 1]);
            }
        }

        do {
            printf("\t\t\t\t\t\t\t\t\t\t\tEnter income amount: ");
            while (scanf("%f", &income) != 1) {
                printf("\t\t\t\t\t\t\t\t\t\t\tInvalid input. Please enter a valid number: ");
                while (getchar() != '\n');
            }
            if (income < 0){
                printf("\t\t\t\t\t\t\t\t\t\t\tIncome can't be negative. Please select a valid number: ");
            }
        } while (income < 0);


        // Adjust income based on frequency (set in the first part)
        if (strcmp(frequencyChoice, "Daily") == 0) {
            income *= 30;
        } else if (strcmp(frequencyChoice, "Weekly") == 0) {
            income *= 4;
        }

        incomes[*incomeCount] = income;
        strcpy(incomeFrequency[*incomeCount], frequencyChoice);

        time_t t = time(NULL);
        struct tm tm = *localtime(&t);
        snprintf(incomeDateTime[*incomeCount], 20, "%02d-%02d-%04d %02d:%02d",
                 tm.tm_mday, tm.tm_mon + 1, tm.tm_year + 1900, tm.tm_hour, tm.tm_min);
        (*incomeCount)++;

        writeUserData(username, income, incomeSources[*incomeCount - 1], incomeFrequency[*incomeCount - 1], "", incomeDateTime[*incomeCount - 1]);

        printf("\n\t\t\t\t\t\t\t\t\t\t\tWhat would you like to do next?\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[1] Add another income\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[2] Back to previous menu\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[3] Exit\n");
        printf("\t\t\t\t\t\t\t\t\t\t\tEnter your choice: ");
        while (scanf("%d", &continueOption) != 1 || (continueOption < 1 || continueOption > 3)) {
            printf("\t\t\t\t\t\t\t\t\t\t\tInvalid choice. Please select 1, 2, or 3: ");
            while (getchar() != '\n');
        }

        if (continueOption == 2) {
             system("cls");
            break;
        } else if (continueOption == 3) {
             system("cls");
            return;
        }
        system("cls");
    }
}

// Add Expense Function
void addExpense(float expenseAmounts[], char expenseSources[][50], char expenseFrequency[][10], char expenseType[][10], char expenseDateTime[][20], int *expenseCount, const char *username, char userType[], char incomeFrequency[][10], int incomeCount) {
    int continueOption = 1;
    char selectedFrequency[10] = "Monthly";

    if (incomeCount > 0) {
        strcpy(selectedFrequency, incomeFrequency[incomeCount - 1]);
    }

    while (continueOption == 1) {
        float amount;
        int expenseChoice, backOption;
        char otherSource[50];
        char *needs[100], *wants[100];
        int needsCount = 0, wantsCount = 0;

        if (strcmp(userType, "student") == 0) {
            needs[needsCount++] = "School project";
            needs[needsCount++] = "Research";
            needs[needsCount++] = "Transportation";
            needs[needsCount++] = "Food";
            needs[needsCount++] = "Boarding house";
            wants[wantsCount++] = "Shopping";
            wants[wantsCount++] = "Entertainment";
            wants[wantsCount++] = "Games";
            wants[wantsCount++] = "Coffee";
            needs[needsCount++] = "Others (Please specify)";
            wants[wantsCount++] = "Others (Please specify)";
        } else {
            needs[needsCount++] = "Electricity bills";
            needs[needsCount++] = "Water bills";
            needs[needsCount++] = "Grocery";
            needs[needsCount++] = "Credit card";
            needs[needsCount++] = "Mortgage";
            needs[needsCount++] = "Transportation";
            needs[needsCount++] = "Food";
            wants[wantsCount++] = "Shopping";
            wants[wantsCount++] = "Entertainment";
            wants[wantsCount++] = "Hanging out";
            wants[wantsCount++] = "Coffee";
            wants[wantsCount++] = "Travel";
            needs[needsCount++] = "Others (Please specify)";
            wants[wantsCount++] = "Others (Please specify)";
        }

        //Expense Type Selection with Back Option
        do {
            printf("\n\t\t\t\t\t\t\t\t\t\t\tSelect Expenses:\n");
            printf("\t\t\t\t\t\t\t\t\t\t\tNeeds:\n");
            for (int i = 0; i < needsCount; i++) {
                printf("\t\t\t\t\t\t\t\t\t\t\t[%d] %s\n", i + 1, needs[i]);
            }
            printf("\n\t\t\t\t\t\t\t\t\t\t\tWants:\n");
            for (int i = 0; i < wantsCount; i++) {
                printf("\t\t\t\t\t\t\t\t\t\t\t[%d] %s\n", i + 1 + needsCount, wants[i]);
            }
            printf("\t\t\t\t\t\t\t\t\t\t\t[%d] Back\n", needsCount + wantsCount + 1);
            printf("\t\t\t\t\t\t\t\t\t\t\tEnter the number of the expense: ");
            while (scanf("%d", &expenseChoice) != 1 || expenseChoice < 1 || expenseChoice > needsCount + wantsCount + 1) {
                printf("\t\t\t\t\t\t\t\t\t\t\tInvalid choice. Please select a valid number: ");
                while (getchar() != '\n');
            }
            if (expenseChoice == needsCount + wantsCount + 1) {
                return; //Go back to previous menu
            }

        } while (expenseChoice == needsCount + wantsCount + 1);


        //Source of Expense with Back Option
        do {
            if (expenseChoice <= needsCount) {
                if (strcmp(needs[expenseChoice - 1], "Others (Please specify)") == 0) {
                    printf("\t\t\t\t\t\t\t\t\t\t\tSpecify other need: ");
                    scanf(" %[^\n]", otherSource);
                    strcpy(expenseSources[*expenseCount], otherSource);
                } else {
                    strcpy(expenseSources[*expenseCount], needs[expenseChoice - 1]);
                }
            } else {
                if (strcmp(wants[expenseChoice - needsCount - 1], "Others (Please specify)") == 0) {
                    printf("\t\t\t\t\t\t\t\t\t\t\tSpecify other want: ");
                    scanf(" %[^\n]", otherSource);
                    strcpy(expenseSources[*expenseCount], otherSource);
                } else {
                    strcpy(expenseSources[*expenseCount], wants[expenseChoice - needsCount - 1]);
                }
            }

        } while (backOption == 1);


        do {
            printf("\t\t\t\t\t\t\t\t\t\t\tEnter expense amount: ");
            while (scanf("%f", &amount) != 1) {
                printf("\t\t\t\t\t\t\t\t\t\t\tInvalid input. Please enter a valid number: ");
                while (getchar() != '\n');
            }
            if (amount < 0) {
                printf("\t\t\t\t\t\t\t\t\t\t\tExpense can't be negative. Please select a valid number: ");
            }
        } while (amount < 0);

        strcpy(expenseFrequency[*expenseCount], selectedFrequency);

        if (strcmp(selectedFrequency, "Daily") == 0) {
            amount *= 30;
        } else if (strcmp(selectedFrequency, "Weekly") == 0) {
            amount *= 4;
        }

        strcpy(expenseType[*expenseCount], (expenseChoice <= needsCount) ? "Need" : "Want");

        expenseAmounts[*expenseCount] = amount;

        time_t t = time(NULL);
        struct tm tm = *localtime(&t);
        snprintf(expenseDateTime[*expenseCount], 20, "%02d-%02d-%04d %02d:%02d",
                 tm.tm_mday, tm.tm_mon + 1, tm.tm_year + 1900, tm.tm_hour, tm.tm_min);

        (*expenseCount)++;

        writeUserData(username, amount, expenseSources[*expenseCount - 1], expenseFrequency[*expenseCount - 1], expenseType[*expenseCount - 1], expenseDateTime[*expenseCount - 1]);

        printf("\n\t\t\t\t\t\t\t\t\t\t\tWhat would you like to do next?\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[1] Add another expense\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[2] Back to previous menu\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[3] Exit\n");
        printf("\t\t\t\t\t\t\t\t\t\t\tEnter your choice: ");
        while (scanf("%d", &continueOption) != 1 || (continueOption < 1 || continueOption > 3)) {
            printf("\t\t\t\t\t\t\t\t\t\t\tInvalid choice. Please select 1, 2, or 3: ");
            while (getchar() != '\n');
        }

        if (continueOption == 2) {
            system("cls");
            break;
        } else if (continueOption == 3) {
             system("cls");
            return;
        }
        system("cls");
    }
}

// Function to display and handle the suggestions menu
void getSuggestions(char userType[]) {
    int suggestionChoice;
    int userTypeChoice;

    do {
        printf("\n\t\t\t\t\t\t\t\t\t\t\t--- Get Suggestions ---\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[1] Fun Challenges\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[2] Return to main menu\n");
        printf("\t\t\t\t\t\t\t\t\t\t\tEnter your choice: ");
        scanf("%d", &suggestionChoice);
        system("cls");

        switch (suggestionChoice) {
            case 1:
                funChallenges();
                break;
            case 2:
                return;
            default:
                printf("\t\t\t\t\t\t\t\t\t\t\tInvalid choice! Please select a valid option.\n");
        }
    } while (1);
}


// Function for Fun Challenges menu
void funChallenges() {
    int challengeOption;

    while (1) {
        printf("\n\t\t\t\t\t\t\t\t\t\t\t=== Fun Challenges for Budgeting ===\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[1] Small Coin Savings Challenge\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[2] Bring Your Own Water Bottle Challenge\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[3] Back to Get Suggestions Menu\n");
        printf("\t\t\t\t\t\t\t\t\t\t\tEnter your choice: ");
        scanf("%d", &challengeOption);

        switch (challengeOption) {
            case 1:
                coinSavingsChallenge();
                break;
            case 2:
                bringYourOwnWaterBottleChallenge();
                break;
            case 3:
                return; // Go back to Get Suggestions Menu
            default:
                printf("\t\t\t\t\t\t\t\t\t\t\tInvalid choice! Please try again.\n");
        }
    }
}

// Function for Coin Savings Challenge
void coinSavingsChallenge() {
    int option;
    float coinSavings = 0.0;

    printf("\n\t\t\t\t\t\t\t\t\t\t\t=== Small Coin Savings Challenge ===\n");
    printf("\t\t\t\t\t\t\t\t\t\t\tSave your loose change daily. Keep your coins in a jar and watch your savings grow!\n");

    while (1) {
        printf("\n\t\t\t\t\t\t\t\t\t\t\t[1] Add Coin\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[2] View Coin Savings\n");
        printf("\t\t\t\t\t\t\t\t\t\t\t[3] Back to Fun Challenges Menu\n");
        printf("\t\t\t\t\t\t\t\t\t\t\tEnter your option: ");
        scanf("%d", &option);

        switch (option) {
            case 1: {
                float coinAmount;
                printf("\t\t\t\t\t\t\t\t\t\t\tEnter the amount of coin to add (in Php): ");
                scanf("%f", &coinAmount);
                coinSavings += coinAmount;
                printf("\t\t\t\t\t\t\t\t\t\t\tYou added Php %.2f to your savings. Keep it up!\n", coinAmount);
                break;
            }
            case 2:
                if (coinSavings == 0.0) {
                    printf("\t\t\t\t\t\t\t\t\t\t\tYour coin savings are empty! Start adding coins.\n");
                } else {
                    printf("\t\t\t\t\t\t\t\t\t\t\tYour total coin savings are: Php %.2f\n", coinSavings);
                }
                break;
            case 3:
                return; // Exit the challenge
            default:
                printf("\t\t\t\t\t\t\t\t\t\t\tInvalid option! Please try again.\n");
        }
    }
}

// Function for Bring Your Own Water Bottle Challenge
void bringYourOwnWaterBottleChallenge() {
    int option, waterBottlesUsed = 0;
    float bottleCost;

    printf("\n\t=== Bring Your Own Water Bottle Challenge ===\n");
    printf("\tSave money and the environment by bringing your own water bottle!\n");

    // Ask for the cost of a bottled water
    printf("\tHow much does a bottled water cost in your area (Php)? ");
    scanf("%f", &bottleCost);

    if (bottleCost <= 0) {
        printf("\tInvalid cost entered. Please start the challenge again with a valid amount.\n");
        return;
    }

    while (1) {
        printf("\n\t[1] Add Water Bottle Used\n");
        printf("\t[2] View Savings from Bringing Water Bottle\n");
        printf("\t[3] View Weekly, Monthly, and Yearly Comparisons\n");
        printf("\t[4] Back to Fun Challenges Menu\n");
        printf("\tEnter your option: ");
        scanf("%d", &option);

        switch (option) {
            case 1:
                waterBottlesUsed++;
                printf("\tYou used your water bottle instead of buying bottled water. Great job!\n");
                break;
            case 2: {
                float totalSavings = waterBottlesUsed * bottleCost;
                printf("\tYou saved Php %.2f by using your water bottle %d time(s)!\n", totalSavings, waterBottlesUsed);
                break;
            }
            case 3: {
                // Calculate weekly, monthly, and yearly costs of buying bottled water
                int weeklyBottles = 7; // Assuming 1 bottle per day for 7 days
                int monthlyBottles = 30; // Approximate monthly usage
                int yearlyBottles = 365; // Daily usage for a year

                float weeklyCost = weeklyBottles * bottleCost;
                float monthlyCost = monthlyBottles * bottleCost;
                float yearlyCost = yearlyBottles * bottleCost;

                printf("\n\t=== Comparison of Costs ===\n");
                printf("\tIf you bought bottled water instead of bringing your own:\n");
                printf("\tWeekly Cost: Php %.2f\n", weeklyCost);
                printf("\tMonthly Cost: Php %.2f\n", monthlyCost);
                printf("\tYearly Cost: Php %.2f\n", yearlyCost);

                float totalSavings = waterBottlesUsed * bottleCost;
                printf("\n\tBy bringing your own water bottle:\n");
                printf("\tTotal Saved So Far: Php %.2f\n", totalSavings);
                break;
            }
            case 4:
                printf("\tExiting the Bring Your Own Water Bottle Challenge. Keep saving!\n");
                return; // Exit the challenge
            default:
                printf("\tInvalid option! Please try again.\n");
        }
    }
}

//Function for View History
void viewHistory(float incomes[], char incomeFrequency[][10], char incomeDateTime[][20], int incomeCount, char incomeSources[][50], char expenseNames[][50], float expenseAmounts[], char expenseFrequency[][10], char expenseType[][10], char expenseDateTime[][20], int expenseCount, const char *username) {
    int i;
    char filename[50];
    sprintf(filename, "%s.txt", username);
    FILE *userFile = fopen(filename, "r");

    if (userFile == NULL) {
        printf("\t\t\t\t\t\t\t\t\t\t\tNo data found for this user.\n");
        return;
    }
printf("\n\t\t\t\t\t\t\t\t\t\t\t--- Transaction History for %s ---\n", username);

    // Display income details
    if (incomeCount > 0) {
        printf("\n\t\t\t\t\t\t\t\t\t\t\t--- Incomes ---\n");
        for (int i = 0; i < incomeCount; i++) {
            printf("\t\t\t\t\t\t\t\t\t\t\tIncome #%d:\n", i + 1);
            printf("\t\t\t\t\t\t\t\t\t\t\tSource         : %s\n", incomeSources[i]);
            printf("\t\t\t\t\t\t\t\t\t\t\tAmount         : %.2f\n", incomes[i]);
            printf("\t\t\t\t\t\t\t\t\t\t\tFrequency      : %s\n", incomeFrequency[i]);
            printf("\t\t\t\t\t\t\t\t\t\t\tDate and Time  : %s\n", incomeDateTime[i]);
        }
    } else {
        printf("\t\t\t\t\t\t\t\t\t\t\tNo income records found.\n");
    }

    // Display expense details
    if (expenseCount > 0) {
        printf("\n\t\t\t\t\t\t\t\t\t\t\t--- Expenses ---\n");
        for (int i = 0; i < expenseCount; i++) {
            printf("\t\t\t\t\t\t\t\t\t\t\tExpense #%d:\n", i + 1);
            printf("\t\t\t\t\t\t\t\t\t\t\tName           : %s\n", expenseNames[i]);
            printf("\t\t\t\t\t\t\t\t\t\t\tAmount         : %.2f\n", expenseAmounts[i]);
            printf("\t\t\t\t\t\t\t\t\t\t\tFrequency      : %s\n", expenseFrequency[i]);
            printf("\t\t\t\t\t\t\t\t\t\t\tType           : %s\n", expenseType[i]);
            printf("\t\t\t\t\t\t\t\t\t\t\tDate and Time  : %s\n", expenseDateTime[i]);
        }
    } else {
        printf("\t\t\t\t\t\t\t\t\t\t\tNo expense records found.\n");
    }

    fclose(userFile);
    printf("\n");
}
void viewSummary(float incomes[], int incomeCount, char expenseType[][10], float expenseAmounts[], int expenseCount, const char *username) {
    float totalIncome = 0, totalNeeds = 0, totalWants = 0, totalExpenses = 0;
    char filename[50];
    sprintf(filename, "%s.txt", username);
    FILE *userFile = fopen(filename, "r");

    if (userFile == NULL) {
        printf("\t\t\t\t\t\t\t\t\t\t\tNo data found for this user.\n");
        return;
    }

    char line[100], source[50], frequency[20], type[20], dateTime[20];
    float amount;

    while (fscanf(userFile, "%f,%[^,],%[^,],%[^,],%[^,\n]", &amount, source, frequency, type, dateTime) == 5) {
        totalExpenses += amount;
        if (strcasecmp(type, "Need") == 0) {
            totalNeeds += amount;
        } else if (strcasecmp(type, "Want") == 0) {
            totalWants += amount;
        }
    }

    fclose(userFile);

    for (int i = 0; i < incomeCount; i++) {
        totalIncome += incomes[i];
    }

    for (int i = 0; i < expenseCount; i++) {
        totalExpenses += expenseAmounts[i];
        if (strcasecmp(expenseType[i], "Need") == 0) {
            totalNeeds += expenseAmounts[i];
        } else if (strcasecmp(expenseType[i], "Want") == 0) {
            totalWants += expenseAmounts[i];
        }
    }

    printf("\n\t\t\t\t\t\t\t\t\t\t\t--- Summary for %s ---\n", username);
    printf("\t\t\t\t\t\t\t\t\t\t\tTotal Income: %.2f\n", totalIncome);
    printf("\t\t\t\t\t\t\t\t\t\t\tTotal Expenses: %.2f\n", totalExpenses);
    printf("\t\t\t\t\t\t\t\t\t\t\tSavings: %.2f\n", totalIncome - totalExpenses);

    if (totalExpenses > 0) {
        printf("\t\t\t\t\t\t\t\t\t\t\tNeeds: %.2f%%\n", (totalNeeds / totalExpenses) * 100);
        printf("\t\t\t\t\t\t\t\t\t\t\tWants: %.2f%%\n", (totalWants / totalExpenses) * 100);
    }

    // Personalized Budgeting Section
    printf("\n\t\t\t\t\t\t\t\t\t\t\tWould you like to personalize your budget?\n");
    printf("\t\t\t\t\t\t\t\t\t\t\t[1] Use 50%% Needs / 30%% Wants / 20%% Savings\n");
    printf("\t\t\t\t\t\t\t\t\t\t\t[2] Use 60%% Needs / 20%% Wants / 20%% Savings\n");
    printf("\t\t\t\t\t\t\t\t\t\t\t[3] Use 40%% Needs / 40%% Wants / 20%% Savings\n");
    printf("\t\t\t\t\t\t\t\t\t\t\t[4] Customize percentages\n");
    printf("\t\t\t\t\t\t\t\t\t\t\t[5] Skip personalization\n");
    printf("\t\t\t\t\t\t\t\t\t\t\tEnter your choice: ");
    int choice;
    scanf("%d", &choice);

    float needsPercentage = 0, wantsPercentage = 0, savingsPercentage = 0;

    switch (choice) {
        case 1:
            needsPercentage = 50;
            wantsPercentage = 30;
            savingsPercentage = 20;
            break;
        case 2:
            needsPercentage = 60;
            wantsPercentage = 20;
            savingsPercentage = 20;
            break;
        case 3:
            needsPercentage = 40;
            wantsPercentage = 40;
            savingsPercentage = 20;
            break;
        case 4:
            printf("\t\t\t\t\t\t\t\t\t\t\tEnter percentages for Needs, Wants, and Savings (e.g., 50 30 20): ");
            scanf("%f %f %f", &needsPercentage, &wantsPercentage, &savingsPercentage);
            if (needsPercentage + wantsPercentage + savingsPercentage != 100) {
                printf("\t\t\t\t\t\t\t\t\t\t\tInvalid percentages! They must add up to 100%%.\n");
                return;
            }
            break;
        case 5:
            printf("\t\t\t\t\t\t\t\t\t\t\tSkipping personalization.\n");
            return;
        default:
            printf("\t\t\t\t\t\t\t\t\t\t\tInvalid choice.\n");
            return;
    }

    printf("\n\t\t\t\t\t\t\t\t\t\t\t--- Recommended Budget Allocation ---\n");
    printf("\t\t\t\t\t\t\t\t\t\t\tNeeds: %.2f\n", (totalIncome * needsPercentage) / 100);
    printf("\t\t\t\t\t\t\t\t\t\t\tWants: %.2f\n", (totalIncome * wantsPercentage) / 100);
    printf("\t\t\t\t\t\t\t\t\t\t\tSavings: %.2f\n", (totalIncome * savingsPercentage) / 100);

    if (totalNeeds > (totalIncome * needsPercentage) / 100) {
        printf("\n\t\t\t\t\t\t\t\t\t\t\tTip: You're spending more on Needs than planned!\n");
    }
    if (totalWants > (totalIncome * wantsPercentage) / 100) {
        printf("\n\t\t\t\t\t\t\t\t\t\t\tTip: Consider reducing your spending on Wants.\n");
    }
    if ((totalIncome - totalExpenses) < (totalIncome * savingsPercentage) / 100) {
        printf("\n\t\t\t\t\t\t\t\t\t\t\tTip: Try to save more to meet your Savings goal.\n");
    }
}


//Function for Writing User Data
void writeUserData(const char *username, float amount, const char *source, const char *frequency, const char *type, const char *dateTime) {
    char filename[50];
    sprintf(filename, "%s.txt", username);
    FILE *userFile = fopen(filename, "a");

    if (userFile == NULL) {
        printf("\t\t\t\t\t\t\t\t\t\t\tError opening user file.\n");
        return;
    }

    fprintf(userFile, "%.2f,%s,%s,%s,%s\n", amount, source, frequency, type, dateTime);
    fclose(userFile);
}
