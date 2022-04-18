;Lodgia - D.Murray - 2005
;Blitzbasic demo
;Free to use, modify in any way
;Disclaimer, use at you own risk
;Please mention me in your credits when you use this library

Type df_copperstruct
	Field width,height
	Field x,y
	Field drawbuffer
	Field sizefact
	Field brightfactr#
	Field brightfactg#
	Field brightfactb#		
	Field as#,as2#,as3#
End Type


Function InitCopper.df_copperstruct(x,y,w,h,buf,brightfactr#,brightfactg#,brightfactb#,sizefact)
	
	current.df_copperstruct= New df_copperstruct
	current\width=w
	current\height=h
	current\x=x
	current\y=y
	current\drawbuffer=buf
	current\brightfactr=brightfactr
	current\brightfactg=brightfactg
	current\brightfactb=brightfactb
	
	current\sizefact=sizefact

	Return current	
End Function 


;-----------------------------------
Function DrawCopper(this.df_copperstruct)
;w,h,buf,sizefact,brightfact#)
SetBuffer this\drawbuffer
 
;WaitKey
For t=1 To this\height Step 1
		
			a=t+this\as
			a2=t+this\as2
			a3=t+this\as3
			
			colv=(Sin(a*10) * 64) + 64
			colv2=(Sin((360 - a2)*2) * 64) + 64
			colv3=(Sin((360 - a3)*3) * 64) + 64
			
			rf#=t:rf=rf/(this\height)
			bf#=(this\height-t):bf=bf/(this\height)
			gf#=(Abs ((this\height/2)-t)) : gf#=1-(gf# / (this\height /2))


			r=gf*colv*this\brightfactr
			g=gf*colv2*this\brightfactg
			b=bf*colv3*this\brightfactb

			If(r<0) r=0
			If(b<0) b=0
			If(g<0) g=0
			If(r>255) r=255
			If(b>255) b=255
			If(g>255) g=255
			
			
			Color r,g,b
			Rect this\x,this\y+(t/this\sizefact),this\width/this\sizefact,1 ,1
			;Rect this\x,this\y,this\width,1 ,1

				
	Next 
	this\as=this\as+.5 Mod 360
	this\as2=this\as2-.7 Mod 360
	this\as3=this\as3-1 Mod 360

End Function