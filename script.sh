#!/bin/bash
for i in `seq 1 10`
do
	time wget -O/dev/null -q http://localhost:8010/api/movies #| Select-Object -Property Content 
	
	time curl -X POST \
	  http://localhost:8010/api/movies \
	  -H 'Cache-Control: no-cache' \
	  -H 'Content-Type: application/json' \
	  -H 'Postman-Token: 350421ff-1c55-460b-ad09-2016ee02fa48' \
	  -d '{
	    "title": "Titanic",
	    "director": "James Cameron",
	    "releaseDate": "1997-11-01",
	    "language": "English",
	    "pending": false
	}
	'

	echo $i
done

