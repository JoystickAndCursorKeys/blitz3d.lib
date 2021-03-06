Const g_maxscores=30

Dim g_scores(g_maxscores+1)
Dim g_names$(g_maxscores+1)
Dim g_difficulty$(g_maxscores+1) ; 0=very easy, 1=easy, 2 = normal, 3=difficult, 4=very hard
Dim g_level(g_maxscores+1)
Dim g_homepage$(g_maxscores+1)
Dim g_new(g_maxscores+1)

Dim gs_encbuf(1000)

Const g_hiscore_ua$="User-Agent: GAMESCORECLIENT/1.1 gsprotocol/1.1"



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

Function Highscore_Init(defaultplayername$)
 
	file = OpenFile ("highscores.dat")
	If(file<>0) Then
			SeedRnd 3464536436
			For t = 0 To g_maxscores-1
				g_names$(t)=ReadString(file)  
				g_difficulty(t)=ReadInt(file) Xor Rnd(-2147483648,2147483647)
				g_level(t)=ReadInt(file) Xor Rnd(-2147483648,2147483647)
				g_homepage(t)=ReadString(file) 
				g_scores(t)=ReadInt(file) Xor Rnd(-2147483648,2147483647)
				g_new(t)=ReadInt(file) Xor Rnd(-2147483648,2147483647)
				
						

			Next 
		CloseFile file 
	Else 
  	    file = WriteFile("highscores.dat")
		SeedRnd 3464536436
		For t = 0 To g_maxscores-1
			WriteString(file,defaultplayername$)
			WriteInt file, 2  Xor Rnd(-2147483648,2147483647)
			WriteInt file, 1  Xor Rnd(-2147483648,2147483647)
			WriteString(file,"www.lodgia.com")
			WriteInt file,((g_maxscores*10)-(t*10) )  Xor Rnd(-2147483648,2147483647)
			WriteInt file,0 Xor Rnd(-2147483648,2147483647)

		Next 
		CloseFile file 

		file = OpenFile ("highscores.dat")
		SeedRnd 3464536436
		For t = 0 To g_maxscores-1
				g_names$(t)=ReadString(file)  
				g_difficulty(t)=ReadInt(file) Xor Rnd(-2147483648,2147483647)
				g_level(t)=ReadInt(file) Xor Rnd(-2147483648,2147483647)
				g_homepage(t)=ReadString(file) 
				g_scores(t)=ReadInt(file) Xor Rnd(-2147483648,2147483647)
				g_new(t)=ReadInt(file) Xor Rnd(-2147483648,2147483647)

		Next 
		CloseFile file 		
		
	EndIf 
End Function


Function InHiScores(score)
	
	For t=0 To g_maxscores-1
		If(score>g_scores(t)) Then  

			DebugLog score + " bigger then ? "+g_scores(t)
			Return 1 
		EndIf
	Next 
	DebugLog "inhighscores="+inhighscores

	Return 0
End Function

Function SaveHiScore(webaddress$, script$, webport , scorebook$, password$, score,name$, difficulty$,level,homepage$)
	
    a=0
	counter=0
	Flipper2=0

	DebugLog "SaveHiScoreLocal "+score+","+name$+","+difficulty$+","+level+","+homepage$


	
	inhighscores=InHiScores(score)
	
	If(inhighscores=0) Return 0
	
	If(name$="") Return 0
	
	g_scores(g_maxscores)=score
	g_names$(g_maxscores)=name$
	g_difficulty(g_maxscores)=difficulty
	g_level(g_maxscores)=level
	g_homepage$(g_maxscores)=homepage

	g_new(g_maxscores)=1
	
	;sortem
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
			swapdifficulty=g_difficulty(x)
			swaplevel=g_level(x)
			swaphomepage$=g_homepage(x)
			swapnew=g_new(x)

			g_scores(x)=g_scores(highestix)
			g_names$(x)=g_names(highestix)
			g_difficulty(x)=g_difficulty(highestix)
			g_level(x)=g_level(highestix)
			g_homepage$(x)=g_homepage(highestix)
			g_new(x)=g_new(highestix)

			g_scores(highestix)=swap
			g_names$(highestix)=swapname$
			g_difficulty(highestix)=swapdifficulty
			g_level(highestix)=swaplevel
			g_homepage$(highestix)=swaphomepage
			g_new(highestix)=swapnew
			
		EndIf 	
		
	Next 
	
	If(webaddress$<>"") Then
	
		;try to save on network
		For t = 0 To g_maxscores-1
			If(g_new(t)=1 And webaddress$<>"" And webport<>0 And scorebook$<>"") Then
				If(SaveScoreOnNet(webaddress$, script$, webport , scorebook$, password$, g_scores(t), g_names$(t),g_difficulty(t),g_level(t),g_homepage$(t))) Then
					g_new(t)=0
				EndIf 
			EndIf 
		Next 
	
		UpdateScoreFromNet(webaddress$, script$, webport , scorebook$)
	EndIf 
		
	;save localy	
  	file = WriteFile("highscores.dat")
	SeedRnd 3464536436	
	For t = 0 To g_maxscores-1
		;WriteString(file,g_names$(t))
		;WriteInt file, g_scores(t) Xor Rnd(-2147483648,2147483647)
		;WriteInt file, g_new(t) Xor Rnd(-2147483648,2147483647)

			;g_scores(highestix)=swap
			;g_names$(highestix)=swapname$
			;g_difficulty(highestix)=swapdifficulty
			;g_level(highestix)=swaplevel
			;g_homepage$(highestix)=swaphomepage
			;g_new(highestix)=swapnew
			
			WriteString file,g_names$(t)
			WriteInt file, g_difficulty(t)  Xor Rnd(-2147483648,2147483647)
			WriteInt file, g_level(t)  Xor Rnd(-2147483648,2147483647)
			WriteString file,g_homepage$(t)
			WriteInt file,g_scores(t)  Xor Rnd(-2147483648,2147483647)
			WriteInt file,g_new(t) Xor Rnd(-2147483648,2147483647)
						
	Next 
	CloseFile file 
	
