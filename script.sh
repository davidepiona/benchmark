#!/bin/bash
for i in `seq 1 10`
do
	wget http://localhost:8010/api/movies #| Select-Object -Property Content

	echo $i
done
