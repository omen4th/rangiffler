#!/bin/bash

# Launch the Frontend
cd rangiffler-client
npm start &

# Launch Rangiffler Services
cd ../rangiffler-auth
gradle bootRun &

cd ../rangiffler-gateway
gradle bootRun &

cd ../rangiffler-users
gradle bootRun &

cd ../rangiffler-geo
gradle bootRun &

cd ../rangiffler-photo
gradle bootRun &

# Wait for all processes to complete
wait

# Open the frontend in the default browser
xdg-open http://127.0.0.1:3001/