#include <stdio.h>

int main() {
    float sum = 0.0;
    float current_number;
    int count = 0;
    char choice;

    printf("--- Average Calculator ---\n");

    do {
        // 1. Get the number from the user
        printf("Enter a number: ");
        scanf("%f", &current_number);

        // 2. Add to the running total and increase the count
        sum += current_number;
        count++;

        // 3. Ask if they want to continue
        printf("Do you want to add another number? (y/n): ");
        // The space before %c is very important! 
        scanf(" %c", &choice); 

    } while (choice == 'y' || choice == 'Y'); // Keep looping if they type y or Y

    // 4. Calculate and display the final result
    if (count > 0) {
        float average = sum / count;
        printf("\nResults:\n");
        printf("You entered %d numbers.\n", count);
        printf("The average is: %.2f\n", average);
    } else {
        // Just in case count is 0 (though the do-while loop makes this impossible here, 
        // it's good practice to prevent dividing by zero).
        printf("\nNo numbers were entered.\n");
    }

    return 0;
}
