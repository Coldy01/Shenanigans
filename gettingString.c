#include <stdio.h>
#include <string.h> // use this library
int main(){
  char _string[40];
 // instead of : scanf("%c", &_String)
  fgets(_string, sizeof(_string), stdin); // use this function (also use "sizeof" to calculate the length of the string)
  printf("%s", _string); //output
}
