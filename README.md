# CNIT 325 Team Project
Darel Aradin, Ryan Hung, Linzzi Ji, Eunchae Jung, Priscilla Tam

**Project Overview:**
TripSync is a Java-based desktop application that helps users organize and plan trips, both domestically and internationally. Users can register and log in to a personal account, select a destination city, browse attractions and points of interest, generate optimized travel routes, and receive context-aware recommendations such as weather-based clothing tips and hotel suggestions.
The system integrates several external APIs to provide real-time data:
Google Maps API – Route generation and location/POI data
Places and Maps 
WeatherAPI – Real-time forecasts and travel recommendations

**Build Instructions**
The application is currently split into two separate workflows due to prototype limitations. Follow the steps below for each. 

# 1. Registration and Login
This demonstrates user account creation, profile creation, and authentication. 
Steps:

Open the project in your IDE.
Run ‘src/main/java/com/tranner/network/UserServer.java’ first.
This starts the local user server that handles authentication requests.
Wait until the console shows the server is running before proceeding.
Run ‘src/main/java/com/tranner/Main.java’.
This launches the login/registration UI.
Use the interface to register a new account and log in with created credentials.

# 2. Create Trip and Itinerary 
This demonstrates destination selection, itinerary building, companion selection, and weather tips.
Steps:
Run ‘src/main/java/com/tranner/MainPanel.java’ directly.
This launches the trip planning interface as a standalone panel.
Select a destination city to begin building your itinerary.
Browse attractions, add stops, and save your trip.
>> Note: This module runs independently from the rest of the system due to a current integration issue (see Known Issues below)

**Known Issues**
Main.java (main module with login and profile creation functionalities) and MainPanel.java (itinerary/trip creation module) are not yet integrated. MainPanel.java currently runs as a standalone file and does not require login. This is a known prototype limitation and will be resolved in a future iteration.
API keys expire after a certain amount of time. Missing keys will cause runtime errors in the weather and map.

**Future Improvements**
Merge the login and trip planning modules into a single unified application flow.
Persist user itineraries to a database. 
Add support for multi-city trip planning.
Add hotel/accomodations APIs (currently there exists the class, but it is not utilized).
Minimize usability friction. Currently the interactions are not the most user-friendly. 

**Team Responsibilities**
Darel Aradin | Backend Development, WeatherAPI Implementation
Ryan Hung | Project Management, Backend Development
Linzzi Ji | Backend Development, Google Maps API Implementation
Eunchae Jung | Frontend Development
Priscilla Tam | UX/UI Design, Frontend Development

