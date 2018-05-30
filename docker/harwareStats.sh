#!/bin/bash

printf "Memory\t\t Disk\t\tCPU\n"
rm disk.txt
touch disk.txt
iostat -dxt /dev/sda 1 > disk.txt &
while true; do
MEMORY=$(free -m | awk 'NR==2{printf "%.2f%%\t\t", $3*100/$2 }') 
CPU=$(expr 100 - $(vmstat 1 2|tail -1|awk '{print $15}'))
DISK=$(cat disk.txt | tail -2 | rev | cut -c 1-6 | rev)
echo "$MEMORY$DISK\t\t$CPU"
done
rm disk.txt
