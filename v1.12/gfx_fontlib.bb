;
; Version 1.12 of gfx_fontlib.bb (www.lodgia.com (D.Murray)
; Free to use in any Blitz program. Free to dsitribute, but please do mention the author,
;  and don't present this library as your own. (basically LGPL)
;  If you make any modifications to this file, add comments in this header, with name (your) and 
;  (library) version
;

Const df_type_26x2=0
Const df_type_26x4=1

Type df_chardata

	Field w,l,r

End Type 

Type df_font
	Field picture
	Field w,h
	Field ftype
	Field chrcnt
	Field chardata.df_chardata[104]
	Field charmap[256]
	Field spacing 
End Type

Global df_font_current.df_font


Function InitBitMapFont.df_font(s$)

	 
    filein = ReadFile(s$+".font")

	If(filein=0) Return Null 
	
	df_font_current=New df_font

    	While(Not(Eof (filein)))
	txt$=ReadLine (filein)
	If(txt$<>"") Then
		If(Mid$(txt$,1,1)<>"#") Then 
			name$=fli_split1(txt$):value$=fli_split2(txt$)
			If(name$="bitmap.fileformat") Then imgformat$=value$
			If(name$="bitmap.width") Then bmw=value$
			If(name$="bitmap.height") Then bmh=value$
			If(name$="chars.cellwidth") Then cellw=value$
			If(name$="chars.cellheight") Then cellh=value$		
			If(name$="chars.cnt") Then 
				cellcnt=value$
				For t=0 To cellcnt-1
					df_font_current\chardata[t]=New df_chardata
				Next  
			EndIf 
			If(name$="bitmap.layout") Then bmplayout1$=value$	
			If(name$="chars.proportional.widths") Then 
				For t=0 To cellcnt-1
					w=fli_getsubvalue(value$,t)
					df_font_current\chardata[t]\w=w
				Next 
			EndIf 
			If(name$="chars.proportional.relcoords.lefts") Then 
				For t=0 To cellcnt-1
					l=fli_getsubvalue(value$,t)	
					df_font_current\chardata[t]\l=l
				Next 
			EndIf 		
			If(name$="chars.proportional.relcoords.rights") Then 
				For t=0 To cellcnt-1
					r=fli_getsubvalue(value$,t)	
					df_font_current\chardata[t]\r=r
				Next 
			EndIf 			
		EndIf 
	EndIf 
       Wend  
	CloseFile(filein)
	
	df_font_current\picture= LoadAnimImage(s$+"."+imgformat$,cellw,cellh,0,cellcnt)		
	df_font_current\w=cellw
	df_font_current\h=cellh
	bmplayout=df_type_26x2
	If(bmplayout1$="26x4") Then 
			bmplayout=df_type_26x4
	EndIf 

	df_font_current\chrcnt=cellcnt
	df_font_current\ftype=bmplayout
	df_font_current\spacing=4

	fli_setCharMap(df_font_current,bmplayout1$)
	
	Return df_font_current 
	
End Function


Function ChangeFontSpacing(font.df_font,n)

	font\spacing=n

End Function

Function DrawFontCenter(y,Txt$,font.df_font)
	x=GraphicsWidth()/2 - ((Len(txt)*font\w)/2)
	DrawFont(x,y,Txt$,font)
End Function

Function DrawFontCenter2(w,y,Txt$,font.df_font)
	x=w/2 - ((Len(txt)*font\w)/2)
	DrawFont(x,y,Txt$,font)
End Function

Function DrawFontCenter3(cw#,y,Txt$,font.df_font)
	x=GraphicsWidth()/2 - ((Len(txt)*cw#)/2)
	DrawFontCustomW(cw#,x,y,Txt$,font)
End Function

Function DrawFont(x,y,Txt$,font.df_font)
	
	DrawFontCustomW(font\w,x,y,Txt$,font.df_font)
	
End Function

Function DrawChar(x,y,c,font.df_font)

	If(c<>32) Then 
			
        cra=font\charmap[c];
		If(cra=-1)  Then 
			Return -2
		Else 
			
			w=font\chardata[cra]\w
			xoff=font\chardata[cra]\l

			DrawImage font\picture ,x-xoff,y,cra
			
			Return w+font\spacing 
			
		EndIf 
	
	Else
		
		Return ((font\w)*3)/7
		
	EndIf 
		
End Function 


Function DrawFontCustomW(cw#,x,y,Txt$,font.df_font)
		
	
	textlen=Len(txt)
	x0=x
	For	cc = 0 To textlen - 1								
		
		c=Asc(Mid$(txt,cc+1,1))
		rv=DrawChar(x0,y,c,font)
		If(rv>0) Then
			x0=x0+rv
		EndIf 
	Next
	
End Function


Function fli_split1$(txt$)

	name$=""
	offset=Instr(txt$,"=",1)
	
	If(offset>0) Then
		
		name$=Mid$(txt$,1,offset-1)
			
	EndIf 

	Return name$
End Function 

Function fli_split2$(txt$)

	value$=""
	offset=Instr(txt$,"=",1)
	
	If(offset>0) Then
		
		value$=Mid$(txt$,offset+1,Len(txt$))
			
	EndIf 

	Return value$
End Function 


Function  fli_getsubvalue$(s$,idx)

	t=0;
	l=Len(s);
	ccnt=0
	start=0
	End_=0;
	temp$=""

	t=0;
	While(t<l And ccnt<idx)
	
		If(Mid(s,t+1,1)=",") Then 
		
			ccnt=ccnt+1
		EndIf 
		t=t+1
	Wend 
	
	start=t
	While(t<l)
	
		If(Mid(s,t+1,1)=",") Then 
		
			Exit 
		EndIf 
		t=t+1 
	Wend 
	End_=t;

	temp=Mid(s,start+1,end_-start)

	Return temp
	
End Function 

Function fli_setCharMap(f.df_font,maptype$)

	t=0;
	While(t<256)
	
		f\charmap[t]=-1;
		t=t+1;
		
	Wend 

	 
	If(maptype="26x2") Then 
	
		t=0:While(t<26) ;//alphabetic characters (Upper And Lower map To the same char)
		
		asc1=Asc("A")+t;
			asc2=Asc("a")+t;

			f\charmap[asc1]=t;
			f\charmap[asc2]=t;
			t=t+1
		Wend 

		t=26:While(t<=36) ;//num characters 0-9
		
		
			asc1=Asc("0") + (t-26);
			f\charmap[asc1]=t;
			t=t+1
		Wend 

		f\charmap[Asc("@")]=36+1;
		f\charmap[Asc("&")]=37+1;
		f\charmap[Asc("*")]=38+1;
		f\charmap[Asc("+")]=39+1;
		f\charmap[Asc("/")]=40+1;
		f\charmap[Asc("\")]=40+1;
		f\charmap[Asc("|")]=40+1;
		f\charmap[Asc("-")]=41+1;
		f\charmap[Asc("_")]=41+1;
		f\charmap[Asc(".")]=42+1;
		f\charmap[Asc(",")]=43+1;
		f\charmap[Asc(":")]=44+1;
		f\charmap[Asc("(")]=45+1;
		f\charmap[Asc(")")]=46+1;
		f\charmap[Asc("[")]=45+1;
		f\charmap[Asc("]")]=46+1;
		f\charmap[Asc("{")]=45+1;
		f\charmap[Asc("}")]=46+1;
		f\charmap[Asc("-")]=47+1;
		f\charmap[Asc("#")]=48+1;
		f\charmap[Asc("?")]=49+1;
		f\charmap[Asc("%")]=50+1;
		f\charmap[Asc("!")]=51+1;

	
	ElseIf(maptype="26x4") Then 
	

		t=0:While(t<26) ;//alphabetic characters (Upper And Lower map To the same char)
		
			asc1=Asc("A") +t;
			asc2=Asc("a") +t;

			f\charmap[asc1]=t;
			f\charmap[asc2]=t+26;

			t=t+1
		Wend 

		t=52:While(t<=62) ; //num characters 0-9
		
			asc1=Asc("0")+(t-52);
			f\charmap[asc1]=t;
			t=t+1
		Wend 

		f\charmap[Asc("!")]=62;
		f\charmap[34]=63         ;" sign
		f\charmap[Asc("#")]=64;
		f\charmap[Asc("$")]=65;
		f\charmap[Asc("%")]=66;
		f\charmap[Asc("&")]=67;
		f\charmap[Asc("`")]=68;
		f\charmap[Asc("(")]=69;
		f\charmap[Asc(")")]=70;
		f\charmap[Asc("*")]=71;
		f\charmap[Asc("+")]=72;
		f\charmap[Asc("'")]=73;
		f\charmap[Asc("-")]=74;
		f\charmap[Asc(".")]=75;
		f\charmap[Asc("/")]=76;
		f\charmap[Asc(":")]=77;
		f\charmap[Asc(";")]=78;
		f\charmap[Asc("<")]=79;
		f\charmap[Asc("=")]=80;
		f\charmap[Asc(">")]=81;
		f\charmap[Asc("?")]=82;
		f\charmap[Asc("@")]=83;
		f\charmap[Asc("[")]=84;
		f\charmap[Asc("\")]=85;
		f\charmap[Asc("]")]=86;
		f\charmap[Asc("^")]=87;
		f\charmap[Asc("_")]=88;
		f\charmap[Asc("`")]=89;
		f\charmap[Asc("{")]=90;
		f\charmap[Asc("|")]=91;
		f\charmap[Asc("}")]=92;
		f\charmap[Asc("~")]=93;
		f\charmap[Asc(",")]=94;

		;//---------------------------------------------

	EndIf 

End Function 
