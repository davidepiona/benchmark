#!/bin/bash
for i in `seq 1 100`
do
	if true; then	
		time wget -O/dev/null -q http://localhost:8010/api/movies #| Select-Object -Property Content
	fi
	if true; then
		time curl -X POST \
		  http://localhost:8010/api/movies \
		  -H 'Cache-Control: no-cache' \
		  -H 'Content-Type: application/json' \
		  -d '{
		    "title": "Titanic",
		    "director": "James Cameron",
		    "releaseDate": "1997-11-01",
		    "language": "English",
		    "pending": false
		}
		'
	fi
	if false; then
		curl -X POST \
		  http://localhost:8020/api/upload/d6dccd42-cda5-4d33-bf08-1180700e8dcd \
		  -H 'Cache-Control: no-cache' \
		  -F 'file=@/home/davide/Desktop/Bambino_Freddo.mp4'
	fi

	if false; then
		wget \
		  --method POST \
		  --header 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
		  --header 'Cache-Control: no-cache' \
		  --body-data '------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name="file"; filename="Bambino_Freddo.mp4"\r\nContent-Type: video/mp4\r\n\r\n\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--' \
		  --output-document \
		  - http://localhost:8020/api/upload/d6dccd42-cda5-4d33-bf08-1180700e8dcd
	fi
	echo $i
done

