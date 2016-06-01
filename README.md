# SCAI AndroidClient

##SCAI (Computerized System for Smart Trailers) Project
This project is part of a computer engineering thesis.
The whole SCAI system consists of: 
  - A main computer (RPI2) located in the truck's trailer, which gathers data from the sensors, processes, and sends it wirelessly to a display device located in the driver's cabin.
  - Several sensors an peripherals, such as: IP Camera, GPS Module, Inclinometer, Gyroscope, Magnetometer, Termometer and Barometer.
  - A display device, which is a 10' android tablet, which runs the app located in this repository and that it is in charge of displaying the "rearview" video stream, an embedded map (OSMdroid) and the data from the sensors gathered by the RPI2.
  
##Android Client for SCAI  project. 
As mentioned above, SCAI Android client is an app that runs on a tablet located in the truck's driver cabin. It is in charge of displaying all of the information gathered by the rest of the system in order to contribute to the road safety.
###Features:
  - Map view:
    - OSMDroid offline map viewer.
    - TO BE DONE: Add functionalities to this map view, such as synchronization with the current GPS coordenates, among others.
  - Video view: 
    - RTSP player, which plays video signal streamed by the IP Camera.
    - TO BE DONE: Find a workaround for the video player, in order to be able to modify the buffer size and decrease the streaming latency.
  - Sensor status views:
    - Tipper inclination (Graphic & Numeric).
    - Side inclination (Graphic & Numeric).
    - Compass (Graphic).
    - Sensor-group status (Graphic).
    - Speed (Numeric).
    - Altitude (Numeric).
    - Date and Time (Numeric).

###How to Run
1. Download and import project.
2. Simulate the RPI web service
    - As you will not have a RPI and the sensors running, you should somehow mock the web service in order to view the results and animations on the GUI.
    - The response must be an XML over HTTP with the following form:
      Response>
        <altitude>187</altitude>
        <tipperInclination>33</tipperInclination>
        <sideInclination>15</sideInclination>
        <speed>8</speed>
        <positionX>15</positionX>
        <positionY>11</positionY>
        <compass>5092</compass>
        <temperature>4</temperature>
      </Response>
    - Finally, do not forget to modify the *SENSOR_QUERY* url string in order to point to your HTTP service.
3. Set up the maps:
  - As I have created my own offline maps using MOBAC (Mobile Atlas Creator), and the MAP files are too large to upload to this repo, in order to try this app, you should change the configuration to download the maps online by request on runtime. To do this, change the line *map.setUseDataConnection(false)* to true in MainScreenActivity class.
4. Set up the video stream:
  - You wil also not have an IP camera streaming RTSP video, so you should grab an online stream, by modifying the  *VIDEO_ADDRESS* url string in MainScreenActivity class to an online one. You could try using "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov"
5. Finally, you can run the application. It is recommended that you use or emulate a 10' tablet, because the UI is designed to run only on this kind of devices. If you omit steps 2,3 or 4, the app will run anyway, but you will not be able to check out some of the features.


