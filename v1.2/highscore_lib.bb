Const g_maxscores=10

Dim g_hscores(g_maxscores+1)
Dim g_hnames$(g_maxscores+1)
Dim g_hdifficulty$(g_maxscores+1) ; 0=very easy, 1=easy, 2 = normal, 3=difficult, 4=very hard
Dim g_hlevel(g_maxscores+1)

Dim g_scores(g_maxscores+1)
Dim g_names$(g_maxscores+1)
Dim g_difficulty$(g_maxscores+1) ; 0=very easy, 1=easy, 2 = normal, 3=difficult, 4=very hard
Dim g_level(g_maxscores+1)
Dim g_new(g_maxscores+1)

Dim gs_encbuf(1000)

Const g_hiscore_ua$="User-Agent: GAMESCORECLIENT/1.2 gsprotocol/1.2"

Global g_boardfull=0
Global g_hsherocnt=0
Global g_hslatestcomment$

Type hshandle
	Field webaddress$
	Field script$
	Field webport 
	Field scorebook$
	Field password$
End Type


Function Highscore_Init.hshandle(defaultplayername$, webaddress$, script$, webport , scorebook$, password$)

	hsh.hshandle=New hshandle
	
	hsh\webaddress$=webaddress$
	hsh\script$=script$
	hsh\webport =webport
	hsh\scorebook$=scorebook$
	hsh\password$=password$
 
	file = OpenFile ("highscores12.dat")
	If(file<>0) Then
			SeedRnd 3464536436
			For t = 0 To g_maxscores-1
				g_names$(t)=ReadString(file)  
				g_difficulty(t)=ReadInt(file) Xor Rnd(-2147483648,2147483647)
				g_level(t)=ReadInt(file) Xor Rnd(-2147483648,2147483647)
				g_scores(t)=ReadInt(file) Xor Rnd(-2147483648,2147483647)
				g_new(t)=ReadInt(file) Xor Rnd(-2147483648,2147483647)
			Next 
		CloseFile file 
	Else 
  	    file = WriteFile("highscores12.dat")
		SeedRnd 3464536436
		For t = 0 To g_maxscores-1
			WriteString(file,defaultplayername$)
			WriteInt file, 2  Xor Rnd(-2147483648,2147483647)
			WriteInt file, 1  Xor Rnd(-2147483648,2147483647)
			WriteInt file,((g_maxscores*10)-(t*10) )  Xor Rnd(-2147483648,2147483647)
			WriteInt file,0 Xor Rnd(-2147483648,2147483647)

		Next 
		CloseFile file 

		file = OpenFile ("highscores12.dat")
		SeedRnd 3464536436
		For t = 0 To g_maxscores-1
				g_names$(t)=ReadString(file)  
				g_difficulty(t)=ReadInt(file) Xor Rnd(-2147483648,2147483647)
				g_level(t)=ReadInt(file) Xor Rnd(-2147483648,2147483647)
				g_scores(t)=ReadInt(file) Xor Rnd(-2147483648,2147483647)
				g_new(t)=ReadInt(file) Xor Rnd(-2147483648,2147483647)

		Next 
		CloseFile file
		
	EndIf 
	
	
	
	Return hsh
End Function


Function HS_MadeItInHiScores(score)
	
	For t=0 To g_maxscores-1
		If(score>g_scores(t)) Then  Return 1 
;		DebugLog score + " bigger then ? "+g_scores(t)
	Next 
	;DebugLog "inhighscores="+inhighscores

	Return 0
End Function


Function HS_SetUserPw(hsh.hshandle,username$,userpw$)

	dir$=SystemProperty("tempdir")
	
	encusername$=EncodeHSE(hsh\password$,username$)
	
	file$=dir$+"upw_hsl_"+encusername$+".dat"
	
;	DebugLog "SetUserPw.file = "  + file$
	encpw$=encodelocalpw(hsh\password$,userpw$)
;	DebugLog "SetUserPw.encpw= "  + encpw$

			
	test=CheckPWOnNet(hsh,  username$, userpw$)
	
	If(test=1) Then
		file = WriteFile(file$)
		WriteString file,encpw$
		CloseFile file 
	EndIf 
	
	If(test=1 Or test=-1) Then 
		Return True
	Else
		DeleteFile file$
		Return False
	EndIf 
	
	
End Function 



Function HS_Password$(hsh.hshandle,  username$, verify)
	
	dir$=SystemProperty("tempdir")
	
	encusername$=EncodeHSE(hsh\password$,username$)

	file$=dir$+"upw_hsl_"+encusername$+".dat"
	
	;Print "file = "  + file$
	;pw$=decodelocalpw(hsh\password$,userpw$)
	;Print "encpw= "  + encpw$
