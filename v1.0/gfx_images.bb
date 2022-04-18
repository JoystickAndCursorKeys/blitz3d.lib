
Type imagesequence
	Field images
	Field image[g_maximagesinsequence]
	Field w
	Field h
End Type

Global g_imageloaddisplaytext=0
Global g_imageloaddisplaytexty=0
Global g_imageloaddisplaytextbgimage=0


Function FilterLight(picture,light,xX$)
	SetBuffer ImageBuffer(picture)
	w=ImageWidth(picture)
	h=ImageWidth(picture)
	For x=0 To w
		For y=0 To h
				GetColor(x,y)
				r=ColorRed()
				g=ColorGreen()
				b=ColorBlue()
				If((r+g+b)<225)
					If(light=0)  
						WritePixel(x,y,0)
					Else
						Color r,g,b
						Plot x,y
					EndIf
				Else 
					If(light=1)  
						WritePixel(x,y,0)
					Else
						Color r,g,b
						Plot x,y
					EndIf 
				EndIf 
		Next 
	Next
	DitherDarkStuff(picture,0,0,0,0)
	If(light=0) DitherMediumDarkStuff(picture)
	MaskImage picture,0,0,0
End Function 



Function DitherDarkStuff(picture,ri,gi,bi,M)
	SetBuffer ImageBuffer(picture)
	w=ImageWidth(picture)
	h=ImageWidth(picture)
	For x=0 To w-1
		For y=0 To h-1
			If (x+y) Mod 2 = m
				GetColor(x,y)
				r=ColorRed()
				g=ColorGreen()
				b=ColorBlue()
				If((r+g+b)<125)  
					Color ri,gi,bi
					Plot(x,y)
				EndIf 
			EndIf	 
		Next 
	Next
	
End Function 

Function DitherDarkStuffFast(picture)
	SetBuffer ImageBuffer(picture)
	w=ImageWidth(picture)
	h=ImageWidth(picture)
	For x=0 To w-1
		For y=0 To h-1
			If (x+y) Mod 2 = 1
				GetColor(x,y)
				r=ColorRed()
				g=ColorGreen()
				b=ColorBlue()
				If((r+g+b)<225)  
					WritePixel x,y,0
				EndIf 
			EndIf	 
		Next 
	Next
	
End Function 

Function RemoveDarkStuff(picture,mr,mg,mb)
	SetBuffer ImageBuffer(picture)
	w=ImageWidth(picture)
	h=ImageWidth(picture)
	Color mr,mg,mb
	For x=0 To w-1
		For y=0 To h-1
;			If (x+y) Mod 2 = 1
				GetColor(x,y)
				r=ColorRed()
				g=ColorGreen()
				b=ColorBlue()
				If((r+g+b)<425)  
					Plot  x,y
				EndIf 
;			EndIf	 
		Next 
	Next
	
End Function 


Function DitherMediumDarkStuff(picture)
	SetBuffer ImageBuffer(picture)
	w=ImageWidth(picture)
	h=ImageWidth(picture)
	For x=0 To w
		For y=0 To h
			If (x+y) Mod 2 = 1
				GetColor(x,y)
				r=ColorRed()
				g=ColorGreen()
				b=ColorBlue()
				If((r+g+b)<700)  
					WritePixel(x,y,0)
				EndIf 
			EndIf	 
		Next 
	Next
	
End Function 



Function SoftenEdges(picture,maskr,maskg,maskb,backr,backg,backb)
	SetBuffer ImageBuffer(picture)
	
	w=ImageWidth(picture)
	h=ImageHeight(picture)
	
	;edges first
	For x=1 To w-1
		For y=1 To h-1
			GetColor(x,y)
			r=ColorRed()
			g=ColorGreen()
			b=ColorBlue()

			If(r<>maskr Or g<>maskg Or b<>maskb)
			
				GetColor(x-1,y)
				r0=ColorRed()
				g0=ColorGreen()
				b0=ColorBlue()

				GetColor(x+1,y)
				r1=ColorRed()
				g1=ColorGreen()
				b1=ColorBlue()

				If( (r0=maskr And g0=maskg And b0=maskb) Or (r1=maskr And g1=maskg And b1=maskb) ) Then
					r=(r +  backr)   /2
					g=(g +  backg)   /2
					b=(b +  backb)   /2
				EndIf 	
	
				Color r,g,b
				Plot x,y
			EndIf 
			
		Next 
	Next

