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
    wifi κ³΅μ κΈ? ?€?Έ??¬λͺ? / λΉλ?λ²νΈ
    --------------------------------------------*/
    WiFi.mode(WIFI_STA);
    WiFiMulti.addAP(AP_NAME, AP_PASSWORD);

    Serial.println();
    Serial.println();
    Serial.print("WiFi ? ??? μ€μ??€... ");

    /*--------------------------------------------
    wifi ? ? ??  ?κΉμ? λ¬΄ν ?°κ²°μ ????€  
    --------------------------------------------*/
    while(WiFiMulti.run() != WL_CONNECTED) {
        Serial.print(".");
        delay(500);
    }

    /*--------------------------------------------
    wifi ? ?°κ²°λλ©? κ³΅μ κΈ°λ‘ λΆ??° ? ?Ήλ°μ? ipλ₯? μΆλ ₯??€
    --------------------------------------------*/
    Serial.println("");
    Serial.println("WiFi connected");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());

    delay(500);

    /*--------------------------------------------
    wifi ? ?°κ²°λ??Όλ©?, ?΄?  ?μΌ? ?λ²μ ? ?? ????€
    --------------------------------------------*/
    const uint16_t port = SERVER_PORT;
    const char * host = SERVER_IP; // ip or dns
    
    Serial.print("connecting to ");
    Serial.println(host);

    Serial.println("?μΌμλ²μ ? ?? ???κ³? ??΅??€");
    while(!client.connect(host, port)){
        Serial.println(".");
        delay(1000);
    }
    Serial.println("?μΌμλ²μ ? ?????΅??€");    
    delay(1000);
}

void loop() {
    ESP.wdtFeed();
    
    /*--------------------------------------------
     ?λ²λ‘λΆ??° ?μ€? ?½?΄?€κΈ?(read back one line from server)
    --------------------------------------------*/
    String msg = client.readStringUntil('\n');
    
    //Serial.println("?λ²μ? λ³΄λ΄?¨ λ©μΈμ§??");    
    Serial.println(msg);

    //D5? ? κΈ? μΆλ ₯?κΈ?
    if(msg.equals("on")){
      digitalWrite(D5 , HIGH);      
      Serial.println("LED μΌ???€");
    }else if(msg.equals("off")){
      digitalWrite(D5 , LOW);
      Serial.println("LED ???€");
    }
    
    
    //Serial.println("closing connection");
    //client.stop();
    //Serial.println("wait 5 sec...");
    delay(10);
}