;	DebugLog "HS_Password:file = "+file$
	file = OpenFile(file$)
	If(file=0) Return ""
	encpw$=ReadString (file)
	CloseFile file 
	
	pw$=decodelocalpw(hsh\password$,encpw$)
;	DebugLog "HS_Password:pw = "+pw$+ "  ("+encpw+")"
	
	Return pw$
	
	If(verify=1 Or verify=True) Then
	
		rv=CheckPWOnNet(hsh,  username$, pw$)
	
		
		If(rv=-1) Then
			DebugLog "Failed to get stat from inet"
			Return pw$
		ElseIf(rv=True ) Then
			Return pw$
		Else
			Return ""
		EndIf 
		
	EndIf 

End Function 

Function CheckPWOnNet(hsh.hshandle,  uname$, upw$)

DebugLog "Connecting for check  action..."
tcp=OpenTCPStream( hsh\webaddress$,hsh\webport)

If Not tcp DebugLog "Failed.": Return -1 

DebugLog  "Connected! Sending request..."

scorebook$=URLEncode(hsh\scorebook$)
password$=URLEncode(hsh\password$)
uname$=URLEncode(uname$)
upw$=URLEncode(upw$)

param$="cmd=checkpw&"
param$=param$+"scorebook="+hsh\scorebook$+"&request="

;encode score entry

entry$=""
entry$=entry$+"name="+StripTrailingSpaceChars(StripHeadingSpaceChars(uname$))+"&"
entry$=entry$+"pw="+StripTrailingSpaceChars(StripHeadingSpaceChars(upw$))+"&"

DebugLog "CheckPWOnNet.UnEncodedEntry="+entry$

param$=param$+EncodeHSE(hsh\password$,entry$)

;difficulty$,level$


myString$="GET "+hsh\script$+"?"+param$+" HTTP/1.0"
DebugLog mystring$
WriteLine tcp,mystring$

myString$="Host: www.gamerprofile.info"
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

;WriteByte tcp,Chr(13)
;WriteByte tcp,Chr(10)


DebugLog mystring$

If Eof(tcp) DebugLog  "Failed.":Return False 

DebugLog  "Request sent! Waiting for reply..."

success=0
While Not Eof(tcp)
	myline$=ReadLine$( tcp )
	DebugLog "checkpw:'"+myline$+"'"
	If(myline$="OK") Then success=1 : Exit 
	If(myline$="NOK") Then success=0 : Exit 
	
Wend

CloseTCPStream tcp
DebugLog "EOF"

If success=1 Then 
	DebugLog  "Success!!" 
	Return 1 
Else 
	DebugLog  "Error! "+ myline$
	Return 0 
EndIf 

End Function 





Function SaveHiScore(hsh.hshandle, score,myname$, difficulty$,level)
	
    a=0
	counter=0
	Flipper2=0

	DebugLog "SaveHiScore"+score+","+myname$+","+difficulty$+","+level+","
	
	;If(g_boardfull=0) Then 
	inhighscores=HS_MadeItInHiScores(score)
	;Else
	;	inhighscores=1
	;EndIf 
	
	DebugLog "SaveHiScore inhighscores="+inhighscores


	If(inhighscores=0) Return 0
	
	If(myname$="") Return 0
	
	DebugLog "SaveHiScore madeit="


	g_scores(g_maxscores)=score
	g_names$(g_maxscores)=myname$
	g_difficulty(g_maxscores)=difficulty
	g_level(g_maxscores)=level
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
			swapnew=g_new(x)

			g_scores(x)=g_scores(highestix)
			g_names$(x)=g_names(highestix)
			g_difficulty(x)=g_difficulty(highestix)
			g_level(x)=g_level(highestix)
			g_new(x)=g_new(highestix)

			g_scores(highestix)=swap
			g_names$(highestix)=swapname$
			g_difficulty(highestix)=swapdifficulty
			g_level(highestix)=swaplevel
			g_new(highestix)=swapnew
			
		EndIf 	
		
	Next 
	
	If(hsh\webaddress$<>"") Then
	

		;try to save on network
		For t = 0 To g_maxscores-1
			If(g_new(t)=1 And hsh\webaddress$<>"" And hsh\webport<>0 And hsh\scorebook$<>"") Then
				
				DebugLog "try to save on network 1"

				pw$=HS_Password$(hsh ,  g_names$(t), 0)
				If(pw$<>"") Then
				
					DebugLog "try to save on network 2"
					stat$=SaveScoreOnNet(hsh, g_scores(t), g_names$(t),pw$, g_difficulty(t),g_level(t))
					If(stat$=1 Or stat$=-1) Then ;1=ok, 0=miscfail,  -1=pwfail
						If(stat$=1) Then 
							g_new(t)=0
						EndIf
						DebugLog "stat = "+stat$
					EndIf 
				EndIf 
			EndIf 
		Next 
	
		UpdateScoreFromNet(hsh)
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
			;g_new(highestix)=swapnew
			
			WriteString file,g_names$(t)
			WriteInt file, g_difficulty(t)  Xor Rnd(-2147483648,2147483647)
			WriteInt file, g_level(t)  Xor Rnd(-2147483648,2147483647)
			WriteInt file,g_scores(t)  Xor Rnd(-2147483648,2147483647)
			WriteInt file,g_new(t) Xor Rnd(-2147483648,2147483647)
						
	Next 
	CloseFile file 
	
