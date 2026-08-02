#include <stdio.h>
// to prevent the input to be a letter or a symbol, this function will keep asking for a number until a valid number is entered
int getNumber() {
    int num;
    char ch;

    while (1) {
        if (scanf("%d%c", &num, &ch) == 2 && ch == '\n') {
            return num;
        }

        printf("Please enter a valid number: ");

        while (getchar() != '\n');
    }
}
// end of the number rule function

int main() {
  int num1;
  int num2;
  char Operator;
//this is where the operation is selected
  printf("=== 2 DIGITS CALCULATOR === \nENTER A OPERATOR \n\nDivision: d \nmultiplication: m \nAddition: a \nSubtraction: s \n\n enter here = ");
  scanf("%c", &Operator);

//this is if the input is indicating division
  if (Operator == 'd' || Operator == 'D'){
    printf("You picked Division, enter first number: ");
    num1 = getNumber();

    printf("Enter second number: ");
    num2 = getNumber();
  

    if (num2 == 0) {
      printf("Error: Division by zero is not allowed.\n");
    } else {
      printf("The result of %d divided by %d is %d \n", num1, num2, num1 / num2);
    }
  }
  //end of the division part

  //this is if the input is indicating multiplication
  if (Operator == 'm' || Operator == 'M'){
    printf("You picked Multiplication, enter first number: ");
    num1 = getNumber();

    printf("Enter second number: ");
    num2 = getNumber();

    printf("The result of %d multiplied by %d is %d \n", num1, num2, num1 * num2);
  } 
  //end of the multiplication part

  // this is if the input is indicating addition
  if (Operator == 'a' || Operator == 'A'){
    printf("You picked Addition, enter first number: ");
    num1 = getNumber();

    printf("Enter second number: ");
    num2 = getNumber();

    printf("The result of %d plus %d is %d \n", num1, num2, num1 + num2);
  }
  //end of the addition part

  // this is if the input is indicating subtraction
  if (Operator == 's' || Operator == 'S'){
    printf("You picked Subtraction, enter first number: ");
    num1 = getNumber();

    printf("Enter second number: ");
    num2 = getNumber();

    printf("The result of %d minus %d is %d \n", num1, num2, num1 - num2);
  }
  // end of the subtraction part


  //this is if the operator input is invalid
  if (Operator != 'd' && Operator != 'D' && Operator != 'm' && Operator != 'M' && Operator != 'a' && Operator != 'A' && Operator != 's' && Operator != 'S') {
    printf("Error: Invalid operator entered. Please use d, m, a, or s.\n for division, multiplication, addition, or subtraction respectively.\n");
  } 


  }
