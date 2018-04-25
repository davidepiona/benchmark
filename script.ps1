$i=10
echo $i
While ($i -ge 1) {
	
	Invoke-WebRequest -URI http://localhost:8010/api/movies #| Select-Object -Property Content
	# Invoke-RestMethod -Uri https://blogs.msdn.microsoft.com/powershell/feed/ 
	# echo $getResponse
	
	$name = 'Titanic'
	$name += $i
	$movie = @{
		title=$name
		director='filippo'
		language='tedesco'
	}
	$json = $movie | ConvertTo-Json
	$postResponse = Invoke-RestMethod 'http://localhost:8010/api/movies' -Method Post -Body $json -ContentType 'application/json'
	
	echo $i
	$i--
}