End Function

Function encodelocalpw$(salt$,pw$)
	Return pw$
End Function

Function decodelocalpw$(salt$,pw$)
	Return pw$
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
	
	DebugLog "EncodeHSE$("+password$+","+mystring$+")=>"+string2
	Return string2$
End Function 


Function SaveScoreOnNet(hsh.hshandle,  score,name$, pw$, difficulty$,level$)

DebugLog "SaveScoreOnNet.Connecting for save action..."
tcp=OpenTCPStream( hsh\webaddress$,hsh\webport)

If Not tcp DebugLog "Failed.": Return False 

DebugLog  "Connected! Sending request..."

scorebook$=URLEncode(hsh\scorebook$)
password$=URLEncode(hsh\password$)
name$=URLEncode(name$)


param$="cmd=save&"
param$=param$+"scorebook="+hsh\scorebook$+"&request="

;encode score entry


entry$=entry$+"name="+StripTrailingSpaceChars(StripHeadingSpaceChars(name$))+"&"
entry$=entry$+"pw="+StripTrailingSpaceChars(StripHeadingSpaceChars(pw$))+"&"
entry$=entry$+"difficulty="+StripTrailingSpaceChars(StripHeadingSpaceChars(difficulty$))+"&"
entry$=entry$+"level="+StripTrailingSpaceChars(StripHeadingSpaceChars(level))+"&"
entry$=entry$+"score="+score+"&"

DebugLog "SaveScoreOnNet.UnEncodedEntry="+entry$

param$=param$+EncodeHSE(hsh\password$,entry$)



myString$="GET "+hsh\script$+"?"+param$+" HTTP/1.0"
DebugLog mystring$
WriteLine tcp,mystring$

myString$="Host: www.gamerprofile.info"
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
	If(myline$="ERROR") Then success=0
	If(myline$="UNAUTHORIZED") Then success=-1 

Wend

CloseTCPStream tcp
DebugLog "EOF"

If success=1 Then 
	DebugLog  "Success!!" 
	Return 1 
Else 
    DebugLog  "fail "+success+"!!" 
	Return success 
EndIf 

End Function 

Function UpdateScoreFromNet(hsh.hshandle)
	; OpenTCPStream/CloseTCPStream Example

g_hslatestcomment$=""

;script$="/test.php"
DebugLog "Connecting for getlist action... '"+hsh\webaddress$+"' :"+hsh\webport 
tcp=OpenTCPStream( hsh\webaddress$,hsh\webport)

If Not tcp DebugLog "Failed.": Return False 

DebugLog  "Connected! Sending request..."

scorebook$=URLEncode(hsh\scorebook$)
password$=URLEncode(hsh\password$)

If(g_distrversion <10 ) Then
	RuntimeError "Error, version constant 'g_distrversion' not defined. Please define it as an integer. 100 for example means 1.0.0"
	End 
EndIf 

param$="cmd=getlist&"
param$=param$+"scorebook="+hsh\scorebook$+"&"
param$=param$+"limit="+g_maxscores+"&"
param$=param$+"cmd2=getdistrversion"+"&"
param$=param$+"distrversion="+g_distrversion

myString$="GET "+hsh\script$+"?"+param$+" HTTP/1.0"
DebugLog mystring$
WriteLine tcp,mystring$

myString$="Host: www.gamerprofile.info"
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

DebugLog  "No EOF,Waiting For reply..."

