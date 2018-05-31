#!/bin/bash

printf "Timestamp, Memory, Disk, , , CPU\n"
rm disk.txt
touch disk.txt
DATE=$(date +"%m%d_%H:%M:%S")
iostat -dxt /dev/sda 1 > disk.txt &
while true; do
MEMORY=$(free -m | awk 'NR==2{printf "%.2f\t", $3*100/$2 }') 
CPU=$(expr 100 - $(vmstat 1 2|tail -1|awk '{print $15}'))
DISK=$(cat disk.txt | tail -2 | rev | cut -c 1-6 | rev)
echo "$DATE,$MEMORY,$DISK,$CPU"
done
rm disk.txt
