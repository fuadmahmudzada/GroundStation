//package org.example.groundstationdemo;
//import javafx.concurrent.Worker;
//import javafx.scene.web.WebEngine;
//import netscape.javascript.JSObject;
//
//import javax.script.ScriptEngine;
//import javax.script.ScriptEngineManager;
//import javax.script.ScriptException;
//
//public class MapUtil {
//    private ScriptEngine engine;
//    private WebViewBridge bridge;
//
//    public MapUtil(WebEngine webEngine) {
//        engine = new ScriptEngineManager().getEngineByName("nashorn");
//        bridge = new WebViewBridge(webEngine);
//    }
//
//    private double[] generateRandomCoordinates() {
//        double latitude = Math.random() * 180 - 90; // Random latitude between -90 and 90
//        double longitude = Math.random() * 360 - 180; // Random longitude between -180 and 180
//        return new double[]{latitude, longitude};
//    }
//
//}