End Function

Function EncodeHSE$(password$,mystring$)

	;mystring$=URLEncode(mystring0$)

	pwlen=Len(password$)
	
	incall=Asc(Mid(password$,1,1))

	For t=1 To Len(mystring$)
		pwix=pwlen-t
		If(pwix<1) Then pwix=1

		While(pwix>pwlen)
			pwix=pwix-pwlen
		Wend 
		gs_encbuf(t)=Asc(Mid(mystring$,t,1))
		gs_encbuf(t)=gs_encbuf(t)+Asc(Mid(password$,pwix,1))
		gs_encbuf(t)=gs_encbuf(t)+incall
		gs_encbuf(t)=gs_encbuf(t)+t-1
		
		;DebugLog ">>"+Asc(Mid(mystring$,t,1)) + " ---> " +gs_encbuf(t) + "    "+Asc(Mid(password$,pwix,1)) + "  " + pwix

		;DebugLog ">"+
	Next 
	
	string2$=""
	For t=1 To Len(mystring$)
	
		codeint=gs_encbuf(t)

		code$="000"+codeint
		lc=Len(code$)
		
		string2$=string2$+Mid(code$,lc-2,3)
		
	Next 
	
	Return string2$
End Function 


Function SaveScoreOnNet(webaddress$, script$, webport , scorebook$, password$,  score,name$, difficulty$,level$,homepage$)

DebugLog "Connecting for save action..."
tcp=OpenTCPStream( webaddress$,webport)

If Not tcp DebugLog "Failed.": Return False 

DebugLog  "Connected! Sending request..."

scorebook$=URLEncode(scorebook$)
password$=URLEncode(password$)
name$=URLEncode(name$)


param$="cmd=save&"
param$=param$+"scorebook="+scorebook$+"&request="

;encode score entry


entry$=entry$+"name="+StripTrailingSpaceChars(StripHeadingSpaceChars(name$))+"&"
entry$=entry$+"difficulty="+StripTrailingSpaceChars(StripHeadingSpaceChars(difficulty$))+"&"
entry$=entry$+"level="+StripTrailingSpaceChars(StripHeadingSpaceChars(level))+"&"
entry$=entry$+"homepage="+StripTrailingSpaceChars(StripHeadingSpaceChars(homepage$))+"&"
entry$=entry$+"score="+score+"&"

DebugLog "SaveScoreOnNet.UnEncodedEntry="+entry$

param$=param$+EncodeHSE(password$,entry$)

;difficulty$,level$,homepage$


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


myString$=""
WriteLine tcp,""
DebugLog mystring$

If Eof(tcp) DebugLog  "Failed.":Return False 

DebugLog  "Request sent! Waiting for reply..."

success=0
While Not Eof(tcp)
	myline$=ReadLine$( tcp )
	DebugLog "save:'"+myline$+"'"
	If(myline$="OK") Then success=1
Wend

CloseTCPStream tcp
DebugLog "EOF"

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

