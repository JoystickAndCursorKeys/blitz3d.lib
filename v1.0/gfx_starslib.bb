; stars library

; (c) Graham Kennedy 2001
; Rewritten by D.Murray


Type star

	Field d#, a#
	Field x#,y#,z#
	Field layer, coll
	
End Type

Global g_stardx#
Global g_stardy#
Global g_stardz#
Global g_starsviewangle#

Global g_starspeed#, g_maxZ#
Global 	g_xoffset#,g_yoffset#
Global 	g_starminx#
Global 	g_starminy#
Global 	g_starmaxx#
Global 	g_starmaxy#
	
Global 	g_starviewx0
Global 	g_starviewx1
Global 	g_starviewy0
Global 	g_starviewy1

Dim g_starsbgbuffer(255)
Global g_starsbgbuffercnt=0

Dim g_starscolors(255)



Function StarsSetBGModePicture(pic,cnt)

	DebugLog "StarsSetBGModePicture(inside) = "+pic+ "  " + cnt
	t=0
	While t<cnt
		g_starsbgbuffer(t)=ImageBuffer(pic,t)
		t=t+1
	Wend 
	g_starsbgbuffercnt=cnt
	
End Function 

Function StarsSetBGModeNormal()

	g_starsbgbuffercnt=0
	
End Function 

Function Stars_Add(x,y,z,layer)
	
		
	;add star
	s.star = New star
	
	s\x = x
	s\y = y
	s\z = z
	s\layer = layer
	
	div=s\layer
	
	dvz=(div /3)
	If(dvz=0) dvz=1
	
	c = 255/ dvz

	s\coll=g_starscolors(c)
	
	

End Function