;normal scores
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
	DebugLog ">'"+myline$+"'"
	If(myline$="END") Then 
		success=1
		Exit 
	Else

	If(index<g_maxscores) Then
	
		g_scores(index)=HSGetTagValue$(myline$,"score")
		g_difficulty(index)=HSGetTagValue$(myline$,"difficulty")
		g_level(index)=HSGetTagValue$(myline$,"level")
		g_names(index)=HSGetTagValue$(myline$,"name")
	 	
		;DebugLog "i("+index+") "+g_names(index)+","+g_scores(index)+","+g_difficulty(index)+","+g_level(index)+","
	EndIf
	INDEX=INDEX+1

	EndIf 
	
	For T=index To g_maxscores-1
		g_scores(t)=0
		g_difficulty(t)=2
		g_level(t)=1
		g_names(t)="nobody"
	Next
Wend

;"hero" scores
success=0
While Not Eof(tcp)
	myline$=ReadLine$( tcp )
	DebugLog "line:"+myline$
	If(myline$="HSTART") Then 
		success=1
		Exit 
	EndIf 
Wend

If(success=0) Then  DebugLog "Failed to find HSTART TAG":CloseTCPStream tcp: Return False 
DebugLog "HSTART tag found"

success=0
index=0
g_hsherocnt=0
g_boardfull=0

While Not Eof(tcp)

	myline$=ReadLine$( tcp )
	DebugLog ">'"+myline$+"'"
	If(myline$="HEND") Then 
		success=1
		Exit 
	Else

	If(index<g_maxscores) Then
	
		g_hscores(index)=HSGetTagValue$(myline$,"score")
		g_hdifficulty(index)=HSGetTagValue$(myline$,"difficulty")
		g_hlevel(index)=HSGetTagValue$(myline$,"level")
		g_hnames(index)=HSGetTagValue$(myline$,"name")
	 	g_hsherocnt=g_hsherocnt+1
		;DebugLog "g_hsherocnt="+g_hsherocnt
		;DebugLog "HHi("+index+") "+g_hnames(index)+","+g_hscores(index)+","+g_hdifficulty(index)+","+g_hlevel(index)+","
	EndIf
	INDEX=INDEX+1

	EndIf 
	
	For T=index To g_maxscores-1
		g_hscores(t)=0
		g_hdifficulty(t)=2
		g_hlevel(t)=1
		g_hnames(t)=""
	Next
Wend


;"comments" from the system
success=0
While Not Eof(tcp)
	myline$=ReadLine$( tcp )
	DebugLog "line:"+myline$
	If(myline$="CSTART") Then 
		success=1
		Exit 
	EndIf 
Wend

If(success=0) Then  DebugLog "Failed to find CSTART TAG":CloseTCPStream tcp: Return False 
DebugLog "CSTART tag found"

success=0
index=0

While Not Eof(tcp)

	myline$=ReadLine$( tcp )
	DebugLog ">'"+myline$+"'"
	If(myline$="CEND") Then 
		success=1
		Exit 
	Else

		g_hslatestcomment$=myline$
		DebugLog "Comment = "+g_hslatestcomment$
	EndIf 
	
Wend

;"board is full?" from the system
success=0
While Not Eof(tcp)
	myline$=ReadLine$( tcp )
	DebugLog "line:"+myline$
	If(myline$="BFSTART") Then 
		success=1
		Exit 
	EndIf 
Wend

If(success=0) Then  DebugLog "Failed to find BFSTARTTAG":CloseTCPStream tcp: Return False 
DebugLog "BFSTARTtag found"

success=0
index=0

While Not Eof(tcp)

	myline$=ReadLine$( tcp )
	DebugLog ">'"+myline$+"'"
	If(myline$="BFEND") Then 
		success=1
		Exit 
	Else

		g_boardfull=myline$
		DebugLog "Comment = "+g_hslatestcomment$
	EndIf 
	
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
		;DebugLog "mid="+str1$
		a=t
		If(c$=" " Or c$=Chr(09)) Then 
			;
		Else
			Exit 
		EndIf 	
	Next 
	;DebugLog "h t="+t+" "+str1$
	For t=a To Len(str1$)
		str2$=str2$+Mid(str1$,t,1)
		;DebugLog  "str2 = "+str2$ 
	Next 
	
	Return str2$
	
End Function 

Function StripTrailingSpaceChars$(str1$)

	str2$=""
	a=Len(str1$)
	For t=Len(str1) To 1 Step -1
		c$=Mid(str1$,t,1) 
		;DebugLog  "tmid="+str1$
		a=t
		If(c$=" " Or c$=Chr(09)) Then 
			;
		Else
			Exit 
		EndIf 	
	Next 
	;DebugLog "h t="+t+" "+str1$

	For t=a To 1 Step -1
		str2$=Mid(str1$,t,1)+str2$
		;DebugLog  "tstr2 = "+str2$ 
	Next 
	
	Return str2$
	
End Function 