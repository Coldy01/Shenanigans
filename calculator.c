#include <stdio.h>
int main() {
  int num1;
  int num2;
  char Operator;
//this is where the operation is selected
  printf("=== 2 DIGITS CALCULATOR === \nENTER A OPERATOR \n\nDivision: d \nmultiplication: m \nAddition: a \nSubtraction: s \n\n enter here = ");
  scanf("%c", &Operator);

//this is if the input is indicating division
  if (Operator == 'd' || Operator == 'D'){
    printf("You picked Division, enter first number: ", num1);
    scanf("%d", &num1);

    printf("Enter second number: ", num2);
    scanf("%d", &num2);

    if (num2 == 0) {
      printf("Error: Division by zero is not allowed.\n");
    } else {
      printf("The result of %d divided by %d is %d \n", num1, num2, num1 / num2);
    }
  } else {
    printf("please enter the right operation");
  }
  //end of the division part

  //this is if the input is indicating multiplication
  if (Operator == 'm' || Operator == 'M'){
    printf("You picked Multiplication, enter first number: ", num1);
    scanf("%d", &num1);

    printf("Enter second number: ", num2);
    scanf("%d", &num2);

    if (num2 == 0) {
      printf("Error: Division by zero is not allowed.\n");
    } else {
      printf("The result of %d multiplied by %d is %d \n", num1, num2, num1 * num2);
    }
  } else {
    printf("please enter the right operation");
  }
  //end of the multiplication part
}
