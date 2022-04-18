Function ParticleFuzz(X,Y,speed,scatter,texture,nofparticles,fadeout)
	
		speed2#=.0
		For i = 1 To nofparticles
;
		lm1#=Rnd(10)
		lmf1#=(lm1#*.01)+.9
		speed2#= Rnd(speed,speed*3)
		speed2=speed2 * .1 

		Angle#= Rnd(360);
		
		
		dx#=Cos(Angle) * speed2
		dy#=Sin(Angle) * speed2
		
		;UseParticle(X+dx,Y+dy,dx,dy,.97,col,scatter/2)
		;DebugLog "ParticleFuzz "+g_smoketexture

		n=New3DSprite(X+dx,Y+dy,(scatter*3),(scatter*3),texture,0,0,1,Rnd(360),0)
		Set3DSpriteVelocity(n,dx#,dy#)
		Set3DSpriteFade(n,.99)
		Set3DSpriteBlowup(n,1.005)

		
	Next	
End Function

Function ParticleFuzz2(X,Y,dx#,dy#,scatter,texture,nofparticles,fadeout)
	
		speed2#=.0
		For i = 1 To nofparticles
;
		lm1#=Rnd(10)
		lmf1#=(lm1#*.01)+.9
		;speed2#= Rnd(speed,speed*3)
		;speed2=speed2 * .1 

		;Angle#= Rnd(360);
		
		
		;dx#=Cos(Angle) * speed2
		;dy#=Sin(Angle) * speed2
		
		;UseParticle(X+dx,Y+dy,dx,dy,.97,col,scatter/2)
		;DebugLog "ParticleFuzz "+g_smoketexture

		n=New3DSprite(X+dx,Y+dy,(scatter*3),(scatter*3),texture,0,0,.2,Rnd(360),0)
		Set3DSpriteVelocity(n,dx#,dy#)
		Set3DSpriteFade(n,.99)
		Set3DSpriteBlowup(n,1.005)

		
	Next	
End Function

Function AsteroidSparcs(X,Y)
	ParticleSparcs(X,Y,3,4,3,.98,fb_blue)
	ParticleFuzz(X,Y,2,20,g_smoketexture(0),20,.98)
	;DebugLog "g_smoketexture(0) "+g_smoketexture(0)

End Function

Function Smoke(X,Y,dx,dy)
	;ParticleSparcs(X,Y,3,4,3,.98,fb_blue)
	ParticleFuzz2(X,Y,dx,dy,4,g_smoketexture(0),20,.78)
	;ParticleFuzz2
	;DebugLog "g_smoketexture(0) "+g_smoketexture(0)

End Function


Function NormalExplosion(X,Y,dx#,dy#)

fadeout#=.89
scatter=20
speed=20
nofparticles=40
;n=New3DSprite(x,y,(100*fadeout),(100*fadeout),g_scattertexture(0),0,0,1,Rnd(360),0)

;Set3DSpriteVelocity(n,dx,dy)
;Set3DSpriteFade(n,.96)
;Set3DSpriteBlowup(n,1.01)

ParticleSparcs(X,Y,speed,scatter,nofparticles,fadeout#,0)
ParticleSparcs(X,Y,speed*2,scatter,nofparticles,.95,0)

ParticleFuzz(X,Y,5,100,g_smoketexture(0),5,.98)


End Function

Function NormalExplosionBlue(X,Y,dx#,dy#)

fadeout#=.89
scatter=20
speed=20
nofparticles=20
;n=New3DSprite(x,y,(100*fadeout),(100*fadeout),g_scattertexture(0),0,0,1,Rnd(360),0)

;Set3DSpriteVelocity(n,dx,dy)
;Set3DSpriteFade(n,.96)
;Set3DSpriteBlowup(n,1.01)

;ParticleSparcs(X,Y,speed,scatter,nofparticles,fadeout#,fb_blue)
	
End Function

Function TinyExplosion(X,Y,dx#,dy#,col)



speed=10
nofparticles=40


For t2=1 To 10

	t=t2/2
	range=t2*10
	
	dist=0;t2
	scatter=Rand(5,8)

	angle=Rand(360)
	dist=Rand(20)
	
	offsetx#=Sin(angle) * dist
	offsety#=Cos(angle) * dist

	
	lm1#=Rnd(10)
	lmf1#=(lm1#*.01)+.9


	fadeout#=.91
	speedfact#=0.05
	
	
	Angle= Rnd(360);
		
	Xx#=Cos(Angle) * dist
	Yy#=Sin(Angle) * dist
		
		;UseParticle(X+dx,Y+dy,dx,dy,.98,col,scatter/2)

	dx#=Xx*speedfact
	dy#=Yy*speedfact
	n=New3DSprite(X+Xx,Y+Yy,(scatter*3),(scatter*3),g_lighttexture(col),0,0,1,Rnd(360),0)
	Set3DSpriteVelocity(n,dx,dy)
	Set3DSpriteFade(n,fadeout#)
	Set3DSpriteBlowup(n,1.08)

		
	;Next	
	;ParticleSparcs(X+offsetx,Y+offsety,speed+speedoffset,scatter,1,fadeout#,0)
Next 


End Function

Function SmallExplosion(X,Y,dx#,dy#)



speed=10
nofparticles=40
;n=New3DSprite(x,y,(100*fadeout),(100*fadeout),g_scattertexture(0),0,0,1,Rnd(360),0)

;Set3DSpriteVelocity(n,dx,dy)
;Set3DSpriteFade(n,.96)
;Set3DSpriteBlowup(n,1.01)

;Blink(X,Y,0,0)

For t2=1 To 10

	t=t2/2
	range=t2*2
	
	dist=t2
	scatter=Rand(5,8)

	angle=Rand(360)
	dist=Rand(20)
	
	offsetx#=Sin(angle) * dist
	offsety#=Cos(angle) * dist

	;speedoffset=Rand(20)
	
	;speed2#=.0
	;For i = 1 To nofparticles
;
	lm1#=Rnd(10)
	lmf1#=(lm1#*.01)+.9
	
	;speed2#= Rnd(t,t*3)
	;speed2=speed2 * .1 

	col=0
	fadeout#=.84
	speedfact#=0.05
	
	If(range>66) Then 
		col=2
		fadeout#=.92
		speedfact#=0.1
	ElseIf(range>33) Then 
		col=1
		fadeout#=.89
		
	EndIf 
	;col=2
	
	Angle= Rnd(360);
		
	Xx#=Cos(Angle) * dist
	Yy#=Sin(Angle) * dist
		
		;UseParticle(X+dx,Y+dy,dx,dy,.98,col,scatter/2)

	dx#=Xx*speedfact
	dy#=Yy*speedfact
	n=New3DSprite(X+Xx,Y+Yy,(scatter*3),(scatter*3),g_lighttexture(col),0,0,1,Rnd(360),0)
	Set3DSpriteVelocity(n,dx,dy)
	Set3DSpriteFade(n,fadeout#)
	Set3DSpriteBlowup(n,1.01)

		
	;Next	
	;ParticleSparcs(X+offsetx,Y+offsety,speed+speedoffset,scatter,1,fadeout#,0)
Next 


End Function


Function SmallExplosion2(X,Y,dx#,dy#)



speed=10
nofparticles=40
;n=New3DSprite(x,y,(100*fadeout),(100*fadeout),g_scattertexture(0),0,0,1,Rnd(360),0)

;Set3DSpriteVelocity(n,dx,dy)
;Set3DSpriteFade(n,.96)
;Set3DSpriteBlowup(n,1.01)

;Blink(X,Y,0,0)

For t2=1 To 200

	t=t2/2
	scatter=Rand(2,20)

	angle=Rand(360)
	dist=Rand(100)
	
	offsetx#=Sin(angle) * dist
	offsety#=Cos(angle) * dist

	;speedoffset=Rand(20)
	
	;speed2#=.0
	;For i = 1 To nofparticles
;
	lm1#=Rnd(10)
	lmf1#=(lm1#*.01)+.9
	
	;speed2#= Rnd(t,t*3)
	;speed2=speed2 * .1 

	col=0
	fadeout#=.89
	
	If(t>66) Then 
		col=2
		fadeout#=.95
	ElseIf(t>33) Then 
		col=1
		fadeout#=.93

	EndIf 
	;col=2
	
	Angle= Rnd(360);
		
	Xx#=Cos(Angle) * t
	Yy#=Sin(Angle) * t
		
		;UseParticle(X+dx,Y+dy,dx,dy,.98,col,scatter/2)

	dx=Xx/100
	dy=Yy/150
	n=New3DSprite(X+Xx,Y+Yy,(scatter*3),(scatter*3),g_lighttexture(col),0,0,1,Rnd(360),0)
	Set3DSpriteVelocity(n,dx,dy)
	Set3DSpriteFade(n,fadeout#)
	Set3DSpriteBlowup(n,1.01)

		
	;Next	
	;ParticleSparcs(X+offsetx,Y+offsety,speed+speedoffset,scatter,1,fadeout#,0)
Next 


End Function


Function BigExplosion(X,Y,dx#,dy#, catastrophe)

FreeSome3DSprites(100)

speed=10
nofparticles=40
;n=New3DSprite(x,y,(100*fadeout),(100*fadeout),g_scattertexture(0),0,0,1,Rnd(360),0)

;Set3DSpriteVelocity(n,dx,dy)
;Set3DSpriteFade(n,.96)
;Set3DSpriteBlowup(n,1.01)

If(catastrophe=1) Then BlinkBoom(X,Y,0,0)

For t2=1 To 200

	t=t2/2
	scatter=Rand(20,30)

	angle=Rand(360)
	dist=Rand(100)
	
	offsetx#=Sin(angle) * dist
	offsety#=Cos(angle) * dist

	;speedoffset=Rand(20)
	
	;speed2#=.0
	;For i = 1 To nofparticles
;
	lm1#=Rnd(10)
	lmf1#=(lm1#*.01)+.9
	
	;speed2#= Rnd(t,t*3)
	;speed2=speed2 * .1 

	col=0
	fadeout#=.89
	
	If(t>66) Then 
		col=2
		fadeout#=.95
	ElseIf(t>33) Then 
		col=1
		fadeout#=.93

	EndIf 
	;col=2
	
	Angle= Rnd(360);
		
	Xx#=Cos(Angle) * t
	Yy#=Sin(Angle) * t
		
		;UseParticle(X+dx,Y+dy,dx,dy,.98,col,scatter/2)

	dx=Xx/50
	dy=Yy/75
	n=New3DSprite(X+Xx,Y+Yy,(scatter*3),(scatter*3),g_lighttexture(col),0,0,1,Rnd(360),0)
	Set3DSpriteVelocity(n,dx,dy)
	Set3DSpriteFade(n,fadeout#)
	Set3DSpriteBlowup(n,1.01)

		
	;Next	
	;ParticleSparcs(X+offsetx,Y+offsety,speed+speedoffset,scatter,1,fadeout#,0)
Next 


End Function


Function PlayerExplosion(X,Y) 

	
BigExplosion(X,Y,0,0,1)
	
End Function

Function ParticleSparcs(X,Y,speed,scatter,nofparticles,fadeout#,col)
	
		speed2#=.0
		For i = 1 To nofparticles
;
		lm1#=Rnd(10)
		lmf1#=(lm1#*.01)+.9
		speed2#= Rnd(speed,speed*3)
		speed2=speed2 * .1 

		Angle#= Rnd(360);
		
		
		dx#=Cos(Angle) * speed2
		dy#=Sin(Angle) * speed2
		
		;UseParticle(X+dx,Y+dy,dx,dy,.98,col,scatter/2)

		n=New3DSprite(X+dx,Y+dy,(scatter*3),(scatter*3),g_lighttexture(col),0,0,1,Rnd(360),0)
		Set3DSpriteVelocity(n,dx#,dy#)
		Set3DSpriteFade(n,fadeout#)
		Set3DSpriteBlowup(n,1.01)

		
	Next	
End Function

Function Blink(X,Y,dx#,dy#)
	
		n=New3DSprite(X+dx,Y+dy,30,30,g_blinktexture(2),0,0,1,0,0)
		Set3DSpriteVelocity(n,dx#,dy#)
		Set3DSpriteFade(n,.987)
		Set3DSpriteBlowup(n,1.001)
		Set3DSpriteRotate(n,1)
		Set3DSpriteBlink(n,1)
		
End Function

Function BlinkBoom(X,Y,dx#,dy#)
	
		n=New3DSprite(X+dx,Y+dy,150,150,g_blinktexture(0),0,0,1,0,0)
		Set3DSpriteVelocity(n,dx#,dy#)
		Set3DSpriteFade(n,.987)
		Set3DSpriteBlowup(n,1.101)
		Set3DSpriteRotate(n,1)
		
		;Set3DSpriteBlink(n,1)
		
End Function

Function BlurryStars3D(X,Y,dx#,dy#)
	
		f#=1.1
		n=New3DSprite(X+dx,Y+dy,2,2,g_blinktexture(2),0,0,1,0,0)
		Set3DSpriteVelocity(n,dx#,dy#)
		Set3DSpriteFade(n,.987)
		Set3DSpriteBlowup(n,1.08)
		Set3DSpriteRotate(n,46)
		Set3DSpriteVelocityIncrease(n,f)
		;Set3DSpriteBlink(n,1)
		
End Function


Function BlinkFast(X,Y,dx#,dy#)
	
	
		n=New3DSprite(X+dx,Y+dy,30,30,g_blinktexture(1),0,0,1,0,0)
		Set3DSpriteVelocity(n,dx#,dy#)
		Set3DSpriteFade(n,.89)
		Set3DSpriteBlowup(n,1.01)
		Set3DSpriteRotate(n,46)
		
End Function


Function ParticleThruster(X,Y,dx,dy,force,longlivety#,col)
	n=New3DSprite(x,y,(50*longlivety),(50*longlivety),g_lighttexture(0),0,0,1,Rnd(360),0)
	Set3DSpriteVelocity(n,dx,dy)
	Set3DSpriteFade(n,.85)
	Set3DSpriteBlowup(n,.99)
	Set3DSpriteRotate(n,Rand(10,15))


	
;	For i = 1 To force
;		myx=(X+Rnd(10))-5
;		myy=(Y+Rnd(10))-5
;		UseParticle(myx,myy,dx/2,dy/2,longlivety,col,force)
;		n=New3DSprite(myx,myy,(50*longlivety),(50*longlivety),g_lighttexture(col),0,0,1,Rnd(360))
;		Set3DSpriteVelocity(n,dx/2,dy/2)		
;	Next	
End Function

Function ParticleThrusterSmall(X,Y,dx,dy,force,longlivety#,col)

	;DebugLog "Function ParticleThrusterSmall"
	
	n=New3DSprite(x,y,(50*longlivety),(50*longlivety),g_blinktexture(1),0,0,1,Rnd(360),0)
	Set3DSpriteVelocity(n,dx,dy)
	Set3DSpriteFade(n,.85)
	Set3DSpriteBlowup(n,.99)
	Set3DSpriteRotate(n,Rand(10,15))


	
;	For i = 1 To force
;		myx=(X+Rnd(10))-5
;		myy=(Y+Rnd(10))-5
;		UseParticle(myx,myy,dx/2,dy/2,longlivety,col,force)
;		n=New3DSprite(myx,myy,(50*longlivety),(50*longlivety),g_lighttexture(col),0,0,1,Rnd(360))
;		Set3DSpriteVelocity(n,dx/2,dy/2)		
;	Next	
End Function

Function MakeSpacyBackGround(Q,bw,bh,rf#,gf#,bf#)

;;	Dim sintable(360)
;	Dim costable(360)

	mousewaiter=0
	interlace=0
	
	rsizeangle#=0
	roffsetangle#=0
	gsizeangle#=0
	goffsetangle#=0
	bsizeangle#=0
	boffsetangle#=0

	;angle0step#=.3
	;angle1step#=.1
	;angle2step#=.15
	;angle3step#=.07
	;angle4step#=.12
	;angle5step#=.08
		

	q=Q


		For k=0 To 	bw Step 5

			
			For l=0  To bh Step 5
		

				i#=k
				j#=l
		
				r#=128
				g#=128
				b#=128
				
				r=Cos((j/8)+i+q)+Cos(j+(i/1)+q)+Sin(-(j/2)+q)+Cos((i/3)+q)+Sin(-(i/4)+q)+Cos(j/5+q)
				r=(r*64)+128
				g=Sin(i)
				g=(g*64)+128
				b=Sin((j/8)+i+q)+Sin(-j+(i/4)+q)+Sin((j/2)+q)+Cos((i/3)+q)+Sin(-(i/4)+q)+Cos((j/5)+q)
				b=(b*64)+128
				


				If(r#>255) r#=255
				If(g#>255) g#=255
				If(b#>255) b#=255

				If(r#<0) r#=0
				If(g#<0) g#=0
				If(b#<0) b#=0

				r=r*rf#
				g=g*gf#
				b=b*bf#
								
				Color(r#,g#,b#)
				Rect i,j, i+5, j+5,1 
			Next
			
		Next
			

End Function