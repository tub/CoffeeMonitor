// 0000 0001
int brewing = 0x0001;
// 0000 0010
int hotplate = 0x0002;

int brewPin = 3;
int hotplatePin = 4;


//delay time
int delayTime = 500; //half sec
int prev = 0;

void setup() {
  Serial.begin(9600); 
}

void loop() {
  sendStatus();
  delay(delayTime);
}


void sendStatus(){
  int out = digitalRead(brewPin) ? brewing : 0;
  out |= digitalRead(hotplatePin) ? hotplate : 0;
  if(out != prev){
    Serial.println(out);
    prev = out;
  }
}
