#include <stdio.h>
#include <string.h>
int main() {
    
    //variables
    char name[50];
    float prelim, midterm, final;
    
    //input
    printf("Enter student name: ");
    fgets(name, sizeof(name), stdin);
    printf("Enter preliminary grade: ");
    scanf("%f", &prelim);
    printf("Enter midterm grade: ");
    scanf("%f", &midterm);
    printf("Enter final grade: ");
    scanf("%f", &final);

    //compute
    float total = prelim + midterm + final;
    float average = total / 3.0;
    
    //output
    printf("===== GRADE REPORT =====\n");
    printf("Student Name: %s", name);
    printf("Preliminary Grade: %.2f\n", prelim);
    printf("Midterm Grade: %.2f\n", midterm);
    printf("Final Grade: %.2f\n", final);
    printf("Final Average: %.2f\n", average);
    printf("================================\n");
    return 0;
}
