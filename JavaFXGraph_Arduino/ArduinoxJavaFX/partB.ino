#include <Arduino.h>
#include <U8x8lib.h>

auto display = U8X8_SSD1306_128X64_NONAME_HW_I2C(U8X8_PIN_NONE);

/* -----setup ---------*/
void setup() {
  Serial.begin(9600);
  
  display.begin();
  display.setFlipMode(1);
  display.clearDisplay();
  display.setFont(u8x8_font_7x14B_1x2_r);
}

/* --------Pot read function. ----------* 
Read the potentiometer value (or any other analogue sensor on A0)* 
Break up the two bytes of data from A0 into "high" and "low"* Move those two bytes into an array of four bytes.* 
First two bytes are zero. Then high byte.  Then low byte.** Once the four bytes are done, send a "newline".** 
Why bytes? Because the Java program is interpreting individual* bytes of data.  
The Java program waits for the newline to signal* that a particular round of data upload is done. */

void sendPotentiometerData() {
  const auto value = analogRead(A0);
  const byte data[] = {0, 0, highByte(value), lowByte(value)};
  
  Serial.write(data, 4);  // send 0,0, "high byte", "low byte"
  Serial.println();       // send "newline"
}
 
 // read A0 and display on OLED
 void displayA0() {
   const auto value = analogRead(A0);
   const byte data[] = {0, 0, highByte(value), lowByte(value)};
   
   display.setCursor(0,0); display.print("A0: ");
   display.setCursor(5,0); display.print(value);
   display.setCursor(0,2); display.print("Bytes 1,2: " + String(data[0]) + "," + String(data[1]));
   display.setCursor(0,4); display.print("Byte 3: " + String(data[2]));
   display.setCursor(0,6); display.print("Byte 4: " + String(data[3]));
}
 
 /* ----loop --------*/
 void loop() {
  displayA0();                // show on OLED
  sendPotentiometerData();    // write to serial port
  delay(1000);      // wait so you don't saturate 
                    // the serial line and block 
                    // potential reloads of Arduino code
}
