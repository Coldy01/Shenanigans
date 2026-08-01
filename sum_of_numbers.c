#include <stdio.h>
int main(){
  int num1;
  int num2;
  //this will hold the input's value
  printf("enter the first number: \n");
  scanf("%d", &num1);//"&" will store the value to the variable, "%d" will determine the value to be inputed

  printf("Enter the second number: \n");
  scanf("%d", &num2);
  
  printf("The sum of the first number and second number is: %d \n", num1 + num2);
  return 0;
}
