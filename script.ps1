$i=10
echo $i
While ($i -ge 1) {

	#get web request
	# $getResponse = Invoke-WebRequest -URI http://10.0.1.24:31167/api/movies #| Select-Object -Property Content
	# echo $getResponse | Select-Object -Property Content
	## get REST
	# $getResponse = Invoke-RestMethod -Uri https://blogs.msdn.microsoft.com/powershell/feed/ 
	# echo $getResponse
	
	$name = 'Titanic'
	$name += $i
	$movie = @{
		title=$name
		director='filippo'
		language='tedesco'
	}
	$json = $movie | ConvertTo-Json
	
	# post REST
	$postResponse = Invoke-RestMethod 'http://localhost:9999/api/movies' -Method Post -Body $json -ContentType 'application/json'
	
	echo $i
	$i--
}
