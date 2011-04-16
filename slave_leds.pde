/* Copyright (C) 2011 by Mosalam Ebrahimi <m.ebrahimi@ieee.org>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
*/

const int baud = 9600;

// number of LEDs
const int num_leds = 3;

// LEDs connected to digital pins 3, 5, and 6
const int pins[num_leds] = {3,5,6};

// current values
int values[num_leds];
  
int selected_led = 0;

int temp = 0;

// parse received command strings
// to select an LED, [number],
// to set the value, [value];
// e.g., "1,250;" selects the second LED (zero-based indexing) and
// sets the value to 250
int processIntString(int& val)
{
  int readVal = Serial.read();
  
  if ((readVal >= '0') &&
      (readVal <= '9')) {
    val *= 10;
    val += readVal - '0';
    return 1;
  } else if (readVal == ';') {
    return 2;
  } else if (readVal == ',') {
    return 3;
  }
}

void setup()  { 
  Serial.begin(baud);
  for (int i=0; i<num_leds; i++) {
    values[i] = 0;
  }
} 

void loop()  {
  if (Serial.available()) {
    int res = processIntString(temp);
    if (res == 2) {
      values[selected_led] = temp;
      temp = 0;
    } else if (res == 3) {
      selected_led = temp;
      temp = 0;
    }
  }

  for (int i=0; i<num_leds; i++) {
    analogWrite(pins[i], values[i]);         
  }
}
