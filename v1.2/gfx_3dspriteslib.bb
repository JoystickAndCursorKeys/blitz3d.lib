
Type sprites
Field active
Field x#
Field y#
Field dx#
Field dy#
Field entity
Field width#
Field height#
Field alpha#
Field alphafactor#
Field sizefactor#
Field rotateangle#
Field speedfactor#
Field rotate#
Field flicker
Field prio
End Type

Dim sprite.sprites(1)

Global Max3DSprites = 1

Function Init3DSprites(maxsprites)

	DebugLog "Function Init3DSprites"
	;delete existing sprites
	For n = 0 To Max3DSprites
		If sprite(n) <> Null
			FreeEntity sprite(n)\entity
			Delete sprite(n)
		End If
	Next
	
	Max3DSprites = maxsprites
	Dim sprite.sprites(maxsprites)

	;create new sprites
	For n = 0 To maxsprites
		sprite(n) = New sprites
		sprite(n)\entity = CreateSprite()
		sprite(n)\active = 0
		HideEntity sprite(n)\entity
		;DebugLog ("CreateSprite")
	Next
	
End Function

Function DebugGetNumberOfFree3DSprites()

	x=0

	For n = 0 To Max3DSprites
		If sprite(n) <> Null
			If sprite(n)\active=0 Then	x=x+1			
		End If
	Next
	Return x
End Function

