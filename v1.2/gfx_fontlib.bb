
Type df_font
	Field picture
	Field w,h
	Field gfxsequence.imagesequence
End Type

Global df_font_current.df_font

Function InitFontDrawer.df_font(s$,w,h)

	If(w=-1 Or h=-1) Then
		pic=LoadImage(s$)
		If(pic=0) Then RuntimeError "Could not load font file "+s$
		
		iw=ImageWidth(pic)
		ih=ImageHeight(pic)
	 	FreeImage  pic
	
		w=iw/26
		h=ih/2
	EndIf 
	
	df_font_current=New df_font
	df_font_current\picture= LoadAnimImage(s$,w,h,0,52)		
	df_font_current\w=w
	df_font_current\h=h
	Return df_font_current 
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

; This creates the large image
; used to show credits and stuff.
Function DrawFont(x,y,Txt$,font.df_font)
	
	DrawFontCustomW(font\w,x,y,Txt$,font.df_font)
	
	
End Function


; This creates the large image
; used to show credits and stuff.
Function DrawFontCustomW(cw#,x,y,Txt$,font.df_font)
	
	textlen=Len(txt)
	For	cc = 0 To textlen - 1								
		cl=cc+1
		cr$ = Mid$(txt$,cl,1)					; Get letter from text array.
		crs = Asc(cr$)									; Get ascii.
		;Text 10,10+(10*cc), " asc="+crs
		If crs => 65									; Get letters and numbers.
			cra = crs - 65
		EndIf
		If crs => 48 And crs <= 57
			cra = crs - 22
		EndIf		
		Select cr$										; Special characters?
			Case "!"
				cra = 51
			Case "&"
				cra = 40
			Case "^"
				cra = 41
			Case "@"
				cra = 36
			Case "#"
				cra = 37
			Case "{"
				cra = 38
			Case "}"
				cra = 39
			Case "."
				cra = 42
			Case ","
				cra= 43
			Case ":"
				cra = 44
			Case "("
				cra = 45
			Case ")"
				cra = 46
			Case "?"
				cra = 49
			Case "-"
				cra = 47
			Case "%"
				cra = 50
			Case "*"
				cra = 48
		End Select
		If cr$ <> " "
			; Draw the letters 
			DrawImage font\picture ,x+(cl * cw) - cw,y,cra	
		EndIf
	Next
End Function