DebugLog  "No EOF,Waiting for reply..."

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

	If(index<g_maxscores) Then
	
		g_scores(index)=HSGetTagValue$(myline$,"score")
		g_difficulty(index)=HSGetTagValue$(myline$,"difficulty")
		g_level(index)=HSGetTagValue$(myline$,"level")
		g_homepage(index)=HSGetTagValue$(myline$,"homepage")
		g_names(index)=HSGetTagValue$(myline$,"name")
	 	
		;DebugLog "i("+index+") "+g_names(index)+","+g_scores(index)+","+g_difficulty(index)+","+g_level(index)+","+g_homepage(index)
	EndIf
	INDEX=INDEX+1

	EndIf 
	
	For T=index To g_maxscores-1
		g_scores(t)=0
		g_difficulty(t)=2
		g_level(t)=1
		g_homepage(t)="www.lodgia.com"
		g_names(t)="nobody"
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

Function URLDecode$(stringg$)

	newstr$=""
	t=1
	While t<= Len(stringg$)
		char$=Mid(Stringg$,t,1)
		
		If( char$<>"%") Then 
			newstr$=newstr$ + char$
			t=t+1
			
		Else 	
			stringval0$=Mid(Stringg$,t+1,1)
			stringval1$=Mid(Stringg$,t+2,1)

			val=16*	HexToInt(stringval0$)
			val=val + HexToInt(stringval1$)
			
			;DebugLog "val = "+val+" from "+stringval0$+stringval1$
			newstr$=newstr$ + Chr(val)

			t=t+3
		EndIf 
		
	Wend  
	
	Return newstr$

End Function 

Function HexToInt(char$)
	Select char$
	Case "0"
	Return 0
	Case "1"
	Return 1
	Case "2"
	Return 2
	Case "3"
	Return 3
	Case "4"
	Return 4
	Case "5"
	Return 5
	Case "6"
	Return 6
	Case "7"
	Return 7
	Case "8"
	Return 8
	Case "9"
	Return 9
	Case "a"
	Return 10
	Case "b"
	Return 11
	Case "c"
	Return 12
	Case "d"
	Return 13
	Case "e"
	Return 14
	Case "f"
	Return 15
	Case "A"
	Return 10
	Case "B"
	Return 11
	Case "C"
	Return 12
	Case "D"
	Return 13
	Case "E"
	Return 14
	Case "F"
	Return 15
	End Select 
	Return 0
End Function 

Function HSGetTagValue$(tags$,tagname$)

	start=1
	offset=1
	While(offset>0)
		
		
		offset=Instr(tags$,"=",start)
		If(offset>0) Then
			offset2=Instr(tags$,"&",offset)
			
			If(offset2=0) Then offset2=Len(tags$)+1

			name$=Mid(tags$,start,offset-start)
			;DebugLog ">>tag '"+name$+"'"
			;DebugLog "value '"+value$+"'"
			
			If(name$=tagname$) Then
					value$=Mid(tags$,offset+1,(offset2-offset)-1)
					
					;DebugLog "found = "+Mid(tags$,offset+1,offset2-offset)
					Return StripTrailingSpaceChars(StripHeadingSpaceChars(URLDecode(value$)))

			EndIf 
			
			start=offset2+1
			
		EndIf 
		
	Wend 
	Return tags$
End Function 


Function StripHeadingSpaceChars$(str1$)
	str2$=""
	a=1
	For t=1 To Len(str1)
		c$=Mid(str1$,t,1) 
		DebugLog "mid="+str1$
		a=t
		If(c$=" " Or c$=Chr(09)) Then 
			;
		Else
			Exit 
		EndIf 	
	Next 
	DebugLog "h t="+t+" "+str1$
	For t=a To Len(str1$)
		str2$=str2$+Mid(str1$,t,1)
		DebugLog  "str2 = "+str2$ 
	Next 
	
	Return str2$
	
End Function 

Function StripTrailingSpaceChars$(str1$)

	str2$=""
	a=Len(str1$)
	For t=Len(str1) To 1 Step -1
		c$=Mid(str1$,t,1) 
		DebugLog  "tmid="+str1$
		a=t
		If(c$=" " Or c$=Chr(09)) Then 
			;
		Else
			Exit 
		EndIf 	
	Next 
	DebugLog "h t="+t+" "+str1$

	For t=a To 1 Step -1
		str2$=Mid(str1$,t,1)+str2$
		DebugLog  "tstr2 = "+str2$ 
	Next 
	
	Return str2$
	
End Function 