Function MoveAndAnimate3DSprites(xoff,yoff)

	z# = 1

	;delete existing sprites
	For n = 0 To Max3DSprites
		
		If sprite(n) <> Null
		
			
			If sprite(n)\active<>0 Then
			
				;DebugLog ("MoveAndAnimate3DSprites"+n)
				
				sprite(n)\alpha#=sprite(n)\alpha#*sprite(n)\alphafactor#
				sprite(n)\rotate#=sprite(n)\rotate#+sprite(n)\rotateangle#
				
				sprite(n)\width= sprite(n)\width#*sprite(n)\sizefactor#
				sprite(n)\height= sprite(n)\height#*sprite(n)\sizefactor#
				
				
				sprite(n)\y=sprite(n)\y+sprite(n)\dy
				sprite(n)\x=sprite(n)\x+sprite(n)\dx

				sprite(n)\dy=sprite(n)\dy*sprite(n)\speedfactor
				sprite(n)\dx=sprite(n)\dx*sprite(n)\speedfactor
				
				
				;DebugLog "XX"+sprite(n)\x + " " + sprite(n)\width
				;DebugLog "YY"+sprite(n)\y + " " + sprite(n)\height
				outofscreen= sprite(n)\x  < -sprite(n)\width Or  sprite(n)\y  < -sprite(n)\height Or 	sprite(n)\x  > g_width+sprite(n)\width Or  sprite(n)\y  > sprite(n)\height + g_height
				
				;DebugLog "outofscreen"+outofscreen
				
				If(sprite(n)\alpha#<.01 Or sprite(n)\width<2 Or sprite(n)\height<2) Or outofscreen Then
					HideEntity sprite(n)\entity
					sprite(n)\active = 0
					;DebugLog "hide sprite " + n
				Else
					ScaleSprite sprite(n)\entity,(z#/g_width)*sprite(n)\width,(z#/g_width)*sprite(n)\height
					
					If(sprite(n)\flicker=1) Then 
						EntityAlpha sprite(n)\entity,Rnd(0,sprite(n)\alpha#)
					Else
						EntityAlpha sprite(n)\entity,sprite(n)\alpha#	
					EndIf  					
					
					tx#=(z*2/g_width)*(sprite(n)\x+xoff)
					tx=tx-((z/g_width)*g_width)
						
					ty#=(z*2/g_width)*-(sprite(n)\y+yoff)
					ty=ty+(z/g_width)*(g_height)
	
					PositionEntity sprite(n)\entity,tx,ty,z
					RotateSprite sprite(n)\entity,sprite(n)\rotate#
				EndIf
			EndIf 
		End If
	Next
	
End Function


Function Set3DSpriteVelocity(n,dx#,dy#)
				If(n=-1) Return
				sprite(n)\dy=dy#
				sprite(n)\dx=dx#
End Function

Function Set3DSpriteBlowup(n,b#)
				If(n=-1) Return
				sprite(n)\sizefactor#=b#
End Function

Function Set3DSpriteFade(n,f#)
				If(n=-1) Return
				sprite(n)\alphafactor#=f#
End Function

Function Set3DSpriteRotate(n,r#)
				If(n=-1) Return
				sprite(n)\rotateangle#=r#
End Function

Function Set3DSpriteBlink(n,b)
				If(n=-1) Return
				sprite(n)\flicker=b
End Function

Function Set3DSpriteVelocityIncrease(n,b#)
				If(n=-1) Return
				sprite(n)\speedfactor#=b#
End Function


Function New3DSprite(x,y,width,height,texture1,texture2,frame,alpha#,angle#,prio)
	z# = 1
	;DebugLog "Function New3DSprite"

	;find a free sprite slot
	For n = 0 To Max3DSprites
		If sprite(n) <> Null
			If sprite(n)\active = 0 

				;Cls 
				;Text 10,50," n = "+n
				;Text 10,60," x = "+x
				;Text 10,70," y = "+y
				;Text 10,80," width = "+width
				;Text 10,90," height = "+height																
				;Flip 
				;Stop 			
				;found free slot
				;setup sprite
				sprite(n)\active = 1
				sprite(n)\flicker = 0
				sprite(n)\width = width
				sprite(n)\height = height
				sprite(n)\alpha#  = alpha#
				sprite(n)\rotate#=angle
				;prite(n)\x
				sprite(n)\y=y; - height/2
				sprite(n)\x=x; - width/2
				sprite(n)\dy=0
				sprite(n)\dx=0
				sprite(n)\alphafactor=1
				sprite(n)\sizefactor=1
				sprite(n)\rotateangle#=0
				sprite(n)\prio=prio
				sprite(n)\speedfactor#=1

				
				
				
				If(texture2 <> 0)   EntityTexture sprite(n)\entity,texture2,frame
				If(texture1 <> 0) 	EntityTexture sprite(n)\entity,texture1,frame
				
				
				ScaleSprite sprite(n)\entity,(z/g_width)*width,(z/g_width)*height
				
				tx#=(z*2/g_width)*x
				tx=tx-((z/g_width)*g_width)
				
				ty#=(z*2/g_width)*-y
				ty=ty+(z/g_width)*(g_height)


				PositionEntity sprite(n)\entity,tx,ty,z
				EntityAlpha sprite(n)\entity,alpha#
				RotateSprite sprite(n)\entity,angle
				ShowEntity sprite(n)\entity
				Return n
			End If
		End If
	Next
	
	If(prio=0) 	Return -1
	
	For n = 0 To Max3DSprites
		If sprite(n) <> Null
			If sprite(n)\prio<prio

				
				;hostile takeover of sprite
				sprite(n)\active = 1
				sprite(n)\width = width
				sprite(n)\height = height
				sprite(n)\alpha#  = alpha#
				sprite(n)\rotate#=angle
				;prite(n)\x
				sprite(n)\y=y; - height/2
				sprite(n)\x=x; - width/2
				sprite(n)\dy=0
				sprite(n)\dx=0
				sprite(n)\alphafactor=1
				sprite(n)\sizefactor=1
				sprite(n)\rotateangle#=0

				
				
				
				If(texture2 <> 0)   EntityTexture sprite(n)\entity,texture2,frame
				If(texture1 <> 0) 	EntityTexture sprite(n)\entity,texture1,frame
				
				
				ScaleSprite sprite(n)\entity,(z/g_width)*width,(z/g_width)*height
				
				tx#=(z*2/g_width)*x
				tx=tx-((z/g_width)*g_width)
				
				ty#=(z*2/g_width)*-y
				ty=ty+(z/g_width)*(g_height)


				PositionEntity sprite(n)\entity,tx,ty,z
				EntityAlpha sprite(n)\entity,alpha#
				RotateSprite sprite(n)\entity,angle
				ShowEntity sprite(n)\entity
				Return n
			End If
		End If
	Next

End Function


Function Move3DSprite(number,x,y)
	z# = 1

	tx#=(z*2/g_width)*x
	tx=tx-((z/g_width)*g_width)
				
	ty#=(z*2/g_width)*-y
	ty=ty+(z/g_width)*(g_height)
	
	PositionEntity sprite(number)\entity,tx,ty,z

End Function


Function Free3DSprite(number)
    If number=-1 Return 
	If sprite(number) <> Null
		If sprite(number)\active = 1
			HideEntity sprite(number)\entity
			sprite(number)\active = 0
		End If
	End If
End Function

Function FreeSome3DSprites(min)

	n=0
	c=0
	While c<min And n<Max3DSprites
		If(sprite(n)<>Null) Then
			sprite(n)\active = 0
			HideEntity sprite(n)\entity
			c=c+1
		EndIf 
		n=n+1
	Wend 
	
End Function

Function FreeAll3DSprites()

	n=0

	While n<Max3DSprites
		If(sprite(n)<>Null) Then
			sprite(n)\active = 0
			HideEntity sprite(n)\entity
		EndIf 
		n=n+1
	Wend 
	
End Function


Function Hide3DSprites()

	n=0

	While n<Max3DSprites
		If(sprite(n)<>Null) Then
			If(sprite(n)\active<>0) Then
				HideEntity sprite(n)\entity
			EndIf 
		EndIf 
		n=n+1
	Wend 
	
End Function

Function UnHide3DSprites()

	n=0

	While n<Max3DSprites
		If(sprite(n)<>Null) Then
			If(sprite(n)\active<>0) Then
				ShowEntity sprite(n)\entity
			EndIf 
		EndIf 
		n=n+1
	Wend 
	
End Function

