;todo
;
;init moves in a function
;locked/automove in on movestype var
;
;

;Const MObjmove_simple=0

Const MObjmove_debug=1


Const al_bug   =0
Const al_rship1 =1
Const al_ship1 =2

Const al_rock1_25  =10
Const al_rock1_50  =11
Const al_rock1_100 =12

Const ab_hideoffscreen = 1
Const ab_deleteoffscreen = 2
Const ab_screenmirror  = 3


Global g_id=0


Type moves
	Field movescount
	Field dx#[g_maxmoves]
	Field dy#[g_maxmoves]
	Field depth[g_maxmoves]
	Field count[g_maxmoves]
	Field cycle
	Field event[g_maxmoves]
	;Field id	
End Type


;Type moevent
;	Field time#
;	Field x#
;	Field y#	
;	Field dx#
;	Field dy#	
;	Field gfxsequence.imagesequence
;	Field imagerc#
;	Field strength#
;End Type


Type MObj
	Field x#,y#
	Field dx#,dy#
	Field w,h
	Field visible
	Field id
	;Field atype
	Field strength#
	Field gfxsequence.imagesequence
	Field imagecount
	Field imageix#
	Field imagerc#
	Field image
	Field moves.moves
	Field moveix
	Field movestepcnt
	Field extref
	Field extfloat#
	
	Field objtype
	Field objbehaviour
	
	Field countme ;count active objects, to see if we can go to the next level or not

	Field automove
	Field locked_tightness#
	Field locked
	Field lockedto.MObj	
	Field lx#,ly#
	Field hidden
	Field debug 
	Field debug2
	
	Field depth 
	;Field priority
	
	Field drawbuffer
	
	Field minx
	Field maxx
	Field miny
	Field maxy
	
	Field outofviewinactive
	
End Type


Const eventbuffer_cnt_max=1000
Dim eventbuffer(eventbuffer_cnt_max)
Dim eventbufferobj.MObj(eventbuffer_cnt_max)
Global eventbuffer_cnt=0
Global eventobject.MObj

Global myeventcnt=0

Const mobj_stacksize=10000
Dim mobj_stack.MObj(mobj_stacksize)
Global mobj_stackcounter=0
Global mobj_redostack=1

Function mobj_addtostack(Mobj.MObj)

	;DebugLog "i>"+Mobj\depth

	i=0
	While i<mobj_stackcounter
		If(mobj_stack(i)\depth>Mobj\depth) Then
			mobj_insertstack(i,Mobj)
			Return 
		EndIf 	
		i=i+1
	Wend 

	mobj_stack(mobj_stackcounter)=Mobj
	mobj_stackcounter=mobj_stackcounter+1
	
End Function

Function mobj_insertstack(index,Mobj.MObj)

	;DebugLog "insert"
	
	i=mobj_stackcounter
	While i>index
		mobj_stack(i)=mobj_stack(i-1)	
		i=i-1
	Wend 

	mobj_stack(index)=Mobj
	mobj_stackcounter=mobj_stackcounter+1
	
End Function

Function PutMobjEvent(code,Mobj.MObj)
	myeventcnt=myeventcnt+1
	
	If(eventbuffer_cnt < eventbuffer_cnt_max) Then
	
								
				eventbuffer(eventbuffer_cnt)=code
				eventbufferobj(eventbuffer_cnt)=Mobj

				eventbuffer_cnt=eventbuffer_cnt+1
				
				Mobj\debug=1
	EndIf 
End Function

Function GetMobjEvent()
	If(eventbuffer_cnt >0) Then
				eventbuffer_cnt=eventbuffer_cnt-1
				eventobject=eventbufferobj(eventbuffer_cnt)
				eventobject\debug=2
				Return eventbuffer(eventbuffer_cnt)
				
	Else
		Return 0
	EndIf 
End Function




Function AddMObj()

	mobj_redostack=1
	s.MObj = New MObj
	
	s\x = 0
	s\y = 0
	s\dx = 0
	s\dy = 0

	;s\imageType=0
	s\visible=False
	s\hidden=False
	s\countme=1
	swap=0	
	s\movestepcnt=0
	s\outofviewinactive=0
	
	s\moves = New moves
	
	s\moveix=0		
	s\dx=0
	s\dy=0
	
	s\debug=0
	
	s\moves\movescount=0
	For t=0 To g_maxmoves
		s\moves\dx[t]=0
		s\moves\dy[t]=0
		s\moves\count[t]=0
		s\moves\event[t]=0
	Next 
	
