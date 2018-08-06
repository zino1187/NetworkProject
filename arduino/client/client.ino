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
    wifi ê³µìœ ê¸? ?„¤?Š¸?›Œ?¬ëª? / ë¹„ë?ë²ˆí˜¸
    --------------------------------------------*/
    WiFi.mode(WIFI_STA);
    WiFiMulti.addAP(AP_NAME, AP_PASSWORD);

    Serial.println();
    Serial.println();
    Serial.print("WiFi ? ‘?†?‹œ?„ ì¤‘ì…?‹ˆ?‹¤... ");

    /*--------------------------------------------
    wifi ?— ? ‘?†?  ?•Œê¹Œì? ë¬´í•œ ?—°ê²°ì„ ?‹œ?„?•œ?‹¤  
    --------------------------------------------*/
    while(WiFiMulti.run() != WL_CONNECTED) {
        Serial.print(".");
        delay(500);
    }

    /*--------------------------------------------
    wifi ?— ?—°ê²°ë˜ë©? ê³µìœ ê¸°ë¡œ ë¶??„° ?• ?‹¹ë°›ì? ipë¥? ì¶œë ¥?•œ?‹¤
    --------------------------------------------*/
    Serial.println("");
    Serial.println("WiFi connected");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());

    delay(500);

    /*--------------------------------------------
    wifi ?— ?—°ê²°ë˜?—ˆ?œ¼ë©?, ?´? œ ?†Œì¼? ?„œë²„ì— ? ‘?†?„ ?‹œ?„?•œ?‹¤
    --------------------------------------------*/
    const uint16_t port = SERVER_PORT;
    const char * host = SERVER_IP; // ip or dns
    
    Serial.print("connecting to ");
    Serial.println(host);

    Serial.println("?†Œì¼“ì„œë²„ì— ? ‘?†?„ ?‹œ?„?•˜ê³? ?ˆ?Šµ?‹ˆ?‹¤");
    while(!client.connect(host, port)){
        Serial.println(".");
        delay(1000);
    }
    Serial.println("?†Œì¼“ì„œë²„ì— ? ‘?†?•˜???Šµ?‹ˆ?‹¤");    
    delay(1000);
}

void loop() {
    ESP.wdtFeed();
    
    /*--------------------------------------------
     ?„œë²„ë¡œë¶??„° ?•œì¤? ?½?–´?˜¤ê¸?(read back one line from server)
    --------------------------------------------*/
    String msg = client.readStringUntil('\n');
    
    //Serial.println("?„œë²„ì—?„œ ë³´ë‚´?˜¨ ë©”ì„¸ì§??Š”");    
    Serial.println(msg);

    //D5?— ? „ê¸? ì¶œë ¥?•˜ê¸?
    if(msg.equals("on")){
      digitalWrite(D5 , HIGH);      
      Serial.println("LED ì¼??‹ˆ?‹¤");
    }else if(msg.equals("off")){
      digitalWrite(D5 , LOW);
      Serial.println("LED ?•?‹ˆ?‹¤");
    }
    
    
    //Serial.println("closing connection");
    //client.stop();
    //Serial.println("wait 5 sec...");
    delay(10);
}
