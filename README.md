# SCAI AndroidClient

##SCAI (Computarized System for Smart Trailers) Project
This project is part of a computer engineering thesis.
The whole SCAI system consists of: 
  - A main computer (RPI2) located in the truck's trailer, which gathers data from the sensors, processes, and sends it wirelessly to a display device located in the driver's cabin.
  - Several sensors an peripherals, such as: IP Camera, GPS Module, Inclinometer, Gyroscope, Magnetometer, Termometer and Barometer.
  - A display device, which is a 10' android tablet, which runs the app located in this repository and that it is in charge of displaying the "rearview" video stream, an embedded map (OSMdroid) and the data from the sensors gathered by the RPI2.
  
##Android Client for SCAI  project. 
As mentioned above, SCAI Android client is an app that runs on a tablet located in the truck's driver cabin. It is in charge of displaying all of the information gathered by the rest of the system in order to contribute to the road safety.
###Features:
  - Map view:
  * OSMDroid offline map viewer.
  * TO BE DONE: Add functionalities to this map view, such as synchronization with the current GPS coordenates, among others.
  - Video view: 
  * RTSP player, which plays video signal streamed by the IP Camera.
  * TO BE DONE: Find a workaround for the video player, in order to be able to modify the buffer size and decrease the streaming latency.
  - Sensor status views:
  * Tipper inclination (Graphic & Numeric).
  * Side inclination (Graphic & Numeric).
  * Compass (Graphic).
  * Sensor-group status (Graphic).
  * Speed (Numeric).
  * Altitude (Numeric).
  * Date and Time (Numeric).


