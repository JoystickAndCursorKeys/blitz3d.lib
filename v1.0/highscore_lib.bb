Const g_maxscores=20

Dim g_scores(g_maxscores+1)
Dim g_names$(g_maxscores+1)
Dim g_new(g_maxscores+1)

Const g_hiscore_ua$="User-Agent: GAMESCORECLIENT/1.0 gsprotocol/1.0"

;Highscore_Init("Mighty Invader")

;SaveHiScore(10,"hello7")
;SaveHiScore(11,"hello6")
;SaveHiScore(12,"hello5")
;SeedRnd MilliSecs () 


;SaveHiScore("localhost","/hiscore.php",80,book$,pw$,Rand(2000,2300),"koos")

;SaveHiScore(14,"hello3")
;SaveHiScore(15,"hello2")
;SaveHiScore(16,"hello1")

;DebugLog "start after save";
;For t=0 To g_maxscores-1
;	DebugLog g_names$(t)+"="+g_scores(t)+":"+g_new(t)
;	Print g_names$(t)+"="+g_scores(t)
;Next 

Function Highscore_Init(playername$)
 
	file = OpenFile ("highscores.dat")
	If(file<>0) Then
			SeedRnd 3464536436
			For t = 0 To g_maxscores-1
				g_names$(t)=ReadString(file)  
				g_scores(t)=ReadInt(file) Xor Rnd(-2147483648,2147483647)
				g_new(t)=ReadInt(file) Xor Rnd(-2147483648,2147483647)

			Next 
		CloseFile file 
	Else 
  	    file = WriteFile("highscores.dat")
		SeedRnd 3464536436
		For t = 0 To g_maxscores-1
			WriteString(file,playername$)
			WriteInt file,((g_maxscores*10)-(t*10) )  Xor Rnd(-2147483648,2147483647)
			WriteInt file, 0  Xor Rnd(-2147483648,2147483647)
			
		Next 
		CloseFile file 

		file = OpenFile ("highscores.dat")
		SeedRnd 3464536436
		For t = 0 To g_maxscores-1
			g_names$(t)=ReadString(file)
			g_scores(t)=ReadInt(file)  Xor Rnd(-2147483648,2147483647)
			g_new(t)=ReadInt(file)  Xor Rnd(-2147483648,2147483647)

		Next 
		CloseFile file 		
		
	EndIf 
End Function


Function InHiScores(score)
	
	For t=0 To g_maxscores-1
		If(score>g_scores(t)) Then  Return 1 
		;DebugLog "bigger then ? "+g_scores(t)
	Next 
	;DebugLog "inhighscores="+inhighscores

	Return 0
End Function

Function SaveHiScore(webaddress$, script$, webport , scorebook$, password$, score,name$)
	
    a=0
	counter=0
	Flipper2=0

	DebugLog "SaveHiScoreLocal "+score+","+name$
	
	inhighscores=InHiScores(score)
	
	If(inhighscores=0) Return 0
	
	If(name$="") Return 0
	
	g_names$(g_maxscores)=name$
	g_scores(g_maxscores)=score
	g_new(g_maxscores)=1
	
	For x=0 To g_maxscores
		highest=0
		highestix=-1

		For t = x To g_maxscores
			If(g_scores(t)>highest)
				highest=g_scores(t)
				highestix=t
			EndIf
		Next

		;swap the highest with x
		
		If highestix>-1
		
			swap=g_scores(x)
			swapname$=g_names$(x)
			swapnew=g_new(x)

			g_scores(x)=g_scores(highestix)
			g_names$(x)=g_names(highestix)
			g_new(x)=g_new(highestix)

			g_scores(highestix)=swap
			g_names$(highestix)=swapname$
			g_new(highestix)=swapnew
			
			
		EndIf 	
		
	Next 
	
	;try to save on network
	For t = 0 To g_maxscores-1
		If(g_new(t)=1 And webaddress$<>"" And webport<>0 And scorebook$<>"") Then
			If(SaveScoreOnNet(webaddress$, script$, webport , scorebook$, password$, g_names$(t), g_scores(t))) Then
				g_new(t)=0
			EndIf 
		EndIf 
	Next 

	UpdateScoreFromNet(webaddress$, script$, webport , scorebook$)
	
	;save localy	
  	file = WriteFile("highscores.dat")
	SeedRnd 3464536436	
	For t = 0 To g_maxscores-1
		WriteString(file,g_names$(t))
		WriteInt file, g_scores(t) Xor Rnd(-2147483648,2147483647)
		WriteInt file, g_new(t) Xor Rnd(-2147483648,2147483647)
			
	Next 
	CloseFile file 
	
End Function

Function SaveScoreOnNet(webaddress$, script$, webport , scorebook$, password$, name$, score)

DebugLog "Connecting for save action..."
tcp=OpenTCPStream( webaddress$,webport)

