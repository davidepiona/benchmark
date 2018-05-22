#!/bin/bash

nGet=1000
nPos=300
nUpl=100
boolGet=true
boolPos=false
boolUpl=false
boolGetCont=false
boolPosCont=false
boolUplCont=false
> error.txt
export TIMEFORMAT='real: %3R  user: %3U sys: %3S'

#-----------------------------------------------------------peak of GET requests
if $boolGet; then
	for i in `seq 1 $nGet`
	do	
		wget -q -O/dev/null http://127.0.0.1:8010/api/movies || echo $? >>error.txt &
	done
wait
errors=$(wc -w error.txt | head -n1)
echo "Si sono verificati "$errors" errori"
echo "Sleeping"
# sleep 5
fi


#-----------------------------------------------------------continuos GET requests
if $boolGetCont; then
	for t in `seq 1 10`
	do
		start=$(date +%s.%N);
		for i in `seq 1 100`
		do	
			wget -q -O/dev/null http://127.0.0.1:9999/api/movies || echo $? >>error.txt &
		done
		wait
		dur=$(echo "$(date +%s.%N) - $start" | bc);
		echo $t" times using "$dur" seconds"
		sleep $( bc <<< "1 - $dur" )
	done
errors=$(wc -w error.txt | head -n1)
echo "Si sono verificati "$errors" errori"
echo "Sleeping"
#sleep 5
fi



#-----------------------------------------------------------POST
if $boolPos; then
	for i in `seq 1 $nPos`
	do
		time curl -s -X POST \
			http://127.0.0.1:9999/api/movies \
			-H 'Cache-Control: no-cache' \
			-H 'Content-Type: application/json' \
			-d '{
			"title": "Titanic'$i'",
			"director": "James Cameron",
			"releaseDate": "1997-11-01",
			"language": "English",
			"pending": false
		}
		' > /dev/null && echo 'post '$i &
	done
wait
echo "Sleeping"
#sleep 5
fi




#------------------------------------------------------------UPLOAD
if $boolUpl; then
	for i in `seq 1 $nUpl`
	do
		time curl -X POST \
			http://127.0.0.1:8020/api/upload/f4d50467-ccbf-4b04-8d02-5daaf6c02710 \
			-H 'Cache-Control: no-cache' \
			-F 'file=@/home/davide/Desktop/Bambino_Freddo.mp4' &
		#if [ $i -eq 10 ]; then
		#	sh script.sh
		#fi
		echo 'upload '$i
	done
	wait
fi