End Function

;Function AddMObjEvent()
;	s.moevent= New moevent
;	s\time#=-1
;	
;End Function




Function MObjSetImageSequence(t.MObj,sequence.imagesequence,imagerc#,startimage)

		t\gfxsequence=sequence
		t\imagecount=t\gfxsequence\images
		t\imagerc=imagerc
		t\image=t\gfxsequence\image[start]
		t\imageix=start
		t\w=ImageWidth(t\image)/2
		t\h=ImageHeight(t\image)/2
		t\objtype=objtype
		;t\image=t\gfxsequence\image[0]

		
		If(objtype>Type_friendlies) 
			t\countme=0
		Else 
			t\countme=1
		EndIf 
	
End Function

Function MObjSetImageSequenceSpeed(t.MObj,imagercfactor#)

		t\imagerc=t\imagerc * imagercfactor
End Function

Function MObjFastForward(s.MObj,steps)
	t=0
	While(t<steps)
		MoveMObjNoAnim(s)
		t=t+1
	Wend
End Function 


Function MoveMObjNoAnim(s.MObj)
	
	If s\visible = True  And s\automove=True
		
	If(s\movestepcnt >= s\moves\count[s\moveix]) Then
						s\moveix=s\moveix+1
						s\movestepcnt=0
						
						
						If(s\moveix >=s\moves\movescount) 
							If s\moves\cycle=1 Then
								s\moveix=0 
								s\dx=s\moves\dx[s\moveix]
								s\dy=s\moves\dy[s\moveix]		
								
								newdepth=s\moves\depth[s\moveix]
								;If s\locked=1 DebugLog "D:"+newdepth 
								If(newdepth<>s\depth) Then 
									mobj_redostack=1
									
								EndIf 
								
								s\depth=newdepth
	
							Else 
								s\visible=False
								mobj_redostack=1

								;Delete s\moves
	
							EndIf 
						Else

						
							If(s\moves\event[s\moveix]>0) Then
									
									PutMobjEvent(s\moves\event[s\moveix],s)
									
							EndIf 						
							
							newdepth=s\moves\depth[s\moveix]
							;If s\locked=1 DebugLog "D:"+newdepth
							
							If(newdepth<>s\depth) Then 
									mobj_redostack=1		 
							EndIf 
							s\depth=newdepth
							
							s\dx=s\moves\dx[s\moveix]
							s\dy=s\moves\dy[s\moveix]
						EndIf 	
		
	
				EndIf 
					
				
				If(s\locked=False)	Then 

					s\x=s\x+s\dx
					s\y=s\y+s\dy
					s\movestepcnt=s\movestepcnt+1			
							

				Else ;locked coords
				
					s\lx=s\lx+s\dx
					s\ly=s\ly+s\dy
					s\movestepcnt=s\movestepcnt+1	
					
					s\x=s\lx+s\lockedto\x
					s\y=s\ly+s\lockedto\y
				EndIf 
	

			
		Else If s\visible = True  And s\automove=False And s\locked=True 


;				s\dx=((s\ly+s\lockedto\x)-s\x);
;				s\dy=((s\ly+s\lockedto\y)-s\y)

				s\dx=((s\lockedto\x)-s\x)
				s\dy=((s\lockedto\y)-s\y)

				s\x=s\x + s\dx * s\locked_tightness
				s\y=s\y + s\dy * s\locked_tightness
				
				
	
		EndIf	
		
End Function 


Function MoveMObjs()
	
	For s.MObj = Each MObj
		If(s\visible=True) Then
				
			s\imageix=s\imageix+s\imagerc
			While(s\imageix<0) 
				s\imageix=s\imageix+s\imagecount
			Wend 
			
			While (s\imageix>=s\imagecount) 
				s\imageix=s\imageix-s\imagecount
			Wend

		EndIf 
	Next 	
	
	For s.MObj = Each MObj
		
		MoveMObjNoAnim(s)
	
		If(s\outofviewinactive=1) Then
			If(s\x<s\minx Or s\y<s\miny Or s\x > s\maxx Or s\y > s\maxy) Then 
				DeactivateMobj(s)
			EndIf 
		EndIf 
		
	Next
	
End Function




Function ShowMObjs(offsetx,offsety,buffer)

	Count=0	
	FCount=0
	Color 255,255,255

	

	If(mobj_redostack=1) Then 
		mobj_stackcounter=0
		For s.MObj = Each MObj
			
			If s\visible=True And s\hidden=False And s\drawbuffer=buffer Then 
			
				mobj_addtostack(s)
			EndIf 
	
		Next 
		mobj_redostack=0
	EndIf 
	
	If MObjmove_debug>0 Then
		Color 255,255,255
		Text 0,0,"stack count = "+mobj_stackcounter
	EndIf 

	For t=0 To mobj_stacksize
		If t >=mobj_stackcounter Then Exit 
		
			s.Mobj = mobj_stack(t)
			
			Count=Count+1
		
			image=Floor(s\imageix)
			DrawImage(s\gfxsequence\image[image],s\x+offsetx,s\y+offsety)
			;Color 255,255,255
			;Text s\x+offsetx,s\y+offsety,s\x+","+s\y
			;If(s\locked) Then
			;	Text s\x+offsetx,s\y+offsety+20,"D:"+s\depth
			;
			;EndIf 
			
			;	Color 255,255,266
			;	Text s\x+offsetx,s\y+offsety , s\moveix + " . "+ s\debug
			;FCount=FCount+1
				
			swap=1-swap
			
			
	Next 
	
	;For s.MObj = Each MObj
;
;
;	Next 
	
End Function
	




Function ActiveMObjs()

	mobj_redostack=1			
	Count=0	
	For s.MObj = Each MObj
		 
		If s\visible=True And s\countme=1 And s\hidden=False
			Count=Count+1
			
		EndIf
	Next 
	Return Count
	
End Function


Function InitMObj(s.MObj,x,y,buffer,thetype)

			s\visible=True
			s\x=x
			s\y=y
			s\lx=x
			s\ly=y
				
			s\id=id

			s\moveix=0	
			s\movestepcnt=0	
			s\outofviewinactive=0
			
			s\dx=0
			s\dy=0
			s\moves\cycle=0
			
			s\moves\movescount=0
			s\moves\dx[0]=0
			s\moves\dy[0]=0
			s\moves\count[0]=0
			s\moves\event[0]=0

			s\objbehaviour=ab_hideoffscreen
			s\locked=0
		
			s\locked_tightness=1
			
			s\automove=1
			s\drawbuffer=buffer
			
			s\debug=0
						
			s\depth=0
			
			s\objtype=thetype
		
			For t=0 To	g_maxmoves
				s\moves\dx#[t]=0
				s\moves\dy#[t]=0
				s\moves\depth[t]=0
				s\moves\count[t]=0
				s\moves\event[t]=0
			Next 
				
End Function

Function ActivateMObj.MObj(x#,y#,buffer, thetype)

	mobj_redostack=1			
	Count=0	
	For s.MObj = Each MObj
		Count=Count+1
		If s\visible=False
		
			InitMObj(s,x,y,buffer,thetype)
			
			
			Return s			
			
		EndIf
	Next 
	Return Null 
	
End Function


Function FindActiveChildMObj.MObj(parent.MObj)

	Count=0
	For s.MObj = Each MObj

		If s\visible=True And s\locked=True And s\lockedto=parent
					
			Return s			
			
		EndIf
	Next 
	Return Null 
	
End Function 

Function ActivateMObjPrio.MObj(x#,y#,buffer,override, thetype)

	mobj_redostack=1			
	Count=0	
	For s.MObj = Each MObj
		Count=Count+1
		If s\visible=False Or s\objtype=override
		
			InitMObj(s,x,y,buffer,thetype)
					
			Return s			
			
		EndIf
	Next 
	Return Null 
	
End Function


Function DeactivateMObj(o.MObj)

	mobj_redostack=1	
	o\visible=False
	o\objtype=0 
	
	For s.MObj= Each MObj
	
		If(s\visible = True And s\locked=1 And s\lockedto=o)	Then 
		
			DeactivateMObj(s)
		
		EndIf 
		
	Next 
	
	
End Function

	
Function DeactivateMObjs()
			
	For s.MObj= Each MObj
	
		DeactivateMObj(s)
		
		
	Next 
	
	eventbuffer_cnt=0

End Function


	
;Function DeactivateMObjEvents()
;			
;	For s.moevent= Each moevent
;		s\time=-1
;	Next 
;
;End Function

Function CleanupMObjs()

	Delete Each MObj	
	Delete Each moves
	eventbuffer_cnt=0
	mobj_redostack=1	
	
End Function