If Not tcp DebugLog "Failed.": Return False 

DebugLog  "Connected! Sending request..."

scorebook$=URLEncode(scorebook$)
password$=URLEncode(password$)
name$=URLEncode(name$)


param$="cmd=save&"
param$=param$+"scorebook="+scorebook$+"&"
param$=param$+"name="+name$+"&"
param$=param$+"score="+score+"&"
param$=param$+"passwd="+password$


myString$="GET "+script$+"?"+param$+" HTTP/1.0"
DebugLog mystring$
WriteLine tcp,mystring$

myString$="Host: www.lodgia.com"
DebugLog mystring$
WriteLine tcp,myString$

myString$="Connection: close"
DebugLog mystring$
WriteLine tcp,myString$

myString$=g_hiscore_ua$
DebugLog mystring$
WriteLine tcp,myString$
;
myString$="Referer: ."
DebugLog mystring$
WriteLine tcp,myString$

WriteLine tcp,""

If Eof(tcp) DebugLog  "Failed.":Return False 

DebugLog  "Request sent! Waiting for reply..."

success=0
While Not Eof(tcp)
	myline$=ReadLine$( tcp )
	DebugLog myline$ 
	If(myline$="OK") Then success=1
Wend

CloseTCPStream tcp


If success=1 Then 
	DebugLog  "Success!!" 
	Return True 
Else 
	DebugLog  "Error! "+ myline$
	Return False 
EndIf 

End Function 

Function UpdateScoreFromNet(webaddress$, script$, webport , scorebook$)
	; OpenTCPStream/CloseTCPStream Example

;script$="/test.php"
DebugLog "Connecting for getlist action... '"+webaddress$+"' :"+webport 
tcp=OpenTCPStream( webaddress$,webport)
;tcp=OpenTCPStream( "www.lodgia.com",80 )

If Not tcp DebugLog "Failed.": Return False 

DebugLog  "Connected! Sending request..."

scorebook$=URLEncode(scorebook$)
password$=URLEncode(password$)

param$="cmd=getlist&"
param$=param$+"scorebook="+scorebook$+"&"
param$=param$+"limit="+g_maxscores

myString$="GET "+script$+"?"+param$+" HTTP/1.0"
DebugLog mystring$
WriteLine tcp,mystring$

myString$="Host: www.lodgia.com"
DebugLog mystring$
WriteLine tcp,myString$

myString$="Connection: close"
DebugLog mystring$
WriteLine tcp,myString$

myString$=g_hiscore_ua$
DebugLog mystring$
WriteLine tcp,myString$
;
myString$="Referer: ."
DebugLog mystring$
WriteLine tcp,myString$

WriteLine tcp,""


DebugLog  "Request send! Waiting for reply..."

If Eof(tcp) DebugLog  "Failed.":Return False 

DebugLog  "Request sent! Waiting for reply..."

success=0
While Not Eof(tcp)
	myline$=ReadLine$( tcp )
	DebugLog "line:"+myline$
	If(myline$="START") Then 
		success=1
		Exit 
	EndIf 
Wend

If(success=0) Then  DebugLog "Failed to find START TAG":CloseTCPStream tcp: Return False 
DebugLog "START tag found"

success=0
index=0
While Not Eof(tcp)

	myline$=ReadLine$( tcp )
	DebugLog ">"+myline$
	If(myline$="END") Then 
		success=1
		Exit 
	Else

	ix=Instr (myline$, "=",1) 
	If(ix=0) Then
		Exit 
	EndIf 
	
	name$=Left$ (myline$, ix-1)
	score$=Right$ (myline$, Len(myline$)-ix)
	If(index<=g_maxscores) Then
	 	   g_scores(index)=score$
			g_names$(index)=NAME$
	EndIf
	INDEX=INDEX+1

	EndIf 
	
	For T=index To g_maxscores-1
		g_scores(t)=0
		g_names$(t)="nobody"

	Next
Wend


CloseTCPStream tcp


If success=1 Then 
	DebugLog  "Success!!" 
	Return True 
Else 
	DebugLog  "Error!"
	Return False 
EndIf 


End Function 

Function URLEncode$(stringg$)

	newstr$=""
	For t=1 To Len(stringg$)
		char$=Mid(Stringg$,t,1)
		value=Asc(char$) 
		If( (value >= Asc("0") And value <= Asc("9")) Or (value >= Asc("a") And value <= Asc("z")) Or (value >= Asc("A") And value <= Asc("Z")) Or value=Asc("_")) Then 
			newstr$=newstr$ + char$
		Else 	
			Hexs$=Hex(value)
			Hexs$=Right(Hexs$,2)
			newstr$=newstr$ + "%"
			newstr$=newstr$ +  hexs$
		EndIf 
	Next 
	
	Return newstr$

End Function 