End Function


Function ChangeMaskColor(picture,srcr,srcg,srcb,dstr,dstg,dstb)
	SetBuffer ImageBuffer(picture)
	
	w=ImageWidth(picture)
	h=ImageHeight(picture)

	For x=0 To w-1
		For y=0 To h-1
			GetColor(x,y)
			r=ColorRed()
			g=ColorGreen()
			b=ColorBlue()

			If(r=srcr And g=srcg And b=srcb)
			Color dstr,dstg,dstb
			Plot x,y
			
			EndIf 
			
		Next 
	Next
	MaskImage picture, dstr,dstg,dstb
End Function

Function LoadImageSequence.imagesequence(prefix$,postfix$,count)
nis.imagesequence = New imagesequence
	For t = 0 To count-1
			
			filename$=prefix$

			If((t+1)<=9) Then 
				filename$=filename$+"000"+(t+1)+postfix$
			ElseIf((t+1)>9 And (t+1)<=99) 
				filename$=filename$+"00"+(t+1)+postfix$
			Else  
				filename$=filename$+"0"+(t+1)+postfix$
			EndIf
											
			nis\image[t]= LoadImage(filename$)
			If(nis\image[t]) RuntimeError "Load Error #0000080"
			
			HandleImage nis\image[t],  ImageWidth(nis\image[t])/2, ImageHeight(nis\image[t])/2
			MaskImage(nis\image[t],0,192,0) ;default mask color
			
	Next
nis\images=count

Return nis
End Function


Function LoadImageSequenceTransparent256.imagesequence(prefix$,postfix$,count)
nis.imagesequence = New imagesequence
	For t = 0 To count-1
			
			filename$=prefix$

			If((t+1)<=9) Then 
				filename$=filename$+"000"+(t+1)+postfix$
			ElseIf((t+1)>9 And (t+1)<=99) 
				filename$=filename$+"00"+(t+1)+postfix$
			Else  
				filename$=filename$+"0"+(t+1)+postfix$
			EndIf
											
			nis\image[t]= LoadImage(filename$)
			If(nis\image[t]) RuntimeError "Load Error #0000070"
			
			
			HandleImage nis\image[t],  ImageWidth(nis\image[t])/2, ImageHeight(nis\image[t])/2
			SetBuffer ImageBuffer(nis\image[t])
			GetColor(0,0)
			r=ColorRed()
			g=ColorGreen()
			b=ColorBlue()	
			SetBuffer BackBuffer()
		
			;DitherDarkStuffFast(nis\image[t])
			RemoveDarkStuff(nis\image[t],100,100,100)

			MaskImage(nis\image[t],r,g,b) ;default mask color
			
	Next
nis\images=count

Return nis
End Function

Function LoadImageSequence256.imagesequence(prefix$,postfix$,count)
nis.imagesequence = New imagesequence
	For t = 0 To count-1
			
			filename$=prefix$

			If((t+1)<=9) Then 
				filename$=filename$+"000"+(t+1)+postfix$
			ElseIf((t+1)>9 And (t+1)<=99) 
				filename$=filename$+"00"+(t+1)+postfix$
			Else  
				filename$=filename$+"0"+(t+1)+postfix$
			EndIf
							
			DebugLog "trying to load "+filename$
			nis\image[t]= LoadImage(filename$)
			If(nis\image[t]=0) RuntimeError "Load Error #0000060"			
			
			HandleImage nis\image[t],  ImageWidth(nis\image[t])/2, ImageHeight(nis\image[t])/2
			SetBuffer ImageBuffer(nis\image[t])
			GetColor(0,0)
			r=ColorRed()
			g=ColorGreen()
			b=ColorBlue()	
			SetBuffer BackBuffer()
		
			MaskImage(nis\image[t],r,g,b) ;default mask color
			
	
	Next
	nis\images=count

Return nis
End Function

