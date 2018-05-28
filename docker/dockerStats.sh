DATE=$(date +"%m%d_%H:%M:%S")
docker stats --format "{{.Container}}, {{.Name}}, {{.CPUPerc}}, {{.MemUsage}}, {{.MemPerc}}, {{.NetIO}}, {{.BlockIO}}, {{.PIDs}}" > stat${DATE}.csv
