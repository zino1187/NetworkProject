#include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>

#define AP_NAME "Woman_AP9F1"
#define AP_PASSWORD "abcd123456"
#define SERVER_IP "192.168.0.54"
#define SERVER_PORT 7777

ESP8266WiFiMulti WiFiMulti;
WiFiClient client;// Use WiFiClient class to create TCP connections
    
void setup() {
    ESP.wdtDisable();
    ESP.wdtEnable(WDTO_8S);
    pinMode(D5, OUTPUT);   
    
    Serial.begin(115200);
    delay(10);

    // We start by connecting to a WiFi network

    /*--------------------------------------------
    wifi 공유�? ?��?��?��?���? / 비�?번호
    --------------------------------------------*/
    WiFi.mode(WIFI_STA);
    WiFiMulti.addAP(AP_NAME, AP_PASSWORD);

    Serial.println();
    Serial.println();
    Serial.print("WiFi ?��?��?��?�� 중입?��?��... ");

    /*--------------------------------------------
    wifi ?�� ?��?��?�� ?��까�? 무한 ?��결을 ?��?��?��?��  
    --------------------------------------------*/
    while(WiFiMulti.run() != WL_CONNECTED) {
        Serial.print(".");
        delay(500);
    }

    /*--------------------------------------------
    wifi ?�� ?��결되�? 공유기로 �??�� ?��?��받�? ip�? 출력?��?��
    --------------------------------------------*/
    Serial.println("");
    Serial.println("WiFi connected");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());

    delay(500);

    /*--------------------------------------------
    wifi ?�� ?��결되?��?���?, ?��?�� ?���? ?��버에 ?��?��?�� ?��?��?��?��
    --------------------------------------------*/
    const uint16_t port = SERVER_PORT;
    const char * host = SERVER_IP; // ip or dns
    
    Serial.print("connecting to ");
    Serial.println(host);

    Serial.println("?��켓서버에 ?��?��?�� ?��?��?���? ?��?��?��?��");
    while(!client.connect(host, port)){
        Serial.println(".");
        delay(1000);
    }
    Serial.println("?��켓서버에 ?��?��?��???��?��?��");    
    delay(1000);
}

void loop() {
    ESP.wdtFeed();
    
    /*--------------------------------------------
     ?��버로�??�� ?���? ?��?��?���?(read back one line from server)
    --------------------------------------------*/
    String msg = client.readStringUntil('\n');
    
    //Serial.println("?��버에?�� 보내?�� 메세�??��");    
    Serial.println(msg);

    //D5?�� ?���? 출력?���?
    if(msg.equals("on")){
      digitalWrite(D5 , HIGH);      
      Serial.println("LED �??��?��");
    }else if(msg.equals("off")){
      digitalWrite(D5 , LOW);
      Serial.println("LED ?��?��?��");
    }
    
    
    //Serial.println("closing connection");
    //client.stop();
    //Serial.println("wait 5 sec...");
    delay(10);
}
