# UMLShuttleTracker
Proof-of-Concept to let students track the location of the UML shuttle bus.

This code was written by a team of 3 students for a grad class to demostrate how a possible mobile location tracking app could be created. It was written about 4 years ago (circa 2011), with Android APK Level 12. I took a lead development role on this project despite nobody on our team having any experience writing mobile apps at the time, including myself. Our app turned out quite well for our first Android app, we entered it in a mobile contest and won 2nd place.

The overall architecture is that there's two android apps: "a server app", and "a client app". The "server app" is used by the bus, and it simply takes GPS coordinates, and sends them to a php script which puts it into a mysql database. The client app does most of the work, where it'll poll the database at set intervals for the latest bus location and it'll update the map accordingly. There's also geo-alerts that will alert you when the bus is nearby. 

This repository only contains the client app (the majority of the total shuttletracker app), the "server app" has been burried in my hard drive among a mountain of other files, and has been lost, quite possibly, forever.