Function Stars_Add3D()
	s.star = New star
	
	s\x = x
	s\y = y
	s\z = z
	s\layer = 1
	
	div=s\layer

	s\z = Rnd(0,g_maxZ#)
	s\x = Rnd(g_starminx,g_starmaxx)
	s\y = Rnd(g_starminy,g_starmaxy)
			
	s\coll=255

End Function

Function Stars_Add3Dr()
	s.star = New star
	
	s\x = x
	s\y = y
	s\z = z
	s\layer = 1
	
	div=s\layer

	s\z = Rnd(0,g_maxZ#)
	s\d = Rnd(g_starmaxx)
	s\a = Rnd(0,360)
			
	s\coll=255

End Function


Function Stars_Randomize3D()
	
	For s.star = Each star

		s\x = x
		s\y = y
		s\z = z
		s\layer = 1
	
		div=s\layer

		s\z = Rnd(0,g_maxZ#)
		s\x = Rnd(g_starminx,g_starmaxx)
		s\y = Rnd(g_starminy,g_starmaxy)
				
		s\coll=255
	
	Next 

End Function


Function StarsSetView(x0,x1,y0,y1)

	g_starviewx0=x0
	g_starviewx1=x1
	g_starviewy0=y0
	g_starviewy1=y1
	
End Function

Function StarsSetZSpace(maxz#)  
	g_maxZ#=maxz 
End Function

Function StarsSetXYOffsetSpace(x#,y#)  
	g_xoffset#=x#
	g_yoffset#=y#
	
End Function


Function StarsSetXYRange(minx,maxx,miny,maxy) 
	g_starminx=minx
	g_starminy=miny
	g_starmaxx=maxx
	g_starmaxy=maxy

End Function

Function Stars_Move(dx#,dy#)

	g_starspeed=dy
	For s.star = Each star
		
		s\y = s\y + ((dy*2) / s\layer)
		
		
		If s\y < 0 Then 
			s\y = s\y + g_height
			s\x = Rnd(g_width-1)
			
		End If
		
		If Int(s\y) >= g_height Then 
			s\y = s\y - g_height
			
			s\x = Rnd(g_width-1)
			
		End If
		
	Next
End Function

Function Stars_Move3D(dx#,dy#,dz#)

	;g_starspeed=dy
	
	g_stardx=dx
	g_stardz=dz
	g_stardy=dy
	
	For s.star = Each star
		
		;s\x = s\x + (dx / s\layer)
		
		s\z = s\z + dz ;((dz*2) / s\layer)
		s\x = s\x + dx
		s\y = s\y + dy

	
		
		If s\z < 0 Then 
			s\z = s\z + g_maxZ#
			s\x = Rnd(g_starminx,g_starmaxx)
			s\y = Rnd(g_starminy,g_starmaxy)

		EndIf
		
		If s\z > g_maxZ# Then 
			s\z = 0
			s\x = Rnd(g_starminx,g_starmaxx)
			s\y = Rnd(g_starminy,g_starmaxy)

		EndIf
			
				
		If s\x < g_starminx Then 
			s\x = g_starmaxx-1
		ElseIf s\x >= g_starmaxx 
			s\x = g_starminx
		EndIf 

		If s\y < g_starminy Then 
			s\y = g_starmaxy-1
		ElseIf s\y >= g_starmaxy 
			s\y = g_starminy
		EndIf 
				
	Next
End Function



Function Stars_Move3Dr(da#,dz#)

	;g_starspeed=dy
	
	
	g_stardx=dx
	g_stardz=dz
	g_stardy=dy
	
	For s.star = Each star
		
		;s\x = s\x + (dx / s\layer)
	
		
		
		s\a = s\a + da;((dz*2) / s\layer)
	
		s\z = s\z + dz

	
		s\x=Cos(s\a) * s\d
		s\y=Sin(s\a) * s\d

		
		If s\z < 0 Then 
			s\z = s\z + g_maxZ#
			s\x = Rnd(g_starminx,g_starmaxx)
			s\y = Rnd(g_starminy,g_starmaxy)

		EndIf
		
		If s\z > g_maxZ# Then 
			s\z = 0
			s\x = Rnd(g_starminx,g_starmaxx)
			s\y = Rnd(g_starminy,g_starmaxy)

		EndIf
			
				
	Next
End Function


Function Stars_Show()

		
		LockBuffer GraphicsBuffer()
		
		;DebugLog "buffer om showstarts" + buffer
		
		dy=g_starspeed
		If(dy<1) dy=1
		
		If(g_starsbgbuffercnt=0) Then

			
			For s.star = Each star
					xx=s\x
					yy=s\y
			
					;DebugLog xx+"+"+yy
					
					;DebugLog "b"+g_starviewx1+"+"+g_starviewy1
					
					If(xx<g_starviewx1 And xx>g_starviewx0 And yy<g_starviewy1 And yy>g_starviewy0) Then
											
						WritePixelFast xx,yy,s\coll
						
						;WaitKey 
						
					EndIf 	
			Next 
			
			

		
		Else
		
			LockBuffer g_starsbgbuffer(0)
			
			For s.star = Each star
					xx=s\x
					yy=s\y
			
					If(xx<g_starviewx1 And xx>g_starviewx0 And yy<g_starviewy1 And yy>g_starviewy0) Then
					
						mycol=ReadPixelFast   (xx/4,yy/4,g_starsbgbuffer(0))
						WritePixelFast xx,yy,mycol
						
					EndIf 	
			Next 
			
			UnlockBuffer g_starsbgbuffer(0)
			
		EndIf 
		
		
		UnlockBuffer GraphicsBuffer()
		
End Function

Function Stars_Show3D()
		
		LockBuffer GraphicsBuffer()
		
		t=0
		While t<g_starsbgbuffercnt
			LockBuffer g_starsbgbuffer(t)
			t=t+1
		Wend 
		;DebugLog "buffer om showstarts" + buffer
		
		If(g_starsbgbuffercnt>0) Then 
			For s.star = Each star
				
				xx#=s\x
				yy#=s\y
				zz#=s\z
				
				xxStep#=g_stardx/3
				yyStep#=g_stardy/3
				zzStep#=g_stardz/3
				
				
				buf#= (zz# / g_maxZ# ) ;/ g_maxZ#
				buf2#= buf * (g_starsbgbuffercnt-1)
				bufi=Int(buf2#)
				
				For trail=1 To 2
				
					xx#=xx#+xxStep
					yy#=yy#+yyStep
					zz#=zz#+zzStep
	
					z#=zz#/10
					
					x#=(xx#/z#)
					y#=(yy#/z#)
					
					x#=x# + g_xoffset
					y#=y# + g_yoffset
				
				
					ix=Int(x)
					iy=Int(y)
				
					If(ix<g_starviewx1 And ix>g_starviewx0 And iy<g_starviewy1 And iy>g_starviewy0) Then
			
						ib=bufi
						
						mycol=ReadPixelFast   (ix/4,iy/4,g_starsbgbuffer(ib))
						WritePixelFast ix,iy,mycol
	
					EndIf 
				Next 
				
			
			Next
		Else
			For s.star = Each star
				
				xx#=s\x
				yy#=s\y
				zz#=s\z
				
				xxStep#=g_stardx/3
				yyStep#=g_stardy/3
				zzStep#=g_stardz/3
				
				
				col#= (zz# / g_maxZ# ) ;/ g_maxZ#
				col2#= col * (255-1)
				coli=Int(col2#)
				
				For trail=1 To 2
				
					xx#=xx#+xxStep
					yy#=yy#+yyStep
					zz#=zz#+zzStep
	
					z#=zz#/10
					
					x#=(xx#/z#)
					y#=(yy#/z#)
					
					x#=x# + g_xoffset
					y#=y# + g_yoffset
				
				
					ix=Int(x)
					iy=Int(y)
				
					If(ix<g_starviewx1 And ix>g_starviewx0 And iy<g_starviewy1 And iy>g_starviewy0) Then
			
						;ib=bufi
						
						;mycol=ReadPixelFast   (ix/4,iy/4,g_starsbgbuffer(ib))
						WritePixelFast ix,iy,g_starscolors(coli)
	
					EndIf 
				Next 
				
			
			Next		
		EndIf 
			
		t=0
		While t<g_starsbgbuffercnt
			UnlockBuffer g_starsbgbuffer(t)
			t=t+1
		Wend 
		UnlockBuffer GraphicsBuffer()		
	
End Function

	
Function Stars_Hide()
	LockBuffer BackBuffer()
	
	For s.star = Each star
		r = 0
		g = 0
		b = 0
	
		WritePixelFast s\x,s\y,0
	
	Next 
	
	UnlockBuffer BackBuffer()
End Function
	
	
Function Stars_Init()

	;cleanup
	Stars_Cleanup()

	;make color array
	; remember pixel and buffer
	; borrow pixel and buffer, to find color value
	oldcolor=ReadPixel(0,0,BackBuffer())
	oldbuffer= GraphicsBuffer()
	SetBuffer BackBuffer()
		
	For t=0 To 255
		c=255-t
		Color c,c,c
		Plot 0,0
		g_starscolors(t)=ReadPixel (0,0,BackBuffer ())
	Next 
	
	; restore buffer and pixel
	WritePixel 0,0,oldcolor,BackBuffer()
	SetBuffer oldbuffer


End Function

Function Stars_Cleanup()

	Delete Each star

End Function