Function LoadImageSequenceMicrSoft.imagesequence(prefix$,postfix$,count)
nis.imagesequence = New imagesequence

	SetBuffer FrontBuffer() 
	For t = 0 To count-1
			filename$=prefix$

			If((t+1)<=9) Then 
				filename$=filename$+"000"+(t+1)+postfix$
			ElseIf((t+1)>9 And (t+1)<=99) 
				filename$=filename$+"00"+(t+1)+postfix$
			Else  
				filename$=filename$+"0"+(t+1)+postfix$
			EndIf
											
			nis\image[t]= LoadImage(filename$)
			If(nis\image[t]) RuntimeError "Load Error #0000050"
			
			
			HandleImage nis\image[t],  ImageWidth(nis\image[t])/2, ImageHeight(nis\image[t])/2
			SetBuffer ImageBuffer(nis\image[t])
			GetColor(0,0)
			r=ColorRed()
			g=ColorGreen()
			b=ColorBlue()	
			SetBuffer BackBuffer()
		
			MaskImage(nis\image[t],r,g,b) ;default mask color
			
	Next

	nis\images=count

	Return nis
End Function

Function LoadImageMicrSoft(filename$)
											
			thisimage = LoadImage(filename$)
		

			If(thisimage ) RuntimeError "Load Error #0000040"
					
			HandleImage thisimage,  ImageWidth(thisimage)/2, ImageHeight(thisimage)/2
			SetBuffer ImageBuffer(thisimage)
			GetColor(0,0)
			r=ColorRed()
			g=ColorGreen()
			b=ColorBlue()	
			SetBuffer BackBuffer()
		
			MaskImage(thisimage,r,g,b) ;default mask color
			
			Return thisimage
			
End Function

Function FreeImageSequence(is.imagesequence)
	
	For t = 0 To is\images-1
			
			FreeImage is\image[t]
	Next

	Delete is
	
End Function


Function LoadImageStripSequence.imagesequence(filename$,count)
DebugLog filename$
nis.imagesequence = New imagesequence
strip=LoadImage(filename$)
If(strip=0) RuntimeError "Load Error #0000090"

h=ImageHeight(strip)
w=ImageWidth(strip)
frameh=h/count

DebugLog " h ="+ h + "count = "+ count + " frameh="+frameh
SetBuffer ImageBuffer(strip)
GetColor(0,0)
r=ColorRed()
g=ColorGreen()
b=ColorBlue()	

	For t = 0 To count-1

			displayimageloading(filename$+" : " + t)
			
			DebugLog "LI"+filename$+" : " + t
														
			nis\image[t]= CreateImage(w,frameh)
			If(nis\image[t]=0) RuntimeError "Load Error #0000091"
			
			SetBuffer ImageBuffer(nis\image[t])
			DrawBlockRect strip,0,0,0,t*frameh,w,frameh

			
			HandleImage nis\image[t],  ImageWidth(nis\image[t])/2, ImageHeight(nis\image[t])/2
	
			SetBuffer BackBuffer()
		
			MaskImage(nis\image[t],r,g,b) ;default mask color
			
		
	Next
	nis\images=count
	FreeImage strip	
	
	nis\h=ImageHeight(nis\image[0])
Return nis
End Function

Function LoadImageStripSequenceTransparent.imagesequence(filename$,count)

nis.imagesequence = New imagesequence
strip=LoadImage(filename$)
If(strip=0) RuntimeError "Load Error #0000100"

h=ImageHeight(strip)
w=ImageWidth(strip)
frameh=h/count
SetBuffer ImageBuffer(strip)
GetColor(0,0)
r=ColorRed()
g=ColorGreen()
b=ColorBlue()	

	For t = 0 To count-1

			displayimageloading(filename$+" : " + t)
			
			nis\image[t]= CreateImage(w,frameh)
			If(nis\image[t]=0) RuntimeError "Load Error #0000101"

			SetBuffer ImageBuffer(nis\image[t])
			DrawBlockRect strip,0,0,0,t*frameh,w,frameh

			
			HandleImage nis\image[t],  ImageWidth(nis\image[t])/2, ImageHeight(nis\image[t])/2
	
			SetBuffer BackBuffer()

			DitherDarkStuff(nis\image[t],r,g,b,t Mod 2)		
			MaskImage(nis\image[t],r,g,b) ;default mask color
			
		
	Next
	nis\images=count
	FreeImage strip	
Return nis

End Function